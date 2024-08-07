package org.aniwoh.myspringbootblogapi.Repository;

import org.aniwoh.myspringbootblogapi.entity.Counter;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CounterRepository extends MongoRepository<Counter, String> {
    Counter findByName(String name);
}
