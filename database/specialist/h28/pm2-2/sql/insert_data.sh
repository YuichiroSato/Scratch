#!/bin/bash

echo "insert data"
mysql -u root -p'example' -D service_db -P 3306 < ./inserts/insert_data.sql

echo ""
