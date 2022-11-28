create table tbl_member_pattern
(
    -- primary key
    id              bigint unsigned                             not null,

    -- columns
    characteristic  varchar(300)                                not null,

    -- common columns
--    created_by      bigint unsigned                             not null,
--    created_at      timestamp       default current_timestamp   not null,
--    updated_by      bigint unsigned                             not null,
--    updated_at      timestamp       default current_timestamp   not null on update current_timestamp,
--    deleted_by      bigint unsigned                                 null,
--    deleted_at      timestamp                                       null
    primary key(id, characteristic)
    ) engine = InnoDB
    default charset = utf8mb4
    collate = utf8mb4_unicode_ci
    comment = 'member table';
