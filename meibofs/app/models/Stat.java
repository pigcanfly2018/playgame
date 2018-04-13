package models;

import java.math.BigDecimal;
import java.util.Date;

public class Stat {
	public Long stat_id;
    public Date stat_date;
    public Integer cust_count1;
    public Integer cust_count2;
    public BigDecimal deposit_count;
    public BigDecimal poundage_count;
    public BigDecimal withdraw_count;
    public BigDecimal gift_count;
    
    
    //辅助字段
    public String stat_mon;
    
  
    
    
}
