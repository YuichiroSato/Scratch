insert into Hinsyu
  (hs_no, name, hinsyu_type)
values
  ('hs001', 'Hs 1', 'Module'),
  ('hs002', 'Hs 2', 'Buzai'),
  ('hs003', 'Hs 3', 'Haisen'),
  ('hs004', 'Hs 4', 'Budebban'),
  ('hs005', 'Hs 5', 'Module');

insert into Hinmoku
  (hm_no, name, hs_no, hinmoku_type)
values
  ('hm001', 'Hm 1', 'hs001', 'b'),
  ('hm002', 'Hm 2', 'hs001', 's'),
  ('hm003', 'Hm 3', 'hs002', 'b'),
  ('hm004', 'Hm 4', 'hs003', 's'),
  ('hm005', 'Hm 5', 'hs004', 'b');

insert into Seihin
  (sh_no, hm_no, hosyou_kikan)
values
  ('sh001', 'hm001', '2020-12-31 00:00:00'),
  ('sh002', 'hm004', '2020-12-31 00:00:00');

insert into Buzai
  (bz_no, hm_no, keiryou_unit)
values
  ('bz001', 'hm001', 'kg'),
  ('bz002', 'hm003', 'kg'),
  ('bz003', 'hm005', 'cm');

insert into Seihin_Buzai
  (sh_no, bz_no, parts_count)
values
  ('sh001', 'bz001', 1),
  ('sh002', 'bz003', 3);

insert into Buzai_Gouki
  (bz_no, pl_no, gk_no, parts_count)
values
  ('bz001', 'pl002', 'gk001', 1),
  ('bz002', 'pl002', 'gk001', 1),
  ('bz003', 'pl002', 'gk001', 5),
  ('bz001', 'pl002', 'gk002', 3);
