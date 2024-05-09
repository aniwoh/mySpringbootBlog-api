package org.aniwoh.myspringbootblogapi.service;

import org.aniwoh.myspringbootblogapi.entity.Article;

import java.util.List;
import java.util.Map;

public interface ArticleService {
    List<Article> Articlelist();

    List<Map<String,Object>> ArticlelistWithTags();

    Article getArticleById(int id);
}
