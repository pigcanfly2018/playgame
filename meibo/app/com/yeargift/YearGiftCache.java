package com.yeargift;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class YearGiftCache {
	
	private Map<String,YearGiftCustBean> custMap=new HashMap<String,YearGiftCustBean>();
	
	private YearGiftCache(){
		
	} 
	
	private static YearGiftCache cache =new YearGiftCache();
	public static YearGiftCache get(){
		if(cache==null)cache =new YearGiftCache();
		return cache;
	}

	public void reload(){
		List<YearGiftCustBean> list=YearGiftCustBean.getCustInfos();
		for(YearGiftCustBean bean :list){
			custMap.put(bean.login_name, bean);
		}
		YearStatBean bean =YearStatBean.getAllStat();
		YearGiftCustBean.all_count=bean.count;
		YearGiftCustBean.all_credit=bean.credit;
		
		YearStatBean bean0 =YearStatBean.getCurrentStat();
		YearGiftCustBean.current_day_count=bean0.count;
		YearGiftCustBean.current_day_credit=bean0.credit;
	}
	
	public YearGiftCustBean getCustBean(String login_name){
		return custMap.get(login_name);
	}
	
	public void setLastLootDate(String login_name){
		YearGiftCustBean bean =custMap.get(login_name);
		if(bean!=null){
			bean.last_loot_date=new Date(System.currentTimeMillis());
		}
		
	}
	
}
