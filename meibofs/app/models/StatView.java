package models;

import java.math.BigDecimal;
import java.util.Date;

public class StatView {
	public Date index_date;
	public Integer account_cnt;
	public Integer account_deposit_cnt;
	public BigDecimal deposit_sum;
	public BigDecimal withdraw_sum;
	public BigDecimal payout;
	public Integer deposit_cnt;
	public Integer withdraw_cnt;
	public BigDecimal profit;
	public String stat_mon;

}
