package com.product.bda.handler;

import java.math.BigDecimal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.product.bda.service.ConfigService;
import com.product.bda.service.CreditService;
import com.product.bda.service.Customer;
import com.product.bda.service.CustomerService;

import bsz.exch.bean.Errors;
import bsz.exch.bean.FobbinInfo;
import bsz.exch.bean.Result;
import bsz.exch.bean.Task;
import bsz.exch.core.After;
import bsz.exch.core.Before;
import bsz.exch.core.Handler;
import bsz.exch.core.InterFace;
import bsz.exch.core.JdbcResource;
import bsz.exch.core.MyPreparedStatementCreator;
import bsz.exch.core.Params;
import bsz.exch.core.Service;
import bsz.exch.game.AginApi;
import bsz.exch.game.BbinApi;
import bsz.exch.game.KgApi;
import bsz.exch.game.MgApi;
import bsz.exch.game.PpApi;
import bsz.exch.game.PtApi;
import bsz.exch.game.SbApi;
import bsz.exch.utils.PhoneUtil;

@Handler(name="PLAT")
public class PlatHandler {
	
	@Before
	public FobbinInfo before(Task task,InterFace inter){
		String plat=task.getParam("plat");
		//检查产品
		String product=task.getParam("product");
		if(!"8da".equals(product) && !"bojin".equals(product)){
			return  new FobbinInfo(true,"No product support!");
		}
		//检查是否支持的平台
		if(!("AG".equals(plat)||"BBIN".equals(plat)||"PT".equals(plat)||"KG".equals(plat)||"MG".equals(plat)||"VS".equals(plat)||"PP".equals(plat))){
			return  new FobbinInfo(true,plat+" is not support!");
		}
		//检查游戏是否关闭
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		ConfigService configService =new ConfigService(template,inter.getDataSource());
		boolean able=configService.queryGameStatus(plat,product);
		if(!able){
			return  new FobbinInfo(true,plat+" is closed");
		}
		return new FobbinInfo(false,"");
		
	}
	
	@After
	public void after(Task task,InterFace inter){
		String plat=task.getParam("plat");
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		CustomerService custService = new CustomerService(template,inter.getDataSource());
		//为客户解锁
		String login_name=task.getParam("login_name");	
		
		if("IN".equals(inter.getService())||"OUT".equals(inter.getService())){
		    custService.unlocked(login_name);
		}
	}
	
	/**
	 * 创建转账记录
	 * @param jdbcTemplate
	 * @param bill_no
	 * @param game_name
	 * @param game_pwd
	 * @param credit
	 * @param direct
	 * @param plat
	 * @return
	 */
	private Long createRecord(JdbcTemplate jdbcTemplate,String bill_no,String game_name,String game_pwd,
			BigDecimal credit,String direct,String plat,String product,String ref_order_no){
		String sql="insert into game_transfer_data (bill_no,game_name,game_pwd,credit,direct,plat,create_date,flag,product,ref_order_no) value(?,?,?,?,?,?,now(),0,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new MyPreparedStatementCreator(sql,new Object[]{bill_no,game_name,game_pwd,credit,direct,plat,product,ref_order_no}), keyHolder);
		Long id= keyHolder.getKey().longValue();
		return id;
	}
	
	/**
	 * 完成转账记录
	 * @param jdbcTemplate
	 * @param data_id
	 * @param flag
	 * @return
	 */
	private boolean finishRecord(JdbcTemplate jdbcTemplate,Long data_id,int flag){
		String sql="update game_transfer_data set flag=?,finish_date=now() where data_id =? ";
		int c=jdbcTemplate.update(sql,new Object[]{flag,data_id});
		return c>0;
	}
	
	
	/**
	 * 转账
	 * @param task
	 * @param inter
	 * @return
	 */
	@Service(name="PTLOGIN")
	@Params(validateField={"username","code","product"})
	public Result ptLogin(Task task,InterFace inter){
		
		String username=task.getParam("username");	
		
		String product=task.getParam("product");
		//额度变化类型
		String code=task.getParam("code");
	
		boolean flag=PtApi.get(product).ptlogin(username, code);
		
		if(flag){
			return Result.createSuccess(task.getId(), task.getFunId(), "1");
		}
		
		
		 return Result.createSuccess(task.getId(), task.getFunId(), "0");
	}
	
	@Service(name="CALL")
	@Params(validateField={"number","product"})
	public Result call(Task task,InterFace inter){
		String product=task.getParam("product");
		String number=task.getParam("number");
		
		boolean flag = PhoneUtil.get(product).callphone(number);
		System.out.println("flag " + flag);
		if(flag){
			return Result.createSuccess(task.getId(), task.getFunId(), "1");
		}else{
			return Result.createSuccess(task.getId(), task.getFunId(), "0");
		}
		
	}

