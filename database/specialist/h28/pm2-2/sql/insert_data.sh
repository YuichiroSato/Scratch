#!/bin/bash

echo "insert plant data"
mysql --defaults-extra-file=./my.cnf -D service_db -P 3306 < ./inserts/insert_plant_data.sql

echo "insert hinsyu data"
mysql --defaults-extra-file=./my.cnf -D service_db -P 3306 < ./inserts/insert_hinsyu_data.sql

echo "insert recall data"
mysql --defaults-extra-file=./my.cnf -D service_db -P 3306 < ./inserts/insert_recall_data.sql

echo "insert keiyaku data"
mysql --defaults-extra-file=./my.cnf -D service_db -P 3306 < ./inserts/insert_keiyaku_data.sql

echo "insert gyoumu data"
mysql --defaults-extra-file=./my.cnf -D service_db -P 3306 < ./inserts/insert_gyoumu_data.sql

echo "insert meisai data"
mysql --defaults-extra-file=./my.cnf -D service_db -P 3306 < ./inserts/insert_meisai_data.sql

echo ""
