package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.RowMapper;

public class ItemRowMap implements RowMapper{
	public Item mapRow(ResultSet rs, int index) throws SQLException {
		Item bean =new Item();
		bean.itemname=rs.getString("itemname");
		bean.itemcode=rs.getString("itemcode");
		bean.itemvalue=rs.getString("itemvalue");
		bean.groupcode=rs.getString("groupcode");
		bean.createdate=rs.getTimestamp("createdate");
		bean.createuser=rs.getString("createuser");
		
		bean.startdate=rs.getString("startdate");
	    
		bean.enddate=rs.getString("enddate");

		bean.pcflag=rs.getInt("pcflag");
		
		bean.mobileflag=rs.getInt("mobileflag");
		
		bean.actbill=rs.getString("actbill");
		
		bean.moneyflag=rs.getInt("moneyflag");
	    
		return bean;
	}
}
