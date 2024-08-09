package org.aniwoh.myspringbootblogapi.service.impl;

import org.aniwoh.myspringbootblogapi.Repository.AccountRepository;
import org.aniwoh.myspringbootblogapi.entity.Account;
import org.aniwoh.myspringbootblogapi.service.AccountService;
import org.aniwoh.myspringbootblogapi.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CounterService counterService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Mono<Account> findAccountByUid(Integer uid){
        return accountRepository.findAccountByUid(uid);
    }
    @Override
    public Mono<Account> findAccountByUsername(String username) {
        return accountRepository.findAccountByUsername(username);
    }

    @Override
    public Mono<Account> addUser(String username, String password) {
        return counterService.getNextSequence("account_uid")
                .flatMap(uid -> {
                    Account account = new Account(username, passwordEncoder.encode(password));
                    account.setUid(uid);
                    return accountRepository.save(account);
                });
    }

    @Override
    public Mono<Account> update(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Mono<Void> updateAvatar(String avatarUrl, Integer uid) {
        return accountRepository.findAccountByUid(uid)
                .flatMap(account -> {
                    account.setUserPic(avatarUrl);
                    return accountRepository.save(account);
                })
                .then();
    }

    @Override
    public Mono<Void> updatePwd(String newPwd,Integer uid) {
        return accountRepository.findAccountByUid(uid)
                .flatMap(account -> {
                    account.setPassword(newPwd);
                    return accountRepository.save(account);
                })
                .then();
    }
}
