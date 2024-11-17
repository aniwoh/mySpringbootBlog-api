package org.aniwoh.myspringbootblogapi.entity;

import lombok.Data;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;

@Data
public class ChapterListVo {
    String id;
    String title;
    Integer index;
}
