package com.product.bda.handler;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;





import com.product.bda.service.Customer;
import com.product.bda.service.CustomerService;
import com.product.bda.service.LetterService;

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
import bsz.exch.utils.MD5Util;
import bsz.exch.utils.Token;



@Handler(name="CUST2")
public class Cust2Handler {
	
	private static Logger logger=Logger.getLogger(Cust2Handler.class);
	@Before
	public FobbinInfo before(Task task, InterFace inter){
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		if(template==null){
			return new FobbinInfo(true,"the datasource is null!");
		}
		return new FobbinInfo(false,"");
	}
	
	
	/**
	 * 手机注册的玩家登陆
	 * @param task
	 * @param inter
	 * @return
	 */
	@Service(name="login")
	@Params(validateField={"phone","login_pwd","login_ip","user_agent"})
	@Tx
	public Result login(Task task,InterFace inter){
		
		//login_name is phone
		String phone=task.getParam("phone");
		String login_pwd=task.getParam("login_pwd");
		String login_ip=task.getParam("login_ip");
		String user_agent=task.getParam("user_agent");
		String ds=inter.getDataSource();
		
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
		CustomerService customerService =new CustomerService(template,ds);
		Customer customer=customerService.getCustomerByRegPhone(phone);
		//用户不存在
		if(customer==null){
			Result result =Result.create(task.getId(), task.getFunId());
			result.setFlag("-1");
			result.setIsList(false);
			result.setRes("2");
			return result;
		}
		//用户被禁用
		if(customer.flag == 0){
			Result result =Result.create(task.getId(), task.getFunId());
			result.setFlag("-1");
			result.setIsList(false);
			result.setRes("3");
			return result;
		}

		//密码错误
		boolean flag=customerService.login(customer.login_name, login_pwd);
		if(!flag){
			Result result =Result.create(task.getId(), task.getFunId());
			result.setFlag("-1");
			result.setIsList(false);
			result.setRes("4");
			return result;
		}
		//登陆正常
		customerService.recordLoginInfo(customer.login_name, login_ip, user_agent);
		Result result =Result.create(task.getId(), task.getFunId());
		result.setFlag("-1");
		result.setIsList(false);
		result.setRes("1");
		return result;
	
	}
	
