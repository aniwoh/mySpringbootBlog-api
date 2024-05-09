package org.aniwoh.myspringbootblogapi.entity;

import lombok.Data;

@Data
public class ArticleTag {
    private Integer id; //主键
    private Integer articleId;
    private Integer tagId;
}
