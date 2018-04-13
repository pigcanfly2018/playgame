package util;

import play.Play;

public class DinpayRandom {
	
	private DinpayRandom(){
		
	}
	private static DinpayRandom random;
	public static DinpayRandom get(){
		if(random==null)random=new DinpayRandom();
		return random;
	}
	public DinpayBean random(){
		DinpayBean bean =new DinpayBean();
		bean.merId=Play.configuration.getProperty("dinpay.merId");
		bean.notify_url=Play.configuration.getProperty("dinpay.notifyUrl");
		bean.weixinnotify_url=Play.configuration.getProperty("dinpay.weixinnotifyUrl");
		bean.md5key=Play.configuration.getProperty("dinpay.md5key");
		bean.account_name=Play.configuration.getProperty("dinpay.account");
		bean.submit_url=Play.configuration.getProperty("dinpay.submitUrl");
		bean.weixinsubmitUrl=Play.configuration.getProperty("dinpay.weixinsubmitUrl");
	    return bean;
	    
	}
	
	public DinpayBean find(String merId){
		if(merId.equals(Play.configuration.getProperty("dinpay.merId"))){
			DinpayBean bean =new DinpayBean();
			bean.merId=Play.configuration.getProperty("dinpay.merId");
			bean.notify_url=Play.configuration.getProperty("dinpay.notifyUrl");
			bean.weixinnotify_url=Play.configuration.getProperty("dinpay.weixinnotifyUrl");
			bean.md5key=Play.configuration.getProperty("dinpay.md5key");
			bean.account_name=Play.configuration.getProperty("dinpay.account");
			bean.submit_url=Play.configuration.getProperty("dinpay.submitUrl");
		    return bean;
		}
		if(merId.equals(Play.configuration.getProperty("dinpay2.merId"))){
			DinpayBean bean =new DinpayBean();
			bean.merId=Play.configuration.getProperty("dinpay2.merId");
			bean.notify_url=Play.configuration.getProperty("dinpay2.notifyUrl");
			bean.weixinnotify_url=Play.configuration.getProperty("dinpay2.weixinnotifyUrl");
			bean.md5key=Play.configuration.getProperty("dinpay2.md5key");
			bean.account_name=Play.configuration.getProperty("dinpay2.account");
			bean.submit_url=Play.configuration.getProperty("dinpay2.submitUrl");
		    return bean;
		}
		return null;
	}
	

}
