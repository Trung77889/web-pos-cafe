-- SCHEMA
CREATE DATABASE IF NOT EXISTS `zerostar_cf` CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `zerostar_cf`;

-- Lookup roles (only code needed; UI i18n will translate)
CREATE TABLE role_codes (
  code VARCHAR(32) PRIMARY KEY,
  description VARCHAR(100)
) ENGINE=InnoDB;

CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  email VARCHAR(190) UNIQUE,
  username VARCHAR(64) NOT NULL UNIQUE,
  password_hash VARCHAR(255),
  oauth_provider VARCHAR(32),
  oauth_id VARCHAR(190),
  status VARCHAR(16) NOT NULL DEFAULT 'active',
  is_super_admin BOOLEAN NOT NULL DEFAULT FALSE,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE stores (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(120) NOT NULL,
  address VARCHAR(255),
  status VARCHAR(16) NOT NULL DEFAULT 'open',
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE user_store_roles (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  store_id BIGINT NOT NULL,
  role_code VARCHAR(32) NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uq_user_store (user_id, store_id),
  KEY idx_role (role_code),
  CONSTRAINT fk_usr_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_usr_store FOREIGN KEY (store_id) REFERENCES stores(id),
  CONSTRAINT fk_usr_role FOREIGN KEY (role_code) REFERENCES role_codes(code)
) ENGINE=InnoDB;

CREATE TABLE categories (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(80) NOT NULL,
  order_index INT NOT NULL DEFAULT 1,
  is_active BOOLEAN NOT NULL DEFAULT TRUE
) ENGINE=InnoDB;

CREATE TABLE menu_items (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  category_id BIGIbooking_itemsNT NOT NULL,
  name VARCHAR(120) NOT NULL,
  image_url VARCHAR(255),
  description TEXT,
  base_price INT NOT NULL,
  unit VARCHAR(16) NOT NULL DEFAULT 'ly',
  is_active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_menu_category FOREIGN KEY (category_id) REFERENCES categories(id)
) ENGINE=InnoDB;

CREATE TABLE option_groups (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(80) NOT NULL,
  type VARCHAR(32) NOT NULL,
  is_required BOOLEAN NOT NULL DEFAULT FALSE,
  min_select INT NOT NULL DEFAULT 0,
  max_select INT NOT NULL DEFAULT 1
) ENGINE=InnoDB;

CREATE TABLE option_values (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  option_group_id BIGINT NOT NULL,
  name VARCHAR(80) NOT NULL,
  price_delta INT NOT NULL DEFAULT 0,
  is_active BOOLEAN NOT NULL DEFAULT TRUE,
  CONSTRAINT fk_optval_group FOREIGN KEY (option_group_id) REFERENCES option_groups(id)
) ENGINE=InnoDB;

CREATE TABLE item_option_groups (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  menu_item_id BIGINT NOT NULL,
  option_group_id BIGINT NOT NULL,
  UNIQUE KEY uq_item_group (menu_item_id, option_group_id),
  CONSTRAINT fk_iog_item FOREIGN KEY (menu_item_id) REFERENCES menu_items(id),
  CONSTRAINT fk_iog_group FOREIGN KEY (option_group_id) REFERENCES option_groups(id)
) ENGINE=InnoDB;

CREATE TABLE store_menu_items (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  store_id BIGINT NOT NULL,
  menu_item_id BIGINT NOT NULL,
  in_menu BOOLEAN NOT NULL DEFAULT TRUE,
  availability_status ENUM('available','sold_out') NOT NULL DEFAULT 'available',
  sold_out_until DATETIME NULL,
  sold_out_note VARCHAR(160),
  UNIQUE KEY uq_store_item (store_id, menu_item_id),
  CONSTRAINT fk_smi_store FOREIGN KEY (store_id) REFERENCES stores(id),
  CONSTRAINT fk_smi_item FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
) ENGINE=InnoDB;

CREATE TABLE price_change_request_items (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  store_id BIGINT NOT NULL,
  menu_item_id BIGINT NOT NULL,
  requested_price INT NOT NULL,
  valid_from DATETIME NOT NULL,
  valid_to DATETIME NOT NULL,
  status ENUM('pending','approved','rejected','canceled') NOT NULL DEFAULT 'pending',
  requested_by BIGINT NOT NULL,
  reviewed_by BIGINT NULL,
  review_note VARCHAR(255),
  requested_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  reviewed_at DATETIME NULL,
  CONSTRAINT fk_prq_store FOREIGN KEY (store_id) REFERENCES stores(id),
  CONSTRAINT fk_prq_item FOREIGN KEY (menu_item_id) REFERENCES menu_items(id),
  CONSTRAINT fk_prq_reqby FOREIGN KEY (requested_by) REFERENCES users(id),
  CONSTRAINT fk_prq_revby FOREIGN KEY (reviewed_by) REFERENCES users(id)
) ENGINE=InnoDB;

CREATE TABLE store_item_price_schedules (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  store_id BIGINT NOT NULL,
  menu_item_id BIGINT NOT NULL,
  price INT NOT NULL,
  valid_from DATETIME NOT NULL,
  valid_to DATETIME NOT NULL,
  approved_by BIGINT NOT NULL,
  approved_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  KEY idx_price_window (store_id, menu_item_id, valid_from, valid_to),
  CONSTRAINT fk_sips_store FOREIGN KEY (store_id) REFERENCES stores(id),
  CONSTRAINT fk_sips_item FOREIGN KEY (menu_item_id) REFERENCES menu_items(id),
  CONSTRAINT fk_sips_approved FOREIGN KEY (approved_by) REFERENCES users(id)
) ENGINE=InnoDB;

CREATE TABLE store_option_values (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  store_id BIGINT NOT NULL,
  option_value_id BIGINT NOT NULL,
  is_active BOOLEAN NOT NULL DEFAULT TRUE,
  availability_status ENUM('available','sold_out') NOT NULL DEFAULT 'available',
  sold_out_until DATETIME NULL,
  note VARCHAR(160),
  UNIQUE KEY uq_store_optval (store_id, option_value_id),
  CONSTRAINT fk_sov_store FOREIGN KEY (store_id) REFERENCES stores(id),
  CONSTRAINT fk_sov_optval FOREIGN KEY (option_value_id) REFERENCES option_values(id)
) ENGINE=InnoDB;

CREATE TABLE zones (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  store_id BIGINT NOT NULL,
  name VARCHAR(80) NOT NULL,
  seat_fee_type ENUM('hourly','fixed','none') NOT NULL DEFAULT 'hourly',
  seat_fee_value INT NOT NULL DEFAULT 0,
  CONSTRAINT fk_zone_store FOREIGN KEY (store_id) REFERENCES stores(id)
) ENGINE=InnoDB;

CREATE TABLE tables_ (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  zone_id BIGINT NOT NULL,
  table_uid VARCHAR(40) NOT NULL,
  capacity INT NOT NULL DEFAULT 2,
  chair_type VARCHAR(32),
  position_note VARCHAR(120),
  UNIQUE KEY uq_zone_tableuid (zone_id, table_uid),
  CONSTRAINT fk_table_zone FOREIGN KEY (zone_id) REFERENCES zones(id)
) ENGINE=InnoDB;

CREATE TABLE bookings (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  store_id BIGINT NOT NULL,
  table_id BIGINT NOT NULL,
  user_id BIGINT NULL,
  start_at DATETIME NOT NULL,
  end_at DATETIME NOT NULL,
  status ENUM('pending','confirmed','seated','no_show','canceled','expired') NOT NULL DEFAULT 'pending',
  deposit_amount INT NOT NULL DEFAULT 0,
  seat_fee_snapshot INT NOT NULL DEFAULT 0,
  late_cancel_threshold_pct INT NOT NULL DEFAULT 50,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_booking_store FOREIGN KEY (store_id) REFERENCES stores(id),
  CONSTRAINT fk_booking_table FOREIGN KEY (table_id) REFERENCES tables_(id),
  CONSTRAINT fk_booking_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;

CREATE TABLE booking_items (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  booking_id BIGINT NOT NULL,
  menu_item_id BIGINT NOT NULL,
  qty INT NOT NULL DEFAULT 1,
  unit_price_snapshot INT NOT NULL,
  payment_status ENUM('unpaid','paid','canceled') NOT NULL DEFAULT 'unpaid',
  note VARCHAR(160),
  CONSTRAINT fk_bi_booking FOREIGN KEY (booking_id) REFERENCES bookings(id),
  CONSTRAINT fk_bi_item FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
) ENGINE=InnoDB;

CREATE TABLE orders (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  store_id BIGINT NOT NULL,
  table_id BIGINT NULL,
  user_id BIGINT NULL,
  booking_id BIGINT NULL,
  status ENUM('open','served','partial_paid','paid','void') NOT NULL DEFAULT 'open',
  opened_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  closed_at DATETIME NULL,
  source ENUM('qr','staff_pos','kiosk','web') NOT NULL DEFAULT 'qr',
  CONSTRAINT fk_order_store FOREIGN KEY (store_id) REFERENCES stores(id),
  CONSTRAINT fk_order_table FOREIGN KEY (table_id) REFERENCES tables_(id),
  CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_order_booking FOREIGN KEY (booking_id) REFERENCES bookings(id)
) ENGINE=InnoDB;

CREATE TABLE order_items (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  order_id BIGINT NOT NULL,
  menu_item_id BIGINT NOT NULL,
  qty INT NOT NULL DEFAULT 1,
  unit_price_snapshot INT NOT NULL,
  note VARCHAR(160),
  CONSTRAINT fk_oi_order FOREIGN KEY (order_id) REFERENCES orders(id),
  CONSTRAINT fk_oi_item FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
) ENGINE=InnoDB;

CREATE TABLE payments (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  source_type ENUM('order','booking') NOT NULL,
  order_id BIGINT NULL,
  booking_id BIGINT NULL,
  user_id BIGINT NULL,
  method ENUM('cash','card','momo','vnpay','zalo','wallet') NOT NULL,
  type ENUM('deposit','prepaid','remaining','refund') NOT NULL,
  status ENUM('pending','paid','failed','refunded') NOT NULL DEFAULT 'pending',
  amount INT NOT NULL,
  redeem_points_used INT NOT NULL DEFAULT 0,
  redeem_value INT NOT NULL DEFAULT 0,
  paid_at DATETIME NULL,
  txn_ref VARCHAR(64),
  CONSTRAINT fk_pay_order FOREIGN KEY (order_id) REFERENCES orders(id),
  CONSTRAINT fk_pay_booking FOREIGN KEY (booking_id) REFERENCES bookings(id),
  CONSTRAINT fk_pay_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;

CREATE TABLE loyalty_accounts (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL UNIQUE,
  points_balance INT NOT NULL DEFAULT 0,
  CONSTRAINT fk_la_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;

CREATE TABLE loyalty_transactions (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  store_id BIGINT NULL,
  type ENUM('earn','redeem','expire','adjust') NOT NULL,
  points INT NOT NULL,
  payment_id BIGINT NULL,
  expiry_date DATE NULL,
  reason VARCHAR(160),
  occurred_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_lt_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_lt_store FOREIGN KEY (store_id) REFERENCES stores(id),
  CONSTRAINT fk_lt_payment FOREIGN KEY (payment_id) REFERENCES payments(id)
) ENGINE=InnoDB;

CREATE TABLE review_stores (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  store_id BIGINT NOT NULL,
  order_id BIGINT NULL,
  rating INT NOT NULL,
  content TEXT,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_rs_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_rs_store FOREIGN KEY (store_id) REFERENCES stores(id),
  CONSTRAINT fk_rs_order FOREIGN KEY (order_id) REFERENCES orders(id)
) ENGINE=InnoDB;

CREATE TABLE review_items (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  menu_item_id BIGINT NOT NULL,
  store_id BIGINT NULL,
  order_item_id BIGINT NULL,
  rating INT NOT NULL,
  content TEXT,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_ri_user FOREIGN KEY (user_id) REFERENCES users(id),
  CONSTRAINT fk_ri_item FOREIGN KEY (menu_item_id) REFERENCES menu_items(id),
  CONSTRAINT fk_ri_store FOREIGN KEY (store_id) REFERENCES stores(id),
  CONSTRAINT fk_ri_oi FOREIGN KEY (order_item_id) REFERENCES order_items(id)
) ENGINE=InnoDB;


-- =========================
-- SAMPLE DATA (real-ish)
-- =========================

INSERT INTO role_codes(code, description) VALUES
('super_admin','System super admin'),
('owner','Store owner'),
('staff','Store staff'),
('customer','Customer');

-- Users
INSERT INTO users (email, username, password_hash, is_super_admin) VALUES
('super@0all.app','superadmin','***', TRUE),
('ownerA@0all.app','ownerA','***', FALSE),
('staffA@0all.app','staffA','***', FALSE);
-- oauth customer
INSERT INTO users (email, username, oauth_provider, oauth_id) VALUES
('khanh@gmail.com','khanhng','google','1001');

-- Stores
INSERT INTO stores (name, address) VALUES
('0ALL – A','12 ABC, Thủ Đức, TP.HCM'),
('0ALL – B','34 XYZ, Quận 1, TP.HCM');

-- Roles per store
INSERT INTO user_store_roles (user_id, store_id, role_code) VALUES
(2,1,'owner'), (3,1,'staff'), (2,2,'owner');

-- Categories
INSERT INTO categories (name, order_index) VALUES
('Coffee',1), ('Tea',2), ('Milk Tea',3), ('Fruit Tea',4);

-- Global Menu (names/prices inspired by sources, adapted)
INSERT INTO menu_items (category_id, name, image_url, description, base_price, unit) VALUES
-- Coffee
(1,'Latte Classic','/img/latte-classic.jpg','Espresso + sữa tươi',49000,'ly'),
(1,'Latte Hazelnut','/img/latte-hazelnut.jpg','Latte vị hạt phỉ',59000,'ly'),
(1,'Cold Brew','/img/coldbrew.jpg','Cà phê ủ lạnh',50000,'ly'),
(1,'Bạc Xỉu','/img/bacxiu.jpg','Sữa đặc + cà phê',49000,'ly'),
-- Tea / Milk Tea
(3,'Trà Sữa Ngô Gia','/img/tra-sua-ngo-gia.jpg','Trà sữa kiểu Đài Loan',29000,'ly'),
(2,'Ô Long Kem Cheese','/img/olong-cheese.jpg','Ô long + kem cheese',26000,'ly'),
(4,'Trà Đào Cam Sả','/img/peach-tea.jpg','Trà trái cây đào cam sả',45000,'ly'),
(2,'Matcha Latte','/img/matcha-latte.jpg','Matcha + sữa',55000,'ly');

-- Option groups
INSERT INTO option_groups (name, type, is_required, min_select, max_select) VALUES
('Size','size', TRUE, 1, 1),
('Sugar','sugar', TRUE, 1, 1),
('Ice','ice', TRUE, 1, 1),
('Topping','topping', FALSE, 0, 3);

-- Option values (global)
INSERT INTO option_values (option_group_id, name, price_delta) VALUES
-- Size
(1,'S',0),(1,'M',5000),(1,'L',10000),
-- Sugar
(2,'0%',0),(2,'30%',0),(2,'50%',0),(2,'70%',0),(2,'100%',0),
-- Ice
(3,'Không đá',0),(3,'Ít đá',0),(3,'Vừa đá',0),
-- Topping
(4,'Cream',6000),(4,'Extra Shot',7000),(4,'Trân châu',5000);

-- Link item -> option groups (coffee: Size + Topping; milk tea/fruit tea: Size + Sugar + Ice + Topping)
INSERT INTO item_option_groups (menu_item_id, option_group_id) VALUES
-- Latte Classic
(1,1),(1,4),
-- Latte Hazelnut
(2,1),(2,4),
-- Cold Brew
(3,1),(3,4),
-- Bạc Xỉu
(4,1),(4,4),
-- Trà Sữa Ngô Gia
(5,1),(5,2),(5,3),(5,4),
-- Ô Long Kem Cheese
(6,1),(6,2),(6,3),(6,4),
-- Trà Đào Cam Sả
(7,1),(7,2),(7,3),(7,4),
-- Matcha Latte
(8,1),(8,4);

-- Per-store menu visibility / availability
INSERT INTO store_menu_items (store_id, menu_item_id, in_menu, availability_status, sold_out_until, sold_out_note) VALUES
-- Store A
(1,1, true,'available', NULL, NULL),  -- Latte Classic
(1,2, true,'available', NULL, NULL),  -- Latte Hazelnut
(1,3, true,'sold_out','2025-10-06 09:00:00','Hết hạt, mai có lại'), -- Cold Brew
(1,4, true,'available', NULL, NULL),  -- Bạc Xỉu
(1,5, true,'available', NULL, NULL),  -- Trà Sữa Ngô Gia
(1,6, true,'available', NULL, NULL),  -- Ô Long Kem Cheese
(1,7, true,'available', NULL, NULL),  -- Trà Đào Cam Sả
(1,8, true,'available', NULL, NULL),  -- Matcha Latte
-- Store B (giản lược)
(2,1, true,'available', NULL, NULL),
(2,5, true,'available', NULL, NULL),
(2,7, true,'available', NULL, NULL);

-- Store-level option availability (ví dụ: Store A tạm hết Trân châu)
INSERT INTO store_option_values (store_id, option_value_id, is_active, availability_status, sold_out_until, note) VALUES
(1, (SELECT id FROM option_values WHERE name='Trân châu' LIMIT 1), true, 'sold_out','2025-10-06 09:00:00','Hết trân châu tạm thời');

-- Owner A đề xuất đổi giá Latte Classic tháng 10, SuperAdmin duyệt
INSERT INTO price_change_request_items (store_id, menu_item_id, requested_price, valid_from, valid_to, status, requested_by, reviewed_by, review_note, requested_at, reviewed_at)
VALUES
(1,1,47000,'2025-10-10 00:00:00','2025-10-31 23:59:59','approved',2,1,'Ưu đãi tháng 10', NOW(), NOW());

INSERT INTO store_item_price_schedules (store_id, menu_item_id, price, valid_from, valid_to, approved_by)
VALUES
(1,1,47000,'2025-10-10 00:00:00','2025-10-31 23:59:59',1);

-- Zones & tables (Store A)
INSERT INTO zones (store_id, name, seat_fee_type, seat_fee_value) VALUES
(1,'Lặng lẽ','hourly',15000),
(1,'Ồn ào','hourly',10000);

INSERT INTO tables_ (zone_id, table_uid, capacity, chair_type, position_note) VALUES
(1,'A-Q1',2,'ergonomic','Vách ngăn'),
(2,'A-N3',4,'normal','Gần quầy');

-- Booking with 50% deposit of 2h x 15k = 30k -> deposit 15k
INSERT INTO bookings (store_id, table_id, user_id, start_at, end_at, status, deposit_amount, seat_fee_snapshot, late_cancel_threshold_pct)
VALUES
(1,1, (SELECT id FROM users WHERE username='khanhng'), '2025-10-06 09:00:00','2025-10-06 11:00:00','confirmed',15000,30000,50);

-- Pre-order 1 Matcha (unpaid)
INSERT INTO booking_items (booking_id, menu_item_id, qty, unit_price_snapshot, payment_status, note)
VALUES
(1, (SELECT id FROM menu_items WHERE name='Matcha Latte'), 1, 55000, 'unpaid','Size M');

-- Payment for booking deposit
INSERT INTO payments (source_type, booking_id, user_id, method, type, status, amount, paid_at, txn_ref)
VALUES
('booking', 1, (SELECT id FROM users WHERE username='khanhng'), 'vnpay', 'deposit', 'paid', 15000, '2025-10-06 08:10:00', 'VNP-BOOK1');

-- Walk-in order at A-N3 (QR)
INSERT INTO orders (store_id, table_id, user_id, status, opened_at, closed_at, source)
VALUES
(1, (SELECT id FROM tables_ WHERE table_uid='A-N3' LIMIT 1), (SELECT id FROM users WHERE username='khanhng'), 'paid', '2025-10-05 09:15:00','2025-10-05 09:40:00','qr');

-- 1 Latte Classic (base 49k) + 1 Matcha Latte (55k) -> snapshot per item (option deltas giữ ở client/chi tiết mở rộng)
INSERT INTO order_items (order_id, menu_item_id, qty, unit_price_snapshot, note) VALUES
(1, (SELECT id FROM menu_items WHERE name='Latte Classic'), 1, 49000, 'Size L + Cream'),
(1, (SELECT id FROM menu_items WHERE name='Matcha Latte'), 1, 55000, 'Size M');

-- Payment for the order (MoMo)
INSERT INTO payments (source_type, order_id, user_id, method, type, status, amount, paid_at, txn_ref)
VALUES
('order', 1, (SELECT id FROM users WHERE username='khanhng'), 'momo', 'remaining', 'paid', 71000, '2025-10-05 09:40:00', 'MOMO-OR1');

-- Loyalty: account + earn 7 pts (10k = 1pt), expiry +6 months
INSERT INTO loyalty_accounts (user_id, points_balance)
VALUES ((SELECT id FROM users WHERE username='khanhng'), 7)
ON DUPLICATE KEY UPDATE points_balance = 7;

INSERT INTO loyalty_transactions (user_id, store_id, type, points, payment_id, expiry_date, reason, occurred_at)
VALUES
((SELECT id FROM users WHERE username='khanhng'), 1, 'earn', 7, (SELECT id FROM payments WHERE txn_ref='MOMO-OR1'), DATE_ADD('2025-10-05', INTERVAL 6 MONTH), '10k = 1 điểm', '2025-10-05 09:40:00');

-- Reviews
INSERT INTO review_stores (user_id, store_id, order_id, rating, content, created_at)
VALUES
((SELECT id FROM users WHERE username='khanhng'), 1, 1, 5, 'Không gian yên tĩnh, phục vụ tốt', '2025-10-05 10:00:00');

INSERT INTO review_items (user_id, menu_item_id, store_id, order_item_id, rating, content, created_at)
VALUES
((SELECT id FROM users WHERE username='khanhng'),
 (SELECT id FROM menu_items WHERE name='Latte Classic'),
 1,
 (SELECT id FROM order_items WHERE note LIKE 'Size L%' LIMIT 1),
 5, 'Latte size L rất ngon', '2025-10-05 10:02:00');
