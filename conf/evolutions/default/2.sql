# --- Sample dataset

# --- !Ups

delete from users_badges;
delete from badges;
delete from merchant_posts;
delete from merchants;
delete from users_fav_posts;
delete from users_dislike_posts;
delete from users_saved_posts;
delete from users_posts;
delete from users_categories;
delete from users_followers;
delete from categories;
delete from users;


INSERT INTO `badges` (`id`, `name`, `description`, `profile_pic_id`, `created_at`, `updated_at`)
VALUES
	(1, 'Badge_Name_1', 'description1', 'Badge_Pic_1', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	(2, 'Badge_Name_2', 'description2', 'Badge_Pic_2', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	(3, 'Badge_Name_3', 'description3', 'Badge_Pic_3', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	(4, 'Badge_Name_4', 'description4', 'Badge_Pic_4', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	(5, 'Badge_Name_5', 'description5', 'Badge_Pic_5', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	(6, 'Badge_Name_6', 'description6', 'Badge_Pic_6', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	(7, 'Badge_Name_7', 'description7', 'Badge_Pic_7', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	(8, 'Badge_Name_8', 'description8', 'Badge_Pic_8', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	(9, 'Badge_Name_9', 'description9', 'Badge_Pic_9', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	(10, 'Badge_Name_10', 'description10', 'Badge_Pic_10', '2012-02-10 14:34:03', '2012-02-10 14:34:03');

INSERT INTO `merchants` (`id`, `name`, `profile_pic_id`, `created_at`, `updated_at`)
VALUES
	(1, 'Brand_Name_Temp1', 'fdsjklsf', NULL, NULL),
	(2, 'Brand_Name_Temp2', 'fdsjklsf22', NULL, NULL),
	(3, 'Brand_Name_Temp3', 'fdsjklsf223', NULL, NULL);

INSERT INTO `categories` (`id`, `name`, `profile_pic_id`, `description`, `created_at`, `updated_at`)
VALUES
	('47891b2a53f411e1b371040cced6719e', 'Frank Fantastic Group47891b2a53f411e1b371040cced6719e', '4f352adb2d26dd38be000001', 'Frank Awesome Group47891b2a53f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	('47891bac53f411e1b371040cced6719e', 'Frank Fantastic Group47891bac53f411e1b371040cced6719e', '4f352adb2d26dd38be000003', 'Frank Awesome Group47891bac53f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	('47891bd453f411e1b371040cced6719e', 'Frank Fantastic Group47891bd453f411e1b371040cced6719e', '4f352adb2d26dd38be000005', 'Frank Awesome Group47891bd453f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	('47891bfc53f411e1b371040cced6719e', 'Frank Fantastic Group47891bfc53f411e1b371040cced6719e', '4f352adb2d26dd38be000007', 'Frank Awesome Group47891bfc53f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	('47891c1a53f411e1b371040cced6719e', 'Frank Fantastic Group47891c1a53f411e1b371040cced6719e', '4f352adb2d26dd38be000009', 'Frank Awesome Group47891c1a53f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	('47891c4253f411e1b371040cced6719e', 'Frank Fantastic Group47891c4253f411e1b371040cced6719e', '4f352adb2d26dd38be00000b', 'Frank Awesome Group47891c4253f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	('47891c6053f411e1b371040cced6719e', 'Frank Fantastic Group47891c6053f411e1b371040cced6719e', '4f352adb2d26dd38be00000d', 'Frank Awesome Group47891c6053f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	('47891cec53f411e1b371040cced6719e', 'Frank Fantastic Group47891cec53f411e1b371040cced6719e', '4f352adb2d26dd38be00000f', 'Frank Awesome Group47891cec53f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	('47891d0a53f411e1b371040cced6719e', 'Frank Fantastic Group47891d0a53f411e1b371040cced6719e', '4f352adb2d26dd38be000011', 'Frank Awesome Group47891d0a53f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	('47891d2853f411e1b371040cced6719e', 'Frank Fantastic Group47891d2853f411e1b371040cced6719e', '4f352adb2d26dd38be000013', 'Frank Awesome Group47891d2853f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03');


INSERT INTO `users` (`id`, `username`, `first_name`, `last_name`, `email`, `profile_pic_id`, `mailing_address`, `gender`, `date_of_birth`, `occupation`, `income`, `points`, `credits`, `fb_token`, `created_at`, `updated_at`)
VALUES
	('47891d6453f411e1b371040cced6719e', 'frank1', 'Frank47891d6453f411e1b371040cced6719e', 'one', 'frank47891d6453f411e1b371040cced6719e@email.com', '4f352adb2d26dd38be000015', NULL, NULL, NULL, NULL, NULL, 62, 95, '47891d8c53f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	('47891ee053f411e1b371040cced6719e', 'frank2', 'Frank47891ee053f411e1b371040cced6719e', 'two', 'frank47891ee053f411e1b371040cced6719e@email.com', '4f352adb2d26dd38be000017', NULL, NULL, NULL, NULL, NULL, 46, 6, '47891efe53f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	('47891f2653f411e1b371040cced6719e', 'frank3', 'Frank47891f2653f411e1b371040cced6719e', 'thrree', 'frank47891f2653f411e1b371040cced6719e@email.com', '4f352adb2d26dd38be000019', NULL, NULL, NULL, NULL, NULL, 24, 80, '47891f3a53f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	('47891f5853f411e1b371040cced6719e', 'frank4', 'Frank47891f5853f411e1b371040cced6719e', 'four', 'frank47891f5853f411e1b371040cced6719e@email.com', '4f352adb2d26dd38be00001b', NULL, NULL, NULL, NULL, NULL, 29, 73, '47891f6c53f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	('47891f9453f411e1b371040cced6719e', 'frank5', 'Frank47891f9453f411e1b371040cced6719e', 'five', 'frank47891f9453f411e1b371040cced6719e@email.com', '4f352adb2d26dd38be00001d', NULL, NULL, NULL, NULL, NULL, 60, 61, '47891fa853f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	('47891fc653f411e1b371040cced6719e', 'frank6', 'Frank47891fc653f411e1b371040cced6719e', 'six', 'frank47891fc653f411e1b371040cced6719e@email.com', '4f352adb2d26dd38be00001f', NULL, NULL, NULL, NULL, NULL, 12, 50, '47891fe453f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	('4789200253f411e1b371040cced6719e', 'frank7', 'Frank4789200253f411e1b371040cced6719e', 'seven', 'frank4789200253f411e1b371040cced6719e@email.com', '4f352adb2d26dd38be000021', NULL, NULL, NULL, NULL, NULL, 88, 25, '4789202053f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	('4789203e53f411e1b371040cced6719e', 'frank8', 'Frank4789203e53f411e1b371040cced6719e', 'eight', 'frank4789203e53f411e1b371040cced6719e@email.com', '4f352adb2d26dd38be000023', NULL, NULL, NULL, NULL, NULL, 89, 15, '4789205253f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	('4789207053f411e1b371040cced6719e', 'frank9', 'Frank4789207053f411e1b371040cced6719e', 'nine', 'frank4789207053f411e1b371040cced6719e@email.com', '4f352adb2d26dd38be000025', NULL, NULL, NULL, NULL, NULL, 4, 49, '4789208e53f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	('478920ac53f411e1b371040cced6719e', 'frank10', 'Frank478920ac53f411e1b371040cced6719e', 'ten', 'frank478920ac53f411e1b371040cced6719e@email.com', '4f352adb2d26dd38be000027', NULL, NULL, NULL, NULL, NULL, 53, 84, '478920c053f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03');


INSERT INTO `users_categories` (`id`, `user_id`, `category_id`, `created_at`, `updated_at`)
VALUES
	(51, '47891d6453f411e1b371040cced6719e', '47891c6053f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	(52, '47891ee053f411e1b371040cced6719e', '47891c6053f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	(53, '47891f2653f411e1b371040cced6719e', '47891c6053f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	(54, '47891f5853f411e1b371040cced6719e', '47891c6053f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	(55, '47891f9453f411e1b371040cced6719e', '47891c6053f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	(56, '47891fc653f411e1b371040cced6719e', '47891c6053f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	(57, '4789200253f411e1b371040cced6719e', '47891c6053f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	(58, '4789203e53f411e1b371040cced6719e', '47891c6053f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	(59, '4789207053f411e1b371040cced6719e', '47891c6053f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	(60, '478920ac53f411e1b371040cced6719e', '47891c6053f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03');

INSERT INTO `users_posts` (`id`, `user_id`, `post_id`, `category_id`, `created_at`, `updated_at`)
VALUES
	(21, '4789200253f411e1b371040cced6719e', '4f352adb2d26dd38be000029', '47891c6053f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	(22, '4789200253f411e1b371040cced6719e', '4f352adb2d26dd38be00002d', '47891c6053f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	(23, '4789200253f411e1b371040cced6719e', '4f352adb2d26dd38be000031', '47891c6053f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	(24, '4789200253f411e1b371040cced6719e', '4f352adb2d26dd38be000035', '47891c6053f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	(25, '4789200253f411e1b371040cced6719e', '4f352adb2d26dd38be000039', '47891c6053f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	(26, '4789200253f411e1b371040cced6719e', '4f352adb2d26dd38be00003d', '47891c6053f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	(27, '4789200253f411e1b371040cced6719e', '4f352adb2d26dd38be000041', '47891c6053f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	(28, '4789200253f411e1b371040cced6719e', '4f352adb2d26dd38be000045', '47891c6053f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	(29, '4789200253f411e1b371040cced6719e', '4f352adb2d26dd38be000049', '47891c6053f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03'),
	(30, '4789200253f411e1b371040cced6719e', '4f352adb2d26dd38be00004d', '47891c6053f411e1b371040cced6719e', '2012-02-10 14:34:03', '2012-02-10 14:34:03');

INSERT INTO `users_badges` (`user_id`, `badge_id`, `created_at`, `updated_at`)
VALUES
	('47891f2653f411e1b371040cced6719e', 1, NULL, NULL),
	('47891ee053f411e1b371040cced6719e', 1, NULL, NULL),
	('47891d6453f411e1b371040cced6719e', 1, NULL, NULL);

  
#--- !Downs
delete from users_badges;
delete from badges;
delete from merchant_posts;
delete from merchants;
delete from users_fav_posts;
delete from users_dislike_posts;
delete from users_saved_posts;
delete from users_posts;
delete from users_categories;
delete from users_followers;
delete from categories;
delete from users;


