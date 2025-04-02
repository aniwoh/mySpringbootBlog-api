package org.aniwoh.myspringbootblogapi.Repository;

import org.aniwoh.myspringbootblogapi.entity.Chapter;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterRepository extends MongoRepository<Chapter,String> {
    @Aggregation(pipeline = {
            "{$match: {'novelId': ?0}}",
            "{$sort: {'index': -1}}",
            "{$limit: 1}"
    })
    Chapter findFirstByNovelIdOrderByIndexDesc(String novelId);

    List<Chapter> findByNovelId(String novelId);

    void deleteChaptersByNovelId(String id);
}
