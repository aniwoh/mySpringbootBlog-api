package org.aniwoh.myspringbootblogapi.Repository;

import org.aniwoh.myspringbootblogapi.entity.Account;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;


@Repository
public interface AccountRepository extends ReactiveMongoRepository<Account, String> {
    Mono<Account> findAccountByUid(Integer uid);

    Mono<Account> findAccountByUsername(String username);
}
