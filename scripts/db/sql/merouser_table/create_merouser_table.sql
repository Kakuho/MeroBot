create table merouser(
  user_id       varchar primary key,
  ignored       boolean default false,
  admin_ignored boolean default false,
  admin_id      varchar,
  date_added    timestamp default LOCALTIMESTAMP(0)
);
