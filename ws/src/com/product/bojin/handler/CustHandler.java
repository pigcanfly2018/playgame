package com.product.bojin.handler;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.product.bojin.service.CustomerService;

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


@Handler(name="bojin_CUST")
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
	 * 玩家登陆
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
		CustomerService customerService =new CustomerService(template,ds);
		boolean flag=customerService.login(login_name, login_pwd);
		if(flag){
			customerService.recordLoginInfo(login_name, login_ip, user_agent);
		}
		
		Result result =Result.create(task.getId(), task.getFunId());
		result.setFlag("-1");
		result.setIsList(false);
		result.setRes(""+(flag));
		return result;
	}
	
	
	
	
	
	
	
	
	

}
