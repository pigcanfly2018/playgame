package com.product.bda.handler;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.xml.sax.InputSource;

import com.itrus.util.sign.RSAWithSoftware;
import com.product.bda.service.OrderNumberService;

import bsz.exch.bank.YinbaoPayResource;
import bsz.exch.bean.FobbinInfo;
import bsz.exch.bean.Result;
import bsz.exch.bean.Task;
import bsz.exch.core.Before;
import bsz.exch.core.Handler;
import bsz.exch.core.InterFace;
import bsz.exch.core.JdbcResource;
import bsz.exch.core.Params;
import bsz.exch.core.Service;
import bsz.exch.utils.HttpClientUtil;


@Handler(name="DINP")
public class DinPayHandler {

	@Before
	public FobbinInfo before(Task task,InterFace inter){
		String product=task.getParam("product");
		if(!"8da".equals(product)){
			return  new FobbinInfo(true,"No product support!");
		}
		return new FobbinInfo(false,"");
	}
	
	@Service(name="pay")
	@Params(validateField={"merchant_code","order_amount","order_no"})
    public Result pay(Task task,InterFace inter){
		
		
		// 交易请求地址
		String reqUrl = "https://api.dinpay.com/gateway/api/weixin";
		
		// 支付返回结果
		String result = null;
		
		// 二维码信息
		String sQrcode = null;
		
		String RSA_S_PrivateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAK/EBi8GR62kcxYk"
				+ "jbdz8QEHUHQtbPBkDrif/h/ynjA5hsAzvE6hvnZJoBJGzc6H1BLgutgHy893sBUN"
				+ "kuehHQsS7SDOwn2qt5rw/P/HQMVzkWo17LQjV4X4rnhjn+9e+IdIQiZJTG7zqY/G"
				+ "Ge8qaJ+hpDIL27sknv623jX8RhplAgMBAAECgYBbdu6g2evC+KjLBQqaRMNNfbjo"
				+ "PBtMnUTr3pYnmeGnxsfQjRuY76Jle7rIx/+hUh2SFVAlyyjMNcRzsDSaRMUPeWxa"
				+ "56sORvRchtUCe4mGf96XTW1tjpVeOHD7lZer1QvCPfoVJWY9yz/hogwXrHJlMmsB"
				+ "qbOxMCA+0IA1OOvxAQJBANR3dOwWJSYohLwtsRDHA6Ffkzdizlqi272wPTz4Oj7n"
				+ "9am9KYIeBabu3Gl4otb0q1CHfymnvndyfJrS507Mkj0CQQDTx31BVHIVrxFwMHYK"
				+ "4MpWgp2OBshUH6QPwwzNRhzrSE1T18Zva6PWvs67G7r/Prx+Jjdh1STSg+QrEH0m"
				+ "K3NJAkBOAD0Qn2CsDZwNacjjm+ydRfVKFCAl7jw1+4m/5HGdvpLV6fhfKJ5ylqac"
				+ "oglGWTS7r8LjjA3E1Gcif+Mjbr8RAkA6MfJodmUXMlFva9G0MYtIdIIJGjIIc91o"
				+ "e2fULLUbrnWZWYzs5z5Rb4NthTRAmaPAYo4Lcz+4HYggkVpil8QJAkApI9efgDPC"
				+ "/6WFFUhnFtkf9x9+CWeo5I/QlJv41o41JzLeN0fW67qbnFgIlap7RKMaMxyInCSH"
				+ "vHgRxyq05AZr";
		
		String interface_version = task.getParam("interface_version");  
		String merchant_code = task.getParam("merchant_code");  
		String notify_url = task.getParam("notify_url"); 
		String order_amount = task.getParam("order_amount");  
		String order_time = task.getParam("order_time"); 
		String order_no = task.getParam("order_no");  
		String product_name = task.getParam("product_name");  
		
		Map<String, String> reqMap = new HashMap<String, String>();
		reqMap.put("merchant_code", merchant_code);
		reqMap.put("service_type", "wxpay");
		reqMap.put("notify_url", notify_url);
		reqMap.put("interface_version", interface_version);
		reqMap.put("sign_type", "RSA-S");
		reqMap.put("order_no", order_no);
		reqMap.put("order_time", order_time);
		reqMap.put("order_amount", order_amount);
		reqMap.put("product_name", product_name);
		
		StringBuffer signSrc= new StringBuffer();	
		signSrc.append("interface_version=").append(interface_version).append("&");
		signSrc.append("merchant_code=").append(merchant_code).append("&");				
		signSrc.append("notify_url=").append(notify_url).append("&");	
		signSrc.append("order_amount=").append(order_amount).append("&");
		signSrc.append("order_no=").append(order_no).append("&");
		signSrc.append("order_time=").append(order_time).append("&");
		signSrc.append("product_name=").append(product_name).append("&");
		signSrc.append("service_type=").append("wxpay");	
		
		String signInfo = signSrc.toString();		
		String sign = "" ;
		
		try{
			sign = RSAWithSoftware.signByPrivateKey(signInfo,RSA_S_PrivateKey);  // 商家签名（签名后报文发往dinpay）				
			reqMap.put("sign", sign);				
			result= new HttpClientUtil().doPost(reqUrl, reqMap, "utf-8");		 // 向智付发送POST支付请求
		}catch(Exception e){
			e.printStackTrace();
		}
		
		System.out.println("签名排序：" + signInfo.length() + " --> " + signInfo);
		System.out.println("sign值：" + sign.length() + " --> " + sign);
		System.out.println("result值："+result);
		
		// 解析result，获取二维码节点qrcode的值
        StringReader read = new StringReader(result);
        InputSource source = new InputSource(read);
        SAXBuilder sb = new SAXBuilder();
        try {     	
            Document doc = sb.build(source);
            Element root = doc.getRootElement();
            List points = root.getChildren();
            Element et = (Element) points.get(0);                             
            if("SUCCESS".equals(et.getChild("resp_code").getText())){
            	
            	Element ee = et.getChild("trade");
            	sQrcode = (String) ee.getChild("qrcode").getText();
                System.out.println("sQrcode值："+sQrcode); 
                
                Result r =Result.create(task.getId(), task.getFunId());
				r.addFields(new String[]{"ok","order_no","message","order_amount","sQrcode"});
				r.setFlag("-1");
				r.setIsList(true);
				r.setLength(1);
				r.addRecord(new String[]{"1",order_no,"",order_amount,sQrcode});
				 return r;
            } 
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
		
		Result r =Result.create(task.getId(), task.getFunId());
		 r.addFields(new String[]{"ok","order_no","message","order_amount"});
		 r.setFlag("-1");
		 r.setIsList(true);
		 r.setLength(1);
		 r.addRecord(new String[]{"0",order_no,"无法创建订单!",order_amount});
		 return r;
		 
	}
	
	@Service(name="notify")
	@Params(validateField={"merchant_code","order_amount","order_no","order_time","trade_no","trade_status","trade_time","sign"})
	public Result notify(Task task,InterFace inter){
		
		String RSA_S_PublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCwQdX6rHQIydSBntY17KkzBnu4BLi0yqBsV5h4lDTOayhdlUqmrWQaAFaTMkHsUbAXVKKnBFAI9ilYfkLoL3wSczADFdhSp79NjSM/MPx9qHEw8nnftPw72P6lhydZdN1i2kJN8hrCA5MYOJbgsWF4UGlBJLzccq95SV3ihuzepQIDAQAB";
		String bank_seq_no = task.getParam("bank_seq_no");  
		String extra_return_param = task.getParam("extra_return_param");  
		String merchant_code = task.getParam("merchant_code"); 
		String notify_id = task.getParam("notify_id");  
		String notify_type = task.getParam("notify_type"); 
		String order_amount = task.getParam("order_amount");  
		String order_no = task.getParam("order_no");  
		
		String product = task.getParam("product");
		
		String order_time = task.getParam("order_time");  
		String trade_no = task.getParam("trade_no");  
		String trade_status = task.getParam("trade_status");  
		String trade_time = task.getParam("trade_time");  
		String dinpaySign = task.getParam("sign");  
		
		StringBuilder signStr = new StringBuilder();
	 	if(null != bank_seq_no && !bank_seq_no.equals("")) {
	 		signStr.append("bank_seq_no=").append(bank_seq_no).append("&");
	 	}
	 	if(null != extra_return_param && !extra_return_param.equals("")) {
	 		signStr.append("extra_return_param=").append(extra_return_param).append("&");
	 	}
	 	signStr.append("interface_version=V3.0").append("&");
	 	signStr.append("merchant_code=").append(merchant_code).append("&"); 	
	 	signStr.append("notify_id=").append(notify_id).append("&");	 	
	 	signStr.append("notify_type=").append(notify_type).append("&"); 	
	 	signStr.append("order_amount=").append(order_amount).append("&");
	 	signStr.append("order_no=").append(order_no).append("&");
	 	signStr.append("order_time=").append(order_time).append("&");
	 	signStr.append("trade_no=").append(trade_no).append("&");	
	 	signStr.append("trade_status=").append(trade_status).append("&");
		signStr.append("trade_time=").append(trade_time);
	 		
		String signInfo =signStr.toString();
		boolean result = false;
				
		try{
			result=RSAWithSoftware.validateSignByPublicKey(signInfo, RSA_S_PublicKey, dinpaySign);
			
			if(result){
				String ds=YinbaoPayResource.instance().getConfig("yinbaopay."+product+".datasource");
				JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
				OrderNumberService orderNumberService =new OrderNumberService(template,ds);
				orderNumberService.createOrderNumber(trade_no);
				Result r =Result.create(task.getId(), task.getFunId());
				r.addFields(new String[]{"ok"});
				r.setFlag("-1");
				r.setIsList(true);
				r.setLength(1);
				r.addRecord(new String[]{"1"});
				return r;
			}
		}catch(Exception e){
			e.printStackTrace();
			Result r =Result.create(task.getId(), task.getFunId());
			 r.addFields(new String[]{"ok"});
			 r.setFlag("-1");
			 r.setIsList(true);
			 r.setLength(1);
			 r.addRecord(new String[]{"0"});
			 return r;
		}		
			// 验签  signInfo签名字符串，RSA_S_PublicKey智付公钥，dinpaySign智付返回的签名数据
		
		
		
		Result r =Result.create(task.getId(), task.getFunId());
		 r.addFields(new String[]{"ok"});
		 r.setFlag("-1");
		 r.setIsList(true);
		 r.setLength(1);
		 r.addRecord(new String[]{"0"});
		 return r;
		 
	}
	
	public static void main(String[] aregs){
		
		String RSA_S_PublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCwQdX6rHQIydSBntY17KkzBnu4BLi0yqBsV5h4lDTOayhdlUqmrWQaAFaTMkHsUbAXVKKnBFAI9ilYfkLoL3wSczADFdhSp79NjSM/MPx9qHEw8nnftPw72P6lhydZdN1i2kJN8hrCA5MYOJbgsWF4UGlBJLzccq95SV3ihuzepQIDAQAB";
		String bank_seq_no = "100570014697201610034077136112";  
		String extra_return_param =""; 
		String merchant_code = "2000282083"; 
		String notify_id = "f1ecd941349c42cc9daf91d14b78b3d8";  
		String notify_type = "offline_notify"; 
		String order_amount ="250"; 
		String order_no = "YF1485733139833405"; 
		
		String order_time = "2016-10-03 21:46:23";  
		String trade_no = "1272543732";   
		String trade_status = "SUCCESS";   
		String trade_time = "2016-10-03 21:46:41";  
		String dinpaySign = "RnqwY5w5Xbc7ZXiBiEcS3yQjntZ5Nq2G3u5E9EGCanXy/j5yWEaRhRAGh+sLIwULpv6uUmeLaEI38nUe3Wu+sJxaDW+oJ1aoPKypCGo73qJS4FLG7mwtUMDpFOLMPCUVWPcG77U34JRgo9k/SzmeVPA537P6AG1hy4Odm7iiLUA=";  
		
		

	 	StringBuilder signStr = new StringBuilder();
	 	if(null != bank_seq_no && !bank_seq_no.equals("")) {
	 		signStr.append("bank_seq_no=").append(bank_seq_no).append("&");
	 	}
	 	if(null != extra_return_param && !extra_return_param.equals("")) {
	 		signStr.append("extra_return_param=").append(extra_return_param).append("&");
	 	}
	 	signStr.append("interface_version=V3.0").append("&");
	 	signStr.append("merchant_code=").append(merchant_code).append("&"); 	
	 	signStr.append("notify_id=").append(notify_id).append("&");	 	
	 	signStr.append("notify_type=").append(notify_type).append("&"); 	
	 	signStr.append("order_amount=").append(order_amount).append("&");
	 	signStr.append("order_no=").append(order_no).append("&");
	 	signStr.append("order_time=").append(order_time).append("&");
	 	signStr.append("trade_no=").append(trade_no).append("&");	
	 	signStr.append("trade_status=").append(trade_status).append("&");
		signStr.append("trade_time=").append(trade_time);
	 		
		String signInfo =signStr.toString();
		boolean result = false;
		try {
			result=RSAWithSoftware.validateSignByPublicKey(signInfo, RSA_S_PublicKey, dinpaySign);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(signInfo);
		System.out.println(result);
	}
}
