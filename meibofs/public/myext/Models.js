/*
 *  角色定义
 */
Ext.define('Role', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'rolecode',
		type : 'string'
	}, {
		name : 'rolename',
		type : 'string'
	}, {
		name : 'createdate',
		type : 'string'
	}, {
		name : 'createuser',
		type : 'string'
	} ],
	validations : [ {
		type : 'length',
		field : 'rolecode',
		min : 1
	}, {
		type : 'length',
		field : 'rolename',
		min : 1
	}, {
		type : 'length',
		field : 'createdate',
		min : 1
	}, {
		type : 'length',
		field : 'createuser',
		min : 1
	} ]
});

Ext.define('Func', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'funccode',
		type : 'string'
	}, {
		name : 'funcname',
		type : 'string'
	}, {
		name : 'pfunccode',
		type : 'string'
	}, {
		name : 'isgroup',
		type : 'string'
	}, {
		name : 'createdate',
		type : 'string'
	}, {
		name : 'createuser',
		type : 'string'
	}, {
		name : 'url',
		type : 'string'
	}, {
		name : 'isbut',
		type : 'string'
	}, {
		name : 'icon',
		type : 'string'
	}

	],
	validations : [ {
		type : 'length',
		field : 'funccode',
		min : 1
	}, {
		type : 'length',
		field : 'funcname',
		min : 1
	}, {
		type : 'length',
		field : 'pfunccode',
		min : 1
	}, {
		type : 'length',
		field : 'isgroup',
		min : 1
	}, {
		type : 'length',
		field : 'createdate',
		min : 1
	}, {
		type : 'length',
		field : 'createuser',
		min : 1
	}, {
		type : 'length',
		field : 'url',
		min : 1
	}, {
		type : 'length',
		field : 'isbut',
		min : 1
	}, ]
});

Ext.define('User', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'loginname',
		type : 'string'
	}, {
		name : 'password',
		type : 'string'
	}, {
		name : 'flag',
		type : 'string'
	}, {
		name : 'issuper',
		type : 'string'
	}, {
		name : 'productid',
		type : 'string'
	}, {
		name : 'lastloginip',
		type : 'string'
	}, {
		name : 'rolecode',
		type : 'string'
	}, {
		name : 'createdate',
		type : 'string'
	}, {
		name : 'createuser',
		type : 'string'
	}, {
		name : 'lastlogindate',
		type : 'string'
	}, {
		name : 'realname',
		type : 'string'
	},

	],
	validations : [ {
		type : 'length',
		field : 'loginname',
		min : 1
	}, {
		type : 'length',
		field : 'password',
		min : 1
	}, {
		type : 'length',
		field : 'flag',
		min : 1
	}, {
		type : 'length',
		field : 'issuper',
		min : 1
	}, {
		type : 'length',
		field : 'productid',
		min : 1
	}, {
		type : 'length',
		field : 'lastloginip',
		min : 1
	}, {
		type : 'length',
		field : 'rolecode',
		min : 1
	}, {
		type : 'length',
		field : 'createdate',
		min : 1
	}, {
		type : 'length',
		field : 'createuser',
		min : 1
	}, {
		type : 'length',
		field : 'lastlogindate',
		min : 1
	}, {
		type : 'length',
		field : 'realname',
		min : 1
	}, ]
});

Ext.define('Item', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'itemname',
		type : 'string'
	}, {
		name : 'itemcode',
		type : 'string'
	}, {
		name : 'itemvalue',
		type : 'string'
	}, {
		name : 'groupcode',
		type : 'string'
	}, {
		name : 'createdate',
		type : 'string'
	}, {
		name : 'createuser',
		type : 'string'
	}, {
		name : 'platform',
		type : 'string'
	}, {
		name : 'frozenflag',
		type : 'string'
	}, {
		name : 'startdate',
		type : 'date'
	}, {
		name : 'enddate',
		type : 'date'
	}, {
		name : 'pcflag',
		type : 'string'
	}, {
		name : 'mobileflag',
		type : 'string'
	}, {
		name : 'actbill',
		type : 'string'
	}, {
		name : 'moneyflag',
		type : 'string'
	}

	]
});

Ext.define('UserLog', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'log_id',
		type : 'string'
	}, {
		name : 'op_user',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'log_msg',
		type : 'string'
	}

	]
});


Ext.define('Billboard', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'board_id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'bet_amount',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'flag',
		type : 'string'
	}

	]
});

Ext.define('JvBao', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'jbp_id',
		type : 'string'
	}, {
		name : 'pay_id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'amount',
		type : 'string'
	}, {
		name : 'goods_name',
		type : 'string'
	}, {
		name : 'partner_id',
		type : 'string'
	}, {
		name : 'pay_method',
		type : 'string'
	}, {
		name : 'remark',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'order_no',
		type : 'string'
	}, {
		name : 'real_amount',
		type : 'string'
	}, {
		name : 'state',
		type : 'string'
	}, {
		name : 'modify_time',
		type : 'string'
	}, {
		name : 'payer_name',
		type : 'string'
	}, {
		name : 'real_pay_method',
		type : 'string'
	}, {
		name : 'finished_date',
		type : 'string'
	}

	]
});


Ext.define('YingBao', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'ybp_id',
		type : 'string'
	}, {
		name : 'pay_id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'amount',
		type : 'string'
	}, {
		name : 'payip',
		type : 'string'
	}, {
		name : 'pay_method',
		type : 'string'
	}, {
		name : 'remark',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'order_no',
		type : 'string'
	}, {
		name : 'real_amount',
		type : 'string'
	}, {
		name : 'state',
		type : 'string'
	}, {
		name : 'modify_time',
		type : 'string'
	}, {
		name : 'payer_name',
		type : 'string'
	}, {
		name : 'real_pay_method',
		type : 'string'
	}, {
		name : 'finished_date',
		type : 'string'
	}

	]
});

Ext.define('HuiTianFu', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'htp_id',
		type : 'string'
	}, {
		name : 'pay_id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'amount',
		type : 'string'
	}, {
		name : 'payip',
		type : 'string'
	}, {
		name : 'pay_method',
		type : 'string'
	}, {
		name : 'remark',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'order_no',
		type : 'string'
	}, {
		name : 'real_amount',
		type : 'string'
	}, {
		name : 'state',
		type : 'string'
	}, {
		name : 'modify_time',
		type : 'string'
	}, {
		name : 'payer_name',
		type : 'string'
	}, {
		name : 'real_pay_method',
		type : 'string'
	}, {
		name : 'finished_date',
		type : 'string'
	}, {
		name : 'return_url',
		type : 'string'
	}

	]
});
Ext.define('YinBao', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'ybp_id',
		type : 'string'
	}, {
		name : 'pay_id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'amount',
		type : 'string'
	}, {
		name : 'payip',
		type : 'string'
	}, {
		name : 'pay_method',
		type : 'string'
	}, {
		name : 'remark',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'order_no',
		type : 'string'
	}, {
		name : 'real_amount',
		type : 'string'
	}, {
		name : 'state',
		type : 'string'
	}, {
		name : 'modify_time',
		type : 'string'
	}, {
		name : 'payer_name',
		type : 'string'
	}, {
		name : 'real_pay_method',
		type : 'string'
	}, {
		name : 'finished_date',
		type : 'string'
	}

	]
});


