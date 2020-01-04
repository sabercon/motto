package cn.sabercon.motto.log.controller;

import cn.sabercon.motto.common.dto.Result;
import cn.sabercon.motto.common.util.AssertUtils;
import cn.sabercon.motto.log.component.SmsHelper;
import cn.sabercon.motto.log.util.PatternUtils;
import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static cn.sabercon.motto.log.component.SmsHelper.*;
import static cn.sabercon.motto.log.component.SmsHelper.SMS_BIND_PREFIX;

/**
 * @author ywk
 * @date 2020-01-04
 */
@CrossOrigin
@RestController
@Api("发送短信的接口")
@RequestMapping("sms")
public class SmsController {

    @Autowired
    private SmsHelper smsHelper;

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

    @ApiOperation("发送短信验证码")
    @ApiImplicitParam(name = "status", allowableValues = "range[1,6]",
            value = "验证码的发送类型：1-注册，2-登录，3-重置密码，4-修改密码，5-解绑手机，6-绑定手机")
    @GetMapping("sms/{status}/{phone}")
    public Result sendCode(@PathVariable Integer status, @PathVariable String phone) {
        PatternUtils.checkPhone(phone);
        // 根据status得到验证码保存到redis时的前缀
        String prefix = SMS_PREFIX_MAP.get(status);
        AssertUtils.isNotEmpty(prefix);
        smsHelper.sendCode(phone, prefix);
        return Result.success();
    }

}
