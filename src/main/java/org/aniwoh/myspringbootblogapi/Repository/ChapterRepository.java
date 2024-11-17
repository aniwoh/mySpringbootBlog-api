package org.aniwoh.myspringbootblogapi.Repository;

import org.aniwoh.myspringbootblogapi.entity.Chapter;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChapterRepository extends MongoRepository<Chapter,String> {
    @Query("{'novelId': ?0}")
    Optional<Chapter> findTopByNovelIdOrderByIndexDesc(String novelId);

}
