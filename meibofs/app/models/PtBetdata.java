package models;

import java.math.BigDecimal;
import java.util.Date;

import util.Sp;

public class PtBetdata {

	public long Id;
	public String login_name;
	public String windowcode;
	public int gameid;
	public long gamecode;
	public String gametype;
	public String gamename;
	public int sessionid;
	public BigDecimal bet;
	public BigDecimal win;
	public BigDecimal balance;
	public Date gamedate;
	public BigDecimal currentbet;
	public BigDecimal progressivebet;
	public BigDecimal progressivewin;
	
	public  boolean NTcreat(String suffix){
		  String sql="insert into  mb_pt_betdata"+suffix+"(login_name,windowcode,"
		  		+ "gameid,gamecode,gametype,gamename,sessionid,bet,win,balance,"
		  		+ "gamedate,"
		  		+ "currentbet,progressivebet,progressivewin) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{login_name,
				  windowcode,gameid,gamecode,gametype,gamename,sessionid,bet,win,balance,
				  gamedate,currentbet,progressivebet,progressivewin});
		  return count>0;
	 }
	
	
	public boolean NTexitsData(String suffix){
		   String sql="select count(1) from mb_pt_betdata"+suffix+" where gamecode=? and login_name=?";
		   int count=Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{gamecode,login_name},Integer.class);
		   return count>0;
	}
	
	public static int createTable(String suffix){
		StringBuffer sb = new StringBuffer("");
		sb.append("CREATE TABLE `mb_pt_betdata" + suffix + "` (");  
        sb.append(" `Id` bigint(20) NOT NULL AUTO_INCREMENT,`login_name` varchar(50) DEFAULT NULL,`windowcode` varchar(5) DEFAULT NULL,`gameid` int(8) DEFAULT NULL,");
        sb.append(" `gamecode` bigint(16) DEFAULT NULL,`gametype` varchar(40) DEFAULT NULL,`gamename` varchar(100) DEFAULT NULL,`sessionid` int(11) DEFAULT NULL,");
        sb.append(" `bet` decimal(10,2) DEFAULT NULL,`win` decimal(10,2) DEFAULT NULL,`balance` decimal(10,2) DEFAULT NULL,`gamedate` datetime DEFAULT NULL,");
        sb.append(" `currentbet` decimal(10,2) DEFAULT NULL,`progressivebet` decimal(10,2) DEFAULT NULL,`progressivewin` decimal(10,2) DEFAULT NULL,PRIMARY KEY (`Id`),");
        sb.append(" KEY `login_name` (`login_name`),KEY `gamecode` (`gamecode`)");
        sb.append(") ENGINE=InnoDB DEFAULT CHARSET=utf8;"); 
        int result = Sp.get().getDefaultJdbc().update(sb.toString());
        return result;
	}
	
	
	
}
