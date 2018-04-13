package models;

import java.math.BigDecimal;
import java.util.Date;

public class YeeOrder {
	public Date create_date;
	public String login_name;
	public String phone;
    public String order_no;
    public BigDecimal credit;
    public String frpid;
    public BigDecimal pay_credit;
    public BigDecimal target_fee;
    public Boolean done_bs;
    public String pay_date;   

}
