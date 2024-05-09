package org.aniwoh.myspringbootblogapi.mapper;

import org.aniwoh.myspringbootblogapi.entity.Account;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AccountMapper {
    @Select("select * from me_account where uid=#{uid}")
    Account findUserByUid(Integer uid);

    @Select("select * from me_account where username=#{username}")
    Account findUserByUsername(String username);

    @Insert("insert into me_account(username,password,create_time) values (#{username},#{password},now())")
    void addUser(@Param("username") String username, @Param("password") String password); // 多参数时需要加上Param注解

    @Update("update me_account set username=#{username} where uid=#{uid}")
    void update(Account account);

    @Update("update me_account set user_pic=#{avatarUrl} where uid=#{uid}")
    void updateAvatar(String avatarUrl,Integer uid);

    @Update("update me_account set password=#{newPwd} where uid=#{uid}")
    void updatePwd(String newPwd,Integer uid);
}
