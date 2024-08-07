package org.aniwoh.myspringbootblogapi.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "counters")
@Data
public class Counter {
    @Id
    private String id;
    private String name;
    private Integer seq;

    // Getters and Setters
}