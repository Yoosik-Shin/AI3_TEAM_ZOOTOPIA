-- 상품 데이터 삽입 (용품, 사료, 장난감, 산책, 기타 카테고리)

-- 사료 상품들
INSERT INTO products (name, category, description, price, image_url, status, reg_date, upd_date, views, likes, dislikes, stock) VALUES
('새 사료 - 사랑에 빠진 새', '사료', '새들이 좋아하는 영양가 높은 사료입니다. 건강한 털과 활발한 활동을 도와줍니다.', 25000, '/assets/dist/img/products/foodbirdfallinlove.png', '판매중', NOW(), NOW(), 50, 5, 0, 15),
('새 사료 - 부엉이가 본', '사료', '다양한 곡물과 씨앗이 포함된 프리미엄 새 사료입니다.', 28000, '/assets/dist/img/products/foodbirdowlsee.png', '판매중', NOW(), NOW(), 35, 3, 0, 12),
('새 사료 - 스크림', '사료', '새들의 건강을 위한 특별한 배합의 사료입니다.', 22000, '/assets/dist/img/products/foodbirdscream.png', '판매중', NOW(), NOW(), 40, 4, 0, 18),
('고양이 사료 - 피프티', '사료', '고양이의 영양 균형을 고려한 프리미엄 사료입니다.', 35000, '/assets/dist/img/products/foodcatfifty.png', '판매중', NOW(), NOW(), 75, 8, 1, 20),
('고양이 사료 - 생선 맛', '사료', '신선한 생선을 주원료로 한 고양이 사료입니다.', 32000, '/assets/dist/img/products/foodcatfishtaste.png', '판매중', NOW(), NOW(), 60, 6, 0, 25),
('고양이 사료 - 고뜨', '사료', '고양이가 좋아하는 맛과 영양을 동시에 충족하는 사료입니다.', 38000, '/assets/dist/img/products/foodcatgoddu.png', '판매중', NOW(), NOW(), 55, 7, 0, 14),
('개 사료 - 아빠가 좋아해', '사료', '개들이 좋아하는 맛있는 사료입니다. 영양가가 풍부합니다.', 42000, '/assets/dist/img/products/foodddogaddylovesit.png', '판매중', NOW(), NOW(), 90, 10, 1, 22),
('개&고양이 건조 사료', '사료', '개와 고양이 모두 먹을 수 있는 건조 사료입니다.', 45000, '/assets/dist/img/products/fooddogandcatdried.png', '판매중', NOW(), NOW(), 80, 9, 0, 18),
('개&고양이 습식 사료', '사료', '수분이 풍부한 습식 사료로 기호성이 뛰어납니다.', 48000, '/assets/dist/img/products/fooddogcatmoistured.png', '판매중', NOW(), NOW(), 65, 8, 1, 16),
('개 사료 - 껌1', '사료', '개의 치아 건강을 위한 껌 형태의 사료입니다.', 18000, '/assets/dist/img/products/fooddoggum1.png', '판매중', NOW(), NOW(), 45, 5, 0, 30),
('개 사료 - 하트빔', '사료', '하트 모양의 귀여운 개 사료입니다.', 25000, '/assets/dist/img/products/fooddogheartbeam.png', '판매중', NOW(), NOW(), 55, 6, 0, 20),
('개 사료 - 고기', '사료', '신선한 고기를 주원료로 한 프리미엄 개 사료입니다.', 55000, '/assets/dist/img/products/fooddogmeat.png', '판매중', NOW(), NOW(), 85, 12, 2, 10);

