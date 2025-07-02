DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `user_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `email` VARCHAR(100) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `nickname` VARCHAR(50) NOT NULL,
  `intro` VARCHAR(255) NULL,
  `phone` VARCHAR(20) NULL,
  `profile_img` VARCHAR(255) NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `ENABLED` INT DEFAULT 1
) COMMENT='회원';



INSERT INTO users (user_id, email, password, nickname, intro, phone, profile_img, created_at) VALUES
(1, 'admin', '$2a$10$2XMPFzReUtpL32VoJznvmuD0n1eV5BNSczGb3oFGdDtd.6cqW5R5O', '관리자계정', '커뮤니티 관리 전담 운영자입니다.', '010-0000-0000', '/images/profile10.png', NOW());