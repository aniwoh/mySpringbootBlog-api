package org.aniwoh.myspringbootblogapi.service;

import org.aniwoh.myspringbootblogapi.entity.Article;
import org.aniwoh.myspringbootblogapi.entity.ArticleTag;
import org.aniwoh.myspringbootblogapi.entity.Tag;

import java.util.List;
import java.util.Optional;

public interface TagService {
    List<Tag> findAllTags();

    List<Tag> findTagsByArticleId(String articleId);

    List<String> findTagNamesByArticleId(String articleId);
    Optional<Tag> findTagByTagNames(String tagNames);

    void insertTag(Tag tag);

    void insertArticleTag(ArticleTag articleTag);
}
