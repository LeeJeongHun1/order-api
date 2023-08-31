-- User 데이터 생성
INSERT INTO `order`.`user`(id, email, last_login_at, name, passwd, login_count, create_at) VALUES (1, 'tester@gmail.com', '2023-08-31 06:25:00.013', 'tester', '$2a$10$s2K4XtfinPjyhxooLR45feoQIcXdnYDFfgCTYv4yweaTaUb1WVM/K', 0, '2023-08-23 04:42:45.682');
INSERT INTO `order`.`user`(id, email, last_login_at, name, passwd, login_count, create_at) VALUES (2, 'user@gmail.com', '2023-08-31 06:03:41.487', 'jhlee', '$2a$10$yVO.ahu7yMTaCywzC7Q3BeYCQ0jeOLzIye40jnH1/Ej3wtKEFGtHy', 0, '2023-08-31 09:54:51.348');


-- Role 데이터 생성
INSERT INTO `role` (id, user_id, role_type) VALUES(1, 1, 'ADMIN');
INSERT INTO `role` (id, user_id, role_type) VALUES(2, 1, 'USER');
INSERT INTO `role` (id, user_id, role_type) VALUES(3, 2, 'USER');


-- Product 데이터 생성
INSERT INTO product (id, name, price, stock, review_count, create_at) VALUES(1, '가나 초콜릿', 1200.0, 27, 0, '2023-08-08 13:37:29.455');
INSERT INTO product (id, name, price, stock, review_count, create_at) VALUES(2, '프링글스 어니언', 5200.0, 18, 0, '2023-08-08 13:37:29.462');
INSERT INTO product (id, name, price, stock, review_count, create_at) VALUES(3, 'Macbook m1 air', 1250000.0, 6, 0, '2023-08-08 13:37:29.462');
INSERT INTO product (id, name, price, stock, review_count, create_at) VALUES(4, 'Macbook m1 pro', 1450000.0, 10, 0, '2023-08-08 13:37:29.462');
INSERT INTO product (id, name, price, stock, review_count, create_at) VALUES(5, '종이컵(중)', 1500.0, 200, 0, '2023-08-08 13:37:29.462');
INSERT INTO product (id, name, price, stock, review_count, create_at) VALUES(6, '종이컵(소)', 1000.0, 140, 0, '2023-08-08 13:37:29.462');
INSERT INTO product (id, name, price, stock, review_count, create_at) VALUES(7, '여행용 파우치', 17900.0, 42, 1, '2023-08-08 13:37:29.462');
INSERT INTO product (id, name, price, stock, review_count, create_at) VALUES(8, '여행용 캐리어 24인치', 57500.0, 37, 0, '2023-08-08 13:37:29.462');
INSERT INTO product (id, name, price, stock, review_count, create_at) VALUES(9, '멀티 어댑터 5구', 8900.0, 87, 0, '2023-08-08 13:37:29.462');
INSERT INTO product (id, name, price, stock, review_count, create_at) VALUES(10, '블루투스 마우스', 19900.0, 48, 0, '2023-08-08 13:37:29.462');
INSERT INTO product (id, name, price, stock, review_count, create_at) VALUES(11, '노트북 받침대', 38800.0, 110, 0, '2023-08-08 13:37:29.462');

-- Review 데이터 생성
INSERT INTO review (id, user_id, product_id, content, create_at) VALUES(1, 2, 7, '잘 사용하겠습니다.', '2023-08-31 06:04:16.189');

-- Order 데이터 생성
INSERT INTO orders (id, user_id, total_amount, create_at) VALUES(1, 1, 14000.0, '2023-08-08 05:37:15.445');
INSERT INTO orders (id, user_id, total_amount, create_at) VALUES(2, 2, 77400.0, '2023-08-08 05:37:15.445');
INSERT INTO orders (id, user_id, total_amount, create_at) VALUES(3, 2, 1250000.0, '2023-08-08 05:37:15.445');
INSERT INTO orders (id, user_id, total_amount, create_at) VALUES(4, 2, 1450000.0, '2023-08-08 05:37:15.445');
INSERT INTO orders (id, user_id, total_amount, create_at) VALUES(5, 2, 8900.0, '2023-08-08 05:37:15.445');
INSERT INTO orders (id, user_id, total_amount, create_at) VALUES(6, 2, 17900.0, '2023-08-08 05:37:15.445');
INSERT INTO orders (id, user_id, total_amount, create_at) VALUES(7, 2, 38800.0, '2023-08-08 05:37:15.445');
INSERT INTO orders (id, user_id, total_amount, create_at) VALUES(8, 2, 10000.0, '2023-08-08 05:37:15.445');
INSERT INTO orders (id, user_id, total_amount, create_at) VALUES(9, 2, 17900.0, '2023-08-08 05:37:15.445');


