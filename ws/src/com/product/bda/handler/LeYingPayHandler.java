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
import com.product.bda.service.LypService;
import com.product.bda.service.OrderNoService;
import com.product.bda.service.OrderNumberService;
import com.product.bda.service.PayOnline;
import com.product.bda.service.PayOnlineService;
import com.product.bda.service.ScoreService;
import com.product.bda.service.YbpService;

import bsz.exch.bank.LyPayResource;
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
import bsz.exch.utils.DateUtil;
import bsz.exch.utils.MD5Util;
import bsz.exch.utils.RandomUtil;


@Handler(name="LYP")
public class LeYingPayHandler {
	
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
		String pre=LyPayResource.instance().getConfig("lypay."+product+".pre");
		String url = task.getParam("return_url");  //客户访问用的网址
		
		String serialID = pre+"_" + String.valueOf(System.currentTimeMillis());
		String show_amount = task.getParam("amount");
		String order_amount = (Integer.parseInt(show_amount)*100)+"";
		String type = task.getParam("type");
		String payip = task.getParam("ip");	//支付ip
		String paytype = task.getParam("bank");	//支付方式
		String orgCode = "";
		if (paytype.equals("WX")) {
			orgCode="wx";
		}else if (paytype.equals("ZFB") ) {
			orgCode="zfb";
		}
		System.out.println("orgCode     "+orgCode);
		String payway = "1000";
		String directFlag = "1";
		String submitTime = DateUtil.getOrderTimeyyyyMMddhhmmss();
		String return_url = LyPayResource.instance().getConfig("lypay."+product+".return_url"); //服务器跳转返回接口
		String notify_url = LyPayResource.instance().getConfig("lypay."+product+".notify_url"); //服务器通知返回接口
		String version =LyPayResource.instance().getConfig("lypay."+product+".version"); 
		String partnerID =LyPayResource.instance().getConfig("lypay1."+product+".partnerID"); 
		String pkey =LyPayResource.instance().getConfig("lypay1."+product+".pkey"); 
		if(type != null && type.equals("2")){
			 partnerID =LyPayResource.instance().getConfig("lypay2."+product+".partnerID"); 
			 pkey =LyPayResource.instance().getConfig("lypay2."+product+".pkey");
		}
		String remark = task.getParam("remark");
		
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		String ds=inter.getDataSource();
		LypService lypService = new LypService(template,ds);
		lypService.createLyp(serialID, task.getParam("login_name"), show_amount, payip, paytype, remark+type,url);
		