	/**
	 * 转账
	 * @param task
	 * @param inter
	 * @return
	 */
	@Service(name="IN")
	@Params(validateField={"plat","login_name","product"})
	public Result transferIn(Task task,InterFace inter){
		String plat=task.getParam("plat");
		String login_name=task.getParam("login_name");	
		String login_ip=task.getParam("login_ip");

		String product=task.getParam("product");
		//额度变化类型
		String log_type=task.getParam("log_type");
		//额度变化备注
		String remarks=task.getParam("remarks");
		//创建人
		String create_user=task.getParam("create_user");
		//订单号
		String order_no=task.getParam("order_no");
		
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		String ds=inter.getDataSource();
		CustomerService custService = new CustomerService(template,ds);
		if(!custService.locked(login_name)){
			 return Result.createError(task.getId(), task.getFunId(), Errors.NoSupportService_1002, "Account is locded,Please try again!");
			
		}
		Customer cust =custService.getCustomer(login_name);
		
		BigDecimal credit =new BigDecimal(0);
		String amount =task.getParam("amount");
		//不支持小数点转入
		if(amount !=null){
			credit=new BigDecimal(new BigDecimal(amount).intValue());
		}else{
			credit=new BigDecimal(cust.credit.intValue());
		}
		
		if(cust.credit.floatValue()<credit.floatValue()){
			 return Result.createError(task.getId(), task.getFunId(), Errors.CreditNotEnough_2002, "Credit Not Enongh!");
		}
		
		String bill_no=System.currentTimeMillis()+"";
		String game_name="";
		String game_pwd="";
		if(!(("AG".equals(plat)&&cust.ag_flag==1)||
				("BBIN".equals(plat)&&cust.bbin_flag==1)||
				("PT".equals(plat)&&cust.pt_flag==1)||
				("KG".equals(plat)&&cust.kg_flag==1)||
				("VS".equals(plat)&&cust.sb_flag==1)||
				("PP".equals(plat)&&cust.pp_flag==1)||
				("MG".equals(plat)&&cust.mg_flag==1))){
			return Result.createError(task.getId(), task.getFunId(), Errors.NoSupportService_1002, " May be plat or account is not support!");
		}
		
		if("AG".equals(plat)){
			 if(cust.ag_flag==0){
				 boolean f= AginApi.get(product).CheckOrCreateGameAccount(cust.ag_game,cust.ag_pwd);
				 if(f){
					 custService.updateGameFlag(plat, login_name, 1);
				 }
			 }
		}
		if("BBIN".equals(plat)){
			if(cust.bbinmobile_flag==0){
				 boolean f= BbinApi.get(product).CheckOrCreateMobileAccount(cust.bbin_game,cust.bbinmobile_pwd);
				 if(f){
					 custService.updateBBinMobileGameFlag( login_name, 1);
				 }
			}
			
			 if(cust.bbin_flag==0){
				 boolean f= BbinApi.get(product).CheckOrCreateGameAccount(cust.ag_game,cust.ag_pwd);
				 if(f){
					 custService.updateGameFlag(plat, login_name, 1);
				 }
			 }
			
		}
		if("PT".equals(plat)){
			 if(cust.pt_flag==0){
				 //boolean f= PtApi.get(product).CheckOrCreateGameAccount(cust.ag_game,cust.ag_pwd);
				 boolean f= PtApi.get(product).CheckOrCreateGameAccount(cust.pt_game,cust.pt_pwd);
				 if(f){
					 custService.updateGameFlag(plat, login_name, 1);
				 }
			 }
		}
		if("KG".equals(plat)){
			if(cust.kg_flag==0){
				String url=KgApi.get(product).CreateAccountAndLogin(cust.kg_game, login_ip);
				if(url!=null&&url.startsWith("/client")){
					 custService.updateGameFlag(plat, login_name, 1);
				}
			 }
			 
		}
		if("MG".equals(plat)){
			if(cust.mg_flag==0){
				 boolean f= MgApi.get(product).addAccount(cust.mg_game, cust.mg_pwd, login_ip);
				 if(f){
					 custService.updateGameFlag(plat, login_name, 1);
				 }
			 }
			 
		}
		
		if("PP".equals(plat)){
			if(cust.pp_flag==0){
				boolean f= PpApi.get(product).CheckOrCreateGameAccount(cust.pp_game);
				 if(f){
					 custService.updateGameFlag(plat, login_name, 1);
				 }
			 }
			 
		}
		
		if("VS".equals(plat)){
			if(cust.sb_flag==0){
				 String f= SbApi.get(product).authorize(login_ip, cust.sb_game);
				 if(f !=null && !f.equals("")){
					 custService.updateGameFlag(plat, login_name, 1);
				 }
			 }
			 
		}
		
		
		
		if("AG".equals(plat)){
			game_name=cust.ag_game;
			game_pwd=cust.ag_pwd;
			if(credit.floatValue()>0){
				  Long record_id= this.createRecord(template, bill_no, game_name, game_pwd, credit, "IN", plat, product,order_no);
				  boolean flag=AginApi.get(product).Transfer(game_name, bill_no,  "IN", credit, game_pwd);
				  if(flag){
					  CreditService creditService =new CreditService(template,ds);
					  if(creditService.subtract(login_name, credit, log_type, remarks, create_user, order_no)){
						  this.finishRecord(template, record_id, 1);
						  return Result.createSuccess(task.getId(), task.getFunId(), "1");
					  }else{
						  //多出额度
						  this.finishRecord(template, record_id, 3);
						  return Result.createSuccess(task.getId(), task.getFunId(), "0");

					  }
				  }
				  this.finishRecord(template, record_id, 2);
			}
		}
		if("BBIN".equals(plat)){
			game_name=cust.bbin_game;
			game_pwd=cust.bbin_pwd;
			if(credit.floatValue()>0){
				  Long record_id= this.createRecord(template, bill_no, game_name, game_pwd, credit, "IN", plat, product,order_no);
				  boolean flag=BbinApi.get(product).Transfer(game_name, bill_no,  "IN", credit);
				  if(flag){
					  CreditService creditService =new CreditService(template,ds);
					  if(creditService.subtract(login_name, credit, log_type, remarks, create_user, order_no)){
						  this.finishRecord(template, record_id, 1);
						  return Result.createSuccess(task.getId(), task.getFunId(), "1");
					  }else{
						//多出额度
						  this.finishRecord(template, record_id, 3);
						  return Result.createSuccess(task.getId(), task.getFunId(), "0");
					  }
				  }
				  this.finishRecord(template, record_id, 2);
			}
		}
		if("KG".equals(plat)){
			game_name=cust.kg_game;
			game_pwd=cust.kg_pwd;
			if(credit.floatValue()>0){
				  Long record_id= this.createRecord(template, bill_no, game_name, game_pwd, credit, "IN", plat, product,order_no);
				  boolean flag=KgApi.get(product).Transfer(game_name, bill_no,  "IN", credit, login_ip);
				  if(flag){
					  CreditService creditService =new CreditService(template,ds);
					  if(creditService.subtract(login_name, credit, log_type, remarks, create_user, order_no)){
						  this.finishRecord(template, record_id, 1);
						  return Result.createSuccess(task.getId(), task.getFunId(), "1");
					  }else{
						//多出额度
						  this.finishRecord(template, record_id, 3);
						  return Result.createSuccess(task.getId(), task.getFunId(), "0");
					  }
				  }
				  this.finishRecord(template, record_id, 2);
			}
		}
		
		if("PP".equals(plat)){
			game_name=cust.pp_game;
			game_pwd=cust.pp_pwd;
			if(credit.floatValue()>0){
				
				  Long record_id= this.createRecord(template, bill_no, game_name, game_pwd, credit, "IN", plat, product,order_no);
				  
				  try{
					  //boolean flag=MgApi.get(product).Transfer(game_name, bill_no,  "IN", credit);
					  boolean flag=PpApi.get(product).Transfer(game_name, bill_no, credit);
					  if(flag){
						  CreditService creditService =new CreditService(template,ds);
						  if(creditService.subtract(login_name, credit, log_type, remarks, create_user, order_no)){
							  this.finishRecord(template, record_id, 1);
							  return Result.createSuccess(task.getId(), task.getFunId(), "1");
						  }else{
							//多出额度
							  this.finishRecord(template, record_id, 3);
							  return Result.createSuccess(task.getId(), task.getFunId(), "0");
						  }
					  }
					  this.finishRecord(template, record_id, 2);
				  }catch(Exception e){
					  
					  this.finishRecord(template, record_id, 3);
					  return Result.createSuccess(task.getId(), task.getFunId(), "0");
					  
				  }
				  
				  
				  
			}
		}
		
		if("MG".equals(plat)){
			game_name=cust.mg_game;
			game_pwd=cust.mg_pwd;
			if(credit.floatValue()>0){
				
				  Long record_id= this.createRecord(template, bill_no, game_name, game_pwd, credit, "IN", plat, product,order_no);
				  
				  try{
					  boolean flag=MgApi.get(product).Transfer(game_name, bill_no,  "IN", credit);
					  if(flag){
						  CreditService creditService =new CreditService(template,ds);
						  if(creditService.subtract(login_name, credit, log_type, remarks, create_user, order_no)){
							  this.finishRecord(template, record_id, 1);
							  return Result.createSuccess(task.getId(), task.getFunId(), "1");
						  }else{
							//多出额度
							  this.finishRecord(template, record_id, 3);
							  return Result.createSuccess(task.getId(), task.getFunId(), "0");
						  }
					  }
					  this.finishRecord(template, record_id, 2);
				  }catch(Exception e){
					  
					  this.finishRecord(template, record_id, 3);
					  return Result.createSuccess(task.getId(), task.getFunId(), "0");
					  
				  }
				  
				  
				  
			}
		}
		if("PT".equals(plat)){
			game_name=cust.pt_game;
			game_pwd=cust.pt_pwd;
			if(credit.floatValue()>0){
				  Long record_id= this.createRecord(template, bill_no, game_name, game_pwd, credit, "IN", plat, product,order_no);
				  boolean flag=PtApi.get(product).Transfer(game_name, bill_no,  "IN", credit);
				  if(flag){
					  CreditService creditService =new CreditService(template,ds);
					  if(creditService.subtract(login_name, credit, log_type, remarks, create_user, order_no)){
						  this.finishRecord(template, record_id, 1);
						  return Result.createSuccess(task.getId(), task.getFunId(), "1");
					  }else{
						//多出额度
						  this.finishRecord(template, record_id, 3);
						  return Result.createSuccess(task.getId(), task.getFunId(), "0");
					  }
				  }
				  this.finishRecord(template, record_id, 2);
			}
		}
		
		
		if("VS".equals(plat)){
			game_name=cust.sb_game;
			if(credit.floatValue()>0){
				  Long record_id= this.createRecord(template, bill_no, game_name, game_pwd, credit, "IN", "SB", product,order_no);
				  boolean flag=SbApi.get(product).addCredit(game_name,credit, bill_no);
				  if(flag){
					  CreditService creditService =new CreditService(template,ds);
					  if(creditService.subtract(login_name, credit, log_type, remarks, create_user, order_no)){
						  this.finishRecord(template, record_id, 1);
						  return Result.createSuccess(task.getId(), task.getFunId(), "1");
					  }else{
						//多出额度
						  this.finishRecord(template, record_id, 3);
						  return Result.createSuccess(task.getId(), task.getFunId(), "0");
					  }
				  }
				  this.finishRecord(template, record_id, 2);
			}
		}
		
			
		 return Result.createSuccess(task.getId(), task.getFunId(), "0");
	}
	
	
	
