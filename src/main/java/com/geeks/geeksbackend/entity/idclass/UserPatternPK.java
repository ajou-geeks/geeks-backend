package com.geeks.geeksbackend.entity.idclass;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserPatternPK implements Serializable {
    private long id;
    private String characteristic;
}
