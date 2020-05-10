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
  `enabled` bool,
  `phone_number` varchar(45) DEFAULT NULL,
  INDEX (`user_name` ),
  PRIMARY KEY (`user_name`),
  UNIQUE KEY `email_address_UNIQUE` (`email_address`),
  KEY `fk_user_orgid_idx` (`distributor_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `distributors` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `distributor_name` varchar(45) NOT NULL,
  `address_id` int(11) NOT NULL, 
  PRIMARY KEY (`id`),
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `customers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(45) NOT NULL,
  `org_name` varchar(60) NOT NULL,
  `distributor_id` int(11) NOT NULL,
  `address_id` int(11) NOT NULL, 
  `contact` varchar(60) DEFAULT NULL,
  `alt_contact` varchar(60) DEFAULT NULL,
  `related_to` int(11) DEFAULT NULL,
  INDEX (`user_name`, `id`),
  PRIMARY KEY (`id`),
  KEY `fk_org_bill_idx` (`address_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address_line1` varchar(45) NOT NULL,
  `address_line2` varchar(45) DEFAULT NULL,
  `address_line3` varchar(45) DEFAULT NULL,
  `address_city` varchar(45) NOT NULL,
  `address_state` varchar(45) DEFAULT NULL,
  `address_country` varchar(45) NOT NULL,
  `address_postal_code` varchar(20) DEFAULT NULL,
  INDEX (`id`),
  PRIMARY KEY (`id`)
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

CREATE TABLE IF NOT EXISTS `delivery` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status` varchar(30) NOT NULL,
  `from_distributor_id` int(11) DEFAULT NULL,
  `from_customer_id` int(11) DEFAULT NULL,
  `from_facility_id` int(11) DEFAULT NULL,
  `to_customer_id` int(11) DEFAULT NULL,
  `to_facility_id` int(11) DEFAULT NULL,
  `to_cooler_id` int(11) DEFAULT NULL,
  `driver_id` int(11) DEFAULT NULL,
  `vehicle_id` int(11) DEFAULT NULL,
  `delivery_date` date NOT NULL,
  `full_bottles` int(11) NOT NULL,
  `route_id` int(11) DEFAULT NULL,
  `empties_retrieved` int(11) DEFAULT NULL,
  `delivery_notes` text NOT NULL,
  `actual_fulls_delivered` int(11) NOT NULL,
  `actual_delivery_date` date DEFAULT NULL,
  `actual_empties_retrieved` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 AUTO_INCREMENT=152;