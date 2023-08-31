-- `order`.orders definition

CREATE TABLE `orders` (
                          `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                          `user_id` int(10) unsigned NOT NULL,
                          `total_amount` double NOT NULL,
                          `create_at` datetime(6) NOT NULL DEFAULT current_timestamp(6),
                          PRIMARY KEY (`id`),
                          KEY `order_FK` (`user_id`),
                          CONSTRAINT `order_FK` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- `order`.order_detail definition

CREATE TABLE `order_detail` (
                                `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                                `order_id` int(10) unsigned NOT NULL,
                                `product_id` int(10) unsigned NOT NULL,
                                `review_id` int(10) unsigned DEFAULT NULL,
                                `product_name` varchar(100) NOT NULL,
                                `price` double NOT NULL,
                                `quantity` int(11) NOT NULL,
                                `state` enum('REQUESTED','SHIPPING','REJECT','COMPLETED') NOT NULL,
                                `reject_msg` varchar(100) DEFAULT NULL,
                                `completed_at` datetime(6) DEFAULT NULL,
                                `rejected_at` datetime(6) DEFAULT NULL,
                                `create_at` datetime(6) NOT NULL DEFAULT current_timestamp(6),
                                PRIMARY KEY (`id`),
                                KEY `order_detail_FK` (`order_id`),
                                KEY `order_detail_FK_1` (`product_id`),
                                KEY `order_detail_FK_2` (`review_id`),
                                CONSTRAINT `order_detail_FK` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
                                CONSTRAINT `order_detail_FK_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
                                CONSTRAINT `order_detail_FK_2` FOREIGN KEY (`review_id`) REFERENCES `review` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- `order`.product definition

CREATE TABLE `product` (
                           `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                           `name` varchar(100) NOT NULL,
                           `price` double NOT NULL DEFAULT 0,
                           `stock` int(11) NOT NULL DEFAULT 0,
                           `review_count` int(11) NOT NULL DEFAULT 0,
                           `create_at` datetime(6) NOT NULL DEFAULT current_timestamp(6),
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- `order`.review definition

CREATE TABLE `review` (
                          `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                          `user_id` int(10) unsigned NOT NULL,
                          `product_id` int(10) unsigned NOT NULL,
                          `content` varchar(255) DEFAULT NULL,
                          `create_at` datetime(6) NOT NULL DEFAULT current_timestamp(6),
                          PRIMARY KEY (`id`),
                          KEY `review_FK_1` (`product_id`),
                          KEY `review_FK` (`user_id`),
                          CONSTRAINT `review_FK` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
                          CONSTRAINT `review_FK_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- `order`.`user` definition

CREATE TABLE `user` (
                        `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                        `email` varchar(255) DEFAULT NULL,
                        `name` varchar(255) DEFAULT NULL,
                        `passwd` varchar(255) DEFAULT NULL,
                        `login_count` int(11) NOT NULL DEFAULT 0,
                        `last_login_at` datetime(6) DEFAULT NULL,
                        `create_at` datetime(6) NOT NULL DEFAULT current_timestamp(6),
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `user_UN` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- `orders`.`role` definition

CREATE TABLE `role` (
                        `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                        `user_id` int(10) unsigned NOT NULL,
                        `role_type` enum('USER','ADMIN') NOT NULL,
                        PRIMARY KEY (`id`),
                        KEY `role_FK` (`user_id`),
                        CONSTRAINT `role_FK` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;