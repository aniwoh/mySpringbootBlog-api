package org.aniwoh.myspringbootblogapi.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection="me_article_tags")
public class ArticleTag {
    private String id; //主键
    private String articleId;
    private String tagId;

    public ArticleTag(String articleId,String tagId){
        this.articleId = articleId;
        this.tagId = tagId;
    }
}
