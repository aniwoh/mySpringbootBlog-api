package org.aniwoh.myspringbootblogapi.entity;

import lombok.Data;

@Data
public class TxtRule {
    Integer id;
    Boolean enable;
    String name;
    String rule;
    String serialNumber;
}