Ext.define('SanV', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'svp_id',
		type : 'string'
	}, {
		name : 'pay_id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'amount',
		type : 'string'
	}, {
		name : 'payip',
		type : 'string'
	}, {
		name : 'pay_method',
		type : 'string'
	}, {
		name : 'remark',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'order_no',
		type : 'string'
	}, {
		name : 'real_amount',
		type : 'string'
	}, {
		name : 'state',
		type : 'string'
	}, {
		name : 'modify_time',
		type : 'string'
	}, {
		name : 'payer_name',
		type : 'string'
	}, {
		name : 'real_pay_method',
		type : 'string'
	}, {
		name : 'finished_date',
		type : 'string'
	}

	]
});

Ext.define('XinBei', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'xbp_id',
		type : 'string'
	}, {
		name : 'pay_id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'amount',
		type : 'string'
	}, {
		name : 'payip',
		type : 'string'
	}, {
		name : 'pay_method',
		type : 'string'
	}, {
		name : 'remark',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'order_no',
		type : 'string'
	}, {
		name : 'real_amount',
		type : 'string'
	}, {
		name : 'state',
		type : 'string'
	}, {
		name : 'modify_time',
		type : 'string'
	}, {
		name : 'payer_name',
		type : 'string'
	}, {
		name : 'real_pay_method',
		type : 'string'
	}, {
		name : 'finished_date',
		type : 'string'
	}

	]
});

Ext.define('XunHuiBao', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'xfbp_id',
		type : 'string'
	}, {
		name : 'pay_id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'amount',
		type : 'string'
	}, {
		name : 'payip',
		type : 'string'
	}, {
		name : 'pay_method',
		type : 'string'
	}, {
		name : 'remark',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'order_no',
		type : 'string'
	}, {
		name : 'real_amount',
		type : 'string'
	}, {
		name : 'state',
		type : 'string'
	}, {
		name : 'modify_time',
		type : 'string'
	}, {
		name : 'payer_name',
		type : 'string'
	}, {
		name : 'real_pay_method',
		type : 'string'
	}, {
		name : 'finished_date',
		type : 'string'
	}

	]
});


Ext.define('YingTongBao', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'ytbp_id',
		type : 'string'
	}, {
		name : 'pay_id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'amount',
		type : 'string'
	}, {
		name : 'payip',
		type : 'string'
	}, {
		name : 'pay_method',
		type : 'string'
	}, {
		name : 'remark',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'order_no',
		type : 'string'
	}, {
		name : 'real_amount',
		type : 'string'
	}, {
		name : 'state',
		type : 'string'
	}, {
		name : 'modify_time',
		type : 'string'
	}, {
		name : 'payer_name',
		type : 'string'
	}, {
		name : 'real_pay_method',
		type : 'string'
	}, {
		name : 'finished_date',
		type : 'string'
	}

	]
});

Ext.define('DingYi', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'dybp_id',
		type : 'string'
	}, {
		name : 'pay_id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'amount',
		type : 'string'
	}, {
		name : 'payip',
		type : 'string'
	}, {
		name : 'pay_method',
		type : 'string'
	}, {
		name : 'remark',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'order_no',
		type : 'string'
	}, {
		name : 'real_amount',
		type : 'string'
	}, {
		name : 'state',
		type : 'string'
	}, {
		name : 'modify_time',
		type : 'string'
	}, {
		name : 'payer_name',
		type : 'string'
	}, {
		name : 'real_pay_method',
		type : 'string'
	}, {
		name : 'finished_date',
		type : 'string'
	}

	]
});


Ext.define('SaoMaFu', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'smfp_id',
		type : 'string'
	}, {
		name : 'pay_id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'amount',
		type : 'string'
	}, {
		name : 'payip',
		type : 'string'
	}, {
		name : 'pay_method',
		type : 'string'
	}, {
		name : 'remark',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'order_no',
		type : 'string'
	}, {
		name : 'real_amount',
		type : 'string'
	}, {
		name : 'state',
		type : 'string'
	}, {
		name : 'modify_time',
		type : 'string'
	}, {
		name : 'payer_name',
		type : 'string'
	}, {
		name : 'real_pay_method',
		type : 'string'
	}, {
		name : 'finished_date',
		type : 'string'
	}

	]
});


Ext.define('XunFuTong', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'xftp_id',
		type : 'string'
	}, {
		name : 'pay_id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'amount',
		type : 'string'
	}, {
		name : 'payip',
		type : 'string'
	}, {
		name : 'pay_method',
		type : 'string'
	}, {
		name : 'remark',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'order_no',
		type : 'string'
	}, {
		name : 'real_amount',
		type : 'string'
	}, {
		name : 'state',
		type : 'string'
	}, {
		name : 'modify_time',
		type : 'string'
	}, {
		name : 'payer_name',
		type : 'string'
	}, {
		name : 'real_pay_method',
		type : 'string'
	}, {
		name : 'finished_date',
		type : 'string'
	}

	]
});

Ext.define('Rpn', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'rpn_id',
		type : 'string'
	}, {
		name : 'order_id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'amount',
		type : 'string'
	}, {
		name : 'customer_ip',
		type : 'string'
	}, {
		name : 'bank_id',
		type : 'string'
	}, {
		name : 'remark',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'rpn_order_no',
		type : 'string'
	}, {
		name : 'real_amount',
		type : 'string'
	}, {
		name : 'state',
		type : 'string'
	}, {
		name : 'modify_time',
		type : 'string'
	}, {
		name : 'payer_name',
		type : 'string'
	}, {
		name : 'real_pay_method',
		type : 'string'
	}, {
		name : 'finished_date',
		type : 'string'
	}

	]
});

Ext.define('HuiBo', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'hbp_id',
		type : 'string'
	}, {
		name : 'pay_id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'amount',
		type : 'string'
	}, {
		name : 'product_name',
		type : 'string'
	}, {
		name : 'bank_code',
		type : 'string'
	}, {
		name : 'remark',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'order_no',
		type : 'string'
	}, {
		name : 'real_amount',
		type : 'string'
	}, {
		name : 'state',
		type : 'string'
	}, {
		name : 'modify_time',
		type : 'string'
	}, {
		name : 'payer_name',
		type : 'string'
	}, {
		name : 'real_pay_method',
		type : 'string'
	}, {
		name : 'finished_date',
		type : 'string'
	}

	]
});

