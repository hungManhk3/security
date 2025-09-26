-- Tạo bảng users
create table if not exists users (
  id           bigserial primary key,
  username     varchar(64) not null unique,
  password     varchar(255) not null,
  full_name    varchar(128),
  enabled      boolean not null default true,
  created_at   timestamp not null default now()
);

-- Tạo bảng roles
create table if not exists roles (
  id    bigserial primary key,
  name  varchar(64) not null unique
);

-- Bảng liên kết users <-> roles
create table if not exists user_roles (
  user_id  bigint not null references users(id) on delete cascade,
  role_id  bigint not null references roles(id) on delete cascade,
  primary key (user_id, role_id)
);

-- ===============================
-- Seed data mẫu
-- ===============================

-- Roles
insert into roles (name) values ('ROLE_ADMIN')
  on conflict (name) do nothing;
insert into roles (name) values ('ROLE_USER')
  on conflict (name) do nothing;

-- Users (password đã được BCrypt hash)
-- Ví dụ hash cho chuỗi "password" = $2a$10$Dow1SZQxPRX9f4mT...
insert into users (username, password, full_name, enabled)
values ('admin', '$2a$10$wMeZiuZlodt0MgkYWjqXR.6hHAMucCN31v/17P9XCMS6JEW/vyGD6', 'admin', true)
on conflict (username) do nothing;

insert into users (username, password, full_name, enabled)
values ('user', '$2a$10$HBOACsMkRJqDUug6XM0T8uwJFAJcP59hr9.MRADEJeDnXvx1jYN.C', 'user', true)
on conflict (username) do nothing;

-- Gán role cho users
insert into user_roles (user_id, role_id)
select u.id, r.id
from users u, roles r
where u.username = 'admin' and r.name = 'ROLE_ADMIN'
on conflict do nothing;

insert into user_roles (user_id, role_id)
select u.id, r.id
from users u, roles r
where u.username = 'user' and r.name = 'ROLE_USER'
on conflict do nothing;
