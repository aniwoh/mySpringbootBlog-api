package org.aniwoh.myspringbootblogapi.Repository;

import org.aniwoh.myspringbootblogapi.entity.ReadingProcess;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReadingProcessRepository extends MongoRepository<ReadingProcess,String> {

    ReadingProcess findByNovelId(String novelId);
}
