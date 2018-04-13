package com.product.bda.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import bsz.exch.core.MyPreparedStatementCreator;
import bsz.exch.utils.LogHelper;
import bsz.exch.utils.RandomUtil;

public class CustomerService {
	
    private static Logger logger=Logger.getLogger(CustomerService.class);
	
	private JdbcTemplate template;
	private String datasource;
	public CustomerService(JdbcTemplate template,String datasource){
		this.template=template;
		this.datasource=datasource;
	}
	
	/**
	 * 检查余额是否大于等于
	 * @param login_name
	 * @param credit
	 * @return
	 */
	public boolean checkCredit(String login_name,BigDecimal credit){
		String sql="select count(1) from mb_customer where login_name =? and credit >= ?";
		List<Object> list =new ArrayList<Object>();
		list.add(login_name);
		list.add(credit);
		logger.info(LogHelper.dbLogObj(datasource,sql, list));
		int count=template.queryForObject(sql,list.toArray(new Object[0]),Integer.class);
		return count>0;
	}
	
	
	/**
	 * 查询玩家额度
	 * @param login_name
	 * @return
	 */
	public BigDecimal queryCredit(String login_name){
		String sql="select credit from mb_customer where login_name =?";
		List<Object> list =new ArrayList<Object>();
		list.add(login_name);
		logger.info(LogHelper.dbLogObj(datasource,sql, list));
		List<Map<String,Object>> result =template.queryForList(sql,list.toArray(new Object[0]));
		if(result.size()>0){
			BigDecimal b=(BigDecimal)result.get(0).get("credit");
			return b;
		}
		return new BigDecimal(0);
		
	}
	
