/*
Navicat MySQL Data Transfer

Source Server         : 192.168.7.187
Source Server Version : 50626
Source Host           : 192.168.7.187:3306
Source Database       : admin

Target Server Type    : MYSQL
Target Server Version : 50626
File Encoding         : 65001

Date: 2017-03-09 15:56:15
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `tb_admin_menu`
-- ----------------------------
DROP TABLE IF EXISTS `tb_admin_menu`;
CREATE TABLE `tb_admin_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一性编号',
  `status` tinyint(3) unsigned DEFAULT NULL COMMENT '通用状态 1,enabled,可用;2,disabled,不可用;3,deleted,已删除;',
  `name` varchar(32) DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(128) DEFAULT NULL COMMENT '菜单URL',
  `parentId` bigint(20) DEFAULT NULL COMMENT '上级菜单id',
  `sequence` int(10) unsigned DEFAULT NULL COMMENT '菜单排序号',
  `createdTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updatedTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='管理员菜单表';

-- ----------------------------
-- Records of tb_admin_menu
-- ----------------------------
INSERT INTO `tb_admin_menu` VALUES ('1', '1', '系统管理', null, '0', '1', null, null);
INSERT INTO `tb_admin_menu` VALUES ('2', '1', '用户管理', 'admin_user_list.html', '1', '1', null, null);
INSERT INTO `tb_admin_menu` VALUES ('3', '1', '角色管理', 'admin_role_list.html', '1', '2', null, null);
INSERT INTO `tb_admin_menu` VALUES ('4', '1', '菜单管理', 'admin_menu_list.html', '1', '3', null, null);

-- ----------------------------
-- Table structure for `tb_admin_role`
-- ----------------------------
DROP TABLE IF EXISTS `tb_admin_role`;
CREATE TABLE `tb_admin_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一性编号',
  `status` tinyint(3) unsigned DEFAULT NULL COMMENT '通用状态 1,enabled,可用;2,disabled,不可用;3,deleted,已删除;',
  `name` varchar(32) DEFAULT NULL COMMENT '角色名称',
  `code` varchar(32) DEFAULT NULL COMMENT '角色代码',
  `description` varchar(128) DEFAULT NULL COMMENT '角色描述',
  `createdTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updatedTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COMMENT='系统角色表';

-- ----------------------------
-- Records of tb_admin_role
-- ----------------------------
INSERT INTO `tb_admin_role` VALUES ('6', '1', '超级管理员', 'administrator', '超级管理员', null, null);
INSERT INTO `tb_admin_role` VALUES ('7', '1', '财务', 'finance', '财务', null, null);

-- ----------------------------
-- Table structure for `tb_admin_role_menu`
-- ----------------------------
DROP TABLE IF EXISTS `tb_admin_role_menu`;
CREATE TABLE `tb_admin_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一性编号',
  `status` tinyint(3) unsigned DEFAULT NULL COMMENT '通用状态 1,enabled,可用;2,disabled,不可用;3,deleted,已删除;',
  `roleId` bigint(20) unsigned DEFAULT NULL COMMENT '角色id',
  `menuId` bigint(20) unsigned DEFAULT NULL COMMENT '菜单id',
  `createdTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updatedTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8 COMMENT='系统角色对应菜单表';

-- ----------------------------
-- Records of tb_admin_role_menu
-- ----------------------------
INSERT INTO `tb_admin_role_menu` VALUES ('6', '1', '6', '1', '2017-03-02 16:55:04', '2017-03-02 16:55:04');
INSERT INTO `tb_admin_role_menu` VALUES ('7', '1', '6', '2', '2017-03-02 16:55:04', '2017-03-02 16:55:04');
INSERT INTO `tb_admin_role_menu` VALUES ('8', '1', '6', '3', '2017-03-02 16:55:04', '2017-03-02 16:55:04');
INSERT INTO `tb_admin_role_menu` VALUES ('16', '1', '6', '4', '2017-03-02 16:55:04', '2017-03-02 16:55:04');

-- ----------------------------
-- Table structure for `tb_admin_user`
-- ----------------------------
DROP TABLE IF EXISTS `tb_admin_user`;
CREATE TABLE `tb_admin_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一性编号',
  `type` tinyint(3) unsigned DEFAULT NULL COMMENT '管理员类型 1,administrator,超级管理员;2,manager,普通管理员;',
  `status` tinyint(3) unsigned DEFAULT NULL COMMENT '通用状态 1,enabled,可用;2,disabled,不可用;3,deleted,已删除;',
  `username` varchar(32) DEFAULT NULL COMMENT '用户名',
  `password` varchar(32) DEFAULT NULL COMMENT '登录密码',
  `name` varchar(32) DEFAULT NULL COMMENT '用户名称',
  `phone` char(11) DEFAULT NULL COMMENT '手机号码',
  `email` varchar(64) DEFAULT NULL COMMENT '电子邮箱',
  `lastLoginTime` datetime DEFAULT NULL COMMENT '上次登录时间',
  `createdTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updatedTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='管理员基本信息表';

-- ----------------------------
-- Records of tb_admin_user
-- ----------------------------
INSERT INTO `tb_admin_user` VALUES ('3', '1', '1', 'admin', 'e10adc3949ba59abbe56e057f20f883e', '超级管理员', '15858585858', 'admin@163.com', '2017-03-02 17:31:24', '2017-03-01 17:56:23', '2017-03-02 17:31:24');

-- ----------------------------
-- Table structure for `tb_admin_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `tb_admin_user_role`;
CREATE TABLE `tb_admin_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '唯一性编号',
  `status` tinyint(3) unsigned DEFAULT NULL COMMENT '通用状态 1,enabled,可用;2,disabled,不可用;3,deleted,已删除;',
  `userId` bigint(20) DEFAULT NULL COMMENT '用户id',
  `roleId` bigint(20) unsigned DEFAULT NULL COMMENT '角色id',
  `createdTime` datetime DEFAULT NULL COMMENT '创建时间',
  `updatedTime` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COMMENT='系统用户角色关系表';

-- ----------------------------
-- Records of tb_admin_user_role
-- ----------------------------
INSERT INTO `tb_admin_user_role` VALUES ('2', '1', '3', '6', '2017-03-02 15:17:59', '2017-03-02 15:17:59');
