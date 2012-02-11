# --- First database schema

# --- !Ups

-- Create syntax for TABLE 'groups'
drop table if exists users_badges;
drop table if exists badges;
drop table if exists brands;
drop table if exists users_posts;
drop table if exists users_groups;
drop table if exists users_followers;
drop table if exists groups;
drop table if exists users;

-- Create syntax for TABLE 'badges'
CREATE TABLE `badges` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `badge_pic` varchar(32) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Create syntax for TABLE 'brands'
CREATE TABLE `brands` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `profile_pic_id` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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


-- Create syntax for TABLE 'user_badges'
CREATE TABLE `users_badges` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` varchar(32) DEFAULT NULL,
  `badge_id` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- Create syntax for TABLE 'users'
CREATE TABLE `users` (
  `id` varchar(32) NOT NULL DEFAULT '',
  `username` varchar(255) DEFAULT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `profile_pic_id` varchar(32) DEFAULT NULL,
  `mailing_address` varchar(255) DEFAULT NULL,
  `gender` char(1) DEFAULT NULL,
  `date_of_birth` date DEFAULT NULL,
  `occupation` varchar(255) DEFAULT NULL,
  `income` float DEFAULT NULL,
  `points` int(11) DEFAULT '0',
  `credits` int(11) DEFAULT '0',
  `fb_token` varchar(255) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Create syntax for TABLE 'users_followers'
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
  `points` int(11) DEFAULT '0',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  KEY `group_id` (`group_id`),
  CONSTRAINT `users_groups_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`),
  CONSTRAINT `users_groups_ibfk_2` FOREIGN KEY (`group_id`) REFERENCES `groups` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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


-- Create syntax for TABLE 'groups'
# --- !Downs
drop table if exists users_badges;
drop table if exists badges;
drop table if exists brands;
drop table if exists users_posts;
drop table if exists users_groups;
drop table if exists users_followers;
drop table if exists groups;
drop table if exists users;

