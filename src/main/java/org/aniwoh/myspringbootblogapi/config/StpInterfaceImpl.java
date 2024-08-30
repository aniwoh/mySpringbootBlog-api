package org.aniwoh.myspringbootblogapi.config;

import cn.dev33.satoken.stp.StpInterface;
import jakarta.annotation.Resource;
import org.aniwoh.myspringbootblogapi.entity.Account;
import org.aniwoh.myspringbootblogapi.service.AccountService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 自定义权限加载接口实现类
 */
@Component    // 保证此类被 SpringBoot 扫描，完成 Sa-Token 的自定义权限验证扩展
public class StpInterfaceImpl implements StpInterface {
    @Resource
    private AccountService accountService;

    /**
     * 返回一个账号所拥有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        //loginId为登录时写入的值
        //按level赋予不同的角色
        Account account = accountService.findAccountByUsername((String) loginId);
        int level = account.getLevel();
        List<String> list = new ArrayList<String>();
        if (level == 0){
            list.add("guest.*");
        } else if (level >= 7){
            list.add("guest.*");
            list.add("user.*");
            list.add("admin.*");
        } else {
            list.add("guest.*");
            list.add("user.*");
        }
        return list;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        Account account = accountService.findAccountByUsername((String) loginId);
        int level = account.getLevel();
        List<String> list = new ArrayList<String>();
        if (level == 0){
            list.add("guest");
        } else if (level >= 7){
            list.add("guest");
            list.add("user");
            list.add("admin");
        } else {
            list.add("guest");
            list.add("user");
        }
        return list;
    }

}
