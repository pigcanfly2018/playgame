package com.product.bda.handler;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.jdbc.core.JdbcTemplate;

import com.product.bda.service.CashGift;
import com.product.bda.service.CashGiftService;
import com.product.bda.service.ChangFuService;
import com.product.bda.service.CreditService;
import com.product.bda.service.Customer;
import com.product.bda.service.CustomerService;
import com.product.bda.service.DepositService;
import com.product.bda.service.OrderNoService;
import com.product.bda.service.OrderNumberService;
import com.product.bda.service.PayOnline;
import com.product.bda.service.PayOnlineService;
import com.product.bda.service.ScoreService;
import com.product.bda.service.XinBeiService;

import bsz.exch.bank.ThPayResource;
import bsz.exch.bean.FobbinInfo;
import bsz.exch.bean.Result;
import bsz.exch.bean.Task;
import bsz.exch.core.Before;
import bsz.exch.core.Handler;
import bsz.exch.core.InterFace;
import bsz.exch.core.JdbcResource;
import bsz.exch.core.Params;
import bsz.exch.core.Service;
import bsz.exch.utils.DateUtil;
import bsz.exch.utils.MD5Util;
import bsz.exch.utils.RandomUtil;

@Handler(name="ChangFuPay")
public class ChangFuHandler {
	
	@Before
	public FobbinInfo before(Task task,InterFace inter){
		String product=task.getParam("product");
		if(!"8da".equals(product)){
			return  new FobbinInfo(true,"No product support!");
		}
		return new FobbinInfo(false,"");
	}
	
