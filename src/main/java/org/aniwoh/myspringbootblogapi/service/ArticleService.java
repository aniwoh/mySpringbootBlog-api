package org.aniwoh.myspringbootblogapi.service;

import org.aniwoh.myspringbootblogapi.entity.Article;
import org.aniwoh.myspringbootblogapi.entity.Tag;

import java.util.List;
import java.util.Map;

public interface ArticleService {
    List<Article> Articlelist();

    List<Map<String,Object>> ArticlelistWithTags();


    Map<String,Object> getArticleByIdWithTags(int id);

    List<Tag> getAllTags();
}
