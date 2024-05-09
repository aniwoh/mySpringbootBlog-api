package org.aniwoh.myspringbootblogapi.service.impl;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import org.aniwoh.myspringbootblogapi.entity.Account;
import org.aniwoh.myspringbootblogapi.mapper.AccountMapper;
import org.aniwoh.myspringbootblogapi.service.AccountService;
import org.aniwoh.myspringbootblogapi.utils.ThreadLocalUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private AccountMapper accountMapper;
    @Override
    public Account findByUsername(String username) {
        return accountMapper.findUserByUsername(username);
    }

    @Override
    public void register(String username, String password) {
        Digester md5 = new Digester(DigestAlgorithm.MD5);
        //密码加密
        String MD5password = md5.digestHex(password);
        //添加
        accountMapper.addUser(username, MD5password);
    }


    @Override
    public void update(Account account) {
        accountMapper.update(account);
    }

    @Override
    public void updateAvatar(String avatarUrl) {
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer uid = Integer.parseInt(map.get("uid").toString());
        accountMapper.updateAvatar(avatarUrl,uid);
    }

    @Override
    public void updatePwd(String newPwd) {
        Map<String,Object> map = ThreadLocalUtil.get();
        Integer uid = Integer.parseInt(map.get("uid").toString());
        accountMapper.updatePwd(newPwd,uid);
    }
}
