This is a simple chat.

## Setup

### Database setup

Install PostgreSQL. Create table. Create user, like "chat_user". Configure password for the user.

```
ALTER USER chat_user with encrypted password "chat_user_passwd';
```

Add a line in pg_hba.config

```
local all chat_user md5
```

Set permission.

```
GRANT ALL PRIVILEGES ON TABLE posts TO chat_user;
GRANT ALL PRIVILEGES ON sequence posts_post_id_seq TO chat_user;
```

Add a line in .env

```
DATABASE_URL=postgres://chat_user:chat_user_passwd@localhost/chat
```

