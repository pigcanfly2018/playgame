package models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.MyTree;
import util.Sp;
import util.TreeNode;

public class Agent implements MyTree<Agent>,Serializable{

	public Long agent_id;
    public String login_name;
    public String login_pwd;
    public String real_name;
    public String phone;
    public String email;
    public String qq;
    public int flag;
    
    public String reg_ip;
    public Date create_date;
    public String create_user;
    public Date audit_date;
    public String audit_user;
    public String audit_msg;
    public Date lock_date;
    public String lock_user;
    public String lock_msg;
    public String last_login_ip;
    public String reg_code;
    public Date last_login_date;
    public BigDecimal credit;
    public Long parent_id;
    public String remarks;
    public String bank_name;
    public String account_type;
    public String bank_dot;
    public String account;
    public Integer share;
    
    //辅助字段
    public int subcount;
    public int active;
    
    
	public boolean isLeaf() {
		// TODO Auto-generated method stub
		return false;
	}
	public TreeNode convertNode() {
		// TODO Auto-generated method stub
		return null;
	}
	public List<Agent> getChilds(boolean includeBut) {
		// TODO Auto-generated method stub
		return null;
	}

	public Agent getParent() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	public  boolean NTcreat(){
		  String sql="insert into  mb_agent (login_name,login_pwd,"
		  		+ "real_name,phone,email,qq,reg_ip,create_date,create_user,last_login_ip,"
		  		+ "last_login_date,"
		  		+ "credit,flag,parent_id,"
		  		+ "remarks,bank_name,account_type,bank_dot,"
		  		+ "account,reg_code) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{login_name,
				  login_pwd,real_name,phone,email,qq,reg_ip,create_date,create_user,last_login_ip,
				  last_login_date,credit,flag,parent_id,
				  remarks,bank_name,account_type,bank_dot,account,reg_code});
		  return count>0;
	 }
	
	
	/**
	 * 是否存在
	 * 
	 * @param login_name
	 * @return
	 */
	public static boolean NTexist(String login_name) {
		String sql = "select count(1) from mb_agent where login_name =?";
		int count = Sp.get().getDefaultJdbc()
				.queryForObject(sql, new Object[] { login_name },Integer.class);
		return count > 0;
	}
	
	
	public static boolean NTexitsRealName(String real_name){
		   String sql="select count(1) from mb_agent where real_name=?";
		   int count=Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{real_name},Integer.class);
		   return count>0;
	}
	
	public static boolean NTexitsQQ(String qq){
		   String sql="select count(1) from mb_agent where qq=?";
		   int count=Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{qq},Integer.class);
		   return count>0;
	}
	
	public static boolean NTexitsPhone(String phone){
		   String sql="select count(1) from mb_agent where phone=?";
		   int count=Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{phone},Integer.class);
		   return count>0;
	}
	
	 public static Agent NTgetAgent(Long agent_id){
		  String sql="select * from mb_agent where agent_id =?";
		  List<Agent> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{agent_id},new AgentRowMap());
		  if(list.size()>0){
			  return list.get(0);
		  }
		  return null;
	 }
	 
	 public static Agent NTget(Long agent_id){
	      String sql ="select * from mb_agent where agent_id=?";
	      Agent agent=(Agent) Sp.get().getDefaultJdbc().queryForObject(sql,
					new Object[]{agent_id},new AgentRowMap());
	      return agent;
     }
	 
	 public static boolean modifyFlag(String userName,long agent_id,String remarks,int flag,Date audit_date){
		 String sql="update mb_agent set audit_user=?,audit_msg=?,flag=?,audit_date=? where agent_id=?";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{userName,remarks,flag,audit_date,agent_id});
		 return count>0;
	 }
	 
	 /**
	     * 修改分成
	     * @param agent_id
	     * @param share
	     * @return
	     */
	    public static boolean modShare(String login_name,Integer share){
	    	
	    		String sql="update mb_agent set share=? where login_name=?";
	        	int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{share,login_name});
	    		return count>0;
	    	
	    }
	    
	 public static boolean NTmodInfo2(String email,String qq,
			 String remarks,String bank_name,String account,String account_type,String bank_dot,
			 Long agent_id){
		  String sql="update mb_agent set ";
		  ArrayList<Object> list=new ArrayList<Object>();
		  if(email!=null){
			  sql=sql+" email=?,";
			  list.add(email);
		  }
		  if(qq!=null){
			  sql=sql+" qq=?,";
			  list.add(qq);
		  }
		  if(bank_name!=null){
			  sql=sql+" bank_name=?,";
			  list.add(bank_name);
		  }
		  if(account!=null){
			  sql=sql+" account=?,";
			  list.add(account);
		  }
		  if(account_type!=null){
			  sql=sql+" account_type=?,";
			  list.add(account_type);
		  }
		  if(bank_dot!=null){
			  sql=sql+" bank_dot=?,";
			  list.add(bank_dot);
		  }
		  
		  sql=sql+" remarks=? where agent_id =? ";
		  list.add(remarks);
		  list.add(agent_id);
		  int count=Sp.get().getDefaultJdbc().update(sql, list.toArray(new Object[0]));
		  return count>0;
	 }
	
	public String toString() {
		return "Agent [agent_id=" + agent_id + ", login_name=" + login_name
				+ ", login_pwd=" + login_pwd + ", real_name=" + real_name
				+ ", phone=" + phone + ", email=" + email + ", qq=" + qq
				+ ", flag=" + flag + ", reg_ip=" + reg_ip + ", create_date="
				+ create_date + ", create_user=" + create_user
				+ ", audit_date=" + audit_date + ", audit_user=" + audit_user
				+ ", audit_msg=" + audit_msg + ", lock_date=" + lock_date
				+ ", lock_user=" + lock_user + ", lock_msg=" + lock_msg
				+ ", last_login_ip=" + last_login_ip + ", reg_code=" + reg_code
				+ ", last_login_date=" + last_login_date + ", credit=" + credit
				+ ", parent_id=" + parent_id + ", remarks=" + remarks
				+ ", bank_name=" + bank_name + ", account_type=" + account_type
				+ ", bank_dot=" + bank_dot + ", account=" + account
				+ ", subcount=" + subcount + "]";
	}
	
	
	public static boolean locked(String userName,long agent_id,String remarks,int flag,Date audit_date){
		 String sql="update mb_agent set lock_user=?,lock_msg=?,flag=?,lock_date=? where agent_id=?";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{userName,remarks,flag,audit_date,agent_id});
		 return count>0;
    }
    public static boolean unLocked(String userName,long agent_id,String remarks,int flag,Date audit_date){
    	String sql="update mb_agent set lock_user=?,lock_msg=?,flag=?,lock_date=? where agent_id=?";
    	int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{userName,remarks,flag,audit_date,agent_id});
		 return count>0;
    }
    
    public static Agent getAgent(String login_name){
		  String sql="select * from mb_agent where login_name =?";
		  List<Agent> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{login_name},new AgentRowMap());
		  if(list.size()>0){
			  return list.get(0);
		  }
		  return null;
	 }
    
    
    /**
     * 修改额度 
     * @param cust_id
     * @param credit
     * @return
     */
    public static boolean modCredit(Long agent_id,BigDecimal credit){
    	if(credit.intValue()>0){
    		String sql="update mb_agent set credit=credit+? where agent_id=?";
        	int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{credit,agent_id});
    		return count>0;
    	}else{
    		BigDecimal t=new BigDecimal(0).subtract(credit);
    		String sql="update mb_agent set credit=credit+? where agent_id=? and credit>=?";
        	int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{credit,agent_id,t});
    		return count>0;
    	}
    }
    
    
    /**
	  * @return
	  */
	 public static boolean NTmodAgentBank(String bank_name,String account_type,
			 String bank_dot,String account,Long agent_id){
		   String sql="update mb_agent set bank_name=?,account_type=?,"
		   		+ "bank_dot=?,account=?  where agent_id=?";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{bank_name,account_type,bank_dot,account,agent_id});
		  return count>0;
		 
	 }
	 
	 public boolean NTupdatePwd(){
		 String sql="update mb_agent set login_pwd=? where login_name=?";
		 int count=Sp.get().getDefaultJdbc().update(sql,
					new Object[]{login_pwd,login_name});
		 return count>0;
	 }
	 
	 public static boolean NTupdateCommission_date(String commission_date,String login_name){
		 String sql="update mb_agent set commission_date=? where login_name=?";
		 int count=Sp.get().getDefaultJdbc().update(sql,
					new Object[]{commission_date,login_name});
		 return count>0;
	 }
    
   
	 
	 
	 
    
    
}
