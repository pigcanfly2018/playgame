# Host: 103.232.87.161  (Version 5.6.29-log)
# Date: 2018-03-22 15:03:36
# Generator: MySQL-Front 6.0  (Build 2.20)


#
# Structure for table "game_transfer_data"
#

CREATE TABLE `game_transfer_data` (
  `data_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_no` varchar(50) DEFAULT NULL COMMENT '转账单号',
  `game_name` varchar(50) DEFAULT NULL COMMENT '玩家账号',
  `game_pwd` varchar(80) DEFAULT NULL COMMENT '玩家密码',
  `credit` decimal(10,2) DEFAULT NULL COMMENT '转账额度',
  `direct` varchar(20) DEFAULT NULL COMMENT '转账方向',
  `plat` varchar(20) DEFAULT NULL COMMENT '操作平台',
  `create_date` datetime DEFAULT NULL COMMENT '操作时间',
  `flag` tinyint(4) DEFAULT NULL COMMENT '账单状态',
  `finish_date` datetime DEFAULT NULL COMMENT '完成时间',
  `product` varchar(20) DEFAULT NULL COMMENT '产品线',
  PRIMARY KEY (`data_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

#
# Data for table "game_transfer_data"
#


#
# Structure for table "sec_key"
#

CREATE TABLE `sec_key` (
  `sec_key_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sec_key` varchar(200) NOT NULL,
  `ip` varchar(60) NOT NULL,
  `msg` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`sec_key_id`),
  UNIQUE KEY `UNIQUE_sec_key` (`ip`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;

#
# Data for table "sec_key"
#

INSERT INTO `sec_key` VALUES (1,'kjsdad','192.168.66.120','lance-pc'),(2,'882ebe9b-9c4d-487a-8f78-b78e7ec4e21f','127.0.0.1','lance-pc01'),(7,'ioweownwehewhe','b123b123','Test');

#
# Structure for table "ws_config"
#

CREATE TABLE `ws_config` (
  `config_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `config_name` varchar(50) NOT NULL COMMENT '配置项',
  `config_value` varchar(100) NOT NULL COMMENT '配置值',
  `product` varchar(20) NOT NULL COMMENT '产品',
  `config_desc` varchar(300) DEFAULT NULL COMMENT '参数描述',
  PRIMARY KEY (`config_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

#
# Data for table "ws_config"
#

INSERT INTO `ws_config` VALUES (1,'AG_game','开','8da',NULL),(2,'BBIN_game','开','8da',NULL),(3,'PT_game','开','8da',NULL),(4,'KG_game','开','8da',NULL),(5,'MG_game','开','8da',NULL);

#
# Structure for table "ws_user"
#

CREATE TABLE `ws_user` (
  `ws_user_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'asas',
  `user_name` varchar(30) NOT NULL COMMENT 'asa',
  `user_pwd` varchar(60) NOT NULL,
  `role` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`ws_user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

#
# Data for table "ws_user"
#

INSERT INTO `ws_user` VALUES (1,'8da_mobile','753951','8da_moblie'),(8,'bojin_web','753951','bojin_web');
