package org.aniwoh.myspringbootblogapi.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "me_article")
public class Article {
    @Id
    private String id;
    private String title;
    private String author;
    private String body;
    @JSONField(format = "yyyy.MM.dd HH:mm")
    private LocalDateTime createDate;
    private Integer viewCounts;
    private Integer commentCounts;
    private Integer thumbsUpCounts;

    public Article(){
        this.viewCounts = 0;
        this.commentCounts=0;
        this.thumbsUpCounts=0;
    }
}
