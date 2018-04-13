package com.product.bda.handler;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.product.bda.service.CreditService;
import com.product.bda.service.Customer;
import com.product.bda.service.CustomerService;
import com.product.bda.service.DepositService;
import com.product.bda.service.LetterService;
import com.product.bda.service.MsgService;
import com.product.bda.service.OrderNoService;
import com.product.bda.service.WithdrawService;

import bsz.exch.bean.Errors;
import bsz.exch.bean.FobbinInfo;
import bsz.exch.bean.Result;
import bsz.exch.bean.Task;
import bsz.exch.core.Before;
import bsz.exch.core.Handler;
import bsz.exch.core.InterFace;
import bsz.exch.core.JdbcResource;
import bsz.exch.core.Params;
import bsz.exch.core.Service;
import bsz.exch.core.Tx;
import bsz.exch.utils.LogHelper;


@Handler(name="CUST")
public class CustHandler {
	
	private static Logger logger=Logger.getLogger(CustHandler.class);

	@Before
	public FobbinInfo before(Task task, InterFace inter){
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		if(template==null){
			return new FobbinInfo(true,"the datasource is null!");
		}
		return new FobbinInfo(false,"");
	}
	
	/**
	 * 玩家登录
	 * @param task
	 * @param inter
	 * @return
	 */
	@Service(name="login")
	@Params(validateField={"login_name","login_pwd","login_ip","user_agent"})
	@Tx
	public Result login(Task task,InterFace inter){
		
		String login_name=task.getParam("login_name");
		String login_pwd=task.getParam("login_pwd");
		String login_ip=task.getParam("login_ip");
		String user_agent=task.getParam("user_agent");
		String ds=inter.getDataSource();
		
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
		String sql="select count(1) from mb_customer where login_name =? and login_pwd=? and flag=1";
		List<String> list =new ArrayList<String>();
		list.add(login_name);
		list.add(login_pwd);
		logger.info(LogHelper.dbLog(inter.getDataSource(),sql, list));
		Integer count=template.queryForObject(sql,list.toArray(new String[0]),Integer.class);
		
		if(count>0){
			sql="update mb_customer set login_ip=?,login_date=now(),login_times=login_times+1 where login_name=? ";
			list =new ArrayList<String>();
			list.add(login_ip);
			list.add(login_name);
			logger.info(LogHelper.dbLog(ds,sql, list));
			int flag=template.update(sql,list.toArray(new String[0]));
			
		    sql="insert into mb_cust_log (log_type,log_msg,ip,user_agent,create_date,cust_id,login_name) select 2,'客户登录',?,?,now(),cust_id,login_name from mb_customer where login_name =?";
			list =new ArrayList<String>();
			list.add(login_ip);
			list.add(user_agent);
			list.add(login_name);
			logger.info(LogHelper.dbLog(ds,sql, list));
			template.update(sql,list.toArray(new String[0]));
			
		}
		Result result =Result.create(task.getId(), task.getFunId());
		result.setFlag("-1");
		result.setIsList(false);
		result.setRes(""+(count>0));
		return result;
	}
	
