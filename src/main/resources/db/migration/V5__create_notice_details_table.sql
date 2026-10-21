CREATE TABLE `notice_details`
(
    `notice_id`      int          NOT NULL AUTO_INCREMENT,
    `notice_summary` varchar(200) NOT NULL,
    `notice_details` varchar(500) NOT NULL,
    `notic_beg_dt`   date         NOT NULL,
    `notic_end_dt`   date DEFAULT NULL,
    `create_dt`      date DEFAULT NULL,
    `update_dt`      date DEFAULT NULL,
    PRIMARY KEY (`notice_id`)
)engine = InnoDB default charset = utf8;