package models;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;

import util.Sp;

public class OrderNo {
	public static String createOrderNo(final String pre) {
		String sql = "{call mb_order(?,?)}";
		final String o_Msg;
		o_Msg = (String) Sp.get().getDefaultJdbc()
				.execute(sql, new CallableStatementCallback() {
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
	
	public static void main(String[] args){
		
		System.out.println(OrderNo.createOrderNo("kj"));
	}
	
	
}
