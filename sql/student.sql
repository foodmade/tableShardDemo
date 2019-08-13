/*
Navicat MySQL Data Transfer

Source Server         : 本地_root
Source Server Version : 50717
Source Host           : 127.0.0.1:3306
Source Database       : shardtable

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2019-08-13 10:58:53
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for student_0
-- ----------------------------
DROP TABLE IF EXISTS `student_0`;
CREATE TABLE `student_0` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `age` int(5) DEFAULT NULL,
  `rule` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of student_0
-- ----------------------------
INSERT INTO `student_0` VALUES ('1', '学生1', '10', '学生');

-- ----------------------------
-- Table structure for student_1
-- ----------------------------
DROP TABLE IF EXISTS `student_1`;
CREATE TABLE `student_1` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `age` int(5) DEFAULT NULL,
  `rule` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of student_1
-- ----------------------------
INSERT INTO `student_1` VALUES ('1', '学生2', '20', '班长');

-- ----------------------------
-- Table structure for student_2
-- ----------------------------
DROP TABLE IF EXISTS `student_2`;
CREATE TABLE `student_2` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `age` int(5) DEFAULT NULL,
  `rule` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of student_2
-- ----------------------------
INSERT INTO `student_2` VALUES ('1', '学生2', '20', '学渣');

-- ----------------------------
-- Table structure for student_3
-- ----------------------------
DROP TABLE IF EXISTS `student_3`;
CREATE TABLE `student_3` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `age` int(5) DEFAULT NULL,
  `rule` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of student_3
-- ----------------------------
INSERT INTO `student_3` VALUES ('1', '学生3', '30', '学习委员');

-- ----------------------------
-- Table structure for student_4
-- ----------------------------
DROP TABLE IF EXISTS `student_4`;
CREATE TABLE `student_4` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `age` int(5) DEFAULT NULL,
  `rule` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of student_4
-- ----------------------------
INSERT INTO `student_4` VALUES ('1', '学生4', '40', '清洁委员');

-- ----------------------------
-- Table structure for student_5
-- ----------------------------
DROP TABLE IF EXISTS `student_5`;
CREATE TABLE `student_5` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `age` int(5) DEFAULT NULL,
  `rule` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of student_5
-- ----------------------------
INSERT INTO `student_5` VALUES ('1', '学生5', '50', '数学课代表');

-- ----------------------------
-- Table structure for student_6
-- ----------------------------
DROP TABLE IF EXISTS `student_6`;
CREATE TABLE `student_6` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `age` int(5) DEFAULT NULL,
  `rule` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of student_6
-- ----------------------------
INSERT INTO `student_6` VALUES ('1', '学生6', '60', '语文课代表');

-- ----------------------------
-- Table structure for student_7
-- ----------------------------
DROP TABLE IF EXISTS `student_7`;
CREATE TABLE `student_7` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `age` int(5) DEFAULT NULL,
  `rule` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of student_7
-- ----------------------------
INSERT INTO `student_7` VALUES ('1', '学生7', '70', '学霸');

-- ----------------------------
-- Table structure for student_8
-- ----------------------------
DROP TABLE IF EXISTS `student_8`;
CREATE TABLE `student_8` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `age` int(5) DEFAULT NULL,
  `rule` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of student_8
-- ----------------------------
INSERT INTO `student_8` VALUES ('1', '学生8', '80', '副班长');

-- ----------------------------
-- Table structure for student_9
-- ----------------------------
DROP TABLE IF EXISTS `student_9`;
CREATE TABLE `student_9` (
  `id` bigint(10) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) DEFAULT NULL,
  `age` int(5) DEFAULT NULL,
  `rule` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of student_9
-- ----------------------------
INSERT INTO `student_9` VALUES ('1', '学生9', '90', '不法分子');
