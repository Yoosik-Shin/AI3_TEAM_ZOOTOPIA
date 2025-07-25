-- ZOOTOPIA 더미 상품 데이터 삽입 스크립트

-- 기존 데이터 정리 (필요시)
-- DELETE FROM products;

-- 더미 상품 데이터 삽입
INSERT INTO products (no, name, category, description, price, image_url, status, stock, views, likes, reg_date, upd_date) VALUES
-- 사료 상품들
(1, '새 사료 - 사랑에 빠진 새', '사료', '새들이 좋아하는 영양가 높은 사료입니다. 건강한 털과 활발한 활동을 도와줍니다.', 25000, '/assets/dist/img/products/foodbirdfallinlove.png', '판매중', 10, 50, 5, NOW(), NOW()),
(2, '새 사료 - 부엉이가 본', '사료', '다양한 곡물과 씨앗이 포함된 프리미엄 새 사료입니다.', 28000, '/assets/dist/img/products/foodbirdowlsee.png', '판매중', 15, 30, 3, NOW(), NOW()),
(3, '새 사료 - 스크림', '사료', '새들의 건강을 위한 특별한 배합의 사료입니다.', 22000, '/assets/dist/img/products/foodbirdscream.png', '판매중', 8, 40, 2, NOW(), NOW()),
(4, '고양이 사료 - 피프티', '사료', '고양이의 영양 균형을 고려한 프리미엄 사료입니다.', 35000, '/assets/dist/img/products/foodcatfifty.png', '판매중', 12, 60, 7, NOW(), NOW()),
(5, '고양이 사료 - 생선 맛', '사료', '신선한 생선을 주원료로 한 고양이 사료입니다.', 32000, '/assets/dist/img/products/foodcatfishtaste.png', '판매중', 20, 80, 9, NOW(), NOW()),
(6, '고양이 사료 - 고뜨', '사료', '고양이가 좋아하는 맛과 영양을 동시에 충족하는 사료입니다.', 38000, '/assets/dist/img/products/foodcatgoddu.png', '판매중', 5, 25, 1, NOW(), NOW()),
(7, '개 사료 - 아빠가 좋아해', '사료', '개들이 좋아하는 맛있는 사료입니다. 영양가가 풍부합니다.', 42000, '/assets/dist/img/products/foodddogaddylovesit.png', '판매중', 18, 90, 12, NOW(), NOW()),
(8, '개&고양이 건조 사료', '사료', '개와 고양이 모두 먹을 수 있는 건조 사료입니다.', 45000, '/assets/dist/img/products/fooddogandcatdried.png', '판매중', 25, 70, 8, NOW(), NOW()),
(9, '개&고양이 습식 사료', '사료', '수분이 풍부한 습식 사료로 기호성이 뛰어납니다.', 48000, '/assets/dist/img/products/fooddogcatmoistured.png', '판매중', 14, 55, 6, NOW(), NOW()),
(10, '개 사료 - 껌1', '사료', '개의 치아 건강을 위한 껌 형태의 사료입니다.', 18000, '/assets/dist/img/products/fooddoggum1.png', '판매중', 30, 35, 4, NOW(), NOW()),
(11, '개 사료 - 하트빔', '사료', '하트 모양의 귀여운 개 사료입니다.', 25000, '/assets/dist/img/products/fooddogheartbeam.png', '판매중', 22, 45, 5, NOW(), NOW()),
(12, '개 사료 - 고기', '사료', '신선한 고기를 주원료로 한 프리미엄 개 사료입니다.', 55000, '/assets/dist/img/products/fooddogmeat.png', '판매중', 8, 65, 7, NOW(), NOW()),

