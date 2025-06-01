CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    username VARCHAR(255) NOT NULL UNIQUE,
    firstname VARCHAR(255),
    lastname VARCHAR(255),
    password VARCHAR(255),
    country VARCHAR(255),
    city VARCHAR(255),
    address VARCHAR(255),
    postal_code VARCHAR(20),
    avatar_color VARCHAR(20),
    receive_news_letters BOOLEAN DEFAULT FALSE,
    is_locked BOOLEAN DEFAULT FALSE,
    is_enabled BOOLEAN DEFAULT TRUE,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE accounts (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    currency VARCHAR(20) NOT NULL,
    current_balance DOUBLE PRECISION NOT NULL,
    type VARCHAR(255) NOT NULL,
    avatar_color VARCHAR(20),
    user_id UUID NOT NULL,
    CONSTRAINT fk_accounts_user FOREIGN KEY(user_id) REFERENCES users(id)
);

CREATE TABLE user_accounts (
    user_id UUID NOT NULL,
    account_id UUID NOT NULL,
    PRIMARY KEY (user_id, account_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (account_id) REFERENCES accounts(id) ON DELETE CASCADE
);

CREATE TABLE account_addition_requests (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    owner_user_id UUID NOT NULL,
    requested_user_id UUID NOT NULL,
    account_name VARCHAR(255) NOT NULL,
    is_request_approved BOOLEAN DEFAULT FALSE,
    is_request_checked BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_owner_user FOREIGN KEY(owner_user_id) REFERENCES users(id),
    CONSTRAINT fk_requested_user FOREIGN KEY(requested_user_id) REFERENCES users(id)
);

CREATE TABLE record_categories (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    description TEXT
);

CREATE TABLE budgets (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    amount DOUBLE PRECISION NOT NULL CHECK (amount >= 1),
    user_id UUID NOT NULL,
    CONSTRAINT fk_budgets_user FOREIGN KEY(user_id) REFERENCES users(id)
);

CREATE TABLE budget_record_categories (
    budget_id UUID NOT NULL,
    record_category_id UUID NOT NULL,
    PRIMARY KEY (budget_id, record_category_id),
    FOREIGN KEY (budget_id) REFERENCES budgets(id) ON DELETE CASCADE,
    FOREIGN KEY (record_category_id) REFERENCES record_categories(id) ON DELETE CASCADE
);

CREATE TABLE email_auth_tokens (
    token UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    is_used BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_eat_user FOREIGN KEY(user_id) REFERENCES users(id)
);

CREATE TABLE records (
     id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
     amount DOUBLE PRECISION NOT NULL,
     user_id UUID NOT NULL,
     payment_time TIMESTAMP,
     category_id UUID,
     type VARCHAR(255) NOT NULL,
     note TEXT,
     currency VARCHAR(20) NOT NULL,
     withdrawal_account_id UUID,
     reciving_account_id UUID,
     CONSTRAINT fk_records_user FOREIGN KEY(user_id) REFERENCES users(id),
     CONSTRAINT fk_records_category FOREIGN KEY(category_id) REFERENCES record_categories(id),
     CONSTRAINT fk_records_withdrawal_account FOREIGN KEY(withdrawal_account_id) REFERENCES accounts(id),
     CONSTRAINT fk_records_reciving_account FOREIGN KEY(reciving_account_id) REFERENCES accounts(id)
);


INSERT INTO record_categories (id,name,description) VALUES
('4b1e0ad3-0d8a-4f8a-b93e-cb2e2aee5b0b','Groceries','Expenses for food, beverages, and household supplies purchased from supermarkets or grocery stores.'),
('99a013c5-3346-44e7-9cb9-4b3e5e00271b','Rent','Monthly payments for apartment, house, or office space rentals.'),
('a417a045-686e-4ab6-bbf1-1e5b33d2be93','Utilities','Bills for electricity, water, gas, heating, and garbage collection.'),
('3d405321-87be-4e89-b57e-fbfa9579f15d','Transportation','Expenses related to public transit, fuel, taxi, ride-sharing, and vehicle maintenance.'),
('db73bb60-f9d8-40c3-84a5-4f1bdb62bc27','Healthcare','Medical and dental expenses, including insurance, medication, and doctor visits.'),
('e39cf8e8-b99b-4650-928d-cf89f74e4203','Dining Out','Money spent at restaurants, cafes, bars, and fast-food outlets.'),
('4d414573-3b45-4b22-bb4c-01d348378b5d','Education','Costs for tuition, courses, books, and other educational materials or fees.'),
('80f246c4-8be8-40ea-aad7-0f155c815a64','Insurance','Premiums for health, car, home, and other insurance policies.'),
('ac6a9441-2d67-4416-90aa-3b9ee6e5e144','Entertainment','Spending on movies, music, streaming, sports, and other recreational activities.'),
('ff41c66e-5ff4-4c0d-b3a7-334b8c4271d5','Clothing','Purchases of clothes, shoes, and accessories.'),
('260b5fc8-00f4-46a6-9ecf-986e37c0d7e0','Personal Care','Expenses for haircuts, cosmetics, toiletries, and personal grooming.'),
('b6f0134f-7497-4c5f-b4e8-5d0b2a8b7bc2','Travel','Costs related to travel, such as flights, hotels, and vacation activities.'),
('a285d771-01d2-4055-9b42-cc54f9a4b2f1','Household Maintenance','Repairs, cleaning, and improvements for your living space.'),
('129e13f2-637e-4dc6-bcf7-6e7dbebc13b9','Childcare','Spending on daycare, babysitters, and children’s activities.'),
('f99e6af1-2b81-4915-a701-3fbc378e1e7b','Gifts & Donations','Money given as gifts or to charity and donations.'),
('9c0f6e10-3cd1-4940-b2ee-419f10f7fd9c','Subscriptions','Recurring expenses for magazines, newspapers, streaming services, and memberships.'),
('b6ef6b15-c2e3-4fd5-bb6a-35bbcbc13e51','Phone & Internet','Bills for mobile, landline, and internet services.'),
('00a7e3e8-e042-40b9-9a25-3a6eb3fd3097','Taxes','Payments for income, property, or other taxes.'),
('4a2a01db-ec30-4b34-97a1-8b2f0556a222','Investment','Money spent on stocks, mutual funds, and other investments.'),
('c79a9c62-65c5-4c47-86f1-8a51b8eabdbb','Miscellaneous','Other expenses that do not fit into the main categories.');


INSERT INTO users (id, username, firstname, lastname, password, country, city, address, postal_code, avatar_color, receive_news_letters, is_locked, is_enabled, role) VALUES
('c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','user@budgetmate.com','Ferdinand','Porsche','$2a$10$kJx2dRS0ZnDlMLwaOSuBqOUCLAh2NQm6mDwtxmV.340BeTyfSgULm','United States','New York','123 Main St, Apt 5B','10001','#00008B',FALSE,FALSE,TRUE,'USER');


INSERT INTO accounts (id, name, currency, current_balance, type, avatar_color, user_id) VALUES
('a1e1b1d4-30c2-4313-bde4-215c7be04715', 'General Account', 'USD', 10000.00, 'GENERAL', '#3498db', 'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786'),
('b2f2c2e5-41d3-5424-cfe5-326d8cf15826', 'Cash Wallet', 'USD', 2050.50, 'CASH', '#27ae60', 'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786'),
('c3a3d3f6-52e4-6535-dfe6-437e9df26937', 'Credit Card', 'USD', 328.22, 'CREDIT_CARD', '#e74c3c', 'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786'),
('d4b4e407-63f5-7646-efe7-548f0ef37a48', 'Savings Account', 'USD', 50000.00, 'SAVINGS_ACCOUNT', '#9b59b6', 'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786'),
('e03e18d2-bdbf-4f96-9b5e-cc2c6d97c80c', 'Bonus Account', 'USD', 1000.00, 'BONUS', '#f1c40f', 'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786');

INSERT INTO budgets (id,name,amount,user_id) VALUES
 ('9b2e264d-931b-4a7e-9f87-9a3d58ebcf38','Groceries',400.00,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786'),
 ('2a4c8390-5d7d-478e-bb85-05d25565a4fd','Rent',1600.00,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786'),
 ('7ad5eb8b-1a5b-491e-9c3a-d243ec51644c','Transportation',150.00,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786'),
 ('b378fecd-b49e-4ab1-b021-f0b30c7a9fba','Utilities',200.00,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786'),
 ('db8d7e1c-b98b-454d-b177-2cf5c6eab13f','Dining Out',120.00,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786'),
 ('29fd0e3d-4736-4042-b308-c8b1d7eac7d5','Healthcare',100.00,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786'),
 ('c062cdb8-70b2-4f8c-9951-e9a6973e3900','Entertainment',80.00,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786'),
 ('e4b6f50c-3dce-4f0a-82a3-cf7ca0e58d5e','Clothing',60.00,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786'),
 ('af25cfac-187a-40cf-bb13-29e9ae527e8f','Savings',500.00,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786'),
 ('e20a1b4f-6f9b-4772-90fd-c2d0ab96822f','Travel',300.00,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786');

INSERT INTO budget_record_categories (budget_id, record_category_id) VALUES
('9b2e264d-931b-4a7e-9f87-9a3d58ebcf38','4b1e0ad3-0d8a-4f8a-b93e-cb2e2aee5b0b'),
('9b2e264d-931b-4a7e-9f87-9a3d58ebcf38','a285d771-01d2-4055-9b42-cc54f9a4b2f1'),
('2a4c8390-5d7d-478e-bb85-05d25565a4fd','99a013c5-3346-44e7-9cb9-4b3e5e00271b'),
('2a4c8390-5d7d-478e-bb85-05d25565a4fd','a417a045-686e-4ab6-bbf1-1e5b33d2be93'),
('2a4c8390-5d7d-478e-bb85-05d25565a4fd','80f246c4-8be8-40ea-aad7-0f155c815a64'),
('7ad5eb8b-1a5b-491e-9c3a-d243ec51644c','3d405321-87be-4e89-b57e-fbfa9579f15d'),
('7ad5eb8b-1a5b-491e-9c3a-d243ec51644c','c79a9c62-65c5-4c47-86f1-8a51b8eabdbb'),
('b378fecd-b49e-4ab1-b021-f0b30c7a9fba','a417a045-686e-4ab6-bbf1-1e5b33d2be93'),
('db8d7e1c-b98b-454d-b177-2cf5c6eab13f','e39cf8e8-b99b-4650-928d-cf89f74e4203'),
('db8d7e1c-b98b-454d-b177-2cf5c6eab13f','ac6a9441-2d67-4416-90aa-3b9ee6e5e144'),
('29fd0e3d-4736-4042-b308-c8b1d7eac7d5','db73bb60-f9d8-40c3-84a5-4f1bdb62bc27'),
('29fd0e3d-4736-4042-b308-c8b1d7eac7d5','80f246c4-8be8-40ea-aad7-0f155c815a64'),
('c062cdb8-70b2-4f8c-9951-e9a6973e3900','ac6a9441-2d67-4416-90aa-3b9ee6e5e144'),
('c062cdb8-70b2-4f8c-9951-e9a6973e3900','9c0f6e10-3cd1-4940-b2ee-419f10f7fd9c'),
('e4b6f50c-3dce-4f0a-82a3-cf7ca0e58d5e','ff41c66e-5ff4-4c0d-b3a7-334b8c4271d5'),
('e4b6f50c-3dce-4f0a-82a3-cf7ca0e58d5e','260b5fc8-00f4-46a6-9ecf-986e37c0d7e0'),
('af25cfac-187a-40cf-bb13-29e9ae527e8f','4a2a01db-ec30-4b34-97a1-8b2f0556a222'),
('af25cfac-187a-40cf-bb13-29e9ae527e8f','c79a9c62-65c5-4c47-86f1-8a51b8eabdbb'),
('af25cfac-187a-40cf-bb13-29e9ae527e8f','00a7e3e8-e042-40b9-9a25-3a6eb3fd3097'),
('e20a1b4f-6f9b-4772-90fd-c2d0ab96822f','b6f0134f-7497-4c5f-b4e8-5d0b2a8b7bc2'),
('e20a1b4f-6f9b-4772-90fd-c2d0ab96822f','ac6a9441-2d67-4416-90aa-3b9ee6e5e144'),
('e20a1b4f-6f9b-4772-90fd-c2d0ab96822f','e39cf8e8-b99b-4650-928d-cf89f74e4203');

INSERT INTO records (id,amount,user_id,payment_time,category_id,type,note,currency,withdrawal_account_id,reciving_account_id) VALUES
('6e5a36ad-a13d-4e45-8b57-ec91518d7164',254.83,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-06-06 21:08:44','c79a9c62-65c5-4c47-86f1-8a51b8eabdbb','INCOME','Crockery','USD',NULL,'a1e1b1d4-30c2-4313-bde4-215c7be04715'),
('31ca7b9c-7d56-4a85-a99e-2a690a5f0002',101.58,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-06-21 01:49:44','ff41c66e-5ff4-4c0d-b3a7-334b8c4271d5','EXPENSE','Board games','USD','d4b4e407-63f5-7646-efe7-548f0ef37a48',NULL),
('97827e2e-f624-4316-9b19-d2922ffb61b1',75.45,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-09-25 05:13:44','f99e6af1-2b81-4915-a701-3fbc378e1e7b','EXPENSE','Cryptocurrency purchase','USD','b2f2c2e5-41d3-5424-cfe5-326d8cf15826',NULL),
('d6cd3e7b-04e1-4a7a-9bcf-36c1f357bfe3',785.11,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-06-28 03:52:44','4b1e0ad3-0d8a-4f8a-b93e-cb2e2aee5b0b','TRANSFER','Hotel booking','USD','d4b4e407-63f5-7646-efe7-548f0ef37a48','b2f2c2e5-41d3-5424-cfe5-326d8cf15826'),
('632749d8-6e92-4601-8dda-5e0b512fd921',552.43,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2025-05-19 22:04:44','b6ef6b15-c2e3-4fd5-bb6a-35bbcbc13e51','EXPENSE','Overdraft fee','USD','e03e18d2-bdbf-4f96-9b5e-cc2c6d97c80c',NULL),
('e078f0c3-c765-4a9e-a8c5-dd0ac31b4da5',165.9,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2025-05-27 16:47:44','b6ef6b15-c2e3-4fd5-bb6a-35bbcbc13e51','TRANSFER','Charity donation','USD','c3a3d3f6-52e4-6535-dfe6-437e9df26937','a1e1b1d4-30c2-4313-bde4-215c7be04715'),
('b42ae190-bc4a-4da8-b8b7-b57156e42f1a',99.53,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-07-04 07:40:44','db73bb60-f9d8-40c3-84a5-4f1bdb62bc27','INCOME','Burger joint','USD',NULL,'b2f2c2e5-41d3-5424-cfe5-326d8cf15826'),
('dd2271e7-6085-431d-ae7f-3dbff6bcc071',157.25,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2025-04-12 10:58:44','a285d771-01d2-4055-9b42-cc54f9a4b2f1','EXPENSE','Market','USD','d4b4e407-63f5-7646-efe7-548f0ef37a48',NULL),
('b429d8b7-cbb0-4f8d-b74e-141d0c9ba8c2',858.7,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-12-03 08:28:44','a417a045-686e-4ab6-bbf1-1e5b33d2be93','INCOME','Clinic','USD',NULL,'e03e18d2-bdbf-4f96-9b5e-cc2c6d97c80c'),
('2ee40f9d-8611-47fa-a1ab-209f99387dec',480.24,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-06-21 05:55:44','4d414573-3b45-4b22-bb4c-01d348378b5d','EXPENSE','Summer dress','USD','a1e1b1d4-30c2-4313-bde4-215c7be04715',NULL),
('8164d88a-1032-4f5f-ac7f-af3ef7a6fa45',74.78,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-10-08 15:59:44','ac6a9441-2d67-4416-90aa-3b9ee6e5e144','TRANSFER','Electrician service','USD','a1e1b1d4-30c2-4313-bde4-215c7be04715','d4b4e407-63f5-7646-efe7-548f0ef37a48'),
('ff789dbe-a1ed-46ad-8c1a-b1dccd191f75',777.82,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2025-01-08 10:39:44','a285d771-01d2-4055-9b42-cc54f9a4b2f1','TRANSFER','Sports match','USD','c3a3d3f6-52e4-6535-dfe6-437e9df26937','b2f2c2e5-41d3-5424-cfe5-326d8cf15826'),
('5f347c0d-2cf0-43fa-9b5c-febdf8126ac9',726.4,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-12-29 14:19:44','c79a9c62-65c5-4c47-86f1-8a51b8eabdbb','INCOME','Tax refund','USD',NULL,'a1e1b1d4-30c2-4313-bde4-215c7be04715'),
('076f8ac3-9ee8-4ea2-b6c7-9c8cd4588510',224.24,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-07-04 16:42:44','f99e6af1-2b81-4915-a701-3fbc378e1e7b','INCOME','Water bill','USD',NULL,'c3a3d3f6-52e4-6535-dfe6-437e9df26937'),
('985a2f38-7590-40f9-a3e7-c5329da394b3',329.94,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-06-16 23:22:44','e39cf8e8-b99b-4650-928d-cf89f74e4203','TRANSFER','Ice cream','USD','c3a3d3f6-52e4-6535-dfe6-437e9df26937','a1e1b1d4-30c2-4313-bde4-215c7be04715'),
('7d356b44-1205-4cd1-9f90-a7e3cfd58c61',541.49,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2025-01-21 09:06:44','4d414573-3b45-4b22-bb4c-01d348378b5d','TRANSFER','Delivery','USD','a1e1b1d4-30c2-4313-bde4-215c7be04715','d4b4e407-63f5-7646-efe7-548f0ef37a48'),
('e2946ab1-bacb-46b0-a1bf-8129c2e558b2',922.42,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-09-07 12:12:44','f99e6af1-2b81-4915-a701-3fbc378e1e7b','TRANSFER','Wine','USD','b2f2c2e5-41d3-5424-cfe5-326d8cf15826','c3a3d3f6-52e4-6535-dfe6-437e9df26937'),
('d1ef30be-3d43-46eb-bf22-b8338c1977a3',986.41,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2025-05-08 20:21:44','b6f0134f-7497-4c5f-b4e8-5d0b2a8b7bc2','EXPENSE','Takeout','USD','b2f2c2e5-41d3-5424-cfe5-326d8cf15826',NULL),
('ff3f88f0-17cf-49a7-8dd4-6d6432df1c81',468.99,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2025-05-21 21:23:44','4a2a01db-ec30-4b34-97a1-8b2f0556a222','EXPENSE','Board games','USD','a1e1b1d4-30c2-4313-bde4-215c7be04715',NULL),
('f3951423-e62c-4153-9154-a3199670d75b',608.8,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-06-25 16:18:44','e39cf8e8-b99b-4650-928d-cf89f74e4203','EXPENSE','Taxi','USD','d4b4e407-63f5-7646-efe7-548f0ef37a48',NULL),
('925a6b2b-be5d-4fca-8d4a-5fbff4a5c614',283.06,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2025-03-29 18:47:44','f99e6af1-2b81-4915-a701-3fbc378e1e7b','EXPENSE','Theater ticket','USD','b2f2c2e5-41d3-5424-cfe5-326d8cf15826',NULL),
('c3fb9ed2-477f-45b0-9d42-2376c829a332',417.39,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2025-01-15 22:13:44','00a7e3e8-e042-40b9-9a25-3a6eb3fd3097','TRANSFER','Netflix payment','USD','e03e18d2-bdbf-4f96-9b5e-cc2c6d97c80c','a1e1b1d4-30c2-4313-bde4-215c7be04715'),
('e426f2c6-272a-4fe9-9789-d59524799f5e',921.23,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-09-20 07:21:44','4a2a01db-ec30-4b34-97a1-8b2f0556a222','TRANSFER','Tax refund','USD','b2f2c2e5-41d3-5424-cfe5-326d8cf15826','e03e18d2-bdbf-4f96-9b5e-cc2c6d97c80c'),
('3fcd0f0d-18c0-4ece-97e8-61dc8cfbe144',792.32,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-07-24 02:32:44','ac6a9441-2d67-4416-90aa-3b9ee6e5e144','EXPENSE','Lost wallet','USD','d4b4e407-63f5-7646-efe7-548f0ef37a48',NULL),
('205407cb-353d-442e-a904-3322d25b39c2',195.55,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2025-05-02 21:48:44','4b1e0ad3-0d8a-4f8a-b93e-cb2e2aee5b0b','INCOME','Yoga class','USD',NULL,'e03e18d2-bdbf-4f96-9b5e-cc2c6d97c80c'),
('90565ca6-470a-4114-a3cc-ed322d380256',67.97,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-10-01 03:02:44','b6f0134f-7497-4c5f-b4e8-5d0b2a8b7bc2','INCOME','Allowance','USD',NULL,'b2f2c2e5-41d3-5424-cfe5-326d8cf15826'),
('58c64e05-4896-4914-a7fe-2c0aa8a912ef',311.87,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2025-03-27 01:33:44','80f246c4-8be8-40ea-aad7-0f155c815a64','EXPENSE','Toys','USD','b2f2c2e5-41d3-5424-cfe5-326d8cf15826',NULL),
('e1172dd6-fa53-49c8-94b8-3459497fa0ef',456.93,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2025-01-13 19:05:44','00a7e3e8-e042-40b9-9a25-3a6eb3fd3097','TRANSFER','Car insurance','USD','a1e1b1d4-30c2-4313-bde4-215c7be04715','c3a3d3f6-52e4-6535-dfe6-437e9df26937'),
('60e64ae0-9b3b-49bd-ad73-97d2cac5116a',440.34,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-06-07 00:28:44','4d414573-3b45-4b22-bb4c-01d348378b5d','INCOME','Boat ride','USD',NULL,'e03e18d2-bdbf-4f96-9b5e-cc2c6d97c80c'),
('1c4ed8ee-562a-4991-9eb9-920639a49c14',726.21,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2025-01-21 09:26:44','db73bb60-f9d8-40c3-84a5-4f1bdb62bc27','INCOME','Life insurance','USD',NULL,'a1e1b1d4-30c2-4313-bde4-215c7be04715'),
('f0ead055-866b-402f-b2df-9732a4c38d4a',274.79,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-11-30 10:39:44','a417a045-686e-4ab6-bbf1-1e5b33d2be93','INCOME','Allowance','USD',NULL,'b2f2c2e5-41d3-5424-cfe5-326d8cf15826'),
('ac056bda-467e-4b68-9bee-d0e8208e74ae',407.49,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-09-03 02:30:44','e39cf8e8-b99b-4650-928d-cf89f74e4203','TRANSFER','Handbag','USD','e03e18d2-bdbf-4f96-9b5e-cc2c6d97c80c','d4b4e407-63f5-7646-efe7-548f0ef37a48'),
('683b38fe-809f-48d3-8d05-e1b164be6326',508.96,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2025-03-28 10:05:44','3d405321-87be-4e89-b57e-fbfa9579f15d','EXPENSE','Hospital bill','USD','a1e1b1d4-30c2-4313-bde4-215c7be04715',NULL),
('03745f8a-6e49-4ea1-bfbb-212d02bd0fbe',637.46,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-07-01 15:55:44','129e13f2-637e-4dc6-bcf7-6e7dbebc13b9','INCOME','Allowance','USD',NULL,'b2f2c2e5-41d3-5424-cfe5-326d8cf15826'),
('34df41fe-0109-40dd-92a0-e6fd79d9eadb',460.35,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2025-02-19 02:11:44','db73bb60-f9d8-40c3-84a5-4f1bdb62bc27','TRANSFER','Medicine','USD','e03e18d2-bdbf-4f96-9b5e-cc2c6d97c80c','c3a3d3f6-52e4-6535-dfe6-437e9df26937'),
('194df489-d8c8-448a-9833-6ffe09b00444',567.33,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2025-05-08 20:52:44','4a2a01db-ec30-4b34-97a1-8b2f0556a222','TRANSFER','Pet care','USD','c3a3d3f6-52e4-6535-dfe6-437e9df26937','b2f2c2e5-41d3-5424-cfe5-326d8cf15826'),
('d98414ed-28be-4d2b-9343-024f791649be',755.91,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-07-31 04:33:44','b6f0134f-7497-4c5f-b4e8-5d0b2a8b7bc2','INCOME','Reimbursement','USD',NULL,'e03e18d2-bdbf-4f96-9b5e-cc2c6d97c80c'),
('729b513b-f883-4a78-b4f1-0b02cddeec9e',615.33,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2025-05-10 03:17:44','ac6a9441-2d67-4416-90aa-3b9ee6e5e144','TRANSFER','Delivery','USD','e03e18d2-bdbf-4f96-9b5e-cc2c6d97c80c','b2f2c2e5-41d3-5424-cfe5-326d8cf15826'),
('2a25b3a0-2d4c-44aa-b36a-17975d3e12eb',689.03,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-10-16 07:08:44','3d405321-87be-4e89-b57e-fbfa9579f15d','EXPENSE','Freelance payment','USD','e03e18d2-bdbf-4f96-9b5e-cc2c6d97c80c',NULL),
('85444af9-5ee1-4430-adbf-de249cdb889f',898.95,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-12-04 08:59:44','4a2a01db-ec30-4b34-97a1-8b2f0556a222','TRANSFER','Fruit store','USD','c3a3d3f6-52e4-6535-dfe6-437e9df26937','e03e18d2-bdbf-4f96-9b5e-cc2c6d97c80c'),
('b0b22ae7-8963-4e95-876d-c7f922a12950',539.18,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2025-02-10 02:00:44','3d405321-87be-4e89-b57e-fbfa9579f15d','INCOME','Train ticket','USD',NULL,'e03e18d2-bdbf-4f96-9b5e-cc2c6d97c80c'),
('808df673-bac1-4c72-8133-b0aa464d17fb',987.6,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-11-09 14:33:44','b6f0134f-7497-4c5f-b4e8-5d0b2a8b7bc2','TRANSFER','Wine','USD','a1e1b1d4-30c2-4313-bde4-215c7be04715','d4b4e407-63f5-7646-efe7-548f0ef37a48'),
('0a48b967-27c3-479a-b254-82cb748a8511',175.47,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2025-03-28 23:45:44','4d414573-3b45-4b22-bb4c-01d348378b5d','EXPENSE','Wine','USD','a1e1b1d4-30c2-4313-bde4-215c7be04715',NULL),
('860f9a7d-faa7-482b-96d4-3783f484a32a',398.8,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-07-01 23:22:44','ac6a9441-2d67-4416-90aa-3b9ee6e5e144','TRANSFER','Lost wallet','USD','a1e1b1d4-30c2-4313-bde4-215c7be04715','e03e18d2-bdbf-4f96-9b5e-cc2c6d97c80c'),
('7fdd3dd7-73ab-4f67-896e-aa7c1ae1eed5',780.43,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-06-01 19:19:44','260b5fc8-00f4-46a6-9ecf-986e37c0d7e0','INCOME','Car rental','USD',NULL,'d4b4e407-63f5-7646-efe7-548f0ef37a48'),
('1af3a363-9a64-4071-bfef-fa11da7422f8',89.45,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2025-05-19 16:09:44','b6ef6b15-c2e3-4fd5-bb6a-35bbcbc13e51','EXPENSE','Cloud storage','USD','e03e18d2-bdbf-4f96-9b5e-cc2c6d97c80c',NULL),
('965eb4c3-ec3c-4d75-9540-d8109c607496',96.43,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-12-10 00:05:44','a285d771-01d2-4055-9b42-cc54f9a4b2f1','TRANSFER','Pet care','USD','d4b4e407-63f5-7646-efe7-548f0ef37a48','a1e1b1d4-30c2-4313-bde4-215c7be04715'),
('1cca41b8-d493-4890-80be-9968f710afe2',194.28,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-10-13 01:05:44','ff41c66e-5ff4-4c0d-b3a7-334b8c4271d5','TRANSFER','Rent payment','USD','c3a3d3f6-52e4-6535-dfe6-437e9df26937','d4b4e407-63f5-7646-efe7-548f0ef37a48'),
('f5453b15-9d8c-4840-84db-106048d62ab5',875.85,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-10-29 22:20:44','4b1e0ad3-0d8a-4f8a-b93e-cb2e2aee5b0b','INCOME','Furniture','USD',NULL,'d4b4e407-63f5-7646-efe7-548f0ef37a48'),
('c21cd15e-099f-4cc2-903d-9a7bbdda3aad',454.38,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2025-01-03 22:46:44','f99e6af1-2b81-4915-a701-3fbc378e1e7b','TRANSFER','Medicine','USD','c3a3d3f6-52e4-6535-dfe6-437e9df26937','a1e1b1d4-30c2-4313-bde4-215c7be04715'),
('bceb5909-df1e-49ea-a294-b2cd63f7b9a2',682.26,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-09-20 08:44:44','260b5fc8-00f4-46a6-9ecf-986e37c0d7e0','EXPENSE','Snacks','USD','b2f2c2e5-41d3-5424-cfe5-326d8cf15826',NULL),
('144bf0c8-bceb-497f-9971-30a20d1cfe86',488.13,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2025-02-20 10:45:44','b6ef6b15-c2e3-4fd5-bb6a-35bbcbc13e51','INCOME','Laundry','USD',NULL,'b2f2c2e5-41d3-5424-cfe5-326d8cf15826'),
('3ce69ceb-a2bd-4362-b3d1-3931508ed654',952.4,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-09-06 01:46:44','b6f0134f-7497-4c5f-b4e8-5d0b2a8b7bc2','EXPENSE','Bakery','USD','b2f2c2e5-41d3-5424-cfe5-326d8cf15826',NULL),
('4aec99bf-93d3-43b7-9127-0482e848e1c6',353.69,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-12-19 17:49:44','e39cf8e8-b99b-4650-928d-cf89f74e4203','TRANSFER','Vacuum cleaner','USD','d4b4e407-63f5-7646-efe7-548f0ef37a48','a1e1b1d4-30c2-4313-bde4-215c7be04715'),
('4673480a-f084-45a8-ab0c-b41ef012d746',415.09,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2025-01-11 22:22:44','a417a045-686e-4ab6-bbf1-1e5b33d2be93','TRANSFER','Gas station','USD','c3a3d3f6-52e4-6535-dfe6-437e9df26937','d4b4e407-63f5-7646-efe7-548f0ef37a48'),
('db081103-00aa-4c59-b732-9abb101ffc8a',810.84,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-07-18 14:49:44','ff41c66e-5ff4-4c0d-b3a7-334b8c4271d5','TRANSFER','Cash withdrawal','USD','d4b4e407-63f5-7646-efe7-548f0ef37a48','e03e18d2-bdbf-4f96-9b5e-cc2c6d97c80c'),
('2d3ac36c-d767-4876-881d-34ca9bed4081',491.91,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-11-19 18:27:44','db73bb60-f9d8-40c3-84a5-4f1bdb62bc27','EXPENSE','Tax refund','USD','d4b4e407-63f5-7646-efe7-548f0ef37a48',NULL),
('5b82f122-b406-4a0d-ab73-27fc0fed2a2f',390.68,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2025-01-06 04:44:44','9c0f6e10-3cd1-4940-b2ee-419f10f7fd9c','TRANSFER','Burger joint','USD','d4b4e407-63f5-7646-efe7-548f0ef37a48','b2f2c2e5-41d3-5424-cfe5-326d8cf15826'),
('791efdd5-e4ba-46cb-857d-37ce97fd529f',880.04,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-09-28 20:12:44','e39cf8e8-b99b-4650-928d-cf89f74e4203','INCOME','Kids clothing','USD',NULL,'c3a3d3f6-52e4-6535-dfe6-437e9df26937'),
('9e041d23-ad9e-47b3-a8fc-9c5294512343',76.49,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-12-14 17:19:44','c79a9c62-65c5-4c47-86f1-8a51b8eabdbb','EXPENSE','Electrician service','USD','b2f2c2e5-41d3-5424-cfe5-326d8cf15826',NULL),
('8bf05126-575f-4fba-acb7-6868166a8bd2',302.96,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2025-03-22 23:30:44','a417a045-686e-4ab6-bbf1-1e5b33d2be93','TRANSFER','Service fee','USD','c3a3d3f6-52e4-6535-dfe6-437e9df26937','b2f2c2e5-41d3-5424-cfe5-326d8cf15826'),
('e294ffee-4e65-4f27-9f5e-78e9e7b3d3f4',926.85,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2025-05-08 16:03:44','4b1e0ad3-0d8a-4f8a-b93e-cb2e2aee5b0b','EXPENSE','Chocolate','USD','c3a3d3f6-52e4-6535-dfe6-437e9df26937',NULL),
('01a2cba8-3fe7-4967-8686-226d25af35a2',99.37,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2025-02-22 13:32:44','4b1e0ad3-0d8a-4f8a-b93e-cb2e2aee5b0b','EXPENSE','Adoption fee','USD','d4b4e407-63f5-7646-efe7-548f0ef37a48',NULL),
('5c73c345-de52-4324-9e3d-e32b8a6d4db2',328.9,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-12-27 21:18:44','ac6a9441-2d67-4416-90aa-3b9ee6e5e144','INCOME','Pet care','USD',NULL,'b2f2c2e5-41d3-5424-cfe5-326d8cf15826'),
('afae78f5-b47d-419e-a1f4-23cb49ccb815',957.06,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-12-10 13:05:44','00a7e3e8-e042-40b9-9a25-3a6eb3fd3097','EXPENSE','Garden supplies','USD','e03e18d2-bdbf-4f96-9b5e-cc2c6d97c80c',NULL),
('5f456049-df11-48ea-8e2c-e7247094cd20',726.93,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-07-28 09:17:44','99a013c5-3346-44e7-9cb9-4b3e5e00271b','INCOME','Late payment fee','USD',NULL,'c3a3d3f6-52e4-6535-dfe6-437e9df26937'),
('16a52c03-af13-49b1-baa3-7d6097cf5339',378.2,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2025-04-22 00:45:44','129e13f2-637e-4dc6-bcf7-6e7dbebc13b9','TRANSFER','Tax refund','USD','b2f2c2e5-41d3-5424-cfe5-326d8cf15826','d4b4e407-63f5-7646-efe7-548f0ef37a48'),
('b420b8ab-9206-447a-933e-dbba1c0123f5',761.18,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-08-02 19:10:44','db73bb60-f9d8-40c3-84a5-4f1bdb62bc27','EXPENSE','Clothing','USD','e03e18d2-bdbf-4f96-9b5e-cc2c6d97c80c',NULL),
('d6f7b303-ed6b-41aa-94ef-f0f486925e33',958.58,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2025-05-24 01:24:44','99a013c5-3346-44e7-9cb9-4b3e5e00271b','EXPENSE','Scholarship','USD','b2f2c2e5-41d3-5424-cfe5-326d8cf15826',NULL),
('b5dec4fd-3c20-41fa-862c-91891cd5b69c',779.31,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2025-03-13 03:40:44','db73bb60-f9d8-40c3-84a5-4f1bdb62bc27','TRANSFER','Child allowance','USD','a1e1b1d4-30c2-4313-bde4-215c7be04715','b2f2c2e5-41d3-5424-cfe5-326d8cf15826'),
('6c7ff8d9-ff33-4068-865b-9f29d0bb1476',790.68,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-12-13 23:28:44','e39cf8e8-b99b-4650-928d-cf89f74e4203','INCOME','Penalty fee','USD',NULL,'c3a3d3f6-52e4-6535-dfe6-437e9df26937'),
('43032ef9-21d0-4fb0-ad97-264a9eecdac9',683.81,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-06-12 06:29:44','b6ef6b15-c2e3-4fd5-bb6a-35bbcbc13e51','INCOME','Bakery','USD',NULL,'e03e18d2-bdbf-4f96-9b5e-cc2c6d97c80c'),
('727d910a-f60b-494f-83ae-bf64d75b62cd',276.37,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-06-27 01:20:44','9c0f6e10-3cd1-4940-b2ee-419f10f7fd9c','INCOME','Farmers market','USD',NULL,'a1e1b1d4-30c2-4313-bde4-215c7be04715'),
('bd0b8d61-89ed-4cf9-bc12-8b9901d01906',790.87,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2025-05-23 10:53:44','99a013c5-3346-44e7-9cb9-4b3e5e00271b','TRANSFER','Electric bill','USD','c3a3d3f6-52e4-6535-dfe6-437e9df26937','e03e18d2-bdbf-4f96-9b5e-cc2c6d97c80c'),
('161b7345-a0b8-4bc1-9b49-71eb17b90ff6',609.51,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2025-01-17 14:06:44','b6f0134f-7497-4c5f-b4e8-5d0b2a8b7bc2','TRANSFER','Adoption fee','USD','a1e1b1d4-30c2-4313-bde4-215c7be04715','b2f2c2e5-41d3-5424-cfe5-326d8cf15826'),
('48e96dc6-b411-4e90-8eda-ca096f43b037',267.35,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2025-03-18 06:02:44','3d405321-87be-4e89-b57e-fbfa9579f15d','TRANSFER','Hospital bill','USD','c3a3d3f6-52e4-6535-dfe6-437e9df26937','b2f2c2e5-41d3-5424-cfe5-326d8cf15826'),
('e9eccb68-b5b4-49c4-a167-6588c85333eb',299.84,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2024-07-11 21:57:44','a285d771-01d2-4055-9b42-cc54f9a4b2f1','INCOME','Microwave','USD',NULL,'e03e18d2-bdbf-4f96-9b5e-cc2c6d97c80c'),
('7e35cc26-f069-4781-b914-a11e365f2b97',47.04,'c6adf7be-c5e3-49a7-9e9b-0e1f9d2ae786','2025-04-13 07:37:44','b6f0134f-7497-4c5f-b4e8-5d0b2a8b7bc2','INCOME','Theater ticket','USD',NULL,'d4b4e407-63f5-7646-efe7-548f0ef37a48');