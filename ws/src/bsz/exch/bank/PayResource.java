package bsz.exch.bank;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;

public class PayResource {
	private ResourceBundle rb;
	

	
	public static PayResource instance(){
		   if(resource==null){
			   resource = new PayResource();
		   }
		   return resource;
	}
	private static PayResource resource = new PayResource();
	private  PayResource(){
		rb= ResourceBundle.getBundle("dpay");
		Enumeration<String> keys=rb.getKeys();
		while (keys.hasMoreElements()) {
			String k=keys.nextElement();
			if(k.endsWith(".pre")){
				String[] kk=k.split("[.]{1,1}");
				String product=kk[1];
				pre_ds_map.put(rb.getString("dpay."+product+".pre"), product);
			}
			if(k.endsWith(".company_id")){
				String[] kk=k.split("[.]{1,1}");
				String product=kk[1];
				company_ds_map.put(rb.getString("dpay."+product+".company_id"), product);
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
	private HashMap<String,String> company_ds_map=new HashMap<String,String>();
	
	public String findProduct(String pre){
		return pre_ds_map.get(pre);
	}
	public String findProductByCompany(String company_id){
		return company_ds_map.get(company_id);
	}
	public static void main(String []args){
		String k="dpay.8da.company_id";
		String [] a=k.split("[.]{1,1}");
		for(String s:a){
			System.out.println(s);
		}
		System.out.print(PayResource.instance().findProductByCompany("10"));
		
	}
	
}
