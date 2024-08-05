package org.aniwoh.myspringbootblogapi.service;

import org.aniwoh.myspringbootblogapi.entity.Article;
import org.aniwoh.myspringbootblogapi.entity.Tag;
import org.aniwoh.myspringbootblogapi.vo.ArticleVo;

import java.util.List;
import java.util.Map;

public interface ArticleService {
    List<Article> Articlelist();

    List<ArticleVo> ArticlelistWithTags();


    ArticleVo getArticleByIdWithTags(int id);

    Article getArticleById(int id);

    void incrementViewCount(int id);

    List<Tag> getAllTags();

    Tag findByName(String name);

    void insertArticle(Article article);

    void insertTag(Tag tag);

    void insertArticleTag(Integer articleId,Integer tagId);

    List<String> findTagsByArticleId(Integer articleId);
}
