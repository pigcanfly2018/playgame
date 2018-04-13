package com.soa.web;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.product.bda.service.CashGift;
import com.product.bda.service.CashGiftService;
import com.product.bda.service.CreditService;
import com.product.bda.service.Customer;
import com.product.bda.service.CustomerService;
import com.product.bda.service.DPayService;
import com.product.bda.service.DepositService;
import com.product.bda.service.OrderNoService;
import com.product.bda.service.ScoreService;
import com.product.bda.service.WithdrawService;

import bsz.exch.bank.PayResource;
import bsz.exch.bean.LogInfo;
import bsz.exch.core.JdbcResource;
import bsz.exch.utils.DateUtil;
import bsz.exch.utils.MD5Util;
import bsz.exch.utils.RandomUtil;

@Controller
public class DPayController {
	
    public static final ThreadLocal<LogInfo>  LOCAL_LOG  = new ThreadLocal<LogInfo>();
    
	private static Logger logger=Logger.getLogger(DPayController.class);
	
	private StringBuffer logRequest(HttpServletRequest request){
		StringBuffer sb =new StringBuffer();
		sb.append("URL:"+request.getRequestURI()+" Params:{");
		Enumeration<String> enums=request.getParameterNames();
		while (enums.hasMoreElements()) {
			String k=enums.nextElement();
			sb.append(k+":"+request.getParameter(k)+",");
		}
		if(sb.toString().endsWith(",")){
			sb.deleteCharAt(sb.length()-1);
		}
		sb.append("}");
		return sb;
	}
	
