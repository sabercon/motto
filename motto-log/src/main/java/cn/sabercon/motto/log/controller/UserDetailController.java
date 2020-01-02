package cn.sabercon.motto.log.controller;

import cn.sabercon.motto.common.dto.CommonResult;
import cn.sabercon.motto.log.dto.UserDetailDto;
import cn.sabercon.motto.log.service.UserDetailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ywk
 * @date 2019-10-15
 */
@Api("前台用户详细信息接口")
@CrossOrigin
@RestController
@RequestMapping("userInfo")
public class UserDetailController {

    @Autowired
    private UserDetailService service;

    @ApiOperation("得到登录用户的详细信息")
    @GetMapping
    public CommonResult<UserDetailDto> get() {
        return CommonResult.success(service.get());
    }

    @ApiOperation("更新登录用户的详细信息")
    @PutMapping
    public CommonResult update(UserDetailDto dto) {
        service.update(dto);
        return CommonResult.success();
    }

    @ApiOperation("上传用户头像")
    @PostMapping("avatar")
    public CommonResult<String> uploadAvatar(MultipartFile avatar) {
        return CommonResult.success(service.uploadAvatar(avatar));
    }

}
