CREATE DATABASE cicoda;
USE cicoda;

CREATE TABLE IF NOT EXISTS `users` (
  `user_name` varchar(45) NOT NULL,
  `email_address` varchar(45) NOT NULL,
  `password` varchar(255) NOT NULL,
  `first_name` varchar(45) DEFAULT NULL,
  `last_name` varchar(45) DEFAULT NULL,
  `distributor_id` int(11) NOT NULL,
  `user_type` varchar(45) DEFAULT NULL,
  INDEX (`user_name` ),
  PRIMARY KEY (`user_name`),
  UNIQUE KEY `email_address_UNIQUE` (`email_address`),
  UNIQUE KEY `password_UNIQUE` (`password`),
  KEY `fk_user_orgid_idx` (`distributor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `distributors` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `distributor_name` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `customers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_name` varchar(60) NOT NULL,
  `distributor_id` int(11) NOT NULL COMMENT 'Customer Organizations belong to Distributor',
  `bill_to_address_id` int(11) DEFAULT NULL,
  `ship_to_address_id` int(11) DEFAULT NULL,
  `contact` varchar(60) DEFAULT NULL,
  `alt_contact` varchar(60) DEFAULT NULL,
  `email_address` varchar(255) DEFAULT NULL,
  `phone_number` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_org_bill_idx` (`bill_to_address_id`,`ship_to_address_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `coolers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `cooler_identifier` varchar(45) NOT NULL,
  `device_id` int(11) DEFAULT NULL,
  `reorder_threshold` int(11) NOT NULL,
  `max_bottle_count` int(11) NOT NULL,
  `current_bottle_count` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `delivery_location_id` int(11) NOT NULL,
  `longitude` float(10,6) DEFAULT NULL,
  `latitude` float(10,6) DEFAULT NULL,
  `last_delivery_date` date DEFAULT NULL,
  `prev_bottle_count` int(11) NOT NULL DEFAULT '0',
  `current_full` int(11) NOT NULL,
  `current_empty` int(11) NOT NULL,
  `device_name` varchar(200) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `customer_id` (`customer_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `resetpassword` (
  `user_name` varchar(45) NOT NULL,
  `request_code` varchar(255) NOT NULL,
  `created_at` date NOT NULL,
  PRIMARY KEY (`user_name`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;