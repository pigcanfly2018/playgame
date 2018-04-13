package models;

import java.util.Date;
import java.util.List;

import util.Sp;

public class Item {

    public String itemname;
 
    public String itemcode;
 
    public String itemvalue;
 
    public String groupcode;
    
    public String platform;
    
    public Boolean frozenflag;
 
    public Date createdate;
 
    public String createuser;
    //优惠字段
    public String startdate;
    
    public String enddate;
    
    public Boolean pcflag;
    
    public Boolean mobileflag;
    
    public String actbill;
    
    public Boolean moneyflag;
    

	public int NTexits(String itemcode){
		 int count=Sp.get().getDefaultJdbc().queryForObject("select count(1) from  mb_item where itemcode=? ",
					new Object[]{itemcode},Integer.class);
		 return count;
	 }
	
	public int NTNameexits(String itemname){
		 int count=Sp.get().getDefaultJdbc().queryForObject("select count(1) from  mb_item where itemname=? ",
					new Object[]{itemname},Integer.class);
		 return count;
	 }
	
	public static Item getItemByItemname(String itemname){
		String sql="select * from mb_item where itemname =?";
		  List<Item> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{itemname},new ItemRowMap());
		  if(list.size()>0){
			  return list.get(0);
		  }
		  return null;
	}
	 
	 public  boolean NTcreat(){
		  String sql="insert into  mb_item (itemname,itemcode,itemvalue,groupcode,platform,frozenflag,createdate,"
		  		+ "createuser,startdate,enddate,pcflag,mobileflag,actbill,moneyflag) "
		  		+ "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{itemname,itemcode,itemvalue,groupcode,platform,frozenflag,createdate,createuser,
				  startdate,enddate,pcflag,mobileflag,actbill,moneyflag});
		  return count>0;
	 }
	 
	 public boolean NTupdate(){
		 String sql="update mb_item set itemname=?,itemvalue=?,groupcode=?,platform=?,frozenflag=?,startdate=?,"
		 		+ "enddate=?,pcflag=?,mobileflag=?,actbill=?,moneyflag=? where itemcode=?";
		 int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{itemname,itemvalue,groupcode,platform,frozenflag,startdate,enddate,pcflag,mobileflag,actbill,moneyflag,itemcode});
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
