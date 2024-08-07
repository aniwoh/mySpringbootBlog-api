package org.aniwoh.myspringbootblogapi.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "me_account")
public class Account {
    @Id
    private String id;
    private Integer uid; //自增字段
    private String username;
    @JsonIgnore //返回时忽略下列属性
    private String password;
    private Integer level; //默认为0
    private String userPic; //用户头像地址
    private LocalDateTime createTime; //创建时间
    private Integer status; //用户的账户状态，默认为0

    public Account(){
        this.level = 0;
        this.createTime = LocalDateTime.now();
        this.status = 0;
    }

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
        this.level = 0;
        this.createTime = LocalDateTime.now();
        this.status = 0;
    }
}
