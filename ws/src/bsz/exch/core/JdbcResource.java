package bsz.exch.core;

import java.util.Hashtable;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcResource {
	
	
	private JdbcResource(){
		
	}
	private static Hashtable<String,JdbcTemplate> jdbcMap = new Hashtable<String,JdbcTemplate>();
	
	public static JdbcResource instance(){
		   if(resource==null){
			   resource = new JdbcResource();
		   }
		   return resource;
	   }
	
    private static JdbcResource resource = new JdbcResource();
	  
    public void addDataSource(String id,BasicDataSource source){
    	if(!jdbcMap.containsKey(id+"_jdbc")){
    		JdbcTemplate template =new JdbcTemplate();
    		template.setDataSource(source);
    		jdbcMap.put(id+"_jdbc", template);
    	}
    }
    
    public boolean contains(String dataSource){
    	if(!jdbcMap.containsKey(dataSource+"_jdbc")){
    		return false;
    	}
    	return true;
    }
    
    
    public JdbcTemplate getJdbcTemplate(String dataSource){
    	
    	if(jdbcMap.containsKey(dataSource+"_jdbc")){
    		return jdbcMap.get(dataSource+"_jdbc");
    	}
    	return null;
    	
    }
     
}
