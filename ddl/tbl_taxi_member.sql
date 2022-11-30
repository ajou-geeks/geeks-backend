create table tbl_taxi_member
(
    -- primary key
    id              bigint unsigned                             not null auto_increment primary key,

    -- columns
    taxi_id         bigint unsigned                             not null,
    user_id         bigint unsigned                             not null,
    email           varchar(300)                                not null
    ) engine = InnoDB
    default charset = utf8mb4
    collate = utf8mb4_unicode_ci
    comment = 'taxi member table';
