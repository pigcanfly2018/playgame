package com.product.bda.service;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

public class ScoreService {
	
	        private static Logger logger=Logger.getLogger(ScoreService.class);
		
			private JdbcTemplate template;
			private String datasource;
			public ScoreService(JdbcTemplate template,String datasource){
				this.template=template;
				this.datasource=datasource;
			}
			
			private  boolean modScore(String login_name,BigDecimal score){
		    	if(score.intValue()>0){
		    		String sql="update mb_customer set score=score+? where login_name=?";
		        	int count=template.update(sql, new Object[]{score,login_name});
		    		return count>0;
		    	}else{
		    		BigDecimal t=new BigDecimal(0).subtract(score);
		    		String sql="update mb_customer set score=score+? where login_name=? and score>=?";
		        	int count=template.update(sql, new Object[]{score,login_name,t});
		    		return count>0;
		    	}
		    }
			    
			 
			
			 public  boolean modScore(String rec_code,String rec_type,BigDecimal score,String login_name,Long cust_id,String user){
				 boolean b=modScore(login_name, score);
				 if(b){
					 CustomerService s=new  CustomerService(template,datasource);
					  Customer cust= s.getCustomer(login_name);
					 
					 createScoreLog(rec_code, rec_type, score, login_name, cust_id, user,cust.score);
				 }
		         return b;
			 }
			 
			 
			private  boolean createScoreLog(String rec_code,String rec_type,BigDecimal score,String login_name,Long cust_id,String user,BigDecimal cur_score){
			  	  final String sql="insert into  mb_score_rec (rec_code,cust_id,login_name,rec_type,score,create_date,create_user,cur_score) values(?,?,?,?,?,now(),?,?)";
			  	  final Object[] objects=new Object[]{rec_code,cust_id,login_name,rec_type,score,user,cur_score};
				  int result=template.update(sql,objects);
				  return result>0;
			    }  
			
			public int depositCountToday(String login_name){
				
				  String sql="SELECT COUNT(1) FROM mb_deposit WHERE create_date>CURDATE() AND STATUS=3 AND login_name=?";
				  int dcount=template.queryForObject(sql,new Object[]{login_name},Integer.class);
				  return  dcount;
				  
			}


}
