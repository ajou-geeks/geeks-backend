package com.geeks.geeksbackend.entity;

import com.geeks.geeksbackend.entity.BaseEntity;
import com.geeks.geeksbackend.entity.Product;
import com.geeks.geeksbackend.entity.User;
import com.geeks.geeksbackend.enumeration.CoBuyUserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Table(name = "tbl_product_user")
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ProductUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private CoBuyUserType type;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
