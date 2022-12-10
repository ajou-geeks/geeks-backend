create table tbl_note
(
    -- primary key
    id              bigint unsigned                             not null auto_increment primary key,

    -- fk columns
    room_id         bigint unsigned                             not null,
    sender_id       bigint unsigned                             not null,
    receiver_id     bigint unsigned                             not null,

    -- columns
    content         text                                        not null,

    -- common columns
    created_by      bigint unsigned                                 null,
    created_at      timestamp       default current_timestamp       null,
    updated_by      bigint unsigned                                 null,
    updated_at      timestamp       default current_timestamp       null on update current_timestamp,
    is_deleted      boolean         default false                   null,
    deleted_at      timestamp                                       null,

    -- constraints
    foreign key (room_id) references tbl_room (id),
    foreign key (sender_id) references tbl_user (id),
    foreign key (receiver_id) references tbl_user (id)

    ) engine = InnoDB
    default charset = utf8mb4
    collate = utf8mb4_unicode_ci
    comment = 'note table';
