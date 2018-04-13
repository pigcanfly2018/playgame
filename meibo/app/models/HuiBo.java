package models;

import java.math.BigDecimal;
import java.util.Date;

import util.Sp;

public class HuiBo {

	public Long hbp_id;
	public String pay_id;
	public String login_name;
	public BigDecimal amount;
	public String product_name;
	public String bank_code;
	public String remark;
	public Date create_date;
	public String order_no;
	public BigDecimal real_amount;
	public Integer state;
	public Date modify_time;
	public String payer_name;
	public String real_pay_method;
	public Date finished_date;
	
	public static boolean NTupdatePwd(String pay_id){
		 String sql="update mb_hbp set state=2 where pay_id=?";
		 int count=Sp.get().getDefaultJdbc().update(sql,
					new Object[]{pay_id});
		 return count>0;
	}
	
	public static boolean NTExist(String login_name){
		return false;
		/*
		 if(login_name.equals("g000428") || login_name.equals("yangjian8") || login_name.equals("xuguoqung")){
			 return false;
		 }
		 String sql="select count(1) from  mb_hbp where login_name=? and amount >= 1000 and state=2 ";
		 int count=Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{login_name},Integer.class);
		 return count>0;*/
	}
	
}
