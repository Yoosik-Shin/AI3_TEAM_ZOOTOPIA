# ZOOTOPIA 프로젝트 DB 연동 및 더미 데이터 설정 가이드

## 1. 데이터베이스 설정

### 1.1 MySQL 접속
```bash
mysql -u aloha -p
# 비밀번호: 123456 입력
```

### 1.2 데이터베이스 선택
```sql
USE aloha;
```

### 1.3 products 테이블 존재 확인
```sql
SHOW TABLES LIKE 'products';
DESC products;
```

## 2. 더미 데이터 삽입

### 2.1 더미 데이터 삽입 스크립트 실행
```sql
source C:/hong260701/AI3_TEAM_ZOOTOPIA/ZOOTOPIA/SQL/zootopia/insert_dummy_products.sql;
```

또는 직접 MySQL Workbench나 phpMyAdmin에서 `insert_dummy_products.sql` 파일을 실행하세요.

### 2.2 데이터 삽입 확인
```sql
-- 전체 상품 수 확인
SELECT COUNT(*) as '총 상품 수' FROM products;

-- 카테고리별 상품 수 확인
SELECT category, COUNT(*) as '상품 수' FROM products GROUP BY category;

-- 첫 5개 상품 확인
SELECT no, name, category, price, stock FROM products LIMIT 5;
```

## 3. 애플리케이션 실행 및 테스트

### 3.1 Spring Boot 애플리케이션 실행
```bash
./gradlew bootRun
```

또는 Spring Boot Dashboard에서 실행

### 3.2 테스트 URL들

#### 상품 목록 페이지 (DB 연동)
- http://localhost:8080/products/listp
- 카테고리 필터: http://localhost:8080/products/listp?category=사료
- 검색: http://localhost:8080/products/listp?search=고양이
- 페이지네이션: http://localhost:8080/products/listp?page=2

#### 상품 상세 페이지 (DB 연동)
- http://localhost:8080/products/detail/1
- http://localhost:8080/products/detail/7
- http://localhost:8080/products/detail/13

#### 상품 등록 페이지 (관리자 권한 필요)
- http://localhost:8080/products/create

## 4. 기능 테스트 체크리스트

### 4.1 상품 목록 페이지 (`/products/listp`)
- [ ] 전체 상품 표시 (DB에서 조회)
- [ ] 카테고리 필터링 (사료, 용품, 장난감, 산책)
- [ ] 상품명 검색
- [ ] 페이지네이션 (9개씩 표시)
- [ ] DB 연결 실패 시 더미 데이터 fallback

### 4.2 상품 상세 페이지 (`/products/detail/{no}`)
- [ ] DB에서 상품 정보 조회
- [ ] 조회수 자동 증가
- [ ] 상품 이미지, 설명, 가격 표시
- [ ] DB 연결 실패 시 더미 데이터 fallback

### 4.3 상품 등록 페이지 (`/products/create`)
- [ ] 관리자 권한 확인
- [ ] 상품 정보 입력 폼
- [ ] 가격 입력 시 "원" 표시 겹침 해결
- [ ] 상품 등록 후 DB에 저장
- [ ] 등록 성공 시 상품 목록으로 리다이렉트
- [ ] 성공 메시지 표시

## 5. 로그 확인 포인트

### 5.1 콘솔 로그에서 확인할 사항
```
=== ProductController /listp 호출됨 (DB 연동) ===
DB에서 조회된 상품 수: 9 / 전체: 42
```

### 5.2 DB 연결 실패 시 로그
```
상품 목록 DB 조회 중 오류 발생: ...
DB 연결 실패 - 더미 데이터로 fallback
```

## 6. 트러블슈팅

### 6.1 DB 연결 오류 시
1. application.properties에서 DB 설정 확인
2. MySQL 서비스 실행 상태 확인
3. 데이터베이스 및 테이블 존재 확인

### 6.2 더미 데이터가 표시되지 않을 때
1. insert_dummy_products.sql 실행 확인
2. 데이터 삽입 결과 확인
3. AUTO_INCREMENT 값 확인

### 6.3 상품 등록이 안될 때
1. 관리자 권한 확인
2. CSRF 토큰 설정 확인
3. products 테이블 구조 확인

## 7. 추가 개선 사항

### 7.1 구현 완료
- ✅ DB 연동 상품 목록 조회
- ✅ DB 연동 상품 상세 조회
- ✅ DB 연동 상품 등록
- ✅ 페이지네이션 (DB 기반)
- ✅ 카테고리 필터링 (DB 기반)
- ✅ 상품명 검색 (DB 기반)
- ✅ 가격 표시 UI 개선
- ✅ Fallback 시스템 (DB 실패 시 더미 데이터)

### 7.2 향후 개선 가능 사항
- 상품 수정/삭제 기능
- 상품 이미지 업로드 기능
- 상품 리뷰 시스템
- 장바구니 연동
- 관리자 대시보드

## 8. SQL 데이터베이스 초기화 (필요시)

기존 데이터를 지우고 다시 설정하려면:

```sql
-- 기존 데이터 삭제
DELETE FROM products;

-- AUTO_INCREMENT 초기화
ALTER TABLE products AUTO_INCREMENT = 1;

-- 더미 데이터 재삽입
source C:/hong260701/AI3_TEAM_ZOOTOPIA/ZOOTOPIA/SQL/zootopia/insert_dummy_products.sql;
```