Ext.define('HuiBoOther', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'id',
		type : 'int'
	}, {
		name : 'account',
		type : 'string'
	}, {
		name : 'orderId',
		type : 'string'
	}, {
		name : 'accNo',
		type : 'string'
	}, {
		name : 'accName',
		type : 'string'
	}, {
		name : 'bankName',
		type : 'string'
	}, {
		name : 'amount',
		type : 'string'
	}, {
		name : 'payPwd',
		type : 'string'
	}, {
		name : 'status',
		type : 'string'
	}, {
		name : 'create_user',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'remark',
		type : 'string'
	}
	]
});

Ext.define('TongHui', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'thp_id',
		type : 'string'
	}, {
		name : 'pay_id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'amount',
		type : 'string'
	}, {
		name : 'product_name',
		type : 'string'
	}, {
		name : 'bank_code',
		type : 'string'
	}, {
		name : 'remark',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'order_no',
		type : 'string'
	}, {
		name : 'real_amount',
		type : 'string'
	}, {
		name : 'state',
		type : 'string'
	}, {
		name : 'modify_time',
		type : 'string'
	}, {
		name : 'payer_name',
		type : 'string'
	}, {
		name : 'real_pay_method',
		type : 'string'
	}, {
		name : 'finished_date',
		type : 'string'
	}

	]
});



Ext.define('DpayDepositExp', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'dpay_id',
		type : 'string'
	}, {
		name : 'exception_order_num',
		type : 'string'
	}, {
		name : 'company_id',
		type : 'string'
	}, {
		name : 'exact_payment_bank',
		type : 'string'
	}, {
		name : 'pay_card_name',
		type : 'string'
	}, {
		name : 'pay_card_num',
		type : 'string'
	}, {
		name : 'receiving_bank',
		type : 'string'
	}, {
		name : 'receiving_account_name',
		type : 'string'
	}, {
		name : 'channel',
		type : 'string'
	}, {
		name : 'note',
		type : 'string'
	}, {
		name : 'area',
		type : 'string'
	}, {
		name : 'exact_time',
		type : 'string'
	}, {
		name : 'amount',
		type : 'string'
	}, {
		name : 'fee',
		type : 'string'
	}, {
		name : 'transaction_charge',
		type : 'string'
	}, {
		name : 'base_info',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'flag',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'action_user',
		type : 'string'
	}, {
		name : 'claim_date',
		type : 'string'
	}, {
		name : 'pic_id',
		type : 'string'
	}, {
		name : 'action_user2',
		type : 'string'
	}, {
		name : 'claim_date2',
		type : 'string'
	}, {
		name : 'remark',
		type : 'string'
	}

	]
});


Ext.define('Customer', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'cust_id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'login_pwd',
		type : 'string'
	}, {
		name : 'real_name',
		type : 'string'
	}, {
		name : 'phone',
		type : 'string'
	}, {
		name : 'email',
		type : 'string'
	}, {
		name : 'qq',
		type : 'string'
	}, {
		name : 'reg_ip',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'create_user',
		type : 'string'
	}, {
		name : 'login_ip',
		type : 'string'
	}, {
		name : 'login_date',
		type : 'string'
	}, {
		name : 'login_times',
		type : 'string'
	}, {
		name : 'sb_game',
		type : 'string'
	}, {
		name : 'sb_pwd',
		type : 'string'
	}, {
		name : 'sb_flag',
		type : 'string'
	}, {
		name : 'sb_actived',
		type : 'string'
	}, {
		name : 'ag_game',
		type : 'string'
	}, {
		name : 'ag_pwd',
		type : 'string'
	}, {
		name : 'ag_flag',
		type : 'string'
	}, {
		name : 'ag_actived',
		type : 'string'
	}, {
		name : 'bbin_game',
		type : 'string'
	}, {
		name : 'bbin_pwd',
		type : 'string'
	}, {
		name : 'bbin_flag',
		type : 'string'
	}, {
		name : 'bbin_actived',
		type : 'string'
	}, {
		name : 'bbinmobile_flag',
		type : 'string'
	}, {
		name : 'bbinmobile_game',
		type : 'string'
	},{
		name : 'pt_flag',
		type : 'string'
	}, {
		name : 'pt_actived',
		type : 'string'
	},{
		name : 'kg_game',
		type : 'string'
	}, {
		name : 'kg_pwd',
		type : 'string'
	}, {
		name : 'kg_flag',
		type : 'string'
	}, {
		name : 'kg_actived',
		type : 'string'
	},{
		name : 'pp_game',
		type : 'string'
	}, {
		name : 'pp_pwd',
		type : 'string'
	}, {
		name : 'pp_flag',
		type : 'string'
	}, {
		name : 'pp_actived',
		type : 'string'
	}, {
		name : 'mg_flag',
		type : 'string'
	}, {
		name : 'mg_actived',
		type : 'string'
	},{
		name : 'credit',
		type : 'string'
	}, {
		name : 'flag',
		type : 'string'
	}, {
		name : 'cust_level',
		type : 'string'
	}, {
		name : 'is_agent',
		type : 'string'
	}, {
		name : 'parent_id',
		type : 'string'
	}, {
		name : 'parent_name',
		type : 'string'
	}, {
		name : 'reg_source',
		type : 'string'
	}, {
		name : 'agent_dm',
		type : 'string'
	}, {
		name : 'first_deposit_date',
		type : 'string'
	}, {
		name : 'remarks',
		type : 'string'
	}, {
		name : 'bank_name',
		type : 'string'
	}, {
		name : 'account_type',
		type : 'string'
	}, {
		name : 'bank_dot',
		type : 'string'
	}, {
		name : 'account',
		type : 'string'
	}, {
		name : 'agent_mode',
		type : 'string'
	}, {
		name : 's_email',
		type : 'string'
	}, {
		name : 'ip_addr',
		type : 'string'
	}, {
		name : 'score',
		type : 'string'
	},{
		name : 'online_pay',
		type : 'string'
	}, {
		name : 'is_block',
		type : 'string'
	},{
		name : 'alipay_flag',
		type : 'string'
	},{
		name : 'promo_flag',
		type : 'string'
	},{
		name : 'reg_info',
		type : 'string'
	},{
		name : 'bank_pay',
		type : 'string'
	}]
});
Ext.define('Deposit', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'deposit_id',
		type : 'string'
	}, {
		name : 'dep_no',
		type : 'string'
	}, {
		name : 'cust_id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'real_name',
		type : 'string'
	}, {
		name : 'amount',
		type : 'string'
	}, {
		name : 'poundage',
		type : 'string'
	}, {
		name : 'pdage_status',
		type : 'string'
	}, {
		name : 'status',
		type : 'string'
	}, {
		name : 'bank_name',
		type : 'string'
	}, {
		name : 'account_name',
		type : 'string'
	}, {
		name : 'deposit_type',
		type : 'string'
	}, {
		name : 'location',
		type : 'string'
	}, {
		name : 'deposit_date',
		type : 'string'
	}, {
		name : 'remarks',
		type : 'string'
	}, {
		name : 'pic_id',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'create_user',
		type : 'string'
	}, {
		name : 'ip',
		type : 'string'
	}, {
		name : 'audit_date',
		type : 'string'
	}, {
		name : 'sb_game',
		type : 'string'
	}, {
		name : 'is_sb',
		type : 'string'
	}, {
		name : 'order_no',
		type : 'string'
	}, {
		name : 'locked',
		type : 'string'
	}, {
		name : 'locked_date',
		type : 'string'
	}

	]
});
Ext.define('CreditLog', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'log_id',
		type : 'string'
	}, {
		name : 'log_type',
		type : 'string'
	}, {
		name : 'credit',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'cust_id',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'create_user',
		type : 'string'
	}, {
		name : 'remarks',
		type : 'string'
	}, {
		name : 'order_no',
		type : 'string'
	}, {
		name : 'org_credit',
		type : 'string'
	}, {
		name : 'after_credit',
		type : 'string'
	} ]
});

