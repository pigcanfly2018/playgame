package models;

import java.util.Date;
import java.util.List;

import util.Sp;

public class Bettime {

	public int id;
	public String platform;
	public String date;
	public String time;
	
	public  Bettime getBettime(String platform){
		  String sql="select * from mb_bettime where platform =?";
		  List<Bettime> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{platform},new BettimeRowMap());
		  if(list.size()>0){
			  return list.get(0);
		  }
		  return null;
	 }
	
	
	public  boolean modifyTime(){
		 String sql="update mb_bettime set date=?,time=? where platform=?";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{date,time,platform});
		 return count>0;
	 }
	
}
