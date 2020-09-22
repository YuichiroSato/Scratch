#!/bin/bash

echo "drop tables"
mysql --defaults-extra-file=./my.cnf -D service_db -P 3306 < ./drops/drop_tables.sql

echo ""
