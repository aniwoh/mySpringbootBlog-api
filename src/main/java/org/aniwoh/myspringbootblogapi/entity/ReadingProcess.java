package org.aniwoh.myspringbootblogapi.entity;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "me_reading_process")
public class ReadingProcess {
    @Id
    private String id;
    private String novelId;
    private String chapterId;
    private Integer paragraphIndex;
    @JSONField(format = "yyyy.MM.dd HH:mm")
    private LocalDateTime updateDate;
}
