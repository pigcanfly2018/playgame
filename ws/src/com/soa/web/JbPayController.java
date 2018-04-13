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

import bsz.exch.bank.JbPayResource;
import bsz.exch.bank.PayResource;
import bsz.exch.bean.LogInfo;
import bsz.exch.core.JdbcResource;
import bsz.exch.jubao.JubaoPay;
import bsz.exch.jubao.RSA;

@Controller
public class JbPayController {
	
    public static final ThreadLocal<LogInfo>  LOCAL_LOG  = new ThreadLocal<LogInfo>();
    
	private static Logger logger=Logger.getLogger(JbPayController.class);
	
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

	@RequestMapping(value="/jbpay.do", produces="text/html;charset=UTF-8")  
	@ResponseBody  
    public void bs(HttpServletRequest request,HttpServletResponse response){
		    StringBuffer sb=logRequest(request);
			response.addHeader("ContentType", "type=text/html;charset=UTF-8");
			try{
			    String message = request.getParameter("message");
			    String signature = request.getParameter("signature");
				RSA.intialize();
				JubaoPay jubaopay = new JubaoPay();
				boolean result = jubaopay.decrypt(message, signature);
				jubaopay.getEncrypt("payid");
				jubaopay.getEncrypt("mobile");
				jubaopay.getEncrypt("realReceive");
				jubaopay.getEncrypt("remark");
				jubaopay.getEncrypt("orderNo");
				jubaopay.getEncrypt("state");
				jubaopay.getEncrypt("modifyTime");
				if(result){
					BigDecimal  real_amount =new BigDecimal(jubaopay.getEncrypt("realReceive"));
					String payid=jubaopay.getEncrypt("payid");
					String orderNo=jubaopay.getEncrypt("orderNo");
					String state =jubaopay.getEncrypt("state");
					String modifyTime =jubaopay.getEncrypt("modifyTime");	
					String remark =jubaopay.getEncrypt("remark");
					String payMethodType =jubaopay.getEncrypt("payMethodType");
					String payMethod =jubaopay.getEncrypt("payMethod");
					BigDecimal amount =new BigDecimal(jubaopay.getEncrypt("amount"));
					sb.append(" Encrypt:{payid :\""+payid+"\",");
					sb.append("orderNo :\""+orderNo+"\",");
					sb.append("state :\""+state+"\",");
					sb.append("modifyTime :\""+modifyTime+"\",");
					sb.append("realReceive :\""+real_amount+"\",");
					sb.append("remark :\""+remark+"\",");
					sb.append("payMethodType :\""+payMethodType+"\",");
					sb.append("payMethod :\""+payMethod+"\",");
					sb.append("amount :\""+amount+"\"} ");
					//判断是否已处理
					String product =JbPayResource.instance().findProduct(payid.substring(0, 2));
					String ds=JbPayResource.instance().getConfig("jbpay."+product+".datasource");
					
					JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
					JbpService jdpService =new JbpService(template,ds);
					CreditService creditService =new CreditService(template,ds);
					CustomerService custService =new CustomerService(template,ds);
					DepositService depositService =new DepositService(template,ds);
					
					//没有处理
					if(jdpService.isNotDoJdp(payid)){
						jdpService.updaeteJdp(payid, orderNo, state, modifyTime, payMethod, real_amount);
						String login_name=jdpService.queryLoginName(payid);
				
						//支付成功
						if("2".equals(state)){
							if(creditService.add(login_name, amount,"自动充值", payMethod+":"+modifyTime, login_name, payid)){
								  try{
										Customer customer= custService.getCustomer(login_name);
										//添加存款记录和日志
										String dep_no = OrderNoService.createLocalNo("DE");
										depositService.addDeposit2(dep_no, customer.cust_id, login_name, customer.real_name, amount, payMethod, "", payMethod, "", payid);
										//查询是否第一次存款,如果是,升级用户等级
										if(depositService.NTgetCount(customer.cust_id) == 1){
											custService.NTmodCustlevelFirst(customer.cust_id, 1);
										}
										ScoreService scoreService =new ScoreService(template,ds);
										if(scoreService.depositCountToday(login_name)==1){
											scoreService.modScore(payid, "签到积分",  new BigDecimal(1), login_name, customer.cust_id, "");
										}
										scoreService.modScore(payid, "存款积分",new BigDecimal( amount.divide(new BigDecimal(100)).intValue()), login_name, customer.cust_id, "");
										response.getWriter().write("success");
										response.getWriter().flush();
										sb.append(" Result: OK ");
									}catch(Exception e){
										sb.append(" Result: Exception - "+e.getMessage());
										e.printStackTrace();
										response.getWriter().write("failed");
										response.getWriter().flush();
									}
							}
						}else{
							response.getWriter().write("failed");
							response.getWriter().flush();
						}
					}else{
						response.getWriter().write("failed");
						response.getWriter().flush();
					}
				} else {
					// 签名验证失败
					response.getWriter().write("failed");
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
