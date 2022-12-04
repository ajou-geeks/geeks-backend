create table tbl_taxi_user
(
    -- primary key
    id              bigint unsigned                             not null auto_increment primary key,

    -- fk columns
    taxi_id         bigint unsigned                             not null,
    user_id         bigint unsigned                             not null,

    -- columns
    email           varchar(300)                                not null,

    -- constraints
    foreign key (taxi_id) references tbl_taxi (id),
    foreign key (user_id) references tbl_user (id)

    ) engine = InnoDB
    default charset = utf8mb4
    collate = utf8mb4_unicode_ci
    comment = 'taxi user table';
