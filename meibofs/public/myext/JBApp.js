var JBApp = {
	/**
	 * 点击查询按钮
	 */
	queryFn : function() {
		var queryData = this.up('grid').down('#queryKey').getValue();
		var ssdate = this.up('grid').down('#sdate').getValue();
		var eedate = this.up('grid').down('#edate').getValue();
		var sdate, edate;
		if (ssdate) {
			sdate = Ext.Date.format(new Date(ssdate), 'Y-m-d');
		}
		if (eedate) {
			edate = Ext.Date.format(new Date(eedate), 'Y-m-d');
		}
		if (ssdate && eedate) {
			if (sdate > edate) {
				Ext.MessageBox.alert('提示信息', '开始日期不能大于结束日期!');
				return;
			}
		}
		var store = this.up('grid').getStore();
		store.on('beforeload', function(store, options) {
			var new_params = {
				queryKey : queryData,
				sdate : sdate,
				edate : edate
			};
			Ext.apply(store.proxy.extraParams, new_params);
		});
		store.load({
			start : 0,
			limit : 30,
			page : 1
		});
	},
	
	queryWithstatusFn : function() {
		var queryData = this.up('grid').down('#queryKey').getValue();
		var ssdate = this.up('grid').down('#sdate').getValue();
		var eedate = this.up('grid').down('#edate').getValue();
		var status = this.up('grid').down('#status').getValue();
		var sdate, edate;
		if (ssdate) {
			sdate = Ext.Date.format(new Date(ssdate), 'Y-m-d');
		}
		if (eedate) {
			edate = Ext.Date.format(new Date(eedate), 'Y-m-d');
		}
		if (ssdate && eedate) {
			if (sdate > edate) {
				Ext.MessageBox.alert('提示信息', '开始日期不能大于结束日期!');
				return;
			}
		}
		var store = this.up('grid').getStore();
		store.on('beforeload', function(store, options) {
			var new_params = {
				queryKey : queryData,
				status : status,
				sdate : sdate,
				edate : edate
			};
			Ext.apply(store.proxy.extraParams, new_params);
		});
		store.load({
			start : 0,
			limit : 30,
			page : 1
		});
	},
	
	exportFn : function() {
		var queryData = this.up('grid').down('#queryKey').getValue();
		var ssdate = this.up('grid').down('#sdate').getValue();
		var eedate = this.up('grid').down('#edate').getValue();
		var sdate, edate;
		if (ssdate) {
			sdate = Ext.Date.format(new Date(ssdate), 'Y-m-d');
		}
		if (eedate) {
			edate = Ext.Date.format(new Date(eedate), 'Y-m-d');
		}
		if (ssdate && eedate) {
			if (sdate > edate) {
				Ext.MessageBox.alert('提示信息', '开始日期不能大于结束日期!');
				return;
			}
		}
		var actionUrl=this.actionUrl;
		Ext.MessageBox.confirm({
			title : '提示信息',
			msg : '你确认要导出这个表格吗?',
			buttons : Ext.MessageBox.OKCANCEL,
			fn : function(btn, text) {
				if ("ok" == btn) {
					var new_params = {
							queryKey : queryData,
							sdate : sdate,
							edate : edate
						};
					window.open(actionUrl+'?'+Ext.urlEncode(new_params));
				}
			}
		});
	},
	/**
	 * 搜索按钮重设
	 */
	queryResetFn : function() {
		this.up('grid').down('#queryKey').setValue("");
		this.up('grid').down('#sdate').setValue("");
		this.up('grid').down('#edate').setValue("");
	},
	
	/**
	 * 带状态的搜索按钮重设
	 */
	queryWithstatusResetFn : function() {
		this.up('grid').down('#status').setValue("");
		this.up('grid').down('#queryKey').setValue("");
		this.up('grid').down('#sdate').setValue("");
		this.up('grid').down('#edate').setValue("");
	},
	
	/**
	 * 删除某一条信息
	 */
	deleteFn : function() {
		var select = this.up('grid').getSelectionModel().getSelection();
		if (select.length != 1) {
			Ext.MessageBox.alert('提示信息', '只能选择一条记录操作');
			return;
		}
		var grid = this.up('grid');
		var config = grid.delconfig;
		Ext.MessageBox.confirm({
			title : '提示信息',
			msg : config.tip,
			buttons : Ext.MessageBox.OKCANCEL,
			fn : function(btn, text) {
				if ("ok" == btn) {
					Ext.Ajax.request({
								url : config.url,
								params : {
									"idcode": select[0].get(config.idcode)
								},
								success : function(response) {
									var result = Ext.JSON
											.decode(response.responseText);
									if (result.success) {
										Ext.Msg.alert('操作成功', result.message);
										grid.getStore().reload();
									} else {
										Ext.Msg.alert('操作失败', result.message);
									}
								},
								failure : function(response) {
									Ext.Msg.alert('保存失败', '网络故障。');
								}
							});
				}
			}
		});
	},
	commonFn : function(btn) {
		var select = this.up('grid').getSelectionModel().getSelection();
		if (select.length != 1) {
			Ext.MessageBox.alert('提示信息', '只能选择一条记录操作');
			return;
		}
		var grid = this.up('grid');
		var configId=btn.configId;
		var config = grid[configId];
		Ext.MessageBox.confirm({
			title : '提示信息',
			msg : config.tip?config.tip:config.tipFn(select),
			buttons : Ext.MessageBox.OKCANCEL,
			fn : function(btn, text) {
				if ("ok" == btn) {
					Ext.Ajax.request({
								url : config.url,
								params : {
									"idcode": select[0].get(config.idcode)
								},
								success : function(response) {
									var result = Ext.JSON
											.decode(response.responseText);
									if (result.success) {
										Ext.Msg.alert('操作成功', result.message);
										grid.getStore().reload();
									} else {
										Ext.Msg.alert('操作失败', result.message);
									}
								},
								failure : function(response) {
									Ext.Msg.alert('保存失败', '网络故障。');
								}
							});
				}
			}
		});
	},
	addFn:function(){
		this.up('grid').win.down('form').getForm().setValues({kact:'1'});
		var grid=this.up('grid')
		var config =grid.addconfig;
		if(config.proFn){
			config.proFn(this.up('grid').win,this.up('grid').win.down('form'));
		}
		grid.win.setTitle(config.title);
		grid.win.show();	
	},
	updateFn:function(){
		  var select =this.up('grid').getSelectionModel().getSelection();
		  if(select.length!=1){
			 Ext.MessageBox.alert('提示信息', '只能选择一条记录操作');
			 return ;
		  }
		  var grid=this.up('grid')
			var config =grid.updateconfig;
			if(config.proFn){
				config.proFn(select[0],this.up('grid').win,this.up('grid').win.down('form'));
			}
	      this.up('grid').win.show();	
	},
	saveFn:function(){
		var form =this.up('form');
		var config=form.saveConfig;
     	if (form.isValid()) {
     		var win=this.up('window');
     		if(win) win.mask();
             form.submit({
            	   url:config.url,
                   success: function(f, action) {
                	  if(win) win.unmask();
                      Ext.Msg.alert('保存成功', action.result.message);
                      config.store.reload();
                      form.up('window').hide();
                   },
                   failure: function(form, action) {
                	   if(win) win.unmask();
                       Ext.Msg.alert('保存失败', action.result.message);
                   }
               });
          }
	},
	commonNoRecord:function(btn){
		var grid=this.up('grid')
		var configId=btn.configId;
		var config = grid[configId];
		   Ext.MessageBox.confirm({
				title : '提示信息',
				msg : config.tip,
				buttons : Ext.MessageBox.OKCANCEL,
				fn : function(btn, text) {
					if ("ok" == btn) {
						Ext.Ajax.request({
									url:config.url,
									success : function(response) {
										var result = Ext.JSON.decode(response.responseText);
										if (result.success) {
											Ext.Msg.alert('操作成功', result.message);
											grid.getStore().reload();
										} else {
											Ext.Msg.alert('操作失败', result.message);
										}
									},
									failure : function(response) {
										Ext.Msg.alert('保存失败', '网络故障。');
									}
								});
					}
				}
			});
	},
	renderer:{
		bool:function(val){
			if(val=='true'){
				return '<span class="icon-true">&nbsp;&nbsp;&nbsp;&nbsp;</span>';
			}else{
				return '<span class="icon-false">&nbsp;&nbsp;&nbsp;&nbsp;</span>';
			}
		},
		deposit_flag:function(val){
			var v=JBDict['deposit_flag'][val];
			if(v=='已通过')return '<span style="color:green">已通过</span>';
			if(v=='待审核')return '<span style="color:red">待审核</span>';
			if(v=='已拒绝')return '<span style="color:#9D9D9D">已拒绝</span>';
			if(v=='已废除')return '<span style="color:#FFE66F">已废除</span>';
			return v?v:val;
		},
		pdage_flag:function(val){
			var v=JBDict['pdage_flag'][val];
			if(v=='已通过')return '<span style="color:green">已通过</span>';
			if(v=='待审核')return '<span style="color:red">待审核</span>';
			if(v=='已拒绝')return '<span style="color:#9D9D9D">已拒绝</span>';
			if(v=='已废除')return '<span style="color:#FFE66F">已废除</span>';
			return v?v:val;
		},
		fix_status:function(val){
			var v=JBDict['fix_status'][val];
			if(v=='已修正')return '<span style="color:green">已修正</span>';
			if(v=='待审核')return '<span style="color:red">待审核</span>';
			if(v=='已废除')return '<span style="color:#FFE66F">已废除</span>';
			return v?v:val;
		},
		egg_gift_status:function(val){
			var v=JBDict['egg_gift_status'][val];
			if(v=='已处理')return '<span style="color:green">已处理</span>';
			if(v=='待处理')return '<span style="color:red">待处理</span>';
			if(v=='已废除')return '<span style="color:#FFE66F">已废除</span>';
			return v?v:val;
		},
		amount:function(val){
			if(val>=10000){
				return '<span style="color:#FF6600;font-weight:bolder">'+val+'</span>';
			}
			return val;
		},
		bankcode:function(val){
			var v=JBDict['bankcode'][val];
			return v?v:val;
		},
		deptype:function(val){
			var v=JBDict['deptype'][val];
			return v?v:val;
		},
		custlevel:function(val){
			var v=JBDict['cust_level'][val];
			return v?v:val;
		},
		withdraw_flag:function(val){
			var v=JBDict['withdraw_flag'][val];
			if(v=='已支付')return '<span style="color:green">已支付  / Successful</span>';
			if(v=='已审核')return '<span style="color:#FF2D2D">已审核  / Approved</span>';
			if(v=='待审核')return '<span style="color:red">待审核  / Pending</span>';
			if(v=='已拒绝')return '<span style="color:#9D9D9D">已拒绝  / Reject</span>';
			if(v=='已废除')return '<span style="color:#9D9D9D">已废除  / Cancle</span>';
			return v?v:val;
		},
		bank_flag:function(val){
			
			if(val=='建行') return '建行 / CCB';
			if(val=='农行') return '农行 / ABC';
			if(val=='工行') return '工行 / ICBC';
			if(val=='中行') return '中行 / BOC';
			if(val=='民生') return '民生 / CMBC';
			if(val=='招行') return '招行 / CMB';
			if(val=='兴业') return '兴业 / CIB';
			if(val=='北京银行') return '北京银行 / BOB';
			if(val=='光大') return '光大 / CEB';
			if(val=='交行') return '交行 / BCM';
			if(val=='邮政') return '邮政 / PSBC';
		    return val;
		},
		gift_flag:function(val){
			var v=JBDict['gift_flag'][val];
			if(v=='已通过')return '<span style="color:green">已通过</span>';
			if(v=='待审核')return '<span style="color:red">待审核</span>';
			if(v=='已拒绝')return '<span style="color:#9D9D9D">已拒绝</span>';
			if(v=='已废除')return '<span style="color:#FFE66F">已废除</span>';
			return v?v:val;
		},
		
		year_gift_flag:function(val){
			if(val=='1')return '<span style="color:green">已派发</span>';
			if(val=='0')return '<span style="color:red">未派发</span>';
			if(val=='2')return '<span style="color:#9D9D9D">派发失败</span>';
			return val;
		},
		form_flag:function(val){
			var v=JBDict['form_flag'][val];
			return v?v:val;
		},
	  	withdrawflag:function(val){
			if(val=='1'){
				return '<span style="color:green">已通过</span>';
			}
			if(val=='2'){
				return '<span style="color:green">已支付</span>';
			}
			if(val=='0'){
				return '<span>待处理</span>';
			}
			if(val=='-2'){
				return '<span style="color:red">已拒绝</span>';
			}
			if(val=='-1'){
				return '<span>已废除</span>';
			}
			if(val=='-3'){
				return '<span>预创建</span>';
			}
			return val;
		},
		custformflag:function(val){
			if(val=='2'){
				return '<span style="color:green">已通过</span>';
			}
			if(val=='0'){
				return '<span>待处理</span>';
			}
			if(val=='-2'){
				return '<span style="color:red">已拒绝</span>';
			}
			if(val=='-1'){
				return '<span>已废除</span>';
			}
			return val;
		},creditlog:function(val){
			if(val=='1'){
				return '存款';
			}
			if(val=='2'){
				return '提款';
			}
			if(val=='3'){
				return '取消提款';
			}
			if(val=='4'){
				return '拒绝提款';
			}
			if(val=='5'){
				return '额度修正';
			}
			if(val=='6'){
				return '保险礼金';
			}
			if(val=='7'){
				return '周洗码';
			}
			if(val=='8'){
				return 'VIP洗码';
			}
			if(val=='9'){
				return '1111活动';
			}
			if(val=='10'){
				return '首存';
			}
			if(val=='11'){
				return '新年30%';
			}
			if(val=='12'){
				return '双倍洗码';
			}
			if(val=='13'){
				return '推荐返水';
			}
			if(val=='14'){
				return '多存红利';
			}
			return val;
		},
		creditflag:function(val){
			if(val=='-2'){
				return '失败';
			}
			if(val=='0'){
				return '创建';
			}
			if(val=='2'){
				return '完成';
			}
			return val;
		},
		transferLogflag:function(val){
			if(val=='2'){
				return '<span style="color:gray">失败</span>';
			}
			if(val=='1'){
				return '<span style="color:green">成功</span>';
			}
			if(val=='3'){
				return '<span style="color:red">异常</span>';
			}
			if(val=='0'){
				return '未响应';
			}
			return val;
		},
		giftflag:function(val){
			if(val=='2'){
				return '<span style="color:green">已通过</span>';
			}
			if(val=='0'){
				return '<span>待处理</span>';
			}
			if(val=='-2'){
				return '<span style="color:red">已拒绝</span>';
			}
			return val;
		}
	}
}