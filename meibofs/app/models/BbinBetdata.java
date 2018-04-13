package models;

import java.math.BigDecimal;
import java.util.Date;

import util.Sp;

public class BbinBetdata {

	
	public long dataId;
	public String login_name;
	public BigDecimal commissionable;
	public String gametype;
	public String wagersid;
	public String result;
	public Date wagersdate;
	public String gamecode;
	public BigDecimal betamount;
	public String gameType;
	public String serialid;
	
	public BigDecimal payoff;
	public String roundno;
	public String resulttype;
	public String card;
	public String currency;
	public String ispaid;
	
	public BigDecimal commission;
	
	
	public  boolean NTcreat(String suffix){
		  String sql="insert into  mb_bbin_betdata"+suffix+"(login_name,wagersid,"
		  		+ "wagersdate,gametype,result,betamount,payoff,currency,commissionable,serialid,"
		  		+ "roundno,"
		  		+ "gamecode,resulttype,card,"
		  		+ "commission,ispaid) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{login_name,
				  wagersid,wagersdate,gametype,result,betamount,payoff,currency,commissionable,
				  serialid,roundno,gamecode,resulttype,
				  card,commission,ispaid});
		  return count>0;
	 }
	
	
	public static boolean NTexitsData(String wagersid,String suffix){
		   String sql="select count(1) from mb_bbin_betdata"+suffix+" where wagersid=? ";
		   int count=Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{wagersid},Integer.class);
		   return count>0;
	}
	
	public static int createTable(String suffix){
		StringBuffer sb = new StringBuffer("");
		sb.append("CREATE TABLE `mb_bbin_betdata" + suffix + "` (");  
        sb.append(" `dataId` int(11) NOT NULL AUTO_INCREMENT,`login_name` varchar(50) DEFAULT NULL,`wagersid` varchar(20) DEFAULT NULL,`wagersdate` datetime DEFAULT NULL,");
        sb.append(" `gametype` varchar(10) DEFAULT NULL,`result` varchar(50) DEFAULT NULL,`betamount` decimal(10,2) DEFAULT NULL,`payoff` decimal(10,2) DEFAULT NULL,");
        sb.append(" `currency` varchar(15) DEFAULT NULL,`commissionable` decimal(10,2) DEFAULT NULL,`serialid` varchar(10) DEFAULT NULL,`roundno` varchar(20) DEFAULT NULL,");
        sb.append(" `gamecode` varchar(20) DEFAULT NULL,`resulttype` varchar(5) DEFAULT NULL,`card` varchar(60) DEFAULT NULL,`commission` decimal(10,2) DEFAULT NULL COMMENT '彩票退水',");
        sb.append(" `ispaid` varchar(5) DEFAULT NULL,PRIMARY KEY (`dataId`),KEY `wagersid` (`wagersid`)");
        sb.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8;"); 
        int result = Sp.get().getDefaultJdbc().update(sb.toString());
        return result;
	}
	

	
	public String toString() {
		return "BbinBetdata [dataId=" + dataId + ", login_name=" + login_name
				+ ", commissionable=" + commissionable + ", gametype="
				+ gametype + ", wagersid=" + wagersid + ", result=" + result
				+ ", wagersdate=" + wagersdate + ", gamecode=" + gamecode
				+ ", betamount=" + betamount + ", gameType=" + gameType
				+ ", serialid=" + serialid + ", payoff=" + payoff
				+ ", roundno=" + roundno + ", resulttype=" + resulttype
				+ ", card=" + card + ", currency=" + currency + ", commission="
				+ commission + "]";
	}
	
	
	
	
}
