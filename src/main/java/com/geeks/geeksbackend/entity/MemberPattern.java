package com.geeks.geeksbackend.entity;

import com.geeks.geeksbackend.entity.idclass.MemberPatternPK;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tbl_member_pattern")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@IdClass(MemberPatternPK.class)
public class MemberPattern {
    @Id
    private long id;
    @Id
    private String characteristic;
}