-- 용품 상품들
INSERT INTO products (name, category, description, price, image_url, status, reg_date, upd_date, views, likes, dislikes, stock) VALUES
('고양이 목걸이', '용품', '고양이용 방울이 달린 예쁜 목걸이입니다.', 15000, '/assets/dist/img/products/productcatbellnecklace.png', '판매중', NOW(), NOW(), 45, 7, 0, 25),
('고양이 식기', '용품', '고양이 전용 식기입니다. 먹기 편한 높이와 각도로 설계되었습니다.', 18000, '/assets/dist/img/products/productcatbowl.png', '판매중', NOW(), NOW(), 60, 8, 0, 20),
('고양이 위생패드', '용품', '고양이가 편안하게 쉴 수 있는 위생패드입니다.', 25000, '/assets/dist/img/products/productcathygienepad.png', '판매중', NOW(), NOW(), 40, 5, 0, 15),
('고양이 물그릇', '용품', '고양이 전용 물그릇입니다. 물이 흘러넘치지 않도록 설계되었습니다.', 12000, '/assets/dist/img/products/productcatwaterbowl.png', '판매중', NOW(), NOW(), 35, 4, 0, 30),
('개 식기', '용품', '개 전용 식기입니다. 크기별로 다양하게 준비되어 있습니다.', 20000, '/assets/dist/img/products/productdogbowl.png', '판매중', NOW(), NOW(), 55, 6, 0, 18),
('개 위생패드', '용품', '개가 편안하게 쉴 수 있는 위생패드입니다.', 28000, '/assets/dist/img/products/productdoghygienepad.png', '판매중', NOW(), NOW(), 50, 7, 1, 22),
('개 물그릇', '용품', '개 전용 물그릇입니다. 넘어지지 않도록 바닥에 미끄럼 방지 처리가 되어 있습니다.', 15000, '/assets/dist/img/products/productdogwaterbowl.png', '판매중', NOW(), NOW(), 38, 5, 0, 28),
('위생 화장실', '용품', '반려동물 전용 화장실입니다. 냄새 차단과 청소가 쉬운 디자인으로 제작되었습니다.', 45000, '/assets/dist/img/products/producthygienetoilet.png', '판매중', NOW(), NOW(), 70, 9, 1, 12),
('펫 침대', '용품', '반려동물이 편안하게 잠들 수 있는 침대입니다.', 65000, '/assets/dist/img/products/productpetbed.png', '판매중', NOW(), NOW(), 85, 11, 0, 8),
('펫 케이지', '용품', '반려동물용 케이지입니다. 안전하고 통풍이 잘 됩니다.', 120000, '/assets/dist/img/products/productpetcage.png', '판매중', NOW(), NOW(), 95, 14, 2, 5),
('펫 빗', '용품', '반려동물의 털을 정리하는 빗입니다.', 22000, '/assets/dist/img/products/productpetcomb.png', '판매중', NOW(), NOW(), 42, 6, 0, 24),
('펫 쿠션', '용품', '반려동물이 편안하게 쉴 수 있는 쿠션입니다.', 35000, '/assets/dist/img/products/productpetcousion.png', '판매중', NOW(), NOW(), 48, 7, 0, 16),
('펫 발톱깎이', '용품', '반려동물의 발톱을 안전하게 깎을 수 있는 도구입니다.', 15000, '/assets/dist/img/products/productpetcutter.png', '판매중', NOW(), NOW(), 35, 4, 0, 32),
('펫 귀 청소용품', '용품', '반려동물의 귀를 깨끗하게 청소하는 용품입니다.', 18000, '/assets/dist/img/products/productpetearcleaner.png', '판매중', NOW(), NOW(), 30, 3, 0, 25),
('펫 하우스', '용품', '반려동물을 위한 집입니다. 실내외 모두 사용 가능합니다.', 150000, '/assets/dist/img/products/productpethouse.png', '판매중', NOW(), NOW(), 120, 18, 3, 3),
('펫 목걸이', '용품', '반려동물용 목걸이입니다. 이름표를 달 수 있습니다.', 25000, '/assets/dist/img/products/productpetnecklace.png', '판매중', NOW(), NOW(), 55, 8, 0, 20),
('펫 샴푸', '용품', '반려동물 전용 샴푸입니다. 피부에 자극이 적습니다.', 28000, '/assets/dist/img/products/productpetshampoo.png', '판매중', NOW(), NOW(), 65, 9, 1, 18);

