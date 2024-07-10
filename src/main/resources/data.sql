-- rolesテーブル --
INSERT IGNORE INTO roles (id, name) VALUES (1, 'ROLE_GENERAL');
INSERT IGNORE INTO roles (id, name) VALUES (2, 'ROLE_ADMIN');

-- usersテーブル --
INSERT IGNORE INTO users (id, role_id, name, email, postal_code, address, nickname, phone_number, introduction, password, enabled, user_photo) VALUES (1, 1, '山田太郎', 'taro.yamada@example.com', 1111111, '東京都', 'タロウ', 11111111111, 'インポート好き', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', true, 'image01');
INSERT IGNORE INTO users (id, role_id, name, email, postal_code, address, nickname, phone_number, introduction, password, enabled, user_photo) VALUES (2, 2, '山田花子', 'hanako.yamada@example.com', 2222222, '東京都', 'ハナコ', 22222222222, 'ヴィンテージ専', '$2a$10$2JNjTwZBwo7fprL2X4sv.OEKqxnVtsVQvuXDkI8xVGix.U3W5B7CO', true, 'image02');

-- categoriesテーブル --
INSERT IGNORE INTO categories (id, name) VALUES (1, 'ユーズド');
INSERT IGNORE INTO categories (id, name) VALUES (2, 'インポート');
INSERT IGNORE INTO categories (id, name) VALUES (3, 'ヴィンテージ');

-- storesテーブル --
INSERT IGNORE INTO stores (id, name, address, latitude, longitude) VALUES (1, 'CUE', '東京都', 1, 1);

-- postsテーブル --
INSERT IGNORE INTO posts (id, category_id, store_id, user_id, post_photo, name, content, latitude, longitude, store_name) VALUES
(1, 1, 1, 1, 'cloth01.jpg', '青デニム', '青デニム', 1.234567, 12.345678, '代官'),
(2, 3, 1, 1, 'cloth03.jpg', 'ジャケット', 'ジャケット', -23.456789, 45.678901, ''),
(3, 2, 1, 1, 'cloth02.jpg', 'スカート', 'スカート', 0.987654, -98.765432, ''),
(4, 2, 1, 1, 'cloth02.jpg', 'シャツ', 'シャツ', 34.567890, 67.890123, ''),
(5, 1, 1, 1, 'cloth01.jpg', 'デニムパンツ', 'デニムパンツ', -12.345678, -23.456789, ''),
(6, 3, 1, 1, 'cloth03.jpg', 'ドレス', 'ドレス', 56.789012, -34.567890, ''),
(7, 1, 1, 1, 'cloth01.jpg', 'Tシャツ', 'Tシャツ', 89.012345, 123.456789, ''),
(8, 2, 1, 1, 'cloth02.jpg', 'パンツ', 'パンツ', -45.678901, 34.567890, ''),
(9, 3, 1, 1, 'cloth03.jpg', '帽子', '帽子', -67.890123, 0.123456, ''),
(10, 1, 1, 1, 'cloth01.jpg', 'ジーンズ', 'ジーンズ', 12.345678, -45.678901, ''),
(11, 3, 1, 1, 'cloth03.jpg', 'セーター', 'セーター', 78.901234, -89.012345, ''),
(12, 2, 1, 1, 'cloth02.jpg', 'ベスト', 'ベスト', -34.567890, 56.789012, ''),
(13, 1, 1, 1, 'cloth01.jpg', 'ブラウス', 'ブラウス', 0.123456, 78.901234, ''),
(14, 2, 1, 1, 'cloth02.jpg', 'ジャージ', 'ジャージ', -89.012345, -12.345678, ''),
(15, 1, 1, 1, 'cloth01.jpg', 'コート', 'コート', 45.678901, -0.987654, ''),
(16, 3, 1, 1, 'cloth03.jpg', 'パーカー', 'パーカー', -1.234567, 89.012345, ''),
(17, 1, 1, 1, 'cloth01.jpg', 'ワンピース', 'ワンピース', 34.567890, -56.789012, ''),
(18, 2, 1, 1, 'cloth02.jpg', 'ジャンパー', 'ジャンパー', 67.890123, 1.234567, ''),
(19, 3, 1, 1, 'cloth03.jpg', 'ベルト', 'ベルト', -123.456789, -34.567890, ''),
(20, 1, 1, 1, 'cloth01.jpg', 'タンクトップ', 'タンクトップ', -78.901234, 12.345678, '');
