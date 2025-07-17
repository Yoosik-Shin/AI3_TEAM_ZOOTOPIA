-- 슈퍼어드민 계정 추가
-- 비밀번호: admin1234 
-- 기존 admin 계정과 동일한 해시 사용 (동일한 비밀번호이므로)

-- 기존 admin@gmail.com 계정이 있다면 삭제
DELETE FROM user_auth WHERE email = 'admin@gmail.com';
DELETE FROM users WHERE email = 'admin@gmail.com';

-- 새로운 슈퍼어드민 계정 추가
INSERT INTO users (email, password, nickname, intro, phone, profile_img, created_at, enabled) VALUES
('admin@gmail.com', '$2a$10$2XMPFzReUtpL32VoJznvmuD0n1eV5BNSczGb3oFGdDtd.6cqW5R5O', '슈퍼어드민', '시스템 관리자입니다.', '010-1234-5678', '/img/default-profile.png', NOW(), 1);

-- 슈퍼어드민 권한 추가
INSERT INTO user_auth (email, auth) VALUES
('admin@gmail.com', 'ROLE_ADMIN'),
('admin@gmail.com', 'ROLE_SUPER_ADMIN');
