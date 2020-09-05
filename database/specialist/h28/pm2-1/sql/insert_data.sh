#!/bin/bash

mysql -u root -p'example' -D bank_db -P 3306 < ./insert/shiten.sql
mysql -u root -p'example' -D bank_db -P 3306 < ./insert/syokugyou.sql
mysql -u root -p'example' -D bank_db -P 3306 < ./insert/kamoku.sql
mysql -u root -p'example' -D bank_db -P 3306 < ./insert/syouhin_syubetu.sql
mysql -u root -p'example' -D bank_db -P 3306 < ./insert/torihiki_syubetu.sql

mysql -u root -p'example' -D bank_db -P 3306 < ./insert/buka.sql
mysql -u root -p'example' -D bank_db -P 3306 < ./insert/unyou_syouhin.sql

mysql -u root -p'example' -D bank_db -P 3306 < ./insert/eigyouin.sql

mysql -u root -p'example' -D bank_db -P 3306 < ./insert/kokyaku_kihon.sql

mysql -u root -p'example' -D bank_db -P 3306 < ./insert/anken.sql
mysql -u root -p'example' -D bank_db -P 3306 < ./insert/kokyaku_syousai.sql
mysql -u root -p'example' -D bank_db -P 3306 < ./insert/kokyaku_kouza.sql

mysql -u root -p'example' -D bank_db -P 3306 < ./insert/schedule.sql
mysql -u root -p'example' -D bank_db -P 3306 < ./insert/contact_history.sql
mysql -u root -p'example' -D bank_db -P 3306 < ./insert/torihiki.sql
