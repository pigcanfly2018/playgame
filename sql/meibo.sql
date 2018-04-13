# Host: 103.232.87.161  (Version 5.6.29-log)
# Date: 2018-03-22 15:14:47
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
  `ref_order_no` varchar(50) DEFAULT NULL COMMENT '订单号',
  PRIMARY KEY (`data_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5711 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_ad"
#

CREATE TABLE `mb_ad` (
  `ad_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ad_title` varchar(100) DEFAULT NULL,
  `ad_describe` varchar(200) DEFAULT NULL,
  `pic_url` varchar(500) DEFAULT NULL,
  `target_url` varchar(500) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `create_user` varchar(30) DEFAULT NULL,
  `available` tinyint(4) DEFAULT NULL,
  `priority` int(11) DEFAULT NULL,
  `mobile_flag` tinyint(1) DEFAULT '0' COMMENT '0表示网页版,1表示手机版',
  PRIMARY KEY (`ad_id`)
) ENGINE=MyISAM AUTO_INCREMENT=134 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_ag_bet_detail"
#

CREATE TABLE `mb_ag_bet_detail` (
  `playerName` varchar(32) DEFAULT NULL,
  `validBetAmount` decimal(10,2) DEFAULT NULL,
  `dataType` varchar(50) DEFAULT NULL,
  `billNo` varchar(25) NOT NULL DEFAULT '',
  `agentCode` varchar(25) DEFAULT NULL,
  `gameCode` varchar(30) DEFAULT NULL,
  `netAmount` decimal(10,2) DEFAULT NULL,
  `betTime` datetime DEFAULT NULL,
  `gameType` varchar(20) DEFAULT NULL,
  `betAmount` decimal(10,2) DEFAULT NULL,
  `flag` tinyint(1) DEFAULT NULL,
  `playType` varchar(10) DEFAULT NULL,
  `currency` varchar(10) DEFAULT NULL,
  `tableCode` varchar(10) DEFAULT NULL,
  `loginIP` varchar(20) DEFAULT NULL,
  `recalcuTime` datetime DEFAULT NULL,
  `platformType` varchar(10) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `round` varchar(20) DEFAULT NULL,
  `slottype` int(11) unsigned DEFAULT NULL,
  `mainbillno` varchar(20) DEFAULT NULL,
  `beforeCredit` decimal(10,2) unsigned DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `deviceType` tinyint(1) unsigned DEFAULT NULL,
  `srcXmlDate` varchar(8) DEFAULT NULL,
  `settleStatus` tinyint(1) NOT NULL DEFAULT '0',
  `settleBillNo` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`billNo`),
  KEY `settleBillNo` (`settleBillNo`),
  KEY `gameType` (`gameType`),
  KEY `playerName` (`playerName`),
  KEY `betTime` (`betTime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Structure for table "mb_ag_betdata"
#

CREATE TABLE `mb_ag_betdata` (
  `dataId` bigint(11) NOT NULL AUTO_INCREMENT,
  `playerName` varchar(50) DEFAULT NULL,
  `validBetAmount` decimal(10,2) DEFAULT NULL,
  `dataType` varchar(50) DEFAULT NULL,
  `billNo` varchar(25) DEFAULT NULL,
  `agentCode` varchar(25) DEFAULT NULL,
  `gameCode` varchar(30) DEFAULT NULL,
  `netAmount` decimal(10,2) DEFAULT NULL,
  `betTime` datetime DEFAULT NULL,
  `gameType` varchar(20) DEFAULT NULL,
  `betAmount` decimal(10,2) DEFAULT NULL,
  `flag` tinyint(1) DEFAULT NULL,
  `playType` varchar(10) DEFAULT NULL,
  `currency` varchar(10) DEFAULT NULL,
  `tableCode` varchar(10) DEFAULT NULL,
  `loginIP` varchar(20) DEFAULT NULL,
  `recalcuTime` datetime DEFAULT NULL,
  `platformType` varchar(10) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `round` varchar(20) DEFAULT NULL,
  `slottype` int(11) unsigned DEFAULT NULL,
  `mainbillno` varchar(20) DEFAULT NULL,
  `beforeCredit` decimal(10,2) unsigned DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `deviceType` tinyint(1) unsigned DEFAULT NULL,
  `srcXmlDate` varchar(8) DEFAULT NULL,
  `settleStatus` tinyint(1) NOT NULL DEFAULT '0',
  `settleBillNo` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`dataId`),
  UNIQUE KEY `billNo` (`billNo`),
  KEY `settleBillNo` (`settleBillNo`)
) ENGINE=InnoDB AUTO_INCREMENT=225098 DEFAULT CHARSET=utf8mb4;

#
# Structure for table "mb_ag_betreward_bill"
#

CREATE TABLE `mb_ag_betreward_bill` (
  `settleBillNo` varchar(20) NOT NULL DEFAULT '',
  `playerName` varchar(40) DEFAULT NULL,
  `userLevel` tinyint(4) DEFAULT NULL,
  `gameCat` varchar(20) DEFAULT NULL,
  `beginTime` datetime DEFAULT NULL,
  `endTime` datetime DEFAULT NULL,
  `betAmount` decimal(12,2) DEFAULT NULL,
  `rewardPercent` smallint(6) DEFAULT NULL,
  `rewardAmount` decimal(12,2) DEFAULT NULL,
  `auditStatus` tinyint(4) DEFAULT NULL,
  `memo` varchar(200) DEFAULT NULL,
  `auditor` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`settleBillNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Structure for table "mb_ag_game"
#

CREATE TABLE `mb_ag_game` (
  `ag_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `game_name` varchar(200) DEFAULT NULL COMMENT '游戏名字',
  `game_code` varchar(100) DEFAULT NULL COMMENT '游戏代码',
  `hot` tinyint(3) DEFAULT '0' COMMENT '是否热',
  `enable` tinyint(4) DEFAULT '1' COMMENT '是否可用',
  `Image_File_Name` varchar(50) DEFAULT NULL,
  `game_type` varchar(20) DEFAULT NULL,
  `is_new` tinyint(3) DEFAULT '0' COMMENT '1是新游戏',
  `sortpriority` int(5) DEFAULT NULL COMMENT '排序优先级,数值越小越靠前，1在最前面，null在最后面',
  `is_html5` tinyint(3) DEFAULT '0' COMMENT '是否是html5',
  PRIMARY KEY (`ag_id`)
) ENGINE=InnoDB AUTO_INCREMENT=135 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_agent"
#

