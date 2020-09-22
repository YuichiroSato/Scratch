create table Hosyu_keiyaku (
  ky_no char(5) not null,
  keiyaku_type char(1) not null,
  start_date datetime not null,
  end_date datetime not null,
  amount_of_payment decimal not null default(0),
  pl_no char(5) not null,
  primary key(ky_no),
  foreign key(pl_no) references Hatuden_plant(pl_no),
  check(keiyaku_type in ('0', '1', '2', '3'))
);

create table Hosyu_Gouki (
  ky_no char(5) not null,
  pl_no char(5) not null,
  gk_no char(5) not null,
  primary key(ky_no, pl_no, gk_no),
  foreign key(ky_no) references Hosyu_keiyaku(ky_no),
  foreign key(pl_no, gk_no) references Gouki(pl_no, gk_no)
);

create table Teiki_tenken_keiyaku (
  ky_no char(5) not null,
  cycle int not null,
  primary key(ky_no),
  foreign key(ky_no) references Hosyu_keiyaku(ky_no)
);

create table Enkaku_kansi_keiyaku (
  ky_no char(5) not null,
  koumoku_type char(1) not null,
  primary key(ky_no),
  foreign key(ky_no) references Hosyu_keiyaku(ky_no)
);

create table Yobou_hozen_keiyaku (
  ky_no char(5) not null,
  taisyou_type char(1) not null,
  primary key(ky_no),
  foreign key(ky_no) references Hosyu_keiyaku(ky_no)
);

create table Sita_kusa_kari_keiyaku(
  ky_no char(5) not null,
  area int not null,
  primary key(ky_no),
  foreign key(ky_no) references Hosyu_keiyaku(ky_no)
);

create table Teiki_tenken_Yobou_hozen(
  t_ky_no char(5) not null,
  y_ky_no char(5) not null,
  primary key(t_ky_no, y_ky_no),
  foreign key(t_ky_no) references Teiki_tenken_keiyaku(ky_no),
  foreign key(y_ky_no) references Yobou_hozen_keiyaku(ky_no)
);
