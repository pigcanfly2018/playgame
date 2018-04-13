package com.product.bda.handler;

import java.math.BigDecimal;

import org.springframework.jdbc.core.JdbcTemplate;

import com.product.bda.service.CreditService;
import com.product.bda.service.Customer;
import com.product.bda.service.CustomerService;
import com.product.bda.service.DepositService;
import com.product.bda.service.JbpService;
import com.product.bda.service.OrderNoService;
import com.product.bda.service.ScoreService;
import com.product.bda.service.YbpService;

import bsz.exch.bank.JbPayResource;
import bsz.exch.bank.PayResource;
import bsz.exch.bank.YbPayResource;
import bsz.exch.bean.FobbinInfo;
import bsz.exch.bean.Result;
import bsz.exch.bean.Task;
import bsz.exch.core.Before;
import bsz.exch.core.Handler;
import bsz.exch.core.InterFace;
import bsz.exch.core.JdbcResource;
import bsz.exch.core.Params;
import bsz.exch.core.Service;
import bsz.exch.jubao.JubaoPay;
import bsz.exch.jubao.RSA;
import bsz.exch.utils.MD5Util;

@Handler(name="YBP")
public class YingBaoPayHandler {
	
	
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
		String pre=YbPayResource.instance().getConfig("ybpay."+product+".pre");
		String url = task.getParam("return_url");  //客户访问用的网址
		
		
        
		
		String order_id = pre+"_" + String.valueOf(System.currentTimeMillis());
		String payamount = task.getParam("amount");
		String type = task.getParam("type");
		
		
		String partner =YbPayResource.instance().getConfig("ybpay."+product+".partner"); 
		String mer_key =YbPayResource.instance().getConfig("ybpay."+product+".mer_key"); 
		String version =YbPayResource.instance().getConfig("ybpay."+product+".version"); 
		String return_url = YbPayResource.instance().getConfig("ybpay."+product+".return_url"); //服务器跳转返回接口
		String notify_url = YbPayResource.instance().getConfig("ybpay."+product+".notify_url"); //服务器通知返回接口
		if(type != null && type.equals("2")){
			partner =YbPayResource.instance().getConfig("ybpay2."+product+".partner"); 
			mer_key =YbPayResource.instance().getConfig("ybpay2."+product+".mer_key");
		}
		String paytype = task.getParam("bank");	//支付方式
		String payip = task.getParam("ip");	//支付ip
		
		

		String remark = task.getParam("remark");
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		String ds=inter.getDataSource();
		YbpService ybpService = new YbpService(template,ds);
		ybpService.createYbp(order_id, task.getParam("login_name"), payamount, payip, paytype, remark+type,url);
		remark = task.getParam("login_name");
		try{
			
				String prestr ="version="+version+"&partner="+partner+"&orderid="+order_id+"&payamount="+payamount
						+"&payip="+payip+"&notifyurl="+notify_url+"&returnurl="+return_url+"&paytype="+paytype+"&remark="+remark+"&key="+mer_key;
						 
				System.out.println(prestr);
				String sign_value = MD5Util.md5Encode(prestr);
			    Result r =Result.create(task.getId(), task.getFunId());
				r.addFields(new String[]{"ok","order_id","message","payamount","sign_value","payip","paytype","remark","partner"});
				r.setFlag("-1");
				r.setIsList(true);
				r.setLength(1);
				r.addRecord(new String[]{"1",order_id,"",payamount,sign_value,payip,paytype,remark,partner});
			return r;
		 }catch(Exception e){
			e.printStackTrace();
		 }
		
