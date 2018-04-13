package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 常见
 * @author robin
 *
 */
public class MyValidator {
	
	private  static Pattern p=Pattern.compile("[\u4e00-\u9fa5]"); 
	
	/**
	 * 检查是否为空或者空串
	 * @param value
	 * @return
	 */
	public static boolean checkBlank(String value){
		if(value!=null&&(!"".equals(value))){
			return false;
		}
		return true;
	}
	
	/**
	 * 检查长度
	 * @param value
	 * @param min
	 * @param max
	 * @return
	 */
	public static boolean checkLength(String value,int min,int max){
		if(!checkBlank(value)){
			if(value.length()>min&&value.length()<max){
				return true;
			}
		}
		return false;
	}
	/**
	 * 检查长度 大于
	 * @param value
	 * @param length
	 * @return
	 */
	public static boolean checkMoreLength(String value,int length){
		if(!checkBlank(value)){
			if(value.length()>length){
				return true;
			}
		}
		return false;
	}
	/**
	 * 检查长度 小于
	 * @param value
	 * @param length
	 * @return
	 */
	public static boolean checkLessLength(String value,int length){
		if(!checkBlank(value)){
			if(value.length()<length){
				return true;
			}
		}
		return false;
	}
	/**
	 * 检查长度 等于
	 * @param value
	 * @param length
	 * @return
	 */
	public static boolean checkEqualsLength(String value,int length){
		if(!checkBlank(value)){
			if(value.length()==length){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 检查是否汉字
	 * @param value
	 * @return
	 */
	public static boolean checkChinese(String value){
		if(!checkBlank(value)){
		        Matcher m=p.matcher(value); 
		        if(m.find()){ 
		            return true;
		        }
		}
		return false;
	}
	
	/**
	 * 检查正则表达式
	 * @param value
	 * @return
	 */
	public static boolean checkRegex(String value,String regex){
		if(!checkBlank(value)){
	       if(value.matches(regex)){
	    	   return true;
	       }
	     }
	     return false;
	}
	
	
	
	

}
