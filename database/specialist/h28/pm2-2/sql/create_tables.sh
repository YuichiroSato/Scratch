#!/bin/bash

echo "aaaaa"
mysql -u root -p'example' -D service_db -P 3306 < ./creates/create_tables.sql