	@Service(name="OUT")
	@Params(validateField={"plat","login_name","product"})
	public Result transferOut(Task task,InterFace inter){
		String plat=task.getParam("plat");
		String login_name=task.getParam("login_name");	
		String login_ip=task.getParam("login_ip");	
		
		String product=task.getParam("product");
		//额度变化类型
		String log_type=task.getParam("log_type");
		//额度变化备注
		String remarks=task.getParam("remarks");
		//创建人
		String create_user=task.getParam("create_user");
		//订单号
		String order_no=task.getParam("order_no");
		
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		String ds=inter.getDataSource();
		CustomerService custService = new CustomerService(template,ds);
		if(!custService.locked(login_name)){
			 return Result.createError(task.getId(), task.getFunId(), Errors.NoSupportService_1002, "Account is locded,Please try again!");
			
		}
		Customer cust =custService.getCustomer(login_name);
		
		BigDecimal credit =null;
		String amount =task.getParam("amount");
				
		if(!(("AG".equals(plat)&&cust.ag_flag==1)||
				("BBIN".equals(plat)&&cust.bbin_flag==1)||
				("PT".equals(plat)&&cust.pt_flag==1)||
				("KG".equals(plat)&&cust.kg_flag==1)||
				("VS".equals(plat)&&cust.sb_flag==1)||
				("PP".equals(plat)&&cust.pp_flag==1)||
				("MG".equals(plat)&&cust.mg_flag==1))){
			return Result.createError(task.getId(), task.getFunId(), Errors.NoSupportService_1002, " May be plat or account is not support!");
		}
	
		String bill_no=System.currentTimeMillis()+"";
		String game_name="";
		String game_pwd="";
		if("AG".equals(plat)){
			game_name=cust.ag_game;
			game_pwd=cust.ag_pwd;
			credit=AginApi.get(product).GetBalance(game_name, game_pwd);
			BigDecimal bamount=new BigDecimal(0);
			if(amount!=null){
				bamount=new BigDecimal(new BigDecimal(amount).intValue());
				if(bamount.floatValue()>credit.floatValue()){
					 return Result.createError(task.getId(), task.getFunId(), Errors.CreditNotEnough_2002, "Credit Not Enongh!");
				}
			}else{
				bamount=new BigDecimal(credit.intValue());
			}
			if(bamount.floatValue()>0){
				  Long record_id= this.createRecord(template, bill_no, game_name, game_pwd, bamount, "OUT", plat, product,order_no);
				  boolean flag=AginApi.get(product).Transfer(game_name, bill_no, "OUT", bamount, game_pwd);
				  if(flag){
					  CreditService creditService =new CreditService(template,ds);
					  if(creditService.addforgametransfer(login_name, bamount, log_type, remarks, create_user, order_no)){
						  this.finishRecord(template, record_id, 1);
						  return Result.createSuccess(task.getId(), task.getFunId(), "1");
					  }else{
						//丢失额度
						  this.finishRecord(template, record_id, 4);
						  return Result.createSuccess(task.getId(), task.getFunId(), "0");
					  }
				  }
				  this.finishRecord(template, record_id, 2);
			}
		}
		
		if("PP".equals(plat)){
			game_name=cust.pp_game;
			game_pwd=cust.pp_pwd;
			credit=PpApi.get(product).GetBalance(game_name);
			BigDecimal bamount=new BigDecimal(0);
			if(amount!=null){
				bamount=new BigDecimal(new BigDecimal(amount).intValue());
				if(bamount.floatValue()>credit.floatValue()){
					 return Result.createError(task.getId(), task.getFunId(), Errors.CreditNotEnough_2002, "Credit Not Enongh!");
				}
			}else{
				bamount=new BigDecimal(credit.intValue());
			}
			if(bamount.floatValue()>0){
				  Long record_id= this.createRecord(template, bill_no, game_name, game_pwd, bamount, "OUT", plat, product,order_no);
				  boolean flag=PpApi.get(product).Transfer(game_name, bill_no, new BigDecimal(0).subtract(bamount));
				  if(flag){
					  CreditService creditService =new CreditService(template,ds);
					  if(creditService.addforgametransfer(login_name, bamount, log_type, remarks, create_user, order_no)){
						  this.finishRecord(template, record_id, 1);
						  return Result.createSuccess(task.getId(), task.getFunId(), "1");
					  }else{
						//丢失额度
						  this.finishRecord(template, record_id, 4);
						  return Result.createSuccess(task.getId(), task.getFunId(), "0");
					  }
				  }
				  this.finishRecord(template, record_id, 2);
			}
		}
		
		if("BBIN".equals(plat)){
			game_name=cust.bbin_game;
			game_pwd=cust.bbin_pwd;
			credit=BbinApi.get(product).GetBalance(game_name);
			BigDecimal bamount=new BigDecimal(0);
			if(amount!=null){
				bamount=new BigDecimal(new BigDecimal(amount).intValue());
				if(bamount.floatValue()>credit.floatValue()){
					 return Result.createError(task.getId(), task.getFunId(), Errors.CreditNotEnough_2002, "Credit Not Enongh!");
				}
			}else{
				bamount=new BigDecimal(credit.intValue());
			}
			if(bamount.floatValue()>0){
				  Long record_id= this.createRecord(template, bill_no, game_name, game_pwd, bamount, "OUT", plat, product,order_no);
				  boolean flag=BbinApi.get(product).Transfer(game_name, bill_no, "OUT", bamount);
				  if(flag){
					  CreditService creditService =new CreditService(template,ds);
					  if(creditService.addforgametransfer(login_name, bamount, log_type, remarks, create_user, order_no)){
						  this.finishRecord(template, record_id, 1);
						  return Result.createSuccess(task.getId(), task.getFunId(), "1");
					  }else{
						//丢失额度
						  this.finishRecord(template, record_id, 4);
						  return Result.createSuccess(task.getId(), task.getFunId(), "0");
					  }
				  }
				  this.finishRecord(template, record_id, 2);
			}
		}
		if("KG".equals(plat)){
			game_name=cust.kg_game;
			game_pwd=cust.kg_pwd;
			credit=KgApi.get(product).GetBalance(game_name);
			BigDecimal bamount=new BigDecimal(0);
			if(amount!=null){
				bamount=new BigDecimal(new BigDecimal(amount).intValue());
				if(bamount.floatValue()>credit.floatValue()){
					 return Result.createError(task.getId(), task.getFunId(), Errors.CreditNotEnough_2002, "Credit Not Enongh!");
				}
			}else{
				bamount=new BigDecimal(credit.intValue());
			}
			if(bamount.floatValue()>0){
				  Long record_id= this.createRecord(template, bill_no, game_name, game_pwd, bamount, "OUT", plat, product,order_no);
				  boolean flag=KgApi.get(product).Transfer(game_name, bill_no, "OUT", bamount, login_ip);
				  if(flag){
					  CreditService creditService =new CreditService(template,ds);
					  if(creditService.addforgametransfer(login_name, bamount, log_type, remarks, create_user, order_no)){
						  this.finishRecord(template, record_id, 1);
						  return Result.createSuccess(task.getId(), task.getFunId(), "1");
					  }else{
						//丢失额度
						  this.finishRecord(template, record_id, 4);
						  return Result.createSuccess(task.getId(), task.getFunId(), "0");
					  }
				  }
				  this.finishRecord(template, record_id, 2);
			}
		}
		if("MG".equals(plat)){
			game_name=cust.mg_game;
			game_pwd=cust.mg_pwd;
			credit= MgApi.get(product).GetBalance(game_name);
			BigDecimal bamount=new BigDecimal(0);
			if(amount!=null){
				bamount=new BigDecimal(new BigDecimal(amount).intValue());
				if(bamount.floatValue()>credit.floatValue()){
					 return Result.createError(task.getId(), task.getFunId(), Errors.CreditNotEnough_2002, "Credit Not Enongh!");
				}
			}else{
				bamount=new BigDecimal(credit.intValue());
			}
			if(bamount.floatValue()>0){
				  Long record_id= this.createRecord(template, bill_no, game_name, game_pwd, bamount, "OUT", plat, product,order_no);
				  boolean flag=MgApi.get(product).Transfer(game_name, bill_no, "OUT", bamount);
				  if(flag){
					  CreditService creditService =new CreditService(template,ds);
					  if(creditService.addforgametransfer(login_name, bamount, log_type, remarks, create_user, order_no)){
						  this.finishRecord(template, record_id, 1);
						  return Result.createSuccess(task.getId(), task.getFunId(), "1");
					  }else{
						//丢失额度
						  this.finishRecord(template, record_id, 4);
						  return Result.createSuccess(task.getId(), task.getFunId(), "0");
					  }
				  }
				  this.finishRecord(template, record_id, 2);
			}
		}
		if("PT".equals(plat)){
			game_name=cust.pt_game;
			game_pwd=cust.pt_pwd;
			credit=PtApi.get(product).GetBalance(game_name);
			BigDecimal bamount=new BigDecimal(0);
			if(amount!=null){
				bamount=new BigDecimal(new BigDecimal(amount).intValue());
				if(bamount.floatValue()>credit.floatValue()){
					 return Result.createError(task.getId(), task.getFunId(), Errors.CreditNotEnough_2002, "Credit Not Enongh!");
				}
			}else{
				bamount=new BigDecimal(credit.intValue());
			}
			if(bamount.floatValue()>0){
				  Long record_id= this.createRecord(template, bill_no, game_name, game_pwd, bamount, "OUT", plat, product,order_no);
				  boolean flag=PtApi.get(product).Transfer(game_name, bill_no, "OUT", bamount);
				  if(flag){
					  CreditService creditService =new CreditService(template,ds);
					  if(creditService.addforgametransfer(login_name, bamount, log_type, remarks, create_user, order_no)){
						  this.finishRecord(template, record_id, 1);
						  return Result.createSuccess(task.getId(), task.getFunId(), "1");
					  }else{
						//丢失额度
						  this.finishRecord(template, record_id, 4);
						  return Result.createSuccess(task.getId(), task.getFunId(), "0");
					  }
				  }
				  this.finishRecord(template, record_id, 2);
			}
		}
		
		if("VS".equals(plat)){
			game_name=cust.sb_game;
			credit= SbApi.get(product).balance(cust.sb_game);
			BigDecimal bamount=new BigDecimal(0);
			if(amount!=null){
				bamount=new BigDecimal(new BigDecimal(amount).intValue());
				if(bamount.floatValue()>credit.floatValue()){
					 return Result.createError(task.getId(), task.getFunId(), Errors.CreditNotEnough_2002, "Credit Not Enongh!");
				}
			}else{
				bamount=new BigDecimal(credit.intValue());
			}
			if(bamount.floatValue()>0){
				  Long record_id= this.createRecord(template, bill_no, game_name, game_pwd, bamount, "OUT", "SB", product,order_no);
				  boolean flag=SbApi.get(product).reduceCredit(game_name, bamount, bill_no);
				  if(flag){
					  CreditService creditService =new CreditService(template,ds);
					  if(creditService.addforgametransfer(login_name, bamount, log_type, remarks, create_user, order_no)){
						  this.finishRecord(template, record_id, 1);
						  return Result.createSuccess(task.getId(), task.getFunId(), "1");
					  }else{
						//丢失额度
						  this.finishRecord(template, record_id, 4);
						  return Result.createSuccess(task.getId(), task.getFunId(), "0");
					  }
				  }
				  this.finishRecord(template, record_id, 2);
			}
		}
		
		
		return Result.createSuccess(task.getId(), task.getFunId(), "0");
	}
	
	
	/**
	 * 查询额度
	 * @param task
	 * @param inter
	 * @return
	 */
	@Service(name="balance")
	@Params(validateField={"plat","login_name","product","login_ip"})
	public Result balance(Task task,InterFace inter){
		String plat=task.getParam("plat");
		String login_name=task.getParam("login_name");		
		String product=task.getParam("product");	
		String login_ip=task.getParam("login_ip");
		
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		String ds=inter.getDataSource();
		CustomerService custService = new CustomerService(template,ds);
		Customer cust =custService.getCustomer(login_name);
		
		BigDecimal balance =new BigDecimal(0);
		if("AG".equals(plat)){
			 if(cust.ag_flag==0){
				 boolean f= AginApi.get(product).CheckOrCreateGameAccount(cust.ag_game,cust.ag_pwd);
				 if(f){
					 custService.updateGameFlag(plat, login_name, 1);
				 }
			 }else{
			     balance= AginApi.get(product).GetBalance(cust.ag_game, cust.ag_pwd);
			 }
		}
		if("BBIN".equals(plat)){
			if(cust.bbinmobile_flag==0){
				 boolean f= BbinApi.get(product).CheckOrCreateMobileAccount(cust.bbin_game,cust.bbinmobile_pwd);
				 if(f){
					 custService.updateBBinMobileGameFlag( login_name, 1);
				 }
			}
			
			 if(cust.bbin_flag==0){
				 boolean f= BbinApi.get(product).CheckOrCreateGameAccount(cust.ag_game,cust.ag_pwd);
				 if(f){
					 custService.updateGameFlag(plat, login_name, 1);
				 }
			 }else{
				 balance= BbinApi.get(product).GetBalance(cust.bbin_game);
			 }
			
		}
		if("PT".equals(plat)){
			 if(cust.pt_flag==0){
				 //boolean f= PtApi.get(product).CheckOrCreateGameAccount(cust.ag_game,cust.ag_pwd);
				 boolean f= PtApi.get(product).CheckOrCreateGameAccount(cust.pt_game,cust.pt_pwd);
				 if(f){
					 custService.updateGameFlag(plat, login_name, 1);
				 }
			 }else{
			     balance= PtApi.get(product).GetBalance(cust.pt_game);
			 }
		}
		if("KG".equals(plat)){
			if(cust.kg_flag==0){
				String url=KgApi.get(product).CreateAccountAndLogin(cust.kg_game, login_ip);
				if(url!=null&&url.startsWith("/client")){
					 custService.updateGameFlag(plat, login_name, 1);
				}
			 }else{
				 balance= KgApi.get(product).GetBalance(cust.kg_game);
			 }
			 
		}
		if("MG".equals(plat)){
			if(cust.mg_flag==0){
				 boolean f= MgApi.get(product).addAccount(cust.mg_game, cust.mg_pwd, login_ip);
				 if(f){
					 custService.updateGameFlag(plat, login_name, 1);
				 }
			 }else{
				 balance= MgApi.get(product).GetBalance(cust.mg_game);
			 }
			 
		}
		if("PP".equals(plat)){
			if(cust.pp_flag==0){
				 boolean f= PpApi.get(product).CheckOrCreateGameAccount(cust.pp_game);
				 if(f){
					 custService.updateGameFlag(plat, login_name, 1);
				 }
			 }else{
				 balance= PpApi.get(product).GetBalance(cust.pp_game);
			 }
			 
		}
		if("VS".equals(plat)){
			if(cust.sb_flag==0){
				 String f= SbApi.get(product).authorize(login_ip, cust.sb_game);
				 if(f !=null && !f.equals("")){
					 custService.updateGameFlag(plat, login_name, 1);
				 }
			 }else{
				 balance= SbApi.get(product).balance(cust.sb_game);
			 }
			 
		}
		
		Result result =Result.create(task.getId(), task.getFunId());
		result.setFlag("-1");
		result.setIsList(false);
		result.setRes(balance.toString());
		return result;
	}
	
	
	/**
	 * 创建账号
	 * @param task
	 * @param inter
	 * @return
	 */
	@Service(name="create")
	@Params(validateField={"plat","login_name","product","login_ip"})
	public Result create(Task task,InterFace inter){
		String plat=task.getParam("plat");
		String login_name=task.getParam("login_name");		
		String product=task.getParam("product");	
		String login_ip=task.getParam("login_ip");
		
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		String ds=inter.getDataSource();
		CustomerService custService = new CustomerService(template,ds);
		Customer cust =custService.getCustomer(login_name);
		
		boolean flag =false;
		if("AG".equals(plat)){
			flag= AginApi.get(product).CheckOrCreateGameAccount(cust.ag_game,cust.ag_pwd);
		}
		if("BBIN".equals(plat)){
			flag= BbinApi.get(product).CheckOrCreateGameAccount(cust.bbin_game,cust.bbin_pwd);
			flag= BbinApi.get(product).CheckOrCreateMobileAccount(cust.bbin_game,cust.bbinmobile_pwd);
		}
		if("PT".equals(plat)){
			flag= PtApi.get(product).CheckOrCreateGameAccount(cust.pt_game, cust.pt_pwd);
		}
		if("KG".equals(plat)){
			String url=KgApi.get(product).CreateAccountAndLogin(cust.kg_game, login_ip);
			if(url!=null&&url.startsWith("/client")){
				flag=true;
			}
		}
		if("MG".equals(plat)){
			flag= MgApi.get(product).addAccount(cust.mg_game, cust.mg_pwd, login_ip);
		}
		if("PP".equals(plat)){
			flag= PpApi.get(product).CheckOrCreateGameAccount(cust.pp_game);
		}
		
		Result result =Result.create(task.getId(), task.getFunId());
		result.setFlag("-1");
		result.setIsList(false);
		result.setRes(""+flag);
		return result;
	}
	
	
	
