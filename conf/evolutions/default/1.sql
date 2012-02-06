# --- First database schema

# --- !Ups

-- Create syntax for TABLE 'groups'

drop table if exists users_posts;
drop table if exists users_groups;
drop table if exists users_followers;
drop table if exists groups;
drop table if exists users;


-- Create syntax for TABLE 'groups'
CREATE TABLE `groups` (
  `id` varchar(32) NOT NULL DEFAULT '',
  `name` varchar(255) DEFAULT NULL,
  `profile_pic_id` varchar(32) DEFAULT NULL,
  `description` text,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Create syntax for TABLE 'users'
CREATE TABLE `users` (
  `id` varchar(32) NOT NULL DEFAULT '',
  `name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `profile_pic_id` varchar(32) DEFAULT NULL,
  `points` int(11) DEFAULT NULL,
  `credits` int(11) DEFAULT NULL,
  `fb_token` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `users_followers` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` varchar(32) DEFAULT NULL,
  `follower_id` varchar(32) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `follower_id` (`follower_id`),
  CONSTRAINT `users_followers_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `users_followers_ibfk_2` FOREIGN KEY (`follower_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Create syntax for TABLE 'users_groups'
CREATE TABLE `users_groups` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` varchar(32) DEFAULT NULL,
  `group_id` varchar(32) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `group_id` (`group_id`),
  CONSTRAINT `users_groups_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `users_groups_ibfk_2` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

-- Create syntax for TABLE 'users_posts'
CREATE TABLE `users_posts` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` varchar(32) DEFAULT NULL,
  `post_id` varchar(32) DEFAULT NULL,
  `group_id` varchar(32) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `users_posts_groups_ibfk1` (`group_id`),
  CONSTRAINT `users_posts_groups_ibfk1` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`),
  CONSTRAINT `users_posts_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8;

# --- !Downs
drop table if exists users_posts;
drop table if exists users_groups;
drop table if exists users_followers;
drop table if exists groups;
drop table if exists users;

