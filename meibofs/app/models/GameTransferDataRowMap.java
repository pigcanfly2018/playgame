package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class GameTransferDataRowMap implements RowMapper{

	
	public GameTransferData mapRow(ResultSet rs, int index) throws SQLException {
		GameTransferData bean = new GameTransferData();
		bean.data_id=rs.getLong("data_id");
		bean.bill_no=rs.getString("bill_no");
		bean.game_name=rs.getString("game_name").substring(3);
		bean.game_pwd=rs.getString("game_pwd");
		bean.credit=rs.getBigDecimal("credit");
		bean.direct=rs.getString("direct");
		bean.plat=rs.getString("plat");
		
		bean.create_date=rs.getTimestamp("create_date");
		bean.flag=rs.getInt("flag");
		
		bean.finish_date=rs.getTimestamp("finish_date");
		bean.product=rs.getString("product");
		bean.ref_order_no=rs.getString("ref_order_no");
		return bean;
	}

}
