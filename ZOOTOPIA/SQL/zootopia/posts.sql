DROP TABLE IF EXISTS `post_likes`;

CREATE TABLE `post_likes` (
  `like_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `created_at` DATETIME NULL,
  `post_id` INT NOT NULL,
  `user_id` INT NOT NULL,
  UNIQUE (`post_id`, `user_id`)
);

DROP TABLE IF EXISTS `lost_animal_images`;

CREATE TABLE `lost_animal_images` (
  `image_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `image_url` VARCHAR(255) NOT NULL,
  `ordering` INT DEFAULT 0,
  `post_id` INT
);

DROP TABLE IF EXISTS `lost_animal_videos`;

CREATE TABLE `lost_animal_videos` (
  `video_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `video_url` VARCHAR(255) NOT NULL,
  `post_id` INT
);

DROP TABLE IF EXISTS `lost_animals`;

CREATE TABLE `lost_animals` (
  `post_id` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(200) NOT NULL,
  `content` TEXT NOT NULL,            
  `lost_location` VARCHAR(255),
  `lost_time` DATETIME,
  `contact_phone` VARCHAR(30),

  `view_count` INT DEFAULT 0,
  `like_count` INT DEFAULT 0,
  `comment_count` INT DEFAULT 0,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_id` INT DEFAULT NULL,
  `thumbnail_url` VARCHAR(255) DEFAULT NULL,

  PRIMARY KEY (`post_id`),
  KEY `FK_users_TO_lost_animals` (`user_id`),
  CONSTRAINT `FK_users_TO_lost_animals` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


DROP TABLE IF EXISTS `posts`;

CREATE TABLE `posts` (
  `post_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `category` ENUM('자유글', '질문글', '자랑글') DEFAULT '자유글',
  `title` VARCHAR(200) NOT NULL,
  `content` TEXT NOT NULL,
  `view_count` INT DEFAULT 0,
  `like_count` INT DEFAULT 0,
  `comment_count` INT DEFAULT 0,
  `created_at` DATETIME DEFAULT NOW(),
  `updated_at` DATETIME DEFAULT NOW() ON UPDATE NOW(),
  `user_id` INT
);

DROP TABLE IF EXISTS `post_images`;

CREATE TABLE `post_images` (
  `image_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `image_url` VARCHAR(255) NOT NULL,
  `ordering` INT DEFAULT 0,
  `post_id` INT
);

DROP TABLE IF EXISTS `post_videos`;

CREATE TABLE `post_videos` (
  `video_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `video_url` VARCHAR(255) NOT NULL,
  `post_id` INT
);

DROP TABLE IF EXISTS `post_comments`;

CREATE TABLE `post_comments` (
  `comment_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `content` TEXT NOT NULL,
  `created_at` DATETIME DEFAULT NOW(),
  `updated_at` DATETIME DEFAULT NOW() ON UPDATE NOW(),
  `is_deleted` BOOLEAN DEFAULT FALSE,
  `user_id` INT,
  `post_id` INT
);

ALTER TABLE post_likes
DROP FOREIGN KEY FK_posts_TO_post_likes;

ALTER TABLE post_likes
ADD CONSTRAINT FK_posts_TO_post_likes
FOREIGN KEY (post_id) REFERENCES posts(post_id)
ON DELETE CASCADE;

ALTER TABLE post_comments
DROP FOREIGN KEY FK_posts_TO_post_comments;

ALTER TABLE post_comments
ADD CONSTRAINT FK_posts_TO_post_comments
FOREIGN KEY (post_id) REFERENCES posts(post_id)
ON DELETE CASCADE;


ALTER TABLE lost_animal_comments
DROP FOREIGN KEY FK_lost_animals_TO_lost_animal_comments;

ALTER TABLE lost_animal_comments
ADD CONSTRAINT FK_lost_animal_TO_lost_animal_comments
FOREIGN KEY (post_id) REFERENCES lost_animals(post_id)
ON DELETE CASCADE;


DROP TABLE IF EXISTS `user_pets`;

CREATE TABLE `user_pets` (
  `pet_id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(50) NOT NULL,
  `species` VARCHAR(100) NOT NULL,
  `breed` VARCHAR(100),
  `birth_date` DATE,
  `created_at` DATETIME DEFAULT NOW(),
  `user_id` INT
);


ALTER TABLE user_pets
MODIFY COLUMN name VARCHAR(50) NULL,
MODIFY COLUMN species VARCHAR(100) NULL;







       


-- FK for post_likes
ALTER TABLE `post_likes`
  ADD CONSTRAINT `FK_posts_TO_post_likes` FOREIGN KEY (`post_id`) REFERENCES `posts`(`post_id`),
  ADD CONSTRAINT `FK_users_TO_post_likes` FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`);

-- FK for lost_animal_images & videos
ALTER TABLE `lost_animal_images`
  ADD CONSTRAINT `FK_lost_animals_TO_images` FOREIGN KEY (`post_id`) REFERENCES `lost_animals`(`post_id`);

ALTER TABLE `lost_animal_videos`
  ADD CONSTRAINT `FK_lost_animals_TO_videos` FOREIGN KEY (`post_id`) REFERENCES `lost_animals`(`post_id`);

-- FK for lost_animals
ALTER TABLE `lost_animals`
  ADD CONSTRAINT `FK_users_TO_lost_animals` FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`);

-- FK for posts
ALTER TABLE `posts`
  ADD CONSTRAINT `FK_users_TO_posts` FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`);

