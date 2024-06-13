CREATE TABLE IF NOT EXISTS `user`
(
    `id`               BIGINT              NOT NULL AUTO_INCREMENT,
    `username`         VARCHAR(255) UNIQUE NOT NULL,
    `encoded_password` VARCHAR(255)        NOT NULL,
    constraint `user_pk` PRIMARY KEY (`id`)
);
CREATE TABLE IF NOT EXISTS `category`
(
    `id`      BIGINT       NOT NULL AUTO_INCREMENT,
    `user_id` BIGINT       NOT NULL,
    `title`   VARCHAR(255) NOT NULL,
    constraint `category_user_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
    constraint `category_pk` PRIMARY KEY (`id`)
);
CREATE TABLE IF NOT EXISTS `todo`
(
    `id`          BIGINT       NOT NULL AUTO_INCREMENT,
    `category_id` BIGINT       NOT NULL,
    `title`       VARCHAR(255) NOT NULL,
    `description` TEXT         NOT NULL,
    `completed`   BOOL         NOT NULL,
    `datetime`    DATETIME,
    `flagged`     BOOL         NOT NULL,
    constraint `todo_category_fk` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`) ON DELETE CASCADE,
    constraint `todo_pk` PRIMARY KEY (`id`)
);
CREATE TABLE IF NOT EXISTS `tag`
(
    `todo_id` BIGINT       NOT NULL,
    `tag`     VARCHAR(255) NOT NULL,
    constraint `tag_todo_todo_fk` FOREIGN KEY (`todo_id`) REFERENCES `todo` (`id`) ON DELETE CASCADE,
    constraint `tag_todo_pk` PRIMARY KEY (`tag`, `todo_id`)
);