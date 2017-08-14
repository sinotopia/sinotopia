--
--    Copyright 2010-2016 the original author or authors.
--
--    Licensed under the Apache License, Version 2.0 (the "License");
--    you may not use this file except in compliance with the License.
--    You may obtain a copy of the License at
--
--       http://www.apache.org/licenses/LICENSE-2.0
--
--    Unless required by applicable law or agreed to in writing, software
--    distributed under the License is distributed on an "AS IS" BASIS,
--    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
--    See the License for the specific language governing permissions and
--    limitations under the License.
--

-- // initialize upms_system data
-- Migration SQL that makes the change goes here.

-- ----------------------------
-- Records of upms_system
-- ----------------------------
INSERT INTO `upms_system` VALUES ('1', 'zmdi zmdi-shield-security', '/resources/zheng-admin/images/zheng-upms.png', '#29A176', 'http://upms.zhangshuzheng.cn:1111', '1', 'zheng-upms-server', '权限管理系统', '用户权限管理系统（RBAC细粒度用户权限、统一后台、单点登录、会话管理）', '1', '1');
INSERT INTO `upms_system` VALUES ('2', 'zmdi zmdi-wikipedia', '/resources/zheng-admin/images/zheng-cms.png', '#455EC5', 'http://cms.zhangshuzheng.cn:2222', '1', 'zheng-cms-admin', '内容管理系统', '内容管理系统（门户、博客、论坛、问答等）', '2', '2');
INSERT INTO `upms_system` VALUES ('3', 'zmdi zmdi-paypal-alt', '/resources/zheng-admin/images/zheng-pay.png', '#F06292', 'http://pay.zhangshuzheng.cn:3331', '1', 'zheng-pay-admin', '支付管理系统', '支付管理系统', '3', '3');
INSERT INTO `upms_system` VALUES ('4', 'zmdi zmdi-account', '/resources/zheng-admin/images/zheng-ucenter.png', '#6539B4', 'http://ucenter.zhangshuzheng.cn:4441', '1', 'zheng-ucenter-home', '用户管理系统', '用户管理系统', '4', '4');
INSERT INTO `upms_system` VALUES ('5', 'zmdi zmdi-cloud', '/resources/zheng-admin/images/zheng-oss.png', '#0B8DE5', 'http://oss.zhangshuzheng.cn:7771', '1', 'zheng-oss-web', '存储管理系统', '存储管理系统', '5', '5');


-- //@UNDO
-- SQL to undo the change goes here.