CREATE TABLE `mb_agent` (
  `agent_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `login_name` varchar(30) NOT NULL COMMENT '用户名',
  `login_pwd` varchar(100) DEFAULT NULL COMMENT '登录密码',
  `flag` tinyint(1) DEFAULT NULL COMMENT '状态. 1待审核 2审核不通过 3 正常 4已冻结',
  `credit` decimal(10,2) DEFAULT '0.00' COMMENT '当前额度',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '上级代理，默认为0',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间、注册时间',
  `create_user` varchar(30) DEFAULT NULL COMMENT '创建人',
  `audit_date` datetime DEFAULT NULL COMMENT '审核时间',
  `audit_user` varchar(30) DEFAULT NULL COMMENT '审核人',
  `audit_msg` varchar(1000) DEFAULT NULL COMMENT '审核日志',
  `lock_date` datetime DEFAULT NULL COMMENT '冻结日期',
  `lock_user` varchar(30) DEFAULT NULL COMMENT '冻结人员',
  `lock_msg` varchar(1000) DEFAULT NULL COMMENT '冻结日志',
  `last_login_date` datetime DEFAULT NULL COMMENT '最后登录日期',
  `reg_ip` varchar(100) DEFAULT NULL COMMENT '注册IP',
  `last_login_ip` varchar(100) DEFAULT NULL COMMENT '最后登录ip',
  `reg_code` varchar(100) NOT NULL COMMENT '代理注册码/唯一',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `qq` varchar(30) DEFAULT NULL COMMENT 'QQ',
  `email` varchar(100) DEFAULT NULL COMMENT '电子邮件',
  `phone` varchar(30) DEFAULT NULL COMMENT '电话',
  `remarks` varchar(1000) DEFAULT NULL COMMENT '备注',
  `bank_name` varchar(100) DEFAULT NULL COMMENT '银行名字',
  `account_type` varchar(100) DEFAULT NULL COMMENT '账户类型',
  `bank_dot` varchar(500) DEFAULT NULL COMMENT '网点',
  `account` varchar(100) DEFAULT NULL COMMENT '账号',
  `advantage` varchar(800) DEFAULT NULL,
  `share` int(10) DEFAULT '15' COMMENT '收益分成比例',
  `commission_date` varchar(8) DEFAULT NULL COMMENT '佣金最后时间(yyyyMMdd)',
  PRIMARY KEY (`agent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Structure for table "mb_agent_commission"
#

CREATE TABLE `mb_agent_commission` (
  `com_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `agent_id` bigint(20) DEFAULT NULL COMMENT '代理ID',
  `login_name` varchar(30) DEFAULT NULL COMMENT '代理用户名',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(30) DEFAULT NULL COMMENT '创建人',
  `status` tinyint(1) DEFAULT NULL COMMENT '1:待审核 2:审核不通过 3审核通过',
  `audit_user` varchar(30) DEFAULT NULL COMMENT '审核人',
  `audit_date` datetime DEFAULT NULL COMMENT '审核时间',
  `credit` decimal(10,2) DEFAULT NULL COMMENT '每月输赢统计佣金',
  `finalcredit` decimal(10,2) DEFAULT '0.00' COMMENT '每月实际佣金',
  `audit_msg` varchar(500) DEFAULT NULL COMMENT '审核日志',
  `com_month` int(11) DEFAULT NULL COMMENT '月份',
  `com_year` int(11) DEFAULT NULL COMMENT '年份',
  `ag_bet` decimal(15,2) DEFAULT NULL COMMENT 'AG投注量',
  `bbin_bet` decimal(15,2) DEFAULT NULL COMMENT 'BBIN投注量',
  `pt_bet` decimal(15,2) DEFAULT NULL COMMENT 'PT投注量',
  `total_bet` decimal(15,2) DEFAULT NULL COMMENT '总投注量',
  `share` int(10) DEFAULT NULL COMMENT '佣金比例',
  `total_deposit` decimal(12,2) DEFAULT '0.00',
  `total_withdraw` decimal(12,2) DEFAULT '0.00',
  `total_gift` decimal(12,2) DEFAULT '0.00',
  `total_cost` decimal(8,2) DEFAULT '0.00' COMMENT '推广花费',
  `remarks` varchar(200) DEFAULT NULL,
  `active` int(5) DEFAULT '0' COMMENT '活跃人数',
  `start_date` varchar(8) DEFAULT NULL COMMENT '佣金统计开始时间',
  `end_date` varchar(8) DEFAULT NULL COMMENT '佣金统计结束时间',
  PRIMARY KEY (`com_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_agent_credit_log"
#

CREATE TABLE `mb_agent_credit_log` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `log_type` varchar(30) DEFAULT NULL COMMENT '变化类型',
  `credit` decimal(10,2) DEFAULT NULL COMMENT '变化额度',
  `login_name` varchar(30) DEFAULT NULL COMMENT '用户名',
  `agent_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `create_user` varchar(30) DEFAULT NULL COMMENT '创建人',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注',
  `order_no` varchar(30) DEFAULT NULL COMMENT '单号',
  `org_credit` decimal(10,2) DEFAULT NULL COMMENT '初始额度',
  `after_credit` decimal(10,2) DEFAULT NULL COMMENT '变化后额度',
  PRIMARY KEY (`log_id`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_agent_cust_bet"
#

CREATE TABLE `mb_agent_cust_bet` (
  `bet_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bet_day` varchar(8) DEFAULT NULL COMMENT '投注量日期(格式20150303)',
  `cust_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `login_name` varchar(30) DEFAULT NULL COMMENT '客户用户名',
  `agent_id` bigint(20) DEFAULT NULL COMMENT '代理ID，无则为空',
  `plat` varchar(30) DEFAULT NULL COMMENT '平台:AG/BBIN/PT',
  `bet` decimal(10,2) DEFAULT NULL COMMENT '有效投注量',
  `create_date` datetime DEFAULT NULL COMMENT '记录创建时间',
  `create_user` varchar(30) DEFAULT NULL COMMENT '记录创建人',
  PRIMARY KEY (`bet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Structure for table "mb_agent_log"
#

CREATE TABLE `mb_agent_log` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `log_type` int(11) DEFAULT NULL COMMENT '记录类型。2登录3提款',
  `log_msg` varchar(2000) DEFAULT NULL COMMENT '记录内容',
  `ip` varchar(20) DEFAULT NULL COMMENT 'IP',
  `user_agent` varchar(400) DEFAULT NULL COMMENT '浏览器信息',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `agent_id` bigint(20) NOT NULL,
  `login_name` varchar(30) NOT NULL COMMENT '用户名',
  PRIMARY KEY (`log_id`)
) ENGINE=MyISAM AUTO_INCREMENT=47 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_agent_withdraw"
#

CREATE TABLE `mb_agent_withdraw` (
  `awithdraw_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `awit_no` varchar(30) DEFAULT NULL,
  `login_name` varchar(30) DEFAULT NULL,
  `real_name` varchar(30) DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `bank_name` varchar(30) DEFAULT NULL,
  `account_name` varchar(30) DEFAULT NULL,
  `account` varchar(30) DEFAULT NULL,
  `account_type` varchar(30) DEFAULT NULL,
  `location` varchar(100) DEFAULT NULL,
  `remarks` varchar(500) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `create_user` varchar(30) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `agent_id` bigint(20) DEFAULT NULL,
  `pay_date` datetime DEFAULT NULL,
  `locked` tinyint(1) DEFAULT '0',
  `wit_cnt` int(11) DEFAULT NULL,
  PRIMARY KEY (`awithdraw_id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_agent_withdraw_log"
#

CREATE TABLE `mb_agent_withdraw_log` (
  `alog_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pre_status` int(11) DEFAULT NULL,
  `after_status` int(11) DEFAULT NULL,
  `awithdraw_id` bigint(20) DEFAULT NULL,
  `remarks` varchar(500) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `create_user` varchar(30) DEFAULT NULL,
  `awit_no` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`alog_id`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_bank"
#

CREATE TABLE `mb_bank` (
  `bank_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bank_name` varchar(30) DEFAULT NULL,
  `account` varchar(30) DEFAULT NULL,
  `account_name` varchar(30) DEFAULT NULL,
  `bank_dot` varchar(50) DEFAULT NULL,
  `cust_level` int(11) DEFAULT NULL,
  `remarks` varchar(500) DEFAULT NULL,
  `create_user` varchar(30) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `available` tinyint(1) DEFAULT '1',
  `specifiedname` varchar(100) DEFAULT NULL COMMENT '针对特定用户的用户名',
  PRIMARY KEY (`bank_id`)
) ENGINE=MyISAM AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_bbin_bet_detail"
#

CREATE TABLE `mb_bbin_bet_detail` (
  `UserName` varchar(16) DEFAULT NULL,
  `WagersID` varchar(16) NOT NULL,
  `WagersDate` datetime DEFAULT NULL,
  `SerialID` bigint(10) DEFAULT NULL,
  `RoundNo` varchar(16) DEFAULT NULL,
  `GameType` varchar(12) DEFAULT NULL,
  `WagerDetail` varchar(64) DEFAULT NULL,
  `GameCode` varchar(8) DEFAULT NULL,
  `Result` varchar(32) DEFAULT NULL,
  `Card` varchar(32) DEFAULT NULL,
  `BetAmount` decimal(10,2) DEFAULT NULL,
  `Origin` varchar(8) DEFAULT NULL,
  `Commissionable` decimal(10,2) DEFAULT NULL,
  `Payoff` decimal(8,2) DEFAULT NULL,
  `Currency` varchar(8) DEFAULT NULL,
  `ExchangeRate` decimal(4,2) DEFAULT NULL,
  `ResultType` varchar(4) DEFAULT NULL,
  `GameKind` varchar(8) DEFAULT NULL,
  `RoundDate` varchar(8) DEFAULT NULL,
  `settleStatus` tinyint(1) NOT NULL DEFAULT '0',
  `settleBillNo` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`WagersID`),
  KEY `settleBillNo` (`settleBillNo`),
  KEY `UserName` (`UserName`),
  KEY `GameKind` (`GameKind`),
  KEY `WagersDate` (`WagersDate`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Structure for table "mb_bbin_betdata"
#

CREATE TABLE `mb_bbin_betdata` (
  `dataId` int(11) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(50) DEFAULT NULL,
  `wagersid` varchar(20) DEFAULT NULL,
  `wagersdate` datetime DEFAULT NULL,
  `gametype` varchar(10) DEFAULT NULL,
  `result` varchar(50) DEFAULT NULL,
  `betamount` decimal(10,2) DEFAULT NULL,
  `payoff` decimal(10,2) DEFAULT NULL,
  `currency` varchar(15) DEFAULT NULL,
  `commissionable` decimal(10,2) DEFAULT NULL,
  `serialid` varchar(10) DEFAULT NULL,
  `roundno` varchar(20) DEFAULT NULL,
  `gamecode` varchar(20) DEFAULT NULL,
  `resulttype` varchar(5) DEFAULT NULL,
  `card` varchar(60) DEFAULT NULL,
  `commission` decimal(10,2) DEFAULT NULL COMMENT '彩票退水',
  `ispaid` varchar(5) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`dataId`),
  UNIQUE KEY `wagersid` (`wagersid`)
) ENGINE=MyISAM AUTO_INCREMENT=744 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_bbin_betreward_bill"
#

CREATE TABLE `mb_bbin_betreward_bill` (
  `settleBillNo` varchar(20) NOT NULL DEFAULT '',
  `playerName` varchar(40) DEFAULT NULL,
  `userLevel` tinyint(4) DEFAULT NULL,
  `gameCat` varchar(20) DEFAULT NULL,
  `beginTime` datetime DEFAULT NULL,
  `endTime` datetime DEFAULT NULL,
  `betAmount` decimal(12,2) DEFAULT NULL,
  `rewardPercent` decimal(4,2) DEFAULT NULL,
  `rewardAmount` decimal(12,2) DEFAULT NULL,
  `auditStatus` tinyint(4) DEFAULT NULL,
  `memo` varchar(200) DEFAULT NULL,
  `auditor` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`settleBillNo`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Structure for table "mb_bbin_game"
#

CREATE TABLE `mb_bbin_game` (
  `game_id` bigint(20) DEFAULT NULL,
  `game_name` varchar(1800) DEFAULT NULL,
  `game_code` varchar(900) DEFAULT NULL,
  `is_hot` tinyint(3) DEFAULT NULL,
  `is_new` tinyint(3) DEFAULT NULL,
  `enable` tinyint(2) DEFAULT NULL,
  `Image_File_Name` varchar(450) DEFAULT NULL,
  `game_type` tinyint(5) DEFAULT NULL,
  `sortpriority` tinyint(10) DEFAULT NULL,
  `enter_directly` tinyint(2) DEFAULT '1' COMMENT '是否可以直接进入'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Structure for table "mb_betdata_gather"
#

CREATE TABLE `mb_betdata_gather` (
  `dataId` bigint(20) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(50) DEFAULT NULL,
  `validBetAmount` decimal(10,2) DEFAULT NULL,
  `platform` varchar(10) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `agent_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`dataId`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_bethistory"
#

CREATE TABLE `mb_bethistory` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `filename` varchar(30) DEFAULT NULL,
  `modifytime` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`Id`),
  KEY `filename` (`filename`)
) ENGINE=InnoDB AUTO_INCREMENT=181 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_bettime"
#

CREATE TABLE `mb_bettime` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `platform` varchar(25) DEFAULT NULL,
  `date` varchar(30) DEFAULT NULL,
  `time` varchar(30) DEFAULT NULL,
  `RowId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=MyISAM AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_billboard"
#

CREATE TABLE `mb_billboard` (
  `board_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(30) DEFAULT NULL,
  `bet_amount` decimal(10,2) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `flag` tinyint(3) DEFAULT '0' COMMENT '0显示在前台上,后台提交的时候把原来所有数据该字段设置成1',
  PRIMARY KEY (`board_id`)
) ENGINE=InnoDB AUTO_INCREMENT=202 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_cash_gift"
#

CREATE TABLE `mb_cash_gift` (
  `gift_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `gift_no` varchar(30) DEFAULT NULL,
  `kh_date` datetime DEFAULT NULL,
  `login_name` varchar(30) DEFAULT NULL,
  `real_name` varchar(50) DEFAULT NULL,
  `cust_level` int(11) DEFAULT NULL,
  `gift_type` varchar(30) DEFAULT NULL,
  `gift_code` varchar(30) DEFAULT NULL,
  `deposit_credit` decimal(10,2) DEFAULT NULL,
  `net_credit` decimal(10,2) DEFAULT NULL,
  `valid_credit` decimal(10,2) DEFAULT NULL,
  `rate` float DEFAULT NULL,
  `payout` decimal(10,2) DEFAULT NULL,
  `cs_date` datetime DEFAULT NULL,
  `remarks` varchar(300) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `create_user` varchar(30) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `audit_date` datetime DEFAULT NULL,
  `audit_user` varchar(30) DEFAULT NULL,
  `audit_msg` varchar(300) DEFAULT NULL,
  `cust_id` bigint(20) DEFAULT NULL,
  `transferflag` int(3) DEFAULT '0' COMMENT '0默认,有转账限制的初始1,完成2',
  PRIMARY KEY (`gift_id`)
) ENGINE=MyISAM AUTO_INCREMENT=11818 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_config"
#

CREATE TABLE `mb_config` (
  `config_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `config_name` varchar(30) DEFAULT NULL,
  `config_value` varchar(200) DEFAULT NULL,
  `config_describe` varchar(400) DEFAULT NULL,
  `product` varchar(50) DEFAULT NULL,
  `config_level` varchar(20) DEFAULT NULL COMMENT '对应的等级',
  `maxlimit` int(22) DEFAULT '0',
  `sortpriority` varchar(10) DEFAULT NULL,
  `para` int(3) DEFAULT NULL,
  PRIMARY KEY (`config_id`)
) ENGINE=MyISAM AUTO_INCREMENT=87 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_credit_fix"
#

CREATE TABLE `mb_credit_fix` (
  `fix_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `fix_no` varchar(30) DEFAULT NULL COMMENT '额度修正',
  `credit` decimal(10,2) DEFAULT NULL COMMENT '修正的额度',
  `cust_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `login_name` varchar(30) DEFAULT NULL COMMENT '用户名',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `create_user` varchar(30) DEFAULT NULL COMMENT '创建人',
  `status` int(11) DEFAULT NULL COMMENT '状态',
  `remarks` varchar(200) DEFAULT NULL COMMENT '变更原因',
  `audit_date` datetime DEFAULT NULL COMMENT '审核日期',
  `audit_user` varchar(30) DEFAULT NULL COMMENT '审核人',
  `audit_msg` varchar(200) DEFAULT NULL COMMENT '审核意见',
  PRIMARY KEY (`fix_id`)
) ENGINE=MyISAM AUTO_INCREMENT=39 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_credit_log"
#

CREATE TABLE `mb_credit_log` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `log_type` varchar(30) DEFAULT NULL COMMENT '变化类型',
  `credit` decimal(10,2) DEFAULT NULL COMMENT '变化额度',
  `login_name` varchar(30) DEFAULT NULL COMMENT '用户名',
  `cust_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `create_user` varchar(30) DEFAULT NULL COMMENT '创建人',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注',
  `order_no` varchar(40) DEFAULT NULL COMMENT '单号',
  `org_credit` decimal(10,2) DEFAULT NULL COMMENT '初始额度',
  `after_credit` decimal(10,2) DEFAULT NULL COMMENT '变化后额度',
  `parent_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`log_id`)
) ENGINE=MyISAM AUTO_INCREMENT=65776 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_cus_phonerecord"
#

CREATE TABLE `mb_cus_phonerecord` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(50) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `create_user` varchar(30) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_cust_log"
#

CREATE TABLE `mb_cust_log` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `log_type` int(11) DEFAULT NULL COMMENT '记录类型',
  `log_msg` varchar(2000) DEFAULT NULL COMMENT '记录内容',
  `ip` varchar(20) DEFAULT NULL COMMENT 'IP',
  `user_agent` varchar(400) DEFAULT NULL COMMENT '浏览器信息',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `cust_id` bigint(20) NOT NULL,
  `login_name` varchar(30) NOT NULL COMMENT '用户名',
  PRIMARY KEY (`log_id`)
) ENGINE=MyISAM AUTO_INCREMENT=35786 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_custform"
#

CREATE TABLE `mb_custform` (
  `form_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cust_id` bigint(20) DEFAULT NULL,
  `login_name` varchar(30) DEFAULT NULL COMMENT '游戏登录名',
  `real_name` varchar(30) DEFAULT NULL COMMENT '真实姓名',
  `org_real_name` varchar(30) DEFAULT NULL COMMENT '原真实姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '电话',
  `org_phone` varchar(20) DEFAULT NULL COMMENT '原电话',
  `email` varchar(50) DEFAULT NULL COMMENT '邮件',
  `org_email` varchar(50) DEFAULT NULL COMMENT '原邮件',
  `qq` varchar(20) DEFAULT NULL COMMENT 'QQ',
  `org_qq` varchar(20) DEFAULT NULL COMMENT '原QQ',
  `bank_name` varchar(30) DEFAULT NULL COMMENT '银行名称',
  `org_bank_name` varchar(30) DEFAULT NULL COMMENT '原银行名称',
  `account` varchar(30) DEFAULT NULL COMMENT '账户',
  `org_account` varchar(30) DEFAULT NULL COMMENT '原账户',
  `bank_dot` varchar(50) DEFAULT NULL COMMENT '开户网点',
  `org_bank_dot` varchar(50) DEFAULT NULL COMMENT '原开户网点',
  `account_type` varchar(30) DEFAULT NULL COMMENT '账户类型',
  `org_account_type` varchar(30) DEFAULT NULL COMMENT '原账户类型',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `create_user` varchar(30) DEFAULT NULL COMMENT '创建人',
  `audit_date` datetime DEFAULT NULL COMMENT '审核日期',
  `audit_user` varchar(30) DEFAULT NULL COMMENT '审核人',
  `status` int(11) DEFAULT NULL COMMENT '状态',
  `remarks` varchar(300) DEFAULT NULL COMMENT '备注',
  `reason` varchar(300) DEFAULT NULL COMMENT '理由',
  PRIMARY KEY (`form_id`)
) ENGINE=MyISAM AUTO_INCREMENT=122 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_customer"
#

