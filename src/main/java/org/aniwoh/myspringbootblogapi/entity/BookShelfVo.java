package org.aniwoh.myspringbootblogapi.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class BookShelfVo extends BookShelf{
    private Chapter lastChapter;
    private Chapter processChapter;
}
