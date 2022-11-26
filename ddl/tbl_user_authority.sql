create table tbl_user_authority
(
    id              bigint unsigned                             ,
    -- columns
    authority_name       varchar(100)                                 not null

    ) engine = InnoDB
    default charset = utf8mb4
    collate = utf8mb4_unicode_ci
    comment = 'user_authority table';
