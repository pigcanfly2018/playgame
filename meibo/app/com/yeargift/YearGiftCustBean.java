package com.yeargift;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import util.DateUtil;
import util.Sp;

public class YearGiftCustBean {
	
	public Long cust_id;
	
	public String login_name;
	
	public Integer cust_level;
	
	public Date last_loot_date;
	
	//红包总数
	public Integer cnt;
	//红包金额
	public BigDecimal sum_credit;
	
	//当期总数
	public Integer current_cnt;
	//红包金额
	public BigDecimal current_credit;
	
	//总已派发总额度
	public static BigDecimal all_credit =new BigDecimal(0);
	
	//总已派发红包数量
	public static Integer all_count =0;
	
	//本期已派发总额度
	public static BigDecimal current_day_credit=new BigDecimal(0);
	
	//总已派发红包数量
	public static Integer current_day_count =0;
	
	
	public static Integer ramdom1=0;
	public static Integer ramdom2=0;
	
	
	public static List<YearGiftCustBean> getCustInfos(){
		String day = DateUtil.getDateString("yyyyMMdd");
		if(new Date().getHours()<20){
			day=day+"08";
		}else{
			day=day+"20";
		}
		String sql ="SELECT a.cust_id cust_id,login_name,cust_level,sum_credit,cnt,current_credit,current_cnt FROM mb_customer a LEFT JOIN (SELECT cust_id ,SUM(credit) sum_credit,COUNT(1) cnt  FROM mb_year_gift GROUP BY cust_id) b ON a.cust_id =b.cust_id LEFT JOIN (SELECT cust_id ,SUM(credit) current_credit,COUNT(1) current_cnt  FROM mb_year_gift WHERE mb_year_gift.day= ? GROUP BY cust_id) c ON a.cust_id =c.cust_id WHERE a.cust_level>0";
		return Sp.get().getDefaultJdbc().query(sql,new Object[]{day}, new YearGiftCustBeanRowMapper());
	} 
	
	public static YearGiftCustBean getCustInfo(Long cust_id){
		String day = DateUtil.getDateString("yyyyMMdd");
		if(new Date().getHours()<20){
			day=day+"08";
		}else{
			day=day+"20";
		}
		String sql ="SELECT a.cust_id cust_id,login_name,cust_level,sum_credit,cnt,current_credit,current_cnt FROM mb_customer a LEFT JOIN (SELECT cust_id ,SUM(credit) sum_credit,COUNT(1) cnt  FROM mb_year_gift GROUP BY cust_id) b ON a.cust_id =b.cust_id LEFT JOIN (SELECT cust_id ,SUM(credit) current_credit,COUNT(1) current_cnt  FROM mb_year_gift WHERE mb_year_gift.day= ? GROUP BY cust_id) c ON a.cust_id =c.cust_id WHERE a.cust_level>0 and a.cust_id=? ";
		List<YearGiftCustBean> list= Sp.get().getDefaultJdbc().query(sql, new Object[]{day,cust_id},new YearGiftCustBeanRowMapper());
		if(list.size()==0){
			return null;
		}
		return list.get(0);
	} 
	

}
