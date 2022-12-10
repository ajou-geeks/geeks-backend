create table tbl_user_authority
(
    -- primary key
    id                  bigint unsigned                             not null primary key,

    -- columns
    authority_name      varchar(100)                                not null,

    -- constraints
    foreign key (id) references tbl_user (id)

    ) engine = InnoDB
    default charset = utf8mb4
    collate = utf8mb4_unicode_ci
    comment = 'user authority table';
