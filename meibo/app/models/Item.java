package models;

import java.util.Date;
import java.util.List;

import util.Sp;

public class Item {

    public String itemname;
 
    public String itemcode;
 
    public String itemvalue;
 
    public String groupcode;
 
    public Date createdate;
 
    public String createuser;
    
    public String startdate;
    
    public String enddate;
    
    public Integer pcflag;
    
    public Integer mobileflag;
    
    public String actbill;
    
    public Integer moneyflag;
    
	public int NTexits(String itemcode){
		 int count=Sp.get().getDefaultJdbc().queryForObject("select count(1) from  mb_item where itemcode=? ",
					new Object[]{itemcode},Integer.class);
		 return count;
	 }
	 
	 public  boolean NTcreat(){
		  String sql="insert into  mb_item (itemname,itemcode,itemvalue,groupcode,createdate,createuser) values(?,?,?,?,?,?)";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{itemname,itemcode,itemvalue,groupcode,createdate,createuser,});
		  return count>0;
	 }
	 
	 public boolean NTupdate(){
		 String sql="update mb_item set itemname=?,itemvalue=?,groupcode=? where itemcode=?";
		 int count=Sp.get().getDefaultJdbc().update(sql,
					new Object[]{itemname,itemvalue,groupcode,itemcode});
		 return count>0;
	 }
	 
	 public boolean NTdelete(String itemcode){
		 String sql="delete from  mb_item  where itemcode=?";
		 int count=Sp.get().getDefaultJdbc().update(sql,
					new Object[]{itemcode});
		 return count>0;
	 }
	 
	 public static List<Item> NTbygroupCode(String groupcode){
		 String sql="select * from mb_item where groupcode=? order by itemcode";
			List<Item> roleList=Sp.get().getDefaultJdbc().query(sql,new Object[]{groupcode},new ItemRowMap());
		return roleList;
	 }
	 
	 public static List<Item> NTgetAll(){
		 List<Item> itemList=Sp.get().getDefaultJdbc().query("select * from mb_item order by itemcode",new ItemRowMap());
		 return itemList;
	 }
	 
    

}
