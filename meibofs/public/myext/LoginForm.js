Ext.define('Ext.app.LoginWin', {
	extend: 'Ext.window.Window',
	initComponent: function(){
    	this.form =Ext.create('Ext.form.Panel', {
        	xtype:'form',
            url:'/AjaxSecure/authenticate',
            bodyStyle:'padding:5px 5px 0',
            fieldDefaults: {
                msgTarget: 'side',
                labelWidth: 75
            },
            border:false,
            defaultType: 'textfield',
            defaults: {
                anchor: '95%'
            },
            items: [{fieldLabel: '用户名',name: 'userName',allowBlank:false},
                    {fieldLabel: '密码',name: 'password',allowBlank:false,inputType:'password'},
                    {
                        xtype: 'container',
                        anchor: '100%',
                        layout:'column',
                        items:[{
                            xtype: 'container',
                            columnWidth:.5,
                            layout: 'anchor',
                            items: [{
                                xtype:'textfield',fieldLabel: '验证码',name: 'veriCode',anchor:'96%',enableKeyEvents:true,allowBlank:false,
                                listeners: {
                                	keyup:function(t, e, eOpts ) {
                                       if(e.getKey( )=="13"){
                                    	   if(form.isValid()){
		                           	        	form.getForm().submit({
		                           	                   success: function(form, action) {
		                           	                      window.location.href="/index";
		                           	                   },
		                           	                   failure: function(form, action) {
		                           	                       Ext.Msg.alert('认证失败', action.result.message);
		                           	                       Ext.getCmp('veriCode_com').getEl().dom.src='VeriCode/index?time'+(new Date().getTime());
		                           	                       form.reset();
		                           	                   }
		                           	               });
                                    	   }
                                       }
                                    }
                                }
                            }]
                        },{
                            xtype: 'container',
                            columnWidth:.3,
                            layout: 'anchor',
                            items: [
                            {xtype : 'component', 
    	                     anchor: '100%',
    	                     id:'veriCode_com',
       	                     autoEl:{tag:'img', src:'VeriCode/index'},
       	                     height:20
       	                     }]
                        },{
                            xtype: 'container',
                            columnWidth:.2,
                            layout: 'anchor',
                            items: [
                            {xtype : 'button', 
    	                     anchor: '90%',
       	                     text:'看不清',
       	                     handler:function(){
       	                    	 Ext.getCmp('veriCode_com').getEl().dom.src='VeriCode/index?time'+(new Date().getTime());
       	                     },
       	                     height:20
       	                     }]
                        }]
                    } ,
                    {xtype:'hidden',name:'item.id'}]
   
        });
    	var win=this;
    	var form=this.form;
    	Ext.apply(this, {
	        width: 320,
	        height: 180,
	        hidden: false,
	        title: '八达国际资金平台',
	        iconCls:'icon-login',
	        id:'loginWindow',
	        closable:false,
	        renderTo:Ext.getBody(),
	        items: [form],
	        modal:true,
	        buttonAlign:'center',
	        resetForm:function(){
        	   Ext.getCmp('veriCode_com').getEl().dom.src='VeriCode/index?time'+(new Date().getTime());
        	   form.getForm().reset();
	        },
	        buttons:[{xtype: 'button', text: '登录',iconCls:'icon-user',handler:function(){
	        	var loginform=form;
	        	form.getForm().submit({
	                   success: function(form, action) {
	                	   window.location.href="/index";
	                   },
	                   failure: function(form, action) {
	                       Ext.Msg.alert('认证失败', action.result.message);
	                       Ext.getCmp('veriCode_com').getEl().dom.src='VeriCode/index?time'+(new Date().getTime());
	                       loginform.getForm().reset();
	                   }
	               });
	        }},
	            {xtype: 'button', text: '重设',iconCls:'icon-reset',handler:function(){
	            	form.getForm().reset();
	            }}]
	    }); 
        this.callParent(arguments);
    }});