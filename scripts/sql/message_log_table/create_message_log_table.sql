create table message_log(
  author_id   varchar,
  message_id  varchar,
  date_added  timestamp default LOCALTIMESTAMP(0),

  primary key(author_id, message_id)
);
