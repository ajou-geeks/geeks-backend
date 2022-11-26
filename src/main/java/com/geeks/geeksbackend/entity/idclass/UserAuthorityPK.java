package com.geeks.geeksbackend.entity.idclass;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserAuthorityPK implements Serializable {
    private long id;
    private String authority_name;
}
