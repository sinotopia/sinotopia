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

-- // add table category
-- Migration SQL that makes the change goes here.

CREATE TABLE `category`
(
  `category_id` INT(11)  NOT NULL AUTO_INCREMENT,
  `category_name`         VARCHAR(255),
  `create_user_id`            bigint(20) not null ,
  `create_datetime`                  datetime  null ,
  `last_modified_user_id`                     bigint(20)  null ,
  `last_modified_datetime`                  datetime  null ,
  PRIMARY KEY (`category_id`)
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
ROW_FORMAT=DYNAMIC
;

-- //@UNDO
-- SQL to undo the change goes here.
drop table if exists `category`;