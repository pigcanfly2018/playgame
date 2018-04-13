package com.yeargift;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import util.DateUtil;
import util.Sp;

public class YearStatBean {
	
	public  BigDecimal credit;
	
	public  Integer count;
	
	
	public static YearStatBean getAllStat(){
		String sql ="SELECT SUM(credit) credit,COUNT(1) cnt  FROM mb_year_gift";
		List<YearStatBean> list= Sp.get().getDefaultJdbc().query(sql, new YearStatBeanRowMapper());
		return list.get(0);
	}
	
	public static YearStatBean getCurrentStat(){
		String day = DateUtil.getDateString("yyyyMMdd");
		if(new Date().getHours()<20){
			day=day+"08";
		}else{
			day=day+"20";
		}
		String sql ="SELECT SUM(credit) credit,COUNT(1) cnt  FROM mb_year_gift where day=? ";
		List<YearStatBean> list= Sp.get().getDefaultJdbc().query(sql,new Object[]{day}, new YearStatBeanRowMapper());
		if(list.size()==0){
			YearStatBean bean  =new YearStatBean();
			bean.count=0;
			bean.credit=new BigDecimal(0);
			return bean;
		}
		return list.get(0);
	}

}
