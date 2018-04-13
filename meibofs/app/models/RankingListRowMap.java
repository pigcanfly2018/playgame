package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class RankingListRowMap implements RowMapper{
	public RankingList mapRow(ResultSet rs, int index) throws SQLException {
		RankingList bean =new RankingList();
		bean.ranking_id=rs.getLong("ranking_id");
		bean.create_date=rs.getTimestamp("create_date");
		bean.login_name=rs.getString("login_name");
		bean.trophy_name=rs.getString("trophy_name");
		bean.trophy_code=rs.getString("trophy_code");
		bean.trophy_count=rs.getInt("trophy_count");
		bean.cost=rs.getBigDecimal("cost");
		bean.cust_id=rs.getLong("cust_id");
		return bean;
	}
}

