package bsz.exch.bank;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;

public class JbPayResource {
	private ResourceBundle rb;
	
	public static JbPayResource instance(){
		   if(resource==null){
			   resource = new JbPayResource();
		   }
		   return resource;
	}
	private static JbPayResource resource = new JbPayResource();
	private  JbPayResource(){
		rb= ResourceBundle.getBundle("jbpay");
		Enumeration<String> keys=rb.getKeys();
		while (keys.hasMoreElements()) {
			String k=keys.nextElement();
			if(k.endsWith(".pre")){
				String[] kk=k.split("[.]{1,1}");
				String product=kk[1];
				pre_ds_map.put(rb.getString("jbpay."+product+".pre"), product);
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
	
	public static void main(String []args){
		String k="jbpay.8da.pre";
		String [] a=k.split("[.]{1,1}");
		for(String s:a){
			System.out.println(s);
		}

		
	}
	
}
