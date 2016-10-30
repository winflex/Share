/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50540
Source Host           : localhost:3306
Source Database       : sharing

Target Server Type    : MYSQL
Target Server Version : 50540
File Encoding         : 65001

Date: 2016-10-30 21:22:48
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'id',
  `share_id` int(10) unsigned NOT NULL COMMENT '所属分享id',
  `content` varchar(100) NOT NULL DEFAULT '' COMMENT '评论内容',
  `from_user_id` int(10) unsigned NOT NULL COMMENT '发评论的用户',
  `to_user_id` int(10) unsigned NOT NULL COMMENT '被评论的用户',
  `kind` tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '评论的类型, 0 = 直接评论的分享, 1 = 回复别人的评论',
  `comment_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '评论时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否被删除, 0=未删除, 1=已删除',
  PRIMARY KEY (`id`),
  KEY `FK_Comment_Share_0` (`share_id`),
  KEY `FK_Comment_User_0` (`from_user_id`),
  KEY `FK_Comment_User_1` (`to_user_id`),
  CONSTRAINT `FK_Comment_Share_0` FOREIGN KEY (`share_id`) REFERENCES `share` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_Comment_User_0` FOREIGN KEY (`from_user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_Comment_User_1` FOREIGN KEY (`to_user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for forbidenword
-- ----------------------------
DROP TABLE IF EXISTS `forbidenword`;
CREATE TABLE `forbidenword` (
  `content` varchar(50) NOT NULL COMMENT '关键词',
  `usable` bit(1) NOT NULL DEFAULT b'1' COMMENT '是否启用, 0=不启用, 1=启用',
  PRIMARY KEY (`content`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for friendship
-- ----------------------------
DROP TABLE IF EXISTS `friendship`;
CREATE TABLE `friendship` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `ask_user_id` int(10) unsigned NOT NULL COMMENT '好友关系中的主动方',
  `asked_user_id` int(10) unsigned NOT NULL COMMENT '好友关系中的被动方',
  `pending` bit(1) NOT NULL DEFAULT b'0' COMMENT '该关系是否确定,0=未确定,1=已确定',
  `ask_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '好友请求时间',
  `answer_time` timestamp NULL DEFAULT NULL COMMENT '请求通过时间',
  PRIMARY KEY (`id`),
  KEY `FK_Friendship_User_0` (`ask_user_id`),
  KEY `FK_Friendship_User_1` (`asked_user_id`),
  CONSTRAINT `FK_Friendship_User_0` FOREIGN KEY (`ask_user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_Friendship_User_1` FOREIGN KEY (`asked_user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for picture
-- ----------------------------
DROP TABLE IF EXISTS `picture`;
CREATE TABLE `picture` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '图片id',
  `path` varchar(255) NOT NULL COMMENT '图片在服务器上的路径',
  `upload_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '图片上传时间',
  `upload_user_id` int(10) unsigned NOT NULL COMMENT '上传改图片的用户id',
  PRIMARY KEY (`id`),
  KEY `FK_Picture_User_0` (`upload_user_id`),
  CONSTRAINT `FK_Picture_User_0` FOREIGN KEY (`upload_user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for praise
-- ----------------------------
DROP TABLE IF EXISTS `praise`;
CREATE TABLE `praise` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '赞id',
  `user_id` int(10) unsigned NOT NULL COMMENT '用户id',
  `share_id` int(10) unsigned NOT NULL COMMENT '分享id',
  `praise_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '点赞时间',
  PRIMARY KEY (`id`),
  KEY `FK_Praise_User_0` (`user_id`),
  KEY `FK_Praise_Share_0` (`share_id`),
  CONSTRAINT `FK_Praise_Share_0` FOREIGN KEY (`share_id`) REFERENCES `share` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_Praise_User_0` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `description` varchar(255) NOT NULL DEFAULT '' COMMENT '角色描述',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否被删除, 0=未删除, 1=已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for share
-- ----------------------------
DROP TABLE IF EXISTS `share`;
CREATE TABLE `share` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '分享id',
  `user_id` int(10) unsigned NOT NULL COMMENT '所属用户',
  `content` varchar(300) NOT NULL DEFAULT '' COMMENT '文字内容',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '分享时间',
  `praises` int(10) unsigned NOT NULL DEFAULT '0' COMMENT '点赞数, Share是高频select的, 为避免每次都join而设计该冗余字段',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否被删除, 0=未删除, 1=已删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for share_collection
-- ----------------------------
DROP TABLE IF EXISTS `share_collection`;
CREATE TABLE `share_collection` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '收藏唯一标识',
  `user_id` int(10) unsigned NOT NULL COMMENT '用户id',
  `share_id` int(10) unsigned NOT NULL COMMENT '所收藏的分享id',
  `collect_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '收藏时间',
  PRIMARY KEY (`id`),
  KEY `FK_ShareCollection_User_0` (`user_id`),
  KEY `FK_ShareCollection_Share_0` (`share_id`),
  CONSTRAINT `FK_ShareCollection_Share_0` FOREIGN KEY (`share_id`) REFERENCES `share` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_ShareCollection_User_0` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for share_picture_relation
-- ----------------------------
DROP TABLE IF EXISTS `share_picture_relation`;
CREATE TABLE `share_picture_relation` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `share_id` int(10) unsigned NOT NULL COMMENT '分享id',
  `picture_id` int(10) unsigned NOT NULL COMMENT '图片id',
  PRIMARY KEY (`id`),
  KEY `FK_SharePictureRelation_Share_0` (`share_id`),
  KEY `FK_SharePictureRelation_Picture_0` (`picture_id`),
  CONSTRAINT `FK_SharePictureRelation_Picture_0` FOREIGN KEY (`picture_id`) REFERENCES `picture` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_SharePictureRelation_Share_0` FOREIGN KEY (`share_id`) REFERENCES `share` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `sex` varchar(2) NOT NULL DEFAULT '男' COMMENT '性别',
  `signature` varchar(255) NOT NULL DEFAULT '' COMMENT '个性签名',
  `role_id` int(11) unsigned NOT NULL COMMENT '角色id ',
  `register_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
  `head_image_id` int(11) unsigned DEFAULT NULL COMMENT '头像图片id',
  `self_forbid` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否自禁用, 自禁用后用户的所有分享对他人不可见，0=未禁用, 1=已禁用',
  `admin_forbid` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否管理员禁用, 0=未禁用, 1=已禁用',
  `deleted` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否被删除, 0=未删除, 1=已删除',
  PRIMARY KEY (`id`),
  KEY `FK_User_Role_0` (`role_id`),
  KEY `FK_User_Picture_0` (`head_image_id`),
  CONSTRAINT `FK_User_Picture_0` FOREIGN KEY (`head_image_id`) REFERENCES `picture` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_User_Role_0` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for user_collection
-- ----------------------------
DROP TABLE IF EXISTS `user_collection`;
CREATE TABLE `user_collection` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `user_id` int(10) unsigned NOT NULL COMMENT '用户id',
  `collected_user_id` int(10) unsigned NOT NULL COMMENT '被收藏的用户id',
  PRIMARY KEY (`id`),
  KEY `FK_UserCollection_User_0` (`user_id`),
  KEY `FK_UserCollection_User_1` (`collected_user_id`),
  CONSTRAINT `FK_UserCollection_User_0` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_UserCollection_User_1` FOREIGN KEY (`collected_user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
