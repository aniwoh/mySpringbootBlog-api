package org.aniwoh.myspringbootblogapi.service;

import org.aniwoh.myspringbootblogapi.entity.Account;

import java.util.List;

public interface AccountService {
    //根据用户名查询
    Account findAccountByUid(Integer uid);

    Account findAccountByUsername(String username);

    List<Account> findAllAccount();

    void addUser(String username, String password);

    Account update(Account account);

    void updateAvatar(String avatarUrl, Integer uid);

    void updatePwd(String newPwd, Integer uid);

}
