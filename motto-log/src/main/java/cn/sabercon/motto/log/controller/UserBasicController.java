package cn.sabercon.motto.log.controller;

import cn.sabercon.motto.common.response.CommonResult;
import cn.sabercon.motto.common.util.CookieUtils;
import cn.sabercon.motto.log.dto.UserBasicDto;
import cn.sabercon.motto.log.service.UserBasicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static cn.sabercon.motto.log.util.JwtTokenUtils.JWT_COOKIE_NAME;
import static cn.sabercon.motto.log.util.JwtTokenUtils.JWT_USER_EXPIRATION;

/**
 * @author ywk
 * @date 2019-10-15
 */
@Api("前台用户基础信息接口")
@CrossOrigin
@RestController
@RequestMapping("user")
public class UserBasicController {

    @Autowired
    private UserBasicService service;

    @ApiOperation("得到登录用户的基础信息")
    @GetMapping
    public CommonResult<UserBasicDto> get() {
        return CommonResult.success(service.get());
    }

    @ApiOperation("用户注册")
    @PostMapping("register")
    public CommonResult register(UserBasicDto userBasicDto) {
        service.register(userBasicDto);
        return CommonResult.success();
    }

    @ApiOperation("用户登录")
    @ApiImplicitParam(name = "remember", value = "是否保存登录信息", defaultValue = "false")
    @PostMapping("login")
    public CommonResult login(UserBasicDto userBasicDto, @RequestParam(defaultValue = "false") boolean remember) {
        String token = service.login(userBasicDto);
        int expiry = remember ? JWT_USER_EXPIRATION.intValue() : -1;
        CookieUtils.addCookie(JWT_COOKIE_NAME, token, expiry);
        return CommonResult.success();
    }

    @ApiOperation("找回密码")
    @PostMapping("reset")
    public CommonResult reset(UserBasicDto userBasicDto) {
        service.reset(userBasicDto);
        return CommonResult.success();
    }

    @ApiOperation("用户退出")
    @PostMapping("logout")
    public CommonResult logout() {
        service.logout();
        CookieUtils.addCookie(JWT_COOKIE_NAME, null, 0);
        return CommonResult.success();
    }

    @ApiOperation("发送短信验证码")
    @ApiImplicitParam(name = "status", allowableValues = "range[1,6]",
            value = "验证码的发送类型：1-注册，2-登录，3-重置密码，4-修改密码，5-解绑手机，6-绑定手机")
    @GetMapping("sms/{status}/{phone}")
    public CommonResult sendSmsCode(@PathVariable Integer status, @PathVariable String phone) {
        service.sendSmsCode(status, phone);
        return CommonResult.success();
    }

    @ApiOperation("修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "password", value = "新密码"),
            @ApiImplicitParam(name = "smsCode", value = "手机号验证码")
    })
    @PostMapping("password")
    public CommonResult updatePassword(String password, String smsCode) {
        service.updatePassword(password, smsCode);
        // 退出登录
        return logout();
    }

    @ApiOperation("解绑手机号")
    @ApiImplicitParam(name = "smsCode", value = "原手机号的验证码")
    @PostMapping("unbind")
    public CommonResult unbindPhone(String smsCode) {
        service.unbindPhone(smsCode);
        return CommonResult.success();
    }

    @ApiOperation("绑定手机号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "新手机号"),
            @ApiImplicitParam(name = "smsCode", value = "新手机号的验证码")
    })
    @PostMapping("bind")
    public CommonResult bindPhone(String phone, String smsCode) {
        service.bindPhone(phone, smsCode);
        return CommonResult.success();
    }

}
