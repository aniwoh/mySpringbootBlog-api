package org.aniwoh.myspringbootblogapi.service;

import org.aniwoh.myspringbootblogapi.entity.Account;

public interface AccountService {
    //根据用户名查询
    Account findByUsername(String username);
    void register(String username,String password);

    void update(Account account);

    //更新头像
    void updateAvatar(String avatarUrl);

    //更新密码
    void updatePwd(String newPwd);
}
