package models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import util.IPSeeker;
import util.Sp;

public class Customer implements Serializable{
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
    public Integer login_times;
    public String sb_game;
    public String sb_pwd;
    public Boolean sb_flag;
    public Boolean sb_actived;
    public String ag_game;
    public String ag_pwd;
    public Boolean ag_flag;
    public Boolean ag_actived;
    public String bbin_game;
    public String bbin_pwd;
    public Boolean bbin_flag;
    public Boolean bbin_actived;
    
    public String bbinmobile_game;
    public String bbinmobile_pwd;
    public Boolean bbinmobile_flag;
    
    public BigDecimal credit;
    public Boolean flag;
    public Integer cust_level;
    public Boolean is_agent;
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
    public Boolean s_email;
    public BigDecimal score;
    
    public Boolean promo_flag;
    
    public String pt_game;
    public String pt_pwd;
    public Boolean pt_flag;
    public Boolean pt_actived;
    
    public Boolean online_pay;
    public Boolean bank_pay;
    public Boolean is_block;
    public Boolean alipay_flag;
	
    public String kg_game;
    public String kg_pwd;
    public Boolean kg_flag;
    public Boolean kg_actived;
    
    public String mg_game;
    public String mg_alias;
    public String mg_pwd;
    public Boolean mg_flag;
    public Boolean mg_actived;
    
    public String pp_game;
	public String pp_pwd;
	public Boolean pp_flag;
	public Boolean pp_actived;
	
    public String alipay_account;
    public String alipay_name;
    public Boolean auto_transfer_flag;
    public Boolean need_gift_flag;
    
    public String reg_domain;
    public String refer_url;
    public String search_key;
    
    public String accountkey;
   
