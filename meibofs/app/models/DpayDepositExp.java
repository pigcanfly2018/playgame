package models;

import java.math.BigDecimal;
import java.util.Date;

import util.Sp;

public class DpayDepositExp {

	public Long dpay_id;
	
	public String exception_order_num;
	
	public String company_id;
	
	public String exact_payment_bank;
	
	public String pay_card_name;
	
	public String pay_card_num;
	
	public String receiving_bank;
	
	public String receiving_account_name;
	
	public String channel;
	
	public String note;
	
	public String area;
	
	public String exact_time;
	
	public BigDecimal amount;
	
	public BigDecimal fee;
	
	public BigDecimal transaction_charge;
	
	public String base_info;
	
	public Date create_date;
	
	public Integer flag;
	
	public String login_name;
	
	public String action_user;
	
	public Date claim_date;
	
	public Long pic_id;
	
	public Long pic2_id;
	
	public String action_user2;
	
	public Date claim_date2;
	
	public String remark;
	
	public BigDecimal poundage;
	
	public static boolean apply(DpayDepositExp dpayDepositExp){
		 String sql ="update mb_dpay_deposit_exp set claim_date2=now(),flag=?,login_name=?,pic_id=?,pic2_id=?,action_user2=?,remark=? where dpay_id=?";
		 int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{dpayDepositExp.flag,dpayDepositExp.login_name,dpayDepositExp.pic_id,dpayDepositExp.pic2_id,dpayDepositExp.action_user2,dpayDepositExp.remark,dpayDepositExp.dpay_id});
		 return count>0;
	}
	
	public static DpayDepositExp NTget(Long dpay_id){
	      String sql ="select * from mb_dpay_deposit_exp where dpay_id=?";
	      DpayDepositExp dep=(DpayDepositExp) Sp.get().getDefaultJdbc().queryForObject(sql,
					new Object[]{dpay_id},new DpayDepositExpRowMap());
	      return dep;
	}
	
	public static boolean NTcancle(DpayDepositExp dpayDepositExp){
		String sql="update mb_dpay_deposit_exp   set flag = -1 where dpay_id=?";
		 int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{dpayDepositExp.dpay_id});
		 return count>0;
	}
	
	public static boolean audit(DpayDepositExp dpayDepositExp){
		  String sql ="update mb_dpay_deposit_exp set claim_date=now(),flag=?,action_user=? where dpay_id=?";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{dpayDepositExp.flag,dpayDepositExp.action_user,dpayDepositExp.dpay_id});
		  return count>0;
	}
	
	
	 
}