		 Result r =Result.create(task.getId(), task.getFunId());
		 r.addFields(new String[]{"ok","order_id","message","payamount","sign_value","payip","paytype","remark","partner"});
		 r.setFlag("-1");
		 r.setIsList(true);
		 r.setLength(1);
		 r.addRecord(new String[]{"0",order_id,"无法创建订单!",payamount,"",paytype,remark,partner});
		 return r;
   }
	

	@Service(name="notify")
	@Params(validateField={"orderid","payamount","opstate","orderno","eypaltime","message","paytype","remark","sign"})
	public Result notify(Task task,InterFace inter){
		String product = task.getParam("product");
		
		String orderid = task.getParam("orderid");
	    String opstate = task.getParam("opstate");
	    String orderno = task.getParam("orderno");
	    String eypaltime = task.getParam("eypaltime");
	    String message = task.getParam("message");
	    String paytype = task.getParam("paytype");
	    String remark = task.getParam("remark");
	    String sign = task.getParam("sign");
	    
	    BigDecimal payamount = new BigDecimal(task.getParam("payamount"));
	    if(opstate.equals("2")){
	    	String ds=YbPayResource.instance().getConfig("ybpay."+product+".datasource");
			
	    	String partner =YbPayResource.instance().getConfig("ybpay."+product+".partner"); 
			String mer_key =YbPayResource.instance().getConfig("ybpay."+product+".mer_key");
			
			String partner2 =YbPayResource.instance().getConfig("ybpay2."+product+".partner"); 
			String mer_key2 =YbPayResource.instance().getConfig("ybpay2."+product+".mer_key"); 
			
			String version =YbPayResource.instance().getConfig("ybpay."+product+".version"); 
			
			String prestr ="version="+version+"&partner="+partner+"&orderid="+orderid+"&payamount="+payamount
	    			+"&opstate="+opstate+"&orderno="+orderno+"&eypaltime="+eypaltime+"&message="+message
	    			+"&paytype="+paytype+"&remark="+remark+"&key="+mer_key;
			String prestr2 ="version="+version+"&partner="+partner2+"&orderid="+orderid+"&payamount="+payamount
	    			+"&opstate="+opstate+"&orderno="+orderno+"&eypaltime="+eypaltime+"&message="+message
	    			+"&paytype="+paytype+"&remark="+remark+"&key="+mer_key2;
	    
			String aftersign_value =  MD5Util.md5Encode(prestr);
			String aftersign_value2 =  MD5Util.md5Encode(prestr2);
			
			JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
			YbpService ybpService =new YbpService(template,ds);
			CreditService creditService =new CreditService(template,ds);
			CustomerService custService =new CustomerService(template,ds);
			DepositService depositService =new DepositService(template,ds);
			
			if(aftersign_value.equals(sign) || aftersign_value2.equals(sign)){
				//没有处理
				if(ybpService.isNotDoYdp(orderid,remark)){
					ybpService.updaeteYdp(orderid, orderno, "2", payamount,paytype);
					String login_name=remark;
			
					//支付成功
					
						if(creditService.add(login_name, payamount,"自动充值", "yb在线支付:"+eypaltime, login_name, orderid)){
							  try{
									Customer customer= custService.getCustomer(login_name);
									//添加存款记录和日志
									String dep_no = OrderNoService.createLocalNo("DE");
									depositService.addDeposit2(dep_no, customer.cust_id, login_name, customer.real_name, payamount, "yb在线支付", "", "yb在线支付", "", orderid);
									//查询是否第一次存款,如果是,升级用户等级
									if(depositService.NTgetCount(customer.cust_id) == 1){
										custService.NTmodCustlevelFirst(customer.cust_id, 1);
									}
									ScoreService scoreService =new ScoreService(template,ds);
									if(scoreService.depositCountToday(login_name)==1){
										scoreService.modScore(orderid, "签到积分",  new BigDecimal(1), login_name, customer.cust_id, "");
									}
									scoreService.modScore(orderid, "存款积分",new BigDecimal( payamount.divide(new BigDecimal(100)).intValue()), login_name, customer.cust_id, "");
									
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
		String prestr ="version="+"1.0"+"&partner="+"1151"+"&orderid="+"gd_1460009906961"+"&payamount="+"100.00"
    			+"&opstate="+"2"+"&orderno="+"B5522565727760368777"+"&eypaltime="+"19000101000000"+"&message="+"success"
    			+"&paytype="+"ALIPAY"+"&remark="+"woody"+"&key="+"3b7c652d10ce454b8e49b6cde355d1da";
    
		String aftersign_value =  MD5Util.md5Encode(prestr);
		System.out.println(prestr);
		System.out.println(aftersign_value);
	}
	
	

}
