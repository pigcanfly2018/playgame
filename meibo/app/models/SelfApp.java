package models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import bsz.exch.service.Result;
import bsz.exch.service.Task;
import service.MeiBoService;
import util.JSONResult;
import util.MyRandom;
import util.Sp;

public class SelfApp {
	
	public int id;
    public String login_name;
    public String app_name;
    public Date create_time;
    public Integer status;
    public String gift_no;
    public String remark;
    
	public static boolean createSelfApp(SelfApp sa) {
		// TODO Auto-generated method stub
		  String sql="insert into mb_self_app (login_name,app_name,create_time,status,gift_no,remark) "
			  		+ "values(?,?,now(),?,?,?)";
			  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{sa.login_name,sa.app_name,1,sa.gift_no,sa.remark});
			  if(count>0){
				  //插入礼金表数据
				  CashGift gift = new CashGift();
				  gift.gift_code=MyRandom.getRandom(8);//上传编号
				  gift.status=1;
				  gift.cs_date=new Date(System.currentTimeMillis());
				  gift.login_name=sa.login_name;
				  gift.gift_type=sa.app_name;
				  gift.create_user=sa.login_name;
			      gift.create_date=new Date(System.currentTimeMillis());
			      gift.gift_no=sa.gift_no;
			      gift.remarks=sa.remark;
			      
			      Customer customer = MeiBoService.getCustomerByUserName(sa.login_name);
			      gift.cust_id=customer.cust_id;
			      gift.real_name=customer.real_name;
			      gift.cust_level=customer.cust_level;
			      gift.kh_date=customer.create_date;
			      if(gift.NTcreat()==null){
			    		 return false;
			      }
			  }
			  return count>0;
	}

	public static Boolean getdic(String app_name) {
		// TODO Auto-generated method stub
		Task task =Task.create("query_dist_check_bygroupcode_8d");
		task.addParam("app_name", app_name);
		Result result = task.perform();
		if(result.success()){
			return result.getIntOfRes()>0;
		}
		return false;
		
		
	}
   
    /*public  boolean NTcreat(){
		  String sql="insert into  mb_ranking_list (create_date,login_name,trophy_name,trophy_code,trophy_count,cost,cust_id) "
		  		+ "values(now(),?,?,?,?,?,?)";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{login_name,trophy_name,trophy_code,trophy_count,cost,cust_id});
		  return count>0;
	}
   
     
     public static boolean NTdelete(Long ranking_id){
    	 
    	 String sql="delete from mb_ranking_list where ranking_id=?";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{ranking_id});
		  return count>0;
     }
     public static List<SelfApp>  rankingList(){
		 String sql="SELECT * FROM mb_ranking_list WHERE DATE_SUB(CURDATE(), INTERVAL 7 DAY) <= DATE(create_date) ORDER BY create_date DESC limit 100";
    	 List<SelfApp> giftList= Sp.get().getDefaultJdbc().query(sql, new Object[]{}, new RankingListRowMap());
    	 return giftList;
	}*/
}
