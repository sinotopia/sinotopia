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

-- // creata table ucenter_user_oauth
-- Migration SQL that makes the change goes here.

-- ----------------------------
-- Table structure for ucenter_user_oauth
-- ----------------------------

CREATE TABLE `ucenter_user_oauth` (
  `user_oauth_id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '编号',
  `user_id` int(10) unsigned NOT NULL COMMENT '帐号编号',
  `oauth_id` int(10) unsigned NOT NULL COMMENT '认证方式编号',
  `open_id` varbinary(50) NOT NULL COMMENT '第三方ID',
  `status` tinyint(4) unsigned DEFAULT NULL COMMENT '绑定状态(0:解绑,1:绑定)',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`user_oauth_id`),
  KEY `FK_Reference_42` (`user_id`),
  KEY `FK_Reference_43` (`oauth_id`),
  CONSTRAINT `FK_Reference_42` FOREIGN KEY (`user_id`) REFERENCES `ucenter_user` (`user_id`),
  CONSTRAINT `FK_Reference_43` FOREIGN KEY (`oauth_id`) REFERENCES `ucenter_oauth` (`oauth_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户认证方式表';


-- //@UNDO
-- SQL to undo the change goes here.
DROP TABLE IF EXISTS `ucenter_user_oauth`;

