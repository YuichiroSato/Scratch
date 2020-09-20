create table Eigyousyo (
  ei_no char(5) not null,
  name varchar(10) not null,
  primary key(ei_no)
);

create table Erea (
  erea_no char(5) not null,
  ei_no char(5) not null,
  name varchar(10) not null,
  primary key(erea_no),
  foreign key(ei_no) references Eigyousyo(ei_no)
);

create table End_user (
  e_user_no char(5) not null,
  name varchar(10) not null,
  contact varchar(10) not null default(''),
  primary key(e_user_no)
);

create table Hatuden_plant (
  pl_no char(5) not null,
  address varchar(10) not null default(''),
  erea_no char(5) not null,
  e_user_no char(5) not null,
  plant_type char(1) not null,
  primary key(pl_no),
  foreign key(erea_no) references Erea(erea_no),
  foreign key(e_user_no) references End_user(e_user_no),
  check(plant_type in ('j', 's'))
);

create table Jutaku_plant (
  pl_no char(5) not null,
  yane_zaisitu varchar(10) not null default(''),
  memo varchar(100) not null default(''),
  primary key(pl_no),
  foreign key(pl_no) references Hatuden_plant(pl_no)
);

create table Sangyou_plant (
  pl_no char(5) not null,
  setti_type char(1) not null,
  hatuden_ryou int not null default(0),
  primary key(pl_no),
  foreign key(pl_no) references Hatuden_plant(pl_no),
  check(setti_type in ('k', 'n'))
);

create table Shita_kusa_kari_area (
  pl_no char(5) not null,
  total_area int not null default(0),
  primary key(pl_no),
  foreign key(pl_no) references Sangyou_plant(pl_no)
);

create table Gouki (
  pl_no char(5) not null,
  gk_no char(5) not null,
  hatuden_ryou int not null default(0),
  setti_type char(5) not null,
  primary key(pl_no, gk_no),
  foreign key(pl_no) references Sangyou_plant(pl_no)
);

create table Hatuden_corporation (
  cp_no char(5) not null,
  name varchar(10) not null,
  pl_no char(5) not null,
  gk_no char(5) not null,
  primary key(cp_no),
  foreign key(pl_no, gk_no) references Gouki(pl_no, gk_no)
);

create table Toiawase (
  ti_no char(5) not null,
  start_date datetime not null,
  end_date datetime,
  pl_no char(5) not null,
  summary nvarchar(100) not null default(''),
  primary key(ti_no),
  foreign key(pl_no) references Hatuden_plant(pl_no)
);

