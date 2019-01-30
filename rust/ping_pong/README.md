This is a small ping pong game.

## Setup

### Redis setup

Do install Redis.

### Nginx setup

Install Nginx. Add setting.


```
http {
    ... ...

    map $http_upgrade $connection_upgrade {
        default upgrade;
        '' close;
    }

    server {
        ... ...

        location /ws/ {
            proxy_pass http://localhost:8001;
            proxy_http_version 1.1;
            proxy_set_header Upgrade $http_pugrade;
            proxy_set_header Connection $connection_upgrade;
        }
```

