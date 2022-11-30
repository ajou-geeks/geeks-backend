create table tbl_taxi
(
    -- primary key
    id              bigint unsigned                             not null auto_increment primary key,

    -- columns
    user_id         bigint unsigned                             not null,
    price           int                                         not null,
    start_time      timestamp                                   not null,
    end_time        timestamp                                   not null,
    max_participant int                                         not null,
    source          varchar(200)                                not null,
    destination     varchar(200)                                not null,
    status          varchar(20)                                 not null,

    -- common columns
    created_by      bigint unsigned                             not null,
    created_at      timestamp       default current_timestamp   not null,
    updated_by      bigint unsigned                             not null,
    updated_at      timestamp       default current_timestamp   not null on update current_timestamp,
    deleted         boolean         default false               not null,
    deleted_at      timestamp                                       null

    ) engine = InnoDB
    default charset = utf8mb4
    collate = utf8mb4_unicode_ci
    comment = 'taxi table';
