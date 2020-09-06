## How to run mysql

```
docker-compose up
docker-compose start
docker exec -it pm22_db_1 bash
```

## How to create danabase

```
mysql -u root -p'example' -P 3306 -e "create database service_db"
```

## How to create tables

```
mysql -u root -p'example' -D service_db -P 3306 < create_tables.sql
```
