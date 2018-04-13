package models;

import java.math.BigDecimal;
import java.util.Date;

import util.Sp;

public class XunFuTong {

	public Long xftp_id;
	public String pay_id;
	public String login_name;
	public BigDecimal amount;
	public String payip;
	public String pay_method;
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
		 String sql="update mb_xftp set state=2 where pay_id=?";
		 int count=Sp.get().getDefaultJdbc().update(sql,
					new Object[]{pay_id});
		 return count>0;
	}
	
	
}
