package models;

import java.math.BigDecimal;
import java.util.List;

import util.Sp;

public class EggTrophy {
	
	public Long trophy_id;
    public String trophy_name;
    public String trophy_code;
    public Integer trophy_count;
    public String egg;
    public Boolean is_default;
    public Boolean enable;
    public BigDecimal cost;
    public String trophy_type;
    
     public  boolean NTcreat(){
		  String sql="insert into  mb_egg_trophy (trophy_name,trophy_code,trophy_count,egg,is_default,enable,cost,trophy_type) values(?,?,?,?,?,?,?,?)";
		  int c=Sp.get().getDefaultJdbc().update(sql, new Object[]{trophy_name,trophy_code,trophy_count,egg,is_default,enable,cost,trophy_type});
		  return c>0;
	 }
	 public  boolean NTupdate(){
		  String sql="update mb_egg_trophy set trophy_name=?,trophy_code=?,trophy_count=?,egg=?,is_default=?,enable=?,cost=?,trophy_type=? where trophy_id=?";
		  int c=Sp.get().getDefaultJdbc().update(sql, new Object[]{trophy_name,trophy_code,trophy_count,egg,is_default,enable,cost,trophy_type,trophy_id});
		  return c>0;
	 }
	 
	 public static  EggTrophy getRandom(String egg){
		 String sql="select * from mb_egg_trophy where egg =? and enable is true order by rand() limit 1";
		 List<EggTrophy> pregList = Sp.get().getDefaultJdbc().query(sql,new Object[]{egg},new EggTrophyRowMap());
	     if(pregList.size()>0){
		   return pregList.get(0);
	     }
		   return null;
	 }
	 public static  EggTrophy getRandomDefault(String egg){
		 String sql="select * from mb_egg_trophy where egg =? and is_default is true and enable is true order by rand() limit 1";
		 List<EggTrophy> pregList = Sp.get().getDefaultJdbc().query(sql,new Object[]{egg},new EggTrophyRowMap());
	     if(pregList.size()>0){
		   return pregList.get(0);
	     }
		   return null;
	 }
	 
	 public static boolean NTexits(String trophy_code){
		  int count=Sp.get().getDefaultJdbc().queryForObject("select count(1) from  mb_egg_trophy where trophy_code=? ",
					new Object[]{trophy_code},Integer.class);
		  return count>0;
	 }
	 
	 public static boolean NTexits(String trophy_code,Long trophy_id){
		  int count=Sp.get().getDefaultJdbc().queryForObject("select count(1) from  mb_egg_trophy where trophy_code=? and trophy_id <> ? ",
					new Object[]{trophy_code,trophy_id},Integer.class);
		  return count>0;
	 }
	 
	 public static boolean NTdelete(Long trophy_id){
		 int count=Sp.get().getDefaultJdbc().update("delete from mb_egg_trophy where trophy_id=? ", new Object[]{trophy_id});
		  return count>0;
		 
	 }

}
