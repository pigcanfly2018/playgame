Ext.require([
    'Ext.data.*',
    'Ext.grid.*',
    'Ext.tree.*'
]);
Ext.define('Ext.app.combobox.TreeCom',{  
    extend : 'Ext.form.field.ComboBox',  
    xtype:'treecom',
    store : new Ext.data.ArrayStore({fields:[],data:[[]]}),  
    editable : false,  
    allowBlank:false,  
    listConfig : {resizable:true,minWidth:250,maxWidth:450},  
    _idValue : null,  
    _txtValue : null,  
    callback : Ext.emptyFn,  
    treeObj :null,  
    initComponent : function(){  
    	
    	var store=Ext.create('Ext.data.TreeStore', {
	    	expanded: true,
	        proxy: {
	            type: 'ajax',
	            url: '/ComboboxApp/getFuncsTree'
	        }
		});
        this.treestore=store;
    	this.treeObj= Ext.create('Ext.tree.Panel',{  
            border : false,  
            height : 250,  
            autoScroll : true,  
            rootVisible: false, 
            viewConfig:{
            	singleSelect:true
            },
            store:store
            
    	});
        this.treeRenderId = Ext.id();  
        this.tpl = "<tpl><div id='"+this.treeRenderId+"'></div></tpl>";       
        this.callParent(arguments);  
        this.on({  
            'expand' : function(){  
                if(!this.treeObj.rendered&&this.treeObj&&!this.readOnly){  
                    Ext.defer(function(){  
                        this.treeObj.render(this.treeRenderId);  
                    },300,this);  
                }     
            }  
        });
        this.treeObj.on('itemclick',function(view,rec){  
            if(rec){  
                this.setValue(this._txtValue = rec.get('text'));  
                this._idValue = rec.get('id');  
                //设置回调  
                this.callback.call(this,rec.get('id'), rec.get('text'));  
                //关闭tree  
                this.collapse();  
            }  
        },this);
    },  
    getValue : function(){//获取id值  
        return this._idValue;  
    },  
    getTextValue : function(){//获取text值  
        return this._txtValue;  
    },
    setValue:function(v,s){
    	var record = this.treeObj.getRootNode().findChild('id',v,true);
        if(record){
        	this._idValue = v;  
        	this.setRawValue(record.get("text"));
        }else{
    	    this.setRawValue(v);
        }
    },
    setLocalValue : function(txt,id){//设值  
    	if(!id){
    		return;
    	}
        this._idValue = id;  
        this.setValue(this._txtValue = txt);  
    }  
});  