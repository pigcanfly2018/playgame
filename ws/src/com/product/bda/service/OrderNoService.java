package com.product.bda.service;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;

public class OrderNoService {
	
	public static String createOrderNo(final String pre,JdbcTemplate template) {
		String sql = "{call mb_order(?,?)}";
		final String o_Msg;
		o_Msg = template.execute(sql, new CallableStatementCallback<String>() {
					public String doInCallableStatement(CallableStatement cs)
							throws SQLException, DataAccessException {
						cs.setString(1, pre);
						cs.registerOutParameter(2, Types.VARCHAR);
						cs.executeUpdate();
						return cs.getString(2);
					}
				});
		return o_Msg;
	}
	
	public static String createLocalNo(final String pre) {

		return  pre+System.nanoTime();
	}

}
