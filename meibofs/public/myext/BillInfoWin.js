Ext.define('Ext.app.Billinfowin', {
    extend: 'Ext.window.Window',
    initComponent: function(){
    	var url=this.url;
    	var act=this.act;
    	var cb=this.cb;
    	var fstitle=this.fstitle;
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
	        }],
        buttons:[{xtype: 'button',iconCls:'icon-close',text: '关闭窗口',handler:function(){this.up('window').destroy();}}]
     });
        Ext.apply(this, {
           closable: true,
           items:[form],
           closeAction: 'destroy',
           iconCls:'icon-window',
           width: width||700,
           height: height||360,
           layout: 'fit',
           bodyStyle: 'padding: 5px;'
        }); 
        this.callParent(arguments);
    }
});