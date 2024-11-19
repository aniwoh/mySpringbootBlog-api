package org.aniwoh.myspringbootblogapi.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.aniwoh.myspringbootblogapi.entity.Article;
import org.aniwoh.myspringbootblogapi.entity.ArticleTag;
import org.aniwoh.myspringbootblogapi.entity.Result;
import org.aniwoh.myspringbootblogapi.entity.Tag;
import org.aniwoh.myspringbootblogapi.service.ArticleService;
import org.aniwoh.myspringbootblogapi.service.TagService;
import org.aniwoh.myspringbootblogapi.utils.AutoReload;
import org.aniwoh.myspringbootblogapi.vo.ArticleVo;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/article")
@Slf4j
public class ArticleController {
    @Resource
    private ArticleService articleService;

    @Resource
    private TagService tagService;

    @GetMapping("/list")
    public Result articleList() {
        //获取文章数据
        List<Article> articleList = articleService.Articlelist();
        List<ArticleVo> articleVoList = articleList.stream().map(article -> {
            List<String> tagNames = tagService.findTagNamesByArticleId(article.getId());
            ArticleVo articleVo = new ArticleVo();
            try {
                AutoReload.reload(article, articleVo);
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
    public Result articleDetail(@RequestParam String id, HttpSession session) {
        Optional<Article> optionalArticle = articleService.findArticleById(id);
        Article article = new Article();
        if (optionalArticle.isPresent()) {
            article = optionalArticle.get();
        }
        ArticleVo articleVo = new ArticleVo();
        try {
            AutoReload.reload(article, articleVo);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        List<String> tagNames = tagService.findTagNamesByArticleId(article.getId());
        articleVo.setTagNames(tagNames);

        String sessionKey = "viewedArticle_" + id;
        if (session.getAttribute(sessionKey) == null) {
            // 增加观看数
            articleService.incrementViewCount(id);
            // 记录用户已经访问过该文章
            session.setAttribute(sessionKey, true);
        }

        //获取文章数据
        return Result.success(articleVo);
    }

    @GetMapping("/tags")
    public Result tags() {
        //获取文章数据
        List<Tag> tags = tagService.findAllTags();
        return Result.success(tags);
    }

    @PutMapping("/upload")
    @SaCheckRole("admin")
    public Result uploadArticle(@RequestBody ArticleVo articleVo) {
        Article article = new Article();
        try {
            AutoReload.reload(articleVo, article);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        articleService.insertArticle(article);
        if (articleVo.getTagNames() != null) {
            for (String tagName : articleVo.getTagNames()) {
                Optional<Tag> optionalTag = tagService.findTagByTagNames(tagName);
                Tag tag = new Tag();
                if (optionalTag.isEmpty()) {
                    tag.setTagName(tagName);
                    log.info("准备添加tag");
                    tagService.insertTag(tag);
                } else {
                    tag = optionalTag.get();
                }
                ArticleTag articleTag = new ArticleTag(article.getId(), tag.getId());
                tagService.insertArticleTag(articleTag);
            }
        }
        return Result.success();
    }

}
