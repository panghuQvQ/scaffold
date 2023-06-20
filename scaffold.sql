/*
 Navicat Premium Data Transfer

 Source Server         : mysql_scaffold
 Source Server Type    : MySQL
 Source Server Version : 80032
 Source Host           : localhost:13306
 Source Schema         : scaffold

 Target Server Type    : MySQL
 Target Server Version : 80032
 File Encoding         : 65001

 Date: 13/03/2023 15:02:53
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for app_auth_token
-- ----------------------------
DROP TABLE IF EXISTS `app_auth_token`;
CREATE TABLE `app_auth_token` (
  `id` int NOT NULL AUTO_INCREMENT,
  `device` varchar(100) DEFAULT NULL,
  `expire_time` datetime(6) DEFAULT NULL,
  `refresh_token` varchar(36) NOT NULL,
  `user_agent` varchar(255) DEFAULT NULL,
  `username` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_tiqxllfn3irou3lrmmdekot0e` (`refresh_token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for app_message
-- ----------------------------
DROP TABLE IF EXISTS `app_message`;
CREATE TABLE `app_message` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_by` varchar(50) DEFAULT NULL,
  `created_time` datetime(6) DEFAULT NULL,
  `updated_by` varchar(50) DEFAULT NULL,
  `updated_time` datetime(6) DEFAULT NULL,
  `body` varchar(255) DEFAULT NULL,
  `msg_from` varchar(255) DEFAULT NULL,
  `msg_to` varchar(255) DEFAULT NULL,
  `pic_url` varchar(255) DEFAULT NULL,
  `read_status` bit(1) NOT NULL,
  `route` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for app_permission
-- ----------------------------
DROP TABLE IF EXISTS `app_permission`;
CREATE TABLE `app_permission` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_by` varchar(50) DEFAULT NULL,
  `created_time` datetime(6) DEFAULT NULL,
  `updated_by` varchar(50) DEFAULT NULL,
  `updated_time` datetime(6) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `role_id` int DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKlyjcfyvbw9ettyeptymtak87n` (`role_id`),
  CONSTRAINT `FKlyjcfyvbw9ettyeptymtak87n` FOREIGN KEY (`role_id`) REFERENCES `app_role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for app_role
-- ----------------------------
DROP TABLE IF EXISTS `app_role`;
CREATE TABLE `app_role` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_by` varchar(50) DEFAULT NULL,
  `created_time` datetime(6) DEFAULT NULL,
  `updated_by` varchar(50) DEFAULT NULL,
  `updated_time` datetime(6) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_dyf5hu50ddvjoy0pfpleevwfl` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for app_user
-- ----------------------------
DROP TABLE IF EXISTS `app_user`;
CREATE TABLE `app_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_by` varchar(50) DEFAULT NULL,
  `created_time` datetime(6) DEFAULT NULL,
  `updated_by` varchar(50) DEFAULT NULL,
  `updated_time` datetime(6) DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `disabled` bit(1) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `username` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_3k4cplvh82srueuttfkwnylq0` (`username`),
  UNIQUE KEY `UK_exslcon9jmfy0xhclbtpf26vo` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for app_deleted_user
-- ----------------------------
DROP TABLE IF EXISTS `app_deleted_user`;
CREATE TABLE `app_deleted_user` (
  `id` int NOT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  `created_by` varchar(255) DEFAULT NULL,
  `created_time` datetime(6) DEFAULT NULL,
  `delete_reason` varchar(500) DEFAULT NULL,
  `deleted_by` varchar(255) DEFAULT NULL,
  `deleted_time` datetime(6) NOT NULL,
  `disabled` bit(1) NOT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `role_ids` varchar(255) DEFAULT NULL,
  `updated_by` varchar(255) DEFAULT NULL,
  `updated_time` datetime(6) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for app_user_active_log
-- ----------------------------
DROP TABLE IF EXISTS `app_user_active_log`;
CREATE TABLE `app_user_active_log` (
  `_id` varchar(255) NOT NULL,
  `created_by` varchar(50) DEFAULT NULL,
  `created_time` datetime(6) DEFAULT NULL,
  `updated_by` varchar(50) DEFAULT NULL,
  `updated_time` datetime(6) DEFAULT NULL,
  `active_date` varchar(16) DEFAULT NULL,
  `times` int NOT NULL,
  `username` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`_id`),
  UNIQUE KEY `UKdjdiumokbux25v1uvkrv0mj14` (`username`,`active_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for app_user_favorite
-- ----------------------------
DROP TABLE IF EXISTS `app_user_favorite`;
CREATE TABLE `app_user_favorite` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_by` varchar(50) DEFAULT NULL,
  `created_time` datetime(6) DEFAULT NULL,
  `updated_by` varchar(50) DEFAULT NULL,
  `updated_time` datetime(6) DEFAULT NULL,
  `category` varchar(255) DEFAULT NULL,
  `details` varchar(255) DEFAULT NULL,
  `resource_id` varchar(255) DEFAULT NULL,
  `route` varchar(255) DEFAULT NULL,
  `secondary_category` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for app_user_preference
-- ----------------------------
DROP TABLE IF EXISTS `app_user_preference`;
CREATE TABLE `app_user_preference` (
  `id` int NOT NULL AUTO_INCREMENT,
  `created_by` varchar(50) DEFAULT NULL,
  `created_time` datetime(6) DEFAULT NULL,
  `updated_by` varchar(50) DEFAULT NULL,
  `updated_time` datetime(6) DEFAULT NULL,
  `preference_key` varchar(255) DEFAULT NULL,
  `preference_value` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for app_user_role
-- ----------------------------
DROP TABLE IF EXISTS `app_user_role`;
CREATE TABLE `app_user_role` (
  `user_id` int NOT NULL,
  `role_id` int NOT NULL,
  KEY `FK6hkq1uibwvjsusnnxrsm1gh83` (`role_id`),
  KEY `FKfnlxi1bmv5ao8u3nf30ymq7xa` (`user_id`),
  CONSTRAINT `FK6hkq1uibwvjsusnnxrsm1gh83` FOREIGN KEY (`role_id`) REFERENCES `app_role` (`id`),
  CONSTRAINT `FKfnlxi1bmv5ao8u3nf30ymq7xa` FOREIGN KEY (`user_id`) REFERENCES `app_user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ----------------------------
-- Table structure for operation_log
-- ----------------------------
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log` (
  `id` int NOT NULL AUTO_INCREMENT,
  `operation` varchar(255) DEFAULT NULL COMMENT '操作，由SysLog注解定义',
  `request_method` varchar(255) DEFAULT NULL COMMENT '请求方法eg:XXController.method',
  `parameters` text COMMENT '方法入参',
  `response` text COMMENT '返回值',
  `operate_time` datetime(3) DEFAULT NULL COMMENT '操作时间',
  `operator` varchar(255) DEFAULT NULL COMMENT '操作人',
  `ip` varchar(255) DEFAULT NULL COMMENT 'ip',
  `user_agent` varchar(255) DEFAULT NULL COMMENT 'userAgent',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `oplogs_sort_op_time` (`operate_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

SET FOREIGN_KEY_CHECKS = 1;
