package org.aniwoh.myspringbootblogapi.service;

import org.aniwoh.myspringbootblogapi.entity.Article;
import org.aniwoh.myspringbootblogapi.entity.Tag;
import org.aniwoh.myspringbootblogapi.vo.ArticleVo;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ArticleService {
    List<Article> Articlelist();
    Optional<Article> findArticleById(String id);
    void incrementViewCount(String articleId);
    void insertArticle(Article article);


}
