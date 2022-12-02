package com.geeks.geeksbackend.entity;

import com.geeks.geeksbackend.entity.idclass.UserPatternPK;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tbl_user_pattern")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(UserPatternPK.class)
public class UserPattern {
    @Id
    private long id;
    @Id
    private String characteristic;
}
