package com.product.bojin.beans;
import java.lang.Long;
import java.lang.String;
import java.util.Date;

import com.product.bojin.service.CustomerService;

public class Customer extends Thread {

  public Long cust_id;
  public String login_name;
  public String login_pwd;
  public String real_name;
  public String phone;
  public String email;
  public String qq;
  public String reg_ip;
  public Date create_date;
  public String create_user;
  public String login_ip;
  public Date login_date;
  public String login_times;
  public String sb_game;
  public String sb_pwd;
  public String sb_flag;
  public String sb_actived;
  public String ag_game;
  public String ag_pwd;
  public String ag_flag;
  public String ag_actived;
  public String bbin_game;
  public String bbin_pwd;
  public String bbin_flag;
  public String bbin_actived;
  public String credit;
  public String flag;
  public String cust_level;
  public String is_agent;
  public Long parent_id;
  public String reg_source;
  public String agent_dm;
  public Date first_deposit_date;
  public String remarks;
  public String bank_name;
  public String account_type;
  public String bank_dot;
  public String account;
  public String agent_mode;
  public String s_email;
  public String score;
  public String pt_game;
  public String pt_pwd;
  public String pt_flag;
  public String pt_actived;
  public String online_pay;
  public String is_block;
  public String alipay_flag;
  public String kg_game;
  public String kg_pwd;
  public String kg_flag;
  public String kg_actived;
  public String mg_game;
  public String mg_pwd;
  public String mg_flag;
  public String mg_actived;
  
  
 
  public void setLoginName(String loginName){
	  
	  this.login_name=loginName;
  }

  
	public static void main(String [] rags){
		Customer ss =new Customer();
		ss.setLoginName("tony");
	
		
	}


  
}
