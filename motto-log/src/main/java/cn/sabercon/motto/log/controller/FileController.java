package cn.sabercon.motto.log.controller;

import cn.sabercon.motto.common.dto.CommonPage;
import cn.sabercon.motto.common.dto.CommonResult;
import cn.sabercon.motto.common.dto.PageReq;
import cn.sabercon.motto.log.dto.FileDto;
import cn.sabercon.motto.log.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author ywk
 * @date 2020-01-02
 */
@Api("上传和获取文件接口")
@CrossOrigin
@RestController
@RequestMapping("file")
public class FileController {

    @Autowired
    private FileService service;

    @ApiOperation("上传文件")
    @PostMapping
    public CommonResult save(MultipartFile file, String name) {
        service.save(file, name);
        return CommonResult.success();
    }

    @ApiOperation("删除文件")
    @DeleteMapping
    public CommonResult delete(Long id) {
        service.delete(id);
        return CommonResult.success();
    }

    @ApiOperation("获取文件列表")
    @GetMapping
    public CommonResult<CommonPage<FileDto>> list(PageReq pageReq) {
        return CommonResult.success(service.list(pageReq));
    }

}
