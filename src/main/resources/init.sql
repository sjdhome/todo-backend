CREATE TABLE IF NOT EXISTS `user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT,
    `username` VARCHAR(255) UNIQUE NOT NULL,
    constraint `user_pk` PRIMARY KEY (`id`)
);
CREATE TABLE IF NOT EXISTS `user_security` (
    `user_id` BIGINT NOT NULL,
    `encoded_password` VARCHAR(255) NOT NULL,
    constraint `user_security_user_fk` FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
    constraint `user_security_pk` PRIMARY KEY (`user_id`)
);
CREATE TABLE IF NOT EXISTS `todo` (
    `id` BIGINT NOT NULL,
    `title` VARCHAR(255) NOT NULL,
    `description` TEXT NOT NULL,
    `completed` BOOL NOT NULL,
    `datetime` DATETIME NOT NULL,
    `flagged` BOOL NOT NULL,
    constraint `todo_pk` PRIMARY KEY (`id`)
);
CREATE TABLE IF NOT EXISTS `todo_user` (
    `todo_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    constraint `todo_fk` FOREIGN KEY (`todo_id`) REFERENCES todo(`id`) ON DELETE CASCADE,
    constraint `user_fk` FOREIGN KEY (`user_id`) REFERENCES user(`id`) ON DELETE CASCADE,
    constraint `todo_user_pk` PRIMARY KEY (`todo_id`, `user_id`)
);
CREATE TABLE IF NOT EXISTS `tag` (
    `id` BIGINT NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    constraint `tag_pk` PRIMARY KEY (`id`)
);
CREATE TABLE IF NOT EXISTS `tag_todo` (
    `tag_id` BIGINT NOT NULL,
    `todo_id` BIGINT NOT NULL,
    constraint `tag_todo_tag_fk` FOREIGN KEY (`tag_id`) REFERENCES tag(`id`) ON DELETE CASCADE,
    constraint `tag_todo_todo_fk` FOREIGN KEY (`todo_id`) REFERENCES todo(`id`) ON DELETE CASCADE,
    constraint `tag_todo_pk` PRIMARY KEY (`tag_id`, `todo_id`)
);
CREATE TABLE IF NOT EXISTS `tag_user` (
    `tag_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    constraint `tag_user_tag_fk` FOREIGN KEY (`tag_id`) REFERENCES tag(`id`) ON DELETE CASCADE,
    constraint `tag_user_user_fk` FOREIGN KEY (`user_id`) REFERENCES user(`id`) ON DELETE CASCADE,
    constraint `tag_user_pk` PRIMARY KEY (`tag_id`, `user_id`)
);
CREATE TABLE IF NOT EXISTS `category` (
    `id` BIGINT NOT NULL,
    `title` VARCHAR(255) NOT NULL,
    constraint `category_pk` PRIMARY KEY (`id`)
);
CREATE TABLE IF NOT EXISTS `category_todo` (
    `category_id` BIGINT NOT NULL,
    `todo_id` BIGINT NOT NULL,
    `order` INT NOT NULL,
    constraint `category_todo_category_fk` FOREIGN KEY (`category_id`) REFERENCES category(`id`) ON DELETE CASCADE,
    constraint `category_todo_todo_fk` FOREIGN KEY (`todo_id`) REFERENCES todo(`id`) ON DELETE CASCADE,
    constraint `category_todo_pk` PRIMARY KEY (`category_id`, `todo_id`)
);
CREATE TABLE IF NOT EXISTS `category_user` (
    `category_id` BIGINT NOT NULL,
    `user_id` BIGINT NOT NULL,
    constraint `category_user_category_fk` FOREIGN KEY (`category_id`) REFERENCES category(`id`) ON DELETE CASCADE,
    constraint `category_user_user_fk` FOREIGN KEY (`user_id`) REFERENCES user(`id`) ON DELETE CASCADE,
    constraint `category_user_pk` PRIMARY KEY (`category_id`, `user_id`)
);