-- `order`.`order` definition

CREATE TABLE `order` (
                         `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                         `user_id` int(10) unsigned NOT NULL,
                         `product_id` int(10) unsigned NOT NULL,
                         `review_id` int(10) unsigned NOT NULL,
                         `state` enum('REQUESTED','ACCEPTED','SHIPPING','COMPLETED','REJECTED') NOT NULL,
                         `request_msg` varchar(255) DEFAULT NULL,
                         `reject_msg` varchar(255) DEFAULT NULL,
                         `rejected_at` datetime DEFAULT NULL,
                         `completed_at` datetime DEFAULT NULL,
                         `create_at` datetime NOT NULL DEFAULT current_timestamp(),
                         PRIMARY KEY (`id`),
                         KEY `orders_FK` (`product_id`),
                         KEY `orders_FK_1` (`review_id`),
                         KEY `orders_FK_2` (`user_id`),
                         CONSTRAINT `orders_FK` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON UPDATE CASCADE,
                         CONSTRAINT `orders_FK_1` FOREIGN KEY (`review_id`) REFERENCES `review` (`id`) ON UPDATE CASCADE,
                         CONSTRAINT `orders_FK_2` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;




-- `order`.product definition

CREATE TABLE `product` (
                           `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                           `name` varchar(100) NOT NULL,
                           `price` double NOT NULL DEFAULT 0,
                           `stock` int(11) NOT NULL DEFAULT 0,
                           `review_count` int(11) NOT NULL DEFAULT 0,
                           `create_at` datetime NOT NULL DEFAULT current_timestamp(),
                           PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


-- `order`.review definition

CREATE TABLE `review` (
                          `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                          `user_id` int(10) unsigned NOT NULL,
                          `product_id` int(10) unsigned NOT NULL,
                          `content` varchar(255) NOT NULL,
                          `create_at` datetime NOT NULL DEFAULT current_timestamp(),
                          PRIMARY KEY (`id`),
                          KEY `reviews_FK` (`user_id`),
                          KEY `reviews_FK_1` (`product_id`),
                          CONSTRAINT `reviews_FK` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON UPDATE CASCADE,
                          CONSTRAINT `reviews_FK_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



-- `order`.`user` definition

CREATE TABLE `user` (
                        `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
                        `name` varchar(255) NOT NULL,
                        `email` varchar(255) NOT NULL,
                        `passwd` varchar(255) NOT NULL,
                        `login_count` int(10) unsigned NOT NULL DEFAULT 0,
                        `last_login_at` datetime DEFAULT NULL,
                        `create_at` datetime NOT NULL DEFAULT current_timestamp(),
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;