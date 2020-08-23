create table Shiten (
  shiten_code char(4) not null,
  shiten_name varchar(30) not null,
  update_ts datetime not null
);

create table Buka (
  buka_code char(4) not null,
  shiten_code char(4) not null,
  buka_name varchar(30) not null,
  update_ts datetime not null
);

create table Eigyouin (
  kouin_no int not null,
  kouin_name varchar(30) not null,
  buka_code char(4) not null,
  update_ts datetime not null
);

create table Kamoku (
  kamoku_code char(3) not null,
  tamoku_name varchar(30) not null,
  update_ts datetime not null
);

create table Syokugyou (
  syokugyou_code char(4) not null,
  syokugyou_name varchar(30) not null,
  update_ts datetime not null
);

create table TorihikiSyubetu (
  torihiki_syubetu_code char(6) not null,
  torihiki_syubetu_name varchar(30) not null,
  update_ts datetime not null
);

create table SyouhinSyubetu (
  syouhin_syubetu_code char(6) not null,
  syouhin_syubetu_name varchar(30) not null,
  update_ts datetime not null
);

create table UnyouSyouhin (
  syouhin_code char(6) not null,
  syouhin_syubetu_code char(20) not null,
  syouhin_name varchar(30) not null,
  syouhin_description varchar(1000) not null,
  update_ts datetime not null
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
  update_ts datetime not null
);

create table KokyakuSyousai (
  kokyaku_no int not null,
  update_ts datetime not null,
  kazoku_kousei varchar(100) not null,
  shisan_sougacu decimal not null,
  toushi_kanou_gaku decimal not null,
  toushi_shistaku_keiken_nensuu int not null,
  gaika_yokin_keiken_nensuu int not null
);

create table KokyakuKouza (
  shiten_code char(4) not null,
  kamoku_code char(3) not null,
  kouza_no char(7) not null,
  kokyaku_no int not null,
  kaisetu_date datetime not null,
  syuki_zandaka decimal not null,
  genzai_zandaka decimal not null,
  update_ts datetime not null
);

create table Anken (
  shiten_code char(4) not null,
  anken_no int not null,
  kouin_no int not null,
  kokyaku_no int not null,
  touroku_date datetime not null,
  state char(2) not null,
  update_ts datetime not null
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
  torihiki_kingaku decimal not null
);

create table ContactHistory (
  shiten_code char(4) not null,
  anken_no int not null,
  contact_date datetime not null,
  update_ts datetime not null,
  kaiwa_1 varchar(1000),
  kaiwa_2 varchar(1000)
);

create table Schedule (
  kouin_no int not null,
  yotei_date datetime not null,
  start_time datetime not null,
  gyou_no smallint not null,
  end_time datetime not null,
  koudou_syubetu char(1) not null,
  koudou_naiyou varchar(1000) not null,
  shiten_code char(4) not null,
  anken_no int
);
