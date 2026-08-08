create table ignored_user(
  user_id     varchar primary key,
  ignored     boolean default false,
  date_added  timestamp default LOCALTIMESTAMP(0)
);
