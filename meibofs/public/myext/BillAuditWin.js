Ext.define('Ext.app.Billauditwin', {
    extend: 'Ext.window.Window',
    initComponent: function(){
    	var url=this.url;
    	var act=this.act;
    	var cb=this.cb;
    	var fstitle=this.fstitle;
    	var htmlHeight=this.htmlHeight;
    	var width=this.width;
    	var height=this.height;
    	var tipTitle=this.tipTitle;
    	var radioTitle=this.radioTitle;
    	var radioYesTitle=this.radioYesTitle;
    	var radioNoTitle=this.radioNoTitle;
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
	            	height:htmlHeight||220,
	            	loader:{url:url,autoLoad:true}
	            }]
	        },{
	            xtype: 'fieldset',
	            title: tipTitle||'审核意见 / Reason',
	            items:[{
	                       xtype: 'radiogroup',
	                       fieldLabel: radioTitle||'审核 / Auditing',
	                       labelWidth:150,
	                       labelStyle:'color:red;font-weight:bolder',
	                       vertical: true,
	                       items: [
	                           {boxLabel: radioYesTitle||'通过 / Successful', name: 'flag', inputValue: 2},
	                           {boxLabel: radioNoTitle||'不通过  / Reject', name: 'flag', inputValue: -2}
	                       ]
	                   },
	                   {fieldLabel: '意见 / Reason',labelWidth:150,xtype:'textfield',name: 'remarks',anchor: '95%',labelStyle:'color:red;font-weight:bolder'}]
	        }],
        buttons:[{xtype: 'button',iconCls:'icon-save',text: '提交数据 / Submit',handler:function(){
        	
        	var form =this.up('form');
        	var win=this.up('window');
        	if(win)win.mask();
         	if (form.isValid()) {
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
	    	     {xtype: 'button',iconCls:'icon-close',text: '关闭窗口 / Close',handler:function(){this.up('window').destroy();}}]
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