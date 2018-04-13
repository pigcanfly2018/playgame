package com.product.bda.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import bsz.exch.bank.BankInfo;
import bsz.exch.core.MyPreparedStatementCreator;
import bsz.exch.utils.DateUtil;
import bsz.exch.utils.LogHelper;
import bsz.exch.utils.RandomUtil;

public class DepositService {

	
private static Logger logger=Logger.getLogger(DepositService.class);
	
	
	private JdbcTemplate template;
	
	private String datasource;
	
	public DepositService(JdbcTemplate template,String datasource){
		this.template=template;
		this.datasource=datasource;
	}
	
	public Long addDeposit(String order_no,Long cust_id,String login_name,String real_name,BigDecimal amount,String bank_name,
			String account_name,String deposit_type,String location,String remarks){
		
		
		List<Object> list0 =new ArrayList<Object>();

		String sql="insert into  mb_deposit (dep_no,cust_id,login_name,real_name,"
		  		+ "amount,poundage,pdage_status,status,bank_name,account_name,"
		  		+ "deposit_type,location,deposit_date,remarks,create_date,"
		  		+ "create_user,ip,audit_date) "
		  		+ "values(?,?,?,?"
		  		+ ",?,?,?,?,?,?"
		  		+ ",?,?,now(),?,now()"
		  		+ ",?,?,now())";
			
			list0.add(order_no);
			list0.add(cust_id);
			list0.add(login_name);
			list0.add(real_name);
			
			list0.add(amount);
			list0.add(new BigDecimal(amount.divide(new BigDecimal(100)).intValue()>2888?2888:amount.divide(new BigDecimal(100)).intValue()));
			list0.add(3);
			list0.add(3);
			list0.add(BankInfo.getBank(bank_name).getBank_name());
			list0.add(account_name);
			
			list0.add(deposit_type);
			list0.add(location);
			
			list0.add(remarks);
			list0.add("system");
			list0.add("127.0.0.1");



		KeyHolder keyHolder = new GeneratedKeyHolder();
		logger.info(LogHelper.dbLogObj(datasource,sql, list0));
		int cnt = template.update(new MyPreparedStatementCreator(sql,list0.toArray(new Object[0])), keyHolder);
		
		
		if(cnt>0){
			Date now =new Date(System.currentTimeMillis());
	    	if(now.getTime()>DateUtil.stringToDate("2018-03-01 10:00:00", "yyyy-MM-dd HH:mm:ss").getTime() &&
		    		  DateUtil.stringToDate("2018-03-15 18:00:00", "yyyy-MM-dd HH:mm:ss").getTime()>now.getTime() && amount.intValue()>=200){
	    		

				 /*double []level1 = {1.88,2.88,3.88,4.88,5.88,6.88};
			    	double []level2 = {8.88,9.88,10.88,11.88,12.88,13.88,14.88,15.88};
			    	double []level3 = {16.0,18.0,19.0,20.0,25.0,28.0,30.0,36.0,38.0,40.0,46.0,48.0,50.0};
			    	double []level4 = {56.0,58.0,59.0,60.0,66.0,68.0,69.0,76.0,78.0,79.0,86.0,88.0,89.0,96.0,
			    			98.0,99.0};
			    	
			    	double []level5 = {100.0,105.0,106.0,108.0,109.0,115.0,116.0,118.0,119.0,125.0,126.0,128.0,
			    			129.0,135.0,136.0,138.0,139.0};
			    	
			    	double []level6 = {140.0,146.0,148.0,150.0,156.0,158.0,160.0,166.0,168.0,170.0,176.0,178.0,
			    			180.0,186.0,188.0,190.0,196.0,199.0};
			    	
			    	double []level7 = {200.0,205.0,206.0,208.0,209.0,215.0,216.0,218.0,219.0,225.0,226.0,228.0,
			    			229.0,235.0,236.0,238.0,239.0};
			    	
			    	double []level8 = {300.0,305.0,306.0,308.0,309.0,315.0,316.0,318.0,319.0,325.0,326.0,328.0,
			    			329.0,335.0,336.0,338.0,339.0};
			    	
			    	double []level9 = {400.0,405.0,406.0,408.0,409.0,415.0,416.0,418.0,419.0,425.0,426.0,428.0,
			    			429.0,435.0,436.0,438.0,439.0};
			    	
			    	double []level10 = {500.0,505.0,506.0,508.0,509.0,515.0,516.0,518.0,519.0,525.0,526.0,528.0,
			    			529.0,535.0,536.0,538.0,539.0};
			    	
			    	double []level11 = {600.0,605.0,606.0,608.0,609.0,615.0,616.0,618.0,619.0,625.0,626.0,628.0,
			    			629.0,635.0,636.0,638.0,639.0};
			    	
			    	double []level12 = {700.0,705.0,506.0,508.0,509.0,515.0,516.0,518.0,519.0,525.0,526.0,528.0,
			    			529.0,535.0,536.0,538.0,539.0};
			    	
			    	double []level13 = {800.0,805.0,806.0,808.0,809.0,815.0,816.0,818.0,819.0,825.0,826.0,828.0,
			    			829.0,835.0,836.0,838.0,839.0,888.0};
			    	
			    	double []level14 = {918.0,928.0,958.0,968.0,988.0,999.0,1018.0,1118.0,1558.0,1668.0,1888.0,1988.0,
			    			2108.0,2118.0};*/
	    		//2018元宵拆宝箱
				String []level1 = {"5","6","7","8","9","10","11","12","13","14","15"};
				String []level2 = {"16","17","18","19","20","21","22","23","24","25"};
		    	String []level3 = {"30","35","36","38","40","45","46","48","50"};
		    	String []level4 = {"60","66","70","76","80","88","90","98","100"};
		    	
		    	String []level5 = {"110","120","130","140","150"};
		    	
		    	String []level6 = {"160","170","180","190","200"};
		    	
		    	String []level7 = {"210","220","230","240","250"};
		    	
		    	String []level8 = {"260","270","280","290","300","310","320","330","340","350"};
		    	
		    	String []level9 = {"360","380","400","420","450","460","480","500"};
		    	
		    	String []level10 = {"550","660","770","880","990","1000"};
		    	
		    	String []level11 = {"10元充值卡"};
		    	
		    	String []level12 = {"20元充值卡"};
		    	
		    	String []level13 = {"30元充值卡"};
		    	
		    	String []level14 = {"50元充值卡"};
		    	
		    	String []level15 = {"100元充值卡"};
		    	
		    	String []level16 = {"200元充值卡"};
		    	
		    	String []level17 = {"500元充值卡"};
		    	
		    	String []level18 = {"2500元以内品牌手机"};
		    	
		    	String []level19 = {"3500元以内品牌手机"};
		    	
		    	String []level20 = {"5000元以内品牌手机"};
		    	
		    	String []level21 = {"iPhone8 64GB手机"};
		    	
		    	
		    	
		    	
		    	HongBaoService Hongbao= new HongBaoService(template,datasource);
		    	
		    	if(amount.intValue()>=200 && amount.intValue() <1000){//level 1
	    			int n = new Random().nextInt(100);
	    			if(n < 90){
	    				int index=(int)(Math.random()*level1.length);
			    		String result = new String(level1[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    			}else if(n>=90 && n < 95){
	    				int index=(int)(Math.random()*level11.length);
	    				String result = new String(level11[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    			}else{
	    				int index=(int)(Math.random()*level12.length);
	    				String result = new String(level12[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    		
	    			}
		    	
		    	}else if(amount.intValue()>=1000 && amount.intValue() < 2000){//level 2
		    		int n = new Random().nextInt(100);
	    			if(n < 50){
	    				int index=(int)(Math.random()*level1.length);
			    		String result = new String(level1[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    			}else if(n>=50 && n < 80){
	    				int index=(int)(Math.random()*level2.length);
	    				String result = new String(level2[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    			}else if(n>=80 && n < 90){
	    				int index=(int)(Math.random()*level11.length);
	    				String result = new String(level11[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    		
	    			}else{
	    				int index=(int)(Math.random()*level12.length);
	    				String result = new String(level12[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    		
	    			}
		    	}else if(amount.intValue()>=2000 && amount.intValue() < 5000){//level 3
		    		int n = new Random().nextInt(100);
	    			if(n < 50){
	    				int index=(int)(Math.random()*level2.length);
			    		String result = new String(level2[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    			}else if(n>=50 && n < 70){
	    				int index=(int)(Math.random()*level3.length);
	    				String result = new String(level3[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    			}else if(n>=70 && n < 75){
	    				int index=(int)(Math.random()*level4.length);
	    				String result = new String(level4[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    		
	    			}else if(n>=75 && n < 85){
	    				int index=(int)(Math.random()*level13.length);
	    				String result = new String(level13[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    		
	    			}else if(n>=85 && n < 95){
	    				int index=(int)(Math.random()*level14.length);
	    				String result = new String(level14[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    			}else{
	    				int index=(int)(Math.random()*level15.length);
	    				String result = new String(level15[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    			}
		    	}else if(amount.intValue()>=5000 &&amount.intValue() < 10000){//level 4
		    		int n = new Random().nextInt(100);
	    			if(n < 60){
	    				int index=(int)(Math.random()*level4.length);
			    		String result = new String(level4[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    			}else if(n>=60 && n < 70){
	    				int index=(int)(Math.random()*level5.length);
	    				String result = new String(level5[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    			}else if(n>=70 && n < 80){
	    				int index=(int)(Math.random()*level6.length);
	    				String result = new String(level6[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    		
	    			}else if(n>=80 && n < 85){
	    				int index=(int)(Math.random()*level7.length);
	    				String result = new String(level7[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    		
	    			}else if(n>=85 && n < 95){
	    				int index=(int)(Math.random()*level15.length);
	    				String result = new String(level15[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    			}else{
	    				int index=(int)(Math.random()*level16.length);
	    				String result = new String(level16[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    			}
		    	}else if(amount.intValue()>=10000 && amount.intValue() < 50000){//level 5
		    		int n = new Random().nextInt(100);
	    			if(n < 40){
	    				int index=(int)(Math.random()*level6.length);
			    		String result = new String(level6[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    			}else if(n>=40 && n < 60){
	    				int index=(int)(Math.random()*level7.length);
	    				String result = new String(level7[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    			}else if(n>=60 && n < 80){
	    				int index=(int)(Math.random()*level8.length);
	    				String result = new String(level8[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    		
	    			}else if(n>=80 && n < 85){
	    				int index=(int)(Math.random()*level9.length);
	    				String result = new String(level9[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    		
	    			}else if(n>=85 && n < 95){
	    				int index=(int)(Math.random()*level16.length);
	    				String result = new String(level16[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    			}else{
	    				int index=(int)(Math.random()*level17.length);
	    				String result = new String(level17[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    			}
		    	}else if(amount.intValue()>=50000 && amount.intValue() < 100000){//level 6
		    		int n = new Random().nextInt(100);
	    			if(n < 20){
	    				int index=(int)(Math.random()*level8.length);
			    		String result = new String(level8[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    			}else if(n>=20 && n < 60){
	    				int index=(int)(Math.random()*level9.length);
	    				String result = new String(level9[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    			}else if(n>=60 && n < 80){
	    				int index=(int)(Math.random()*level10.length);
	    				String result = new String(level10[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    		
	    			}else if(n>=80 && n < 90){
	    				
	    				int index=(int)(Math.random()*level17.length);
	    				String result = new String(level17[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    			}else{
	    				int index=(int)(Math.random()*level18.length);
	    				String result = new String(level18[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    			}
		    	}else if(amount.intValue()>=100000){//level 7
		    		int n = new Random().nextInt(100);
	    			if(n < 40){
	    				int index=(int)(Math.random()*level9.length);
			    		String result = new String(level9[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    			}else if(n>=40 && n < 60){
	    				int index=(int)(Math.random()*level10.length);
	    				String result = new String(level10[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    			}else if(n>=60 && n < 70){
	    				int index=(int)(Math.random()*level17.length);
	    				String result = new String(level17[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    		
	    			}else if(n>=70 && n < 80){
	    				
	    				int index=(int)(Math.random()*level18.length);
	    				String result = new String(level18[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    			}else if(n>=80 && n < 90){
	    				
	    				int index=(int)(Math.random()*level19.length);
	    				String result = new String(level19[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    			}else if(n>=90 && n < 95){
	    				
	    				int index=(int)(Math.random()*level20.length);
	    				String result = new String(level20[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    			}else{
	    				int index=(int)(Math.random()*level21.length);
	    				String result = new String(level21[index]);
			    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
	    			}
			}
		   
	    	
			
	    	}
	    	
	    	
	    	
	    	//		    	if(amount.intValue()>=300 ){
//				 SignService signService= new SignService(template,datasource);
//				 signService.signToday(login_name, cust_id);
//			 }
	    	
	    	
	    	
	    	long deposit_id=keyHolder.getKey().longValue();
	    	createDepositLog(deposit_id,order_no,0,1,remarks);
	    	createDepositLog(deposit_id,order_no,1,3,remarks);
	    	return deposit_id;
	   }
		
//	    if(cnt>0){
//	    	long deposit_id=keyHolder.getKey().longValue();
//	    	createDepositLog(deposit_id,order_no,0,1,remarks);
//	    	createDepositLog(deposit_id,order_no,1,3,remarks);
//	    	return deposit_id;
//	   }
	    return null;
	}
		
		
		public Long addDepositWithPhone(String order_no,Long cust_id,String login_name,String real_name,BigDecimal amount,String bank_name,
				String account_name,String deposit_type,String location,String remarks,String depositdate){
			
			
			List<Object> list0 =new ArrayList<Object>();

			String sql="insert into  mb_deposit (dep_no,cust_id,login_name,real_name,"
			  		+ "amount,poundage,pdage_status,status,bank_name,account_name,"
			  		+ "deposit_type,location,deposit_date,remarks,create_date,"
			  		+ "create_user,ip,audit_date) "
			  		+ "values(?,?,?,?"
			  		+ ",?,?,?,?,?,?"
			  		+ ",?,?,?,?,now()"
			  		+ ",?,?,now())";
				
				list0.add(order_no);
				list0.add(cust_id);
				list0.add(login_name);
				list0.add(real_name);
				
				list0.add(amount);
				Date now =new Date(System.currentTimeMillis());
				if(now.getTime()>DateUtil.stringToDate("2017-12-01", "yyyy-MM-dd").getTime() &&
			    		  DateUtil.stringToDate("2018-01-01", "yyyy-MM-dd").getTime()>now.getTime() && amount.intValue()>=500){
					
					float f = 0.0f;
					
					if(amount.intValue() >= 500 && amount.intValue()<5000){
						
			        	f=amount.floatValue()*1/100;
					 }else if(amount.intValue() >= 5000 && amount.intValue()<10000){
						 
			        	
			        	 f=amount.floatValue()*1.8f/100;
					 }else if(amount.intValue() >= 10000 && amount.intValue()<30000){
						 
			        	 
			        	 f=amount.floatValue()*2.5f/100;
					 }else if(amount.intValue() >= 30000 && amount.intValue()<50000){
						
			        	
			        	 f=amount.floatValue()*3.8f/100;
					 }else if(amount.intValue() >= 50000 ){
						
			        	 
			        	 f=amount.floatValue()*5/100;
			        	 if(f>2888){
			        		 f=2888;
			        	 }
			        	 //list0.add(new BigDecimal(amount.divide(new BigDecimal(100)).multiply(new BigDecimal(5)).intValue()>2888?2888:amount.divide(new BigDecimal(100)).intValue()));
					 }
					
					list0.add(new BigDecimal(f));
					
				}else{
					list0.add(new BigDecimal(amount.divide(new BigDecimal(100)).intValue()>2888?2888:amount.divide(new BigDecimal(100)).intValue()));
				}
				
				list0.add(1);
				list0.add(1);
				list0.add(bank_name);
				list0.add(account_name);
				
				list0.add(deposit_type);
				list0.add(location);
				list0.add(depositdate);
				list0.add(remarks);
				list0.add("system");
				list0.add("127.0.0.1");



			KeyHolder keyHolder = new GeneratedKeyHolder();
			logger.info(LogHelper.dbLogObj(datasource,sql, list0));
			int cnt = template.update(new MyPreparedStatementCreator(sql,list0.toArray(new Object[0])), keyHolder);
		    if(cnt>0){
		    	long deposit_id=keyHolder.getKey().longValue();
		    	createDepositLog(deposit_id,order_no,0,1,remarks);
		    	//createDepositLog(deposit_id,order_no,1,3,remarks);
		    	return deposit_id;
		   }
		    return null;
		}
		
		//聚宝
		public Long addDeposit2(String order_no,Long cust_id,String login_name,String real_name,BigDecimal amount,String bank_name,
				String account_name,String deposit_type,String location,String remarks){
			
			
			List<Object> list0 =new ArrayList<Object>();

			String sql="insert into  mb_deposit (dep_no,cust_id,login_name,real_name,"
			  		+ "amount,poundage,pdage_status,status,bank_name,account_name,"
			  		+ "deposit_type,location,deposit_date,remarks,create_date,"
			  		+ "create_user,ip,audit_date) "
			  		+ "values(?,?,?,?"
			  		+ ",?,?,?,?,?,?"
			  		+ ",?,?,now(),?,now()"
			  		+ ",?,?,now())";
				
				list0.add(order_no);
				list0.add(cust_id);
				list0.add(login_name);
				list0.add(real_name);
				
				list0.add(amount);
				list0.add(new BigDecimal(0));
				list0.add(3);
				list0.add(3);
				list0.add(bank_name);
				list0.add(account_name);
				
				list0.add(deposit_type);
				list0.add(location);
				
				list0.add(remarks);
				list0.add("system");
				list0.add("127.0.0.1");



			KeyHolder keyHolder = new GeneratedKeyHolder();
			logger.info(LogHelper.dbLogObj(datasource,sql, list0));
			int cnt = template.update(new MyPreparedStatementCreator(sql,list0.toArray(new Object[0])), keyHolder);
			//2018年，抽奖代码开始
		    if(cnt>0){
		    	Date now =new Date(System.currentTimeMillis());
		    	if(now.getTime()>DateUtil.stringToDate("2018-03-01 10:00:00", "yyyy-MM-dd HH:mm:ss").getTime() &&
			    		  DateUtil.stringToDate("2018-03-15 18:00:00", "yyyy-MM-dd HH:mm:ss").getTime()>now.getTime() && amount.intValue()>=200){
		    		

					 /*double []level1 = {1.88,2.88,3.88,4.88,5.88,6.88};
				    	double []level2 = {8.88,9.88,10.88,11.88,12.88,13.88,14.88,15.88};
				    	double []level3 = {16.0,18.0,19.0,20.0,25.0,28.0,30.0,36.0,38.0,40.0,46.0,48.0,50.0};
				    	double []level4 = {56.0,58.0,59.0,60.0,66.0,68.0,69.0,76.0,78.0,79.0,86.0,88.0,89.0,96.0,
				    			98.0,99.0};
				    	
				    	double []level5 = {100.0,105.0,106.0,108.0,109.0,115.0,116.0,118.0,119.0,125.0,126.0,128.0,
				    			129.0,135.0,136.0,138.0,139.0};
				    	
				    	double []level6 = {140.0,146.0,148.0,150.0,156.0,158.0,160.0,166.0,168.0,170.0,176.0,178.0,
				    			180.0,186.0,188.0,190.0,196.0,199.0};
				    	
				    	double []level7 = {200.0,205.0,206.0,208.0,209.0,215.0,216.0,218.0,219.0,225.0,226.0,228.0,
				    			229.0,235.0,236.0,238.0,239.0};
				    	
				    	double []level8 = {300.0,305.0,306.0,308.0,309.0,315.0,316.0,318.0,319.0,325.0,326.0,328.0,
				    			329.0,335.0,336.0,338.0,339.0};
				    	
				    	double []level9 = {400.0,405.0,406.0,408.0,409.0,415.0,416.0,418.0,419.0,425.0,426.0,428.0,
				    			429.0,435.0,436.0,438.0,439.0};
				    	
				    	double []level10 = {500.0,505.0,506.0,508.0,509.0,515.0,516.0,518.0,519.0,525.0,526.0,528.0,
				    			529.0,535.0,536.0,538.0,539.0};
				    	
				    	double []level11 = {600.0,605.0,606.0,608.0,609.0,615.0,616.0,618.0,619.0,625.0,626.0,628.0,
				    			629.0,635.0,636.0,638.0,639.0};
				    	
				    	double []level12 = {700.0,705.0,506.0,508.0,509.0,515.0,516.0,518.0,519.0,525.0,526.0,528.0,
				    			529.0,535.0,536.0,538.0,539.0};
				    	
				    	double []level13 = {800.0,805.0,806.0,808.0,809.0,815.0,816.0,818.0,819.0,825.0,826.0,828.0,
				    			829.0,835.0,836.0,838.0,839.0,888.0};
				    	
				    	double []level14 = {918.0,928.0,958.0,968.0,988.0,999.0,1018.0,1118.0,1558.0,1668.0,1888.0,1988.0,
				    			2108.0,2118.0};*/
					//2018元宵拆宝箱
					String []level1 = {"5","6","7","8","9","10","11","12","13","14","15"};
					String []level2 = {"16","17","18","19","20","21","22","23","24","25"};
			    	String []level3 = {"30","35","36","38","40","45","46","48","50"};
			    	String []level4 = {"60","66","70","76","80","88","90","98","100"};
			    	
			    	String []level5 = {"110","120","130","140","150"};
			    	
			    	String []level6 = {"160","170","180","190","200"};
			    	
			    	String []level7 = {"210","220","230","240","250"};
			    	
			    	String []level8 = {"260","270","280","290","300","310","320","330","340","350"};
			    	
			    	String []level9 = {"360","380","400","420","450","460","480","500"};
			    	
			    	String []level10 = {"550","660","770","880","990","1000"};
			    	
			    	String []level11 = {"10元充值卡"};
			    	
			    	String []level12 = {"20元充值卡"};
			    	
			    	String []level13 = {"30元充值卡"};
			    	
			    	String []level14 = {"50元充值卡"};
			    	
			    	String []level15 = {"100元充值卡"};
			    	
			    	String []level16 = {"200元充值卡"};
			    	
			    	String []level17 = {"500元充值卡"};
			    	
			    	String []level18 = {"2500元以内品牌手机"};
			    	
			    	String []level19 = {"3500元以内品牌手机"};
			    	
			    	String []level20 = {"5000元以内品牌手机"};
			    	
			    	String []level21 = {"iPhone8 64GB手机"};
			    	
			    	
			    	
			    	
			    	HongBaoService Hongbao= new HongBaoService(template,datasource);
			    	
			    	if(amount.intValue()>=200 && amount.intValue() <1000){//level 1
		    			int n = new Random().nextInt(100);
		    			if(n < 90){
		    				int index=(int)(Math.random()*level1.length);
				    		String result = new String(level1[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    			}else if(n>=90 && n < 95){
		    				int index=(int)(Math.random()*level11.length);
		    				String result = new String(level11[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    			}else{
		    				int index=(int)(Math.random()*level12.length);
		    				String result = new String(level12[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    		
		    			}
			    	
			    	}else if(amount.intValue()>=1000 && amount.intValue() < 2000){//level 2
			    		int n = new Random().nextInt(100);
		    			if(n < 50){
		    				int index=(int)(Math.random()*level1.length);
				    		String result = new String(level1[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    			}else if(n>=50 && n < 80){
		    				int index=(int)(Math.random()*level2.length);
		    				String result = new String(level2[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    			}else if(n>=80 && n < 90){
		    				int index=(int)(Math.random()*level11.length);
		    				String result = new String(level11[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    		
		    			}else{
		    				int index=(int)(Math.random()*level12.length);
		    				String result = new String(level12[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    		
		    			}
			    	}else if(amount.intValue()>=2000 && amount.intValue() < 5000){//level 3
			    		int n = new Random().nextInt(100);
		    			if(n < 50){
		    				int index=(int)(Math.random()*level2.length);
				    		String result = new String(level2[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    			}else if(n>=50 && n < 70){
		    				int index=(int)(Math.random()*level3.length);
		    				String result = new String(level3[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    			}else if(n>=70 && n < 75){
		    				int index=(int)(Math.random()*level4.length);
		    				String result = new String(level4[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    		
		    			}else if(n>=75 && n < 85){
		    				int index=(int)(Math.random()*level13.length);
		    				String result = new String(level13[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    		
		    			}else if(n>=85 && n < 95){
		    				int index=(int)(Math.random()*level14.length);
		    				String result = new String(level14[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    			}else{
		    				int index=(int)(Math.random()*level15.length);
		    				String result = new String(level15[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    			}
			    	}else if(amount.intValue()>=5000 &&amount.intValue() < 10000){//level 4
			    		int n = new Random().nextInt(100);
		    			if(n < 60){
		    				int index=(int)(Math.random()*level4.length);
				    		String result = new String(level4[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    			}else if(n>=60 && n < 70){
		    				int index=(int)(Math.random()*level5.length);
		    				String result = new String(level5[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    			}else if(n>=70 && n < 80){
		    				int index=(int)(Math.random()*level6.length);
		    				String result = new String(level6[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    		
		    			}else if(n>=80 && n < 85){
		    				int index=(int)(Math.random()*level7.length);
		    				String result = new String(level7[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    		
		    			}else if(n>=85 && n < 95){
		    				int index=(int)(Math.random()*level15.length);
		    				String result = new String(level15[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    			}else{
		    				int index=(int)(Math.random()*level16.length);
		    				String result = new String(level16[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    			}
			    	}else if(amount.intValue()>=10000 && amount.intValue() < 50000){//level 5
			    		int n = new Random().nextInt(100);
		    			if(n < 40){
		    				int index=(int)(Math.random()*level6.length);
				    		String result = new String(level6[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    			}else if(n>=40 && n < 60){
		    				int index=(int)(Math.random()*level7.length);
		    				String result = new String(level7[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    			}else if(n>=60 && n < 80){
		    				int index=(int)(Math.random()*level8.length);
		    				String result = new String(level8[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    		
		    			}else if(n>=80 && n < 85){
		    				int index=(int)(Math.random()*level9.length);
		    				String result = new String(level9[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    		
		    			}else if(n>=85 && n < 95){
		    				int index=(int)(Math.random()*level16.length);
		    				String result = new String(level16[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    			}else{
		    				int index=(int)(Math.random()*level17.length);
		    				String result = new String(level17[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    			}
			    	}else if(amount.intValue()>=50000 && amount.intValue() < 100000){//level 6
			    		int n = new Random().nextInt(100);
		    			if(n < 20){
		    				int index=(int)(Math.random()*level8.length);
				    		String result = new String(level8[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    			}else if(n>=20 && n < 60){
		    				int index=(int)(Math.random()*level9.length);
		    				String result = new String(level9[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    			}else if(n>=60 && n < 80){
		    				int index=(int)(Math.random()*level10.length);
		    				String result = new String(level10[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    		
		    			}else if(n>=80 && n < 90){
		    				
		    				int index=(int)(Math.random()*level17.length);
		    				String result = new String(level17[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    			}else{
		    				int index=(int)(Math.random()*level18.length);
		    				String result = new String(level18[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    			}
			    	}else if(amount.intValue()>=100000){//level 7
			    		int n = new Random().nextInt(100);
		    			if(n < 40){
		    				int index=(int)(Math.random()*level9.length);
				    		String result = new String(level9[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    			}else if(n>=40 && n < 60){
		    				int index=(int)(Math.random()*level10.length);
		    				String result = new String(level10[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    			}else if(n>=60 && n < 70){
		    				int index=(int)(Math.random()*level17.length);
		    				String result = new String(level17[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    		
		    			}else if(n>=70 && n < 80){
		    				
		    				int index=(int)(Math.random()*level18.length);
		    				String result = new String(level18[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    			}else if(n>=80 && n < 90){
		    				
		    				int index=(int)(Math.random()*level19.length);
		    				String result = new String(level19[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    			}else if(n>=90 && n < 95){
		    				
		    				int index=(int)(Math.random()*level20.length);
		    				String result = new String(level20[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    			}else{
		    				int index=(int)(Math.random()*level21.length);
		    				String result = new String(level21[index]);
				    		Hongbao.createHongbao(cust_id, login_name, new BigDecimal(0),result, order_no+"("+amount.toString()+")");
		    			}
		   
	    	
			}	
		    	
				
		    	}
		    	
		    	
		    	
		    	
		    	//	if(amount.intValue()>=300 ){
				//	 SignService signService= new SignService(template,datasource);
				//	 signService.signToday(login_name, cust_id);
				// }
		    	
		    	
		    	
		    	long deposit_id=keyHolder.getKey().longValue();
		    	createDepositLog(deposit_id,order_no,0,1,remarks);
		    	createDepositLog(deposit_id,order_no,1,3,remarks);
		    	return deposit_id;
		   }
		    //抽奖代码结束
		    return null;
		}
		
		public boolean addGift(String gift_no,String login_name,BigDecimal deposit_credit,BigDecimal payout, Date kh_date,String real_name,int cust_level){
			String sql="insert into  mb_cash_gift (gift_no,kh_date,login_name,real_name,cust_level,"
					+ "gift_type,gift_code,deposit_credit,net_credit,valid_credit,"
					+ "rate,payout,cs_date,remarks,create_date,"
					+ "create_user,status,audit_date,audit_user,audit_msg,cust_id) "
					+ "values(?,?,?,?,?,"
					+ "?,?,?,?,?,"
					+ "?,?,now(),?,now(),"
					+ "?,?,now(),?,?,?)";
			List<Object> list0 =new ArrayList<Object>();
			list0.add(gift_no);
			list0.add(kh_date);
			list0.add(login_name);
			list0.add(real_name);
			list0.add(cust_level);
			
			list0.add("转账红利");
			list0.add(RandomUtil.getRandom(8));
			list0.add(deposit_credit);
			list0.add(0);
			list0.add(0);
			
			list0.add(1);
			list0.add(payout);
			list0.add("系统自动添加转账福利");
			
			list0.add("system");
			list0.add(3);
			list0.add("system");
			list0.add("系统自动审核");
			list0.add(cust_level);
			
			
			logger.info(LogHelper.dbLogObj(datasource,sql, list0));
			int count=template.update(sql,list0.toArray(new Object[0]));
			return count>0;
		}
		
	
	public void createDepositLog(Long deposit_id,String dep_no,Integer pre_status,Integer after_status,String remarks){
		List<Object> list0 =new ArrayList<Object>();
		String sql="insert into  mb_deposit_log (pre_status,after_status,deposit_id,remarks,create_date,create_user,dep_no) values(?,?,?,?,now(),?,?)";
		list0.add(pre_status);
		list0.add(after_status);
		list0.add(deposit_id);
		list0.add(remarks);
		
		
		list0.add("system");
		list0.add("dep_no");
		
		logger.info(LogHelper.dbLogObj(datasource,sql, list0));
		template.update(sql,list0.toArray(new Object[0]));
		
	}
	
	public int NTgetCount(Long cust_id){
		String sql ="select count(1) from mb_deposit where cust_id=? and status=3";
		 Object[] objs=new Object[]{cust_id};
		 logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
		int count=template.queryForObject(sql, objs, Integer.class);
		return count;
	}
	
	public  int NTgetCountToday(Long cust_id){
		  String sql="SELECT COUNT(1) FROM mb_deposit WHERE create_date>CURDATE() AND STATUS=3 AND cust_id=?";
		  Object[] objs=new Object[]{cust_id};
			 logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
			int count=template.queryForObject(sql, objs, Integer.class);
			return count;
	  }
	
	
	//login_name|login_ip|amount|bank_name|account_name|deposit_type|deposit_date|location|remarks
	public Long createDeposit(String order_no,String login_name,BigDecimal amount,String bank_name,
			String account_name,String deposit_type,String deposit_date,String location,String remarks,
			String create_user){
//		int count=querySuccessWithdrawCount(login_name);
//		String sql="";
//		List<Object> list0 =new ArrayList<Object>();
//		if(count>0){
//			 sql="insert into  mb_withdraw (wit_no,login_name,real_name,amount,bank_name,"
//					+ "account_name,account,account_type,location,remarks,create_date,"
//					+ "create_user,status,cust_id,wit_cnt) "
//					+ "select ?,login_name,real_name,?,bank_name,"
//					+ "real_name,account,account_type,bank_dot,?,now(),"
//					+ "?,1,cust_id,? from mb_customer where login_name =?";
//			
//			list0.add(order_no);
//			list0.add(amount);
//			list0.add(remarks);
//			list0.add(create_user);
//			list0.add(count);
//			list0.add(login_name);
//		}else{
//			   sql="insert into  mb_withdraw (wit_no,login_name,real_name,amount,bank_name,"
//						+ "account_name,account,account_type,location,remarks,create_date,"
//						+ "create_user,status,cust_id,wit_cnt) "
//						+ "select ?,login_name,real_name,?,?,"
//						+ "real_name,?,?,?,?,now(),"
//						+ "login_name,1,cust_id,? from mb_customer where login_name =?";
//				list0.add(order_no);
//				list0.add(amount);
//				list0.add(bank_name);
//				list0.add(account);
//				list0.add(account_type);
//				list0.add(bank_dot);
//				list0.add(remarks);
//				list0.add(create_user);
//				list0.add(count);
//				list0.add(login_name);
//		}
//		KeyHolder keyHolder = new GeneratedKeyHolder();
//		logger.info(LogHelper.dbLogObj(datasource,sql, list0));
//	    int cnt=template.update(new MyPreparedStatementCreator(sql,list0.toArray(new Object[0])), keyHolder);
//	    if(cnt>0){
//	    	long withdraw_id=keyHolder.getKey().longValue();
//	    	createWithdrawLog(0,1,withdraw_id,remarks,create_user);
//	    	return withdraw_id;
//	   }
	    return null;
	}
	
	/**
	 * 查询未处理存款数量
	 */
	public int queryUndoDepositCount(String login_name){
		String sql="select count(1) from mb_deposit where login_name =? and status =1";
		List<String> list =new ArrayList<String>();
		list.add(login_name);
		logger.info(LogHelper.dbLog(datasource,sql, list));
		Integer count=template.queryForObject(sql,list.toArray(new String[0]),Integer.class);
		return count;
	}
	
}