	@Service(name="pay")
	@Params(validateField={"login_name","amount","return_url"})
    public Result pay(Task task,InterFace inter){
		
		String product = task.getParam("product");
		String pre=ThPayResource.instance().getConfig("changfupay."+product+".pre");
		
		String return_url = task.getParam("return_url");  //客户访问用的网址
		
		String orderid = pre+"_" + String.valueOf(System.currentTimeMillis());
		String money = task.getParam("amount");
		
		String userid =ThPayResource.instance().getConfig("changfupay."+product+".userid"); 
		String mer_key =ThPayResource.instance().getConfig("changfupay."+product+".mer_key");
		String url = ThPayResource.instance().getConfig("changfupay."+product+".url"); //异步通知返回接口
		String bankid = task.getParam("payType");	//支付方式
		String payip = task.getParam("customer_ip");	//支付ip
		String login_name = task.getParam("login_name");
		
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		String ds=inter.getDataSource();
		String submit_value = "";
		ChangFuService cfpService = new ChangFuService(template,ds);
		PayOnlineService poService= new PayOnlineService(template,ds);
		PayOnline online = poService.getPayOnline("changfu", product);
		if(online != null){
			submit_value = online.submit_value;
			url = online.notify_value;
		}
		
		String paytype="";
		if(bankid.equals("2008")){
			paytype="QQ钱包";
		}else if(bankid.equals("2001")){
			paytype="微信支付";
			
		}else if(bankid.equals("100067")){
			paytype="支付宝支付";
			
		}else if(bankid.equals("2010")){
			paytype="京东钱包";
			
		}
		
		cfpService.createCfp(orderid, task.getParam("login_name"), money, payip, paytype, "8da",return_url);
		
		try{
			
			String sign = "";
			String sign2 = "";
			//拼接字符串
			
			String SignValue = "userid="+userid+"&orderid="+orderid+"&bankid="+bankid+"&keyvalue="+mer_key;
			String SignValue2 = "money="+money+"&userid="+userid+"&orderid="+orderid+"&bankid="+bankid+"&keyvalue="+mer_key;
			
			
			
			sign = MD5Util.MD5(SignValue).toLowerCase();
			sign2 = MD5Util.MD5(SignValue2).toLowerCase();
			
			Result r =Result.create(task.getId(), task.getFunId());
			 r.addFields(new String[]{"ok","orderid","message","money","sign","ext","userid","submit_value","url","bankid","sign2"});
			 r.setFlag("-1");
			 r.setIsList(true);
			 r.setLength(1);
			 r.addRecord(new String[]{"1",orderid,"",money,sign,login_name,userid,submit_value,url,bankid,sign2});
			 return r;
			 
			
		}catch(Exception e){
			e.printStackTrace();
		 }
		
		Result r =Result.create(task.getId(), task.getFunId());
		 r.addFields(new String[]{"ok","orderid","message","money","sign","ext","userid","submit_value","url","bankid","sign2"});
		 r.setFlag("-1");
		 r.setIsList(true);
		 r.setLength(1);
		 r.addRecord(new String[]{"0",orderid,"无法创建订单!",money,"",login_name,"",submit_value,"","",""});
		 return r;
		 
	}
	
	
	@Service(name="notify")
	@Params(validateField={"userid"})
	public Result notify(Task task,InterFace inter){
		String product = task.getParam("product");
		
		String userid = task.getParam("userid");
	    String returncode = task.getParam("returncode");
	    String orderid = task.getParam("orderid");
	    String sign = task.getParam("sign");
	    String sign2 = task.getParam("sign2");
	    String ext = task.getParam("ext");
	    BigDecimal money = new BigDecimal(task.getParam("money"));
	    
	    
	    if ("1".equalsIgnoreCase(returncode)){
	    	String ds=ThPayResource.instance().getConfig("changfupay."+product+".datasource");
	    	String mer_key =ThPayResource.instance().getConfig("changfupay."+product+".mer_key"); 
			
			
			//校验MD5码是不是正确的
	    	String SignValue = "";
	    	String SignValue2 = "";
			//拼接字符串
			
			String source1 = "returncode="+returncode+"&userid="+userid+"&orderid="+orderid+"&keyvalue="+mer_key;
			String source2 = "money="+money+"&returncode="+returncode+"&userid="+userid+"&orderid="+orderid+"&keyvalue="+mer_key;
			//System.out.println(SignValue);
			SignValue= MD5Util.md5Encode(source1);
			SignValue2= MD5Util.md5Encode(source2);
		
			
			
			JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
			ChangFuService cfpService = new ChangFuService(template,ds);
			CreditService creditService =new CreditService(template,ds);
			CustomerService custService =new CustomerService(template,ds);
			DepositService depositService =new DepositService(template,ds);
			OrderNumberService orderNumberService =new OrderNumberService(template,ds);
			if(SignValue.equals(sign) && SignValue2.equals(sign2)){ 
				//没有处理
				if(cfpService.isNotDoYdp(orderid)){
					
					String payType = cfpService.queryPayType(orderid);
					cfpService.updaete(orderid, orderid,orderid, "2", money, new BigDecimal(0.975));
					
					orderNumberService.createOrderNumber(orderid);
					
					String login_name = ext;
			
					//支付成功
					
						if(creditService.add(login_name, money,"自动充值", "cf在线支付:"+payType, login_name, orderid)){
							  try{
									Customer customer= custService.getCustomer(login_name);
									//添加存款记录和日志
									String dep_no = OrderNoService.createLocalNo("DE");
									depositService.addDeposit2(dep_no, customer.cust_id, login_name, customer.real_name, money, "cf在线支付"+payType, "", "cf在线支付"+payType, "", orderid);
									//查询是否第一次存款,如果是,升级用户等级
									if(depositService.NTgetCount(customer.cust_id) == 1){
										custService.NTmodCustlevelFirst(customer.cust_id, 1);
									}
									
									if(customer.promo_flag != null && customer.promo_flag){
										ScoreService scoreService =new ScoreService(template,ds);
										if(scoreService.depositCountToday(login_name)==1){
											scoreService.modScore(orderid, "签到积分",  new BigDecimal(1), login_name, customer.cust_id, "");
										}
										scoreService.modScore(orderid, "存款积分",new BigDecimal( money.divide(new BigDecimal(100)).intValue()), login_name, customer.cust_id, "");
										
										
									    
									}
									
									
									
									
								    
									Result r =Result.create(task.getId(), task.getFunId());
									r.addFields(new String[]{"ok"});
									r.setFlag("-1");
									r.setIsList(true);
									r.setLength(1);
									r.addRecord(new String[]{"1"});
								return r;
									
									
								}catch(Exception e){
									
									e.printStackTrace();
									
								}
						}
					
				}
			}
			
	    }
	    
	    Result r =Result.create(task.getId(), task.getFunId());
		 r.addFields(new String[]{"ok"});
		 r.setFlag("-1");
		 r.setIsList(true);
		 r.setLength(1);
		 r.addRecord(new String[]{"0"});
		 return r;
		
		
	}
	
	public static void main(String[] args){
		String source1 = "returncode="+"1"+"&userid="+"7736"+"&orderid="+"cf_1510823288538"+"&keyvalue="+"52c445d25fc7f6c76529a6c32cc21a4e";
		String source2 = "money="+new BigDecimal("3.00")+"&returncode="+"1"+"&userid="+"7736"+"&orderid="+"cf_1510823288538"+"&keyvalue="+"52c445d25fc7f6c76529a6c32cc21a4e";
		//System.out.println(SignValue);
		String SignValue= MD5Util.MD5(source1).toLowerCase();
		String SignValue2= MD5Util.MD5(source2).toLowerCase();
		
		System.out.println(SignValue);
		System.out.println(SignValue2);
		
		
		
		
	}

}
