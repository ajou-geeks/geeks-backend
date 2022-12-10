create table tbl_delivery
(
    -- primary key
    id              bigint unsigned                             not null auto_increment primary key,

    -- fk columns
    user_id         bigint unsigned                             not null,

    -- columns
    name            varchar(50)                                 not null,
    type1           varchar(20)                                 not null,
    min_amount      int                                         not null,
    start_time      timestamp       default current_timestamp   not null,
    end_time        timestamp                                       null,
    destination     varchar(10)                                     null,
    thumbnail_url   varchar(255)                                    null,
    bank_name       varchar(20)                                     null,
    account_number  varchar(20)                                     null,
    total_amount    int             default 0                       null,
    pickup_location varchar(20)                                     null,
    pickup_datetime timestamp                                       null,
    status          varchar(20)                                 not null,

    -- common columns
    created_by      bigint unsigned                             not null,
    created_at      timestamp       default current_timestamp   not null,
    updated_by      bigint unsigned                             not null,
    updated_at      timestamp       default current_timestamp   not null on update current_timestamp,
    is_deleted      boolean         default false               not null,
    deleted_at      timestamp                                       null,

    -- constraints
    foreign key (user_id) references tbl_user (id)

    ) engine = InnoDB
    default charset = utf8mb4
    collate = utf8mb4_unicode_ci
    comment = 'delivery table';
