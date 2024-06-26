package org.aniwoh.myspringbootblogapi.controller;

import org.aniwoh.myspringbootblogapi.entity.Article;
import org.aniwoh.myspringbootblogapi.entity.Result;
import org.aniwoh.myspringbootblogapi.entity.Tag;
import org.aniwoh.myspringbootblogapi.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/article")
@CrossOrigin(origins = "http://localhost:5173")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @GetMapping("/list")
    public Result articleList(){
        //获取文章数据
        List<Map<String,Object>> articleList=articleService.ArticlelistWithTags();
        return Result.success(articleList);
    }

    @GetMapping("/detail")
    public Result articleDetail(int id){
        //获取文章数据
        Map<String,Object> article=articleService.getArticleByIdWithTags(id);
        return Result.success(article);
    }

    @GetMapping("/tags")
    public Result tags(){
        //获取文章数据
        List<Tag> tags=articleService.getAllTags();
        return Result.success(tags);
    }
}
