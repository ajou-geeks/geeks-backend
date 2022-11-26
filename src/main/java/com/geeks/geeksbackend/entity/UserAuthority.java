package com.geeks.geeksbackend.entity;

import com.geeks.geeksbackend.entity.idclass.UserAuthorityPK;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_user_authority")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(UserAuthorityPK.class)
public class UserAuthority {
    @Id
    private long id;
    @Id
    private String authority_name;
}
