package models;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import util.DateUtil;
import util.Sp;

public class YearGift {
	
	public Long year_gift_id;
	
	public String day;
	
	public Long cust_id;
	
	public String login_name;
	
	public BigDecimal credit;
	
	public Date create_date;
	
	public String gift_no;
	
	public Integer flag;
	
	
	public static Long create(Long cust_id,String login_name,BigDecimal credit,String no,Integer flag){
		     
		      String day =DateUtil.dateToString(new Date(), "yyyyMMddHH");
		      String sql="insert into  mb_year_gift (day,cust_id,login_name,credit,create_date,gift_no,flag) values(?,?,?,?,now(),?,?)";
			  Object[] objects =new Object[]{day,cust_id,login_name,credit,no,0};
			  KeyHolder keyHolder = new GeneratedKeyHolder();
			  Sp.get().getDefaultJdbc().update(new MyPreparedStatementCreator(sql,objects), keyHolder);
			  return keyHolder.getKey().longValue();
	}
	
	public static boolean finishedGift(Long year_gift_id,Integer flag){
		 String sql="update mb_year_gift set flag=? where year_gift_id=? ";
		 int c=Sp.get().getDefaultJdbc().update(sql, new Object[]{flag,year_gift_id});
		 return c>0;
	}
	
	
	
	

}
