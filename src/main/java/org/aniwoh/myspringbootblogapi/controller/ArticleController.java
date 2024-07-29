package org.aniwoh.myspringbootblogapi.controller;

import lombok.extern.slf4j.Slf4j;
import org.aniwoh.myspringbootblogapi.entity.Article;
import org.aniwoh.myspringbootblogapi.entity.Result;
import org.aniwoh.myspringbootblogapi.entity.Tag;
import org.aniwoh.myspringbootblogapi.service.ArticleService;
import org.aniwoh.myspringbootblogapi.utils.AutoReload;
import org.aniwoh.myspringbootblogapi.vo.ArticleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
@CrossOrigin(origins = "http://localhost:5173")
@Slf4j
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @GetMapping("/list")
    public Result articleList(){
        //获取文章数据
        List<Article> articleList=articleService.Articlelist();
        List<ArticleVo> articleVoList = articleList.stream().map(article -> {
            List<String> tagNames = articleService.findTagsByArticleId(article.getId());
            ArticleVo articleVo = new ArticleVo();
            try {
                AutoReload.reload(article,articleVo);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            articleVo.setTagNames(tagNames);
            articleVo.setBody(null); // 设置 body 为 null
            return articleVo;
        }).toList();
        return Result.success(articleVoList);
    }

    @GetMapping("/detail")
    public Result articleDetail(int id){
        Article article = articleService.getArticleById(id);
        ArticleVo articleVo = new ArticleVo();
        try {
            AutoReload.reload(article,articleVo);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        List<String> tagNames = articleService.findTagsByArticleId(article.getId());
        articleVo.setTagNames(tagNames);

        //获取文章数据
        return Result.success(articleVo);
    }

    @GetMapping("/tags")
    public Result tags(){
        //获取文章数据
        List<Tag> tags=articleService.getAllTags();
        return Result.success(tags);
    }

    @PutMapping("/upload")
    public Result uploadArticle(@RequestBody ArticleVo articleVo){
        Article article = new Article();
        article.setTitle(articleVo.getTitle());
        article.setAuthor(articleVo.getAuthor());
        article.setBody(articleVo.getBody());
        article.setCreateDate(articleVo.getCreateDate());
        articleService.insertArticle(article);
        if (articleVo.getTagNames() != null){
        for(String tagName : articleVo.getTagNames()){
            Tag tag = articleService.findByName(tagName);
            if (tag == null){
                tag = new Tag();
                tag.setTagName(tagName);
                log.info("准备添加tag");
                articleService.insertTag(tag);
            }
            articleService.insertArticleTag(article.getId(), tag.getId());
            }
        }
        return Result.success();
    }

}