	@Service(name="reg")
	@Params(validateField={"login_name","login_pwd","real_name","phone","reg_ip","create_user","notify_title","notify_msg","game_pre"})
	@Tx
	public Result reg(Task task,InterFace inter){
		String login_name=task.getParam("login_name");
		String login_pwd=task.getParam("login_pwd");
		String real_name=task.getParam("real_name");
		String phone=task.getParam("phone");
		String qq=task.getParam("qq");
		String reg_ip=task.getParam("reg_ip");
		String create_user=task.getParam("create_user");
		String notify_msg=task.getParam("notify_msg");
		String notify_title=task.getParam("notify_title");
		String game_pre=task.getParam("game_pre");
		
		String email=task.getParam("email");
		String reg_code=task.getParam("reg_code");
		String reg_source=task.getParam("reg_source");
		String user_agent =task.getParam("user_agent");
		
		String reg_domain =task.getParam("reg_domain");
		String refer_url =task.getParam("refer_url");
		String search_key =task.getParam("search_key");
		
		String ds=inter.getDataSource();
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
		CustomerService custService  =new CustomerService(template,ds);
		LetterService letterService  =new LetterService(template,ds);
		
		Long parent_id =null;
		if(reg_code!=null){
			parent_id=custService.getAgentByRegCode(reg_code);
		}
		Long cust_id=custService.createCustomer(login_name, login_pwd, real_name, phone, email, qq, reg_ip,
				create_user, game_pre+login_name, "b123b123", game_pre+login_name, "b123b123", game_pre+login_name,  "b123b123", parent_id,
				reg_source, (game_pre+login_name).toUpperCase(), "c123c123", game_pre+login_name,
				 "b123b123", game_pre+login_name, "c123c123",reg_domain,refer_url,search_key);
		if(cust_id==null){
			return Result.createError(task.getId(), task.getFunId(),Errors.CanNotCreateCustomer_4001,"Can Not Create Customer!");
		}
		if(login_name.equals(create_user)){
			custService.loginRecord(reg_ip, login_name);
			custService.createCustLog(reg_ip,user_agent,login_name,1,"客户注册并登录");
		}
		letterService.create(cust_id, login_name, notify_title, notify_msg, create_user, true);
		Result result =Result.create(task.getId(), task.getFunId());
		result.setFlag("-1");
		result.setIsList(false);
		result.setRes(cust_id+"");
		return result;
	}
	
	
	@Service(name="withdraw")
	@Params(validateField={"login_name","login_ip","amount"})
	@Tx
	public Result withdraw(Task task,InterFace inter){
		String login_name=task.getParam("login_name");
		String login_ip=task.getParam("login_ip");
		BigDecimal amount=new BigDecimal(task.getParam("amount"));
		String bank_name=task.getParam("bank_name");
		String account=task.getParam("account");
		String account_type=task.getParam("account_type");
		String bank_dot=task.getParam("bank_dot");
		String remarks=task.getParam("remarks");
		String create_user=task.getParam("create_user");

		String ds=inter.getDataSource();
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
		WithdrawService service  =new WithdrawService(template,ds);
		CustomerService custService  =new CustomerService(template,ds);
		
		//检查玩家是否有未处理提款
		if(service.queryUndoWithdrawCount(login_name)>0){
			return Result.createError(task.getId(), task.getFunId(),Errors.ExistWithdraw_2001,"Exist Untreated Withdraw!");
		}
		//检查是否余额足够
		if(!custService.checkCredit(login_name, amount)){
			return Result.createError(task.getId(), task.getFunId(),Errors.CreditNotEnough_2002,"Credit Not Enough!");
		}
		//产生订单号.创建提案提案
		String order_no=OrderNoService.createLocalNo("WI");
		Long withdraw_id=service.createWithdraw(order_no,login_name, amount, bank_name, account, account_type, bank_dot, remarks, create_user);
		if(withdraw_id==null){
			return Result.createError(task.getId(), task.getFunId(),Errors.CanNotCreateWithdraw_2003,"Can Not Create Withdraw!");
		}
		//扣除提款金额
		CreditService creditService =new CreditService(template,ds);
		if(!creditService.subtractforwithdraw(login_name, amount,"提款", remarks, create_user, order_no)){
			//扣款不成功,删除提案
			service.deleteWithdraw(withdraw_id);
			return Result.createError(task.getId(), task.getFunId(),Errors.CreditChange_3001,"Credit Change Not Success!");
		}
		//提醒
		new MsgService(template,ds).createMsg(5, "客户"+login_name+"提款"+amount+"元，请及时处理!");
		
		Result result =Result.create(task.getId(), task.getFunId());
		result.setFlag("-1");
		result.setIsList(false);
		result.setRes(withdraw_id+"");
		return result;
	}
	
	
	@Service(name="deposit")
	@Params(validateField={"login_name","login_ip","amount"})
	@Tx
	public Result deposit(Task task,InterFace inter){
		String login_name=task.getParam("login_name");
		String login_ip=task.getParam("login_ip");
		BigDecimal amount=new BigDecimal(task.getParam("amount"));
		String bank_name=task.getParam("bank_name");
		String account_name=task.getParam("account_name");
		String deposit_type=task.getParam("deposit_type");
		String deposit_date=task.getParam("deposit_date");
		String bank_dot=task.getParam("bank_dot");
		String remarks=task.getParam("remarks");
		String create_user=task.getParam("create_user");

		String ds=inter.getDataSource();
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
		DepositService service  =new DepositService(template,ds);
		CustomerService custService  =new CustomerService(template,ds);
		
//		
//		Deposit deposit =new Deposit();
//		 deposit.deposit_date=new Date(DateUtil.stringToDate(deposit_date, "yyyy-MM-dd HH:mm:ss").getTime()); 
//		 deposit.cust_id=cust.cust_id;
//		 deposit.pdage_status=1;
//		 deposit.status=1;
//		 deposit.create_user=cust.login_name;
//		 deposit.create_date=new Date(System.currentTimeMillis());
//		 deposit.ip=IPApp.getIpAddr();
//		 deposit.amount=amount;
//		 deposit.poundage=poundage;
//		 deposit.bank_name=bank_name;
//		 deposit.account_name=account_name;
//		 deposit.deposit_type=deposit_type;
//		 deposit.location=location;
//		 deposit.remarks=remarks;
//		 deposit.login_name=cust.login_name;
//		 deposit.real_name=cust.real_name;
//		 deposit.dep_no=OrderNo.createLocalNo("DE");
//		 Long dep_id=deposit.NTcreat();
//		 if(dep_id!=null){
//			 DepositLog log =new DepositLog();
//			 log.after_status=1;
//			 log.create_user=cust.login_name;
//			 log.deposit_id=dep_id;
//			 log.pre_status=0;
//			 log.remarks=deposit.remarks;
//			 log.NTcreat();
//			  Msg.NTcreateMsg(4, "客户"+cust.real_name+"存款"+deposit.amount+"元，请及时处理!");
//		 }
//		 
		 
		//检查玩家是否有未处理提款
		if(service.queryUndoDepositCount(login_name)>0){
			return Result.createError(task.getId(), task.getFunId(),Errors.ExistDeposit_2004,"Exist Untreated Deposit!");
		}
		
		Customer cust = custService.getCustomer(login_name);
		//产生订单号.创建提案提案
		String order_no=OrderNoService.createLocalNo("DE");
		Long deposit_id=service.addDepositWithPhone(order_no,cust.cust_id,login_name,cust.real_name, amount, bank_name, account_name, deposit_type, bank_dot, remarks,deposit_date);
		if(deposit_id==null){
			return Result.createError(task.getId(), task.getFunId(),Errors.CanNotCreateDeposit_2005,"Can Not Create Deposit!");
		}
		
		//提醒
		new MsgService(template,ds).createMsg(4, "客户"+login_name+"存款款"+amount+"元，请及时处理!");
		
		Result result =Result.create(task.getId(), task.getFunId());
		result.setFlag("-1");
		result.setIsList(false);
		result.setRes(deposit_id+"");
		return result;
	}
	
	
	
	
	
	
	

}
