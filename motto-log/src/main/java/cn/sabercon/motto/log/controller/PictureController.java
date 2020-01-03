package cn.sabercon.motto.log.controller;

import cn.sabercon.motto.common.dto.CommonPage;
import cn.sabercon.motto.common.dto.CommonResult;
import cn.sabercon.motto.common.dto.PageReq;
import cn.sabercon.motto.log.dto.FileDto;
import cn.sabercon.motto.log.dto.PictureDto;
import cn.sabercon.motto.log.service.FileService;
import cn.sabercon.motto.log.service.PictureService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ywk
 * @date 2020-01-03
 */
@Api("上传和获取图片接口")
@CrossOrigin
@RestController
@RequestMapping("pic")
public class PictureController {

    @Autowired
    private PictureService service;

    @ApiOperation("上传图片")
    @PostMapping
    public CommonResult save(MultipartFile pic, String name) {
        service.save(pic, name);
        return CommonResult.success();
    }

    @ApiOperation("删除图片")
    @DeleteMapping
    public CommonResult delete(Long id) {
        service.delete(id);
        return CommonResult.success();
    }

    @ApiOperation("获取图片列表")
    @GetMapping
    public CommonResult<CommonPage<PictureDto>> list(PageReq pageReq) {
        return CommonResult.success(service.list(pageReq));
    }

}