Ext.define('AgentCreditLog', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'log_id',
		type : 'string'
	}, {
		name : 'log_type',
		type : 'string'
	}, {
		name : 'credit',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'cust_id',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'create_user',
		type : 'string'
	}, {
		name : 'remarks',
		type : 'string'
	}, {
		name : 'order_no',
		type : 'string'
	}, {
		name : 'org_credit',
		type : 'string'
	}, {
		name : 'after_credit',
		type : 'string'
	} ]
});

Ext.define('Withdraw', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'withdraw_id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'real_name',
		type : 'string'
	}, {
		name : 'amount',
		type : 'string'
	}, {
		name : 'bank_name',
		type : 'string'
	}, {
		name : 'account_name',
		type : 'string'
	}, {
		name : 'account',
		type : 'string'
	}, {
		name : 'account_type',
		type : 'string'
	}, {
		name : 'location',
		type : 'string'
	}, {
		name : 'remarks',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'create_user',
		type : 'string'
	}, {
		name : 'status',
		type : 'string'
	}, {
		name : 'wit_no',
		type : 'string'
	}, {
		name : 'cust_id',
		type : 'string'
	},{
		name : 'locked',
		type : 'string'
	},{
		name : 'wit_cnt',
		type : 'string'
	},{
		name : 'cust_level',
		type : 'string'
	}
	]
});

Ext.define('BankAccount', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'id',
		type : 'string'
	}, {
		name : 'bankcode',
		type : 'string'
	}, {
		name : 'accountname',
		type : 'string'
	}, {
		name : 'accountno',
		type : 'string'
	}, {
		name : 'trust_level',
		type : 'string'
	}, {
		name : 'cust_level',
		type : 'string'
	}, {
		name : 'branch',
		type : 'string'
	}, {
		name : 'limit_amount',
		type : 'string'
	}, {
		name : 'bankname',
		type : 'string'
	}, {
		name : 'province',
		type : 'string'
	}, {
		name : 'city',
		type : 'string'
	}, {
		name : 'product_id',
		type : 'string'
	}, {
		name : 'remarks',
		type : 'string'
	}, {
		name : 'deposit_amount',
		type : 'string'
	}, {
		name : 'flag',
		type : 'string'
	}, {
		name : 'createby',
		type : 'string'
	}, {
		name : 'createdate',
		type : 'string'
	}, {
		name : 'customer_level',
		type : 'string'
	} ]
});

Ext.define('Creditfix', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'fix_id',
		type : 'string'
	}, {
		name : 'fix_no',
		type : 'string'
	}, {
		name : 'credit',
		type : 'string'
	}, {
		name : 'cust_id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'create_user',
		type : 'string'
	}, {
		name : 'status',
		type : 'string'
	}, {
		name : 'remarks',
		type : 'string'
	}, {
		name : 'audit_date',
		type : 'string'
	}, {
		name : 'audit_user',
		type : 'string'
	}, {
		name : 'audit_msg',
		type : 'string'
	} ]
});


Ext.define('AgentCreditfix', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'fix_id',
		type : 'string'
	}, {
		name : 'fix_no',
		type : 'string'
	}, {
		name : 'credit',
		type : 'string'
	}, {
		name : 'agent_id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'create_user',
		type : 'string'
	}, {
		name : 'status',
		type : 'string'
	}, {
		name : 'remarks',
		type : 'string'
	}, {
		name : 'audit_date',
		type : 'string'
	}, {
		name : 'audit_user',
		type : 'string'
	}, {
		name : 'audit_msg',
		type : 'string'
	} ]
});

Ext.define('Sharemodify', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'modify_id',
		type : 'string'
	}, {
		name : 'modify_no',
		type : 'string'
	}, {
		name : 'share_before',
		type : 'string'
	}, {
		name : 'share_after',
		type : 'string'
	}, {
		name : 'agent_id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'create_user',
		type : 'string'
	}, {
		name : 'status',
		type : 'string'
	}, {
		name : 'remarks',
		type : 'string'
	}, {
		name : 'audit_date',
		type : 'string'
	}, {
		name : 'audit_user',
		type : 'string'
	}, {
		name : 'audit_msg',
		type : 'string'
	} ]
});

Ext.define('Custform', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'form_id',
		type : 'string'
	}, {
		name : 'cust_id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'real_name',
		type : 'string'
	}, {
		name : 'org_real_name',
		type : 'string'
	}, {
		name : 'tel',
		type : 'string'
	}, {
		name : 'org_tel',
		type : 'string'
	}, {
		name : 'email',
		type : 'string'
	}, {
		name : 'org_email',
		type : 'string'
	}, {
		name : 'qq',
		type : 'string'
	}, {
		name : 'org_qq',
		type : 'string'
	}, {
		name : 'bank_name',
		type : 'string'
	}, {
		name : 'org_bank_name',
		type : 'string'
	}, {
		name : 'account',
		type : 'string'
	}, {
		name : 'org_account',
		type : 'string'
	}, {
		name : 'bank_dot',
		type : 'string'
	}, {
		name : 'org_bank_dot',
		type : 'string'
	}, {
		name : 'reason',
		type : 'string'
	}, {
		name : 'account_type',
		type : 'string'
	}, {
		name : 'org_account_type',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'create_user',
		type : 'string'
	}, {
		name : 'audit_date',
		type : 'string'
	}, {
		name : 'audit_user',
		type : 'string'
	}, {
		name : 'status',
		type : 'string'
	}, {
		name : 'remarks',
		type : 'string'
	}, {
		name : 'reg_domain',
		type : 'string'
	}, {
		name : 'agent_domain',
		type : 'string'
	} ]
});

