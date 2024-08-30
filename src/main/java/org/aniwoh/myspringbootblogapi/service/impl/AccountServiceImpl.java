package org.aniwoh.myspringbootblogapi.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import org.aniwoh.myspringbootblogapi.Repository.AccountRepository;
import org.aniwoh.myspringbootblogapi.entity.Account;
import org.aniwoh.myspringbootblogapi.service.AccountService;
import org.aniwoh.myspringbootblogapi.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CounterService counterService;

    @Override
    public Account findAccountByUid(Integer uid){
        return accountRepository.findAccountByUid(uid);
    }
    @Override
    public Account findAccountByUsername(String username) {
        return accountRepository.findAccountByUsername(username);
    }

    @Override
    public Account addUser(String username, String password) {
        Integer uid = counterService.getNextSequence("account_uid");
        Account account = new Account(username, BCrypt.hashpw(password));
        account.setUid(uid);
        return accountRepository.save(account);
    }

    @Override
    public Account update(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public void updateAvatar(String avatarUrl, Integer uid) {

    }

    @Override
    public void updatePwd(String newPwd,Integer uid) {}

}