-- 장난감 상품들
INSERT INTO products (name, category, description, price, image_url, status, reg_date, upd_date, views, likes, dislikes, stock) VALUES
('강아지 공', '장난감', '강아지가 좋아하는 탱탱볼입니다. 씹어도 안전합니다.', 12000, '/assets/dist/img/products/toydogball.png', '판매중', NOW(), NOW(), 60, 8, 0, 35),
('고양이 낚시대', '장난감', '고양이와 함께 놀 수 있는 낚시대 장난감입니다.', 15000, '/assets/dist/img/products/toycatfishingrod.png', '판매중', NOW(), NOW(), 75, 10, 1, 28),
('펫 로프', '장난감', '반려동물과 줄다리기를 할 수 있는 로프입니다.', 8000, '/assets/dist/img/products/toypetrope.png', '판매중', NOW(), NOW(), 45, 6, 0, 40),
('스마트 레이저 포인터', '장난감', '고양이가 좋아하는 레이저 포인터입니다.', 18000, '/assets/dist/img/products/toylaserpointer.png', '판매중', NOW(), NOW(), 55, 7, 0, 22),
('츄잉 본', '장난감', '개가 씹어도 안전한 뼈 모양 장난감입니다.', 20000, '/assets/dist/img/products/toychewingbone.png', '판매중', NOW(), NOW(), 50, 6, 0, 25);

-- 산책 상품들
INSERT INTO products (name, category, description, price, image_url, status, reg_date, upd_date, views, likes, dislikes, stock) VALUES
('개 하네스', '산책', '산책 시 사용하는 개 하네스입니다. 편안하고 안전합니다.', 35000, '/assets/dist/img/products/productdogharness.png', '판매중', NOW(), NOW(), 70, 9, 1, 15),
('강아지 목줄', '산책', '산책 시 사용하는 강아지 목줄입니다.', 22000, '/assets/dist/img/products/walkdogleash.png', '판매중', NOW(), NOW(), 65, 8, 0, 20),
('LED 목걸이', '산책', '야간 산책 시 안전을 위한 LED 목걸이입니다.', 25000, '/assets/dist/img/products/walklednecklace.png', '판매중', NOW(), NOW(), 45, 5, 0, 18),
('펫 운동화', '산책', '반려동물 발가락 보호를 위한 운동화입니다.', 35000, '/assets/dist/img/products/walkpetshoes.png', '판매중', NOW(), NOW(), 40, 4, 1, 12),
('휴대용 물병', '산책', '산책 시 사용하는 반려동물 전용 물병입니다.', 18000, '/assets/dist/img/products/walkwaterbottle.png', '판매중', NOW(), NOW(), 55, 7, 0, 25),
('위생 플라스틱 봉투', '산책', '산책 시 사용하는 배변봉투입니다. 친환경 소재로 만들어졌습니다.', 8000, '/assets/dist/img/products/producthygieneplasticbag.png', '판매중', NOW(), NOW(), 30, 3, 0, 50),
('펫 캐리어', '산책', '반려동물과 함께 외출할 때 사용하는 캐리어입니다.', 85000, '/assets/dist/img/products/productpetcarriage.png', '판매중', NOW(), NOW(), 90, 12, 2, 8),
('산책 가방', '산책', '산책 시 필요한 용품을 담을 수 있는 가방입니다.', 32000, '/assets/dist/img/products/walkpetbag.png', '판매중', NOW(), NOW(), 42, 5, 0, 16);

-- 기타 상품들
INSERT INTO products (name, category, description, price, image_url, status, reg_date, upd_date, views, likes, dislikes, stock) VALUES
('반려동물 응급처치키트', '기타', '응급상황에 대비한 반려동물 전용 응급처치 키트입니다.', 45000, '/img/default-thumbnail.png', '판매중', NOW(), NOW(), 35, 4, 0, 10),
('펫 체온계', '기타', '반려동물의 체온을 측정할 수 있는 디지털 체온계입니다.', 28000, '/img/default-thumbnail.png', '판매중', NOW(), NOW(), 25, 2, 0, 15),
('반려동물 영양제', '기타', '반려동물의 건강을 위한 종합 영양제입니다.', 35000, '/img/default-thumbnail.png', '판매중', NOW(), NOW(), 40, 5, 1, 20),
('펫 칫솔 세트', '기타', '반려동물 치아 건강을 위한 칫솔 세트입니다.', 15000, '/img/default-thumbnail.png', '판매중', NOW(), NOW(), 30, 3, 0, 25),
('반려동물 헤어클리퍼', '기타', '집에서 간편하게 사용할 수 있는 반려동물 전용 헤어클리퍼입니다.', 65000, '/img/default-thumbnail.png', '판매중', NOW(), NOW(), 50, 6, 1, 8);
