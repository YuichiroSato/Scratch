insert into Teiki_tenken
  (tt_no, description, ky_no)
values
  ('tt001', 'first', 'ky002'),
  ('tt002', 'second', 'ky002'),
  ('tt003', 'third', 'ky002');

insert into Enkaku_kansi
  (ek_no, hatuden_ryou, max_temp, min_temp, nissya_ryou, ky_no)
values
  ('ek001', 1, 30, 10, 1, 'ky003');

insert into Sita_kusa_kari
  (sk_no, time, ky_no)
values
  ('sk001', 1, 'ky005'),
  ('sk002', 3, 'ky005'),
  ('sk003', 4, 'ky005');

insert into Gentyou
  (gt_no, description, ti_no, tt_no, ek_no, sk_no)
values
  ('gt001', '', 'ti001', null, null, 'sk001');

insert into Gentyou_tehai
  (gt_no, yotei_date, tehai_date, gyoumu_description, ei_no, hn_no)
values
  ('gt001', '2020-01-01', '2020-01-01', '', 'ei001', 'hn001');
