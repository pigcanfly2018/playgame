package models;

import java.math.BigDecimal;
import java.util.Date;

public class CustomerView {
	public String login_name;
	public String real_name;
	public String phone;
	public String qq;
	public Date create_date;
	public int deposit_cnt;
	public BigDecimal deposit_amount;
	public Date last_deposit_date;
	public int withdraw_cnt;
	public BigDecimal withdraw_amount;
	public Date last_withdraw_date;
	public Date login_date;
}


/*
SELECT a.login_name login_name,a.real_name real_name,a.tel tel,a.qq qq,a.create_date create_date,
b.cnt  deposit_cnt,b.amount deposit_amount,b.last_deposit_date last_deposit_date,
d.cnt withdraw_cnt,d.amount withdraw_amount,d.last_withdraw_date last_withdraw_date
FROM (SELECT login_name,real_name,tel,qq,create_date FROM sb_customer WHERE cust_level >0) a LEFT JOIN 
(SELECT COUNT(1) cnt ,SUM(amount) amount,MAX(create_date) last_deposit_date, login_name  FROM sb_deposit WHERE STATUS=3 GROUP BY  login_name) b
ON a.login_name =b.login_name
LEFT JOIN (SELECT COUNT(1) cnt ,SUM(amount) amount,MAX(create_date) last_withdraw_date, login_name  FROM sb_withdraw WHERE STATUS=5 GROUP BY  login_name) d
ON a.login_name =d.login_name;*/