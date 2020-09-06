## How to run mysql

```
docker-compose start
docker exec -it pm21_db_1 bash
```

## How to create danabase

```
mysql -u root -p'example' -P 3306 -e "create database bank_db"
```

## How to create tables

```
mysql -u root -p'example' -D bank_db -P 3306 < create_tables.sql
```
