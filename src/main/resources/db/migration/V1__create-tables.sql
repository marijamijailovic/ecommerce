create table if not exists users (
  id uuid default uuid_generate_v4(),
  created timestamp default localtimestamp not null,
  email text not null,
  constraint users_pk primary key (id)
);

create table if not exists products (
  id uuid default uuid_generate_v4(),
  created timestamp default localtimestamp not null,
  category text not null,
  name text not null,
  price numeric(10,3) not null,
  updated timestamp default localtimestamp not null,
  constraint products_pk primary key (id)
);

create table if not exists orders (
  id uuid not null,
  created timestamp default localtimestamp not null,
  users_id uuid not null,
  constraint orders_pk primary key (id),
  constraint fk_users_id foreign key (users_id) references users
);

create table if not exists orders_products (
  orders_id uuid not null,
  products_id uuid not null,
  products_price numeric(10,3) not null,
  constraint orders_products_pk primary key (orders_id, products_id),
  constraint fk_products_id foreign key (products_id) references products,
  constraint fk_orders_id foreign key (orders_id) references orders
);