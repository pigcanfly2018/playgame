package com.product.bda.handler;

import org.springframework.jdbc.core.JdbcTemplate;

import com.product.bda.service.JbpService;

import bsz.exch.bank.JbPayResource;
import bsz.exch.bank.PayResource;
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

@Handler(name="JBP")
public class JuBaoPayHandler {
	
	
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
		String pre=JbPayResource.instance().getConfig("jbpay."+product+".pre");
		String payid = pre+"_" + String.valueOf(System.currentTimeMillis());
		String partnerid =JbPayResource.instance().getConfig("jbpay."+product+".partnerid"); 
		String amount = task.getParam("amount");
		String payerName = payid;
		String goodsName = "衣服";
		String returnURL = task.getParam("return_url");
		String callBackURL = "";
		String remark = task.getParam("remark");
		String payMethod = "WANGYIN";
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		String ds=inter.getDataSource();
		JbpService jbpService = new JbpService(template,ds);
		jbpService.createJbp(payid, task.getParam("login_name"), amount, goodsName, partnerid, payMethod, remark,returnURL);
		try{
			RSA.intialize();
				JubaoPay jubaoPay = new JubaoPay();
				jubaoPay.setEncrypt("payid",payid);
				jubaoPay.setEncrypt("partnerid",partnerid);
				jubaoPay.setEncrypt("amount",amount);
				jubaoPay.setEncrypt("payerName",payerName);
				jubaoPay.setEncrypt("goodsName",goodsName);
				jubaoPay.setEncrypt("returnURL","");
				jubaoPay.setEncrypt("callBackURL",callBackURL);
				jubaoPay.setEncrypt("payMethod",payMethod);
				jubaoPay.setEncrypt("remark", remark);
				jubaoPay.interpret();         
			    String message = jubaoPay.getMessage();
			    String signature = jubaoPay.getSignature();
			    Result r =Result.create(task.getId(), task.getFunId());
				r.addFields(new String[]{"ok","pay_id","message","amount","signature"});
				r.setFlag("-1");
				r.setIsList(true);
				r.setLength(1);
				r.addRecord(new String[]{"1",payid,message,amount,signature});
			return r;
		 }catch(Exception e){
			e.printStackTrace();
		 }
		 Result r =Result.create(task.getId(), task.getFunId());
		 r.addFields(new String[]{"ok","payid","message","amount","signature"});
		 r.setFlag("-1");
		 r.setIsList(true);
		 r.setLength(1);
		 r.addRecord(new String[]{"0",payid,"无法创建订单!",amount,""});
		 return r;
   }
	
	

}
