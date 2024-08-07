package org.aniwoh.myspringbootblogapi.controller;

import org.aniwoh.myspringbootblogapi.Repository.AccountRepository;
import org.aniwoh.myspringbootblogapi.entity.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
@Slf4j
public class IndexController {
    @Autowired
    AccountRepository accountRepository;
    @RequestMapping({"/test"})
    @ResponseBody
    public Optional<Account> index(Integer uid){
        log.info("即将返回一个用户");
        return accountRepository.findAccountByUid(uid);
    }

    @RequestMapping({"/"})
    public String index(){
        return "index.html";
    }

    @RequestMapping({"/post"})
    public String post(){
        return "index/post.html";
    }


}