Ext.define('Announcement', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'announcement_id',
		type : 'string'
	}, {
		name : 'product_id',
		type : 'string'
	}, {
		name : 'effectivity_date',
		type : 'date'
	}, {
		name : 'expiry_date',
		type : 'date'
	}, {
		name : 'created_by',
		type : 'string'
	}, {
		name : 'created_date',
		type : 'string'
	}, {
		name : 'last_update',
		type : 'string'
	}, {
		name : 'last_updated_by',
		type : 'string'
	}, {
		name : 'content',
		type : 'string'
	}, {
		name : 'title',
		type : 'string'
	} ]
});

Ext.define('MessageBoard', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'msg_id',
		type : 'string'
	}, {
		name : 'cust_name',
		type : 'string'
	}, {
		name : 'question',
		type : 'string'
	}, {
		name : 'reply',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'create_user',
		type : 'string'
	}, {
		name : 'reply_user',
		type : 'string'
	}, {
		name : 'reply_date',
		type : 'string'
	}, {
		name : 'show_date',
		type : 'string'
	} ]
});

Ext.define('BetDetail', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'billNo',
		type : 'string'
	}, {
		name : 'playerName',
		type : 'string'
	}, {
		name : 'agentCode',
		type : 'string'
	}, {
		name : 'gameCode',
		type : 'string'
	}, {
		name : 'netAmount',
		type : 'string'
	}, {
		name : 'betTime',
		type : 'string'
	}, {
		name : 'gameType',
		type : 'string'
	}, {
		name : 'betAmount',
		type : 'string'
	}, {
		name : 'validBetAmount',
		type : 'string'
	}, {
		name : 'flag',
		type : 'string'
	}, {
		name : 'playType',
		type : 'string'
	}, {
		name : 'currency',
		type : 'string'
	}, {
		name : 'tableCode',
		type : 'string'
	}, {
		name : 'loginIP',
		type : 'string'
	}, {
		name : 'recalcuTime',
		type : 'string'
	}, {
		name : 'platformId',
		type : 'string'
	}, {
		name : 'platformType',
		type : 'string'
	}, {
		name : 'stringex',
		type : 'string'
	}, {
		name : 'fileName',
		type : 'string'
	} ]
});

Ext.define('AccountTransfer', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'id',
		type : 'string'
	}, {
		name : 'agentCode',
		type : 'string'
	}, {
		name : 'transferId',
		type : 'string'
	}, {
		name : 'tradeNo',
		type : 'string'
	}, {
		name : 'platformId',
		type : 'string'
	}, {
		name : 'platformType',
		type : 'string'
	}, {
		name : 'playerName',
		type : 'string'
	}, {
		name : 'transferType',
		type : 'string'
	}, {
		name : 'transferAmount',
		type : 'string'
	}, {
		name : 'previousAmount',
		type : 'string'
	}, {
		name : 'currentAmount',
		type : 'string'
	}, {
		name : 'currency',
		type : 'string'
	}, {
		name : 'exchangeRate',
		type : 'string'
	}, {
		name : 'IP',
		type : 'string'
	}, {
		name : 'flag',
		type : 'string'
	}, {
		name : 'creationTime',
		type : 'string'
	}, {
		name : 'gameCode',
		type : 'string'
	}, {
		name : 'fileName',
		type : 'string'
	} ]
});

Ext.define('WashTask', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'task_id',
		type : 'string'
	}, {
		name : 'task_name',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'start_time',
		type : 'string'
	}, {
		name : 'end_time',
		type : 'string'
	}, {
		name : 'create_user',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'isall',
		type : 'string'
	}, {
		name : 'flag',
		type : 'string'
	}, {
		name : 'remark',
		type : 'string'
	} ]
});

Ext.define('WashRec', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'rec_id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'cust_level',
		type : 'string'
	}, {
		name : 'water_rate',
		type : 'string'
	}, {
		name : 'sum_credit',
		type : 'string'
	}, {
		name : 'water_credit',
		type : 'string'
	}, {
		name : 'start_time',
		type : 'string'
	}, {
		name : 'end_time',
		type : 'string'
	}, {
		name : 'create_user',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'task_id',
		type : 'string'
	}, {
		name : 'task_name',
		type : 'string'
	}, {
		name : 'flag',
		type : 'string'
	}, {
		name : 'remark',
		type : 'string'
	}, {
		name : 'net_credit',
		type : 'string'
	} ]
});

Ext.define('UserMsg', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'msg_id',
		type : 'string'
	}, {
		name : 'msg_type',
		type : 'string'
	}, {
		name : 'content',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'flag',
		type : 'string'
	} ]
});

Ext.define('GameLog', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'log_id',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'out_credit',
		type : 'string'
	}, {
		name : 'local_credit',
		type : 'string'
	}, {
		name : 'in_credit',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'out_date',
		type : 'string'
	}, {
		name : 'out_flag',
		type : 'string'
	}, {
		name : 'local_date',
		type : 'string'
	}, {
		name : 'local_flag',
		type : 'string'
	}, {
		name : 'in_date',
		type : 'string'
	}, {
		name : 'in_flag',
		type : 'string'
	}, {
		name : 'cagent',
		type : 'string'
	}, {
		name : 'out_bill_no',
		type : 'string'
	}, {
		name : 'in_bill_no',
		type : 'string'
	} ]
});
Ext.define('CashGift', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'gift_id',
		type : 'string'
	}, {
		name : 'kh_date',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'real_name',
		type : 'string'
	}, {
		name : 'cust_level',
		type : 'string'
	}, {
		name : 'gift_type',
		type : 'string'
	}, {
		name : 'gift_code',
		type : 'string'
	}, {
		name : 'deposit_credit',
		type : 'string'
	}, {
		name : 'net_credit',
		type : 'string'
	}, {
		name : 'valid_credit',
		type : 'string'
	}, {
		name : 'rate',
		type : 'string'
	}, {
		name : 'payout',
		type : 'string'
	}, {
		name : 'cs_date',
		type : 'string'
	}, {
		name : 'remarks',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'create_user',
		type : 'string'
	}, {
		name : 'status',
		type : 'string'
	}, {
		name : 'audit_date',
		type : 'string'
	}, {
		name : 'audit_user',
		type : 'string'
	}, {
		name : 'audit_msg',
		type : 'string'
	}, {
		name : 'cust_id',
		type : 'string'
	}, {
		name : 'gift_no',
		type : 'string'
	}, {
		name : 'transferflag',
		type : 'string'
	}

	]
});

Ext.define('YearGift', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'year_gift_id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'day',
		type : 'string'
	}, {
		name : 'cust_id',
		type : 'string'
	}, {
		name : 'credit',
		type : 'double'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'gift_no',
		type : 'string'
	}, {
		name : 'flag',
		type : 'string'
	}

	]
});

Ext.define('HuoliGift', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'huoli_gift_id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'cust_id',
		type : 'string'
	}, {
		name : 'content',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'gift_no',
		type : 'string'
	}, {
		name : 'flag',
		type : 'string'
	}, {
		name : 'level',
		type : 'string'
	}

	]
});

