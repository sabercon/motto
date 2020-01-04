package cn.sabercon.motto.log.service;

import cn.hutool.crypto.SecureUtil;
import cn.sabercon.motto.common.util.AssertUtils;
import cn.sabercon.motto.common.util.EntityUtils;
import cn.sabercon.motto.log.dao.UserBasicRepository;
import cn.sabercon.motto.log.dao.UserDetailRepository;
import cn.sabercon.motto.log.dto.UserReq;
import cn.sabercon.motto.log.dto.UserDto;
import cn.sabercon.motto.log.entity.UserBasic;
import cn.sabercon.motto.log.entity.UserDetail;
import cn.sabercon.motto.log.util.LoginUtils;
import cn.sabercon.motto.log.util.PatternUtils;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

import static cn.sabercon.motto.common.enums.ErrorCode.*;
import static cn.sabercon.motto.log.component.SmsHelper.*;
import static cn.sabercon.motto.log.util.JwtTokenUtils.*;

/**
 * @author ywk
 * @date 2019-10-15
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserBasicRepository repository;
    @Autowired
    private UserDetailRepository detailRepository;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Value("${motto.user.defaultAvatar}")
    private String defaultAvatar;

    /**
     * 表示手机是否处于可换绑状态的redis键前缀
     */
    private static final String PHONE_BIND_PREFIX = "phone:bind:";

    public UserDto get() {
        return getUser();
    }

    public void update(UserDto dto) {
        UserDetail userDetail = detailRepository.findByUserId(LoginUtils.getId());
        EntityUtils.copyIgnoreNotCover(dto, userDetail);
        // 更新缓存
        UserDto user = getUser();
        EntityUtils.copyIgnoreNotCover(dto, user);
        refreshUserInRedis(user);
    }

    public void register(UserReq userReq) {
        // 校验参数
        PatternUtils.checkUsername(userReq.getUsername());
        PatternUtils.checkPassword(userReq.getPassword());
        PatternUtils.checkPhone(userReq.getPhone());
        PatternUtils.checkSmsCode(userReq.getSmsCode());
        // 校验用户名和电话号唯一性，验证码正确性
        matchSmsCode(userReq.getPhone(), userReq.getSmsCode(), SMS_REGISTER_PREFIX);
        AssertUtils.isNull(repository.findByUsername(userReq.getUsername()), USERNAME_EXISTS);
        AssertUtils.isNull(repository.findByPhone(userReq.getPhone()), PHONE_EXISTS);
        // 新增用户
        UserBasic user = new UserBasic();
        BeanUtils.copyProperties(userReq, user);
        user.setPassword(SecureUtil.md5(userReq.getPassword()));
        user = repository.save(user);
        // 新增用户详细信息，默认昵称为用户名
        UserDetail userDetail = new UserDetail();
        userDetail.setUserId(user.getId());
        userDetail.setNickname(userReq.getUsername());
        userDetail.setAvatar(defaultAvatar);
        detailRepository.save(userDetail);
    }

    public String login(UserReq userReq) {
        UserBasic user;
        if (StringUtils.isEmpty(userReq.getUsername())) {
            // 验证手机号和验证码
            PatternUtils.checkPhone(userReq.getPhone());
            PatternUtils.checkSmsCode(userReq.getSmsCode());
            user = repository.findByPhone(userReq.getPhone());
            AssertUtils.isNotNull(user, USER_NOT_EXISTS);
            matchSmsCode(userReq.getPhone(), userReq.getSmsCode(), SMS_LOGIN_PREFIX);
        } else {
            // 验证用户名和密码
            PatternUtils.checkUsername(userReq.getUsername());
            PatternUtils.checkPassword(userReq.getPassword());
            user = repository.findByUsername(userReq.getUsername());
            AssertUtils.isNotNull(user, USER_NOT_EXISTS);
            AssertUtils.isTrue(user.getPassword().equals(SecureUtil.md5(userReq.getPassword())), PASSWORD_WRONG);
        }
        // 缓存用户信息到redis
        UserDetail detail = detailRepository.findByUserId(user.getId());
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        BeanUtils.copyProperties(detail, userDto);
        String userStr = JSON.toJSONString(userDto);
        redisTemplate.opsForValue().set(JWT_USER_PREFIX + user.getId(), userStr, JWT_USER_EXPIRATION, TimeUnit.SECONDS);

        return generateTokenById(user.getId());
    }

    public void reset(UserReq userReq) {
        // 校验参数
        PatternUtils.checkPassword(userReq.getPassword());
        PatternUtils.checkPhone(userReq.getPhone());
        PatternUtils.checkSmsCode(userReq.getSmsCode());
        matchSmsCode(userReq.getPhone(), userReq.getSmsCode(), SMS_RESET_PREFIX);
        UserBasic userBasic = repository.findByPhone(userReq.getPhone());
        AssertUtils.isNotNull(userBasic, USER_NOT_EXISTS);
        // 更新密码
        userBasic.setPassword(SecureUtil.md5(userReq.getPassword()));
    }

    public void logout() {
        redisTemplate.delete(JWT_USER_PREFIX + LoginUtils.getId());
    }

    public void updatePassword(String password, String smsCode) {
        // 校验参数
        PatternUtils.checkPassword(password);
        PatternUtils.checkSmsCode(smsCode);
        UserDto user = getUser();
        matchSmsCode(user.getPhone(), smsCode, SMS_UPDATE_PREFIX);
        // 更新密码
        UserBasic userBasic = repository.findByPhone(user.getPhone());
        userBasic.setPassword(SecureUtil.md5(password));
        // controller中退出登录
    }

    public void unbindPhone(String smsCode) {
        // 校验参数
        PatternUtils.checkSmsCode(smsCode);
        UserDto user = getUser();
        matchSmsCode(user.getPhone(), smsCode, SMS_UNBIND_PREFIX);
        // 在redis中存入一个信息表示账号为可换手机状态，时间为5分钟
        redisTemplate.opsForValue().set(PHONE_BIND_PREFIX + LoginUtils.getId(), "", 5, TimeUnit.MINUTES);
    }

    public void bindPhone(String phone, String smsCode) {
        // 校验参数
        PatternUtils.checkPhone(phone);
        PatternUtils.checkSmsCode(smsCode);
        matchSmsCode(phone, smsCode, SMS_BIND_PREFIX);
        AssertUtils.isNull(repository.findByPhone(phone), PHONE_EXISTS);
        // 判断用户是否是可换绑状态
        AssertUtils.isTrue(redisTemplate.delete(PHONE_BIND_PREFIX + LoginUtils.getId()), PHONE_BIND_WRONG);
        // 绑定新手机
        UserDto user = getUser();
        UserBasic userBasic = repository.findByPhone(user.getPhone());
        userBasic.setPhone(phone);
        // 更新缓存
        user.setPhone(phone);
        refreshUserInRedis(user);
    }

    /**
     * 验证短信验证码的正确性，不一致抛出异常，一致则删除redis缓存的验证码
     *
     * @param phone
     * @param smsCode
     * @param prefix  验证码保存到redis的键名前缀
     */
    private void matchSmsCode(String phone, String smsCode, String prefix) {
        AssertUtils.isTrue(smsCode.equalsIgnoreCase(redisTemplate.opsForValue().get(prefix + phone)), SMS_CODE_WRONG);
        // delete smsCode when success
        redisTemplate.delete(prefix + phone);
    }

    /**
     * 根据request域中取得的用户id找到redis中缓存的信息
     *
     * @return redis中缓存的用户信息
     */
    private UserDto getUser() {
        String userStr = redisTemplate.opsForValue().get(JWT_USER_PREFIX + LoginUtils.getId());
        return JSON.parseObject(userStr, UserDto.class);
    }

    /**
     * 刷新redis里的用户信息
     *
     * @param dto 新用户信息
     */
    private void refreshUserInRedis(UserDto dto) {
        String userStr = JSON.toJSONString(dto);
        redisTemplate.opsForValue().set(JWT_USER_PREFIX + LoginUtils.getId(), userStr, JWT_USER_EXPIRATION, TimeUnit.SECONDS);
    }

}
