create table tbl_product_user
(
    -- primary key
    id              bigint unsigned                             not null auto_increment primary key,

    -- fk columns
    product_id      bigint unsigned                             not null,
    user_id         bigint unsigned                             not null,

    -- columns
    type            varchar(10)                                 not null,

    -- common columns
    created_by      bigint unsigned                             not null,
    created_at      timestamp       default current_timestamp   not null,
    updated_by      bigint unsigned                             not null,
    updated_at      timestamp       default current_timestamp   not null on update current_timestamp,
    is_deleted      boolean         default false               not null,
    deleted_at      timestamp                                       null,

    -- constraints
    foreign key (product_id) references tbl_product (id),
    foreign key (user_id) references tbl_user (id)

    ) engine = InnoDB
    default charset = utf8mb4
    collate = utf8mb4_unicode_ci
    comment = 'product user table';