Ext.define('Hongbao', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'mb_hongbao_id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'cust_id',
		type : 'string'
	}, {
		name : 'content',
		type : 'string'
	}, {
		name : 'credit',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'gift_id',
		type : 'string'
	}, {
		name : 'status',
		type : 'string'
	}, {
		name : 'level',
		type : 'string'
	}

	]
});

Ext.define('Profit', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'customer_id',
		type : 'string'
	}, {
		name : 'mon',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'agentId',
		type : 'string'
	}, {
		name : 'deposit',
		type : 'string'
	}, {
		name : 'withdraw',
		type : 'string'
	}, {
		name : 'bet',
		type : 'string'
	}, {
		name : 'gift',
		type : 'string'
	}, {
		name : 'agent_name',
		type : 'string'
	} ]
});

Ext.define('Config', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'config_id',
		type : 'string'
	}, {
		name : 'config_name',
		type : 'string'
	}, {
		name : 'config_value',
		type : 'string'
	}, {
		name : 'config_describe',
		type : 'string'
	}, {
		name : 'maxlimit',
		type : 'string'
	}, {
		name : 'config_level',
		type : 'string'
	}, {
		name : 'sortpriority',
		type : 'string'
	} ]
});

Ext.define('Payonline', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'payonline_id',
		type : 'string'
	}, {
		name : 'name',
		type : 'string'
	}, {
		name : 'value',
		type : 'string'
	}, {
		name : 'submit_value',
		type : 'string'
	}, {
		name : 'return_value',
		type : 'string'
	}, {
		name : 'notify_value',
		type : 'string'
	}, {
		name : 'valuedescribe',
		type : 'string'
	}, {
		name : 'req_referer',
		type : 'string'
	} ]
});

Ext.define('Ad', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'ad_id',
		type : 'string'
	}, {
		name : 'ad_title',
		type : 'string'
	}, {
		name : 'ad_describe',
		type : 'string'
	}, {
		name : 'pic_url',
		type : 'string'
	}, {
		name : 'target_url',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'start_date',
		type : 'date'
	}, {
		name : 'end_date',
		type : 'date'
	}, {
		name : 'create_user',
		type : 'string'
	}, {
		name : 'available',
		type : 'string'
	}, {
		name : 'priority',
		type : 'string'
	},{
		name : 'mobile_flag',
		type : 'string'
	}

	]
});

Ext.define('Notice', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'notice_id',
		type : 'string'
	}, {
		name : 'title',
		type : 'string'
	}, {
		name : 'content',
		type : 'string'
	}, {
		name : 'start_date',
		type : 'date'
	}, {
		name : 'end_date',
		type : 'date'
	}, {
		name : 'create_user',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'date'
	}, {
		name : 'priority',
		type : 'string'
	}, {
		name : 'available',
		type : 'string'
	}

	]
});

Ext.define('Discount', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'discount_id',
		type : 'string'
	}, {
		name : 's_url',
		type : 'string'
	}, {
		name : 'b_url',
		type : 'string'
	}, {
		name : 'title',
		type : 'string'
	}, {
		name : 'summary',
		type : 'string'
	}, {
		name : 'content',
		type : 'string'
	}, {
		name : 'available',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'date'
	}, {
		name : 'start_date',
		type : 'date'
	}, {
		name : 'end_date',
		type : 'date'
	}, {
		name : 'create_user',
		type : 'string'
	}, {
		name : 'priority',
		type : 'string'
	},{
		name : 'is_hot',
		type : 'string'
	},{
		name : 'mobile_flag',
		type : 'string'
	}

	]
});

Ext.define('Preg', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'preg_id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'bat_code',
		type : 'string'
	}, {
		name : 'agent_id',
		type : 'strings'
	}, {
		name : 'agent_name',
		type : 'string'
	} ]
});

Ext.define('Bank', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'bank_id',
		type : 'string'
	}, {
		name : 'bank_name',
		type : 'string'
	}, {
		name : 'account',
		type : 'string'
	}, {
		name : 'account_name',
		type : 'string'
	}, {
		name : 'bank_dot',
		type : 'string'
	}, {
		name : 'cust_level',
		type : 'string'
	}, {
		name : 'remarks',
		type : 'string'
	}, {
		name : 'create_user',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	} ,{
		name : 'available',
		type : 'string'
	},{
		name : 'specifiedname',
		type : 'string'
	}
	]
});

Ext.define('Stat', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'index_date',
		type : 'date',
		format : 'Y-m-d'
	}, {
		name : 'account_cnt',
		type : 'int'
	}, {
		name : 'account_deposit_cnt',
		type : 'int'
	}, {
		name : 'deposit_sum',
		type : 'float'
	}, {
		name : 'withdraw_sum',
		type : 'float'
	}, {
		name : 'payout',
		type : 'float'
	}, {
		name : 'deposit_cnt',
		type : 'int'
	}, {
		name : 'withdraw_cnt',
		type : 'int'
	}, {
		name : 'profit',
		type : 'float'
	}, {
		name : 'stat_mon',
		type : 'string'
	} ]
});


Ext.define('AgentStat', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'agent_name',
		type : 'string'
	}, {
		name : 'stat_date',
		type : 'string'
	}, {
		name : 'deposit_count',
		type : 'int'
	}, {
		name : 'withdraw_count',
		type : 'int'
	}, {
		name : 'gift_count',
		type : 'int'
	}, {
		name : 'deposit_collect',
		type : 'float'
	}, {
		name : 'withdraw_collect',
		type : 'float'
	}, {
		name : 'gift_collect',
		type : 'float'
	} ]
});


Ext.define('CustomerView', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'real_name',
		type : 'string'
	}, {
		name : 'tel',
		type : 'string'
	}, {
		name : 'login_date',
		type : 'date',
		format : 'Y-m-d'
	}, {
		name : 'qq',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'date',
		format : 'Y-m-d'
	}, {
		name : 'deposit_cnt',
		type : 'int'
	}, {
		name : 'withdraw_cnt',
		type : 'int'
	}, {
		name : 'deposit_amount',
		type : 'float'
	}, {
		name : 'withdraw_amount',
		type : 'float'
	}, {
		name : 'last_deposit_date',
		type : 'date',
		format : 'Y-m-d'
	}, {
		name : 'last_withdraw_date',
		type : 'date',
		format : 'Y-m-d'
	} ]
});

Ext.define('YeeOrder', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'phone',
		type : 'string'
	}, {
		name : 'order_no',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'date',
		format : 'Y-m-d'
	}, {
		name : 'credit',
		type : 'float'
	}, {
		name : 'frpid',
		type : 'string'
	}, {
		name : 'pay_credit',
		type : 'float'
	}, {
		name : 'target_fee',
		type : 'float'
	}, {
		name : 'done_bs',
		type : 'bool'
	}, {
		name : 'pay_date',
		type : 'string'
	} ]
});

