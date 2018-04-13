package models;

import java.math.BigDecimal;
import java.util.Date;

import util.Sp;

public class RPN {

	public Long rpn_id;
	public String order_id;
	public String login_name;
	public BigDecimal amount;
	public String customer_ip;
	public String bank_id;
	public String remark;
	public Date create_date;
	public String rpn_order_no;
	public BigDecimal real_amount;
	public Integer state;
	public Date modify_time;
	public String payer_name;
	public String real_pay_method;
	public Date finished_date;
	
	 public static boolean NTupdatePwd(String order_id){
		 String sql="update mb_rpn set state=2 where order_id=?";
		 int count=Sp.get().getDefaultJdbc().update(sql,
					new Object[]{order_id});
		 return count>0;
	 }
	 
}
