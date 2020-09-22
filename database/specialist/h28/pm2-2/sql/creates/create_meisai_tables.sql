create table Taiou_meisai (
  tm_no char(5) not null,
  taiou_type char(1) not null,
  start_date datetime,
  end_date datetime,
  primary key(tm_no),
  check(taiou_type in ('0', '1', '2', '3'))
);

create table Setti_meisai (
  tm_no char(5) not null,
  se_no char(5) not null,
  setti_hm char(5) not null,
  number_of_setti int not null,
  primary key(tm_no, se_no),
  foreign key(tm_no) references Taiou_meisai(tm_no),
  foreign key(setti_hm) references Hinmoku(hm_no)
);

create table Kaisyuu_meisai (
  tm_no char(5) not null,
  ka_no char(5) not null,
  kaisyuu_hm char(5) not null,
  seizou_nm varchar(10) not null,
  primary key(tm_no, ka_no),
  foreign key(tm_no) references Taiou_meisai(tm_no),
  foreign key(kaisyuu_hm) references Hinmoku(hm_no)
);

create table Syuttyou_meisai (
  tm_no char(5) not null,
  sy_no char(5) not null,
  enkaku_type char(1) not null,
  primary key(tm_no, sy_no),
  foreign key(tm_no) references Taiou_meisai(tm_no),
  check(enkaku_type in ('0', '1'))
);

create table Sagyou_meisai (
  tm_no char(5) not null,
  sa_no char(5) not null,
  time int not null,
  primary key(tm_no, sa_no),
  foreign key(tm_no) references Taiou_meisai(tm_no)
);
