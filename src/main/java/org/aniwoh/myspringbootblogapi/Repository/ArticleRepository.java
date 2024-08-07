package org.aniwoh.myspringbootblogapi.Repository;

import org.aniwoh.myspringbootblogapi.entity.Article;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends MongoRepository<Article,String> {

    Optional<Article> findArticleById(String id);
}
