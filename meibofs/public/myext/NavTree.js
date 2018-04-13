Ext.define('Ext.app.NavTree', {
    extend: 'Ext.tree.Panel',
    uses: ['Ext.app.PortalPanel', 'Ext.app.PortalColumn'],
    initComponent: function(){
    	  Ext.define('ctreemodel', {  
    		    extend: 'Ext.data.Model',  
    		    fields: [  
    		        {name: 'attributes',  type: 'object'},
    		        {name: 'text',  type: 'string'}
    		    ]  
    		});
        Ext.apply(this, {
        	rootVisible: false,
            useArrows: true,
            frame: false,
            height:"100%",
            store:  Ext.create('Ext.data.TreeStore', {
	            proxy: {
	                type: 'ajax',
	                url: this.url,
	                extraParams :this.params
	            },
	            model:'ctreemodel',
	            sorters: [{
	            	property: 'id',
	                direction: 'ASC'
	            }]
	        }),
	        listeners:{
	        	itemclick:function(v,r){
	        		if(r.data.attributes.url){
	        			var tab0=Ext.getCmp('tab');
	        			var tabId="tabs"+r.data.id;
	        			var t=tab0.getComponent(tabId);
	        			if(t){
	        				tab0.setActiveTab(tabId);
	        				return ;
	        			}
	        			Ext.getCmp('tab').add({
	                        title: r.data.text,
	                        id:tabId,
	                        iconCls: 'icon-tabs',
	                        layout:'fit',
	                        closable:true,
	                        autoLoad: { url:r.data.attributes.url,params:{tabId:tabId},method: 'GET',scripts: true}
	                    });
	        			tab0.setActiveTab(tabId);
	        		}
	        	}
	        }
        }); 
        this.callParent(arguments);
    }
});