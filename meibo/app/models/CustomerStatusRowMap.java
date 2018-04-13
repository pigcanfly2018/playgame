package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class CustomerStatusRowMap implements RowMapper{

	public CustomerStatus mapRow(ResultSet rs, int index) throws SQLException {
		CustomerStatus bean =new CustomerStatus();
		bean.cust_id=rs.getLong("cust_id");
		bean.login_name=rs.getString("login_name");
		bean.ag_transfer_flag=rs.getBoolean("ag_transfer_flag");
		bean.bbin_transfer_flag=rs.getBoolean("bbin_transfer_flag");
		bean.kg_transfer_flag=rs.getBoolean("kg_transfer_flag");
		bean.mg_transfer_flag=rs.getBoolean("mg_transfer_flag");
		bean.pt_transfer_flag=rs.getBoolean("pt_transfer_flag");
		return bean;
	}

}
