package org.aniwoh.myspringbootblogapi.service.impl;

import org.aniwoh.myspringbootblogapi.Repository.AccountRepository;
import org.aniwoh.myspringbootblogapi.entity.Account;
import org.aniwoh.myspringbootblogapi.service.AccountService;
import org.aniwoh.myspringbootblogapi.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CounterService counterService;

    @Override
    public Optional<Account> findAccountByUid(Integer uid){
        return accountRepository.findAccountByUid(uid);
    }
    @Override
    public Optional<Account> findAccountByUsername(String username) {
        return accountRepository.findAccountByUsername(username);
    }

    @Override
    public Account addUser(String username, String password) {
        Account account = new Account(username,password);
        account.setUid(counterService.getNextSequence("account_uid"));
        return accountRepository.save(account);
    }

    @Override
    public Account update(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public void updateAvatar(String avatarUrl, Integer uid) {
        Optional<Account> optionalAccount = accountRepository.findAccountByUid(uid);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setUserPic(avatarUrl);
            accountRepository.save(account);
        }

    }

    @Override
    public void updatePwd(String newPwd,Integer uid) {
        Optional<Account> optionalAccount = accountRepository.findAccountByUid(uid);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            account.setPassword(newPwd);
            accountRepository.save(account);
        }
    }
}
