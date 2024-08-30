package org.aniwoh.myspringbootblogapi.service;

import org.aniwoh.myspringbootblogapi.entity.Account;
import reactor.core.publisher.Mono;


import java.util.Optional;

public interface AccountService {
    //根据用户名查询
    public Account findAccountByUid(Integer uid);

    public Account findAccountByUsername(String username);

    public Account addUser(String username, String password);

    public Account update(Account account);

    public void updateAvatar(String avatarUrl, Integer uid);

    public void updatePwd(String newPwd,Integer uid);

}
