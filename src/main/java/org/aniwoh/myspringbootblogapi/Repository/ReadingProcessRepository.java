package org.aniwoh.myspringbootblogapi.Repository;

import org.aniwoh.myspringbootblogapi.entity.ReadingProcess;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReadingProcessRepository extends MongoRepository<ReadingProcess,String> {

    ReadingProcess findByNovelId(String novelId);
}