	//充值到账
	/**
	 *  pay_time	16	充值时间
		bank_id	2	银行id
		amount	10	充值金额
		company_order_num	64	平台订单号
		mownecum_order_num	64	DP订单号
		pay_card_num	32	付款卡卡号
		pay_card_name	32	付款卡用户名
		channel	32	交易渠道
		area	64	交易地址
		fee	6	手续费
		transaction_charge	6	服务费
		key	32	Key
		deposit_mode	1	充值渠道
		base_info	256	渠道原始关键信息

	 * @param request
	 * @param response
	 */
	private String addTransfer(HttpServletRequest request,HttpServletResponse response){

		String pay_time=request.getParameter("pay_time");
		String bank_id=request.getParameter("bank_id");
		String amount=request.getParameter("amount");
		String company_order_num=request.getParameter("company_order_num");
		String mownecum_order_num=request.getParameter("mownecum_order_num");
		String pay_card_num=request.getParameter("pay_card_num");
		String pay_card_name=request.getParameter("pay_card_name");
		String channel=request.getParameter("channel");
		String area=request.getParameter("area");
		String fee=request.getParameter("fee");
		String transaction_charge=request.getParameter("transaction_charge");
		String key=request.getParameter("key");
		String deposit_mode=request.getParameter("deposit_mode");
		String base_info=request.getParameter("base_info");
			
		StringBuffer sb =new StringBuffer();
		sb.append(pay_time);
		sb.append(bank_id);
		sb.append(amount);
		sb.append(company_order_num);
		sb.append(mownecum_order_num);
		if(pay_card_num !=null && !pay_card_num.equals("")){
			sb.append(pay_card_num);
		}
		if(pay_card_name !=null && !pay_card_name.equals("")){
			sb.append(pay_card_name);
		}
		if(channel !=null && !channel.equals("")){
			sb.append(channel);
		}
		if(area !=null && !area.equals("")){
			sb.append(area);
		}
		
		
		
		sb.append(fee);
		sb.append(transaction_charge);
		sb.append(deposit_mode);
		
		if(company_order_num==null||"".equals(company_order_num))return "";
		String product =PayResource.instance().findProduct(company_order_num.substring(0, 2));
		String ds=PayResource.instance().getConfig("dpay."+product+".datasource");
		String keyconfig=PayResource.instance().getConfig("dpay."+product+".keyconfig");
		String key0=MD5Util.MD5(MD5Util.MD5(keyconfig).toLowerCase()+sb.toString()).toLowerCase();
		
		JSONObject resultjson =new JSONObject(); 
		resultjson.put("mownecum_order_num", mownecum_order_num);
		resultjson.put("company_order_num", company_order_num);
		//匹配成功
		if(key0.equals(key)){
			JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
			CreditService creditService =new CreditService(template,ds);
			DPayService dPayService =new DPayService(template,ds);
			CustomerService custService =new CustomerService(template,ds);
			DepositService depositService =new DepositService(template,ds);
			
			String login_name=dPayService.findDepositLoginName(company_order_num);
			if(login_name!=null){
				//入库处理
				if(!dPayService.notifyDeposit(pay_time,bank_id, 
						amount, 
						company_order_num, mownecum_order_num, pay_card_num,
						pay_card_name, 
						channel, area, fee, transaction_charge, 
						deposit_mode, base_info)){
						resultjson.put("status", 0);
						resultjson.put("error_msg", "Aready Done!");
						return resultjson.toString();
				}
				
				
				BigDecimal  amountd =new BigDecimal (amount);
				String remarks="";
				String deposit_type ="";
				if("1".equals(deposit_mode)){
					remarks="银行卡直冲";
					deposit_type = "银行卡直冲";
				}
				if("2".equals(deposit_mode)){
					remarks="第三方支付充值";
					deposit_type = "第三方支付充值";
				}
				if("3".equals(deposit_mode)){
					remarks="移动钱包（二维码）充值";
					deposit_type="移动钱包（二维码）充值";
				}
				remarks=" 订单号:"+company_order_num;
				if(creditService.add(login_name, amountd,"自动充值", remarks, login_name, company_order_num)){
					//增加积分  存款红利
					try{
					
					Customer customer= custService.getCustomer(login_name);
					
					//添加存款记录和日志
					String dep_no = OrderNoService.createLocalNo("DE");
					depositService.addDeposit(dep_no, customer.cust_id, login_name, customer.real_name, amountd, bank_id, "", deposit_type, area, remarks);
					
					//查询是否第一次存款,如果是,升级用户等级
					if(depositService.NTgetCount(customer.cust_id) == 1){
						custService.NTmodCustlevelFirst(customer.cust_id, 1);
					}
					 
					 
					
					Date now =new Date(System.currentTimeMillis());
					
					if(customer.promo_flag != null && customer.promo_flag){
						
						
						//只有银行卡直冲送1%红利
//						if("1".equals(deposit_mode)){
//							
//							if((now.getTime()>DateUtil.stringToDate("2017-04-04", "yyyy-MM-dd").getTime()&&
//						    		  DateUtil.stringToDate("2017-05-01", "yyyy-MM-dd").getTime()>now.getTime())){
//								String orderNo = OrderNoService.createLocalNo("GI");
//								if(depositService.addGift(orderNo,customer.login_name, amountd, new BigDecimal( amountd.divide(new BigDecimal(100)).multiply(new BigDecimal(1.5)).intValue()), customer.create_date, customer.real_name, customer.cust_level)){
//									creditService.add(login_name, new BigDecimal( amountd.divide(new BigDecimal(100)).multiply(new BigDecimal(1.5)).intValue()),"礼金添加", "添加礼金转账红利", "system", orderNo);
//								}
//							}else{
//								String orderNo = OrderNoService.createLocalNo("GI");
//								if(depositService.addGift(orderNo,customer.login_name, amountd, new BigDecimal( amountd.divide(new BigDecimal(100)).intValue()), customer.create_date, customer.real_name, customer.cust_level)){
//									creditService.add(login_name, new BigDecimal( amountd.divide(new BigDecimal(100)).intValue()),"礼金添加", "添加礼金转账红利", "system", orderNo);
//								}
//							}
//							
//							
//						}
						
						ScoreService scoreService =new ScoreService(template,ds);
						if(scoreService.depositCountToday(login_name)==1){
							scoreService.modScore(company_order_num, "签到积分",  new BigDecimal(1), login_name, customer.cust_id, "");
						}
						scoreService.modScore(company_order_num, "存款积分",new BigDecimal( amountd.divide(new BigDecimal(100)).intValue()), login_name, customer.cust_id, "");
						
						
					}
					
					
					
					
					if(now.getTime()>DateUtil.stringToDate("2017-12-01", "yyyy-MM-dd").getTime() &&
				    		  DateUtil.stringToDate("2018-01-01", "yyyy-MM-dd").getTime()>now.getTime()){
				    	  //int dcount=depositService.NTgetCountToday(customer.cust_id);
				    	  //if(dcount<=5){
				    		  String giftCode=RandomUtil.getRandom(8);
								 CashGift gift =new CashGift();
					        	 gift.gift_code=giftCode;
					        	 gift.login_name=customer.login_name;
					        	 gift.deposit_credit=amountd;
					        	 gift.valid_credit=new BigDecimal(0);
					        	 gift.net_credit=new BigDecimal(0);
					        	 float rate = 0;
					        	 float f = 0.0f;
					        	 if(amountd.intValue()<5000){
					        		 gift.rate=Float.valueOf(1);
						        	 rate = 1;
						        	 f=amountd.floatValue()*1/100;
								 }else if(amountd.intValue() >= 5000 && amountd.intValue()<10000){
									 gift.rate=Float.valueOf(1.8f);
						        	 rate = 1.8f;
						        	 f=amountd.floatValue()*1.8f/100;
								 }else if(amountd.intValue() >= 10000 && amountd.intValue()<30000){
									 gift.rate=Float.valueOf(2.5f);
						        	 rate = 2.5f;
						        	 f=amountd.floatValue()*2.5f/100;
								 }else if(amountd.intValue() >= 30000 && amountd.intValue()<50000){
									 gift.rate=Float.valueOf(3.8f);
						        	 rate = 3.8f;
						        	 f=amountd.floatValue()*3.8f/100;
								 }else if(amountd.intValue() >= 50000 ){
									 gift.rate=Float.valueOf(5);
						        	 rate = 5;
						        	 f=amountd.floatValue()*5/100;
						        	 if(f>2888){
						        		 f=2888;
						        	 }
								 }

					        	 
					        	 gift.payout=new BigDecimal(f);
					        	 gift.gift_type="年终现金大回馈 ";
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
					        	 
				    	 // }
				    }
					
					}catch(Exception e){
						e.printStackTrace();
					}
					
					
					resultjson.put("status", 1);
					resultjson.put("error_msg", "");
					return resultjson.toString();
		
				}else{
					resultjson.put("status", 0);
					resultjson.put("error_msg", "Credit Change Failure!");
					return resultjson.toString();
				}
			}else{
				resultjson.put("status", 0);
				resultjson.put("error_msg", "Can't find username!");
				return resultjson.toString();
			}
		}
		resultjson.put("status", 0);
		resultjson.put("error_msg", "Key pair not success!");
		return resultjson.toString();
	}

