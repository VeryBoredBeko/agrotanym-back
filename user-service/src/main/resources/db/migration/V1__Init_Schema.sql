create schema farm;

create table farm.cultures (
    id bigserial primary key,
    name varchar(255) not null,
    created_at timestamp default current_timestamp not null,
    updated_at timestamp null
);

create table farm.workers (
    user_id uuid primary key,
    created_at timestamp default current_timestamp not null,
    updated_at timestamp
);

create table farm.fields (
    id bigserial primary key,
    user_id uuid not null,
    name varchar(255) not null,
    info text not null,
    culture_id bigint references farm.cultures(id),
    created_at timestamp default current_timestamp not null,
    updated_at timestamp
);

create table farm.field_worker_links (
    field_id bigint not null references farm.fields(id) on delete cascade,
    worker_id uuid not null references farm.workers(user_id) on delete cascade,
    primary key (field_id, worker_id)
);

create table farm.actions {
    id bigserial primary key,
    user_id uuid not null,
    field_id bigint references farm.fields(id),
    description text not null,
    created_at timestamp default current_timestamp not null,
    updated_at timestamp
}


