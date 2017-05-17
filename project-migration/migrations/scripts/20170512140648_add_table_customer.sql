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

-- // add table customer;
-- Migration SQL that makes the change goes here.

CREATE TABLE `customer` (
`CUSTOMER_ID`                     bigint(20) NOT NULL ,
`CUSTOMER_ANONYMOUS`  bit(1) NULL DEFAULT NULL ,
`BILLING_STREET_ADDRESS`    varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`BILLING_CITY`                         varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`BILLING_COMPANY`               varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`BILLING_FIRST_NAME`            varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`BILLING_LAST_NAME`             varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`BILLING_POSTCODE`               varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`BILLING_STATE`                      varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`BILLING_TELEPHONE`             varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`CUSTOMER_COMPANY`         varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`CUSTOMER_DOB`  datetime   NULL DEFAULT NULL ,
`DELIVERY_STREET_ADDRESS`  varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`DELIVERY_CITY`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`DELIVERY_COMPANY`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`DELIVERY_FIRST_NAME`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`DELIVERY_LAST_NAME`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`DELIVERY_POSTCODE`  varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`DELIVERY_STATE`  varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`DELIVERY_TELEPHONE`  varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`CUSTOMER_EMAIL_ADDRESS`  varchar(96) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL ,
`CUSTOMER_GENDER`  varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`CUSTOMER_NICK`  varchar(96) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`CUSTOMER_PASSWORD`  varchar(60) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL ,
`BILLING_COUNTRY_ID`  int(11) NOT NULL ,
`BILLING_ZONE_ID`  bigint(20) NULL DEFAULT NULL ,
`LANGUAGE_ID`  int(11) NOT NULL ,
`DELIVERY_COUNTRY_ID`  int(11) NULL DEFAULT NULL ,
`DELIVERY_ZONE_ID`  bigint(20) NULL DEFAULT NULL ,
`MERCHANT_ID`  int(11) NOT NULL ,
`CREATE_USER_ID`                     bigint(20) NOT NULL ,
`CREATE_DATETIME`                  datetime  NULL ,
`LAST_MODIFIED_USER_ID`                     bigint(20)  NULL ,
`LAST_MODIFIED_DATETIME`                  datetime  NULL ,
PRIMARY KEY (`CUSTOMER_ID`)
/*
,
FOREIGN KEY (`DELIVERY_ZONE_ID`) REFERENCES `zone` (`ZONE_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
FOREIGN KEY (`BILLING_COUNTRY_ID`) REFERENCES `country` (`COUNTRY_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
FOREIGN KEY (`BILLING_ZONE_ID`) REFERENCES `zone` (`ZONE_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
FOREIGN KEY (`LANGUAGE_ID`) REFERENCES `language` (`LANGUAGE_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
FOREIGN KEY (`MERCHANT_ID`) REFERENCES `merchant_store` (`MERCHANT_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
FOREIGN KEY (`DELIVERY_COUNTRY_ID`) REFERENCES `country` (`COUNTRY_ID`) ON DELETE RESTRICT ON UPDATE RESTRICT,
INDEX `FK_3j13fd0w8fcubko0fdv53w18j` (`BILLING_COUNTRY_ID`) USING BTREE ,
INDEX `FK_72ork8lgtlvgwi0iwf0khdhv7` (`BILLING_ZONE_ID`) USING BTREE ,
INDEX `FK_eyy3eqwvkp4nudh4r02afghhs` (`LANGUAGE_ID`) USING BTREE ,
INDEX `FK_k7gt46oima5w2h4wq84hhg19c` (`DELIVERY_COUNTRY_ID`) USING BTREE ,
INDEX `FK_2atbo3aje6mi15gk8p7a5qvxb` (`DELIVERY_ZONE_ID`) USING BTREE ,
INDEX `FK_j00dj3m9tcac4sua8ajqln77a` (`MERCHANT_ID`) USING BTREE 
*/
)
ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
ROW_FORMAT=DYNAMIC
;

-- //@UNDO
-- SQL to undo the change goes here.
drop table if exists `customer`;
