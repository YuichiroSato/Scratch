insert into Taiou_meisai
  (tm_no, taiou_type, start_date, end_date)
values
  ('tm001', '0', null, null),
  ('tm002', '1', null, null),
  ('tm003', '2', null, null),
  ('tm004', '3', null, null);

insert into Setti_meisai
  (tm_no, se_no, setti_hm, number_of_setti)
values
  ('tm001', 'se001', 'hm001', 2),
  ('tm001', 'se002', 'hm002', 1);

insert into Kaisyuu_meisai
  (tm_no, ka_no, kaisyuu_hm, seizou_nm)
values
  ('tm002', 'ka001', 'hm001', '12345'),
  ('tm002', 'ka002', 'hm002', 'xyz01');
