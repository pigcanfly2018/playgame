package models;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.MyTree;
import util.Sp;
import util.TreeNode;

public class Customer implements MyTree<Customer>,Serializable{
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
    public Boolean bbinmobile_flag;
    
    public BigDecimal credit;
    public Boolean flag;
    public Integer cust_level;
    public Boolean is_agent;
    public Long parent_id;
    public String parent_name;
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
    
    //辅助字段
    public String ip_addr;
    
    
    public String kg_game;
    public String kg_pwd;
    public Boolean kg_flag;
    public Boolean kg_actived;
    
    public String pp_game;
	public String pp_pwd;
	public Boolean pp_flag;
	public Boolean pp_actived;
    
    public String mg_game;
    public String mg_pwd;
    public Boolean mg_flag;
    public Boolean mg_actived;
    
    public String alipay_account;
    public String alipay_name;
    
    public String reg_info;
    
 	public boolean isLeaf() {
 		return !is_agent;
 	}
 	/**
 	 * 重写tree方法
 	 */
 	
 	public TreeNode convertNode() {
 		TreeNode node =new TreeNode();
 		node.id=String.valueOf(this.cust_id);
 		node.text=this.login_name;
 		node.leaf=isLeaf();
 		node.attributes.put("agent_mode", agent_mode);
 		node.expanded=true;
 		return node; 
 	}
 	
