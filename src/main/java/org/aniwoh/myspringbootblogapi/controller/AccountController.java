package org.aniwoh.myspringbootblogapi.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aniwoh.myspringbootblogapi.entity.Account;
import org.aniwoh.myspringbootblogapi.entity.Result;
import org.aniwoh.myspringbootblogapi.entity.ResultCode;
import org.aniwoh.myspringbootblogapi.service.AccountService;
import org.aniwoh.myspringbootblogapi.vo.LoginRequest;
import org.hibernate.validator.constraints.URL;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
@RequiredArgsConstructor
public class AccountController {
    @Resource
    private AccountService accountService;

    @PostMapping("/register")
    public Result register(@RequestBody LoginRequest loginRequest) {
        Account account = accountService.findAccountByUsername(loginRequest.getUsername());
        if (account == null){
            try {
//                accountService.addUser(loginRequest.getUsername(),loginRequest.getPassword());
                return Result.error(ResultCode.ERROR,"注册功能暂未开放");
            } catch (Exception e){
                return Result.error(ResultCode.ERROR,e.getMessage());
            }
        } else {
            return Result.error(ResultCode.ERROR, "用户名已占用");
        }
    }

    @PostMapping("/login")
    public Result Login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        Account account = accountService.findAccountByUsername(username);
        if (account == null){
            return Result.error(ResultCode.ERROR,"用户不存在或密码错误");
        } else {
            if (BCrypt.checkpw(password, account.getPassword())) {
                StpUtil.login(username);
                return Result.success();
            } else {
                return Result.error(ResultCode.ERROR,"用户不存在或密码错误"); //密码不正确
            }
        }
   }

   @GetMapping("/logout")
   @SaCheckLogin
   public Result logout(){
        StpUtil.logout();
        return Result.success();
   }

    @GetMapping("/accountInfo")
    @SaCheckLogin
    public Result accountInfo() {
        String username = (String) StpUtil.getLoginId();
        Account account = accountService.findAccountByUsername(username);
        return Result.success(account);
    }

    @GetMapping("/accountList")
    @SaCheckRole("admin")
    public Result accountList() {
        log.info(StpUtil.searchSessionId("",0,-1,false).toString());
        log.info(StpUtil.getRoleList().toString());
        return Result.success(accountService.findAllAccount());
    }

//
//    @PutMapping("/update")
//    public Mono<Result> update(@RequestBody Account account) {
//        return accountService.update(account)
//                .then(Mono.just(Result.success()));
//    }
//
//    @PatchMapping("/updateAvatar")
//    public Mono<Result> updateAvatar(@RequestParam @URL String avatarUrl) {
//        // Assume a fixed UID for simplicity; replace with actual logic
//        return accountService.updateAvatar(avatarUrl, 1)
//                .then(Mono.just(Result.success()));
//    }
}
