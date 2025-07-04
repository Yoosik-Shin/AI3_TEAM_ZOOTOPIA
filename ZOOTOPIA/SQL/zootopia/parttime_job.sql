CREATE TABLE parttime_job (
job_id INT AUTO_INCREMENT PRIMARY KEY,
title VARCHAR(100) NOT NULL,
location VARCHAR(100) NOT NULL,
pay INT NOT NULL,
work_date DATETIME NOT NULL,
user_id INT NOT NULL,           -- 보호자(등록자)의 user_id
pet_info VARCHAR(100),
memo TEXT,
created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (user_id) REFERENCES users(user_id)
ON DELETE CASCADE
);

CREATE TABLE parttime_job_applicant (
applicant_id INT AUTO_INCREMENT PRIMARY KEY,
job_id INT NOT NULL,
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

CREATE TABLE parttime_job_comment (
comment_id INT AUTO_INCREMENT PRIMARY KEY,
job_id INT NOT NULL,
user_id INT NOT NULL,           -- 댓글 작성자 user_id
content TEXT NOT NULL,
created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
FOREIGN KEY (job_id) REFERENCES parttime_job(job_id)
ON DELETE CASCADE,
FOREIGN KEY (user_id) REFERENCES users(user_id)
ON DELETE CASCADE
);