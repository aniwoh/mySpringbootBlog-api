package org.aniwoh.myspringbootblogapi.service.impl;

import jakarta.annotation.Resource;
import org.aniwoh.myspringbootblogapi.entity.Counter;
import org.aniwoh.myspringbootblogapi.service.CounterService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.FindAndModifyOptions;


@Service
public class CounterServiceImpl implements CounterService {
    @Resource
    private MongoTemplate mongoTemplate;

    @Override
    public Integer getNextSequence(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        Update update = new Update().inc("seq", 1);
        FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true).upsert(true);

        Counter counter = mongoTemplate.findAndModify(query, update, options, Counter.class);
        return (counter != null) ? counter.getSeq() : 1;
    }
}