CREATE TABLE `mb_customer` (
  `cust_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `login_name` varchar(50) NOT NULL COMMENT '用户名',
  `login_pwd` varchar(70) DEFAULT NULL COMMENT '密码',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实账户',
  `phone` varchar(20) DEFAULT NULL COMMENT '电话号码',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `qq` varchar(15) DEFAULT NULL COMMENT 'QQ号码',
  `reg_ip` varchar(20) DEFAULT NULL COMMENT '注册IP',
  `create_date` datetime DEFAULT NULL COMMENT '注册时间',
  `create_user` varchar(30) DEFAULT NULL COMMENT '注册操作人',
  `login_ip` varchar(20) DEFAULT NULL COMMENT '登录IP',
  `login_date` datetime DEFAULT NULL COMMENT '登录时间',
  `login_times` int(11) DEFAULT NULL COMMENT '登录次数',
  `sb_game` varchar(30) DEFAULT NULL COMMENT '申博游戏名',
  `sb_pwd` varchar(70) DEFAULT NULL COMMENT '申博密码',
  `sb_flag` tinyint(1) DEFAULT NULL COMMENT '申博开立状态',
  `sb_actived` varchar(1) DEFAULT '0' COMMENT '申博厅是否可进',
  `ag_game` varchar(30) DEFAULT NULL COMMENT 'AG游戏名',
  `ag_pwd` varchar(70) DEFAULT NULL COMMENT 'AG密码',
  `ag_flag` tinyint(1) DEFAULT NULL COMMENT 'AG厅开立状态',
  `ag_actived` varchar(1) DEFAULT '0' COMMENT 'AG厅是否可进',
  `bbin_game` varchar(30) DEFAULT NULL COMMENT 'BBIN游戏名',
  `bbin_pwd` varchar(70) DEFAULT NULL COMMENT 'BBIN密码',
  `bbin_flag` tinyint(1) DEFAULT NULL COMMENT 'BBIN开立状态',
  `bbin_actived` varchar(1) DEFAULT '0' COMMENT 'BBIN厅是否可进',
  `credit` decimal(10,2) DEFAULT '0.00' COMMENT '本地额度',
  `flag` tinyint(1) DEFAULT NULL COMMENT '账户状态',
  `cust_level` int(11) DEFAULT NULL COMMENT '客户等级',
  `is_agent` tinyint(1) DEFAULT NULL COMMENT '是否代理',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父级ID',
  `reg_source` varchar(120) DEFAULT NULL COMMENT '注册来源',
  `agent_dm` varchar(120) DEFAULT NULL COMMENT '推广连接',
  `first_deposit_date` datetime DEFAULT NULL COMMENT '第一次存款时间',
  `remarks` varchar(200) DEFAULT NULL COMMENT '备注',
  `bank_name` varchar(30) DEFAULT NULL COMMENT '银行名字',
  `account_type` varchar(30) DEFAULT NULL COMMENT '账户类型',
  `bank_dot` varchar(100) DEFAULT NULL COMMENT '开户网点',
  `account` varchar(50) DEFAULT NULL COMMENT '银行帐号',
  `agent_mode` varchar(30) DEFAULT NULL COMMENT '代理模式',
  `s_email` tinyint(1) DEFAULT NULL COMMENT '是否发送Email',
  `score` decimal(10,2) DEFAULT '0.00' COMMENT '积分',
  `pt_game` varchar(30) DEFAULT NULL,
  `pt_pwd` varchar(30) DEFAULT NULL,
  `pt_flag` tinyint(4) DEFAULT NULL,
  `pt_actived` varchar(4) DEFAULT NULL,
  `online_pay` tinyint(4) DEFAULT '1' COMMENT '是否能在线支付',
  `is_block` tinyint(4) DEFAULT '0' COMMENT '是否黑名单',
  `alipay_flag` tinyint(1) DEFAULT '0' COMMENT '是否可以使用支付宝',
  `kg_game` varchar(30) DEFAULT NULL COMMENT 'kg用户名',
  `kg_pwd` varchar(70) DEFAULT NULL COMMENT 'kg密码',
  `kg_flag` tinyint(1) DEFAULT NULL,
  `kg_actived` varchar(1) DEFAULT '0',
  `mg_game` varchar(30) DEFAULT NULL COMMENT 'mg游戏名',
  `mg_pwd` varchar(70) DEFAULT NULL COMMENT 'mg密码',
  `mg_flag` tinyint(1) DEFAULT NULL COMMENT 'mg开立状态',
  `mg_actived` varchar(1) DEFAULT '0' COMMENT 'MG是否可进',
  `mg_alias` varchar(30) DEFAULT NULL,
  `alipay_account` varchar(40) DEFAULT NULL COMMENT '支付宝账号',
  `alipay_name` varchar(50) DEFAULT NULL COMMENT '支付宝名字',
  `auto_transfer_flag` tinyint(1) DEFAULT '0' COMMENT '是否自动转账',
  `need_gift_flag` tinyint(1) DEFAULT '1' COMMENT '是否需要礼金',
  `sys_locked` tinyint(1) DEFAULT '0' COMMENT '系统锁定',
  `locked_date` datetime DEFAULT NULL COMMENT '锁定时间',
  `bbinmobile_game` varchar(50) DEFAULT NULL COMMENT 'bbin手机客户端用户名',
  `bbinmobile_pwd` varchar(30) DEFAULT NULL COMMENT '手机端密码',
  `bbinmobile_flag` tinyint(1) DEFAULT '0',
  `phone_reg` tinyint(1) DEFAULT '0' COMMENT '是否手机注册',
  `promo_flag` tinyint(3) DEFAULT '1' COMMENT '是否自动添加优惠,1X需要0不需要',
  `reg_domain` varchar(32) DEFAULT NULL COMMENT '注册域名',
  `refer_url` varchar(100) DEFAULT NULL COMMENT '流量来源URL',
  `search_key` varchar(100) DEFAULT NULL COMMENT '搜索关键词',
  `bank_pay` tinyint(1) DEFAULT '1',
  `accountkey` varchar(60) DEFAULT NULL,
  `pp_game` varchar(30) DEFAULT NULL,
  `pp_pwd` varchar(70) DEFAULT NULL,
  `pp_flag` tinyint(1) DEFAULT NULL,
  `pp_actived` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`cust_id`,`login_name`),
  UNIQUE KEY `login_name` (`login_name`)
) ENGINE=MyISAM AUTO_INCREMENT=77443 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_customer_status"
#

CREATE TABLE `mb_customer_status` (
  `cust_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `login_name` varchar(50) NOT NULL COMMENT '用户名',
  `mg_transfer_flag` tinyint(1) DEFAULT '0' COMMENT 'mg转账状态',
  `pt_transfer_flag` tinyint(1) DEFAULT '0' COMMENT 'pt转账状态',
  `ag_transfer_flag` tinyint(1) DEFAULT '0' COMMENT 'ag转账状态',
  `bbin_transfer_flag` tinyint(1) DEFAULT '0' COMMENT 'bbin转账状态',
  `kg_transfer_flag` tinyint(1) DEFAULT '0' COMMENT 'kg转账状态',
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`cust_id`,`login_name`),
  UNIQUE KEY `login_name` (`login_name`)
) ENGINE=MyISAM AUTO_INCREMENT=3548 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_deposit"
#

CREATE TABLE `mb_deposit` (
  `deposit_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dep_no` varchar(25) DEFAULT NULL COMMENT '存款单号',
  `cust_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `login_name` varchar(30) DEFAULT NULL COMMENT '用户名',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '存款金额',
  `poundage` decimal(10,2) DEFAULT NULL COMMENT '手续费',
  `pdage_status` int(11) DEFAULT NULL COMMENT '手续费状态',
  `status` int(11) DEFAULT NULL COMMENT '提案状态',
  `bank_name` varchar(30) DEFAULT NULL COMMENT '存款银行',
  `account_name` varchar(30) DEFAULT NULL COMMENT '收款人',
  `deposit_type` varchar(30) DEFAULT NULL COMMENT '存款类型',
  `location` varchar(100) DEFAULT NULL COMMENT '存款地点',
  `deposit_date` datetime DEFAULT NULL COMMENT '存款时间',
  `remarks` varchar(300) DEFAULT NULL COMMENT '备注',
  `pic_id` bigint(20) DEFAULT NULL COMMENT '图片地址',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(30) DEFAULT NULL COMMENT '创建人',
  `ip` varchar(30) DEFAULT NULL COMMENT '客户IP',
  `audit_date` datetime DEFAULT NULL COMMENT '审核通过时间',
  `sb_game` varchar(30) DEFAULT NULL COMMENT '申博帐号',
  `is_sb` tinyint(1) DEFAULT NULL COMMENT '是否存入申博',
  `order_no` varchar(30) DEFAULT NULL COMMENT '在线支付订单',
  `locked` tinyint(1) DEFAULT NULL COMMENT '是否被锁',
  `locked_date` datetime DEFAULT NULL COMMENT '锁住时间',
  PRIMARY KEY (`deposit_id`)
) ENGINE=MyISAM AUTO_INCREMENT=11653 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_deposit_log"
#

CREATE TABLE `mb_deposit_log` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pre_status` int(11) DEFAULT NULL COMMENT '变更前状态',
  `after_status` int(11) DEFAULT NULL COMMENT '变更后状态',
  `deposit_id` bigint(20) DEFAULT NULL COMMENT '存款提案ID',
  `remarks` varchar(300) DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL COMMENT '操作时间',
  `create_user` varchar(30) DEFAULT NULL COMMENT '操作人',
  `dep_no` varchar(30) DEFAULT NULL COMMENT '存款单号',
  PRIMARY KEY (`log_id`)
) ENGINE=MyISAM AUTO_INCREMENT=23302 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_dinpay"
#

CREATE TABLE `mb_dinpay` (
  `dinpay_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cust_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `login_name` varchar(30) DEFAULT NULL COMMENT '登录名',
  `merchant_code` varchar(30) DEFAULT NULL COMMENT '商店号码',
  `order_no` varchar(60) DEFAULT NULL COMMENT '订单号',
  `order_amount` decimal(10,2) DEFAULT NULL COMMENT '下单金额',
  `order_time` varchar(25) DEFAULT NULL COMMENT '下单时间',
  `client_ip` varchar(50) DEFAULT NULL COMMENT '客户ID',
  `product_name` varchar(50) DEFAULT NULL COMMENT '产品名字',
  `bank_code` varchar(30) DEFAULT NULL COMMENT '银行号码',
  `bank_name` varchar(30) DEFAULT NULL COMMENT '银行名字',
  `bank_seq_no` varchar(50) DEFAULT NULL COMMENT '银行流水号',
  `trade_status` varchar(20) DEFAULT NULL COMMENT '支付状态',
  `trade_no` varchar(30) DEFAULT NULL COMMENT '智付订单号',
  `trade_time` varchar(30) DEFAULT NULL COMMENT '智付时间',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `notify_id` varchar(50) DEFAULT NULL COMMENT '提醒窜',
  `interface_version` varchar(30) DEFAULT NULL COMMENT '接口版本',
  `rec_sign_type` varchar(20) DEFAULT NULL COMMENT '签名类型',
  `rec_sign` varchar(70) DEFAULT NULL COMMENT '签名信息',
  `pay_amount` decimal(10,2) DEFAULT NULL COMMENT '支付金额',
  `send_sign_type` varchar(20) DEFAULT NULL COMMENT '发送签名类型',
  `send_sign` varchar(350) DEFAULT NULL COMMENT '发送签名',
  `finished` tinyint(1) DEFAULT '0' COMMENT '是否完成',
  `finished_date` datetime DEFAULT NULL COMMENT '完成时间',
  `dep_no` varchar(30) DEFAULT NULL COMMENT '关联存款单',
  PRIMARY KEY (`dinpay_id`),
  UNIQUE KEY `trade_no` (`trade_no`),
  UNIQUE KEY `dinpay_order_no` (`order_no`)
) ENGINE=MyISAM AUTO_INCREMENT=19532 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_discount"
#

CREATE TABLE `mb_discount` (
  `discount_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `b_url` varchar(200) DEFAULT NULL,
  `s_url` varchar(200) DEFAULT NULL,
  `title` varchar(300) DEFAULT NULL,
  `content` text,
  `available` tinyint(1) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `create_user` varchar(100) DEFAULT NULL,
  `priority` int(11) DEFAULT NULL,
  `summary` varchar(200) DEFAULT NULL,
  `is_hot` tinyint(1) DEFAULT '0',
  `start_date` datetime DEFAULT NULL COMMENT '开始时间',
  `end_date` datetime DEFAULT NULL COMMENT '结束时间',
  `mobile_flag` tinyint(1) DEFAULT '0' COMMENT '0表示网页版,1表示手机端优惠',
  PRIMARY KEY (`discount_id`)
) ENGINE=MyISAM AUTO_INCREMENT=120 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_dpay_deposit"
#

