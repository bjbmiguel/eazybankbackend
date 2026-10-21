CREATE TABLE `customer` (
`customer_id` int NOT NULL AUTO_INCREMENT,
`name` varchar(100) NOT NULL,
`email` varchar(100) NOT NULL,
`mobile_number` varchar(20) NOT NULL,
`pwd` varchar(500) NOT NULL,
`role` varchar(100) NOT NULL,
`create_dt` date DEFAULT NULL,
PRIMARY KEY (`customer_id`)
)engine=InnoDB default charset=utf8;
