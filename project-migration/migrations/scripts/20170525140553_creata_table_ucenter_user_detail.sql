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

-- // creata table ucenter_user_detail
-- Migration SQL that makes the change goes here.

-- ----------------------------
-- Table structure for ucenter_user_details
-- ----------------------------

CREATE TABLE `ucenter_user_details` (
  `user_id` int(10) unsigned NOT NULL COMMENT '编号',
  `signature` varchar(300) DEFAULT NULL COMMENT '个性签名',
  `real_name` varchar(20) DEFAULT NULL COMMENT '真实姓名',
  `birthday` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '出生日期',
  `question` varchar(100) DEFAULT NULL COMMENT '帐号安全问题',
  `answer` varchar(100) DEFAULT NULL COMMENT '帐号安全答案',
  PRIMARY KEY (`user_id`),
  CONSTRAINT `FK_Reference_41` FOREIGN KEY (`user_id`) REFERENCES `ucenter_user` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户详情表';

-- //@UNDO
-- SQL to undo the change goes here.

DROP TABLE IF EXISTS `ucenter_user_details`;
