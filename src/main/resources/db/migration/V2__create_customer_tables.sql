create table customer(
id bigint not null auto_increment,
email varchar(60) not null,
pwd varchar(200) not null,
role varchar(60) not null,
primary key (id)
)engine=InnoDB default charset=utf8;