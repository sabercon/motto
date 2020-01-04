package cn.sabercon.motto.log.service;

import cn.hutool.crypto.SecureUtil;
import cn.sabercon.motto.common.util.AssertUtils;
import cn.sabercon.motto.log.component.SmsHelper;
import cn.sabercon.motto.log.dao.UserBasicRepository;
import cn.sabercon.motto.log.dao.UserDetailRepository;
import cn.sabercon.motto.log.dto.UserReq;
import cn.sabercon.motto.log.entity.UserBasic;
import cn.sabercon.motto.log.entity.UserDetail;
import cn.sabercon.motto.log.util.LoginUtils;
import cn.sabercon.motto.log.util.PatternUtils;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.ImmutableMap;
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
public class UserBasicService {

    @Autowired
    private UserBasicRepository repository;
    @Autowired
    private UserDetailRepository userDetailRepository;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private SmsHelper smsHelper;
    @Value("${motto.user.defaultAvatar}")
    private String defaultAvatar;

    /**
     * 表示手机是否处于可换绑状态的redis键前缀
     */
    private static final String PHONE_BIND_PREFIX = "phone:bind:";

    /**
     * 短信验证码保存前缀的映射
     */
    private static ImmutableMap<Integer, String> SMS_PREFIX_MAP;

    static {
        // 初始化构建映射
        ImmutableMap.Builder<Integer, String> builder = ImmutableMap.builder();
        SMS_PREFIX_MAP = builder
                .put(1, SMS_REGISTER_PREFIX)
                .put(2, SMS_LOGIN_PREFIX)
                .put(3, SMS_RESET_PREFIX)
                .put(4, SMS_UPDATE_PREFIX)
                .put(5, SMS_UNBIND_PREFIX)
                .put(6, SMS_BIND_PREFIX)
                .build();
    }

    public UserReq get() {
        UserReq userReq = new UserReq();
        BeanUtils.copyProperties(getUser(), userReq);
        userReq.setPassword(null);
        return userReq;
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
        userDetailRepository.save(userDetail);
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
        refreshUserInRedis(user);
        return generateTokenById(user.getId());
    }

    public void reset(UserReq userReq) {
        // 校验参数
        PatternUtils.checkPassword(userReq.getPassword());
        PatternUtils.checkPhone(userReq.getPhone());
        PatternUtils.checkSmsCode(userReq.getSmsCode());
        UserBasic user = repository.findByPhone(userReq.getPhone());
        AssertUtils.isNotNull(user, USER_NOT_EXISTS);
        matchSmsCode(userReq.getPhone(), userReq.getSmsCode(), SMS_RESET_PREFIX);
        // 更新密码
        user.setPassword(SecureUtil.md5(userReq.getPassword()));
        repository.save(user);
    }

    public void logout() {
        redisTemplate.delete(JWT_USER_PREFIX + LoginUtils.getId());
    }

    public void sendSmsCode(Integer status, String phone) {
        PatternUtils.checkPhone(phone);
        // 根据status得到验证码保存到redis时的前缀
        String prefix = SMS_PREFIX_MAP.get(status);
        AssertUtils.isNotEmpty(prefix);
        smsHelper.sendCode(phone, prefix);
    }

    public void updatePassword(String password, String smsCode) {
        // 校验参数
        PatternUtils.checkPassword(password);
        PatternUtils.checkSmsCode(smsCode);
        UserBasic user = getUser();
        matchSmsCode(user.getPhone(), smsCode, SMS_UPDATE_PREFIX);
        // 更新密码
        user.setPassword(SecureUtil.md5(password));
        repository.save(user);
        // controller中退出登录
    }

    public void unbindPhone(String smsCode) {
        // 校验参数
        PatternUtils.checkSmsCode(smsCode);
        UserBasic user = getUser();
        matchSmsCode(user.getPhone(), smsCode, SMS_UNBIND_PREFIX);
        // 在redis中存入一个信息表示账号为可换手机状态，时间为5分钟
        redisTemplate.opsForValue().set(PHONE_BIND_PREFIX + user.getId(), "", 5, TimeUnit.MINUTES);
    }

    public void bindPhone(String phone, String smsCode) {
        // 校验参数
        PatternUtils.checkPhone(phone);
        PatternUtils.checkSmsCode(smsCode);
        matchSmsCode(phone, smsCode, SMS_BIND_PREFIX);
        AssertUtils.isNull(repository.findByPhone(phone), PHONE_EXISTS);
        // 判断用户是否是可换绑状态
        UserBasic user = getUser();
        AssertUtils.isTrue(redisTemplate.delete(PHONE_BIND_PREFIX + user.getId()), PHONE_BIND_WRONG);
        // 绑定新手机
        user.setPhone(phone);
        repository.save(user);
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
    private UserBasic getUser() {
        String userStr = redisTemplate.opsForValue().get(JWT_USER_PREFIX + LoginUtils.getId());
        return JSON.parseObject(userStr, UserBasic.class);
    }

    /**
     * 刷新或保存用户信息到redis
     *
     * @param user
     */
    private void refreshUserInRedis(UserBasic user) {
        String userStr = JSON.toJSONString(user);
        redisTemplate.opsForValue().set(JWT_USER_PREFIX + user.getId(), userStr, JWT_USER_EXPIRATION, TimeUnit.SECONDS);
    }

}
