create table peserta (
  id varchar(36),
  nama varchar(255) not null,
  email varchar(255) not null,
  tanggal_lahir date not null, 
  foto varchar(255),
  primary key (id), 
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