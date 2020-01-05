package cn.sabercon.motto.log.controller;

import cn.sabercon.motto.common.dto.PageRes;
import cn.sabercon.motto.common.dto.Result;
import cn.sabercon.motto.common.dto.PageReq;
import cn.sabercon.motto.log.entity.Diary;
import cn.sabercon.motto.log.service.DiaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Result save(Diary diary) {
        service.save(diary);
        return Result.success();
    }

    @ApiOperation("删除日记")
    @DeleteMapping("{id}")
    public Result delete(@PathVariable Long id) {
        service.delete(id);
        return Result.success();
    }

    @ApiOperation("获取日记")
    @GetMapping("{id}")
    public Result<Diary> get(@PathVariable Long id) {
        return Result.success(service.get(id));
    }

    @ApiOperation("获取日记列表")
    @GetMapping("list")
    public Result<PageRes<Diary>> list(PageReq pageReq) {
        return Result.success(service.list(pageReq));
    }

}