Ext.define('Transfer', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'transfer_id',
		type : 'string'
	}, {
		name : 'cust_id',
		type : 'string'
	}, {
		name : 'transfer_no',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'transfer_type',
		type : 'string'
	}, {
		name : 'credit',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'create_user',
		type : 'string'
	}, {
		name : 'status',
		type : 'string'
	}, {
		name : 'audit_date',
		type : 'string'
	}, {
		name : 'audit_user',
		type : 'string'
	}, {
		name : 'audit_msg',
		type : 'string'
	} ]
});

Ext.define('Dinpay', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'dinpay_id',
		type : 'string'
	}, {
		name : 'cust_id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'merchant_code',
		type : 'string'
	}, {
		name : 'order_no',
		type : 'string'
	}, {
		name : 'order_amount',
		type : 'string'
	}, {
		name : 'order_time',
		type : 'string'
	}, {
		name : 'client_ip',
		type : 'string'
	}, {
		name : 'product_name',
		type : 'string'
	}, {
		name : 'bank_code',
		type : 'string'
	}, {
		name : 'bank_name',
		type : 'string'
	}, {
		name : 'bank_seq_no',
		type : 'string'
	}, {
		name : 'trade_status',
		type : 'string'
	}, {
		name : 'trade_no',
		type : 'string'
	}, {
		name : 'trade_time',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'notify_id',
		type : 'string'
	}, {
		name : 'interface_version',
		type : 'string'
	}, {
		name : 'rec_sign_type',
		type : 'string'
	}, {
		name : 'rec_sign',
		type : 'string'
	}, {
		name : 'pay_amount',
		type : 'string'
	}, {
		name : 'send_sign_type',
		type : 'string'
	}, {
		name : 'send_sign',
		type : 'string'
	}, {
		name : 'finished',
		type : 'string'
	}, {
		name : 'finished_date',
		type : 'string'
	}, {
		name : 'dep_no',
		type : 'string'
	} ]
});

Ext.define('Letter', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'letter_id',
		type : 'string'
	}, {
		name : 'title',
		type : 'string'
	}, {
		name : 'content',
		type : 'string'
	}, {
		name : 'cust_id',
		type : 'string'
	}, {
		name : 'read_flag',
		type : 'string'
	}, {
		name : 'read_date',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'create_user',
		type : 'string'
	}, {
		name : 'is_public',
		type : 'string'
	}, {
		name : 'letter_code',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	} ]
});
Ext.define('ScoreRec', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'rec_id',
		type : 'string'
	}, {
		name : 'rec_code',
		type : 'string'
	}, {
		name : 'cust_id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'rec_type',
		type : 'string'
	}, {
		name : 'score',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'create_user',
		type : 'string'
	}, {
		name : 'cur_score',
		type : 'string'
	} ]
});

Ext.define('Sign', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'sign_id',
		type : 'string'
	}, {
		name : 'cust_id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	},  {
		name : 'sign_date',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	} ]
});


Ext.define('PhoneRecord', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'Id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'content',
		type : 'string'
	},  {
		name : 'create_user',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	} ]
});

Ext.define('BetRec', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'dataId',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'validBetAmount',
		type : 'string'
	}, {
		name : 'platform',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'bet_date',
		type : 'string'
	}, {
		name : 'agent_id',
		type : 'string'
	} ]
});



Ext.define('Trophy', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'trophy_id',
		type : 'string'
	}, {
		name : 'trophy_name',
		type : 'string'
	}, {
		name : 'trophy_code',
		type : 'string'
	}, {
		name : 'trophy_count',
		type : 'string'
	}, {
		name : 'egg',
		type : 'string'
	}, {
		name : 'is_default',
		type : 'string'
	}, {
		name : 'enable',
		type : 'string'
	}, {
		name : 'cost',
		type : 'string'
	}, {
		name : 'trophy_type',
		type : 'string'
	} ]
});

Ext.define('EggGift', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'gift_id',
		type : 'string'
	}, {
		name : 'gift_code',
		type : 'string'
	}, {
		name : 'cust_id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'real_name',
		type : 'string'
	}, {
		name : 'trophy_code',
		type : 'string'
	}, {
		name : 'trophy_name',
		type : 'string'
	}, {
		name : 'trophy_count',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'create_user',
		type : 'string'
	}, {
		name : 'status',
		type : 'string'
	}, {
		name : 'remark',
		type : 'string'
	}, {
		name : 'audit_user',
		type : 'string'
	}, {
		name : 'audit_date',
		type : 'string'
	}, {
		name : 'ref_gift_code',
		type : 'string'
	}, {
		name : 'score',
		type : 'string'
	}, {
		name : 'trophy_id',
		type : 'string'
	}, {
		name : 'cost',
		type : 'string'
	}, {
		name : 'trophy_type',
		type : 'string'
	} ]
});

Ext.define('RankingList', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'ranking_id',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'trophy_name',
		type : 'string'
	}, {
		name : 'trophy_code',
		type : 'string'
	}, {
		name : 'trophy_count',
		type : 'string'
	}, {
		name : 'cost',
		type : 'string'
	}, {
		name : 'cust_id',
		type : 'string'
	}]
});
Ext.define('PtGame', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'pt_id',
		type : 'string'
	}, {
		name : 'game_name',
		type : 'string'
	}, {
		name : 'game_type',
		type : 'string'
	}, {
		name : 'progressive',
		type : 'string'
	}, {
		name : 'branded',
		type : 'string'
	}, {
		name : 'game_code',
		type : 'string'
	}, {
		name : 'client',
		type : 'string'
	}, {
		name : 'flash',
		type : 'string'
	},{
		name : 'mobile',
		type : 'string'
	},{
		name : 'cn_name',
		type : 'string'
	},{
		name : 'pool_name',
		type : 'string'
	},{
		name : 'hot',
		type : 'string'
	},{
		name : 'recommend',
		type : 'string'
	},{
		name : 'enable',
		type : 'string'
	}]
});

Ext.define('MgGame', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'mg_id',
		type : 'string'
	}, {
		name : 'Category',
		type : 'string'
	}, {
		name : 'Game_caegory',
		type : 'string'
	}, {
		name : 'Sub_category',
		type : 'string'
	}, {
		name : 'GameName',
		type : 'string'
	}, {
		name : 'CHINESE_SIMP_Game_Name',
		type : 'string'
	}, {
		name : 'CHINESE_TRAD_Game_Name',
		type : 'string'
	}, {
		name : 'Image_File_Name',
		type : 'string'
	},{
		name : 'GameCode',
		type : 'string'
	},{
		name : 'enable',
		type : 'string'
	},{
		name : 'sortpriority',
		type : 'string'
	},{
		name : 'is_hot',
		type : 'string'
	},{
		name : 'is_new',
		type : 'string'
	},{
		name : 'is_html5',
		type : 'string'
	},{
		name : 'is_pchtml5',
		type : 'string'
	},{
		name : 'pchtml5code',
		type : 'string'
	},{
		name : 'mobilehtml5code',
		type : 'string'
	}]
});

