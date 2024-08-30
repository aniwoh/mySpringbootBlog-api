package org.aniwoh.myspringbootblogapi.controller;

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
@Validated
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
                accountService.addUser(loginRequest.getUsername(),loginRequest.getPassword());
                return Result.success();
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
            return Result.error(ResultCode.ERROR);
        } else {
            if (BCrypt.checkpw(password, account.getPassword())) {
                StpUtil.login(username);

                return Result.success();
            } else {
                return Result.error(ResultCode.ERROR); //密码不正确
            }
        }
   }
//
//    @GetMapping("/accountInfo")
//    public Mono<Result> accountInfo(ServerWebExchange webExchange) {
//        String token = webExchange.getRequest().getHeaders().getFirst("Authorization");
//        if (token == null) {
//            webExchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            return Mono.just(Result.error(ResultCode.ERROR));
//        }
//        return accountService.findAccountByUsername("usernaem")
//                .map(Result::success)
//                .switchIfEmpty(Mono.just(Result.error(ResultCode.ERROR)));
//    }
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
