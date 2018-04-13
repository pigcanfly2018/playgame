package bsz.exch.test.dev;

import java.util.Date;

public class GetClassName {
	
	public static Class get(String type){
		
		if("VARCHAR".equals(type)){
			return java.lang.String.class;
		}
		if("BIGINT".equals(type)){
			return java.lang.Long.class;
		}
		if("DATETIME".equals(type)){
			return java.util.Date.class;
		}
		   System.out.println(type);
		
		
	
		return java.lang.String.class;
	}

}
