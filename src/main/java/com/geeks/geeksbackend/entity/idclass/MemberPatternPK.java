package com.geeks.geeksbackend.entity.idclass;

import lombok.Data;

import java.io.Serializable;

@Data
public class MemberPatternPK implements Serializable {
    private long id;
    private String characteristic;
}