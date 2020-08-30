create table Shiten (
  shiten_code char(4) not null,
  shiten_name varchar(30) not null,
  update_ts datetime not null,
  primary key(shiten_code)
);

create table Buka (
  buka_code char(4) not null,
  shiten_code char(4) not null,
  buka_name varchar(30) not null,
  update_ts datetime not null,
  primary key(buka_code),
  foreign key(shiten_code) references Shiten(shiten_code)
);

create table Eigyouin (
  kouin_no int not null,
  kouin_name varchar(30) not null,
  buka_code char(4) not null,
  update_ts datetime not null,
  primary key(kouin_no),
  foreign key(buka_code) references Buka(buka_code)
);

create table Kamoku (
  kamoku_code char(3) not null,
  tamoku_name varchar(30) not null,
  update_ts datetime not null,
  primary key(kamoku_code)
);

create table Syokugyou (
  syokugyou_code char(4) not null,
  syokugyou_name varchar(30) not null,
  update_ts datetime not null,
  primary key(syokugyou_code)
);

create table TorihikiSyubetu (
  torihiki_syubetu_code char(6) not null,
  torihiki_syubetu_name varchar(30) not null,
  update_ts datetime not null,
  primary key(torihiki_syubetu_code)
);

create table SyouhinSyubetu (
  syouhin_syubetu_code char(6) not null,
  syouhin_syubetu_name varchar(30) not null,
  update_ts datetime not null,
  primary key(syouhin_syubetu_code)
);

create table UnyouSyouhin (
  syouhin_code char(6) not null,
  syouhin_syubetu_code char(20) not null,
  syouhin_name varchar(30) not null,
  syouhin_description varchar(1000) not null,
  update_ts datetime not null,
  primary key(syouhin_code),
  foreign key(syouhin_syubetu_code) references SyouhinSyubetu(syouhin_syubetu_code)
);

create table KokyakuKihon (
  kokyaku_no int not null,
  kokyaku_name varchar(30) not null,
  address varchar(30) not null,
  phone_number char(10) not null,
  birth_date datetime not null,
  gender char(1) not null,
  syokugyou_code char(4) not null,
  kinmusaki varchar(1000) not null,
  tantou_kouin_no int not null,
  update_ts datetime not null,
  primary key(kokyaku_no),
  foreign key(syokugyou_code) references Syokugyou(syokugyou_code),
  foreign key(tantou_kouin_no) references Eigyouin(kouin_no)
);

create table KokyakuSyousai (
  kokyaku_no int not null,
  update_ts datetime not null,
  kazoku_kousei varchar(100) not null,
  shisan_sougacu decimal not null,
  toushi_kanou_gaku decimal not null,
  toushi_shistaku_keiken_nensuu int not null,
  gaika_yokin_keiken_nensuu int not null,
  primary key(kokyaku_no, update_ts),
  foreign key(kokyaku_no) references KokyakuKihon(kokyaku_no)
);

create table KokyakuKouza (
  shiten_code char(4) not null,
  kamoku_code char(3) not null,
  kouza_no char(7) not null,
  kokyaku_no int not null,
  kaisetu_date datetime not null,
  syuki_zandaka decimal not null,
  genzai_zandaka decimal not null,
  update_ts datetime not null,
  primary key(shiten_code, kamoku_code, kouza_no),
  foreign key(shiten_code) references Shiten(shiten_code),
  foreign key(kamoku_code) references Kamoku(kamoku_code),
  foreign key(kokyaku_no) references KokyakuKihon(kokyaku_no)
);

create table Anken (
  shiten_code char(4) not null,
  anken_no int not null,
  kouin_no int not null,
  kokyaku_no int not null,
  touroku_date datetime not null,
  state char(2) not null,
  update_ts datetime not null,
  primary key(shiten_code, anken_no),
  foreign key(shiten_code) references Shiten(shiten_code),
  foreign key(kouin_no) references Eigyouin(kouin_no),
  foreign key(kokyaku_no) references KokyakuKihon(kokyaku_no)
);

create table Torihiki (
  shiten_code char(4) not null,
  anken_no int not null,
  torihiki_no int not null,
  update_ts datetime not null,
  torihiki_syubetu_code char(6) not null,
  syouhin_code char(6) not null,
  torihiki_date datetime not null,
  kamoku_code char(3) not null,
  kouza_no char(7) not null,
  torihiki_kousuu int not null,
  torihiki_kingaku decimal not null,
  primary key(shiten_code, anken_no, torihiki_no),
  foreign key(torihiki_syubetu_code) references TorihikiSyubetu(torihiki_syubetu_code),
  foreign key(syouhin_code) references UnyouSyouhin(syouhin_code),
  foreign key(shiten_code, anken_no) references Anken(shiten_code, anken_no),
  foreign key(shiten_code, kamoku_code, kouza_no) references KokyakuKouza(shiten_code, kamoku_code, kouza_no)
);

create table ContactHistory (
  shiten_code char(4) not null,
  anken_no int not null,
  contact_date datetime not null,
  update_ts datetime not null,
  kaiwa_1 varchar(1000),
  kaiwa_2 varchar(1000),
  primary key(shiten_code, anken_no, contact_date),
  foreign key(shiten_code, anken_no) references Anken(shiten_code, anken_no),
  foreign key(shiten_code) references Shiten(shiten_code)
);

create table Schedule (
  kouin_no int not null,
  yotei_date datetime not null,
  start_time datetime not null,
  gyou_no smallint not null,
  end_time datetime not null,
  koudou_syubetu char(1),
  koudou_naiyou varchar(1000),
  shiten_code char(4),
  anken_no int,
  primary key(kouin_no, yotei_date, start_time, gyou_no),
  foreign key(kouin_no) references Eigyouin(kouin_no),
  foreign key(shiten_code, anken_no) references Anken(shiten_code, anken_no)
);