    public static boolean createCustomer(Customer cust){
		  String sql="insert into  mb_customer (login_name,login_pwd,real_name,phone,email,"
		  		+ "qq,reg_ip,create_date,create_user,login_ip,"
		  		+ "login_date,login_times,sb_game,sb_pwd,sb_flag,"
		  		+ "sb_actived,ag_game,ag_pwd,ag_flag,ag_actived,"
		  		+ "bbin_game,bbin_pwd,bbin_flag,bbin_actived,credit,"
		  		+ "flag,cust_level,is_agent,parent_id,reg_source,"
		  		+ "agent_dm,first_deposit_date,remarks,bank_name,account_type,"
		  		+ "bank_dot,account,agent_mode,s_email,pt_game,pt_pwd,pt_flag,pt_actived,kg_game,kg_pwd,kg_flag,kg_actived,mg_game,mg_pwd,mg_flag,"
		  		+ "mg_actived,bbinmobile_game,bbinmobile_pwd,bbinmobile_flag,reg_domain,refer_url,search_key,pp_game,pp_pwd,pp_flag,pp_actived) "
		  		+ "values(?,?,?,?,?,"
		  		+ "?,?,now(),?,?,"
		  		+ "?,?,?,?,?,"
		  		+ "?,?,?,?,?,"
		  		+ "?,?,?,?,?,"
		  		+ "?,?,?,?,?,"
		  		+ "?,?,?,?,?,"
		  		+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{
				  cust.login_name, cust.login_pwd,cust.real_name,cust.phone,cust.email,
				  cust.qq,cust.reg_ip,cust.create_user,cust.login_ip,
				  cust.login_date,cust.login_times,cust.sb_game,cust.sb_pwd,cust.sb_flag,
				  cust.sb_actived,cust.ag_game,cust.ag_pwd,cust.ag_flag,cust.ag_actived,
				  cust.bbin_game,cust.bbin_pwd,cust.bbin_flag,cust.bbin_actived,cust.credit,
				  cust.flag,cust.cust_level,cust.is_agent,cust.parent_id,cust.reg_source,
				  cust.agent_dm,cust.first_deposit_date,cust.remarks,cust.bank_name,cust.account_type,
				  cust.bank_dot,cust.account,cust.agent_mode,cust.s_email,cust.pt_game,cust.pt_pwd,cust.pt_flag,cust.pt_actived,
				  cust.kg_game,cust.kg_pwd,cust.kg_flag,cust.kg_actived,cust.mg_game,cust.mg_pwd,cust.mg_flag,cust.mg_actived,
				  cust.bbinmobile_game,cust.bbinmobile_pwd,cust.bbinmobile_flag,cust.reg_domain,cust.refer_url,cust.search_key,cust.pp_game,cust.pp_pwd,cust.pp_flag,cust.pp_actived});
		  return count>0;
	 }
    
    public static Customer getCustomer(String login_name){
		  String sql="select * from mb_customer where login_name =?";
		  List<Customer> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{login_name},new CustomerRowMap());
		  if(list.size()>0){
			  return list.get(0);
		  }
		  return null;
	 }
    
    public static Customer getCustomerByAccountkey(String accountkey){
		  String sql="select * from mb_customer where accountkey =?";
		  List<Customer> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{accountkey},new CustomerRowMap());
		  if(list.size()>0){
			  return list.get(0);
		  }
		  return null;
	 }
    
    
    public static Customer getCustomerByPhone(String phone){
		  String sql="select * from mb_customer where phone =?";
		  List<Customer> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{phone},new CustomerRowMap());
		  if(list.size()>0){
			  return list.get(0);
		  }
		  return null;
	 }
   
    public static boolean logLogin(String ip,Long cust_id){
    	String sql="update mb_customer set login_ip=?,login_date=now(),login_times=login_times+1 where cust_id=? ";
	   int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{ip,cust_id});
	  return count>0;
    	
    }
    
    public static boolean NTmodfyPwd(String login_name,String pwd){
		 String sql="update mb_customer set login_pwd=? where login_name=?";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{pwd,login_name});
		 return count>0;
	 }
    
    public static boolean NTmodfyAccountkey(String login_name,String accountkey){
		 String sql="update mb_customer set accountkey=? where login_name=?";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{accountkey,login_name});
		 return count>0;
	 }
    
    
    public static boolean NTmodfyBank(String login_name,String bank_name,String account,String account_type,String bank_dot){
		 String sql="update mb_customer set bank_name=?,account=?,account_type=?,bank_dot=? where login_name=?";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{bank_name,account,account_type,bank_dot,login_name});
		 return count>0;
	 }
    
    public static boolean NTmoddyGame(boolean flag,String plat,Long cust_id){
    	String sql="";
    	if("AGIN".equals(plat)){
    	    sql="update mb_customer set ag_flag=? where cust_id=?";
    	}
    	if("BBIN".equals(plat)){
     	    sql="update mb_customer set bbin_flag=? where cust_id=?";
     	}
    	if("PT".equals(plat)){
      	    sql="update mb_customer set pt_flag=? where cust_id=?";
      	}
    	if("KG".equals(plat)){
      	    sql="update mb_customer set kg_flag=? where cust_id=?";
      	}
    	if("MG".equals(plat)){
      	    sql="update mb_customer set mg_flag=? where cust_id=?";
      	}
    	
    	 int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{flag,cust_id});
		 return count>0;
    }
    
    /**
     * 修改额度 
     * @param cust_id
     * @param credit
     * @return
     */
    public static boolean modCredit(Long cust_id,BigDecimal credit){
    	if(credit.intValue()>0){
    		String sql="update mb_customer set credit=credit+? where cust_id=?";
        	int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{credit,cust_id});
    		return count>0;
    	}else{
    		BigDecimal t=new BigDecimal(0).subtract(credit);
    		String sql="update mb_customer set credit=credit+? where cust_id=? and credit>=?";
        	int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{credit,cust_id,t});
    		return count>0;
    	}
    }
   
    public static boolean NTmodCustlevelFirst(Long customerId,Integer cust_level){
		 String sql="update mb_customer set cust_level=?,first_deposit_date=now() where cust_id=? and cust_level = 0";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{cust_level,customerId});
		  return count>0;
		 
	 }
    
    /*
     * 修改分值
     */
    public static boolean modScore(Long cust_id,BigDecimal score){
    	if(score.intValue()>0){
    		String sql="update mb_customer set score=score+? where cust_id=?";
        	int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{score,cust_id});
    		return count>0;
    	}else{
    		BigDecimal t=new BigDecimal(0).subtract(score);
    		String sql="update mb_customer set score=score+? where cust_id=? and score>=?";
        	int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{score,cust_id,t});
    		return count>0;
    	}
    }

    public static boolean updatePTGamePwd(Long cust_id,String pwd){
    	String sql="update mb_customer set pt_pwd=? where cust_id=?";
    	int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{pwd,cust_id});
		return count>0;
    }
    
    public static boolean updateMGGamePwd(Long cust_id,String pwd){
    	String sql="update mb_customer set mg_pwd=? where cust_id=?";
    	int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{pwd,cust_id});
		return count>0;
    }
    
    public static boolean updateBBINMobileGamePwd(Long cust_id,String pwd){
    	String sql="update mb_customer set bbinmobile_pwd=? where cust_id=?";
    	int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{pwd,cust_id});
		return count>0;
    }
    
    public static int getRegCntToday(String ip){
    	String sql="SELECT COUNT(1) FROM mb_customer WHERE reg_ip=? AND create_date >CURDATE()";
    	int count=Sp.get().getDefaultJdbc().queryForObject(sql, new Object[]{ip},Integer.class);
		return count;
    }
    
    
    public static boolean updateAlipayAccount(String alipay_account,String alipay_name,String login_name){
    	String sql="update mb_customer set  alipay_account = ?, alipay_name=?  WHERE login_name = ? ";
    	int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{alipay_account,alipay_name,login_name});
		return count>0;
    }
    
    public static void main(String []args){
    	  String sql="select * from mb_customer where cust_level>0 order by cust_level desc";
		  List<Customer> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{},new CustomerRowMap());
		  IPSeeker seeker = IPSeeker.getInstance();
		  for(Customer c:list){
		     String address=seeker.getAddress(c.reg_ip==null?"":c.reg_ip);
		     if(address.contains("贵州")){
		    	   System.out.println(c.login_name+","+c.real_name+",等级"+c.cust_level+","+c.reg_ip+","+address); 
		     }
		    
		  }
		  System.out.println("----------------------------------------------"); 
		  for(Customer c:list){
		
		     String address0=seeker.getAddress(c.login_ip==null?"":c.login_ip);
		     if(address0.contains("贵州")){
		    	   System.out.println(c.login_name+","+c.real_name+",等级"+c.cust_level+","+c.reg_ip+","+address0); 
		     }
		  }
    }
    
}
