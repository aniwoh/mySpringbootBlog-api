package org.aniwoh.myspringbootblogapi.vo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ArticleVo {
    private Integer id;
    private String title;
    private String author;
    private String body;
    @JSONField(format = "yyyy.MM.dd HH:mm")
    private LocalDateTime createDate;
    private Integer viewCounts;
    private Integer commentCounts;
    private Integer thumbsUpCounts;
    private List<String> tagNames;
}
