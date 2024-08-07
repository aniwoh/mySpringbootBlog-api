package org.aniwoh.myspringbootblogapi.controller;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.jwt.JWTUtil;
import jakarta.annotation.Resource;
import org.aniwoh.myspringbootblogapi.entity.Account;
import org.aniwoh.myspringbootblogapi.entity.Result;
import org.aniwoh.myspringbootblogapi.entity.ResultCode;
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
import java.util.Optional;

@RestController
@RequestMapping("/user")
@Validated
@Slf4j
public class AccountController {
    @Resource
    private AccountService accountService;
    @PostMapping("/register")
    public Result register(@Pattern(regexp = "^\\S{1,10}$") String username, @Pattern(regexp = "^\\S{5,16}$") String password){
        //查询用户
        Optional<Account> account = accountService.findAccountByUsername(username);
        if (account.isEmpty()){
            //用户名没有占用
            //注册用户
            accountService.addUser(username,password);
            log.info("用户:"+username+"注册成功");
            return Result.success();
        }else {
            log.error("用户:"+username+"注册失败,用户名已占用");
            return Result.error(ResultCode.ERROR,"用户名已占用");
        }
    }

    @PostMapping("/login")
    public Result Login(@Pattern(regexp = "^\\S{1,10}$") String username,@Pattern(regexp = "^\\S{5,16}$") String password){
        //查询用户
        Optional<Account> account = accountService.findAccountByUsername(username);
        if (account.isEmpty()){
            // 没有找到用户，用户名不存在
            log.info("登陆失败，用户不存在");
            return Result.error(ResultCode.ERROR,"用户不存在");
        }else {
            Digester md5 = new Digester(DigestAlgorithm.MD5);
            if (Objects.equals(md5.digestHex(password), account.get().getPassword())){
                // 密码正确
                log.info("登录成功");
                Map<String, Object> map = new HashMap<>();
                map.put("uid", account.get().getUid());
                map.put("username", account.get().getUsername());
                String token=JWTUtil.createToken(map, "aniwoh".getBytes());
                return Result.success(token);
            }else {
                log.info("登陆失败，密码不正确");
                return Result.error(ResultCode.ERROR,"密码不正确");
            }
        }
    }
    @GetMapping("/accountInfo")
    public Result accountInfo(@RequestHeader(name = "Authorization") String token){
        Map<String,Object> map = ThreadLocalUtil.get();
        String username = (String) map.get("username");

        Optional<Account> account = accountService.findAccountByUsername(username);
        if (account.isEmpty()){
            return Result.error(ResultCode.ERROR);
        }
        return Result.success(account);
    }
    @PutMapping("/update")
    public Result update(@RequestBody Account account) {
        accountService.update(account);
        return Result.success();
    }

    @PatchMapping("/updateAvatar")
    public Result updateAvatar(@RequestParam @URL String avatarUrl){
        accountService.updateAvatar(avatarUrl,1);
        return Result.success();
    }

    @PatchMapping("/updatePassword")
    public Result updatePwd(@RequestBody Map<String,String> params){
        //1.参数校验
        String oldPwd=params.get("old_pwd");
        String newPwd=params.get("new_pwd");
        String rePwd=params.get("re_pwd");

        if (!StringUtils.hasLength(oldPwd) || !StringUtils.hasLength(newPwd) || !StringUtils.hasLength(rePwd)){
            return Result.error(ResultCode.ERROR,"参数不完整");
        }
        //校验原密码
        Map<String,Object> map = ThreadLocalUtil.get();
        String username = map.get("username").toString();
        log.info(username);
        Optional<Account> account = accountService.findAccountByUsername(username);
        Digester md5 = new Digester(DigestAlgorithm.MD5);
        if (!account.get().getPassword().equals(md5.digestHex(oldPwd))){
            return Result.error(ResultCode.ERROR,"原密码不正确");
        }

        //校验新密码
        if (!newPwd.equals(rePwd)){
            return Result.error(ResultCode.ERROR,"两次密码不一致");
        }
        //2.修改密码
        accountService.updatePwd(md5.digestHex(newPwd),1);
        return Result.success();
    }
}