	/**
	 * 更新游戏厅开立状态
	 * @param plat
	 * @param login_name
	 * @param flag
	 */
	public void updateGameFlag(String plat,String login_name,int flag){
		
		String sql="";
		
		if("AG".equals(plat)){
			sql="update mb_customer set ag_flag=? where login_name=? ";
		}
		if("BBIN".equals(plat)){
			sql="update mb_customer set bbin_flag=? where login_name=? ";
		}
		if("PT".equals(plat)){
			sql="update mb_customer set pt_flag=? where login_name=? ";
		}
		if("KG".equals(plat)){
			sql="update mb_customer set kg_flag=? where login_name=? ";
		}
		if("MG".equals(plat)){
			sql="update mb_customer set mg_flag=? where login_name=? ";
		}
		if("VS".equals(plat)){
			sql="update mb_customer set sb_flag=? where login_name=? ";
		}
		if("PP".equals(plat)){
			sql="update mb_customer set pp_flag=? where login_name=? ";
		}
		List<Object> list =new ArrayList<Object>();
		list.add(flag);
		list.add(login_name);
		logger.info(LogHelper.dbLogObj(datasource,sql, list));
		template.update(sql,list.toArray(new Object[0]));
	}
	
	
public void updateMgAlias(String plat,String login_name,String Alias){
		
		String sql="";
		
		if("MG".equals(plat)){
			sql="update mb_customer set mg_alias=? where login_name=? ";
		}
		List<Object> list =new ArrayList<Object>();
		list.add(Alias);
		list.add(login_name);
		logger.info(LogHelper.dbLogObj(datasource,sql, list));
		template.update(sql,list.toArray(new Object[0]));
	}
	
	
	/**
	 * 更新游戏厅开立状态
	 * @param plat
	 * @param login_name
	 * @param flag
	 */
	public void updateBBinMobileGameFlag(String login_name,int flag){
		
		String sql="";
		
		sql="update mb_customer set bbinmobile_flag=? where login_name=? ";
		
		List<Object> list =new ArrayList<Object>();
		list.add(flag);
		list.add(login_name);
		logger.info(LogHelper.dbLogObj(datasource,sql, list));
		template.update(sql,list.toArray(new Object[0]));
	}
	
	
	/**
	 * 获取客户信息
	 * @param login_name
	 * @return
	 */
	public Customer getCustomer(String login_name){
		String sql="select * from mb_customer where login_name =?";
		List<Object> list =new ArrayList<Object>();
		list.add(login_name);
		logger.info(LogHelper.dbLogObj(datasource,sql, list));
		List<Customer> custList=template.query(sql,list.toArray(new Object[0]),new  RowMapper<Customer>(){
			public Customer mapRow(ResultSet rs, int arg1)
					throws SQLException {
				Customer customer =new Customer();
				customer.cust_id=rs.getLong("cust_id");
				customer.login_name=rs.getString("login_name");
				customer.cust_level=rs.getInt("cust_level");
				customer.flag=rs.getInt("flag");
				customer.credit=rs.getBigDecimal("credit");
				customer.score=rs.getBigDecimal("score");
				customer.promo_flag=rs.getBoolean("promo_flag");
				customer.real_name=rs.getString("real_name");
				customer.alipay_account=rs.getString("alipay_account");
				customer.create_date = rs.getDate("create_date");
				customer.parent_id = rs.getLong("parent_id");
				
				customer.sb_game=rs.getString("sb_game");
				customer.sb_pwd=rs.getString("sb_pwd");
				customer.sb_flag=rs.getInt("sb_flag");
				customer.sb_actived=rs.getInt("sb_actived");
				
				customer.ag_game=rs.getString("ag_game");
				customer.ag_pwd=rs.getString("ag_pwd");
				customer.ag_flag=rs.getInt("ag_flag");
				customer.ag_actived=rs.getInt("ag_actived");
				
				customer.bbin_game=rs.getString("bbin_game");
				customer.bbin_pwd=rs.getString("bbin_pwd");
				customer.bbin_flag=rs.getInt("bbin_flag");
				customer.bbin_actived=rs.getInt("bbin_actived");
				
				customer.bbinmobile_game=rs.getString("bbinmobile_game");
				customer.bbinmobile_pwd=rs.getString("bbinmobile_pwd");
				customer.bbinmobile_flag=rs.getInt("bbinmobile_flag");
				
				customer.pt_game=rs.getString("pt_game");
				customer.pt_pwd=rs.getString("pt_pwd");
				customer.pt_flag=rs.getInt("pt_flag");
				customer.pt_actived=rs.getInt("pt_actived");
				
				customer.kg_game=rs.getString("kg_game");
				customer.kg_pwd=rs.getString("kg_pwd");
				customer.kg_flag=rs.getInt("kg_flag");
				customer.kg_actived=rs.getInt("kg_actived");
				
				customer.mg_game=rs.getString("mg_game");
				customer.mg_pwd=rs.getString("mg_pwd");
				customer.mg_flag=rs.getInt("mg_flag");
				customer.mg_actived=rs.getInt("mg_actived");
				
				customer.pp_game=rs.getString("pp_game");
				customer.pp_pwd=rs.getString("pp_pwd");
				customer.pp_flag=rs.getInt("pp_flag");
				customer.pp_actived=rs.getInt("pp_actived");
				
				customer.alipay_name=rs.getString("alipay_name");
				
				return customer;
			}
		});
		if(custList.size() == 0){
			return null;
		}else{
			return custList.get(0);
		}
		
	}
	
	
	
	
	public Long createCustomer(String login_name,String login_pwd,String real_name,String phone,String email,String qq,String reg_ip,String create_user,String sb_game,String sb_pwd,
			String ag_game,String ag_pwd,String bbin_game,String bbin_pwd,Long parent_id,String reg_source,
			String pt_game,String pt_pwd,String kg_game,String kg_pwd,String mg_game,String mg_pwd, 
			String reg_domain, String refer_url, String search_key){
		String sql="insert into  mb_customer (login_name,login_pwd,real_name,phone,email,"
				+ "qq,reg_ip,create_date,create_user,login_ip,login_date,login_times,sb_game,sb_pwd,sb_flag,sb_actived,ag_game,ag_pwd,ag_flag,ag_actived,"
				+ "bbin_game,bbin_pwd,bbin_flag,bbin_actived,credit,flag,cust_level,is_agent,parent_id,reg_source,"
				+ "agent_dm,first_deposit_date,remarks,bank_name,account_type,bank_dot,account,agent_mode,s_email,pt_game,pt_pwd,pt_flag,"
				+ "pt_actived,kg_game,kg_pwd,kg_flag,kg_actived,mg_game,mg_pwd,mg_flag,mg_actived,bbinmobile_game,bbinmobile_pwd,reg_domain,refer_url,search_key) "
				+ "values(?,?,?,?,?,?,?,now(),?,null,null,0,?,?,0,1,?,?,0,1,?,?,0,1,0,1,0,0,?,?,"
				+ "'',null,'','','','','',0,0,?,?,0,1,?,?,0,1,?,?,0,1,?,?,?,?,?)";
	   
		List<Object> list =new ArrayList<Object>();
		list.add(login_name);
		list.add(login_pwd);
		list.add(real_name);
		list.add(phone);
		list.add(email);
		list.add(qq);
		list.add(reg_ip);
		list.add(create_user);
		
		list.add(sb_game);//13
		list.add(sb_pwd);
		
		list.add(ag_game);
		list.add(ag_pwd);
		list.add(bbin_game);
		list.add(bbin_pwd);
		list.add(parent_id);
		list.add(reg_source);
		list.add(pt_game);
		list.add(pt_pwd);
		list.add(kg_game);
		list.add(kg_pwd);
		list.add(mg_game);
		list.add(mg_pwd);
		list.add("daw"+login_name+"@dj8");
		list.add(mg_pwd);
		list.add(reg_domain);
		list.add(refer_url);
		list.add(search_key);
		
		logger.info(LogHelper.dbLogObj(datasource,sql, list));
		
		KeyHolder keyHolder = new GeneratedKeyHolder();
		int result=template.update(new MyPreparedStatementCreator(sql,list.toArray(new Object[0])), keyHolder);
		if(result>0){
			return keyHolder.getKey().longValue();
		}
		return null;
	}
	
	
	
