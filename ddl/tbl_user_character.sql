create table tbl_user_character
(
    -- primary key
    id              bigint unsigned                             not null,

    -- columns
    type            varchar(300)                                not null,

    -- common columns
    created_by      bigint unsigned                             not null,
    created_at      timestamp       default current_timestamp   not null,
    updated_by      bigint unsigned                             not null,
    updated_at      timestamp       default current_timestamp   not null on update current_timestamp,
    is_deleted      boolean                                         null,
    deleted_at      timestamp                                       null,

    -- constraints
    primary key (id, type),
    foreign key (id) references tbl_user (id)

    ) engine = InnoDB
    default charset = utf8mb4
    collate = utf8mb4_unicode_ci
    comment = 'user character table';
