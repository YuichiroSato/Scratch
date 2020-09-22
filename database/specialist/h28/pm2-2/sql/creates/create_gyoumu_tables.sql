create table Teiki_tenken (
  tt_no char(5) not null,
  description varchar(100) not null default(''),
  ky_no char(5) not null,
  primary key(tt_no),
  foreign key(ky_no) references Teiki_tenken_keiyaku(ky_no)
);

create table Enkaku_kansi (
  ek_no char(5) not null,
  hatuden_ryou int not null,
  max_temp int not null,
  min_temp int not null,
  nissya_ryou int not null,
  ky_no char(5) not null,
  primary key(ek_no),
  foreign key(ky_no) references Enkaku_kansi_keiyaku(ky_no)
);

create table Sita_kusa_kari (
  sk_no char(5) not null,
  time int not null,
  ky_no char(5) not null,
  primary key(sk_no),
  foreign key(ky_no) references Sita_kusa_kari_keiyaku(ky_no)
);

create table Syuuri_taiou (
  st_no char(5) not null,
  tt_no char(5),
  ek_no char(5),
  sk_no char(5),
  ei_no char(5) not null,
  hn_no char(5) not null,
  primary key(st_no),
  foreign key(tt_no) references Teiki_tenken(tt_no),
  foreign key(ek_no) references Enkaku_kansi(ek_no),
  foreign key(sk_no) references Sita_kusa_kari(sk_no),
  foreign key(ei_no, hn_no) references Service_han(ei_no, hn_no)
);

create table Yobou_hozen (
  yh_no char(5) not null,
  tt_no char(5),
  ek_no char(5),
  sk_no char(5),
  ky_no char(5) not null,
  ei_no char(5) not null,
  hn_no char(5) not null,
  primary key(yh_no),
  foreign key(tt_no) references Teiki_tenken(tt_no),
  foreign key(ek_no) references Enkaku_kansi(ek_no),
  foreign key(sk_no) references Sita_kusa_kari(sk_no),
  foreign key(ky_no) references Yobou_hozen_keiyaku(ky_no),
  foreign key(ei_no, hn_no) references Service_han(ei_no, hn_no)
);

create table Recall_taiou (
  rt_no char(5) not null,
  zyoukyou_type char(1) not null,
  rc_no char(5) not null,
  ei_no char(5) not null,
  hn_no char(5) not null,
  wk_no char(5) not null,
  primary key(rt_no),
  foreign key(rc_no, ei_no, hn_no, wk_no) references Recall_waku(rc_no, ei_no, hn_no, wk_no),
  check(zyoukyou_type in ('0', '1', '2')) -- 0: 日程調整済 1: 対応中 2: 対応完了
);

create table Gentyou (
  gt_no char(5) not null,
  description varchar(100) not null default(''),
  ti_no char(5) not null,
  tt_no char(5),
  ek_no char(5),
  sk_no char(5),
  primary key(gt_no),
  foreign key(ti_no) references Toiawase(ti_no),
  foreign key(tt_no) references Teiki_tenken(tt_no),
  foreign key(ek_no) references Enkaku_kansi(ek_no),
  foreign key(sk_no) references Sita_kusa_kari(sk_no)
);

create table Gentyou_tehai (
  gt_no char(5) not null,
  yotei_date datetime not null,
  tehai_date datetime not null,
  gyoumu_description varchar(100) not null default(''),
  ei_no char(5) not null,
  hn_no char(5) not null,
  primary key(gt_no),
  foreign key(gt_no) references Gentyou(gt_no),
  foreign key(ei_no, hn_no) references Service_han(ei_no, hn_no)
);

