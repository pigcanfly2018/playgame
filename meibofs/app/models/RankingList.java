package models;

import java.math.BigDecimal;
import java.util.Date;

import util.Sp;

public class RankingList {
	
	public Long ranking_id;
    public Date create_date;
    public String login_name;
    public String trophy_name;
    public String trophy_code;
    public Integer trophy_count;
    public BigDecimal cost;
    public Long cust_id;
     public  boolean NTcreat(){
		  String sql="insert into  mb_ranking_list (create_date,login_name,trophy_name,trophy_code,trophy_count,cost,cust_id) "
		  		+ "values(now(),?,?,?,?,?,?)";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{login_name,trophy_name,trophy_code,trophy_count,cost,cust_id});
		  return count>0;
	}
     public  boolean NTupdate(){
		  String sql="update mb_ranking_list set login_name=?, trophy_name=?,trophy_code=?,trophy_count=?,cost=?,cust_id=? where ranking_id=?";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{login_name,trophy_name,trophy_code,trophy_count,cost,cust_id,ranking_id});
		  return count>0;
	}
     
     public static boolean NTdelete(Long ranking_id){
    	 
    	 String sql="delete from mb_ranking_list where ranking_id=?";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{ranking_id});
		  return count>0;
     }

}
