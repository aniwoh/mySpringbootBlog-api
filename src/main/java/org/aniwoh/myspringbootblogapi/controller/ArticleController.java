package org.aniwoh.myspringbootblogapi.controller;

import org.aniwoh.myspringbootblogapi.entity.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @GetMapping("/list")
    public Result<String> list(){
        //
        return Result.success("所有文章的数据");
    }
}
