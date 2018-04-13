package util;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MyGsonBuilder {
	
	public static Gson getGson(final String[] fittleFields){
		Gson gson = new GsonBuilder()
	    .setExclusionStrategies(new ExclusionStrategy() {
	        public boolean shouldSkipClass(Class<?> clazz) {
	            return false;
	        }
	        public boolean shouldSkipField(FieldAttributes f) {
	        	for(String fd:fittleFields){
	        		if(f.getName().equals(fd)){
	    	        	System.out.println(f.getName());
	        			return true;
	        		}
	        	}
	            return false;
	        }

	     }).serializeNulls().create();
		return gson;
	}

}