 	public List<Customer> getChilds(boolean includeBut) {
 		 if(cust_id==null||cust_id==0L){
 			  String sql="select * from mb_customer where parent_id is null and agent=1 order by cust_id desc";
 			  List<Customer> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{},new CustomerRowMap());
 			  return list;
 		 }else{
 			 String sql="select * from mb_customer where parent_id =? and agent=1 order by cust_id desc";
 			  List<Customer> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{cust_id},new CustomerRowMap());
 			  return list;
 		 }
 	}
 	
 	
 	public Customer getParent() {
 		  String sql="select * from mb_customer where agent=1 and cust_id=? order by cust_id desc";
 		  List<Customer> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{this.parent_id},new CustomerRowMap());
 		  if(list.size()==0)return null;
 		  else return list.get(0);
 	}
 	
    


    public  boolean NTcreat(){
		  String sql="insert into  mb_customer (login_name,login_pwd,"
		  		+ "real_name,phone,email,qq,reg_ip,create_date,create_user,login_ip,"
		  		+ "login_date,login_times,sb_game,sb_pwd,sb_flag,sb_actived,ag_game,"
		  		+ "ag_pwd,ag_flag,ag_actived,bbin_game,bbin_pwd,bbin_flag,bbin_actived,"
		  		+ "credit,flag,cust_level,is_agent,parent_id,reg_source,agent_dm,"
		  		+ "first_deposit_date,remarks,bank_name,account_type,bank_dot,"
		  		+ "account,agent_mode,s_email,pt_game,pt_pwd,pt_flag,pt_actived,"
		  		+ "kg_game,kg_pwd,kg_flag,kg_actived,mg_game,mg_pwd,mg_flag,mg_actived,pp_game,pp_pwd,pp_flag,pp_actived) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,"
		  		+ "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{login_name,
				  login_pwd,real_name,phone,email,qq,reg_ip,create_date,create_user,login_ip,
				  login_date,login_times,sb_game,sb_pwd,sb_flag,sb_actived,ag_game,ag_pwd,
				  ag_flag,ag_actived,bbin_game,bbin_pwd,bbin_flag,bbin_actived,credit,flag,
				  cust_level,is_agent,parent_id,reg_source,agent_dm,first_deposit_date,
				  remarks,bank_name,account_type,bank_dot,account,agent_mode,s_email,pt_game,pt_pwd,pt_flag,pt_actived,
				  kg_game,kg_pwd,kg_flag,kg_actived,mg_game,mg_pwd,mg_flag,mg_actived,pp_game,pp_pwd,pp_flag,pp_actived});
		  return count>0;
	 }
    
    //暂无用
	 public  boolean NTupdate(){
		  String sql="update mb_customer set login_name=?,login_pwd=?,"
		  		+ "real_name=?,phone=?,email=?,qq=?,reg_ip=?,create_date=?,create_user=?,"
		  		+ "login_ip=?,login_date=?,login_times=?,sb_game=?,sb_pwd=?,sb_flag=?,"
		  		+ "sb_actived=?,ag_game=?,ag_pwd=?,ag_flag=?,ag_actived=?,bbin_game=?,"
		  		+ "bbin_pwd=?,bbin_flag=?,bbin_actived=?,credit=?,flag=?,cust_level=?,"
		  		+ "is_agent=?,parent_id=?,reg_source=?,agent_dm=?,first_deposit_date=?,"
		  		+ "remarks=?,bank_name=?,account_type=?,bank_dot=?,account=?,agent_mode=?,"
		  		+ "s_email=? where cust_id=?";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{login_name,login_pwd,real_name,
				  phone,email,qq,reg_ip,create_date,create_user,login_ip,login_date,login_times,
				  sb_game,sb_pwd,sb_flag,sb_actived,ag_game,ag_pwd,ag_flag,ag_actived,bbin_game,
				  bbin_pwd,bbin_flag,bbin_actived,credit,flag,cust_level,is_agent,parent_id,
				  reg_source,agent_dm,first_deposit_date,remarks,bank_name,account_type,
				  bank_dot,account,agent_mode,s_email,cust_id});
		  return count>0;
	 }

	/**
	 * 是否存在
	 * 
	 * @param login_name
	 * @return
	 */
	public static boolean NTexist(String login_name) {
		String sql = "select count(1) from mb_customer where login_name =?";
		int count = Sp.get().getDefaultJdbc()
				.queryForObject(sql, new Object[] { login_name },Integer.class);
		return count > 0;
	}

	/**
	 * 检测电话号码
	 * 
	 * @param login_name
	 * @return
	 */
	public static boolean NTexistPhone(String phone) {
		String sql = "select count(1) from mb_customer where phone =?";
		int count = Sp.get().getDefaultJdbc()
				.queryForObject(sql, new Object[] { phone },Integer.class);
		return count > 0;
	}
	/**
	 * 
	 * @param ip
	 * @return
	 */
	public static int NTCountIp(String ip) {
		String sql = "select count(1) from mb_customer where reg_ip =?";
		int count = Sp.get().getDefaultJdbc()
				.queryForObject(sql, new Object[] { ip },Integer.class);
		return count;
	}
	
	
	   public static boolean NTexitsRealName(String real_name){
		   String sql="select count(1) from mb_customer where real_name=?";
		   int count=Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{real_name},Integer.class);
		   return count>0;
	   }
	   public static boolean NTexitsQQ(String qq){
		   String sql="select count(1) from mb_customer where qq=?";
		   int count=Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{qq},Integer.class);
		   return count>0;
	   }
	   public static boolean NTexitsPhone(String phone){
		   String sql="select count(1) from mb_customer where phone=?";
		   int count=Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{phone},Integer.class);
		   return count>0;
	   }
	   
	   public static boolean NTactive(Long cust_id,boolean s_email,String user){
		   String sql="update mb_customer set s_email=? ,send_date=now(),activated=1, activated_date=now(),"
		   		+ "activated_user =? where cust_id=? ";
		   int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{s_email,user,cust_id});
			return count>0;
	   }
	   
	   
	 
		 
		 public static Long NTgetId(){
			 String sql ="select T_CUSTOMERs_ID_SEQ.Nextval from dual";
			 return Sp.get().getDefaultJdbc().queryForObject(sql,Long.class);
		 }
		 
		 public  boolean NTupdateGameFlag(){
			  String sql="update t_customers set ag_flag=?,ag_dsp_flag=?,bbin_flag=?  where customer_id=?";
			  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{});
			  return count>0;
		 }
		 
		 public static boolean NTrdisable(Long custId){
			  String sql="update t_customers set flag =(case when flag=0 then 1 else 0 end) where customer_id =?";
			  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{custId});
			  return count>0;
		 }
		 
		 public static Customer NTgetCustomer(Long custId){
			  String sql="select * from mb_customer where cust_id =?";
			  List<Customer> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{custId},new CustomerRowMap());
			  if(list.size()>0){
				  return list.get(0);
			  }
			  return null;
		 }
		 
		 public static Customer NTgetCustomer(String login_name){
			  String sql="select * from mb_customer where login_name =?";
			  List<Customer> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{login_name},new CustomerRowMap());
			  if(list.size()>0){
				  return list.get(0);
			  }
			  return null;
		 }
		 
		 public static Customer getCustomer(String login_name){
			  String sql="select * from mb_customer where login_name =?";
			  List<Customer> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{login_name},new CustomerRowMap());
			  if(list.size()>0){
				  return list.get(0);
			  }
			  return null;
		 }
		 
		 public static List<Customer> getCustomerListBylevel(String level){
			  String sql="select * from mb_customer where cust_level =?";
			  List<Customer> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{level},new CustomerRowMap());
			  
			  return list;
		 }
		 
		 public static Customer NTgetCustomerByLoginName(String login_name){
			  String sql="select * from mb_customer where login_name =?";
			  List<Customer> list=Sp.get().getDefaultJdbc().query(sql,new Object[]{login_name},new CustomerRowMap());
			  if(list.size()>0){
				  return list.get(0);
			  }
			  return null;
		 }
		 
		 
		 public static boolean NTmodInfo(String real_name,String phone,String email,String qq,String bank_name,
				 String account,String account_type,String bank_dot,Long custId){
			  String sql="update mb_customer set ";
			  ArrayList<Object> list=new ArrayList<Object>();
			  if(real_name!=null){
				  sql=sql+" real_name=?,";
				  list.add(real_name);
			  }
			  if(phone!=null){
				  sql=sql+" phone=?,";
				  list.add(phone);
			  }
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
			  if(sql.endsWith(",")){
				  sql=sql.substring(0, sql.length()-1);
			  }
			  sql=sql+" where cust_id =? ";;
			  list.add(custId);
			  int count=Sp.get().getDefaultJdbc().update(sql, list.toArray(new Object[0]));
			  return count>0;
		 }
		 
		
		 public static boolean NTmodInfo2(String email,String qq,
				 String remarks,String bank_name,String account,String account_type,String bank_dot,
				 String sb_game,String sb_pwd,Boolean sb_flag,Long custId){
			  String sql="update mb_customer set ";
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
			  if(sb_game!=null){
				  sql=sql+" sb_game=?,";
				  list.add(sb_game);  
			  }
			  if(sb_pwd!=null){
				  sql=sql+" sb_pwd=?,";
				  list.add(sb_pwd);  
			  }
			  if(sb_flag!=null){
				  sql=sql+" sb_flag=?,";
				  list.add(sb_flag);  
			  }
			  sql=sql+" remarks=? where cust_id =? ";
			  list.add(remarks);
			  list.add(custId);
			  int count=Sp.get().getDefaultJdbc().update(sql, list.toArray(new Object[0]));
			  return count>0;
		 }
		 public static boolean NTmodCustStatus(Long cust_id,Integer cust_level,
				Boolean flag,Boolean pt_actived, Boolean bbin_actived,Boolean ag_actived,Boolean mg_actived,Boolean sb_actived,Boolean pp_actived,Boolean pt_flag, Boolean bbin_flag,Boolean ag_flag,Boolean mg_flag,Boolean sb_flag,Boolean pp_flag
				, String pwd,String remarks,Boolean online_pay,Boolean is_block,Boolean alipay_flag,Boolean bbinmobile_flag,Boolean promo_flag,Boolean bank_pay){
			 if(pwd==null){
				 String sql="update mb_customer set flag=?,cust_level=?,pt_actived=?,bbin_actived=?,ag_actived=?,mg_actived=?,sb_actived=?,pp_actived=?,pt_flag=?,bbin_flag=?,ag_flag=?,mg_flag=?,sb_flag=?,pp_flag=?"
					 		+ ",remarks=?,online_pay=?,is_block=?,alipay_flag=?,bbinmobile_flag=?,promo_flag=?,bank_pay=?  where cust_id=? ";
				 Object[] objs=new Object[]{flag,cust_level,pt_actived,bbin_actived,ag_actived,mg_actived,sb_actived,pp_actived,pt_flag,bbin_flag,ag_flag,mg_flag,sb_flag,pp_flag,remarks,online_pay,is_block,alipay_flag,bbinmobile_flag,promo_flag,bank_pay,cust_id};
				 int count=Sp.get().getDefaultJdbc().update(sql, objs);
				 return count>0;
			 }else{
				 String sql="update mb_customer set login_pwd=?,flag=?,cust_level=?,pt_actived=?,bbin_actived=?,ag_actived=?,mg_actived=?,sb_actived=?,pp_actived=?,pt_flag=?,bbin_flag=?,ag_flag=?,mg_flag=?,sb_flag=?,pp_flag=?,remarks=?,online_pay=?,is_block=?,alipay_flag=?,bbinmobile_flag=?,promo_flag=?,bank_pay=?  where cust_id=? ";
				 Object[] objs=new Object[]{pwd,flag,cust_level,pt_actived,bbin_actived,ag_actived,mg_actived,sb_actived,pp_actived,pt_flag,bbin_flag,ag_flag,mg_flag,sb_flag,pp_flag,remarks,online_pay,is_block,alipay_flag,bbinmobile_flag,promo_flag,bank_pay,cust_id};
				 int count=Sp.get().getDefaultJdbc().update(sql, objs);
				 return count>0;
			 }
			 
			 
		 }
		 public static boolean NTmodCustlevel(Long customerId,Integer cust_level){
			 String sql="update mb_customer set cust_level=? where cust_id=?";
			  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{cust_level,customerId});
			  return count>0;
			 
		 }
		 public static boolean NTmodCustlevelFirst(Long customerId,Integer cust_level){
			 String sql="update mb_customer set cust_level=?,first_deposit_date=now() where cust_id=? and cust_level = 0";
			  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{cust_level,customerId});
			  return count>0;
			 
		 }
		 
		 
		 
		 public static boolean NTmodLoginName(Long customerId,String login_name){
			 String sql="update mb_customer set login_name=? where cust_id=? and activated = 0";
			  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{login_name,customerId});
			  return count>0;
			 
		 }
		 
		 public static boolean NTmodCustLevel(Long customerId,Integer cust_level){
			 String sql="update mb_customer set cust_level=? where cust_id=? and cust_level = 0";
			  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{customerId,cust_level});
			  return count>0;
			 
		 }
		 
		 /**
		  * @return
		  */
		 public static boolean NTmodCustBank(String bank_name,String account_type,
				 String bank_dot,String account,Long cust_id){
			   String sql="update mb_customer set bank_name=?,account_type=?,"
			   		+ "bank_dot=?,account=?  where cust_id=?";
			  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{bank_name,account_type,bank_dot,account,cust_id});
			  return count>0;
			 
		 }
		 
		 
		 
		public static boolean NTdeleteCustomer(Long cust_id){
			 String sql="delete from mb_customer where cust_id =? and cust_level=0";
			 int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{cust_id});
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
	    
	    
	    public static List<Customer> getSubCustomoerList(Long agent_id){
	    	String sql="select * from mb_customer where parent_id =? ";
	 		List<Customer> funcs=Sp.get().getDefaultJdbc().query(sql,
	 					new Object[]{agent_id},new CustomerRowMap());
	     	return funcs;
	    }
	    
	    
}
