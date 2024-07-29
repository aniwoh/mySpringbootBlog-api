package org.aniwoh.myspringbootblogapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.aniwoh.myspringbootblogapi.entity.Article;
import org.aniwoh.myspringbootblogapi.entity.Tag;
import org.aniwoh.myspringbootblogapi.mapper.ArticleMapper;
import org.aniwoh.myspringbootblogapi.service.ArticleService;
import org.aniwoh.myspringbootblogapi.vo.ArticleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    ArticleMapper articleMapper;
    @Override
    public List<Article> Articlelist() {
        return articleMapper.findAllArticles();
    }

    @Deprecated
    public  List<ArticleVo> ArticlelistWithTags(){
        return articleMapper.findAllArticlesWithTags();
    }

    @Deprecated
    public ArticleVo getArticleByIdWithTags(int id) {
        return articleMapper.findArticleByIdWithTags(id);
    }

    @Override
    public Article getArticleById(int id) {
        return articleMapper.findArticleById(id);
    }

    @Override
    public List<Tag> getAllTags() {
        return articleMapper.findAllTags();
    }

    @Override
    public Tag findByName(String name) {
        return articleMapper.findTagByName(name);
    }

    @Override
    public void insertArticle(Article article) {
        List<Tag> tagList = getAllTags();
        articleMapper.insertArticle(article);
    }

    @Override
    public void insertTag(Tag tag) {
        articleMapper.insertTag(tag);
    }

    @Override
    public void insertArticleTag(Integer articleId,Integer tagId) {
        articleMapper.insertArticleTag(articleId,tagId);
    }

    @Override
    public List<String> findTagsByArticleId(Integer articleId) {
        return articleMapper.findTagsByArticleId(articleId);
    }
}