CREATE TABLE `mb_dpay_deposit` (
  `dp_pay_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `company_id` varchar(2) NOT NULL COMMENT '企业ID',
  `bank_id` varchar(2) DEFAULT NULL COMMENT '存款银行',
  `bank_code` varchar(30) DEFAULT NULL COMMENT '银行CODE',
  `order_no` varchar(64) NOT NULL COMMENT '订单号',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '存款金额',
  `login_name` varchar(30) DEFAULT NULL COMMENT '用户名',
  `pay_bank_id` varchar(2) DEFAULT NULL COMMENT '付款银行',
  `pay_bank_code` varchar(30) DEFAULT NULL COMMENT '付款银行代号',
  `mode` varchar(2) DEFAULT NULL COMMENT '存款方式',
  `group_id` varchar(2) DEFAULT '0' COMMENT '组0',
  `web_url` varchar(200) DEFAULT NULL COMMENT '通知URL',
  `memo` varchar(200) DEFAULT NULL COMMENT '备用字段',
  `note` varchar(200) DEFAULT NULL COMMENT '附言',
  `note_model` varchar(2) DEFAULT NULL COMMENT '附言格式',
  `terminal` varchar(2) DEFAULT NULL COMMENT '使用终端',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `rec_bank_account` varchar(30) DEFAULT NULL COMMENT '收款银行账号',
  `rec_bank_account_name` varchar(30) DEFAULT NULL COMMENT '收款银行帐户名',
  `rec_amount` decimal(10,2) DEFAULT NULL COMMENT '收款金额',
  `rec_email` varchar(50) DEFAULT NULL COMMENT '收款EMAIL',
  `rec_order_no` varchar(64) DEFAULT NULL COMMENT '收款订单号',
  `rec_date_time` varchar(30) DEFAULT NULL COMMENT '接收时间',
  `rec_note` varchar(200) DEFAULT NULL COMMENT '接收附言',
  `rec_pay_no` varchar(64) DEFAULT NULL COMMENT '平台订单号',
  `rec_status` varchar(2) DEFAULT NULL COMMENT '接收状态',
  `rec_error_msg` varchar(150) DEFAULT NULL COMMENT '错误信息',
  `rec_mode` varchar(2) DEFAULT NULL COMMENT '接收模式',
  `rec_bank_dot` varchar(80) DEFAULT NULL COMMENT '收款开户网点',
  `rec_break_url` varchar(300) DEFAULT NULL COMMENT '跳转URL',
  `rec_bank_code` varchar(30) DEFAULT NULL COMMENT '收款银行',
  `status` varchar(2) DEFAULT NULL COMMENT '订单状态-1/0/1/2',
  `finished_date` datetime DEFAULT NULL COMMENT '完成时间',
  `error_msg` varchar(200) DEFAULT NULL COMMENT '错误信息',
  `nfy_pay_time` varchar(20) DEFAULT NULL COMMENT '提醒时间',
  `nfy_bank_id` varchar(2) DEFAULT NULL COMMENT '银行',
  `nfy_amount` decimal(10,2) DEFAULT NULL,
  `nfy_order_no` varchar(64) DEFAULT NULL,
  `nfy_pay_no` varchar(64) DEFAULT NULL,
  `nfy_pay_account` varchar(30) DEFAULT NULL,
  `nfy_pay_name` varchar(30) DEFAULT NULL,
  `nfy_channel` varchar(16) DEFAULT NULL,
  `nfy_area` varchar(32) DEFAULT NULL,
  `nfy_fee` varchar(30) DEFAULT NULL,
  `nfy_charge` decimal(10,2) DEFAULT NULL,
  `nfy_deposit_mode` varchar(3) DEFAULT NULL,
  `nfy_base_info` varchar(256) DEFAULT NULL,
  `nfy_date` varchar(20) DEFAULT NULL,
  `P_flag` tinyint(4) DEFAULT '0' COMMENT '是否已处理',
  PRIMARY KEY (`dp_pay_id`)
) ENGINE=InnoDB AUTO_INCREMENT=159 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_dpay_deposit_exp"
#

CREATE TABLE `mb_dpay_deposit_exp` (
  `dpay_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `exception_order_num` varchar(64) DEFAULT NULL COMMENT '异常订单号',
  `company_id` varchar(2) DEFAULT NULL COMMENT '公司ID',
  `exact_payment_bank` varchar(16) DEFAULT NULL COMMENT '实际付款银行',
  `pay_card_name` varchar(32) DEFAULT NULL COMMENT '支付账户名',
  `pay_card_num` varchar(32) DEFAULT NULL COMMENT '支付卡号',
  `receiving_bank` varchar(2) DEFAULT NULL COMMENT '收款银行',
  `receiving_account_name` varchar(32) DEFAULT NULL COMMENT '收款账号',
  `channel` varchar(16) DEFAULT NULL COMMENT '通道',
  `note` varchar(30) DEFAULT NULL COMMENT '备注',
  `area` varchar(32) DEFAULT NULL COMMENT '区域',
  `exact_time` varchar(16) DEFAULT NULL COMMENT '实际时间',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '金额',
  `fee` decimal(10,2) DEFAULT NULL COMMENT '手续费',
  `transaction_charge` decimal(10,2) DEFAULT NULL COMMENT '交易费',
  `base_info` varchar(256) DEFAULT NULL COMMENT '付款信息',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `flag` int(11) DEFAULT '0' COMMENT '是否被认领,0表示未认领',
  `login_name` varchar(30) DEFAULT NULL COMMENT '认领账号',
  `action_user` varchar(30) DEFAULT NULL COMMENT '认领操作人',
  `claim_date` datetime DEFAULT NULL COMMENT '认领日期',
  `pic_id` bigint(20) DEFAULT NULL COMMENT '截图地址',
  `action_user2` varchar(30) DEFAULT NULL COMMENT '认领申请人',
  `claim_date2` datetime DEFAULT NULL COMMENT '认领申请日期',
  `remark` varchar(150) DEFAULT NULL COMMENT '备注',
  `pic2_id` bigint(20) DEFAULT NULL COMMENT '截图2地址',
  PRIMARY KEY (`dpay_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_dpay_withdraw"
#

CREATE TABLE `mb_dpay_withdraw` (
  `dp_pay_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `company_id` varchar(20) DEFAULT NULL,
  `bank_id` varchar(2) DEFAULT NULL COMMENT '支付银行',
  `bank_code` varchar(30) DEFAULT NULL,
  `order_no` varchar(64) DEFAULT NULL COMMENT '订单号',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '支付金额',
  `card_num` varchar(32) DEFAULT NULL COMMENT '银行卡号',
  `card_name` varchar(32) DEFAULT NULL COMMENT '姓名',
  `login_name` varchar(30) DEFAULT NULL COMMENT '登录账号',
  `issue_bank_name` varchar(32) DEFAULT NULL COMMENT '开户行',
  `issue_bank_address` varchar(64) DEFAULT NULL COMMENT '开户行地址',
  `memo` varchar(128) DEFAULT NULL COMMENT '备注',
  `rec_pay_no` varchar(64) DEFAULT NULL COMMENT '平台订单号',
  `status` int(11) DEFAULT NULL COMMENT '订单状态/0(开始创建)/1(创建成功)/2(确认成功)/3（支付成功）/-1（失败）',
  `error_msg` varchar(128) DEFAULT NULL COMMENT '订单错误信息',
  `transaction_charge` decimal(10,2) DEFAULT NULL COMMENT '预计手续费',
  `exact_amount` decimal(10,2) DEFAULT NULL COMMENT '实际支付金额',
  `rec_detail` varchar(128) DEFAULT NULL COMMENT '支付明细',
  `rec_second_status` int(11) DEFAULT NULL COMMENT '支付状态',
  `rec_second_error_msg` varchar(128) DEFAULT NULL COMMENT '失败原因',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `first_date` datetime DEFAULT NULL COMMENT '确认日期',
  `second_date` datetime DEFAULT NULL COMMENT '支付日期',
  `exact_transaction_charge` decimal(10,2) DEFAULT NULL COMMENT '实际手续费',
  PRIMARY KEY (`dp_pay_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_dybp"
#

CREATE TABLE `mb_dybp` (
  `dybp_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pay_id` varchar(32) NOT NULL COMMENT '支付订单',
  `login_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '金额',
  `payip` varchar(100) DEFAULT NULL COMMENT 'ip',
  `pay_method` varchar(50) DEFAULT NULL COMMENT '支付渠道',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `order_no` varchar(64) DEFAULT NULL,
  `real_amount` decimal(10,2) DEFAULT NULL COMMENT '实际到账',
  `state` int(2) DEFAULT '0' COMMENT '2为支付成功',
  `modify_time` datetime DEFAULT NULL COMMENT '支付时间',
  `payer_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `real_pay_method` varchar(64) DEFAULT NULL COMMENT '实际支付类型',
  `finished_date` datetime DEFAULT NULL COMMENT '支付成功时间',
  `return_url` varchar(300) DEFAULT NULL COMMENT '返回的URL',
  PRIMARY KEY (`dybp_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_egg_gift"
#

CREATE TABLE `mb_egg_gift` (
  `gift_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '中奖单ID',
  `gift_code` varchar(50) DEFAULT NULL COMMENT '中奖号码',
  `cust_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `login_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `real_name` varchar(50) DEFAULT NULL COMMENT '客户姓名',
  `trophy_code` varchar(30) DEFAULT NULL COMMENT '奖品编号',
  `trophy_name` varchar(30) DEFAULT NULL COMMENT '奖品名称',
  `trophy_count` int(11) DEFAULT NULL COMMENT '奖品数量',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(30) DEFAULT NULL COMMENT '创建人',
  `status` int(11) DEFAULT NULL COMMENT '状态|已兑奖|待兑奖|兑奖失败',
  `remark` varchar(200) DEFAULT NULL COMMENT '兑奖内容',
  `audit_user` varchar(50) DEFAULT NULL COMMENT '兑奖人',
  `audit_date` datetime DEFAULT NULL COMMENT '兑奖日期',
  `ref_gift_code` varchar(50) DEFAULT NULL COMMENT '关联礼单编号',
  `score` decimal(10,2) DEFAULT NULL COMMENT '消耗积分',
  `trophy_id` bigint(20) DEFAULT NULL,
  `cost` decimal(10,2) DEFAULT NULL COMMENT '价值',
  `trophy_type` varchar(30) DEFAULT NULL COMMENT '奖品类型',
  PRIMARY KEY (`gift_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5983 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_egg_trophy"
#

CREATE TABLE `mb_egg_trophy` (
  `trophy_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '奖池',
  `trophy_name` varchar(50) DEFAULT NULL COMMENT '奖品名字',
  `trophy_code` varchar(30) DEFAULT NULL COMMENT '奖品代码',
  `trophy_count` int(11) DEFAULT '1' COMMENT '数量',
  `egg` varchar(30) DEFAULT NULL COMMENT '对应的蛋种',
  `is_default` tinyint(1) DEFAULT '0' COMMENT '是否默认',
  `enable` tinyint(1) DEFAULT '1' COMMENT '是否可用',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `cost` decimal(10,2) DEFAULT NULL COMMENT '价值',
  `trophy_type` varchar(30) DEFAULT NULL COMMENT '奖品类型',
  PRIMARY KEY (`trophy_id`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_func"
#

CREATE TABLE `mb_func` (
  `funccode` varchar(30) NOT NULL,
  `funcname` varchar(30) DEFAULT NULL,
  `pfunccode` varchar(30) DEFAULT NULL,
  `isgroup` tinyint(1) DEFAULT NULL,
  `createuser` varchar(30) DEFAULT NULL,
  `url` varchar(100) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `isbut` tinyint(1) DEFAULT NULL,
  `icon` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`funccode`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

#
# Structure for table "mb_hbp"
#

CREATE TABLE `mb_hbp` (
  `hbp_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pay_id` varchar(32) NOT NULL COMMENT '支付单号',
  `login_name` varchar(50) NOT NULL COMMENT '用户名',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '金额',
  `product_name` varchar(100) DEFAULT NULL COMMENT '商品',
  `bank_code` varchar(50) DEFAULT NULL COMMENT '支付渠道',
  `customer_ip` varchar(50) DEFAULT NULL COMMENT '支付IP',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `order_no` varchar(64) DEFAULT NULL,
  `real_amount` decimal(10,2) DEFAULT NULL COMMENT '实际到账金额',
  `state` int(11) DEFAULT '0' COMMENT '2为支付成功',
  `modify_time` datetime DEFAULT NULL COMMENT '支付时间',
  `payer_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `real_pay_method` varchar(64) DEFAULT NULL COMMENT '实际支付类型',
  `finished_date` datetime DEFAULT NULL COMMENT '支付成功时间',
  `return_url` varchar(300) DEFAULT NULL COMMENT '返回的URL',
  PRIMARY KEY (`hbp_id`),
  UNIQUE KEY `pay_id` (`pay_id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_hc_pay"
#

CREATE TABLE `mb_hc_pay` (
  `gc_pay_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `bill_no` varchar(30) DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `bank_code` varchar(30) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `order_time` varchar(20) DEFAULT NULL,
  `finish_date` datetime DEFAULT NULL,
  `dep_no` varchar(30) DEFAULT NULL,
  `login_name` varchar(30) DEFAULT NULL,
  `cust_id` bigint(20) DEFAULT NULL,
  `finished` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`gc_pay_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_hongbao"
#

CREATE TABLE `mb_hongbao` (
  `mb_hongbao_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `cust_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `gift_id` bigint(20) DEFAULT NULL COMMENT '礼物ID',
  `credit` decimal(10,2) DEFAULT NULL COMMENT '金额',
  `status` int(1) DEFAULT '0',
  `content` varchar(200) DEFAULT NULL,
  `level` varchar(4) DEFAULT NULL,
  `gift_no` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`mb_hongbao_id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_htp"
#

CREATE TABLE `mb_htp` (
  `htp_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pay_id` varchar(96) NOT NULL COMMENT '支付单号',
  `login_name` varchar(150) NOT NULL COMMENT '用户名',
  `amount` decimal(12,0) DEFAULT NULL COMMENT '金额',
  `payip` varchar(300) DEFAULT NULL COMMENT 'ip',
  `pay_method` varchar(150) DEFAULT NULL COMMENT '支付渠道',
  `remark` varchar(600) DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `order_no` varchar(192) DEFAULT NULL,
  `real_amount` decimal(12,0) DEFAULT NULL COMMENT '实际到账金额',
  `state` int(11) DEFAULT NULL COMMENT '2为支付成功',
  `modify_time` datetime DEFAULT NULL COMMENT '支付时间',
  `payer_name` varchar(150) DEFAULT NULL COMMENT '用户名',
  `real_pay_method` varchar(192) DEFAULT NULL COMMENT '实际支付类型',
  `finished_date` datetime DEFAULT NULL COMMENT '支付成功时间',
  `return_url` varchar(900) DEFAULT NULL COMMENT '返回的URL',
  PRIMARY KEY (`htp_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4;

#
# Structure for table "mb_huibo_otherpay"
#

CREATE TABLE `mb_huibo_otherpay` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `account` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '商户账号',
  `orderId` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '订单编号',
  `accNo` varchar(30) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '银行账号',
  `accName` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '账户名称',
  `bankName` varchar(30) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '银行名称',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '交易金额',
  `payPwd` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '支付密码',
  `status` tinyint(10) DEFAULT '0' COMMENT '0已提交，1成功，2失败',
  `create_user` varchar(20) DEFAULT NULL COMMENT '创建人',
  `create_date` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `remark` varchar(50) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_huoli_gift"
#

CREATE TABLE `mb_huoli_gift` (
  `huoli_gift_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `cust_id` bigint(20) NOT NULL COMMENT '用户ID',
  `login_name` varchar(50) NOT NULL COMMENT '用户名',
  `content` varchar(350) NOT NULL COMMENT '内容',
  `create_date` datetime NOT NULL COMMENT '时间',
  `gift_no` varchar(40) NOT NULL COMMENT '编号',
  `flag` tinyint(4) DEFAULT '0' COMMENT '派发是否成功(0:未派发，1:已派发,2:派发失败)',
  `level` tinyint(2) DEFAULT '0' COMMENT '活力等级',
  PRIMARY KEY (`huoli_gift_id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_index_date"
#

CREATE TABLE `mb_index_date` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `index_date` date NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1043 DEFAULT CHARSET=latin1;

#
# Structure for table "mb_item"
#

CREATE TABLE `mb_item` (
  `itemname` varchar(30) DEFAULT NULL,
  `itemcode` varchar(30) NOT NULL,
  `itemvalue` varchar(30) DEFAULT NULL,
  `groupcode` varchar(30) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `createuser` varchar(30) DEFAULT NULL,
  `platform` varchar(20) DEFAULT 'all',
  `frozenflag` tinyint(3) DEFAULT '0' COMMENT '0表示不冻结,1表示冻结',
  `startdate` datetime DEFAULT NULL COMMENT '活动开始时间',
  `enddate` datetime DEFAULT NULL COMMENT '活动结束时间',
  `pcflag` tinyint(2) DEFAULT NULL COMMENT 'PC前台是否显示1显示，0不显示',
  `mobileflag` tinyint(2) DEFAULT NULL COMMENT '移动前台是否显示',
  `actbill` varchar(30) DEFAULT NULL COMMENT '活动流水',
  `moneyflag` tinyint(2) DEFAULT NULL COMMENT '前台输入金额',
  PRIMARY KEY (`itemcode`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

#
# Structure for table "mb_jbp"
#

CREATE TABLE `mb_jbp` (
  `jbp_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pay_id` varchar(32) NOT NULL COMMENT '支付单号',
  `login_name` varchar(50) NOT NULL COMMENT '用户名',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '金额',
  `goods_name` varchar(100) DEFAULT NULL COMMENT '商品',
  `partner_id` varchar(100) DEFAULT NULL COMMENT '合作商品',
  `pay_method` varchar(50) DEFAULT NULL COMMENT '支付渠道',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `order_no` varchar(64) DEFAULT NULL,
  `real_amount` decimal(10,2) DEFAULT NULL COMMENT '实际到账金额',
  `state` int(11) DEFAULT '0' COMMENT '2为支付成功',
  `modify_time` datetime DEFAULT NULL COMMENT '支付时间',
  `payer_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `real_pay_method` varchar(64) DEFAULT NULL COMMENT '实际支付类型',
  `finished_date` datetime DEFAULT NULL COMMENT '支付成功时间',
  `return_url` varchar(300) DEFAULT NULL COMMENT '返回的URL',
  PRIMARY KEY (`jbp_id`),
  UNIQUE KEY `pay_id` (`pay_id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_letter"
#

CREATE TABLE `mb_letter` (
  `letter_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `title` varchar(100) DEFAULT NULL COMMENT '标题',
  `content` varchar(1000) DEFAULT NULL COMMENT '内容',
  `cust_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `read_flag` tinyint(4) DEFAULT NULL COMMENT '是否已读',
  `read_date` datetime DEFAULT NULL COMMENT '阅读日期',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `create_user` varchar(50) DEFAULT NULL COMMENT '创建人',
  `is_public` tinyint(4) DEFAULT NULL,
  `login_name` varchar(50) DEFAULT NULL,
  `agent_id` bigint(20) DEFAULT NULL COMMENT '代理ID',
  `letter_code` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`letter_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4016 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_message_board"
#

CREATE TABLE `mb_message_board` (
  `msg_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cust_name` varchar(30) DEFAULT NULL,
  `question` varchar(300) DEFAULT NULL,
  `reply` varchar(800) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `create_user` varchar(30) DEFAULT NULL,
  `reply_user` varchar(30) DEFAULT NULL,
  `show_date` datetime DEFAULT NULL,
  `reply_date` datetime DEFAULT NULL,
  PRIMARY KEY (`msg_id`)
) ENGINE=MyISAM AUTO_INCREMENT=390 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_mg_bet_detail"
#

CREATE TABLE `mb_mg_bet_detail` (
  `RowId` bigint(12) NOT NULL,
  `TotalPayout` int(11) DEFAULT NULL,
  `ProgressiveWage` int(11) DEFAULT NULL,
  `TotalWager` int(11) DEFAULT NULL,
  `GamePlatform` varchar(24) DEFAULT NULL,
  `AccountNumber` varchar(16) DEFAULT NULL,
  `TransactionId` int(11) DEFAULT NULL,
  `PCA` decimal(10,2) DEFAULT NULL,
  `ISOCode` varchar(8) DEFAULT NULL,
  `GameEndTime` datetime DEFAULT NULL,
  `ModuleId` int(11) DEFAULT NULL,
  `DisplayName` varchar(32) DEFAULT NULL,
  `ClientId` int(11) DEFAULT NULL,
  `IsFreeGame` varchar(8) DEFAULT NULL,
  `DisplayGameCategory` varchar(32) DEFAULT NULL,
  `SessionId` int(11) DEFAULT NULL,
  `settleStatus` tinyint(1) NOT NULL DEFAULT '0',
  `settleBillNo` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`RowId`),
  KEY `settleBillNo` (`settleBillNo`),
  KEY `AccountNumber` (`AccountNumber`),
  KEY `DisplayGameCategory` (`DisplayGameCategory`),
  KEY `GameEndTime` (`GameEndTime`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Structure for table "mb_mg_betdata"
#

CREATE TABLE `mb_mg_betdata` (
  `dataId` bigint(20) NOT NULL AUTO_INCREMENT,
  `RowId` bigint(20) DEFAULT NULL,
  `AccountNumber` varchar(30) DEFAULT NULL,
  `DisplayName` varchar(100) DEFAULT NULL,
  `DisplayGameCategory` varchar(200) DEFAULT NULL,
  `SessionId` bigint(20) DEFAULT NULL,
  `GameEndTime` datetime DEFAULT NULL,
  `TotalWager` decimal(10,2) DEFAULT NULL,
  `TotalPayout` decimal(10,2) DEFAULT NULL,
  `ProgressiveWage` decimal(10,2) DEFAULT NULL,
  `ISOCode` varchar(5) DEFAULT NULL,
  `GamePlatform` varchar(10) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`dataId`),
  UNIQUE KEY `RowId` (`RowId`),
  KEY `AccountNumber` (`AccountNumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Structure for table "mb_mg_betreward_bill"
#

CREATE TABLE `mb_mg_betreward_bill` (
  `settleBillNo` varchar(20) NOT NULL DEFAULT '',
  `playerName` varchar(40) DEFAULT NULL,
  `userLevel` tinyint(4) DEFAULT NULL,
  `gameCat` varchar(20) DEFAULT NULL,
  `beginTime` datetime DEFAULT NULL,
  `endTime` datetime DEFAULT NULL,
  `betAmount` decimal(12,2) DEFAULT NULL,
  `rewardPercent` decimal(4,2) DEFAULT NULL,
  `rewardAmount` decimal(12,2) DEFAULT NULL,
  `auditStatus` tinyint(4) DEFAULT NULL,
  `memo` varchar(200) DEFAULT NULL,
  `auditor` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`settleBillNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Structure for table "mb_mg_flash_game"
#

CREATE TABLE `mb_mg_flash_game` (
  `mg_id` int(11) NOT NULL AUTO_INCREMENT,
  `Category` varchar(30) DEFAULT NULL,
  `Game_caegory` varchar(30) DEFAULT NULL,
  `Sub_category` varchar(30) DEFAULT NULL,
  `GameName` varchar(50) DEFAULT NULL,
  `CHINESE_SIMP_Game_Name` varchar(30) DEFAULT NULL,
  `CHINESE_TRAD_Game_Name` varchar(30) DEFAULT NULL,
  `Image_File_Name` varchar(50) DEFAULT NULL,
  `PlayCheck` varchar(40) DEFAULT NULL,
  `New_Game` varchar(50) DEFAULT NULL,
  `iCafe-Chinese_Simp_zh` varchar(30) DEFAULT NULL,
  `iCafe-Chinese_Trad_zh-tw` varchar(30) DEFAULT NULL,
  `Translations` varchar(30) DEFAULT NULL,
  `GameCode` varchar(40) DEFAULT NULL,
  `ModuleID` int(20) DEFAULT NULL,
  `ClientID` int(20) DEFAULT NULL,
  `MinBet-CNY` int(20) DEFAULT NULL,
  `MaxBet-CNY` int(20) DEFAULT NULL,
  `Indonesian_id` varchar(30) DEFAULT NULL,
  `Vietnamese_vi` varchar(30) DEFAULT NULL,
  `French_fr` varchar(30) DEFAULT NULL,
  `German_de` varchar(30) DEFAULT NULL,
  `Greek_el` varchar(30) DEFAULT NULL,
  `Italian_it` varchar(30) DEFAULT NULL,
  `Japanese_ja` varchar(30) DEFAULT NULL,
  `Korean_ko` varchar(30) DEFAULT NULL,
  `Russian_ru` varchar(30) DEFAULT NULL,
  `Spanish_es` varchar(30) DEFAULT NULL,
  `Turkish_tr` varchar(30) DEFAULT NULL,
  `Portuguese_pt-br` varchar(30) DEFAULT NULL,
  `HM_game` varchar(30) DEFAULT NULL,
  `temporarily_removed` varchar(30) DEFAULT NULL,
  `enable` tinyint(1) DEFAULT NULL COMMENT '是否可用',
  `sortpriority` int(5) DEFAULT NULL COMMENT '排序优先级,数值越小越靠前，1在最前面，null在最后面',
  `is_hot` tinyint(3) DEFAULT '0' COMMENT '是否热门 1',
  `is_new` tinyint(3) DEFAULT '0' COMMENT '是否最新 1',
  `is_html5` tinyint(3) DEFAULT '0' COMMENT '是否支持html5 1支持',
  `is_pchtml5` tinyint(3) DEFAULT '0',
  `pchtml5code` varchar(40) DEFAULT NULL,
  `mobilehtml5code` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`mg_id`)
) ENGINE=InnoDB AUTO_INCREMENT=518 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_molehit_active"
#

CREATE TABLE `mb_molehit_active` (
  `id` bigint(10) NOT NULL DEFAULT '0',
  `login_name` varchar(60) DEFAULT NULL,
  `cust_id` bigint(10) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `hitcount` tinyint(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Structure for table "mb_msg"
#

CREATE TABLE `mb_msg` (
  `msg_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `msg_type` int(11) DEFAULT NULL COMMENT '消息类型',
  `content` varchar(300) DEFAULT NULL COMMENT '内容',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `flag` int(11) DEFAULT NULL COMMENT '是否处理',
  PRIMARY KEY (`msg_id`)
) ENGINE=MyISAM AUTO_INCREMENT=5523 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_notice"
#

CREATE TABLE `mb_notice` (
  `notice_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) DEFAULT NULL,
  `content` varchar(2000) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `end_date` datetime DEFAULT NULL,
  `create_user` varchar(30) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `priority` int(11) DEFAULT NULL,
  `available` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`notice_id`)
) ENGINE=MyISAM AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_ordernumber"
#

CREATE TABLE `mb_ordernumber` (
  `ordernumber` varchar(100) NOT NULL DEFAULT '',
  UNIQUE KEY `ordernumber` (`ordernumber`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Structure for table "mb_payonline"
#

CREATE TABLE `mb_payonline` (
  `payonline_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(30) DEFAULT NULL,
  `value` varchar(20) DEFAULT NULL,
  `submit_value` varchar(200) DEFAULT NULL,
  `return_value` varchar(200) DEFAULT NULL,
  `notify_value` varchar(200) DEFAULT NULL,
  `product` varchar(50) DEFAULT NULL,
  `valuedescribe` varchar(400) DEFAULT NULL,
  `req_referer` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`payonline_id`)
) ENGINE=MyISAM AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_pic"
#

CREATE TABLE `mb_pic` (
  `pic_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pic_data` mediumblob,
  `create_date` datetime DEFAULT NULL,
  `pic_size` bigint(20) DEFAULT NULL,
  `ftype` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`pic_id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_pp_game"
#

CREATE TABLE `mb_pp_game` (
  `game_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `game_name` varchar(1800) DEFAULT NULL,
  `game_code` varchar(900) DEFAULT NULL,
  `is_hot` tinyint(3) DEFAULT NULL,
  `is_new` tinyint(3) DEFAULT NULL,
  `enable` tinyint(2) DEFAULT NULL,
  `Image_File_Name` varchar(450) DEFAULT NULL,
  `game_type` varchar(10) DEFAULT NULL,
  `sortpriority` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`game_id`)
) ENGINE=InnoDB AUTO_INCREMENT=61 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_pt_bet_detail"
#

CREATE TABLE `mb_pt_bet_detail` (
  `PLAYERNAME` varchar(32) DEFAULT NULL,
  `WINDOWCODE` varchar(4) DEFAULT NULL,
  `GAMEID` int(8) DEFAULT NULL,
  `GAMECODE` bigint(16) NOT NULL,
  `GAMETYPE` varchar(32) DEFAULT NULL,
  `GAMENAME` varchar(64) DEFAULT NULL,
  `SESSIONID` bigint(16) DEFAULT NULL,
  `BET` decimal(10,2) DEFAULT NULL,
  `WIN` decimal(10,2) DEFAULT NULL,
  `PROGRESSIVEBET` decimal(10,2) DEFAULT NULL,
  `PROGRESSIVEWIN` decimal(10,2) DEFAULT NULL,
  `BALANCE` decimal(10,2) DEFAULT NULL,
  `CURRENTBET` decimal(10,2) DEFAULT NULL,
  `GAMEDATE` datetime DEFAULT NULL,
  `LIVENETWORK` varchar(32) DEFAULT NULL,
  `SETTLESTATUS` tinyint(1) NOT NULL DEFAULT '0',
  `SETTLEBILLNO` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`GAMECODE`),
  KEY `SETTLEBILLNO` (`SETTLEBILLNO`),
  KEY `PLAYERNAME` (`PLAYERNAME`),
  KEY `GAMETYPE` (`GAMETYPE`),
  KEY `GAMEDATE` (`GAMEDATE`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

#
# Structure for table "mb_pt_betdata"
#

CREATE TABLE `mb_pt_betdata` (
  `Id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(50) DEFAULT NULL,
  `windowcode` varchar(5) DEFAULT NULL,
  `gameid` int(8) DEFAULT NULL,
  `gamecode` bigint(20) DEFAULT NULL,
  `gametype` varchar(40) DEFAULT NULL,
  `gamename` varchar(100) DEFAULT NULL,
  `sessionid` bigint(20) DEFAULT NULL,
  `bet` decimal(10,2) DEFAULT NULL,
  `win` decimal(10,2) DEFAULT NULL,
  `balance` decimal(10,2) DEFAULT NULL,
  `gamedate` datetime DEFAULT NULL,
  `currentbet` decimal(10,2) DEFAULT NULL,
  `progressivebet` decimal(10,2) DEFAULT NULL,
  `progressivewin` decimal(10,2) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  PRIMARY KEY (`Id`),
  UNIQUE KEY `gamecode` (`gamecode`),
  KEY `login_name` (`login_name`)
) ENGINE=MyISAM AUTO_INCREMENT=241537 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_pt_betreward_bill"
#

CREATE TABLE `mb_pt_betreward_bill` (
  `settleBillNo` varchar(20) NOT NULL DEFAULT '',
  `playerName` varchar(40) DEFAULT NULL,
  `userLevel` tinyint(4) DEFAULT NULL,
  `gameCat` varchar(20) DEFAULT NULL,
  `beginTime` datetime DEFAULT NULL,
  `endTime` datetime DEFAULT NULL,
  `betAmount` decimal(12,2) DEFAULT NULL,
  `rewardPercent` decimal(4,2) DEFAULT NULL,
  `rewardAmount` decimal(12,2) DEFAULT NULL,
  `auditStatus` tinyint(4) DEFAULT NULL,
  `memo` varchar(200) DEFAULT NULL,
  `auditor` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`settleBillNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Structure for table "mb_random"
#

CREATE TABLE `mb_random` (
  `random_total` int(11) DEFAULT NULL,
  `random_now` int(11) DEFAULT NULL,
  `random_id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`random_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_ranking_list"
#

CREATE TABLE `mb_ranking_list` (
  `ranking_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `login_name` varchar(30) DEFAULT NULL COMMENT '用户名',
  `trophy_name` varchar(30) DEFAULT NULL COMMENT '礼金品',
  `trophy_code` varchar(30) DEFAULT NULL COMMENT '礼品代码',
  `trophy_count` int(11) DEFAULT NULL COMMENT '礼金数量',
  `cost` decimal(10,2) DEFAULT NULL COMMENT '价值',
  `cust_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  PRIMARY KEY (`ranking_id`)
) ENGINE=InnoDB AUTO_INCREMENT=666 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_role"
#

CREATE TABLE `mb_role` (
  `rolecode` varchar(30) DEFAULT NULL,
  `rolename` varchar(30) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `createuser` varchar(30) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

#
# Structure for table "mb_role_func"
#

CREATE TABLE `mb_role_func` (
  `rolecode` varchar(30) DEFAULT NULL,
  `funccode` varchar(30) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `createuser` varchar(30) DEFAULT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

#
# Structure for table "mb_rpn"
#

CREATE TABLE `mb_rpn` (
  `rpn_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` varchar(32) NOT NULL COMMENT '支付单号',
  `login_name` varchar(50) NOT NULL COMMENT '用户名',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '金额',
  `customer_ip` varchar(100) DEFAULT NULL COMMENT 'ip',
  `bank_id` varchar(50) DEFAULT NULL COMMENT '支付渠道',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `rpn_order_no` varchar(64) DEFAULT NULL,
  `real_amount` decimal(10,2) DEFAULT NULL COMMENT '实际到账金额',
  `state` int(11) DEFAULT '0' COMMENT '2为支付成功',
  `modify_time` datetime DEFAULT NULL COMMENT '支付时间',
  `payer_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `real_pay_method` varchar(64) DEFAULT NULL COMMENT '实际支付类型',
  `finished_date` datetime DEFAULT NULL COMMENT '支付成功时间',
  `return_url` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`rpn_id`),
  UNIQUE KEY `order_id` (`order_id`)
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_sanvpay"
#

CREATE TABLE `mb_sanvpay` (
  `svp_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pay_id` varchar(32) NOT NULL COMMENT '支付单号',
  `login_name` varchar(50) NOT NULL COMMENT '用户名',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '金额',
  `payip` varchar(100) DEFAULT NULL COMMENT 'ip',
  `pay_method` varchar(50) DEFAULT NULL COMMENT '支付渠道',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `order_no` varchar(64) DEFAULT NULL,
  `real_amount` decimal(10,2) DEFAULT NULL COMMENT '实际到账金额',
  `state` int(11) DEFAULT '0' COMMENT '2为支付成功',
  `modify_time` datetime DEFAULT NULL COMMENT '支付时间',
  `payer_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `real_pay_method` varchar(64) DEFAULT NULL COMMENT '实际支付类型',
  `finished_date` datetime DEFAULT NULL COMMENT '支付成功时间',
  `return_url` varchar(300) DEFAULT NULL COMMENT '返回的URL',
  PRIMARY KEY (`svp_id`),
  UNIQUE KEY `pay_id` (`pay_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Structure for table "mb_sb_bet_detail"
#

CREATE TABLE `mb_sb_bet_detail` (
  `ugsbetid` varchar(50) NOT NULL,
  `txid` varchar(50) DEFAULT NULL,
  `betid` varchar(64) DEFAULT NULL,
  `beton` datetime DEFAULT NULL,
  `betclosedon` datetime DEFAULT NULL,
  `betupdatedon` datetime DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  `roundid` varchar(50) DEFAULT NULL,
  `roundstatus` varchar(10) DEFAULT NULL,
  `userid` varchar(50) DEFAULT NULL,
  `username` varchar(50) DEFAULT NULL,
  `riskamt` decimal(10,2) DEFAULT NULL,
  `winamt` decimal(10,2) DEFAULT NULL,
  `winloss` decimal(10,2) DEFAULT NULL,
  `beforebal` decimal(10,2) DEFAULT NULL,
  `postbal` decimal(10,2) DEFAULT NULL,
  `cur` varchar(3) DEFAULT NULL,
  `gameprovider` varchar(50) DEFAULT NULL,
  `gameprovidercode` varchar(50) DEFAULT NULL,
  `gamename` varchar(50) DEFAULT NULL,
  `gameid` varchar(50) DEFAULT NULL,
  `platformtype` varchar(50) DEFAULT NULL,
  `ipaddress` varchar(15) DEFAULT NULL,
  `bettype` varchar(30) DEFAULT NULL,
  `playtype` varchar(50) DEFAULT NULL,
  `playertype` varchar(50) DEFAULT NULL,
  `turnover` decimal(10,2) DEFAULT NULL,
  `validbet` decimal(10,2) DEFAULT NULL,
  `settleStatus` tinyint(1) NOT NULL DEFAULT '0',
  `settleBillNo` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ugsbetid`),
  KEY `settleBillNo` (`settleBillNo`),
  KEY `userid` (`userid`),
  KEY `gameprovidercode` (`gameprovidercode`),
  KEY `betclosedon` (`betclosedon`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Structure for table "mb_sb_betreward_bill"
#

CREATE TABLE `mb_sb_betreward_bill` (
  `settleBillNo` varchar(20) NOT NULL DEFAULT '',
  `playerName` varchar(40) DEFAULT NULL,
  `userLevel` tinyint(4) DEFAULT NULL,
  `gameCat` varchar(20) DEFAULT NULL,
  `beginTime` datetime DEFAULT NULL,
  `endTime` datetime DEFAULT NULL,
  `betAmount` decimal(12,2) DEFAULT NULL,
  `rewardPercent` decimal(4,2) DEFAULT NULL,
  `rewardAmount` decimal(12,2) DEFAULT NULL,
  `auditStatus` tinyint(4) DEFAULT NULL,
  `memo` varchar(200) DEFAULT NULL,
  `auditor` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`settleBillNo`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Structure for table "mb_sb_game"
#

CREATE TABLE `mb_sb_game` (
  `sb_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `game_name` varchar(200) DEFAULT NULL COMMENT '游戏名字',
  `game_code` varchar(100) DEFAULT NULL COMMENT '游戏代码',
  `enable` tinyint(4) DEFAULT '1' COMMENT '是否可用',
  `image_game_name` varchar(50) DEFAULT NULL,
  `image_preview_name` varchar(50) DEFAULT NULL,
  `providercode` varchar(10) DEFAULT NULL,
  `game_type` varchar(20) DEFAULT NULL,
  `sortpriority` int(5) DEFAULT NULL COMMENT '排序优先级,数值越小越靠前，1在最前面，null在最后面',
  PRIMARY KEY (`sb_id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_score_rec"
#

CREATE TABLE `mb_score_rec` (
  `rec_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '积分记录',
  `rec_code` varchar(30) DEFAULT NULL COMMENT '积分代码|存款号码|抽奖号码',
  `cust_id` bigint(20) DEFAULT NULL COMMENT '用ID',
  `login_name` varchar(30) DEFAULT NULL COMMENT '用户名',
  `rec_type` varchar(50) DEFAULT NULL COMMENT '积分类型',
  `score` decimal(10,2) DEFAULT NULL COMMENT '积分',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(30) DEFAULT NULL COMMENT '创建人',
  `cur_score` decimal(10,2) DEFAULT NULL COMMENT '当前积分',
  PRIMARY KEY (`rec_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19281 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_sdpay"
#

CREATE TABLE `mb_sdpay` (
  `sd_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` varchar(32) NOT NULL COMMENT '支付单号',
  `login_name` varchar(50) NOT NULL COMMENT '用户名',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '金额',
  `customer_ip` varchar(100) DEFAULT NULL COMMENT 'ip',
  `bank_id` varchar(50) DEFAULT NULL COMMENT '支付渠道',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `rpn_order_no` varchar(64) DEFAULT NULL,
  `real_amount` decimal(10,2) DEFAULT NULL COMMENT '实际到账金额',
  `state` int(11) DEFAULT '0' COMMENT '2为支付成功',
  `modify_time` datetime DEFAULT NULL COMMENT '支付时间',
  `payer_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `real_pay_method` varchar(64) DEFAULT NULL COMMENT '实际支付类型',
  `finished_date` datetime DEFAULT NULL COMMENT '支付成功时间',
  `return_url` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`sd_id`),
  UNIQUE KEY `order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Structure for table "mb_self_app"
#

CREATE TABLE `mb_self_app` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '申请用户名',
  `app_name` varchar(20) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '优惠名字',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `status` tinyint(2) DEFAULT NULL COMMENT '0未读，1已读',
  `gift_no` varchar(30) DEFAULT NULL,
  `actbill` varchar(30) DEFAULT NULL COMMENT '活动流水',
  `moneyflag` tinyint(2) DEFAULT NULL COMMENT '前台显示金额',
  `remark` varchar(30) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=169 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_share_modify"
#

CREATE TABLE `mb_share_modify` (
  `modify_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `modify_no` varchar(30) DEFAULT NULL COMMENT '分成修改',
  `share_before` int(11) DEFAULT NULL COMMENT '修改前的分成',
  `share_after` int(11) DEFAULT NULL COMMENT '修改后的分成',
  `agent_id` bigint(20) DEFAULT NULL COMMENT '代理客户ID',
  `login_name` varchar(30) DEFAULT NULL COMMENT '用户名',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `create_user` varchar(30) DEFAULT NULL COMMENT '创建人',
  `status` int(11) DEFAULT NULL COMMENT '状态',
  `remarks` varchar(200) DEFAULT NULL COMMENT '变更原因',
  `audit_date` datetime DEFAULT NULL COMMENT '审核日期',
  `audit_user` varchar(30) DEFAULT NULL COMMENT '审核人',
  `audit_msg` varchar(200) DEFAULT NULL COMMENT '审核意见',
  PRIMARY KEY (`modify_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_sign"
#

CREATE TABLE `mb_sign` (
  `sign_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(50) DEFAULT NULL COMMENT '会员名',
  `cust_id` bigint(20) DEFAULT NULL COMMENT '会员ID',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `sign_date` timestamp NOT NULL DEFAULT '1971-01-01 00:00:00' COMMENT '签到时间',
  PRIMARY KEY (`sign_id`)
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_sign_gift"
#

CREATE TABLE `mb_sign_gift` (
  `sg_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(30) NOT NULL COMMENT '用户名',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '金额',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `flag` tinyint(4) DEFAULT NULL COMMENT '是否派送',
  `remarks` varchar(200) DEFAULT NULL COMMENT '备注',
  `audit_date` datetime DEFAULT NULL COMMENT '派送时间',
  `sg_num` varchar(30) DEFAULT NULL COMMENT '派送单号',
  PRIMARY KEY (`sg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Structure for table "mb_sign_reward"
#

CREATE TABLE `mb_sign_reward` (
  `reward_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `login_name` varchar(50) DEFAULT NULL,
  `cust_id` bigint(20) DEFAULT NULL,
  `reward_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '领取时间',
  `amount` decimal(10,0) DEFAULT NULL COMMENT '领取金额',
  `cust_level` int(11) DEFAULT NULL COMMENT '客户等级',
  `create_date` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`reward_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_smfp"
#

CREATE TABLE `mb_smfp` (
  `smfp_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pay_id` varchar(32) NOT NULL COMMENT '支付单号',
  `login_name` varchar(50) NOT NULL COMMENT '用户名',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '金额',
  `payip` varchar(100) DEFAULT NULL COMMENT 'ip',
  `pay_method` varchar(50) DEFAULT NULL COMMENT '支付渠道',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `order_no` varchar(64) DEFAULT NULL,
  `real_amount` decimal(10,2) DEFAULT NULL COMMENT '实际到账金额',
  `state` int(11) DEFAULT '0' COMMENT '2为支付成功',
  `modify_time` datetime DEFAULT NULL COMMENT '支付时间',
  `payer_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `real_pay_method` varchar(64) DEFAULT NULL COMMENT '实际支付类型',
  `finished_date` datetime DEFAULT NULL COMMENT '支付成功时间',
  `return_url` varchar(300) DEFAULT NULL COMMENT '返回的URL',
  PRIMARY KEY (`smfp_id`),
  UNIQUE KEY `pay_id` (`pay_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_stat"
#

CREATE TABLE `mb_stat` (
  `stat_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `cust_id` bigint(20) NOT NULL COMMENT '客户ID',
  `agent_id` bigint(20) DEFAULT NULL COMMENT '代理ID',
  `login_name` varchar(50) CHARACTER SET latin1 NOT NULL COMMENT '客户用户名',
  `stat_date` varchar(8) DEFAULT NULL COMMENT '统计日期',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `deposit_collect` decimal(10,2) DEFAULT NULL COMMENT '存款量',
  `withdraw_collect` decimal(10,2) DEFAULT NULL COMMENT '提款量',
  `gift_collect` decimal(10,2) DEFAULT NULL COMMENT '礼金量',
  `deposit_count` int(11) DEFAULT NULL COMMENT '存款笔数',
  `withdraw_count` int(11) DEFAULT NULL COMMENT '提款笔数',
  `gift_count` int(11) DEFAULT NULL COMMENT '礼金笔数',
  PRIMARY KEY (`stat_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2726910 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_test_order"
#

CREATE TABLE `mb_test_order` (
  `order_no` varchar(22) NOT NULL COMMENT '订单',
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`order_no`,`id`),
  KEY `id` (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=54682 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_thp"
#

CREATE TABLE `mb_thp` (
  `thp_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pay_id` varchar(32) NOT NULL COMMENT '支付单号',
  `login_name` varchar(50) NOT NULL COMMENT '用户名',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '金额',
  `product_name` varchar(100) DEFAULT NULL COMMENT '商品',
  `bank_code` varchar(50) DEFAULT NULL COMMENT '支付渠道',
  `customer_ip` varchar(50) DEFAULT NULL COMMENT '支付IP',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `order_no` varchar(64) DEFAULT NULL,
  `real_amount` decimal(10,2) DEFAULT NULL COMMENT '实际到账金额',
  `state` int(11) DEFAULT '0' COMMENT '2为支付成功',
  `modify_time` datetime DEFAULT NULL COMMENT '支付时间',
  `payer_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `real_pay_method` varchar(64) DEFAULT NULL COMMENT '实际支付类型',
  `finished_date` datetime DEFAULT NULL COMMENT '支付成功时间',
  `return_url` varchar(300) DEFAULT NULL COMMENT '返回的URL',
  PRIMARY KEY (`thp_id`),
  UNIQUE KEY `pay_id` (`pay_id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_transfer"
#

CREATE TABLE `mb_transfer` (
  `transfer_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cust_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `transfer_no` varchar(30) DEFAULT NULL COMMENT '转账单',
  `login_name` varchar(30) DEFAULT NULL COMMENT '游戏帐号',
  `transfer_type` varchar(10) DEFAULT NULL COMMENT 'IN/OUT转入与转出申博',
  `credit` decimal(10,2) DEFAULT NULL COMMENT '转账金额',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `create_user` varchar(30) DEFAULT NULL COMMENT '创建人',
  `status` int(11) DEFAULT NULL COMMENT '状态',
  `audit_date` datetime DEFAULT NULL COMMENT '审核时间',
  `audit_user` varchar(30) DEFAULT NULL COMMENT '审核人',
  `audit_msg` varchar(200) DEFAULT NULL COMMENT '审核信息',
  `remarks` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`transfer_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_transfer_log"
#

CREATE TABLE `mb_transfer_log` (
  `transfer_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cust_id` bigint(20) DEFAULT NULL,
  `credit` decimal(10,2) DEFAULT NULL COMMENT '转账额度',
  `transer_from` varchar(30) DEFAULT NULL COMMENT '来源',
  `transer_to` varchar(30) DEFAULT NULL COMMENT '目的',
  `status` int(11) DEFAULT NULL COMMENT '状态',
  `bill_no` varchar(30) DEFAULT NULL COMMENT '账单',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  `create_user` varchar(30) DEFAULT NULL COMMENT '创建人',
  `remarks` varchar(100) DEFAULT NULL COMMENT '备注',
  `login_name` varchar(30) DEFAULT NULL COMMENT '登录名',
  `finish_date` datetime DEFAULT NULL COMMENT '完成时间',
  PRIMARY KEY (`transfer_id`)
) ENGINE=MyISAM AUTO_INCREMENT=33066 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_user"
#

CREATE TABLE `mb_user` (
  `loginname` varchar(30) NOT NULL,
  `realname` varchar(50) DEFAULT NULL,
  `PASSWORD` varchar(80) DEFAULT NULL,
  `flag` tinyint(4) DEFAULT NULL,
  `issuper` tinyint(4) DEFAULT NULL,
  `productId` varchar(30) DEFAULT NULL,
  `rolecode` varchar(50) DEFAULT NULL,
  `createdate` datetime DEFAULT NULL,
  `createuser` varchar(30) DEFAULT NULL,
  `lastloginip` varchar(30) DEFAULT NULL,
  `lastlogindate` datetime DEFAULT NULL,
  `phonecode` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`loginname`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

#
# Structure for table "mb_user_log"
#

CREATE TABLE `mb_user_log` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `op_user` varchar(30) DEFAULT NULL COMMENT '操作人',
  `create_date` datetime DEFAULT NULL COMMENT '操作时间',
  `log_msg` varchar(500) DEFAULT NULL COMMENT '操作日志',
  `cust_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `login_name` varchar(30) DEFAULT NULL COMMENT '客户游戏名',
  PRIMARY KEY (`log_id`)
) ENGINE=MyISAM AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_user_msg"
#

CREATE TABLE `mb_user_msg` (
  `um_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `login_name` varchar(30) DEFAULT NULL COMMENT '用户名',
  `msg_id` bigint(20) DEFAULT NULL COMMENT '消息Id',
  `notify_flag` int(11) DEFAULT NULL COMMENT '是否已经提醒',
  `create_date` datetime DEFAULT NULL COMMENT '创建日期',
  PRIMARY KEY (`um_id`)
) ENGINE=MyISAM AUTO_INCREMENT=105324 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_win_list"
#

CREATE TABLE `mb_win_list` (
  `win_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `platform` varchar(50) DEFAULT NULL,
  `game_name` varchar(50) DEFAULT NULL,
  `login_name` varchar(16) DEFAULT NULL,
  `win_amount` decimal(10,2) DEFAULT NULL,
  `img_path` varchar(128) DEFAULT NULL,
  `upload_date` datetime DEFAULT NULL,
  `publish_date` datetime DEFAULT NULL,
  `publish_status` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`win_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5435 DEFAULT CHARSET=utf8mb4;

#
# Structure for table "mb_withdraw"
#

CREATE TABLE `mb_withdraw` (
  `withdraw_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `wit_no` varchar(30) DEFAULT NULL,
  `login_name` varchar(30) DEFAULT NULL,
  `real_name` varchar(30) DEFAULT NULL,
  `amount` decimal(10,2) DEFAULT NULL,
  `bank_name` varchar(30) DEFAULT NULL,
  `account_name` varchar(30) DEFAULT NULL,
  `account` varchar(30) DEFAULT NULL,
  `account_type` varchar(30) DEFAULT NULL,
  `location` varchar(100) DEFAULT NULL,
  `remarks` varchar(500) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `create_user` varchar(30) DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '5:手动支付成功/6:进入自动支付/7:自动支付中/8:自动支付失败/9:自动支付成功',
  `cust_id` bigint(20) DEFAULT NULL,
  `pay_date` datetime DEFAULT NULL,
  `locked` tinyint(1) DEFAULT '0',
  `wit_cnt` int(11) DEFAULT '0' COMMENT '提款次数',
  `dpay_no` varchar(64) DEFAULT NULL COMMENT '自动支付订单号',
  `dpay_pay_date` datetime DEFAULT NULL COMMENT '支付日期',
  `exact_charge` decimal(10,2) DEFAULT NULL COMMENT '实际手续费(手动支付，自动支付需要填写此项)',
  `error_msg` varchar(128) DEFAULT NULL COMMENT '支付错误信息',
  `bank_id` varchar(10) DEFAULT NULL COMMENT '银行代号',
  `bank_code` varchar(20) DEFAULT NULL COMMENT '银行编码',
  `dpay_exact_mount` decimal(10,2) DEFAULT NULL COMMENT '实际支付金额',
  PRIMARY KEY (`withdraw_id`)
) ENGINE=MyISAM AUTO_INCREMENT=3225 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_withdraw_log"
#

CREATE TABLE `mb_withdraw_log` (
  `log_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `pre_status` int(11) DEFAULT NULL,
  `after_status` int(11) DEFAULT NULL,
  `withdraw_id` bigint(20) DEFAULT NULL,
  `remarks` varchar(500) DEFAULT NULL,
  `create_date` datetime DEFAULT NULL,
  `create_user` varchar(30) DEFAULT NULL,
  `wit_no` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`log_id`)
) ENGINE=MyISAM AUTO_INCREMENT=9189 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_xfbp"
#

CREATE TABLE `mb_xfbp` (
  `xfbp_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pay_id` varchar(32) NOT NULL COMMENT '支付单号',
  `login_name` varchar(50) NOT NULL COMMENT '用户名',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '金额',
  `payip` varchar(100) DEFAULT NULL COMMENT 'ip',
  `pay_method` varchar(50) DEFAULT NULL COMMENT '支付渠道',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `order_no` varchar(64) DEFAULT NULL,
  `real_amount` decimal(10,2) DEFAULT NULL COMMENT '实际到账金额',
  `state` int(11) DEFAULT '0' COMMENT '2为支付成功',
  `modify_time` datetime DEFAULT NULL COMMENT '支付时间',
  `payer_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `real_pay_method` varchar(64) DEFAULT NULL COMMENT '实际支付类型',
  `finished_date` datetime DEFAULT NULL COMMENT '支付成功时间',
  `return_url` varchar(300) DEFAULT NULL COMMENT '返回的URL',
  PRIMARY KEY (`xfbp_id`),
  UNIQUE KEY `pay_id` (`pay_id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_xftp"
#

CREATE TABLE `mb_xftp` (
  `xftp_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pay_id` varchar(32) NOT NULL COMMENT '支付单号',
  `login_name` varchar(50) NOT NULL COMMENT '用户名',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '金额',
  `payip` varchar(100) DEFAULT NULL COMMENT 'ip',
  `pay_method` varchar(50) DEFAULT NULL COMMENT '支付渠道',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `order_no` varchar(64) DEFAULT NULL,
  `real_amount` decimal(10,2) DEFAULT NULL COMMENT '实际到账金额',
  `state` int(11) DEFAULT '0' COMMENT '2为支付成功',
  `modify_time` datetime DEFAULT NULL COMMENT '支付时间',
  `payer_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `real_pay_method` varchar(64) DEFAULT NULL COMMENT '实际支付类型',
  `finished_date` datetime DEFAULT NULL COMMENT '支付成功时间',
  `return_url` varchar(300) DEFAULT NULL COMMENT '返回的URL',
  PRIMARY KEY (`xftp_id`),
  UNIQUE KEY `pay_id` (`pay_id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_xinbeipay"
#

CREATE TABLE `mb_xinbeipay` (
  `xbp_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pay_id` varchar(32) NOT NULL COMMENT '支付单号',
  `login_name` varchar(50) NOT NULL COMMENT '用户名',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '金额',
  `payip` varchar(100) DEFAULT NULL COMMENT 'ip',
  `pay_method` varchar(50) DEFAULT NULL COMMENT '支付渠道',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `order_no` varchar(64) DEFAULT NULL,
  `real_amount` decimal(10,2) DEFAULT NULL COMMENT '实际到账金额',
  `state` int(11) DEFAULT '0' COMMENT '2为支付成功',
  `modify_time` datetime DEFAULT NULL COMMENT '支付时间',
  `payer_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `real_pay_method` varchar(64) DEFAULT NULL COMMENT '实际支付类型',
  `finished_date` datetime DEFAULT NULL COMMENT '支付成功时间',
  `return_url` varchar(300) DEFAULT NULL COMMENT '返回的URL',
  PRIMARY KEY (`xbp_id`),
  UNIQUE KEY `pay_id` (`pay_id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_ybp"
#

CREATE TABLE `mb_ybp` (
  `ybp_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pay_id` varchar(32) NOT NULL COMMENT '支付单号',
  `login_name` varchar(50) NOT NULL COMMENT '用户名',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '金额',
  `payip` varchar(100) DEFAULT NULL COMMENT 'ip',
  `pay_method` varchar(50) DEFAULT NULL COMMENT '支付渠道',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `order_no` varchar(64) DEFAULT NULL,
  `real_amount` decimal(10,2) DEFAULT NULL COMMENT '实际到账金额',
  `state` int(11) DEFAULT '0' COMMENT '2为支付成功',
  `modify_time` datetime DEFAULT NULL COMMENT '支付时间',
  `payer_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `real_pay_method` varchar(64) DEFAULT NULL COMMENT '实际支付类型',
  `finished_date` datetime DEFAULT NULL COMMENT '支付成功时间',
  `return_url` varchar(300) DEFAULT NULL COMMENT '返回的URL',
  PRIMARY KEY (`ybp_id`),
  UNIQUE KEY `pay_id` (`pay_id`)
) ENGINE=InnoDB AUTO_INCREMENT=98 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_year_gift"
#

CREATE TABLE `mb_year_gift` (
  `year_gift_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `day` varchar(10) NOT NULL COMMENT '期-YYYYMMDDHH',
  `cust_id` bigint(20) NOT NULL COMMENT '用户ID',
  `login_name` varchar(50) NOT NULL COMMENT '用户名',
  `credit` decimal(10,2) NOT NULL COMMENT '红包金额',
  `create_date` datetime NOT NULL COMMENT '抢中时间',
  `gift_no` varchar(40) NOT NULL COMMENT '编号',
  `flag` tinyint(4) DEFAULT '0' COMMENT '派发是否成功(0:未派发，1:已派发,2:派发失败)',
  PRIMARY KEY (`year_gift_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#
# Structure for table "mb_yinbbaopay"
#

CREATE TABLE `mb_yinbbaopay` (
  `ybp_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pay_id` varchar(32) NOT NULL COMMENT '支付单号',
  `login_name` varchar(50) NOT NULL COMMENT '用户名',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '金额',
  `payip` varchar(100) DEFAULT NULL COMMENT 'ip',
  `pay_method` varchar(50) DEFAULT NULL COMMENT '支付渠道',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `order_no` varchar(64) DEFAULT NULL,
  `real_amount` decimal(10,2) DEFAULT NULL COMMENT '实际到账金额',
  `state` int(11) DEFAULT '0' COMMENT '2为支付成功',
  `modify_time` datetime DEFAULT NULL COMMENT '支付时间',
  `payer_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `real_pay_method` varchar(64) DEFAULT NULL COMMENT '实际支付类型',
  `finished_date` datetime DEFAULT NULL COMMENT '支付成功时间',
  `return_url` varchar(300) DEFAULT NULL COMMENT '返回的URL',
  PRIMARY KEY (`ybp_id`),
  UNIQUE KEY `pay_id` (`pay_id`)
) ENGINE=InnoDB AUTO_INCREMENT=206 DEFAULT CHARSET=utf8;

#
# Structure for table "mb_ytbp"
#

CREATE TABLE `mb_ytbp` (
  `ytbp_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pay_id` varchar(32) NOT NULL COMMENT '支付单号',
  `login_name` varchar(50) NOT NULL COMMENT '用户名',
  `amount` decimal(10,2) DEFAULT NULL COMMENT '金额',
  `payip` varchar(100) DEFAULT NULL COMMENT 'ip',
  `pay_method` varchar(50) DEFAULT NULL COMMENT '支付渠道',
  `remark` varchar(200) DEFAULT NULL COMMENT '备注',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `order_no` varchar(64) DEFAULT NULL,
  `real_amount` decimal(10,2) DEFAULT NULL COMMENT '实际到账金额',
  `state` int(11) DEFAULT '0' COMMENT '2为支付成功',
  `modify_time` datetime DEFAULT NULL COMMENT '支付时间',
  `payer_name` varchar(50) DEFAULT NULL COMMENT '用户名',
  `real_pay_method` varchar(64) DEFAULT NULL COMMENT '实际支付类型',
  `finished_date` datetime DEFAULT NULL COMMENT '支付成功时间',
  `return_url` varchar(300) DEFAULT NULL COMMENT '返回的URL',
  PRIMARY KEY (`ytbp_id`),
  UNIQUE KEY `pay_id` (`pay_id`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8;

#
# Structure for table "pt_game"
#

CREATE TABLE `pt_game` (
  `pt_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `game_name` varchar(200) DEFAULT NULL COMMENT '游戏名字',
  `game_type` varchar(100) DEFAULT NULL COMMENT '游戏类别',
  `progressive` varchar(100) DEFAULT NULL COMMENT '奖池',
  `branded` varchar(100) DEFAULT NULL COMMENT '品牌',
  `game_code` varchar(100) DEFAULT NULL COMMENT '游戏代码',
  `client` varchar(20) DEFAULT NULL COMMENT '客户端',
  `flash` varchar(20) DEFAULT NULL COMMENT '网页版',
  `mobile` varchar(20) DEFAULT NULL COMMENT '是否支持手机',
  `cn_name` varchar(200) DEFAULT NULL COMMENT '中文名',
  `pool_name` varchar(100) DEFAULT NULL COMMENT '奖池名字',
  `hot` tinyint(4) DEFAULT NULL COMMENT '是否热',
  `recommend` tinyint(4) DEFAULT '0' COMMENT '是否推荐',
  `enable` tinyint(4) DEFAULT '1' COMMENT '是否可用',
  PRIMARY KEY (`pt_id`)
) ENGINE=InnoDB AUTO_INCREMENT=323 DEFAULT CHARSET=utf8;

#
# Structure for table "test"
#

CREATE TABLE `test` (
  `a` char(1) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

#
# Procedure "proc_month_commission"
#

CREATE PROCEDURE `proc_month_commission`(IN agent_login_name VARCHAR(50),IN end_date VARCHAR(8))
BEGIN
         DECLARE Done INT DEFAULT 0;
	 DECLARE temp_agent_id BIGINT (20);
	 DECLARE temp_login_name VARCHAR (50);
	 DECLARE temp_start_date VARCHAR (8);
	 
	 DECLARE rs CURSOR FOR SELECT agent_id, login_name, commission_date FROM mb_agent WHERE flag=3;
	 
	 DECLARE CONTINUE HANDLER FOR SQLSTATE '02000' SET Done = 1;
	  
	 IF end_date IS NULL  THEN 
		 SET end_date = DATE_FORMAT(DATE_SUB(DATE_SUB(DATE_FORMAT(NOW(),'%Y%m%d'),INTERVAL EXTRACT( DAY FROM NOW()) DAY),INTERVAL 0 MONTH),'%Y%m%d');
	 END IF;
	 
	 SET end_date =IF(STR_TO_DATE(end_date, '%Y%m%d')>DATE_FORMAT(NOW(),'%Y%m%d'),DATE_FORMAT(NOW(),'%Y%m%d'),end_date);
	 
	 OPEN rs;  
	 
	 FETCH NEXT FROM rs INTO temp_agent_id, temp_login_name,temp_start_date;     
	 
	 REPEAT
	     IF NOT Done THEN 
	       IF agent_login_name IS NOT NULL THEN
	            IF agent_login_name = temp_login_name THEN
                          INSERT INTO mb_agent_commission (agent_id,login_name,create_date,create_user,STATUS,credit,SHARE,total_deposit,total_withdraw,total_gift,total_cost,active,start_date,end_date,com_month,com_year)
				 SELECT tt1.agent_id,tt1.login_name,NOW(),'system',0,(((tt2.dc-tt2.wc)*0.9-tt2.gc*0.1)*SHARE)/100,SHARE,tt2.dc,tt2.wc,tt2.gc,0,tt3.ac,temp_start_date,end_date, DATE_FORMAT(STR_TO_DATE(end_date, '%Y%m%d'),'%m'), DATE_FORMAT(STR_TO_DATE(end_date, '%Y%m%d'),'%Y') FROM mb_agent tt1 LEFT JOIN (
				     SELECT agent_id,IF(SUM(deposit_collect) IS NULL,0,SUM(deposit_collect)) AS dc,IF(SUM(withdraw_collect) IS NULL,0,SUM(withdraw_collect)) AS wc,IF(SUM(gift_collect) IS NULL,0,SUM(gift_collect)) AS gc FROM mb_stat 
					    WHERE agent_id IS NOT NULL AND stat_date> temp_start_date AND  stat_date<=end_date AND agent_id=temp_agent_id 
					    GROUP BY agent_id
				) tt2 
				ON tt1.agent_id = tt2.agent_id
				LEFT JOIN(
				    SELECT agent_id,IF(COUNT(1) IS NULL,0,COUNT(1)) AS ac FROM (
				       SELECT cust_id,agent_id,IF(SUM(deposit_collect) IS NULL,0,SUM(deposit_collect)) AS cd FROM mb_stat 
					     WHERE agent_id IS NOT NULL AND stat_date> temp_start_date AND stat_date<=end_date AND agent_id=temp_agent_id 
					     GROUP BY agent_id,cust_id
				) t1 WHERE t1.cd >2000 GROUP BY agent_id
				)tt3 
				ON tt1.agent_id =tt3.agent_id WHERE  tt1.agent_id=temp_agent_id ;
	            END IF;
	       ELSE
	            INSERT INTO mb_agent_commission (agent_id,login_name,create_date,create_user,STATUS,credit,SHARE,total_deposit,total_withdraw,total_gift,total_cost,active,start_date,end_date,com_month,com_year)
				 SELECT tt1.agent_id,tt1.login_name,NOW(),'system',0,(((tt2.dc-tt2.wc)*0.9-tt2.gc*0.1)*SHARE)/100,SHARE,tt2.dc,tt2.wc,tt2.gc,0,tt3.ac,temp_start_date,end_date, DATE_FORMAT(STR_TO_DATE(end_date, '%Y%m%d'),'%m'), DATE_FORMAT(STR_TO_DATE(end_date, '%Y%m%d'),'%Y') FROM mb_agent tt1 LEFT JOIN (
				     SELECT agent_id,IF(SUM(deposit_collect) IS NULL,0,SUM(deposit_collect)) AS dc,IF(SUM(withdraw_collect) IS NULL,0,SUM(withdraw_collect)) AS wc,IF(SUM(gift_collect) IS NULL,0,SUM(gift_collect)) AS gc FROM mb_stat 
					    WHERE agent_id IS NOT NULL AND stat_date> temp_start_date AND  stat_date<=end_date AND agent_id=temp_agent_id 
					    GROUP BY agent_id
				) tt2 
				ON tt1.agent_id = tt2.agent_id
				LEFT JOIN(
				    SELECT agent_id,IF(COUNT(1) IS NULL,0,COUNT(1)) AS ac FROM (
				       SELECT cust_id,agent_id,IF(SUM(deposit_collect) IS NULL,0,SUM(deposit_collect)) AS cd FROM mb_stat 
					     WHERE agent_id IS NOT NULL AND stat_date> temp_start_date AND stat_date<=end_date AND agent_id=temp_agent_id 
					     GROUP BY agent_id,cust_id
				) t1 WHERE t1.cd >2000 GROUP BY agent_id
				)tt3 
				ON tt1.agent_id =tt3.agent_id WHERE  tt1.agent_id=temp_agent_id ;
	       END IF;
	     END IF;
	FETCH NEXT FROM rs INTO temp_agent_id, temp_login_name,temp_start_date; 
	UNTIL Done END REPEAT;
	CLOSE rs;
END;

#
# Procedure "proc_stats"
#

CREATE PROCEDURE `proc_stats`(IN stat_day VARCHAR(8))
BEGIN
  DECLARE deposit_collect DECIMAL (10,2);
  DECLARE withdraw_collect DECIMAL (10,2);
  DECLARE gift_collect DECIMAL (10,2);
  DECLARE deposit_count INT (11);
  DECLARE withdraw_count INT (11);
  DECLARE gift_count INT (11);
  
 IF stat_day IS NULL THEN
   SET stat_day = DATE_FORMAT(ADDDATE(NOW(),-1),'%Y%m%d');
 END IF;
 DELETE FROM mb_stat WHERE stat_date =stat_day;
 INSERT INTO mb_stat(cust_id,agent_id,login_name,stat_date,create_date,deposit_collect,withdraw_collect,gift_collect,deposit_count,withdraw_count,gift_count) 
   SELECT cust_id,parent_id,login_name,stat_day,NOW(),IF(tt1.a1 IS NULL,0,tt1.a1),IF(tt2.a3 IS NULL,0,tt2.a3),IF(tt3.a5 IS NULL,0,tt3.a5),IF(tt1.a2 IS NULL,0,tt1.a2),IF(tt2.a4 IS NULL,0,tt2.a4),IF(tt3.a6 IS NULL,0,tt3.a6) 
   FROM mb_customer tt0 
 LEFT JOIN (
 SELECT SUM(t1.amount) AS a1 ,COUNT(1) AS a2,t1.cust_id AS c1 FROM mb_deposit t1 LEFT JOIN mb_deposit_log t2 ON t1.deposit_id =t2.deposit_id  WHERE STATUS =3 
 AND  t2.deposit_id IS NOT NULL AND t2.after_status=3 AND DATE_FORMAT(t2.create_date,'%Y%m%d') =stat_day GROUP BY t1.cust_id 
 ) tt1
 ON tt0.cust_id=tt1.c1
 LEFT JOIN(
 SELECT SUM(t1.amount) AS a3 ,COUNT(1) AS a4,t1.cust_id AS c2 FROM mb_withdraw t1 LEFT JOIN mb_withdraw_log t2 ON t1.withdraw_id =t2.withdraw_id  WHERE STATUS =5 
 AND  t2.withdraw_id IS NOT NULL AND t2.after_status=5 AND DATE_FORMAT(t2.create_date,'%Y%m%d') =stat_day GROUP BY t1.cust_id 
 ) tt2
 ON tt0.cust_id=tt2.c2
 LEFT JOIN(
 SELECT SUM(payout) a5,COUNT(1) AS a6,cust_id AS c3 FROM mb_cash_gift WHERE STATUS =3 AND DATE_FORMAT(audit_date,'%Y%m%d') =stat_day GROUP BY  cust_id 
 ) tt3
ON tt0.cust_id=tt3.c3
WHERE  0=(tt1.a1 IS NULL AND tt1.a2 IS NULL  AND tt2.a3 IS  NULL AND tt2.a4 IS  NULL AND tt3.a5 IS  NULL AND tt3.a6 IS NULL);
END;