	 public  Long getAgentByRegCode(String reg_code){
		  String sql="select * from mb_agent where reg_code =?";
		  logger.info(LogHelper.dbLogObj(datasource,sql,   Arrays.asList(new Object[]{reg_code})));
		  List<Map<String,Object>> result=template.queryForList(sql,new Object[]{reg_code});
			if(result.size()>0){
				Long b=(Long)result.get(0).get("agent_id");
				return b;
			}
		  return null;
	 }
	 
	 /**
		 * 记录登陆信息
		 * @param login_name
		 * @param login_ip
		 */
		public void recordLoginInfo(String login_name,String login_ip,String user_agent){
			String sql="update mb_customer set login_ip=?,login_date=now(),login_times=login_times+1 where login_name=? ";
			List<String> list =new ArrayList<String>();
			list =new ArrayList<String>();
			list.add(login_ip);
			list.add(login_name);
			logger.info(LogHelper.dbLog(datasource,sql, list));
			int flag=template.update(sql,list.toArray(new String[0]));
			this.createCustLog(login_ip, "user_agent",login_name,2, "客户登录");
		}
	 
	 
	 public void createCustLog(String login_ip,String user_agent,String login_name,int log_type,String login_msg){
		String sql="insert into mb_cust_log (log_type,log_msg,ip,user_agent,create_date,cust_id,login_name) select ?,?,?,?,now(),cust_id,login_name from mb_customer where login_name =?";
	    List<Object>list =new ArrayList<Object>();
	    list.add(log_type);
	    list.add(login_msg);
		list.add(login_ip);
		list.add(user_agent);
		list.add(login_name);
		logger.info(LogHelper.dbLogObj(datasource,sql,list));
		template.update(sql,list.toArray(new Object[0])); 
	 }
	 
	 
	 public void loginRecord(String login_ip,String login_name){
		 String  sql="update mb_customer set login_ip=?,login_date=now(),login_times=login_times+1 where login_name=? ";
		 List<Object> list =new ArrayList<Object>();
		list.add(login_ip);
		list.add(login_name);
		logger.info(LogHelper.dbLogObj(datasource,sql,list));
		template.update(sql,list.toArray(new Object[0]));
	 }
	 

	public boolean locked(String login_name){
		String sql="update mb_customer set sys_locked =1,locked_date=now() where sys_locked =0 and login_name= ? ";
		List<Object> list =new ArrayList<Object>();
		list.add(login_name);
	    logger.info(LogHelper.dbLogObj(datasource,sql,   Arrays.asList(new Object[]{login_name})));
	    int c= template.update(sql,list.toArray(new Object[0]));
	    return c>0;
	}
	
	public boolean unlocked(String login_name){
		String sql="update mb_customer set sys_locked =0,locked_date=null  where login_name= ? ";
		List<Object> list =new ArrayList<Object>();
		list.add(login_name);
	    logger.info(LogHelper.dbLogObj(datasource,sql,   Arrays.asList(new Object[]{login_name})));
	    int c= template.update(sql,list.toArray(new Object[0]));
	    return c>0;
	}
	
	 public boolean NTmodCustlevelFirst(Long customerId,Integer cust_level){
		 String sql="update mb_customer set cust_level=?,first_deposit_date=now() where cust_id=? and cust_level = 0";
		 List<Object> list =new ArrayList<Object>();
		 list.add(cust_level);
		 list.add(customerId);
		 logger.info(LogHelper.dbLogObj(datasource,sql,list));
		 int c= template.update(sql,list.toArray(new Object[0]));
		 return c>0;
	 }
	 
