package org.aniwoh.myspringbootblogapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Account {
    private Integer uid;
    private String username;
    @JsonIgnore //返回时忽略下列属性
    private String password;
    private Integer level;
    private String userPic; //用户头像地址
    private LocalDateTime createTime; //创建时间
    private Integer status; //用户的账户状态，默认为0
}
