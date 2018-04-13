package models;

import java.math.BigDecimal;
import java.util.Date;

public class AgentStat {

	public Long stat_id;
	public Long cust_id;
	public Long agent_id;
	public String login_name;
	public String stat_date;
    public Date create_date;
    public Integer deposit_count;
    public Integer withdraw_count;
    public Integer gift_count;
    public BigDecimal deposit_collect;
    public BigDecimal withdraw_collect;
    public BigDecimal gift_collect;
    
    
    //辅助字段
    public String stat_mon;
    public String agent_name;
    
}
