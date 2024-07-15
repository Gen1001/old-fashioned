-- rolesテーブル --
INSERT IGNORE INTO roles (id, name) VALUES (1, 'ROLE_GENERAL');
INSERT IGNORE INTO roles (id, name) VALUES (2, 'ROLE_ADMIN');

-- usersテーブル --
INSERT IGNORE INTO users (id, role_id, name, email, postal_code, address, nickname, phone_number, introduction, password, enabled, user_photo) VALUES (1, 1, '山田太郎', 'taro.yamada@example.com', '1111111', '東京都', 'タロウ', '11111111111', 'インポート好き', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', true, 'profile01.png');

-- categoriesテーブル --
INSERT IGNORE INTO categories (id, name) VALUES (1, 'ユーズド');
INSERT IGNORE INTO categories (id, name) VALUES (2, 'インポート');
INSERT IGNORE INTO categories (id, name) VALUES (3, 'ヴィンテージ');

-- storesテーブル --
INSERT IGNORE INTO stores (id, name, address, latitude, longitude) VALUES (1, 'JAM 広島店', '広島県広島市中区三河町3-16', 34.39109708327413, 132.4614975408896);
INSERT IGNORE INTO stores (id, name, address, latitude, longitude) VALUES (2, 'UNION RAG 広島店', '広島県広島市中区紙屋町2丁目2-18', 34.39467782353733, 132.45637055221016);
INSERT IGNORE INTO stores (id, name, address, latitude, longitude) VALUES (3, 'Vintage Tokyo', '東京都渋谷区神南1-12-16', 35.6619912, 139.7041046);
INSERT IGNORE INTO stores (id, name, address, latitude, longitude) VALUES (4, 'Retro Osaka', '大阪府大阪市中央区難波5-1-60', 34.6655972, 135.5014697);
INSERT IGNORE INTO stores (id, name, address, latitude, longitude) VALUES (5, 'Oldies Nagoya', '愛知県名古屋市中区栄3-27-18', 35.1682578, 136.9054567);
INSERT IGNORE INTO stores (id, name, address, latitude, longitude) VALUES (6, 'Classic Kyoto', '京都府京都市中京区河原町通四条上ル米屋町380', 35.0092623, 135.7672048);
INSERT IGNORE INTO stores (id, name, address, latitude, longitude) VALUES (7, 'Timeless Fukuoka', '福岡県福岡市中央区天神2-11-3', 33.5900414, 130.4006865);
INSERT IGNORE INTO stores (id, name, address, latitude, longitude) VALUES (8, 'Retro Sendai', '宮城県仙台市青葉区一番町3-2-2', 38.2591692, 140.8738033);
INSERT IGNORE INTO stores (id, name, address, latitude, longitude) VALUES (9, 'Vintage Sapporo', '北海道札幌市中央区南1条西4-16-1', 43.0566965, 141.3507808);
INSERT IGNORE INTO stores (id, name, address, latitude, longitude) VALUES (10, 'Antique Kobe', '兵庫県神戸市中央区三宮町2-10-7', 34.6937241, 135.1945531);
INSERT IGNORE INTO stores (id, name, address, latitude, longitude) VALUES (11, 'Retro Hiroshima', '広島県広島市中区紙屋町1-4-3', 34.3915513, 132.4535292);
INSERT IGNORE INTO stores (id, name, address, latitude, longitude) VALUES (12, 'Vintage Okinawa', '沖縄県那覇市久茂地3-5-12', 26.2139935, 127.6792017);
INSERT IGNORE INTO stores (id, name, address, latitude, longitude) VALUES (13, 'Retro Yokohama', '神奈川県横浜市中区山下町123', 35.4439037, 139.6499256);
INSERT IGNORE INTO stores (id, name, address, latitude, longitude) VALUES (14, 'Antique Nagasaki', '長崎県長崎市浜町10-13', 32.7497003, 129.878152);
INSERT IGNORE INTO stores (id, name, address, latitude, longitude) VALUES (15, 'Retro Kagoshima', '鹿児島県鹿児島市東千石町14-10', 31.5928745, 130.5524195);
INSERT IGNORE INTO stores (id, name, address, latitude, longitude) VALUES (16, 'Vintage Niigata', '新潟県新潟市中央区万代1-4-8', 37.9171212, 139.0614891);
