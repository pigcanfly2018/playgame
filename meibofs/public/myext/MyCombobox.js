Ext.define('Ext.app.combobox.RoleCom', {
	extend: 'Ext.form.ComboBox',
	fieldLabel:'任务组',
	xtype:'rolecom',
	valueField:'rolecode',
	displayField:'rolename',
	store:{
		fields: [
	                 {name: 'rolecode', type: 'string'},
	                 {name: 'rolename',  type: 'rolename'}
	             ],
        proxy: {
            type: 'ajax',
            url: '/ComboboxApp/getRoles'
        },
        autoLoad: true
    },
    triggerAction: 'all',
    emptyText:'---请选择---',
    submitEmptyText:false,
    queryMode: 'local',
    initComponent: function(){
    	  this.callParent(arguments);
    	
    }});


Ext.define('Ext.app.combobox.ItemCom', {
	extend: 'Ext.form.ComboBox',
	xtype:'itemcom',
	valueField:'itemcode',
	displayField:'itemname',
    triggerAction: 'all',
    emptyText:'---请选择---',
    defaultValue:'',
    queryMode: 'local',
    submitEmptyText:false,
    initComponent: function(){
    	Ext.apply(this, {
    		store:{
    			fields: [
    		                 {name: 'itemcode',  type: 'string'},
    		                 {name: 'itemname', type: 'string'},
    		                 {name: 'itemvalue', type: 'string'}
    		             ],
    	        proxy: {
    	            type: 'ajax',
    	            url: '/ComboboxApp/getItems',
    	            extraParams:{
    	            	code:this.groupcode
    	            }
    	            
    	        },
    	        autoLoad: true
    	    }
        });
    	  this.callParent(arguments);
    	
    }});

Ext.define('Ext.app.combobox.CustCom', {
	extend: 'Ext.form.ComboBox',
	fieldLabel:'客户名',
	xtype:'custcom',
	valueField:'cust_id',
	displayField:'login_name',
	minChars:1,
	store:{
        proxy: {
        type: 'ajax',
        url : '/ComboboxApp/getCustomersBySortName',
        reader: {
            type: 'json',
            root: 'data',
            idProperty: 'id',
            totalProperty: 'total'
        }
    },
    pageSize: 10,
    model: 'Customer'
	},
    triggerAction: 'all',
    emptyText:'---请选择---',
    submitEmptyText:false,
    queryMode: 'remote',
    typeAhead: false,
    hideTrigger:true,
    anchor: '90%',
    listConfig: {
        loadingText: '正在查找...',
        emptyText: '没发现匹配数据.',
        getInnerTpl: function() {
            return '{login_name}({real_name})';
        }
    },
    pageSize: 10,
    initComponent: function(){
    	  this.callParent(arguments);
    	
    }});


Ext.define('Ext.app.combobox.AgentCom', {
	extend: 'Ext.form.ComboBox',
	fieldLabel:'代理名',
	xtype:'newagentcom',
	valueField:'agent_id',
	displayField:'login_name',
	minChars:1,
	store:{
        proxy: {
        type: 'ajax',
        url : '/ComboboxApp/getAgentsBySortedName',
        reader: {
            type: 'json',
            root: 'data',
            idProperty: 'id',
            totalProperty: 'total'
        }
    },
    pageSize: 10,
    model: 'Agent'
	},
    triggerAction: 'all',
    emptyText:'---请选择---',
    submitEmptyText:false,
    queryMode: 'remote',
    typeAhead: false,
    hideTrigger:true,
    anchor: '90%',
    listConfig: {
        loadingText: '正在查找...',
        emptyText: '没发现匹配数据.',
        getInnerTpl: function() {
            return '{login_name}({real_name})';
        }
    },
    pageSize: 10,
    initComponent: function(){
    	  this.callParent(arguments);
    	
    }});


Ext.define('Ext.app.combobox.agentCom', {
	extend: 'Ext.form.ComboBox',
	fieldLabel:'客户名',
	xtype:'agentcom',
	valueField:'cust_id',
	displayField:'login_name',
	minChars:1,
	store:{
        proxy: {
        type: 'ajax',
        url : '/ComboboxApp/getAgentsBySortName',
        reader: {
            type: 'json',
            root: 'data',
            idProperty: 'id',
            totalProperty: 'total'
        }
    },
    pageSize: 10,
    model: 'Customer'
	},
    triggerAction: 'all',
    emptyText:'---请选择---',
    submitEmptyText:false,
    queryMode: 'remote',
    typeAhead: false,
    hideTrigger:true,
    anchor: '90%',
    listConfig: {
        loadingText: '正在查找...',
        emptyText: '没发现匹配数据.',
        getInnerTpl: function() {
            return '{login_name}({real_name})';
        }
    },
    pageSize: 10,
    initComponent: function(){
    	  this.callParent(arguments);
    	
    }});


