package com.soa.web;

import java.math.BigDecimal;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.product.bda.service.CreditService;
import com.product.bda.service.Customer;
import com.product.bda.service.CustomerService;
import com.product.bda.service.DepositService;
import com.product.bda.service.JbpService;
import com.product.bda.service.OrderNoService;
import com.product.bda.service.ScoreService;
import com.product.bda.service.YbpService;

import bsz.exch.bank.YbPayResource;
import bsz.exch.bean.LogInfo;
import bsz.exch.core.JdbcResource;
import bsz.exch.jubao.JubaoPay;
import bsz.exch.jubao.RSA;
import bsz.exch.utils.MD5Util;

@Controller
public class YbPayController {

	
public static final ThreadLocal<LogInfo>  LOCAL_LOG  = new ThreadLocal<LogInfo>();
    
	private static Logger logger=Logger.getLogger(YbPayController.class);
	
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
	
	
	@RequestMapping(value="/ybpay.do", produces="text/html;charset=UTF-8")  
	@ResponseBody  
    public void bs(HttpServletRequest request,HttpServletResponse response){
		    StringBuffer sb=logRequest(request);
			response.addHeader("ContentType", "type=text/html;charset=UTF-8");
			try{
			    String notify_id = request.getParameter("notify_id");
			    String sign_value = request.getParameter("sign_value");
			    String notify_time = request.getParameter("notify_time");
			    String goods_name = request.getParameter("goods_name");
			    String trade_create_time = request.getParameter("trade_create_time");
			    String trade_pay_time = request.getParameter("trade_pay_time");
			    String trade_code = request.getParameter("trade_code");
			    String trade_id = request.getParameter("trade_id");
			    String order_id = request.getParameter("order_id");
			    String channel = request.getParameter("channel");
			    
			    BigDecimal total_fee = new BigDecimal(request.getParameter("total_fee"));
			    
			    
				
				if(trade_code.equals("success")){
					
					//判断是否已处理
					String product =YbPayResource.instance().findProduct(order_id.substring(0, 2));
					String ds=YbPayResource.instance().getConfig("ybpay."+product+".datasource");
					
					String mer_key =YbPayResource.instance().getConfig("ybpay."+product+".mer_key"); 
					String prestr ="channel="+channel+"&goods_name="+goods_name +"&notify_id"+notify_id+"&notify_time"+notify_time
			    			+"&order_id="+order_id+"&total_fee"+total_fee+"&trade_code="+trade_code+"&trade_create_time="+trade_create_time
			    			+"&trade_id="+trade_id+"&trade_pay_time="+trade_pay_time+mer_key;
			    
					String aftersign_value =  MD5Util.md5Encode(prestr);
					
					JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
					YbpService ybpService =new YbpService(template,ds);
					CreditService creditService =new CreditService(template,ds);
					CustomerService custService =new CustomerService(template,ds);
					DepositService depositService =new DepositService(template,ds);
					
					if(aftersign_value.equals(sign_value)){
						//没有处理
						if(ybpService.isNotDoYdp(order_id,"")){
							ybpService.updaeteYdp(order_id, trade_id, "2", total_fee,channel);
							String login_name=ybpService.queryLoginName(order_id);
					
							//支付成功
							
								if(creditService.add(login_name, total_fee,"自动充值", "盈宝支付:"+trade_pay_time, login_name, order_id)){
									  try{
											Customer customer= custService.getCustomer(login_name);
											//添加存款记录和日志
											String dep_no = OrderNoService.createLocalNo("DE");
											depositService.addDeposit2(dep_no, customer.cust_id, login_name, customer.real_name, total_fee, "盈宝支付", "", "盈宝支付", "", order_id);
											//查询是否第一次存款,如果是,升级用户等级
											if(depositService.NTgetCount(customer.cust_id) == 1){
												custService.NTmodCustlevelFirst(customer.cust_id, 1);
											}
											ScoreService scoreService =new ScoreService(template,ds);
											if(scoreService.depositCountToday(login_name)==1){
												scoreService.modScore(order_id, "签到积分",  new BigDecimal(1), login_name, customer.cust_id, "");
											}
											scoreService.modScore(order_id, "存款积分",new BigDecimal( total_fee.divide(new BigDecimal(100)).intValue()), login_name, customer.cust_id, "");
											response.getWriter().write("success");
											response.getWriter().flush();
											sb.append(" Result: OK ");
										}catch(Exception e){
											sb.append(" Result: Exception - "+e.getMessage());
											e.printStackTrace();
											response.getWriter().write("fail");
											response.getWriter().flush();
										}
								}
							
						}else{
							response.getWriter().write("fail");
							response.getWriter().flush();
						}
					}else{
						response.getWriter().write("fail");
						response.getWriter().flush();
					}
					
				} else {
					// 签名验证失败
					response.getWriter().write("fail");
					response.getWriter().flush();
				}
				logger.info(sb.toString());	
			}catch(Exception e){
				sb.append(" Result: Exception - "+e.getMessage());
				logger.info(sb.toString());
		        e.printStackTrace();  
			}
    }
	
}
