package org.aniwoh.myspringbootblogapi.service;

import org.aniwoh.myspringbootblogapi.entity.Account;
import reactor.core.publisher.Mono;


import java.util.Optional;

public interface AccountService {
    //根据用户名查询
    public Mono<Account> findAccountByUid(Integer uid);

    public Mono<Account> findAccountByUsername(String username);

    public Mono<Account> addUser(String username, String password);

    public Mono<Account> update(Account account);

    public Mono<Void> updateAvatar(String avatarUrl, Integer uid);

    public Mono<Void> updatePwd(String newPwd,Integer uid);

}
