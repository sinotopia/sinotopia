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

-- // initialize upms_organization data
-- Migration SQL that makes the change goes here.

-- ----------------------------
-- Records of upms_organization
-- ----------------------------
INSERT INTO `upms_organization` VALUES ('1', null, '总部', '北京总部', '1');
INSERT INTO `upms_organization` VALUES ('4', null, '河北分部', '河北石家庄', '1488122466236');
INSERT INTO `upms_organization` VALUES ('5', null, '河南分部', '河南郑州', '1488122480265');
INSERT INTO `upms_organization` VALUES ('6', null, '湖北分部', '湖北武汉', '1488122493265');
INSERT INTO `upms_organization` VALUES ('7', null, '湖南分部', '湖南长沙', '1488122502752');

-- //@UNDO
-- SQL to undo the change goes here.


