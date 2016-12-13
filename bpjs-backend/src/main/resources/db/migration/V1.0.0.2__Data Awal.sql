insert into peserta(id, nama, email, tanggal_lahir, foto)
values ('p001', 'Peserta 001', 'peserta001@example.com', '1945-08-17', 'foto.jpg');

insert into tagihan(id, id_peserta, tanggal_tagihan, tanggal_jatuh_tempo, nilai)
values ('t001', 'p001', '2016-01-01', '2016-01-20', 150000.00);
