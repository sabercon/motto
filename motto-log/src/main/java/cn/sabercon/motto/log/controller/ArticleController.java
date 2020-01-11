package cn.sabercon.motto.log.controller;

import cn.sabercon.motto.common.dto.PageReq;
import cn.sabercon.motto.common.dto.PageRes;
import cn.sabercon.motto.common.dto.Result;
import cn.sabercon.motto.log.entity.Article;
import cn.sabercon.motto.log.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author ywk
 * @date 2020-01-03
 */
@Api("上传和获取文章接口")
@CrossOrigin
@RestController
@RequestMapping("article")
public class ArticleController {

    @Autowired
    private ArticleService service;

    @ApiOperation("新增或修改文章")
    @PostMapping
    public Result save(Article article) {
        service.save(article);
        return Result.success();
    }

    @ApiOperation("删除文章")
    @DeleteMapping("{id}")
    public Result delete(@PathVariable Long id) {
        service.delete(id);
        return Result.success();
    }

    @ApiOperation("获取文章")
    @GetMapping("{id}")
    public Result<Article> get(@PathVariable Long id) {
        return Result.success(service.get(id));
    }

    @ApiOperation("获取文章列表")
    @GetMapping("list")
    public Result<PageRes<Article>> list(PageReq pageReq) {
        return Result.success(service.list(pageReq));
    }

}