	//通过token登陆 - 从第三方网站跳转过来
	@Service(name="token")
	@Params(validateField={"login_name","token","login_ip","user_agent"})
	@Tx
	public Result token(Task task,InterFace inter){

		String login_name=task.getParam("login_name");
		String token=task.getParam("token");
		String login_ip=task.getParam("login_ip");
		String user_agent=task.getParam("user_agent");
		String ds=inter.getDataSource();
		
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
		CustomerService customerService =new CustomerService(template,ds);
		Customer customer=customerService.getCustomer(login_name);
		//用户不存在
		if(customer==null){
			Result result =Result.create(task.getId(), task.getFunId());
			result.setFlag("-1");
			result.setIsList(false);
			result.setRes("2");
			return result;
		}
		//用户被禁用
		if(customer.flag == 0){
			Result result =Result.create(task.getId(), task.getFunId());
			result.setFlag("-1");
			result.setIsList(false);
			result.setRes("3");
			return result;
		}

		boolean flag = Token.validate(login_name, token);
		//token失效
		if(!flag){
			Result result =Result.create(task.getId(), task.getFunId());
			result.setFlag("-1");
			result.setIsList(false);
			result.setRes("4");
			return result;
		}
		//登陆正常
		customerService.recordLoginInfo(customer.login_name, login_ip, user_agent);
		Result result =Result.create(task.getId(), task.getFunId());
		result.setFlag("-1");
		result.setIsList(false);
		result.setRes("1");
		return result;
	
	}
	
	
	/**
	 * 手机玩家注册
	 * @param task
	 * @param inter
	 * @return
	 */
	@Service(name="reg")
	@Params(validateField={"phone","real_name","reg_ip"})
	@Tx
	public Result reg(Task task,InterFace inter){
		//String login_name=task.getParam("login_name");
		//String login_pwd=task.getParam("login_pwd");
		String phone=task.getParam("phone");
		String real_name=task.getParam("real_name");
		String qq =task.getParam("qq");
		String reg_ip=task.getParam("reg_ip");
		String reg_code=task.getParam("reg_code");

		String reg_domain=task.getParam("reg_domain");
		String referdomain=task.getParam("referdomain");
		String keyword=task.getParam("keyword");
		
		String ds=inter.getDataSource();
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
		CustomerService customerService =new CustomerService(template,ds);
		LetterService letterService = new LetterService(template,ds);
		//查询手机号码是否注册过
		if(phone.length()<8){
			 Result r =Result.create(task.getId(), task.getFunId());
			 r.addFields(new String[]{"ok","login_name","login_pwd","token","message"});
			 r.setFlag("-1");
			 r.setIsList(true);
			 r.setLength(1);
			 r.addRecord(new String[]{"2","","","","The Phone unvalidat"});
			return r;

		}
		int c=customerService.queryCntByPhone(phone);
		if(c>0){
			 Result r =Result.create(task.getId(), task.getFunId());
			 r.addFields(new String[]{"ok","login_name","login_pwd","token","message"});
			 r.setFlag("-1");
			 r.setIsList(true);
			 r.setLength(1);
			 r.addRecord(new String[]{"3","","","","The Phone already registered"});
			return r;
		}
		//查询当前IP当天注册个数
		int c1=customerService.queryCntByRegIp(reg_ip);
		if(c1>3  && !"122.49.213.98".equals(reg_ip)){
			 Result r =Result.create(task.getId(), task.getFunId());
			 r.addFields(new String[]{"ok","login_name","login_pwd","token","message"});
			 r.setFlag("-1");
			 r.setIsList(true);
			 r.setLength(1);
			 r.addRecord(new String[]{"4","","","","too much try register"});
			return r;
		}
		
		//寻找合适的用户名
		String login_name=phone.substring(phone.length()-8,phone.length());
		Customer cust =customerService.getCustomer(login_name);
		if(cust!=null){
			String pp="abcedfghijklmnopqrstuvwxyz";
			for(int i=0;i<pp.length();i++){
				login_name=pp.charAt(i)+phone.substring(phone.length()-7,phone.length());
				cust =customerService.getCustomer(login_name);
				if(cust==null) break;
			}	
			//无法生成用户名
			if(cust!=null){
				 Result r =Result.create(task.getId(), task.getFunId());
				 r.addFields(new String[]{"ok","login_name","login_pwd","token","message"});
				 r.setFlag("-1");
				 r.setIsList(true);
				 r.setLength(1);
				 r.addRecord(new String[]{"4","","","","Can't select User name"});
				return r;
			}
		}
		//密码
		String login_pwd=phone.substring(phone.length()-6,phone.length());
		String login_pwd_md5=MD5Util.MD5FromMeibo(login_pwd);
		
		//开始注册
		boolean flag=customerService.reg(login_name, login_pwd_md5, real_name, phone, qq,reg_ip, login_name, reg_code,reg_domain,referdomain,keyword);
		if(flag){
			cust =customerService.getCustomer(login_name);
			customerService.updatePhoneRegFlag(login_name);
			//发送站内信
		    StringBuffer sb=new StringBuffer("<p>尊敬的"+real_name+"，您好！欢迎您加入八达国际娱乐城，八达国际致力为您提供优质的线上博彩服务，同时为了您的资金安全，我们温馨提示您：</p>");                                                                        
	        sb.append("<p>1、请您留意您的注册信息如姓名，手机号码，QQ号码是否真实，我们在您的第一笔提款的时候，会致电对您的注册信息进行确认，也请您保持电话通畅，如果您已绑定错误信息，可以随时联系我们客服申请修正，您也可以采用真实信息重新注册一个帐号。</p>");
	        sb.append("<p>2、请您再次确认您绑定的提款银行卡信息是否与您注册的真实姓名是否一致，以便我们能顺利为您支付您的款项。</p>");
	        sb.append("<p>3、我们提供24小时免费回拨热线，<a href=\"javascript:open800ChatBox();\"><span>在线客服（点击联系）</span></a>.QQ：800186517</p>");
	        sb.append("<p style=\"text-align:right\">八达国际客服部上</p>");
	        letterService.create(cust.cust_id, cust.login_name, "欢迎加入八达国际娱乐城", sb.toString(), cust.login_name, true);
		}
		 String token =Token.get(login_name);
		 Result r =Result.create(task.getId(), task.getFunId());
		 r.addFields(new String[]{"ok","login_name","login_pwd","token","message"});
		 r.setFlag("-1");
		 r.setIsList(true);
		 r.setLength(1);
		 r.addRecord(new String[]{"1",login_name,login_pwd,token,""});
		return r;
	}
	
	public static void main(String []args){
		String phone = "18695984361";
		String login_pwd=phone.substring(phone.length()-6,phone.length());
		String login_pwd_md5=MD5Util.MD5FromMeibo(login_pwd);
		System.out.println(login_pwd);
		System.out.println(login_pwd_md5);
	}
}