	/**
     *充值异常提醒
	 * @param request
	 * @param response
	 * @return
	 */
	private String exceptionWithdrawApply(HttpServletRequest request,HttpServletResponse response){
		String exception_order_num=request.getParameter("exception_order_num");
		String company_id=request.getParameter("company_id");
		String exact_payment_bank=request.getParameter("exact_payment_bank");
		String pay_card_name=request.getParameter("pay_card_name");
		String pay_card_num=request.getParameter("pay_card_num");
		String receiving_bank=request.getParameter("receiving_bank");
		String receiving_account_name=request.getParameter("receiving_account_name");
		String channel=request.getParameter("channel");
		String note=request.getParameter("note");
		String area=request.getParameter("area");
		String exact_time=request.getParameter("exact_time");
		String amount=request.getParameter("amount");
		String fee=request.getParameter("fee");
		String transaction_charge=request.getParameter("transaction_charge");
		String key=request.getParameter("key");
		String base_info=request.getParameter("base_info");
		if(exception_order_num==null||"".equals(exception_order_num))return "";
		StringBuffer sb =new StringBuffer();
		sb.append(exception_order_num);
		sb.append(company_id);
		sb.append(exact_payment_bank);
		sb.append(pay_card_name);
		sb.append(pay_card_num);
		sb.append(receiving_bank);
		sb.append(receiving_account_name);
		sb.append(channel);
		sb.append(note);
		sb.append(area);
		sb.append(exact_time);
		sb.append(amount);
		sb.append(fee); 
		sb.append(transaction_charge);
		JSONObject resultjson =new JSONObject(); 
		resultjson.put("exception_order_num", exception_order_num);
		
		if(exception_order_num==null||"".equals(exception_order_num))return "";
		String product =PayResource.instance().findProductByCompany(company_id);
		String ds=PayResource.instance().getConfig("dpay."+product+".datasource");
		String keyconfig=PayResource.instance().getConfig("dpay."+product+".keyconfig");
		String key0=MD5Util.MD5(MD5Util.MD5(keyconfig).toLowerCase()+sb.toString()).toLowerCase();
		if(key0.equals(key)){
			if(pay_card_name.startsWith("\\u")){
				pay_card_name=convert(pay_card_name);
			}
			JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
			DPayService dPayService =new DPayService(template,ds);
			if(!dPayService.isHasFalseDeposit(exception_order_num)){
				long l=dPayService.notifyFalseDeposit(exception_order_num, company_id, exact_payment_bank, 
						pay_card_name, pay_card_num, receiving_bank, receiving_account_name, channel, 
						note, area, exact_time, amount, fee, transaction_charge, base_info);
				
				if(l!=0L){
					resultjson.put("status", 1);
					resultjson.put("error_msg", "");
					return resultjson.toString();
					
				}
			}
			resultjson.put("status", 0);
			resultjson.put("error_msg", "Aready Done!");
			return resultjson.toString();
		}
		resultjson.put("status", 0);
		resultjson.put("error_msg", "Key pair not success!");
		return resultjson.toString();
	}
	/**
	 * 提款确认
	 * @param request
	 * @param response
	 * @return
	 */
	private String requestWithdrawApproveInformation(HttpServletRequest request,
			HttpServletResponse response){
		String company_order_num=request.getParameter("company_order_num");
		String mownecum_order_num=request.getParameter("mownecum_order_num");
		String amount=request.getParameter("amount");
		String card_num=request.getParameter("card_num");
		String card_name=request.getParameter("card_name");
		String company_user=request.getParameter("company_user");
		String key=request.getParameter("key");
		StringBuffer sb =new StringBuffer();
		sb.append(company_order_num);
		sb.append(mownecum_order_num);
		sb.append(amount);
		sb.append(card_num);
		sb.append(card_name);
		sb.append(company_user);
		if(company_order_num==null||"".equals(company_order_num))return "";
		String product =PayResource.instance().findProduct(company_order_num.substring(0, 2));
		String ds=PayResource.instance().getConfig("dpay."+product+".datasource");
		String keyconfig=PayResource.instance().getConfig("dpay."+product+".keyconfig");
        String key0=MD5Util.MD5(MD5Util.MD5(keyconfig).toLowerCase()+sb.toString()).toLowerCase();
    	JSONObject resultjson =new JSONObject(); 
		resultjson.put("mownecum_order_num", mownecum_order_num);
		resultjson.put("company_order_num", company_order_num);
		
		if(key0.equals(key)){
			JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
			DPayService dPayService =new DPayService(template,ds);
			if(dPayService.confirmWithdraw(company_order_num, amount, card_num, card_name, company_user)){
				resultjson.put("status", 4);
				resultjson.put("error_msg", "");
				return resultjson.toString();
			}else{
				resultjson.put("status", 0);
				resultjson.put("error_msg", "");
				return resultjson.toString();
			}
		}else{
			resultjson.put("status", 5);
			resultjson.put("error_msg", "Key pair not success");
			return resultjson.toString();
		}	
		
	}
	/**
     * 提现处理结果
	 */
	private String withdrawalResult(HttpServletRequest request,
			HttpServletResponse response){
		String mownecum_order_num=request.getParameter("mownecum_order_num");
		String company_order_num=request.getParameter("company_order_num");
		String status=request.getParameter("status");
		String amount=request.getParameter("amount");
		String detail=request.getParameter("detail");
		String key=request.getParameter("key");
		String exact_transaction_charge=request.getParameter("exact_transaction_charge");
		
		StringBuffer sb =new StringBuffer();
		sb.append(mownecum_order_num);
		sb.append(company_order_num);
		sb.append(status);
		sb.append(amount);
		sb.append(exact_transaction_charge);
		if(company_order_num==null||"".equals(company_order_num))return "";
		String product =PayResource.instance().findProduct(company_order_num.substring(0, 2));
		String ds=PayResource.instance().getConfig("dpay."+product+".datasource");
		String keyconfig=PayResource.instance().getConfig("dpay."+product+".keyconfig");
		String key0=MD5Util.MD5(MD5Util.MD5(keyconfig).toLowerCase()+sb.toString()).toLowerCase();
		JSONObject resultjson =new JSONObject(); 
		resultjson.put("mownecum_order_num", mownecum_order_num);
		resultjson.put("company_order_num", company_order_num);
		
		
		if(key0.equals(key)){
			JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
			DPayService dPayService =new DPayService(template,ds);
			dPayService.payResultWithdraw(company_order_num, amount, detail, status,"", exact_transaction_charge);
			WithdrawService withdrawService =new WithdrawService(template,ds);
			int st=8;
			if("1".equals(status)){
				st=9;
			}
			if("2".equals(status)){
				st=10;
			}
			if("0".equals(status)){
				st=8;
			}
			withdrawService.updateWithdrawDpayResult(st, new BigDecimal(amount),new BigDecimal(exact_transaction_charge), "", company_order_num);
			resultjson.put("status", 1);
			resultjson.put("error_msg", "");
	
			return resultjson.toString();
		}
		resultjson.put("status", 0);
		resultjson.put("error_msg", "key pair no success");
		return resultjson.toString();
	}
	