	 public boolean NTmodCustlevelOnly(Long customerId,Integer cust_level){
		 String sql="update mb_customer set cust_level=? where cust_id=? and cust_level = 0";
		 List<Object> list =new ArrayList<Object>();
		 list.add(cust_level);
		 list.add(customerId);
		 logger.info(LogHelper.dbLogObj(datasource,sql,list));
		 int c= template.update(sql,list.toArray(new Object[0]));
		 return c>0;
	 }
	 
	 public boolean NTmodFirstDepositDateOnly(Long customerId,Integer cust_level){
		 String sql="update mb_customer set first_deposit_date=now() where cust_id=? and cust_level = 0";
		 List<Object> list =new ArrayList<Object>();
		 list.add(customerId); 
		 logger.info(LogHelper.dbLogObj(datasource,sql,list));
		 int c= template.update(sql,list.toArray(new Object[0]));
		 return c>0;
	 }
	 
	 public boolean updatePhoneRegFlag(String login_name){
			String sql ="update mb_customer set phone_reg=1 where login_name=?";
			List<String> objs =new ArrayList<String>();
			objs.add(login_name);
			logger.info(LogHelper.dbLog(datasource,sql, objs));
			int result=this.template.update(sql, objs.toArray(new String[0]));
			return result>0;
			
		}
		
	 public Customer getCustomerByRegPhone(String login_name){
			
			String sql="select * from mb_customer where phone =? and phone_reg =1";
			
			Object[] objs=new Object[]{login_name} ;
			
			List<Customer> list=this.template.query(sql,objs, new  RowMapper<Customer>(){
				public Customer mapRow(ResultSet rs, int arg1)
						throws SQLException {
					Customer customer =new Customer();
					customer.cust_id=rs.getLong("cust_id");
					customer.login_name=rs.getString("login_name");
					customer.cust_level=rs.getInt("cust_level");
					customer.flag=rs.getInt("flag");
					customer.credit=rs.getBigDecimal("credit");
					customer.score=rs.getBigDecimal("score");
					customer.real_name=rs.getString("real_name");
					customer.alipay_account=rs.getString("alipay_account");
					customer.create_date = rs.getDate("create_date");
					
					customer.ag_game=rs.getString("ag_game");
					customer.ag_pwd=rs.getString("ag_pwd");
					customer.ag_flag=rs.getInt("ag_flag");
					customer.ag_actived=rs.getInt("ag_actived");
					
					customer.sb_game=rs.getString("sb_game");
					customer.sb_pwd=rs.getString("sb_pwd");
					customer.sb_flag=rs.getInt("sb_flag");
					customer.sb_actived=rs.getInt("sb_actived");
					
					customer.bbin_game=rs.getString("bbin_game");
					customer.bbin_pwd=rs.getString("bbin_pwd");
					customer.bbin_flag=rs.getInt("bbin_flag");
					customer.bbin_actived=rs.getInt("bbin_actived");
					
					customer.bbinmobile_game=rs.getString("bbinmobile_game");
					customer.bbinmobile_pwd=rs.getString("bbinmobile_pwd");
					customer.bbinmobile_flag=rs.getInt("bbinmobile_flag");
					
					customer.pt_game=rs.getString("pt_game");
					customer.pt_pwd=rs.getString("pt_pwd");
					customer.pt_flag=rs.getInt("pt_flag");
					customer.pt_actived=rs.getInt("pt_actived");
					
					customer.kg_game=rs.getString("kg_game");
					customer.kg_pwd=rs.getString("kg_pwd");
					customer.kg_flag=rs.getInt("kg_flag");
					customer.kg_actived=rs.getInt("kg_actived");
					
					customer.mg_game=rs.getString("mg_game");
					customer.mg_pwd=rs.getString("mg_pwd");
					customer.mg_flag=rs.getInt("mg_flag");
					customer.mg_actived=rs.getInt("mg_actived");
					customer.alipay_name=rs.getString("alipay_name");
					
					return customer;
				}
			});
			
			if(list.isEmpty()){
				
				return null;
			}
			return list.get(0);
		}
	 
	 /**
		 * 用户登陆
		 * @param login_name 用户名
		 * @param login_pwd  密码
		 * @return
		 */
		public boolean login(String login_name,String login_pwd){
			String sql="select count(1) from mb_customer where login_name =? and login_pwd=? and flag=1";
			List<String> list =new ArrayList<String>();
			list.add(login_name);
			list.add(login_pwd);
			logger.info(LogHelper.dbLog(datasource,sql, list));
			Integer count=template.queryForObject(sql,list.toArray(new String[0]),Integer.class);
			return count>0;
		}
		
		
		//通过手机查询
		public int queryCntByPhone(String phone){
			 String sql ="select count(1) from mb_customer where phone =?";
			 Object[] objs=new Object[]{phone};
			 logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
			int result=this.template.queryForObject(sql, objs, Integer.class);
			return result;
		}
		