		String submit_value = "";
		
		
		PayOnlineService poService= new PayOnlineService(template,ds);
		PayOnline online = poService.getPayOnline("leying", product);
		if(online != null){
			submit_value = online.submit_value;
			return_url = online.return_value;
			notify_url = online.notify_value;	
		}
		remark = task.getParam("login_name");
		String charset = "1";
		String signType = "2";
		try{
			String orderDetails=serialID+","+order_amount+","+"商城,"+"手机配件,"+"1";
			String prestr ="version="+version+"&serialID="+serialID+"&submitTime="+submitTime+"&failureTime="+""+"&customerIP="+""
					+"&orderDetails="+orderDetails+"&totalAmount="+order_amount+"&type="+payway
					+"&buyerMarked="+""+"&payType="+paytype+"&orgCode="+orgCode+"&currencyCode="+"1"+"&directFlag="+"1"+"&borrowingMarked="+""
					+"&couponFlag="+"0"+"&platformID="+""+"&returnUrl="+return_url+"&noticeUrl="+notify_url+"&partnerID="+partnerID
					+"&remark="+remark+"&charset="+charset+"&signType="+signType;
			
			prestr = prestr+"&pkey="+pkey;
			System.out.println(prestr);
			String signMsg = MD5Util.md5Encode(prestr);
			Result r =Result.create(task.getId(), task.getFunId());
			r.addFields(new String[]{"ok","serialID","message","submitTime","orderDetails","order_amount","show_amount","signMsg","payip","paytype","remark","partnerID","submit_value","return_value","notify_value"});
			r.setFlag("-1");
			r.setIsList(true);
			r.setLength(1);
			r.addRecord(new String[]{"1",serialID,"",submitTime,orderDetails,order_amount,show_amount,signMsg,payip,paytype,remark,partnerID,submit_value,return_url,notify_url});
			return r;
		}catch(Exception e){
			e.printStackTrace();
		 }
		 Result r =Result.create(task.getId(), task.getFunId());
		 r.addFields(new String[]{"ok","serialID","message","submitTime","orderDetails","order_amount","show_amount","signMsg","payip","paytype","remark","partnerID","submit_value","return_value","notify_value"});
		 r.setFlag("-1");
		 r.setIsList(true);
		 r.setLength(1);
		 r.addRecord(new String[]{"1",serialID,"",submitTime,"",order_amount,show_amount,"",payip,paytype,remark,partnerID,submit_value,return_url,notify_url});
		 return r;
	}
	
	
	@Service(name="notify")
	@Params(validateField={"orderID","orderAmount","payAmount","acquiringTime","completeTime","partnerID","remark","charset","signType","signMsg","stateCode"})
	public Result notify(Task task,InterFace inter){
		String product = task.getParam("product");
		
		String orderID = task.getParam("orderID");
		String orderAmount = task.getParam("orderAmount");
		String payAmount = task.getParam("payAmount");
		
		String acquiringTime = task.getParam("acquiringTime");
		String completeTime = task.getParam("completeTime");
		String orderNo = task.getParam("orderNo");
		
		String partnerID = task.getParam("partnerID");
		String remark = task.getParam("remark");
		String charset = task.getParam("charset");
		
		String signType = task.getParam("signType");
		String signMsg = task.getParam("signMsg");
		String resultCode = task.getParam("resultCode");
		
		String localpartnerID1 =LyPayResource.instance().getConfig("lypay1."+product+".partnerID");
		String pkey =LyPayResource.instance().getConfig("lypay1."+product+".pkey"); 
		if(!partnerID.equals(localpartnerID1)){
			pkey =LyPayResource.instance().getConfig("lypay2."+product+".pkey"); 
		}
		
		String stateCode = task.getParam("stateCode");
		
		if ("2".equalsIgnoreCase(stateCode)){
			try{
			String prestr ="orderID="+orderID+"&resultCode="+resultCode+"&stateCode="+stateCode+"&orderAmount="+orderAmount
	    			+"&payAmount="+payAmount+"&acquiringTime="+acquiringTime+"&completeTime="+completeTime+"&orderNo="+orderNo
	    			+"&partnerID="+partnerID+"&remark="+remark+"&charset="+charset+"&signType="+signType+"&pkey="+pkey;
			
			String aftersignMsg = MD5Util.md5Encode(prestr);
			if(aftersignMsg.equalsIgnoreCase(signMsg)){
				String ds=LyPayResource.instance().getConfig("lypay."+product+".datasource");
				JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
				LypService lypService = new LypService(template,ds);
				CreditService creditService =new CreditService(template,ds);
				CustomerService custService =new CustomerService(template,ds);
				DepositService depositService =new DepositService(template,ds);
				OrderNumberService orderNumberService =new OrderNumberService(template,ds);
				if(lypService.isNotDoLyp(orderID,remark)){
					
					String payType = lypService.queryPayType(orderID);
					if(payType == null)
						payType = "";
					
					
					if(payType.equals("ZFB")){
						lypService.updaeteZFBLyp(orderID, orderNo, "2", new BigDecimal(payAmount).divide(new BigDecimal(100)));
					}else{
						lypService.updaeteLyp(orderID, orderNo, "2", new BigDecimal(payAmount).divide(new BigDecimal(100)));
					}
					
					
					orderNumberService.createOrderNumber(orderNo);
					
					String login_name=remark;
					
					
					if(creditService.add(login_name, new BigDecimal(payAmount).divide(new BigDecimal(100)),"自动充值", "ly支付:"+payType+completeTime, login_name, orderID)){
						 
							 Customer customer= custService.getCustomer(login_name);
								//添加存款记录和日志
								String dep_no = OrderNoService.createLocalNo("DE");
								depositService.addDeposit2(dep_no, customer.cust_id, login_name, customer.real_name, new BigDecimal(payAmount).divide(new BigDecimal(100)), "ly在线支付"+payType, "", "ly在线支付"+payType, "", orderID);
								//查询是否第一次存款,如果是,升级用户等级
								if(depositService.NTgetCount(customer.cust_id) == 1){
									custService.NTmodCustlevelFirst(customer.cust_id, 1);
								}
								
								if(customer.promo_flag != null && customer.promo_flag){
									ScoreService scoreService =new ScoreService(template,ds);
									if(scoreService.depositCountToday(login_name)==1){
										scoreService.modScore(orderID, "签到积分",  new BigDecimal(1), login_name, customer.cust_id, "");
									}
									scoreService.modScore(orderID, "存款积分",new BigDecimal( new BigDecimal(payAmount).divide(new BigDecimal(100)).divide(new BigDecimal(100)).intValue()), login_name, customer.cust_id, "");
									
									Date now =new Date(System.currentTimeMillis());
									if((now.getTime()>DateUtil.stringToDate("2016-12-21", "yyyy-MM-dd").getTime()&&
								    		  DateUtil.stringToDate("2017-01-02", "yyyy-MM-dd").getTime()>now.getTime())){
								    	  //int dcount=depositService.NTgetCountToday(customer.cust_id);
								    	 // if(dcount<=5){
								    		  String giftCode=RandomUtil.getRandom(8);
												 CashGift gift =new CashGift();
									        	 gift.gift_code=giftCode;
									        	 gift.login_name=customer.login_name;
									        	 gift.deposit_credit=new BigDecimal(payAmount).divide(new BigDecimal(100));
									        	 gift.valid_credit=new BigDecimal(0);
									        	 gift.net_credit=new BigDecimal(0);
									        	 gift.rate=Float.valueOf(2);
									        	 Integer rate = 2;
									        	 float f=new BigDecimal(payAmount).divide(new BigDecimal(100)).floatValue()*2/100;
//									        	 if(dcount==5){
//									        		 gift.rate=Float.valueOf(3);
//									        		 f=new BigDecimal(payAmount).divide(new BigDecimal(100)).floatValue()*3/100;
//									        		 rate = 3;
//									        	 }
//									        	 
//									        	 if(f>1111){
//									        		 f=1111;
//									        	 }
									        	 gift.payout=new BigDecimal(f);
									        	 gift.gift_type="圣诞元旦双节礼";
									        	 gift.status=1;
									        	 gift.gift_no=OrderNoService.createLocalNo("GI");
									     		 gift.cust_id=customer.cust_id;
									        	 gift.cs_date=new Date(System.currentTimeMillis());
									        	 gift.real_name=customer.real_name;
									        	 gift.cust_level=customer.cust_level;
									           	 gift.kh_date=customer.create_date;
									        	 gift.create_user="系统";
									        	 gift.create_date=new Date(System.currentTimeMillis());
									        	 
									        	 CashGiftService giftService =new CashGiftService(template,ds);
									        	 Long giftId = giftService.NTcreat(gift);
									        	 if(giftId != null){
									        		 giftService.NTAuditGift(giftId, 3, "系统", "系统审核通过-"+dep_no+" |"+rate);
									        		 
									        		 creditService.add(login_name, gift.payout,gift.gift_type, "添加礼金"+gift.gift_type+" |"+rate+"%", "系统", gift.gift_no);
									        	 }
									        	 
								    	  //}
								    }
								}
								
								
							      
								Result r =Result.create(task.getId(), task.getFunId());
								r.addFields(new String[]{"ok"});
								r.setFlag("-1");
								r.setIsList(true);
								r.setLength(1);
								r.addRecord(new String[]{"1"});
						 
					}
				}
			}
			
			}catch(Exception e){	
				e.printStackTrace();
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
