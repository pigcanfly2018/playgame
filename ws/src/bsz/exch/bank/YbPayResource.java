package bsz.exch.bank;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;

public class YbPayResource {
	private ResourceBundle rb;
	
	public static YbPayResource instance(){
		   if(resource==null){
			   resource = new YbPayResource();
		   }
		   return resource;
	}
	private static YbPayResource resource = new YbPayResource();
	private  YbPayResource(){
		rb= ResourceBundle.getBundle("ybpay");
		Enumeration<String> keys=rb.getKeys();
		while (keys.hasMoreElements()) {
			String k=keys.nextElement();
			if(k.endsWith(".pre")){
				String[] kk=k.split("[.]{1,1}");
				String product=kk[1];
				pre_ds_map.put(rb.getString("ybpay."+product+".pre"), product);
			}
        }
		
	}
	public String getConfig(String name){
		if(rb.containsKey(name)){
		   return rb.getString(name);
		}else{
			return null;
		}
	}
	
	private HashMap<String,String> pre_ds_map=new HashMap<String,String>();

	
	public String findProduct(String pre){
		return pre_ds_map.get(pre);
	}
	
}