Ext.define('AgGame', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'ag_id',
		type : 'string'
	}, {
		name : 'game_name',
		type : 'string'
	}, {
		name : 'game_type',
		type : 'string'
	}, {
		name : 'game_code',
		type : 'string'
	},{
		name : 'sortpriority',
		type : 'string'
	},{
		name : 'Image_File_Name',
		type : 'string'
	},{
		name : 'hot',
		type : 'string'
	},{
		name : 'is_new',
		type : 'string'
	},{
		name : 'enable',
		type : 'string'
	}]
});


Ext.define('SbGame', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'sb_id',
		type : 'string'
	}, {
		name : 'game_name',
		type : 'string'
	}, {
		name : 'game_type',
		type : 'string'
	}, {
		name : 'game_code',
		type : 'string'
	},{
		name : 'sortpriority',
		type : 'string'
	},{
		name : 'image_game_name',
		type : 'string'
	},{
		name : 'image_preview_name',
		type : 'string'
	},{
		name : 'providercode',
		type : 'string'
	},{
		name : 'enable',
		type : 'string'
	}]
});


Ext.define('Agent', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'agent_id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'login_pwd',
		type : 'string'
	}, {
		name : 'real_name',
		type : 'string'
	}, {
		name : 'phone',
		type : 'string'
	}, {
		name : 'email',
		type : 'string'
	}, {
		name : 'qq',
		type : 'string'
	}, {
		name : 'reg_ip',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'create_user',
		type : 'string'
	}, {
		name : 'last_login_ip',
		type : 'string'
	}, {
		name : 'last_login_date',
		type : 'string'
	},{
		name : 'credit',
		type : 'string'
	},{
		name : 'subcount',
		type : 'string'
	},{
		name : 'active',
		type : 'string'
	}, {
		name : 'flag',
		type : 'string'
	}, {
		name : 'share',
		type : 'string'
	}, {
		name : 'parent_id',
		type : 'string'
	}, {
		name : 'remarks',
		type : 'string'
	}, {
		name : 'bank_name',
		type : 'string'
	}, {
		name : 'account_type',
		type : 'string'
	}, {
		name : 'bank_dot',
		type : 'string'
	}, {
		name : 'account',
		type : 'string'
	} ]
});

Ext.define('TransferLog', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'transfer_id',
		type : 'string'
	}, {
		name : 'cust_id',
		type : 'string'
	}, {
		name : 'bill_no',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'transer_from',
		type : 'string'
	}, {
		name : 'transer_to',
		type : 'string'
	}, {
		name : 'credit',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'create_user',
		type : 'string'
	}, {
		name : 'status',
		type : 'string'
	}, {
		name : 'finish_date',
		type : 'string'
	}, {
		name : 'remarks',
		type : 'string'
	} ]
});


Ext.define('GameTransferData', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'data_id',
		type : 'string'
	}, {
		name : 'bill_no',
		type : 'string'
	}, {
		name : 'game_name',
		type : 'string'
	}, {
		name : 'game_pwd',
		type : 'string'
	}, {
		name : 'credit',
		type : 'string'
	}, {
		name : 'direct',
		type : 'string'
	}, {
		name : 'plat',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'flag',
		type : 'string'
	}, {
		name : 'finish_date',
		type : 'string'
	}, {
		name : 'ref_order_no',
		type : 'string'
	}, {
		name : 'product',
		type : 'string'
	} ]
});

Ext.define('MoleHit', {
	 extend : 'Ext.data.Model',
	 fields : [ {
	  name : 'id',
	  type : 'string'
	 }, {
	  name : 'login_name',
	  type : 'string'
	 }, {
	  name : 'cust_id',
	  type : 'string'
	 }, {
	  name : 'create_date',
	  type : 'string'
	 },{
	  name : 'hitcount',
	  type : 'string'
	 }

	 ]
});

Ext.define('WinList', {
	 extend : 'Ext.data.Model',
	 fields : [ {
	  name : 'win_id',
	  type : 'string'
	 }, {
	  name : 'platform',
	  type : 'string'
	 }, {
	  name : 'game_name',
	  type : 'string'
	 }, {
	  name : 'login_name',
	  type : 'string'
	 }, {
	  name : 'win_amount',
	  type : 'string'
	 },{
	  name : 'img_path',
	  type : 'string'		  
	 },{
	  name : 'upload_date',
	  type : 'string'
	 },{
	  name : 'publish_date',
	  type : 'string'
	 },{
	  name : 'publish_status',
	  type : 'string'
	 } ]
});


Ext.define('AgentWithdraw', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'awithdraw_id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'real_name',
		type : 'string'
	}, {
		name : 'amount',
		type : 'string'
	}, {
		name : 'bank_name',
		type : 'string'
	}, {
		name : 'account_name',
		type : 'string'
	}, {
		name : 'account',
		type : 'string'
	}, {
		name : 'account_type',
		type : 'string'
	}, {
		name : 'location',
		type : 'string'
	}, {
		name : 'remarks',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'create_user',
		type : 'string'
	}, {
		name : 'status',
		type : 'string'
	}, {
		name : 'awit_no',
		type : 'string'
	}, {
		name : 'agent_id',
		type : 'string'
	},{
		name : 'locked',
		type : 'string'
	},{
		name : 'wit_cnt',
		type : 'string'
	}
	]
});

Ext.define('AgentCommission', {
	extend : 'Ext.data.Model',
	fields : [ {
		name : 'com_id',
		type : 'string'
	}, {
		name : 'agent_id',
		type : 'string'
	}, {
		name : 'login_name',
		type : 'string'
	}, {
		name : 'create_date',
		type : 'string'
	}, {
		name : 'create_user',
		type : 'string'
	}, {
		name : 'status',
		type : 'string'
	}, {
		name : 'audit_user',
		type : 'string'
	}, {
		name : 'audit_date',
		type : 'string'
	}, {
		name : 'credit',
		type : 'string'
	},{
		name : 'finalcredit',
		type : 'string'
	}, {
		name : 'audit_msg',
		type : 'string'
	}, {
		name : 'share',
		type : 'string'
	}, {
		name : 'active',
		type : 'string'
	}, {
		name : 'start_date',
		type : 'string'
	}, {
		name : 'end_date',
		type : 'string'
	}, {
		name : 'total_deposit',
		type : 'string'
	}, {
		name : 'total_withdraw',
		type : 'string'
	}, {
		name : 'total_gift',
		type : 'string'
	},{
		name : 'total_cost',
		type : 'string'
	},{
		name : 'remarks',
		type : 'string'
	}
	]
});

