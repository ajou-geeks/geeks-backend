create table tbl_product
(
    -- primary key
    id              bigint unsigned                             not null auto_increment primary key,

    -- columns
    name            varchar(50)                                 not null,
    type1           varchar(20)                                 not null,
    price           int                                         not null,
    start_time      timestamp       default current_timestamp   not null,
    end_time        timestamp                                       null,
    max_participant int                                         not null,
    destination     varchar(10)                                     null,
    thumbnail_url   varchar(255)                                    null,
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
    comment = 'product table';
