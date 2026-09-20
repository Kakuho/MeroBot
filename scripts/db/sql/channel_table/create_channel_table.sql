create table channel(
  channel_id  varchar primary key,
  ignored     boolean default false,
  date_added  timestamp default LOCALTIMESTAMP(0)
);
