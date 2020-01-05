package cn.sabercon.motto.log.controller;

import cn.sabercon.motto.common.dto.PageRes;
import cn.sabercon.motto.common.dto.Result;
import cn.sabercon.motto.common.dto.PageReq;
import cn.sabercon.motto.log.entity.File;
import cn.sabercon.motto.log.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Result save(File file) {
        service.save(file);
        return Result.success();
    }

    @ApiOperation("删除文件")
    @DeleteMapping("{id}")
    public Result delete(@PathVariable Long id) {
        service.delete(id);
        return Result.success();
    }

    @ApiOperation("获取文件列表")
    @GetMapping
    public Result<PageRes<File>> list(PageReq pageReq) {
        return Result.success(service.list(pageReq));
    }

}
