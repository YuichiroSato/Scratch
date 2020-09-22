create table Hinsyu (
  hs_no char(5) not null,
  name varchar(10) not null,
  hinsyu_type char(10) not null,
  primary key(hs_no)
);

create table Hinmoku (
  hm_no char(5) not null,
  name varchar(10) not null,
  hs_no char(5) not null,
  hinmoku_type char(1) not null,
  primary key(hm_no),
  foreign key(hs_no) references Hinsyu(hs_no),
  check(hinmoku_type in ('s', 'b')) -- s: 製品 b: 部材
);

create table Seihin (
  sh_no char(5) not null,
  hm_no char(5) not null,
  hosyou_kikan datetime not null,
  primary key(sh_no),
  foreign key(hm_no) references Hinmoku(hm_no)
);

create table Buzai (
  bz_no char(5) not null,
  hm_no char(5) not null,
  keiryou_unit char(2) not null,
  primary key(bz_no),
  foreign key(hm_no) references Hinmoku(hm_no)
);

create table Seihin_Buzai (
  sh_no char(5) not null,
  bz_no char(5) not null,
  parts_count int not null default(0),
  primary key(sh_no, bz_no),
  foreign key(sh_no) references Seihin(sh_no),
  foreign key(bz_no) references Buzai(bz_no)
);

create table Buzai_Gouki (
  bz_no char(5) not null,
  pl_no char(5) not null,
  gk_no char(5) not null,
  parts_count int not null default(0),
  primary key(bz_no, pl_no, gk_no),
  foreign key(bz_no) references Buzai(bz_no),
  foreign key(pl_no, gk_no) references Gouki(pl_no, gk_no)
);
