package org.aniwoh.myspringbootblogapi.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "me_novelChapter")
public class Chapter {
    @Id
    private String id; //章节id
    private String title; //章节标题
    private String content; //章节内容
    private String novelId; //所属的小说id
    private Integer index; //小说章节的顺序
    @JSONField(format = "yyyy.MM.dd HH:mm")
    private LocalDateTime createDate;
    @JSONField(format = "yyyy.MM.dd HH:mm")
    private LocalDateTime updateDate;
}
