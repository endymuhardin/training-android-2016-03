create table peserta (
  id varchar(36),
  nomor varchar(30) not null,
  nama varchar(255) not null,
  email varchar(255) not null,
  tanggal_lahir date not null, 
  foto varchar(255),
  primary key (id), 
  unique (nomor),
  unique (email)
);

create table tagihan (
  id varchar(36),
  id_peserta varchar(36) not null,
  tanggal_tagihan date not null,
  tanggal_jatuh_tempo date not null,
  nilai number(19,2) not null,
  primary key (id),
  foreign key (id_peserta) references peserta(id)
);

create table fcm_token (
  id varchar(36),
  id_peserta varchar(36) not null,
  fcm_token varchar(255) not null,
  primary key (id),
  foreign key (id_peserta) references peserta(id),
  unique (id_peserta)
);