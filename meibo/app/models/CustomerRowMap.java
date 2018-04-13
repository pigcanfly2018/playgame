package models;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class CustomerRowMap implements RowMapper{
	public Customer mapRow(ResultSet rs, int index) throws SQLException {
		Customer bean =new Customer();
		bean.cust_id=rs.getLong("cust_id");
		bean.login_name=rs.getString("login_name");
		bean.login_pwd=rs.getString("login_pwd");
		bean.real_name=rs.getString("real_name");
		bean.phone=rs.getString("phone");
		bean.email=rs.getString("email");
		bean.qq=rs.getString("qq");
		bean.reg_ip=rs.getString("reg_ip");
		bean.create_date=rs.getTimestamp("create_date");
		bean.create_user=rs.getString("create_user");
		bean.login_ip=rs.getString("login_ip");
		bean.login_date=rs.getTimestamp("login_date");
		bean.login_times=rs.getInt("login_times");
		bean.sb_game=rs.getString("sb_game");
		bean.sb_pwd=rs.getString("sb_pwd");
		bean.sb_flag=rs.getBoolean("sb_flag");
		bean.sb_actived=rs.getBoolean("sb_actived");
		bean.ag_game=rs.getString("ag_game");
		bean.ag_pwd=rs.getString("ag_pwd");
		bean.ag_flag=rs.getBoolean("ag_flag");
		bean.ag_actived=rs.getBoolean("ag_actived");
		bean.bbin_game=rs.getString("bbin_game");
		bean.bbin_pwd=rs.getString("bbin_pwd");
		bean.bbin_flag=rs.getBoolean("bbin_flag");
		bean.bbin_actived=rs.getBoolean("bbin_actived");
		
		bean.bbinmobile_game=rs.getString("bbinmobile_game");
		
		bean.credit=rs.getBigDecimal("credit");
		bean.flag=rs.getBoolean("flag");
		bean.cust_level=rs.getInt("cust_level");
		bean.is_agent=rs.getBoolean("is_agent");
		bean.parent_id=rs.getLong("parent_id");
		bean.reg_source=rs.getString("reg_source");
		bean.agent_dm=rs.getString("agent_dm");
		bean.first_deposit_date=rs.getDate("first_deposit_date");
		bean.remarks=rs.getString("remarks");
		bean.bank_name=rs.getString("bank_name");
		bean.account_type=rs.getString("account_type");
		bean.bank_dot=rs.getString("bank_dot");
		bean.account=rs.getString("account");
		bean.agent_mode=rs.getString("agent_mode");
		bean.s_email=rs.getBoolean("s_email");
		bean.score=rs.getBigDecimal("score");
		bean.promo_flag=rs.getBoolean("promo_flag");
		
		bean.pt_game=rs.getString("pt_game");
		bean.pt_pwd=rs.getString("pt_pwd");
		bean.pt_flag=rs.getBoolean("pt_flag");
		bean.pt_actived=rs.getBoolean("pt_actived");
		bean.online_pay=rs.getBoolean("online_pay");
		bean.bank_pay=rs.getBoolean("bank_pay");
		bean.is_block=rs.getBoolean("is_block");
		bean.alipay_flag=rs.getBoolean("alipay_flag");
		bean.kg_game=rs.getString("kg_game");
		bean.kg_pwd=rs.getString("kg_pwd");
		bean.kg_flag=rs.getBoolean("kg_flag");
		bean.kg_actived=rs.getBoolean("kg_actived");
		
		bean.mg_game=rs.getString("mg_game");
		bean.mg_alias = rs.getString("mg_alias");
		bean.mg_pwd=rs.getString("mg_pwd");
		bean.mg_flag=rs.getBoolean("mg_flag");
		bean.mg_actived=rs.getBoolean("mg_actived");
		
		bean.pp_game=rs.getString("pp_game");
		bean.pp_pwd=rs.getString("pp_pwd");
		bean.pp_flag=rs.getBoolean("pp_flag");
		bean.pp_actived=rs.getBoolean("pp_actived");
		
		bean.alipay_account=rs.getString("alipay_account");
		bean.alipay_name=rs.getString("alipay_name");
		bean.auto_transfer_flag=rs.getBoolean("auto_transfer_flag");
		bean.need_gift_flag=rs.getBoolean("need_gift_flag");
		
		bean.accountkey=rs.getString("accountkey");
		
		return bean;
	}

}
