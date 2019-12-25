package cn.sabercon.motto.log.component;

import cn.hutool.core.util.RandomUtil;
import cn.sabercon.motto.common.enums.ErrorCode;
import cn.sabercon.motto.common.exception.CommonException;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 阿里短信发送工具类
 *
 * @author ywk
 * @date 2019-10-15
 */
@Component
@Slf4j
public class SmsHelper {
    @Value("${aliyun.sms.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.sms.accessKeySecret}")
    private String accessKeySecret;
    @Value("${aliyun.sms.signName}")
    private String signName;
    @Value("${aliyun.sms.templateCode}")
    private String templateCode;
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 注册用验证码
     */
    public static final String SMS_REGISTER_PREFIX = "sms:register:";
    /**
     * 登录用验证码
     */
    public static final String SMS_LOGIN_PREFIX = "sms:login:";
    /**
     * 重置密码用验证码
     */
    public static final String SMS_RESET_PREFIX = "sms:reset:";
    /**
     * 修改密码用验证码
     */
    public static final String SMS_UPDATE_PREFIX = "sms:update:";
    /**
     * 解绑手机用验证码
     */
    public static final String SMS_UNBIND_PREFIX = "sms:unbind:";
    /**
     * 绑定手机用验证码
     */
    public static final String SMS_BIND_PREFIX = "sms:bind:";

    /**
     * 发送短信验证码
     *
     * @param phone  要发送的手机号码
     * @param prefix 存储到 redis中的键名前缀（键名为前缀加手机号码）
     */
    public void sendCode(String phone, String prefix) {
        // 生成六位数的随机字符串
        String code = RandomUtil.randomNumbers(6);

        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + code + "\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            log.info("result of smsCode sending request:{}", response.getData());
            if (!response.getData().contains("OK")) {
                throw new RuntimeException();
            }
        } catch (ClientException | RuntimeException e) {
            log.warn("error when sending smsCode, phone number:{}", phone);
            throw new CommonException(ErrorCode.SMS_SEND_ERROR);
        }

        // 保存验证码到redis中，保存时间五分钟
        redisTemplate.opsForValue().set(prefix + phone, code, 5, TimeUnit.MINUTES);
        log.info("success for sending smsCode, phone number:{}, code:{}", phone, code);
    }

}
