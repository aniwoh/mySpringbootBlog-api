package org.aniwoh.myspringbootblogapi.controller;

import org.aniwoh.myspringbootblogapi.entity.Account;
import org.aniwoh.myspringbootblogapi.mapper.AccountMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class IndexController {
    @Autowired
    AccountMapper accountMapper;
    @RequestMapping({"/test"})
    @ResponseBody
    public Account index(Integer uid){
        log.info("即将返回一个用户");
        return accountMapper.findUserByUid(uid);
    }

    @RequestMapping({"/"})
    public String index(){
        return "index.html";
    }
}
