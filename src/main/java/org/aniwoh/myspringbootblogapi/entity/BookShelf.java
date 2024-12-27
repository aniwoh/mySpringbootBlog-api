package org.aniwoh.myspringbootblogapi.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "me_bookshelf")
public class BookShelf {
    @Id
    private String id;
    private String title;
    private String author;
    private String filePath;
    private String status; //解析状态（e.g., "UPLOADED", "PARSING", "COMPLETED", "FAILED"）
    @JSONField(format = "yyyy.MM.dd HH:mm")
    private LocalDateTime createDate;
    @JSONField(format = "yyyy.MM.dd HH:mm")
    private LocalDateTime updateDate;
}
