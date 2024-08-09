package org.aniwoh.myspringbootblogapi.service.impl;

import org.aniwoh.myspringbootblogapi.entity.Counter;
import org.aniwoh.myspringbootblogapi.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import reactor.core.publisher.Mono;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class CounterServiceImpl implements CounterService {
    @Autowired
    private ReactiveMongoTemplate reactiveMongoTemplate;
    @Override
    public Mono<Integer> getNextSequence(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        Update update = new Update().inc("seq", 1);
        FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true).upsert(true);

        return reactiveMongoTemplate.findAndModify(query, update, options, Counter.class)
                .map(Counter::getSeq)
                .defaultIfEmpty(1);
    }
}
