package models;

import java.util.List;

import util.Sp;

public class Bethistory {

	public long id;
	public String filename;
	public Long modifytime;
	
	public  boolean NTcreat(){
		  String sql="insert into  mb_bethistory (filename,modifytime) values(?,?)";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{filename,modifytime});
		  return count>0;
	}
	
	public static boolean NTexitsHistory(String filename){
		   String sql="select count(1) from mb_bethistory where filename=? ";
		   int count=Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{filename},Integer.class);
		   return count>0;
	}
	
	public  static Bethistory getBethistory(String filename){
		  String sql="select * from mb_bethistory where filename =?";
		  List<Bethistory> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{filename},new BethistoryRowMap());
		  if(list.size()>0){
			  return list.get(0);
		  }
		  return null;
	 }
	
	public  boolean modifyTime(){
		 String sql="update mb_bethistory set modifytime=? where filename=?";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{modifytime,filename});
		 return count>0;
	 }
	
}
