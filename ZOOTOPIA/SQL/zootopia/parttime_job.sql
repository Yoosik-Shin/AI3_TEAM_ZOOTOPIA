DROP TABLE IF EXISTS `parttime_job`;

CREATE TABLE parttime_job (
job_id BIGINT AUTO_INCREMENT PRIMARY KEY,
title VARCHAR(100) NOT NULL,
location VARCHAR(100) NOT NULL,
pay INT NOT NULL,
start_date DATE NOT NULL,
end_date DATE NOT NULL,
user_id INT NOT NULL,           -- 보호자(등록자)의 user_id
pet_info VARCHAR(100),
memo TEXT NOT NULL,
created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (user_id) REFERENCES users(user_id)
ON DELETE CASCADE
);

DROP TABLE IF EXISTS `parttime_job_applicant`;

CREATE TABLE parttime_job_applicant (
applicant_id INT AUTO_INCREMENT PRIMARY KEY,
job_id BIGINT NOT NULL,
user_id INT NOT NULL,           -- 지원자 user_id
rating FLOAT DEFAULT 0,
review_count INT DEFAULT 0,
introduction TEXT,
created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (job_id) REFERENCES parttime_job(job_id)
ON DELETE CASCADE,
FOREIGN KEY (user_id) REFERENCES users(user_id)
ON DELETE CASCADE
);

DROP TABLE IF EXISTS `parttime_job_comment`;

CREATE TABLE parttime_job_comment (
    comment_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,  -- 로그인 사용자용 (nullable)
    writer VARCHAR(100) NOT NULL,  -- 비로그인 사용자용 닉네임
    content TEXT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL
);

