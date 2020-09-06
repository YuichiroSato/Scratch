#!/bin/bash

mysql -u root -p'example' -D service_db -P 3306 < ./drops/drop_tables.sql
