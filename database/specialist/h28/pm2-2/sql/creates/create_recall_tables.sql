create table Service_han (
  ei_no char(5) not null,
  hn_no char(5) not null,
  name char(10) not null,
  han_tyou_name char(10) not null default(''),
  primary key(ei_no, hn_no),
  foreign key(ei_no) references Eigyousyo(ei_no)
);

create table Kosyou_Cause (
  kc_no char(5) not null,
  description char(100) not null,
  hantei_way char(10) not null,
  syuuri_type char(1) not null,
  recall_flag char(1) not null,
  primary key(kc_no),
  check(syuuri_type in ('u', 'm')), -- u: 有償 m: 無償
  check(recall_flag in ('t', 'f')) -- t: リコール f: リコールでない
);

create table Kosyou_Hinmoku (
  kc_no char(5) not null,
  hm_no char(5) not null,
  kanren_type char(1) not null,
  primary key(kc_no, hm_no),
  foreign key(kc_no) references Kosyou_Cause(kc_no),
  foreign key(hm_no) references Hinmoku(hm_no),
  check(kanren_type in ('t', 'k')) -- t: 直接 k: 間接
);

create table Recall (
  rc_no char(5) not null,
  kc_no char(5) not null,
  start_date datetime not null,
  end_date datetime,
  primary key(rc_no),
  foreign key(kc_no) references Kosyou_Cause(kc_no)
);

create table Recall_waku (
  rc_no char(5) not null,
  ei_no char(5) not null,
  hn_no char(5) not null,
  wk_no char(5) not null,
  taiou_date datetime not null,
  start_time datetime not null,
  end_time datetime not null,
  waku_type char(1) not null,
  primary key(rc_no, ei_no, hn_no, wk_no),
  foreign key(rc_no) references Recall(rc_no),
  foreign key(ei_no, hn_no) references Service_han(ei_no, hn_no),
  check(waku_type in ('0', '1', '2', '3')) -- 0: 未アサイン 1: アサイン済 2: 実施済 3: 解法済
);
