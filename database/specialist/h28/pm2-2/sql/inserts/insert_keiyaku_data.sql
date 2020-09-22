insert into Hosyu_keiyaku
  ( ky_no
  , keiyaku_type
  , start_date
  , end_date
  , amount_of_payment
  , pl_no)
values
  ('ky001', '0', '2020-01-01', '2020-01-02', 3, 'pl002'),
  ('ky002', '0', '2020-01-01', '2020-01-02', 3, 'pl002'),
  ('ky003', '1', '2020-01-01', '2020-01-02', 3, 'pl002'),
  ('ky004', '2', '2020-01-01', '2020-01-02', 3, 'pl002'),
  ('ky005', '3', '2020-01-01', '2020-01-02', 3, 'pl002');

insert into Hosyu_Gouki
  (ky_no, pl_no, gk_no)
values
  ('ky001', 'pl002', 'gk001'),
  ('ky001', 'pl002', 'gk002'),
  ('ky002', 'pl002', 'gk001'),
  ('ky003', 'pl002', 'gk002'),
  ('ky004', 'pl002', 'gk002'),
  ('ky005', 'pl002', 'gk002');

insert into Teiki_tenken_keiyaku
  (ky_no, cycle)
values
  ('ky002', 3);

insert into Enkaku_kansi_keiyaku
  (ky_no, koumoku_type)
values
  ('ky003', '0');

insert into Yobou_hozen_keiyaku
  (ky_no, taisyou_type)
values
  ('ky004', '1');

insert into Sita_kusa_kari_keiyaku
  (ky_no, area)
values
  ('ky005', 5);

insert into Teiki_tenken_Yobou_hozen
  (t_ky_no, y_ky_no)
values
  ('ky002', 'ky004');
