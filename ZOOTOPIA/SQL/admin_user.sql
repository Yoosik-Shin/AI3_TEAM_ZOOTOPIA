-- 관리자 계정 생성 (super@admin.com)
-- 비밀번호: admin123 (BCrypt 암호화)
-- 현재, 비밀번호: 123456

-- users 테이블에 관리자 추가
INSERT INTO users (user_id, user_pw, name, email, enabled) 
VALUES ('super@admin.com', '$2a$10$dXJ3SW6G7P0lksqr.G06zuIBhFCVRE8H9c8A0UhPKs2XQ7gQ2K5Jm', '관리자', 'super@admin.com', 1)
ON DUPLICATE KEY UPDATE
user_pw = '$2a$10$dXJ3SW6G7P0lksqr.G06zuIBhFCVRE8H9c8A0UhPKs2XQ7gQ2K5Jm',
name = '관리자',
email = 'super@admin.com',
enabled = 1;

-- user_auth 테이블에 관리자 권한 추가
INSERT INTO user_auth (user_id, auth) 
VALUES ('super@admin.com', 'ROLE_ADMIN')
ON DUPLICATE KEY UPDATE
auth = 'ROLE_ADMIN';

INSERT INTO user_auth (user_id, auth) 
VALUES ('super@admin.com', 'ROLE_USER')
ON DUPLICATE KEY UPDATE
auth = 'ROLE_USER';

-- 기존 관리자 계정의 비밀번호를 admin123으로 업데이트 (필요시 실행)
UPDATE users 
SET user_pw = '$2a$10$dXJ3SW6G7P0lksqr.G06zuIBhFCVRE8H9c8A0UhPKs2XQ7gQ2K5Jm' 
WHERE user_id = 'super@admin.com';
