package org.aniwoh.myspringbootblogapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.aniwoh.myspringbootblogapi.Aspect.CostTime;
import org.aniwoh.myspringbootblogapi.Repository.ArticleRepository;
import org.aniwoh.myspringbootblogapi.entity.Article;
import org.aniwoh.myspringbootblogapi.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    ArticleRepository articleRepository;
    @Override
    @CostTime
    public List<Article> Articlelist() {
        return articleRepository.findAll();
    }

    @Override
    public Optional<Article> findArticleById(String id) {
        return articleRepository.findArticleById(id);
    }

    @Override
    public void incrementViewCount(String articleId) {
        Optional<Article> optionalArticle = articleRepository.findArticleById(articleId);
        if (optionalArticle.isPresent()){
            Article article=optionalArticle.get();
            article.setViewCounts(article.getViewCounts()+1);
            articleRepository.save(article);
        }
    }

    @Override
    public void insertArticle(Article article) {
        articleRepository.save(article);
    }
}
