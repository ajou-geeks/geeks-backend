create table tbl_notice
(
    -- primary key
    id              bigint unsigned                             not null auto_increment primary key,

    -- fk columns
    user_id         bigint unsigned                             not null,

    -- columns
    title           varchar(100)                                not null,
    content         varchar(100)                                    null,
    type            varchar(50)                                 not null,
    value1          varchar(100)                                    null,
    value2          varchar(100)                                    null,

    -- common columns
    created_by      bigint unsigned                                 null,
    created_at      timestamp       default current_timestamp       null,
    updated_by      bigint unsigned                                 null,
    updated_at      timestamp       default current_timestamp       null on update current_timestamp,
    is_deleted      boolean         default false                   null,
    deleted_at      timestamp                                       null,

    -- constraints
    foreign key (user_id) references tbl_user (id)

    ) engine = InnoDB
    default charset = utf8mb4
    collate = utf8mb4_unicode_ci
    comment = 'notice table';
