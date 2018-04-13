package com.product.bda.service;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

public class ConfigService {
	
    private static Logger logger=Logger.getLogger(ConfigService.class);
		
    private JdbcTemplate template;
	private String datasource;
		
	public ConfigService(JdbcTemplate template,String datasource){
			this.template=template;
			this.datasource=datasource;
	}
	
	 public boolean queryGameStatus(String plat,String product){
		String sql="select count(1) from mb_config where config_name = ? and config_value='开' and product = ?";
		int count=template.queryForObject(sql, new Object[]{(plat+"_game").toLowerCase(),product}, Integer.class);
		return count>0;
	}
	

}