		//通过注册IP查询
		public int queryCntByRegIp(String reg_ip){
			 String sql ="select count(1) as cnt from mb_customer where reg_ip= ?  and  create_date > curdate() ";
			 Object[] objs=new Object[]{reg_ip};
			 logger.info(LogHelper.dbLogObj(datasource,sql,Arrays.asList(objs)));
			int result=this.template.queryForObject(sql, objs, Integer.class);
			return result;
		}
		
		
		/**
		 * 注册
		 * @param keyword 
		 * @param referdomain 
		 * @param reg_domain 
		 * @return
		 */
		public boolean reg(String login_name,String login_pwd,String real_name,String phone,
				String qq,String reg_ip,String create_user,String reg_code, String reg_domain, String referdomain, String keyword){
			
			Long agentId=null;
			if(reg_code!=null){
				String queryAgentIdSql="select agent_id from mb_agent where reg_code=?";
				Object[] objs=new Object[]{reg_code} ;
				List<Long> agentList=this.template.queryForList(queryAgentIdSql, objs, Long.class);
				if(agentList.size()==0){
					agentId=null;
				}else{
				   agentId = agentList.get(0);
				}
			}
			
			String sql ="insert into mb_customer(login_name,login_pwd,real_name,phone,email," //5
					+ "qq,reg_ip,create_date,create_user,credit,"                             //5
					+ "flag,cust_level,is_agent,parent_id,reg_source," 						//5
					+ "agent_dm,first_deposit_date,remarks,bank_name,account_type,"			//5
					+ "bank_dot,account,agent_mode,s_email,score,"							//5
					+ "pt_game,pt_pwd,pt_flag,pt_actived,online_pay,"						//5
					+ "is_block,alipay_flag,mg_game,mg_pwd,mg_flag,"						//5
					+ "mg_actived,kg_game,kg_pwd,kg_flag,kg_actived,"						//5
					+ "bbin_game,bbin_pwd,bbin_flag,bbin_actived,bbinmobile_game,bbinmobile_pwd,bbinmobile_flag,"  //7
					+ "sb_game,sb_pwd,sb_flag,sb_actived,"						//4
					+ "ag_game,ag_pwd,ag_flag,ag_actived,reg_domain,refer_url,search_key,"
					+ "pp_game,pp_pwd,pp_flag,pp_actived) "   //4
					
					+ "values(?,?,?,?,?,"		//5
					+ "?,?,now(),?,0.00,"       //5
					+ "1,0,0,?,?,"				//5
					+ "?,null,?,?,?,"           //5
					+ "?,?,null,0,0.00,"		//5
					+ "?,?,0,1,1,"				//5
					+ "0,1,?,?,0,"				//5
					+ "1,?,?,0,1,"				//5
					+ "?,?,0,1,?,?,0,"
					+ "?,?,0,1,"				//4
					+ "?,?,0,1,?,?,?,"
					+ "?,?,0,1"				//4
					+ ")";
			Object[] objs=new Object[]{login_name,login_pwd,real_name,phone,null,
					qq,reg_ip,create_user,
					agentId,"",
					"","","","",
					"","",
					("daw"+login_name).toUpperCase(),"c123c123",
					"daw"+login_name,"a123a123",
					"daw"+login_name,"b123b123",
					"daw"+login_name,"b123b123","daw"+login_name+"@dj8",RandomUtil.getRandom(8),
					"daw"+login_name,"b123b123",
					"daw"+login_name,"b123b123",
					reg_domain,referdomain,keyword,"daw"+login_name,"b123b123"} ;
			  KeyHolder keyHolder = new GeneratedKeyHolder();
			  logger.info(LogHelper.dbLogObj(datasource,sql, Arrays.asList(objs)));
			  template.update(new MyPreparedStatementCreator(sql,objs), keyHolder);
			  long cust_id= keyHolder.getKey().longValue();
			  
			  return cust_id>0;
		}

		/**
	 * 激活PT
	 * @param login_name
	 * @return
	 */
	public boolean activedPlat(String login_name,String plat_flag){
		String sql ="update mb_customer set "+plat_flag+" =1  where login_name = ? ";
		List<String> objs =new ArrayList<String>();
		objs.add(login_name);
		logger.info(LogHelper.dbLog(datasource,sql, objs));
		int result=this.template.update(sql, objs.toArray(new String[0]));
		return result>0;
	}
		
	 

}
