package org.aniwoh.myspringbootblogapi.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "org.aniwoh.myspringbootblogapi.Repository")
public class MongoConfig {
    // 其他 MongoDB 配置
}
