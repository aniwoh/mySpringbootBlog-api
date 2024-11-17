package org.aniwoh.myspringbootblogapi.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;

@EqualsAndHashCode(callSuper = true)
@Data
public class BookShelfVo extends BookShelf{
    private Chapter lastChapter;
    private Chapter processChapter;
    private Integer chapterCount;
}
