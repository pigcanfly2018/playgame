package util;

public class PayBank {
	
	public static String getBankName(String bank_code){
		if(bank_code==null)return "";
		if("ABC".equals(bank_code)){
			return "农业银行";
		}
		if("ICBC".equals(bank_code)){
			return "工商银行";
		}
		if("CCB".equals(bank_code)){
			return "建设银行";
		}
		if("BCOM".equals(bank_code)){
			return "交通银行";
		}
		if("BOC".equals(bank_code)){
			return "中国银行";
		}
		if("CMB".equals(bank_code)){
			return "招商银行";
		}
		if("CMBC".equals(bank_code)){
			return "民生银行";
		}
		if("CEBB".equals(bank_code)){
			return "光大银行";
		}
		if("CIB".equals(bank_code)){
			return "兴业银行";
		}
		if("PSBC".equals(bank_code)){
			return "邮储银行";
		}
		if("SPABANK".equals(bank_code)){
			return "平安银行";
		}
		if("ECITIC".equals(bank_code)){
			return "中信银行";
		}
		if("GDB".equals(bank_code)){
			return "广发银行";
		}
		if("HXB".equals(bank_code)){
			return "华夏银行";
		}
		if("SPDB".equals(bank_code)){
			return "浦发银行";
		}
		if("BEA".equals(bank_code)){
			return "东亚银行";
		}
		if("NBB".equals(bank_code)){
			return "宁波银行";
		}
		if("SHB".equals(bank_code)){
			return "上海银行";
		}
		if("HZB".equals(bank_code)){
			return "杭州银行";
		}
		if("BOB".equals(bank_code)){
			return "北京银行";
		}
		return "";
		
	}
	
	
	public static String getPointCardName(String bank_code){
		if(bank_code==null)return "";
		if("YDSZX".equals(bank_code)){
			return "移动神州行";
		}
		if("DXGK".equals(bank_code)){
			return "电信国卡";
		}
		if("QBCZK".equals(bank_code)){
			return "Q币充值卡";
		}
		if("LTYKT".equals(bank_code)){
			return "联通一卡通";
		}
		if("JWYKT".equals(bank_code)){
			return "骏网一卡通";
		}
		if("SDYKT".equals(bank_code)){
			return "盛大一卡通";
		}
		if("WMYKT".equals(bank_code)){
			return "完美一卡通";
		}
		if("ZTYKT".equals(bank_code)){
			return "征途一卡通";
		}
		if("WYYKT".equals(bank_code)){
			return "网易一卡通";
		}
		if("SHYKT".equals(bank_code)){
			return "搜狐一卡通";
		}
		if("JYYKT".equals(bank_code)){
			return "九游一卡通";
		}
		if("THYKT".equals(bank_code)){
			return "天宏一卡通";
		}
		if("TXYKT".equals(bank_code)){
			return "天下一卡通";
		}
		if("ZYYKT".equals(bank_code)){
			return "纵游一卡通";
		}
		if("TXYKTZX".equals(bank_code)){
			return "天下一卡通专项";
		}
		if("SFYKT".equals(bank_code)){
			return "盛付通一卡通";
		}
		return "";	
	}

}
