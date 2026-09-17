create table role(
  id          serial,
  value       varchar,
  is_admin    boolean default false,
  date_added  timestamp default LOCALTIMESTAMP(0),

  primary key(id, value)
);
