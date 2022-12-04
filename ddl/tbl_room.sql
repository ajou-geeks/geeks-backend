create table tbl_room
(
    -- primary key
    id              bigint unsigned                             not null auto_increment primary key,

    -- fk columns
    inviter_id      bigint unsigned                             not null,
    invitee_id      bigint unsigned                             not null,

    -- common columns
    created_by      bigint unsigned                                 null,
    created_at      timestamp       default current_timestamp       null,
    updated_by      bigint unsigned                                 null,
    updated_at      timestamp       default current_timestamp       null on update current_timestamp,
    deleted         boolean         default false                   null,
    deleted_at      timestamp                                       null,

    -- constraints
    foreign key (inviter_id) references tbl_user (id),
    foreign key (invitee_id) references tbl_user (id)

    ) engine = InnoDB
    default charset = utf8mb4
    collate = utf8mb4_unicode_ci
    comment = 'room table';
