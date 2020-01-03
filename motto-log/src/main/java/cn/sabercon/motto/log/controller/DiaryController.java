package cn.sabercon.motto.log.controller;

import cn.sabercon.motto.common.dto.CommonPage;
import cn.sabercon.motto.common.dto.CommonResult;
import cn.sabercon.motto.common.dto.PageReq;
import cn.sabercon.motto.log.dto.DiaryDto;
import cn.sabercon.motto.log.dto.PictureDto;
import cn.sabercon.motto.log.service.DiaryService;
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
@Api("上传和获取日记接口")
@CrossOrigin
@RestController
@RequestMapping("diary")
public class DiaryController {

    @Autowired
    private DiaryService service;

    @ApiOperation("新增或修改日记")
    @PostMapping
    public CommonResult save(DiaryDto diaryDto) {
        service.save(diaryDto);
        return CommonResult.success();
    }

    @ApiOperation("删除日记")
    @DeleteMapping
    public CommonResult delete(Long id) {
        service.delete(id);
        return CommonResult.success();
    }

    @ApiOperation("获取日记")
    @GetMapping
    public CommonResult<DiaryDto> get(Long id) {
        return CommonResult.success(service.get(id));
    }

    @ApiOperation("获取日记列表")
    @GetMapping("list")
    public CommonResult<CommonPage<DiaryDto>> list(PageReq pageReq) {
        return CommonResult.success(service.list(pageReq));
    }

}
