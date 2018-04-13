package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class DpayDepositExpRowMap implements RowMapper{

	public DpayDepositExp mapRow(ResultSet rs, int index) throws SQLException {

		DpayDepositExp bean = new DpayDepositExp();
		bean.dpay_id = rs.getLong("dpay_id");
		bean.exception_order_num = rs.getString("exception_order_num");
		bean.company_id = rs.getString("company_id");
		bean.exact_payment_bank = rs.getString("exact_payment_bank");
		bean.pay_card_name = rs.getString("pay_card_name");
		bean.pay_card_num = rs.getString("pay_card_num");
		bean.receiving_bank = rs.getString("receiving_bank");
		bean.receiving_account_name = rs.getString("receiving_account_name");
		bean.channel = rs.getString("channel");
		bean.note = rs.getString("note");
		bean.area = rs.getString("area");
		bean.exact_time = rs.getString("exact_time");
		bean.amount = rs.getBigDecimal("amount");
		bean.fee = rs.getBigDecimal("fee");
		bean.transaction_charge = rs.getBigDecimal("transaction_charge");
		bean.base_info= rs.getString("base_info");
		bean.create_date=rs.getTimestamp("create_date");
		bean.flag = rs.getInt("flag");
		bean.login_name = rs.getString("login_name");
		bean.action_user = rs.getString("action_user");
		bean.claim_date = rs.getTimestamp("claim_date");
		bean.pic_id = rs.getLong("pic_id");
		bean.pic2_id = rs.getLong("pic2_id");
		bean.action_user2 = rs.getString("action_user2");
		bean.claim_date2 = rs.getTimestamp("claim_date2");
		bean.remark = rs.getString("remark");
		
		return bean;
	}

}
