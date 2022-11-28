create table tbl_member
(
    -- primary key
    id              bigint unsigned                             not null auto_increment primary key,

    -- columns
    email           varchar(300)                                not null,
    password        varchar(300)                                not null,
    profile_image   varchar(255)                                    null,
    filename        varchar(300)                                    null,
    dormitory       varchar(200)                                    null,
    ho              varchar(20)                                     null,
    detail          varchar(1000)                                   null,
    pattern         varchar(100)                                    null,
    pattern_detail  varchar(500)                                    null

    -- common columns
--    created_by      bigint unsigned                             not null,
--    created_at      timestamp       default current_timestamp   not null,
--    updated_by      bigint unsigned                             not null,
--    updated_at      timestamp       default current_timestamp   not null on update current_timestamp,
--    deleted         boolean         default false               not null,
--    deleted_at      timestamp                                       null

    ) engine = InnoDB
    default charset = utf8mb4
    collate = utf8mb4_unicode_ci
    comment = 'member table';
