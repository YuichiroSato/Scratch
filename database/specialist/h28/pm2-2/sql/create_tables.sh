#!/bin/bash

echo "create plant tables"
mysql --defaults-extra-file=./my.cnf -D service_db -P 3306 < ./creates/create_plant_tables.sql

echo "create hinsyu tables"
mysql --defaults-extra-file=./my.cnf -D service_db -P 3306 < ./creates/create_hinsyu_tables.sql

echo "create recall tables"
mysql --defaults-extra-file=./my.cnf -D service_db -P 3306 < ./creates/create_recall_tables.sql

echo "create keiyaku tables"
mysql --defaults-extra-file=./my.cnf -D service_db -P 3306 < ./creates/create_keiyaku_tables.sql

echo "create gyoumu tables"
mysql --defaults-extra-file=./my.cnf -D service_db -P 3306 < ./creates/create_gyoumu_tables.sql

echo "create meisai tables"
mysql --defaults-extra-file=./my.cnf -D service_db -P 3306 < ./creates/create_meisai_tables.sql

echo ""
