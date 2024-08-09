package org.aniwoh.myspringbootblogapi.controller;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.aniwoh.myspringbootblogapi.config.JwtConfig.TokenProvider;
import org.aniwoh.myspringbootblogapi.entity.Account;
import org.aniwoh.myspringbootblogapi.entity.Result;
import org.aniwoh.myspringbootblogapi.entity.ResultCode;
import org.aniwoh.myspringbootblogapi.service.AccountService;
import org.aniwoh.myspringbootblogapi.service.impl.UserDetailsService;
import org.aniwoh.myspringbootblogapi.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.aniwoh.myspringbootblogapi.vo.LoginRequest;
import org.hibernate.validator.constraints.URL;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    TokenProvider tokenProvider;
    @Resource
    UserDetailsService userDetailsService;

    @PostMapping("/register")
    public Mono<Result> register(@Pattern(regexp = "^\\S{1,10}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password){
        //查询用户
        return accountService.findAccountByUsername(username)
                .flatMap(account -> {
                    log.error("用户:" + username + "注册失败,用户名已占用");
                    return Mono.just(Result.error(ResultCode.ERROR, "用户名已占用"));
                })
                .switchIfEmpty(accountService.addUser(username, password)
                        .doOnSuccess(account -> log.info("用户:" + username + "注册成功"))
                        .then(Mono.just(Result.success())));
    }

    @PostMapping("/login")
    public Mono<Result> Login(@RequestBody LoginRequest loginRequest){
        //查询用户
        return userDetailsService.findByUsername(loginRequest.getUsername())
                .filter(u -> passwordEncoder.matches(loginRequest.getPassword(), u.getPassword()))
                .map(userDetails -> Result.success(tokenProvider.generateToken(userDetails)))
                .switchIfEmpty(Mono.just(Result.error(ResultCode.ERROR, "用户不存在")));
    }
    @GetMapping("/accountInfo")
    public Mono<Result> accountInfo(@RequestHeader(name = "Authorization") String token){
        Map<String, Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");

        return accountService.findAccountByUsername(username)
                .map(account -> Result.success(account))
                .switchIfEmpty(Mono.just(Result.error(ResultCode.ERROR)));
    }
    @PutMapping("/update")
    public Mono<Result> update(@RequestBody Account account) {
        return accountService.update(account)
                .then(Mono.just(Result.success()));
    }

    @PatchMapping("/updateAvatar")
    public Mono<Result> updateAvatar(@RequestParam @URL String avatarUrl) {
        // Assume a fixed UID for simplicity; replace with actual logic
        return accountService.updateAvatar(avatarUrl, 1)
                .then(Mono.just(Result.success()));
    }

    @PatchMapping("/updatePassword")
    public Mono<Result> updatePwd(@RequestBody Map<String, String> params) {
        String oldPwd = params.get("old_pwd");
        String newPwd = params.get("new_pwd");
        String rePwd = params.get("re_pwd");

        if (!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)) {
            return Mono.just(Result.error(ResultCode.ERROR, "参数不完整"));
        }

        Map<String, Object> map = ThreadLocalUtil.get();
        String username = map.get("username").toString();
        return accountService.findAccountByUsername(username)
                .flatMap(account -> {
                    Digester md5 = new Digester(DigestAlgorithm.MD5);
                    if (!account.getPassword().equals(md5.digestHex(oldPwd))) {
                        return Mono.just(Result.error(ResultCode.ERROR, "原密码不正确"));
                    }

                    if (!newPwd.equals(rePwd)) {
                        return Mono.just(Result.error(ResultCode.ERROR, "两次密码不一致"));
                    }

                    return accountService.updatePwd(md5.digestHex(newPwd), 1)
                            .then(Mono.just(Result.success()));
                })
                .switchIfEmpty(Mono.just(Result.error(ResultCode.ERROR, "用户不存在")));
    }
}
