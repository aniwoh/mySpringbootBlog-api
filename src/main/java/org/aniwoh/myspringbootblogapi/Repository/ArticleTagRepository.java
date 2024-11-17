package org.aniwoh.myspringbootblogapi.Repository;

import org.aniwoh.myspringbootblogapi.entity.ArticleTag;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ArticleTagRepository extends MongoRepository<ArticleTag,String> {
    List<ArticleTag> findArticleTagsByArticleId(String articleId);
}
