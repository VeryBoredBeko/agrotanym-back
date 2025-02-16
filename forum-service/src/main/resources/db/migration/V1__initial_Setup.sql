create schema forum;

create table forum.posts (
    id bigserial primary key,
    user_id uuid not null,
    title varchar(255) not null,
    content text not null,
    created_at timestamp default current_timestamp not null,
    updated_at timestamp null
);

create table forum.comments (
    id bigserial primary key,
    user_id uuid not null,
    post_id bigint not null references forum.posts(id),
    content text not null,
    created_at timestamp default current_timestamp not null,
    updated_at timestamp null
);

create table forum.questions (
    id bigserial primary key,
    user_id uuid not null,
    title varchar(255) not null,
    description text not null,
    created_at timestamp default current_timestamp not null,
    updated_at timestamp null
);

create table forum.answers (
    id bigserial primary key,
    user_id uuid not null,
    question_id bigint not null references forum.questions(id),
    content text not null,
    created_at timestamp default current_timestamp not null,
    updated_at timestamp null
);