	@RequestMapping(value="/dpay.do", produces="text/html;charset=UTF-8")  
	@ResponseBody  
    public void bs(HttpServletRequest request,HttpServletResponse response){
		StringBuffer sb=logRequest(request);
		try { 
			response.addHeader("ContentType", "type=text/html;charset=UTF-8");
			String type=request.getParameter("type");
			String result="Not Support!";
			//充值成功提醒
			if("addTransfer".equals(type)){
				result=addTransfer(request,response);
			}
			//充值异常提醒
			if("exceptionWithdrawApply".equals(type)){
				result=exceptionWithdrawApply(request,response);
			}
			//提款确认提醒
			if("requestWithdrawApproveInformation".equals(type)){
				result=requestWithdrawApproveInformation(request,response);
			}
			//提款结果提醒
			if("withdrawalResult".equals(type)){
				result=withdrawalResult(request,response);
			}
			sb.append(" Result:"+result);
			logger.info(sb.toString());
		    response.getWriter().write(result);  
	    } catch (Exception e) {   
	    	sb.append(" Result: Exception - "+e.getMessage());
			logger.info(sb.toString());
	        e.printStackTrace();  
	    }  
    }
	
	public static String convert(String unicodeStr){  
		
		try{
			StringBuffer sb = new StringBuffer();  
		    String str[] = unicodeStr.toUpperCase().split("U");  
		    for(int i=0;i<str.length;i++){  
		      if(str[i].equals("")) continue;  
		      char c = (char)Integer.parseInt(str[i].trim(),16);  
		      sb.append(c);  
		    }  
		    return sb.toString();  

		}catch(Exception e){
			return unicodeStr;
		}
	}  
	
	public  static void main(String [] arg){
		String s="\u5c0f\u5c0f\u725b";
		System.out.println(s);
		System.out.println(convert(s));
		StringBuffer sb =new StringBuffer();
	}
	
}
