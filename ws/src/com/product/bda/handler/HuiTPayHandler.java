package com.product.bda.handler;

import java.math.BigDecimal;

import org.springframework.jdbc.core.JdbcTemplate;

import com.product.bda.service.CreditService;
import com.product.bda.service.Customer;
import com.product.bda.service.CustomerService;
import com.product.bda.service.DepositService;
import com.product.bda.service.OrderNoService;
import com.product.bda.service.OrderNumberService;
import com.product.bda.service.PayService;
import com.product.bda.service.ScoreService;

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
import bsz.exch.utils.MD5Util;

@Handler(name="HuiTPay")
public class HuiTPayHandler {

	@Before
	public FobbinInfo before(Task task,InterFace inter){
		String product=task.getParam("product");
		if(!"8da".equals(product)){
			return  new FobbinInfo(true,"No product support!");
		}
		return new FobbinInfo(false,"");
	}
	
	@Service(name="pay")
	@Params(validateField={"login_name"})
    public Result pay(Task task,InterFace inter){
		
		String product = task.getParam("product");
		String pre=ThPayResource.instance().getConfig("huitpay."+product+".pre");
		String parter =ThPayResource.instance().getConfig("huitpay."+product+".merNo"); 
		String mer_key =ThPayResource.instance().getConfig("huitpay."+product+".mer_key");
		String callbackurl = ThPayResource.instance().getConfig("huitpay."+product+".notifyUrl"); //服务器通知返回接口
		String hrefbackurl =ThPayResource.instance().getConfig("huitpay."+product+".hrefbackurl");
		String paytype = task.getParam("paytype");  //支付方式
		String attach = task.getParam("login_name");
		String return_url = task.getParam("return_url");  //客户访问用的网址
		String orderid = pre+"" + String.valueOf(System.currentTimeMillis());
		String payerIp = task.getParam("ip");	//支付ip
		
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		String ds=inter.getDataSource();
		String value = task.getParam("amount");
		
		PayService payservice = new PayService(template,ds);
		String banktype=null;
		if("2099".equals(paytype)){
			banktype = "微信WAP";
		}else if("2097".equals(paytype)){
			banktype = "QQWAP";
		}
		payservice.createOrder("huitpay",orderid, task.getParam("login_name"), value, payerIp, banktype, attach,return_url);
		String submiturl="";
		try{
			
			String prestr = "parter="+parter+"&type="+paytype+"&value="+value+"&orderid="+orderid+"&callbackurl="+callbackurl;
	        
			System.out.println(prestr);
			String signMsg = MD5Util.md5Encode(prestr+mer_key);
			submiturl=hrefbackurl+"?"+prestr+"&hrefbackurl=&payerIp=&attach=&sign="+signMsg;
	         Result r =Result.create(task.getId(), task.getFunId());
				r.addFields(new String[]{"ok","pay_id","message","amount","directurl"});
				r.setFlag("-1");
				r.setIsList(true);
				r.setLength(1);
				r.addRecord(new String[]{"1",orderid,"",value,submiturl.toString()});
				return r;
			
		}catch(Exception e){
			e.printStackTrace();
		 }
		Result r =Result.create(task.getId(), task.getFunId());
		r.addFields(new String[]{"ok","pay_id","message","amount","directurl"});
		r.setFlag("-1");
		r.setIsList(true);
		r.setLength(1);
		r.addRecord(new String[]{"0",orderid,"无法创建订单",value,""});
		return r;
		
	}
	
	@Service(name="notify")
	@Params(validateField={"orderid"})
	public Result notify(Task task,InterFace inter){
		
		String product = task.getParam("product");
		String mer_key =ThPayResource.instance().getConfig("huitpay."+product+".mer_key");
		
	    String orderid = task.getParam("orderid");
	    String opstate = task.getParam("opstate");
	    BigDecimal ovalue = new BigDecimal(task.getParam("ovalue"));
	    String sysorderid = task.getParam("sysorderid");
	    String sign = task.getParam("sign");
		
	    String prestr = "orderid="+orderid+"&opstate="+opstate+"&ovalue="+ovalue;
	    System.out.println(prestr);
		String signMsg = MD5Util.md5Encode(prestr+mer_key);
		System.out.println("ori="+signMsg+",sign="+sign);
        if (signMsg.equals(sign)) {
        	if ("0".equals(opstate)) {  
        		String ds=ThPayResource.instance().getConfig("huitpay."+product+".datasource");
        		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
    	    	OrderNumberService orderNumberService =new OrderNumberService(template,ds);
    	    	CreditService creditService =new CreditService(template,ds);
    			CustomerService custService =new CustomerService(template,ds);
    			DepositService depositService =new DepositService(template,ds);
    			PayService payservice = new PayService(template,ds);
    			String login_name =payservice.queryLoginName("huitpay", orderid);
    			if(payservice.isNotDone("huitpay", orderid, login_name)){
    				
    				payservice.updaeteStatus("huitpay", orderid, sysorderid, "2", ovalue, "");
					
					orderNumberService.createOrderNumber(orderid);
					String paytype = payservice.queryPayType("huitpay",orderid);
					if(creditService.add(login_name, ovalue,"自动充值", "huitpay在线支付:"+paytype, login_name, orderid)){
						try{
							Customer customer= custService.getCustomer(login_name);
							
							//添加存款记录和日志
							String dep_no = OrderNoService.createLocalNo("DE");
							depositService.addDeposit2(dep_no, customer.cust_id, login_name, customer.real_name, ovalue, "huitpay在线支付"+paytype, "", "huitpay在线支付"+paytype, "", orderid);
							//查询是否第一次存款,如果是,升级用户等级
							if(depositService.NTgetCount(customer.cust_id) == 1){
								if(ovalue.intValue() >=100){
									custService.NTmodCustlevelFirst(customer.cust_id, 1);
								}else{
									custService.NTmodFirstDepositDateOnly(customer.cust_id, 1);
								}
								
							}else{ 
								if(customer.cust_level == 0){
									if(ovalue.intValue() >=100){
										custService.NTmodCustlevelOnly(customer.cust_id, 1);
									}
								}
							}
							
							if(customer.promo_flag != null && customer.promo_flag){
								ScoreService scoreService =new ScoreService(template,ds);
								if(scoreService.depositCountToday(login_name)==1 && ovalue.intValue() >=100){
									scoreService.modScore(orderid, "签到积分",  new BigDecimal(1), login_name, customer.cust_id, "");
								}
								if(ovalue.intValue() >=100){
									scoreService.modScore(orderid, "存款积分",new BigDecimal( ovalue.divide(new BigDecimal(100)).intValue()), login_name, customer.cust_id, "");
								}
							   
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
	
}
