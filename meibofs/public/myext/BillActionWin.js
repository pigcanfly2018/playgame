Ext.define('Ext.app.Billactionwin', {
    extend: 'Ext.window.Window',
    initComponent: function(){
    	var url=this.url;
    	var act=this.act;
    	var cb=this.cb;
    	var fstitle=this.fstitle;
    	var msg=this.msg;
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
	            title: fstitle,
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
	            title: '确认信息',
	            items:[{
	            	xtype: 'panel',
	            	height:40,
	            	html:'<span style="color:red;font-weight:bolder">'+msg+'</span>'
	            },
	           {fieldLabel: '意见',xtype:'textfield',name: 'remarks',anchor: '95%',labelStyle:'color:red;font-weight:bolder'}]
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