	/**
	 * 跳转
	 * @param task
	 * @param inter
	 * @return
	 */
	@Service(name="forward")
	@Params(validateField={"plat","login_name","login_ip","product"})
	public Result forward(Task task,InterFace inter){
		String plat=task.getParam("plat");
		String login_name=task.getParam("login_name");
		String login_ip =task.getParam("login_ip");	
		String site =task.getParam("site");	
		String product=task.getParam("product");
		String vistor=task.getParam("vistor");
		String gamecode=task.getParam("gamecode");
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		String ds=inter.getDataSource();
		
		Customer cust = null;
		
		//1表示试玩,数据库里没有试玩用户的记录
		if("0".equals(vistor)){
			CustomerService custService = new CustomerService(template,ds);
			cust =custService.getCustomer(login_name);
		}
		
		String url ="";
		if("AG".equals(plat)){
			if("1".equals(vistor)){
				 url= AginApi.get(product).ForwardGame(login_name, "a123a123",gamecode);
			}else{
				
			     url= AginApi.get(product).ForwardGame(cust.ag_game, cust.ag_pwd,gamecode);
			}
		}
		if("BBIN".equals(plat)){
			
			if("1".equals(vistor)){
				if(gamecode!=null && !"".equals(gamecode)){
					url= BbinApi.get(product).login2Forward(login_name, "a123a123", gamecode);
				}else{
					
					url= BbinApi.get(product).ForwardGame(login_name, "a123a123", site);
				}
			}else{
				if(gamecode!=null && !"".equals(gamecode)){
					url= BbinApi.get(product).login2Forward(cust.bbin_game, cust.bbin_pwd, gamecode);
				}else{
					url= BbinApi.get(product).ForwardGame(cust.bbin_game, cust.bbin_pwd, site);
				}
				
			}
			
		}
		
		if("PP".equals(plat)){
			
			if("1".equals(vistor)){
				url= PpApi.get(product).ForwardGame(login_name,gamecode);
				
			}else{
				
				url= PpApi.get(product).ForwardGame(cust.pp_game,gamecode);
				
			}
			
		}

		if("VS".equals(plat)){
			
			if("1".equals(vistor)){
				
				if(site.contains("http")){
					//site = site;
				}else{
					site = "http://"+site+"";
				}
				 
				 url= SbApi.get(product).forward(login_ip,login_name,site); //试用,无登录
				 
			}else{
				if(site.contains("http")){
					site = site+"/tranfer.php";
				}else{
					site = "http://"+site+"/tranfer.php";
				}
				
				if(gamecode == null || gamecode.equals("")){
					url= SbApi.get(product).forward(login_ip,cust.sb_game,site);
				}else{
					String gpcode = "TGP";
					if(gamecode.equals("Fishing_King")){
						gpcode = "GDF";
					}
					
					url= SbApi.get(product).forwardwithgamecode(login_ip,cust.sb_game,site,gamecode,gpcode,"0");
				}
				
			}
			
		}

		if("PT".equals(plat)){
			//无实现方法
		}
		if("KG".equals(plat)){
			
			
			if("1".equals(vistor)){
				 url=KgApi.get(product).CreateAccountAndLogin(login_name, login_ip);
			}else{
				 url=KgApi.get(product).CreateAccountAndLogin(cust.kg_game, login_ip);
			}
			
		}
		if("MG".equals(plat)){
			//无实现方法
		}
		Result result =Result.create(task.getId(), task.getFunId());
		result.setFlag("-1");
		result.setIsList(false);
		result.setRes(url);
		return result;
	}
	
	
	@Service(name="logout")
	@Params(validateField={"plat","login_name","login_ip"})
	public Result logout(Task task,InterFace inter){
		String plat=task.getParam("plat");
		String login_name=task.getParam("login_name");
		String login_ip =task.getParam("login_ip");	
		String product=task.getParam("product");
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		String ds=inter.getDataSource();
		CustomerService custService = new CustomerService(template,ds);
		Customer cust =custService.getCustomer(login_name);
		
		boolean flag =false;
		if("AG".equals(plat)){
			//无实现
		}
		if("BBIN".equals(plat)){
			//无实现
		}
		if("PT".equals(plat)){
			flag=PtApi.get(product).Logout(cust.pt_game);
		}
		if("KG".equals(plat)){
			//无实现
		}
		if("MG".equals(plat)){
			//无实现
		}
		return Result.createSuccess(task.getId(), task.getFunId(),(flag?"1":"0"));
	}
	