-- 용품 상품들
(13, '고양이 목걸이', '용품', '고양이용 방울이 달린 예쁜 목걸이입니다.', 15000, '/assets/dist/img/products/productcatbellnecklace.png', '판매중', 25, 40, 3, NOW(), NOW()),
(14, '고양이 식기', '용품', '고양이 전용 식기입니다. 먹기 편한 높이와 각도로 설계되었습니다.', 18000, '/assets/dist/img/products/productcatbowl.png', '판매중', 15, 50, 4, NOW(), NOW()),
(15, '고양이 위생패드', '용품', '고양이가 편안하게 쉴 수 있는 위생패드입니다.', 25000, '/assets/dist/img/products/productcathygienepad.png', '판매중', 18, 30, 2, NOW(), NOW()),
(16, '고양이 물그릇', '용품', '고양이 전용 물그릇입니다. 물이 흘러넘치지 않도록 설계되었습니다.', 12000, '/assets/dist/img/products/productcatwaterbowl.png', '판매중', 20, 35, 3, NOW(), NOW()),
(17, '개 식기', '용품', '개 전용 식기입니다. 크기별로 다양하게 준비되어 있습니다.', 20000, '/assets/dist/img/products/productdogbowl.png', '판매중', 12, 45, 5, NOW(), NOW()),
(18, '개 하네스', '산책', '산책 시 사용하는 개 하네스입니다. 편안하고 안전합니다.', 35000, '/assets/dist/img/products/productdogharness.png', '판매중', 8, 60, 6, NOW(), NOW()),
(19, '개 위생패드', '용품', '개가 편안하게 쉴 수 있는 위생패드입니다.', 28000, '/assets/dist/img/products/productdoghygienepad.png', '판매중', 16, 25, 1, NOW(), NOW()),
(20, '개 물그릇', '용품', '개 전용 물그릇입니다. 넘어지지 않도록 바닥에 미끄럼 방지 처리가 되어 있습니다.', 15000, '/assets/dist/img/products/productdogwaterbowl.png', '판매중', 22, 40, 4, NOW(), NOW()),
(21, '위생 플라스틱 봉투', '산책', '산책 시 사용하는 배변봉투입니다. 친환경 소재로 만들어졌습니다.', 8000, '/assets/dist/img/products/producthygieneplasticbag.png', '판매중', 50, 20, 2, NOW(), NOW()),
(22, '위생 화장실', '용품', '반려동물 전용 화장실입니다. 냄새 차단과 청소가 쉬운 디자인으로 제작되었습니다.', 45000, '/assets/dist/img/products/producthygienetoilet.png', '판매중', 6, 70, 8, NOW(), NOW()),
(23, '펫 침대', '용품', '반려동물이 편안하게 잠들 수 있는 침대입니다.', 65000, '/assets/dist/img/products/productpetbed.png', '판매중', 10, 85, 10, NOW(), NOW()),
(24, '펫 케이지', '용품', '반려동물용 케이지입니다. 안전하고 통풍이 잘 됩니다.', 120000, '/assets/dist/img/products/productpetcage.png', '판매중', 4, 55, 5, NOW(), NOW()),
(25, '펫 캐리어', '산책', '반려동물과 함께 외출할 때 사용하는 캐리어입니다.', 85000, '/assets/dist/img/products/productpetcarriage.png', '판매중', 7, 65, 7, NOW(), NOW()),
(26, '펫 빗', '용품', '반려동물의 털을 정리하는 빗입니다.', 22000, '/assets/dist/img/products/productpetcomb.png', '판매중', 15, 30, 2, NOW(), NOW()),
(27, '펫 쿠션', '용품', '반려동물이 편안하게 쉴 수 있는 쿠션입니다.', 35000, '/assets/dist/img/products/productpetcousion.png', '판매중', 12, 40, 4, NOW(), NOW()),
(28, '펫 발톱깎이', '용품', '반려동물의 발톱을 안전하게 깎을 수 있는 도구입니다.', 15000, '/assets/dist/img/products/productpetcutter.png', '판매중', 20, 25, 1, NOW(), NOW()),
(29, '펫 귀 청소용품', '용품', '반려동물의 귀를 깨끗하게 청소하는 용품입니다.', 18000, '/assets/dist/img/products/productpetearcleaner.png', '판매중', 18, 35, 3, NOW(), NOW()),
(30, '펫 하우스', '용품', '반려동물을 위한 집입니다. 실내외 모두 사용 가능합니다.', 150000, '/assets/dist/img/products/productpethouse.png', '판매중', 3, 90, 12, NOW(), NOW()),
(31, '펫 목걸이', '용품', '반려동물용 목걸이입니다. 이름표를 달 수 있습니다.', 25000, '/assets/dist/img/products/productpetnecklace.png', '판매중', 14, 50, 5, NOW(), NOW()),
(32, '펫 샴푸', '용품', '반려동물 전용 샴푸입니다. 피부에 자극이 적습니다.', 28000, '/assets/dist/img/products/productpetshampoo.png', '판매중', 16, 45, 4, NOW(), NOW()),

-- 장난감 상품들
(33, '강아지 공', '장난감', '강아지가 좋아하는 탱탱볼입니다. 씹어도 안전합니다.', 12000, '/assets/dist/img/products/toydogball.png', '판매중', 30, 40, 3, NOW(), NOW()),
(34, '고양이 낚시대', '장난감', '고양이와 함께 놀 수 있는 낚시대 장난감입니다.', 15000, '/assets/dist/img/products/toycatfishingrod.png', '판매중', 25, 55, 6, NOW(), NOW()),
(35, '펫 로프', '장난감', '반려동물과 줄다리기를 할 수 있는 로프입니다.', 8000, '/assets/dist/img/products/toypetrope.png', '판매중', 40, 30, 2, NOW(), NOW()),
(36, '스마트 레이저 포인터', '장난감', '고양이가 좋아하는 레이저 포인터입니다.', 18000, '/assets/dist/img/products/toylaserpointer.png', '판매중', 20, 60, 7, NOW(), NOW()),
(37, '츄잉 본', '장난감', '개가 씹어도 안전한 뼈 모양 장난감입니다.', 20000, '/assets/dist/img/products/toychewingbone.png', '판매중', 18, 35, 3, NOW(), NOW()),

-- 산책 상품들
(38, '강아지 목줄', '산책', '산책 시 사용하는 강아지 목줄입니다.', 22000, '/assets/dist/img/products/walkdogleash.png', '판매중', 15, 50, 5, NOW(), NOW()),
(39, 'LED 목걸이', '산책', '야간 산책 시 안전을 위한 LED 목걸이입니다.', 25000, '/assets/dist/img/products/walklednecklace.png', '판매중', 12, 45, 4, NOW(), NOW()),
(40, '펫 운동화', '산책', '반려동물 발가락 보호를 위한 운동화입니다.', 35000, '/assets/dist/img/products/walkpetshoes.png', '판매중', 8, 65, 6, NOW(), NOW()),
(41, '휴대용 물병', '산책', '산책 시 사용하는 반려동물 전용 물병입니다.', 18000, '/assets/dist/img/products/walkwaterbottle.png', '판매중', 22, 40, 3, NOW(), NOW()),
(42, '산책 가방', '산책', '산책 시 필요한 용품을 담을 수 있는 가방입니다.', 32000, '/assets/dist/img/products/walkpetbag.png', '판매중', 10, 55, 5, NOW(), NOW());

-- AUTO_INCREMENT 값 재설정
ALTER TABLE products AUTO_INCREMENT = 43;

-- 데이터 확인
SELECT COUNT(*) as '총 상품 수' FROM products;
SELECT category, COUNT(*) as '카테고리별 상품 수' FROM products GROUP BY category;