-- OrderDetail 데이터 생성
INSERT INTO order_detail (id, order_id, product_id, review_id, product_name, price, quantity, state, reject_msg, completed_at, rejected_at, create_at) VALUES(1, 1, 1, NULL, '가나 초콜릿', 3600.0, 3, 'REQUESTED', NULL, NULL, NULL, '2023-08-08 05:37:15.513');
INSERT INTO order_detail (id, order_id, product_id, review_id, product_name, price, quantity, state, reject_msg, completed_at, rejected_at, create_at) VALUES(2, 1, 2, NULL, '프링글스 어니언', 10400.0, 2, 'COMPLETED', NULL, '2023-08-10 01:17:37.067', NULL, '2023-08-08 05:37:15.513');
INSERT INTO order_detail (id, order_id, product_id, review_id, product_name, price, quantity, state, reject_msg, completed_at, rejected_at, create_at) VALUES(3, 2, 10, NULL, '블루투스 마우스', 19900.0, 1, 'REQUESTED', NULL, NULL, NULL, '2023-08-08 05:37:15.513');
INSERT INTO order_detail (id, order_id, product_id, review_id, product_name, price, quantity, state, reject_msg, completed_at, rejected_at, create_at) VALUES(4, 2, 8, NULL, '여행용 캐리어 24인치', 57500.0, 1, 'REQUESTED', NULL, NULL, NULL, '2023-08-08 05:37:15.513');
INSERT INTO order_detail (id, order_id, product_id, review_id, product_name, price, quantity, state, reject_msg, completed_at, rejected_at, create_at) VALUES(5, 3, 3, NULL, 'Macbook m1 air', 1250000.0, 1, 'REQUESTED', NULL, NULL, NULL, '2023-08-08 05:37:15.513');
INSERT INTO order_detail (id, order_id, product_id, review_id, product_name, price, quantity, state, reject_msg, completed_at, rejected_at, create_at) VALUES(6, 4, 4, NULL, 'Macbook m1 pro', 1450000.0, 1, 'REQUESTED', NULL, NULL, NULL, '2023-08-08 05:37:15.513');
INSERT INTO order_detail (id, order_id, product_id, review_id, product_name, price, quantity, state, reject_msg, completed_at, rejected_at, create_at) VALUES(7, 5, 9, NULL, '멀티 어댑터 5구', 8900.0, 1, 'REQUESTED', NULL, NULL, NULL, '2023-08-08 05:37:15.513');
INSERT INTO order_detail (id, order_id, product_id, review_id, product_name, price, quantity, state, reject_msg, completed_at, rejected_at, create_at) VALUES(8, 6, 7, NULL, '여행용 파우치', 17900.0, 1, 'REQUESTED', NULL, NULL, NULL, '2023-08-08 05:37:15.513');
INSERT INTO order_detail (id, order_id, product_id, review_id, product_name, price, quantity, state, reject_msg, completed_at, rejected_at, create_at) VALUES(9, 7, 11, NULL, '노트북 받침대', 38800.0, 1, 'REQUESTED', NULL, NULL, NULL, '2023-08-08 05:37:15.513');
INSERT INTO order_detail (id, order_id, product_id, review_id, product_name, price, quantity, state, reject_msg, completed_at, rejected_at, create_at) VALUES(10, 8, 6, NULL, '종이컵(소)', 10000.0, 10, 'COMPLETED', NULL, '2023-08-10 01:17:37.067', NULL, '2023-08-08 05:37:15.513');
INSERT INTO order_detail (id, order_id, product_id, review_id, product_name, price, quantity, state, reject_msg, completed_at, rejected_at, create_at) VALUES(11, 9, 7, 1, '여행용 파우치', 17900.0, 1, 'COMPLETED', NULL, '2023-08-10 01:17:37.067', NULL, '2023-08-08 05:37:15.513');
