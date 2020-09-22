insert into Eigyousyo
  (ei_no, name)
values
  ('ei001', 'Ei 1'),
  ('ei002', 'Ei 2'),
  ('ei003', 'Ei 3'),
  ('ei004', 'Ei 4'),
  ('ei005', 'Ei 5');

insert into Erea
  (erea_no, ei_no, name)
values
  ('er001', 'ei001', 'Erea 1'),
  ('er002', 'ei001', 'Erea 2'),
  ('er003', 'ei002', 'Erea 3'),
  ('er004', 'ei003', 'Erea 4');

insert into End_user
  (e_user_no, name, contact)
values
  ('eu001', 'User 1', 'phone 1'),
  ('eu002', 'User 2', 'phone 2'),
  ('eu003', 'User 3', 'phone 3'),
  ('eu004', 'User 4', 'phone 4');

insert into Hatuden_plant
  (pl_no, address, erea_no, e_user_no, plant_type)
values
  ('pl001', 'at jp', 'er001', 'eu001', 'j'),
  ('pl002', 'at jp', 'er002', 'eu001', 's'),
  ('pl003', '', 'er001', 'eu002', 's'),
  ('pl004', '', 'er003', 'eu002', 'j'),
  ('pl005', 'at jp', 'er004', 'eu003', 'j');

insert into Jutaku_plant
  (pl_no, yane_zaisitu, memo)
values
  ('pl001', '', ''),
  ('pl004', '', 'too old'),
  ('pl005', 'totan', '');

insert into Sangyou_plant
  (pl_no, setti_type, hatuden_ryou)
values
  ('pl002', 'k', 1),
  ('pl003', 'm', 0);

insert into Miriyou_plant
  (pl_no, total_sita_kusa_kari_area)
values
  ('pl003', 10);

insert into Gouki
  (pl_no, gk_no, hatuden_ryou, setti_type)
values
  ('pl002', 'gk001', 1, 'unknw'),
  ('pl002', 'gk002', 5, '');

insert into Hatuden_corporation
  (cp_no, name, pl_no, gk_no)
values
  ('cp001', 'denki 1', 'pl002', 'gk001'),
  ('cp002', 'denki 2', 'pl002', 'gk002');

insert into Toiawase
  (ti_no, start_date, end_date, pl_no, summary)
values
  ('ti001', '3020-01-01 00:00:00', null, 'pl001', '');