	@Service(name="pwd")
	@Params(validateField={"plat","login_name","password","login_ip"})
	public Result pwd(Task task,InterFace inter){
		String plat=task.getParam("plat");
		String login_name=task.getParam("login_name");
		String login_ip =task.getParam("login_ip");	
		String product=task.getParam("product");
		String password=task.getParam("password");
		boolean flag =false;
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		String ds=inter.getDataSource();
		
		if("AG".equals(plat)){
			//无实现
		}
		if("PP".equals(plat)){
			//无实现
		}
		if("BBIN".equals(plat)){
			CustomerService custService = new CustomerService(template,ds);
			Customer cust =custService.getCustomer(login_name);
			flag = BbinApi.get(product).UpdateMobileAccountPassword(cust.bbin_game, password);
		}
		if("PT".equals(plat)){
			CustomerService custService = new CustomerService(template,ds);
			Customer cust =custService.getCustomer(login_name);
			flag=PtApi.get(product).UpdatePwd(cust.pt_game, password);
			//need update db
		}
		if("KG".equals(plat)){
			//无实现
		}
		if("MG".equals(plat)){
			CustomerService custService = new CustomerService(template,ds);
			Customer cust =custService.getCustomer(login_name);
			flag=MgApi.get(product).updatePassword(cust.mg_game, password);
			
			//need update db
		}
		return Result.createSuccess(task.getId(), task.getFunId(),(flag?"1":"0"));
		
	}
	
	
	@Service(name="registerAlias")
	@Params(validateField={"plat","login_name","alias"})
	public Result registerAlias(Task task,InterFace inter){
		String plat=task.getParam("plat");
		String login_name=task.getParam("login_name");
			
		String product=task.getParam("product");
		String alias=task.getParam("alias");
		boolean flag =false;
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		String ds=inter.getDataSource();
		
		if("MG".equals(plat)){
			CustomerService custService = new CustomerService(template,ds);
			Customer cust =custService.getCustomer(login_name);
			flag=MgApi.get(product).registerAlias(cust.mg_game, alias);
			
			if(flag){
				 custService.updateMgAlias(plat, login_name, alias);
			 }
		}
		return Result.createSuccess(task.getId(), task.getFunId(),(flag?"1":"0"));
		
	}

	
}
