package org.aniwoh.myspringbootblogapi.service.impl;

import cn.dev33.satoken.secure.BCrypt;
import jakarta.annotation.Resource;
import org.aniwoh.myspringbootblogapi.Repository.AccountRepository;
import org.aniwoh.myspringbootblogapi.entity.Account;
import org.aniwoh.myspringbootblogapi.service.AccountService;
import org.aniwoh.myspringbootblogapi.service.CounterService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    @Resource
    private AccountRepository accountRepository;

    @Resource
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
    public void addUser(String username, String password) {
        Integer uid = counterService.getNextSequence("account_uid");
        Account account = new Account(username, BCrypt.hashpw(password));
        account.setUid(uid);
        accountRepository.save(account);
    }

    @Override
    public List<Account> findAllAccount() {
        return accountRepository.findAll();
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
