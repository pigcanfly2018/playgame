package com.product.bda.handler;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.product.bda.service.Customer;
import com.product.bda.service.CustomerService;

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
import bsz.exch.utils.Token;

@Handler(name="TOKEN")
public class TokenHandler {
	
	private static Logger logger=Logger.getLogger(TokenHandler.class);

	@Before
	public FobbinInfo before(Task task, InterFace inter){
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		if(template==null){
			return new FobbinInfo(true,"the datasource is null!");
		}
		return new FobbinInfo(false,"");
	}
	
	
//	@Service(name="login")
//	@Params(validateField={"token","login_name","login_ip","user_agent"})
//	@Tx
//	public Result login(Task task,InterFace inter){
//		
//		String login_name=task.getParam("login_name");
//		String token=task.getParam("token");
//		String login_ip=task.getParam("login_ip");
//		String user_agent=task.getParam("user_agent");
//		String ds=inter.getDataSource();
//		
//		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
//		CustomerService customerService =new CustomerService(template,ds);
//		Customer customer =customerService.getCustomerByLoginName(login_name);
//		//用户不存在
//		if(customer==null){
//			Result result =Result.create(task.getId(), task.getFunId());
//			result.setFlag("-1");
//			result.setIsList(false);
//			result.setRes("2");
//			return result;
//		}
//		//用户被禁用
//		if(!customer.flag){
//			Result result =Result.create(task.getId(), task.getFunId());
//			result.setFlag("-1");
//			result.setIsList(false);
//			result.setRes("3");
//			return result;
//		}
//
//		boolean flag = Token.validate(login_name, token);
//		//token失效
//		if(!flag){
//			Result result =Result.create(task.getId(), task.getFunId());
//			result.setFlag("-1");
//			result.setIsList(false);
//			result.setRes("4");
//			return result;
//		}
//		//登陆正常
//		customerService.recordLoginInfo(customer.login_name, login_ip, user_agent);
//		Result result =Result.create(task.getId(), task.getFunId());
//		result.setFlag("-1");
//		result.setIsList(false);
//		result.setRes("1");
//		return result;
//	}
//	
	
	@Service(name="validate")
	@Params(validateField={"token","login_name"})
	@Tx
	public Result validate(Task task,InterFace inter){
		String login_name=task.getParam("login_name");
		String token=task.getParam("token");
		String ds=inter.getDataSource();
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
		CustomerService customerService =new CustomerService(template,ds);
		Customer customer =customerService.getCustomer(login_name);
		boolean flag = Token.validate(login_name, token);
		if(customer==null)flag=false;
		//token失效
		if(!flag){
			Result result =Result.create(task.getId(), task.getFunId());
			result.setFlag("-1");
			result.setIsList(false);
			result.setRes("4");
			return result;
		}
		Result result =Result.create(task.getId(), task.getFunId());
		result.setFlag("-1");
		result.setIsList(false);
		result.setRes("1");
		return result;
	}
	
	@Service(name="get")
	@Params(validateField={"login_name"})
	@Tx
	public Result get(Task task,InterFace inter){
		String login_name=task.getParam("login_name");
		String token = Token.get(login_name);
		if(token!=null){
			Result result =Result.create(task.getId(), task.getFunId());
			result.setFlag("-1");
			result.setIsList(false);
			result.setRes(token);
			return result;
		}
		Result result =Result.create(task.getId(), task.getFunId());
		result.setFlag("-2");
		result.setIsList(false);
		result.setRes("");
		return result;
	}
	
	
	

}
