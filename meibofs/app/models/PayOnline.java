package models;

import util.Sp;

public class PayOnline {

	public Long payonline_id;
	
	public String name;
	
	public String value;
	
	public String submit_value;
	
	public String return_value;
	
	public String notify_value;
	
	public String valuedescribe;
	
	public String req_referer;
	
	
	public static  PayOnline NTget(Long payonline_id){
		PayOnline payOnline=(PayOnline) Sp.get().getDefaultJdbc().queryForObject("select * from  mb_payonline where payonline_id=? ",
				new Object[]{payonline_id},new PayOnlineRowMap());
		return payOnline;
	 }
	
	public static boolean NTexits(String name){ 
		String sql="select count(1) from mb_payonline where name=?";
		int count=Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{name},Integer.class);
		return count>0;
	}
	
	 public  boolean NTcreat(){
		  String sql="insert into mb_payonline(name,value,submit_value,return_value,notify_value,valuedescribe,req_referer)" +
		 		" values(?,?,?,?,?,?,?)";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{name,value,submit_value,return_value,notify_value,valuedescribe,req_referer});
		  return count>0;
	 }
	 
	 public boolean NTupdate(){
		 String sql="update mb_payonline set name=?,value=?,submit_value=?,return_value=?,notify_value=?,valuedescribe=?,req_referer=? where payonline_id=?";
		 int count=Sp.get().getDefaultJdbc().update(sql,
					new Object[]{name,value,submit_value,return_value,notify_value,valuedescribe,req_referer,payonline_id});
		 return count>0;
	 }
	 
	 
	
}
