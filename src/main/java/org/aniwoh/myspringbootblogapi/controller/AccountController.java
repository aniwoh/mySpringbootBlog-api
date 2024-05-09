package org.aniwoh.myspringbootblogapi.controller;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.jwt.JWTUtil;
import org.aniwoh.myspringbootblogapi.entity.Account;
import org.aniwoh.myspringbootblogapi.entity.Result;
import org.aniwoh.myspringbootblogapi.service.AccountService;
import org.aniwoh.myspringbootblogapi.utils.ThreadLocalUtil;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/user")
@Validated
@Slf4j
public class AccountController {
    @Autowired
    private AccountService accountService;
    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{1,10}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password){
        //查询用户
        Account account = accountService.findByUsername(username);
        if (account == null ){
            //用户名没有占用
            //注册用户
            accountService.register(username,password);
            log.info("用户:"+username+"注册成功");
            return Result.success();
        }else {
            log.error("用户:"+username+"注册失败,用户名已占用");
            return Result.error("用户名已占用");
        }
    }

    @PostMapping("/login")
    public Result Login(@Pattern(regexp = "^\\S{1,10}$") String username,@Pattern(regexp = "^\\S{5,16}$") String password){
        //查询用户
        Account account = accountService.findByUsername(username);
        if (account == null){
            // 没有找到用户，用户名不存在
            log.info("登陆失败，用户不存在");
            return Result.error("用户不存在");
        }else {
            Digester md5 = new Digester(DigestAlgorithm.MD5);
            if (Objects.equals(md5.digestHex(password), account.getPassword())){
                // 密码正确
                log.info("登录成功");
                Map<String, Object> map = new HashMap<>();
                map.put("uid", account.getUid());
                map.put("username", account.getUsername());
                String token=JWTUtil.createToken(map, "aniwoh".getBytes());
                return Result.success(token);
            }else {
                log.info("登陆失败，密码不正确");
                return Result.error("密码不正确");
            }
        }
    }
    @GetMapping("/accountInfo")
    public Result<Account> accountInfo(@RequestHeader(name = "Authorization") String token){
        Map<String,Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");

        Account account = accountService.findByUsername(username);
        return Result.success(account);
    }
    @PutMapping("/update")
    public Result update(@RequestBody Account account) {
        accountService.update(account);
        return Result.success();
    }

    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl){
        accountService.updateAvatar(avatarUrl);
        return Result.success();
    }

    @PatchMapping("/updatePassword")
    public Result updatePwd(@RequestBody Map<String,String> params){
        //1.参数校验
        String oldPwd=params.get("old_pwd");
        String newPwd=params.get("new_pwd");
        String rePwd=params.get("re_pwd");

        if (!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)){
            return Result.error("参数不完整");
        }
        //校验原密码
        Map<String,Object> map = ThreadLocalUtil.get();
        String username = map.get("username").toString();
        log.info(username);
        Account account = accountService.findByUsername(username);
        Digester md5 = new Digester(DigestAlgorithm.MD5);
        if (!account.getPassword().equals(md5.digestHex(oldPwd))){
            return Result.error("原密码不正确");
        }

        //校验新密码
        if (!newPwd.equals(rePwd)){
            return Result.error("两次密码不一致");
        }
        //2.修改密码
        accountService.updatePwd(md5.digestHex(newPwd));
        return Result.success();
    }
}
