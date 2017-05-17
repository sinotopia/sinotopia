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

-- // initialize cms_tag data
-- Migration SQL that makes the change goes here.



-- ----------------------------
-- Records of cms_tag
-- ----------------------------
INSERT INTO `cms_tag` VALUES ('1', 'JAVA', 'java标签', '', '1', 'java', '1', '1489585694864', '1489585694864');
INSERT INTO `cms_tag` VALUES ('2', 'Android', 'android标签', '', '1', 'android', '1', '1489585720382', '1489585720382');
INSERT INTO `cms_tag` VALUES ('3', 'zheng', 'zheng标签', '', '2', 'zheng', '1', '1489585815042', '1489585815042');
INSERT INTO `cms_tag` VALUES ('4', '谈恋爱', '谈恋爱标签', '', '1', 'love', '2', '1489585815043', '1489585815043');
INSERT INTO `cms_tag` VALUES ('5', 'java', 'java标签', '', '1', 'java', '3', '1489585815044', '1489585815044');

-- //@UNDO
-- SQL to undo the change goes here.


