package cn.sabercon.motto.log.controller;

import cn.sabercon.motto.common.dto.Result;
import cn.sabercon.motto.common.util.CookieUtils;
import cn.sabercon.motto.log.dto.UserDto;
import cn.sabercon.motto.log.dto.UserReq;
import cn.sabercon.motto.log.service.UserService;
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
@CrossOrigin
@Api("用户接口")
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService service;

    @ApiOperation("得到登录用户的信息")
    @GetMapping
    public Result<UserDto> get() {
        return Result.success(service.get());
    }

    @ApiOperation("更新登录用户的详细信息")
    @PutMapping
    public Result update(UserDto dto) {
        service.update(dto);
        return Result.success();
    }

    @ApiOperation("用户注册")
    @PostMapping("register")
    public Result register(UserReq userReq) {
        service.register(userReq);
        return Result.success();
    }

    @ApiOperation("用户登录")
    @ApiImplicitParam(name = "remember", value = "是否保存登录信息", defaultValue = "false")
    @PostMapping("login")
    public Result login(UserReq userReq, @RequestParam(defaultValue = "false") boolean remember) {
        String token = service.login(userReq);
        int expiry = remember ? JWT_USER_EXPIRATION.intValue() : -1;
        CookieUtils.addCookie(JWT_COOKIE_NAME, token, expiry);
        return Result.success();
    }

    @ApiOperation("找回密码")
    @PostMapping("reset")
    public Result reset(UserReq userReq) {
        service.reset(userReq);
        return Result.success();
    }

    @ApiOperation("用户退出")
    @PostMapping("logout")
    public Result logout() {
        service.logout();
        CookieUtils.addCookie(JWT_COOKIE_NAME, null, 0);
        return Result.success();
    }

    @ApiOperation("修改密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "password", value = "新密码"),
            @ApiImplicitParam(name = "smsCode", value = "手机号验证码")
    })
    @PostMapping("password")
    public Result updatePassword(String password, String smsCode) {
        service.updatePassword(password, smsCode);
        // 退出登录
        return logout();
    }

    @ApiOperation("解绑手机号")
    @ApiImplicitParam(name = "smsCode", value = "原手机号的验证码")
    @PostMapping("unbind")
    public Result unbindPhone(String smsCode) {
        service.unbindPhone(smsCode);
        return Result.success();
    }

    @ApiOperation("绑定手机号")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "phone", value = "新手机号"),
            @ApiImplicitParam(name = "smsCode", value = "新手机号的验证码")
    })
    @PostMapping("bind")
    public Result bindPhone(String phone, String smsCode) {
        service.bindPhone(phone, smsCode);
        return Result.success();
    }

}
