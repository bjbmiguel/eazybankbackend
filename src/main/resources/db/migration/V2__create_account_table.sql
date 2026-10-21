CREATE TABLE `accounts`
(
    `customer_id`    int          NOT NULL,
    `account_number` int          NOT NULL,
    `account_type`   varchar(100) NOT NULL,
    `branch_address` varchar(200) NOT NULL,
    `create_dt`      date DEFAULT NULL,
    PRIMARY KEY (`account_number`),
    KEY `customer_id` (`customer_id`),
    CONSTRAINT `customer_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`) ON DELETE CASCADE
)engine = InnoDB default charset = utf8;


CREATE TABLE `account_transactions`
(
    `transaction_id`      varchar(200) NOT NULL,
    `account_number`      int          NOT NULL,
    `customer_id`         int          NOT NULL,
    `transaction_dt`      date         NOT NULL,
    `transaction_summary` varchar(200) NOT NULL,
    `transaction_type`    varchar(100) NOT NULL,
    `transaction_amt`     int          NOT NULL,
    `closing_balance`     int          NOT NULL,
    `create_dt`           date DEFAULT NULL,
    PRIMARY KEY (`transaction_id`),
    KEY `customer_id` (`customer_id`),
    KEY `account_number` (`account_number`),
    CONSTRAINT `accounts_ibfk_2` FOREIGN KEY (`account_number`) REFERENCES `accounts` (`account_number`) ON DELETE CASCADE,
    CONSTRAINT `acct_user_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`) ON DELETE CASCADE
) engine = InnoDB default charset = utf8;