-- FK for post_images & videos
ALTER TABLE `post_images`
  ADD CONSTRAINT `FK_posts_TO_post_images` FOREIGN KEY (`post_id`) REFERENCES `posts`(`post_id`);

ALTER TABLE `post_videos`
  ADD CONSTRAINT `FK_posts_TO_post_videos` FOREIGN KEY (`post_id`) REFERENCES `posts`(`post_id`);

-- FK for post_comments
ALTER TABLE `post_comments`
  ADD CONSTRAINT `FK_users_TO_post_comments` FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`),
  ADD CONSTRAINT `FK_posts_TO_post_comments` FOREIGN KEY (`post_id`) REFERENCES `posts`(`post_id`);

-- FK for user_pets
ALTER TABLE `user_pets`
  ADD CONSTRAINT `FK_users_TO_user_pets` FOREIGN KEY (`user_id`) REFERENCES `users`(`user_id`);


ALTER TABLE posts ADD COLUMN thumbnail_url VARCHAR(255);
<<<<<<< HEAD



CREATE TABLE tags (
  tag_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(50) NOT NULL UNIQUE
) COMMENT='태그 목록';



CREATE TABLE post_tags (
  post_id INT NOT NULL,
  tag_id INT NOT NULL,
  PRIMARY KEY (post_id, tag_id),
  FOREIGN KEY (post_id) REFERENCES posts(post_id) ON DELETE CASCADE,
  FOREIGN KEY (tag_id) REFERENCES tags(tag_id) ON DELETE CASCADE
) COMMENT='게시글-태그 연결';

CREATE TABLE lost_animal_tags (
  post_id INT NOT NULL,
  tag_id INT NOT NULL,
  PRIMARY KEY (post_id, tag_id),
  FOREIGN KEY (post_id) REFERENCES lost_animals(post_id) ON DELETE CASCADE,
  FOREIGN KEY (tag_id) REFERENCES tags(tag_id) ON DELETE CASCADE
) COMMENT='유실동물 게시글-태그 연결';


CREATE TABLE `lost_animal_comments` (
  `comment_id` INT NOT NULL AUTO_INCREMENT,
  `content` TEXT NOT NULL,
  `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `is_deleted` TINYINT(1) DEFAULT 0,
  `user_id` INT DEFAULT NULL,
  `post_id` INT DEFAULT NULL,
  `secret` TINYINT(1) DEFAULT 0, 

  PRIMARY KEY (`comment_id`),
  KEY `FK_users_TO_lost_animal_comments` (`user_id`),
  KEY `FK_lost_post_TO_lost_animal_comments` (`post_id`),

  CONSTRAINT `FK_lost_post_TO_lost_animal_comments` 
    FOREIGN KEY (`post_id`) REFERENCES `lost_animals` (`post_id`) ON DELETE CASCADE,
  CONSTRAINT `FK_users_TO_lost_animal_comments` 
    FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='유실동물 게시판 댓글';

ALTER TABLE post_comments
ADD COLUMN parent_id INT NULL;


ALTER TABLE lost_animal_comments
ADD COLUMN parent_id INT NULL;