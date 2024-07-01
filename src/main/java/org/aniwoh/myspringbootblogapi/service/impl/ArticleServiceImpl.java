package org.aniwoh.myspringbootblogapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.aniwoh.myspringbootblogapi.entity.Article;
import org.aniwoh.myspringbootblogapi.entity.Tag;
import org.aniwoh.myspringbootblogapi.mapper.ArticleMapper;
import org.aniwoh.myspringbootblogapi.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    ArticleMapper articleMapper;
    @Override
    public List<Article> Articlelist() {
        return articleMapper.findAllArticles();
    }

    @Override
    public  List<Map<String,Object>> ArticlelistWithTags(){
        return articleMapper.findAllArticlesWithTags();
    }

    @Override
    public Map<String,Object> getArticleByIdWithTags(int id) {
        return articleMapper.findArticleByIdWithTags(id);
    }

    @Override
    public List<Tag> getAllTags() {
        return articleMapper.findAllTags();
    }
}
