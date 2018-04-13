package models;

import java.math.BigDecimal;
import java.util.Date;

import util.Sp;

public class AgBetdata {

	public long dataId;
	public String login_name;
	public BigDecimal validBetAmount;
	public String dataType;
	public String billNo;
	public String agentCode;
	public String gameCode;
	public BigDecimal netAmount;
	public Date betTime;
	public String gameType;
	public BigDecimal betAmount;
	public int flag;
	public String playType;
	public String currency;
	public String tableCode;
	public String loginIP;
	public Date recalcuTime;
	public String platformType;
	public String remark;
	public String round;
	public int slottype;
	public String result;
	public String mainbillno;
	public BigDecimal beforeCredit;
	
	public  boolean NTcreat(String suffix){
		  String sql="insert into  mb_ag_betdata"+suffix+"(login_name,validBetAmount,"
		  		+ "dataType,billNo,agentCode,gameCode,netAmount,betTime,gameType,betAmount,"
		  		+ "flag,"
		  		+ "playType,currency,tableCode,"
		  		+ "loginIP,recalcuTime,platformType,remark,"
		  		+ "round,slottype,result,mainbillno,beforeCredit) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{login_name,
				  validBetAmount,dataType,billNo,agentCode,gameCode,netAmount,betTime,gameType,betAmount,
				  flag,playType,currency,tableCode,
				  loginIP,recalcuTime,platformType,remark,round,slottype,result,mainbillno,beforeCredit});
		  return count>0;
	 }
	
	
	public static boolean NTexitsData(String billNo,String suffix){
		   String sql="select count(1) from mb_ag_betdata"+suffix+" where billNo=? ";
		   int count=Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{billNo},Integer.class);
		   return count>0;
	}
	
	public static int createTable(String suffix){
		StringBuffer sb = new StringBuffer("");
		sb.append("CREATE TABLE `mb_ag_betdata" + suffix + "` (");  
        sb.append(" `dataId` bigint(11) NOT NULL AUTO_INCREMENT,");
        sb.append(" `login_name` varchar(50) DEFAULT NULL,");
        sb.append(" `validBetAmount` decimal(10,2) DEFAULT NULL,");
        sb.append(" `dataType` varchar(50) DEFAULT NULL,");
        sb.append(" `billNo` varchar(25) DEFAULT NULL,");
        sb.append(" `agentCode` varchar(25) DEFAULT NULL,");
        sb.append(" `gameCode` varchar(30) DEFAULT NULL,");
        sb.append(" `netAmount` decimal(10,2) DEFAULT NULL,");
        sb.append(" `betTime` datetime DEFAULT NULL,");
        sb.append(" `gameType` varchar(20) DEFAULT NULL,");
        sb.append(" `betAmount` decimal(10,2) DEFAULT NULL,");
        sb.append(" `flag` tinyint(1) unsigned DEFAULT NULL,`playType` varchar(10) DEFAULT NULL,`currency` varchar(10) DEFAULT NULL,`tableCode` varchar(10) DEFAULT NULL,");
        sb.append(" `loginIP` varchar(20) DEFAULT NULL,");
        sb.append(" `recalcuTime` datetime DEFAULT NULL,`platformType` varchar(10) DEFAULT NULL,`remark` varchar(255) DEFAULT NULL,`round` varchar(20) DEFAULT NULL,");
        sb.append(" `slottype` int(11) unsigned DEFAULT NULL,`result` varchar(100) DEFAULT NULL,`mainbillno` varchar(20) DEFAULT NULL,`beforeCredit` decimal(10,2) unsigned DEFAULT NULL,");
        sb.append(" PRIMARY KEY (`dataId`),KEY `billNo` (`billNo`)");
        sb.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8;"); 
        int result = Sp.get().getDefaultJdbc().update(sb.toString());
        return result;
	}

	public String toString() {
		return "AgBetdata [dataId=" + dataId + ", login_name=" + login_name
				+ ", validBetAmount=" + validBetAmount + ", dataType="
				+ dataType + ", billNo=" + billNo + ", agentCode=" + agentCode
				+ ", gameCode=" + gameCode + ", netAmount=" + netAmount
				+ ", betTime=" + betTime + ", gameType=" + gameType
				+ ", betAmount=" + betAmount + ", flag=" + flag + ", playType="
				+ playType + ", currency=" + currency + ", tableCode="
				+ tableCode + ", loginIP=" + loginIP + ", recalcuTime="
				+ recalcuTime + ", platformType=" + platformType + ", remark="
				+ remark + ", round=" + round + ", slottype=" + slottype
				+ ", result=" + result + ", mainbillno=" + mainbillno
				+ ", beforeCredit=" + beforeCredit + "]";
	}
	
	
	
}
