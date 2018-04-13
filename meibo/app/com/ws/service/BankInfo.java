package com.ws.service;

import java.util.HashMap;
import java.util.Map;




public class BankInfo {
	
	private String bank_name;
	
	private String code;
	
	private String abbr;
	
	private String types;
	
	private String url;
	
	
	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAbbr() {
		return abbr;
	}

	public void setAbbr(String abbr) {
		this.abbr = abbr;
	}

	public String getTypes() {
		return types;
	}

	public void setTypes(String types) {
		this.types = types;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public BankInfo(String bank_name,String code,String abbr,String types){
		this.bank_name=bank_name;
		this.code=code;
		this.abbr=abbr;
		this.types=types;
	}
	
	public BankInfo(String bank_name,String code,String abbr,String types,String url){
		this.bank_name=bank_name;
		this.code=code;
		this.abbr=abbr;
		this.types=types;
		this.url=url;
	}
	
	private static Map<String,BankInfo> banks=new HashMap<String,BankInfo>();
	
	private static Map<String,String> link=new HashMap<String,String>();

	static {
		banks.put("1", new BankInfo("中国工商银行","1","ICBC","Bank","http://www.icbc.com.cn/"));
		banks.put("2", new BankInfo("招商银行","2","CMB","Bank","http://www.cmbchina.com/"));
		banks.put("3", new BankInfo("中国建设银行","3","CCB","Bank","http://www.ccb.com/"));
		banks.put("4", new BankInfo("中国农业银行","4","ABC","Bank","http://www.abchina.com/"));
		banks.put("5", new BankInfo("中国银行","5","BOC","Bank","http://www.boc.cn/"));
		banks.put("6", new BankInfo("交通银行","6","BCM","Bank","http://www.bankcomm.com/"));
		banks.put("7", new BankInfo("中国民生银行","7","CMBC","Bank","http://www.cmbc.com.cn/"));
		banks.put("8", new BankInfo("中信银行","8","ECC","Bank","http://bank.ecitic.com/"));
		banks.put("9", new BankInfo("上海浦东发展银行","9","SPDB","Bank","http://www.spdb.com.cn/"));
		banks.put("10", new BankInfo("邮政储汇","10","PSBC","Bank","http://www.psbc.com/"));
		banks.put("11", new BankInfo("中国光大银行","11","CEB","Bank","http://www.cebbank.com/"));
		banks.put("12", new BankInfo("平安银行","12","PINGAN","Bank","http://bank.pingan.com/"));
		banks.put("13", new BankInfo("广发银行","13","CGB","Bank","http://www.cgbchina.com.cn/"));
		banks.put("14", new BankInfo("华夏银行","14","HXB","Bank","http://www.hxb.com.cn/"));
		banks.put("15", new BankInfo("兴业银行","15","CIB","Bank","http://www.cib.com.cn/"));
		banks.put("30", new BankInfo("支付宝(二维码)","30","ALIPAYQR","E-wallet","https://www.alipay.com/"));
		banks.put("31", new BankInfo("财付通","31","TENPAY","E-wallet","http://www.tenpay.com/"));
		banks.put("40", new BankInfo("微信支付(二维码)","40","WECHAT","E-wallet","http://pay.weixin.qq.com/"));
		banks.put("41", new BankInfo("支付宝(网页)","41","ALIPAYWEB","E-wallet","https://www.alipay.com/"));	
	}
	
	public static BankInfo getBank(String code){
		  if(banks.containsKey(code)){
			  return banks.get(code);
		  }
		  return null;         
	} 
	
	public static void main(String []args){
		System.out.println(BankInfo.getBank("3").abbr);
	}

}
