package models;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class BigDecimalRowMap implements RowMapper{
	public BigDecimal mapRow(ResultSet rs, int index) throws SQLException {
		BigDecimal bean =new BigDecimal(0);
		bean=rs.getBigDecimal(1);
		return bean;
	}
}