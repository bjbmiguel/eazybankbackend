set foreign_key_checks = 0;

delete  from users;
delete  from authorities;
delete from customer;

set foreign_key_checks = 1;

alter table customer auto_increment=1;

insert into users  (username, password,enabled) values('admin', '{argon2}$argon2id$v=19$m=19456,t=2,p=1$UlUxSzZYZ0lGSXMwMDRpcQ$bqLPeNr2TswtvGSofVvSjZM/ySLrds2dpuns2QYSE5Y', true);
