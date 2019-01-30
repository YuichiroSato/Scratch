CREATE TABLE posts (
  post_id SERIAL PRIMARY KEY,
  user_name text NOT null DEFAULT '',
  body text NOT null DEFAULT '',
  posted_time timestamp NOT null DEFAULT CURRENT_TIMESTAMP,
  is_deleted boolean NOT null DEFAULT 'f'
);
