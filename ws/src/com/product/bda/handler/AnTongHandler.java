package com.product.bda.handler;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.jdbc.core.JdbcTemplate;

import com.product.bda.service.CashGift;
import com.product.bda.service.CashGiftService;
import com.product.bda.service.CreditService;
import com.product.bda.service.Customer;
import com.product.bda.service.CustomerService;
import com.product.bda.service.DepositService;
import com.product.bda.service.OrderNoService;
import com.product.bda.service.OrderNumberService;
import com.product.bda.service.PayOnline;
import com.product.bda.service.PayOnlineService;
import com.product.bda.service.PayService;
import com.product.bda.service.ScoreService;
import com.product.bda.service.YbpService;

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

@Handler(name="ATP")
public class AnTongHandler {

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
		String pre=ThPayResource.instance().getConfig("atpay."+product+".pre");
		String url = task.getParam("return_url");  //客户访问用的网址
		
		
        
		
		String sdorderno = pre+"_" + String.valueOf(System.currentTimeMillis());
		String total_fee = task.getParam("amount");
		
		String customerid =ThPayResource.instance().getConfig("atpay."+product+".customerid"); 
		String mer_key =ThPayResource.instance().getConfig("atpay."+product+".mer_key"); 
		String version ="1.0"; 
		String returnurl = "http://www.baidu.com"; //服务器跳转返回接口
		String notifyurl = ThPayResource.instance().getConfig("atpay."+product+".notifyUrl"); //服务器通知返回接口
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		String ds=inter.getDataSource();
		PayOnlineService poService= new PayOnlineService(template,ds);
		PayOnline online = poService.getPayOnline("antong", product);
		String submit_value="";
		if(online != null){
			
			submit_value = online.submit_value;
			returnurl = online.return_value;
			notifyurl = online.notify_value;
			
			
			
		}
		
		String paytype = task.getParam("paytype");	//支付方式
		String payip = task.getParam("ip");	//支付ip
		String bankcode = "";
		String get_code = "";

		String remark = task.getParam("login_name");
		
		PayService payservice = new PayService(template,ds);
		payservice.createOrder("antong",sdorderno, task.getParam("login_name"), total_fee, payip, paytype, remark,url);

		try{
			
//				String prestr ="version="+version+"&partner="+partner+"&orderid="+order_id+"&payamount="+payamount
//						+"&payip="+payip+"&notifyurl="+notify_url+"&returnurl="+return_url+"&paytype="+paytype+"&remark="+remark+"&key="+mer_key;
				
				String prestr ="version="+version+"&customerid="+customerid+"&total_fee="+total_fee+"&sdorderno="+sdorderno+"&notifyurl="+notifyurl+"&returnurl="+returnurl+"&"+mer_key;
						 
				//System.out.println(prestr);
				String sign = MD5Util.md5Encode(prestr).toLowerCase();
			    Result r =Result.create(task.getId(), task.getFunId());
				r.addFields(new String[]{"ok","sdorderno","message","total_fee","sign","paytype","remark","customerid","submit_value","returnurl","notifyurl"});
				r.setFlag("-1");
				r.setIsList(true);
				r.setLength(1);
				r.addRecord(new String[]{"1",sdorderno,"",total_fee,sign,paytype,remark,customerid,submit_value,returnurl,notifyurl});
			return r;
		 }catch(Exception e){
			e.printStackTrace();
		 }
		
		 Result r =Result.create(task.getId(), task.getFunId());
		 r.addFields(new String[]{"ok","sdorderno","message","total_fee","sign_value","paytype","remark","customerid","submit_value","returnurl","notifyurl"});
		 r.setFlag("-1");
		 r.setIsList(true);
		 r.setLength(1);
		 r.addRecord(new String[]{"0",sdorderno,"无法创建订单!",total_fee,"",paytype,remark,customerid,"","",""});
		 return r;
   }
	
	
	@Service(name="notify")
	public Result notify(Task task,InterFace inter){
		
		
		String product = task.getParam("product");
		
		
		String status = task.getParam("status");
	    String customerid = task.getParam("customerid");
	    String sdpayno = task.getParam("sdpayno");
	    String sdorderno = task.getParam("sdorderno");
	    String paytype = task.getParam("paytype");
	    String remark = task.getParam("remark");
	    
	    String sign = task.getParam("sign");
	    BigDecimal total_fee = new BigDecimal(task.getParam("total_fee"));
	    if(status.equals("1")){
	    	String ds=ThPayResource.instance().getConfig("ytbpay."+product+".datasource");
	    	JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
	    	OrderNumberService orderNumberService =new OrderNumberService(template,ds);
	    	CreditService creditService =new CreditService(template,ds);
			CustomerService custService =new CustomerService(template,ds);
			DepositService depositService =new DepositService(template,ds);
			String mer_key =ThPayResource.instance().getConfig("atpay."+product+".mer_key"); 
			PayService payservice = new PayService(template,ds);
			
			
			String prestr ="customerid="+customerid+"&status="+status+"&sdpayno="+sdpayno+"&sdorderno="+sdorderno+"&total_fee="+total_fee+"&paytype="+paytype+"&"+mer_key;
			 
			String signafter = MD5Util.md5Encode(prestr).toLowerCase();
			
			if(signafter.equals(sign) ){ 
				
				if(payservice.isNotDone("antong", sdorderno, remark)){
					
					
					payservice.updaeteStatus("antong", sdorderno, sdpayno, "2", total_fee, paytype);
					
					orderNumberService.createOrderNumber(sdorderno);
					
					if(creditService.add(remark, total_fee,"自动充值", "at在线支付:"+paytype, remark, sdorderno)){
						try{
							String login_name = remark;
							Customer customer= custService.getCustomer(login_name);
							//添加存款记录和日志
							String dep_no = OrderNoService.createLocalNo("DE");
							depositService.addDeposit2(dep_no, customer.cust_id, login_name, customer.real_name, total_fee, "ytb在线支付"+paytype, "", "ytb在线支付"+paytype, "", sdorderno);
							//查询是否第一次存款,如果是,升级用户等级
							
							if(depositService.NTgetCount(customer.cust_id) == 1){
								if(total_fee.intValue() >=100){
									custService.NTmodCustlevelFirst(customer.cust_id, 1);
								}else{
									custService.NTmodFirstDepositDateOnly(customer.cust_id, 1);
								}
								
							}else{ 
								if(customer.cust_level == 0){
									if(total_fee.intValue() >=100){
										custService.NTmodCustlevelOnly(customer.cust_id, 1);
									}
								}
							}
							
							if(customer.promo_flag != null && customer.promo_flag){
								ScoreService scoreService =new ScoreService(template,ds);
								if(scoreService.depositCountToday(login_name)==1 && total_fee.intValue() >=100){
									scoreService.modScore(sdorderno, "签到积分",  new BigDecimal(1), login_name, customer.cust_id, "");
								}
								if(total_fee.intValue() >=100){
									scoreService.modScore(sdorderno, "存款积分",new BigDecimal( total_fee.divide(new BigDecimal(100)).intValue()), login_name, customer.cust_id, "");
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
