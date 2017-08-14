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

-- // creata table pay_in_order
-- Migration SQL that makes the change goes here.

CREATE TABLE `pay_in_order` (
  `pay_in_order_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `pay_vendor_id` int(10) DEFAULT NULL,
  `pay_mch_id` int(10) DEFAULT NULL,
  `amount` decimal(10,0) NOT NULL,
  `status` tinyint(4) NOT NULL,
  `ctime` bigint(20) unsigned NOT NULL,
  PRIMARY KEY (`pay_in_order_id`),
  KEY `FK_Reference_32` (`pay_vendor_id`),
  KEY `FK_Reference_38` (`pay_mch_id`),
  CONSTRAINT `FK_Reference_32` FOREIGN KEY (`pay_vendor_id`) REFERENCES `pay_vendor` (`pay_vendor_id`),
  CONSTRAINT `FK_Reference_38` FOREIGN KEY (`pay_mch_id`) REFERENCES `pay_mch` (`pay_mch_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='收入订单表';

-- //@UNDO
-- SQL to undo the change goes here.


DROP TABLE IF EXISTS `pay_in_order`;