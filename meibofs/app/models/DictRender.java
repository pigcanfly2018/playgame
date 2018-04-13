package models;

import java.util.Date;
import java.util.List;

import util.DateUtil;

public class DictRender {
	static List<Item> items;
//	public String dictName(String groupcode,String value){
//		if(value==null){return "";}
//		if(items==null)items=Item.NTgetAll();
//		for(Item item:items){
//		  if(item.groupcode.equals(groupcode)){
//			  if(item.itemvalue.equals(value.toString())){
//				  return item.itemname;
//			  }
//		  }
//		}
//		return value.toString();
//	}
	
	public static String withdrawToEn(String n,String v){
		if("5".equals(v)){
			return n+" / Successful";
		}
		if("4".equals(v)){
			return n+" / Reject";
		}
		if("3".equals(v)){
			return n+" / Approved";
		}
		if("2".equals(v)){
			return n+" / Cancle";
		}
		if("1".equals(v)){
			return n+" / Pending";
		}
		return "";
	}
	
	public static String bankToEn(String n,String v){
		if("建行".equals(v)){
			return n+" / CCB";
		}
		if("农行".equals(v)){
			return n+" / ABC";
		}
		if("工行".equals(v)){
			return n+" / ICBC";
		}
		if("中行".equals(v)){
			return n+" / BOC";
		}
		if("民生".equals(v)){
			return n+" / CMBC";
		}
		
		if("招行".equals(v)){
			return n+" / CMB";
		}
		if("兴业".equals(v)){
			return n+" / CIB";
		}
		if("北京银行".equals(v)){
			return n+" / BOB";
		}
		if("光大".equals(v)){
			return n+" / CEB";
		}
		if("交行".equals(v)){
			return n+" / BCM";
		}
		if("邮政".equals(v)){
			return n+" / PSBC";
		}
		return n;
	}
	
	public String dictName(String groupcode,Object v){
		if(v==null){return "";}
		String value=v.toString();
		if(items==null)items=Item.NTgetAll();
		for(Item item:items){
		  if(item.groupcode.equals(groupcode)){
			  if(item.itemvalue.equals(value.toString())){
				  if("withdraw_flag".equals(groupcode)){
					 return withdrawToEn(item.itemname,value.toString());
				  } else if("bankcode".equals(groupcode)){
					  return bankToEn(item.itemname,value.toString());
				  }else{
					  return item.itemname;  
				  } 
			  }
		  }
		}
		return value.toString();
	}
	
	public String dictName2(String groupcode,Object v){
		if(v==null){return "";}
		String value=v.toString();
		if(items==null)items=Item.NTgetAll();
		for(Item item:items){
		  if(item.groupcode.equals(groupcode)){
			  if(item.itemname.equals(value.toString())){
				  return item.itemvalue;
			  }
		  }
		}
		return value.toString();
	}
	
	/**
	 * code 1:custform
	 * code 2:deposit
	 * code 3:withdraw
	 *  
	 * @param flag
	 * @param code
	 * @return
	 */
	public String status(Integer flag,String code){
		if(flag==null) return "";
		if("custform".equals(code)){
			if(flag.equals(-2)){
				return "不通过";
			}
			if(flag.equals(-1)){
				return "已废除";
			}
			if(flag.equals(0)){
				return "待处理";
			}
			if(flag.equals(2)){
				return "已通过";
			}
		}
		if("withdraw".equals(code)){
			if(flag.equals(-3)){
				return "预创建";
			}
			if(flag.equals(0)){
				return "待审核";
			}
			if(flag.equals(-2)){
				return "已拒绝";
			}
			if(flag.equals(-1)){
				return "已废除";
			}
			if(flag.equals(1)){
				return "已通过";
			}
			if(flag.equals(2)){
				return "已支付";
			}
		}
	
		if("deposit".equals(code)){
			if(flag.equals(-2)){
				return "已拒绝";
			}
			if(flag.equals(-1)){
				return "已废除";
			}
			if(flag.equals(0)){
				return "待处理";
			}
			if(flag.equals(2)){
				return "已通过";
			}
		}
		if("cashgift".equals(code)){
			if(flag.equals(1)){
				return "待处理";
			}
			if(flag.equals(3)){
				return "已通过";
			}
			if(flag.equals(2)){
				return "已拒绝";
			}
		}
		
		return "";
	}
	
	public String depositFlag(Integer flag){
		if(flag==null){return "";}
		if(flag.equals(-2)){
			return "已拒绝";
		}
		if(flag.equals(-1)){
			return "已废除";
		}
		if(flag.equals(0)){
			return "待处理";
		}
		if(flag.equals(2)){
			return "已通过";
		}
		return flag.toString();
	}
	
	public String date(Date date,String formate){
		if(date==null){return "";}
		return DateUtil.dateToString(date, formate);
	}
}
