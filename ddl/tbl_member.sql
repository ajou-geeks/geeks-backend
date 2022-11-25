create table tbl_member
(
    -- primary key
    id              bigint unsigned                             not null auto_increment primary key,

    -- columns
    name            varchar(50)                                 not null,
    nickname        varchar(20)                                     null,
    email           varchar(255)                                not null,
    birthday        date                                            null,
    profile_image   varchar(255)                                    null,
    bio             varchar(255)                                    null,
    dormitory       varchar(20)                                     null,

    -- common columns
    created_by      bigint unsigned                             not null,
    created_at      timestamp       default current_timestamp   not null,
    updated_by      bigint unsigned                             not null,
    updated_at      timestamp       default current_timestamp   not null on update current_timestamp,
    deleted_by      bigint unsigned                                 null,
    deleted_at      timestamp                                       null

    ) engine = InnoDB
    default charset = utf8mb4
    collate = utf8mb4_unicode_ci
    comment = 'member table';
