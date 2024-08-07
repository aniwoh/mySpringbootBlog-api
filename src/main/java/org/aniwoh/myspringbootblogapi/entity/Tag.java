package org.aniwoh.myspringbootblogapi.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "me_tags")
public class Tag {
    @Id
    private String id;
    private String tagName;
}
