Ext.define('Ext.app.PhoneRecordwin', {
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
	            
	            anchor: '100%',
	            layout:'column',
	            bodyStyle:'margin:0px 0px 0px 0px',
	            items:[{
	            	xtype: 'panel',
	            	itemId:'more_panel',
	            	height:htmlHeight||1,
	            	loader:{ url:url,autoLoad:true}
	            }]
	        },{
	            xtype: 'fieldset',
	            title: '电话记录备注',
	            items:[
	                   {fieldLabel: '通话备注',xtype:'textfield',name: 'content',anchor: '95%',labelStyle:'font-weight:bolder'}]
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
           height: height||140,
           layout: 'fit',
           bodyStyle: 'padding: 5px;'
        }); 
        this.callParent(arguments);
    }
});