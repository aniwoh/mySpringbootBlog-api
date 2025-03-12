package org.aniwoh.myspringbootblogapi.Repository;

import org.aniwoh.myspringbootblogapi.entity.ArticleTag;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleTagRepository extends MongoRepository<ArticleTag,String> {
    List<ArticleTag> findArticleTagsByArticleId(String articleId);
}
