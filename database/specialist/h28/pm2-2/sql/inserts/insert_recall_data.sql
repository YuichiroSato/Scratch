insert into Service_han
  (ei_no, hn_no, name, han_tyou_name)
values
  ('ei001', 'hn001', 'Han 1', 'xyz'),
  ('ei002', 'hn002', 'Han 2', 'abc'),
  ('ei003', 'hn003', 'Han 3', 'www');

insert into Kosyou_Cause
  (kc_no, description, hantei_way, syuuri_type, recall_flag)
values
  ('kc001', 'desc', 'way', 'u', 't'),
  ('kc002', 'desc', 'way', 'm', 't'),
  ('kc003', 'desc', 'way', 'm', 'f'),
  ('kc004', 'desc', 'way', 'u', 'f'),
  ('kc005', 'desc', 'way', 'u', 'f');

insert into Kosyou_Hinmoku
  (kc_no, hm_no, kanren_type)
values
  ('kc001', 'hm001', 'k'),
  ('kc001', 'hm002', 't'),
  ('kc001', 'hm003', 't'),
  ('kc002', 'hm001', 'k'),
  ('kc002', 'hm002', 't'),
  ('kc003', 'hm003', 'k'),
  ('kc003', 'hm004', 'k'),
  ('kc004', 'hm005', 'k');

insert into Recall
  (rc_no, kc_no, start_date, end_date)
values
  ('rc001', 'kc001', '2020-01-01 00:00:00', '2020-01-31 00:00:00'),
  ('rc002', 'kc002', '2020-01-01 00:00:00', '2020-01-31 00:00:00');

insert into Recall_waku
  (rc_no
  , ei_no
  , hn_no
  , wk_no
  , taiou_date
  , start_time
  , end_time
  , waku_type)
values
  ('rc001'
  , 'ei001'
  , 'hn001'
  , 'wk001'
  , '2020-01-01 00:00:00'
  , '2020-01-01 10:00:00'
  , '2020-01-01 12:00:00'
  , '1'),
  ('rc002'
  , 'ei002'
  , 'hn002'
  , 'wk002'
  , '2020-01-01 00:00:00'
  , '2020-01-01 10:00:00'
  , '2020-01-01 12:00:00'
  , '1');
