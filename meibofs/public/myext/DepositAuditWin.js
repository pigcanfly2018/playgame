Ext.define('Ext.app.Depauditwin', {
    extend: 'Ext.window.Window',
    initComponent: function(){
    	var url=this.url;
    	var act=this.act;
    	var cb=this.cb;
    	var htmlHeight=this.htmlHeight;
    	var width=this.width;
    	var height=this.height;
    	var form=Ext.create('Ext.form.Panel', {
	        bodyStyle:'padding:5px 5px 0',
	        width: '100%',
	        fieldDefaults: {
	            labelWidth: 75
	        },
	        items: [{
	            xtype: 'fieldset',
	            title: '存款详单',
	            anchor: '100%',
	            layout:'column',
	            bodyStyle:'margin:5px 5px 5px 5px',
	            items:[{
	            	xtype: 'panel',
	            	itemId:'more_panel',
	            	height:htmlHeight||180,
	            	loader:{ url:url,autoLoad:true}
	            }]
	        },{
	            xtype: 'fieldset',
	            title: '审核意见',
	            items:[{
	                       xtype: 'radiogroup',
	                       fieldLabel: '存款',
	                       labelStyle:'color:red;font-weight:bolder',
	                       vertical: true,
	                       items: [
	                           {boxLabel: '通过', name: 'flag', inputValue: 2},
	                           {boxLabel: '不通过', name: 'flag', inputValue: -2}
	                       ]
	                   },{
	                       xtype: 'radiogroup',
	                       fieldLabel: '存款红利',
	                       labelStyle:'color:red;font-weight:bolder',
	                       vertical: true,
	                       items: [{boxLabel: '通过', name: 'pdage_status', inputValue: 2},
	                               {boxLabel: '不通过', name: 'pdage_status', inputValue: -2}]
	                   },
	                   {fieldLabel: '意见',xtype:'textfield',name: 'remarks',anchor: '95%',labelStyle:'font-weight:bolder'}]
	        }],
        buttons:[{xtype: 'button',iconCls:'icon-save',text: '提交数据',handler:function(){
        	var form =this.up('form');
         	if (form.isValid()) {
         		var win=this.up('window');
         		if(win)win.mask();
                 form.submit({
                	   url:act,
                       success: function(f, action) {
                    	  if(win)win.unmask();
                          Ext.Msg.alert('保存成功', action.result.message);
                          if(cb){
                        	  cb();
                          }
                          form.up('window').hide();
                       },
                       failure: function(form, action) {
                    	   if(win)win.unmask();
                           Ext.Msg.alert('保存失败', action.result.message);
                       }
                   });
              }
        }},
	    	     {xtype: 'button',iconCls:'icon-close',text: '关闭窗口',handler:function(){this.up('window').destroy();}}]
     });
        Ext.apply(this, {
           closable: true,
           items:[form],
           closeAction: 'destroy',
           iconCls:'icon-window',
           width: width||700,
           height: height||440,
           layout: 'fit',
           bodyStyle: 'padding: 5px;'
        }); 
        this.callParent(arguments);
    }
});