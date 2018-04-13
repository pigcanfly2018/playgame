package service;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.List;

import models.Ad;
import models.AdRowMap;
import models.AgGame;
import models.Bank;
import models.BankRowMap;
import models.BbinGame;
import models.Billboard;
import models.BillboardRowMap;
import models.Customer;
import models.Discount;
import models.DiscountRowMap;
import models.MessageBoard;
import models.MessageBoardRowMap;
import models.MgGame;
import models.Notice;
import models.NoticeRowMap;
import models.PpGame;
import models.PtGame;
import models.SbGame;
import models.WinList;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import play.cache.Cache;
import util.Sp;

public class MeiBoService {
	
	public static String cacheTime="20s";
	public static String gameCacheTime="1800s";
	/**
	 * 获取优惠 (3分钟缓存)
	 * @return
	 */
	public static List<Discount> getDiscounts(){
		 List<Discount> discountList=(List<Discount>)Cache.get("discount_list");
		 if(discountList==null){
			 String sql ="select * from mb_discount where available =1 and mobile_flag=0 AND NOW() BETWEEN start_date AND end_date order by priority";
			 discountList=Sp.get().getDefaultJdbc().query(sql,new DiscountRowMap());
			 Cache.set("discount_list", discountList,cacheTime);
		 }
		return discountList;
	}
	
	//获取排行榜数据
	public static List<Billboard> getBillboardList(){
		 List<Billboard> billboardList=(List<Billboard>)Cache.get("billboard_list");
		 if(billboardList==null){
			 String sql ="select * from mb_billboard where flag =0 order by bet_amount desc limit 50";
			 billboardList=Sp.get().getDefaultJdbc().query(sql,new BillboardRowMap());
			 Cache.set("billboard_list", billboardList,"300s");
		 }
		return billboardList;
	}
	
	/**
	 * 获取优惠
	 * @param id
	 * @return
	 */
	public static Discount getDiscount(Long id){
		 Discount discount=(Discount)Cache.get("discount_"+id);
		 if(discount==null){
			 String sql ="select * from mb_discount where available =1 and mobile_flag=0  and discount_id=? order by priority";
			 List<Discount> disList=Sp.get().getDefaultJdbc().query(sql,new Object[]{id},new DiscountRowMap());
			 if(disList.size()>0){
				 discount=disList.get(0);
				 Cache.set("discount_"+id, discount,cacheTime);
			 }
		 }
		return discount;
	}
	/**
	 * 获取一个主推优惠
	 * @param id
	 * @return
	 */
	public static Discount getDiscount(){
			 String sql ="select * from mb_discount where available =1 and is_hot=1 and mobile_flag=0 order by priority limit 1";
			 List<Discount> disList=Sp.get().getDefaultJdbc().query(sql,new Object[]{},new DiscountRowMap());
			 if(disList.size()>0){
				return disList.get(0);
			 }
		    return null;
	}

	
    /**
     * 获取广告(3分钟缓存)
     * @return 
     */
	public static List<Ad> getAds(){
		 List<Ad> adList=(List<Ad>)Cache.get("ad_list");
		 if(adList==null){
			 String sql ="select * from mb_ad where available=1 and mobile_flag=0 and start_date<=now() and now()<=end_date order by priority";
			 adList=Sp.get().getDefaultJdbc().query(sql,new AdRowMap());
			 Cache.set("ad_list", adList,cacheTime);
		 }
		return adList;
	}
	/**
	 * 获取PT游戏
	 */
	public static List<PtGame> getPtGames(){
		 List<PtGame> ptList=(List<PtGame>)Cache.get("pt_game_list");
		 if(ptList==null){
			 ptList=PtGame.getAllFlash();
			 Cache.set("pt_game_list", ptList,gameCacheTime);
		 }
		return ptList;
	}
	
	/**
	 * 获取AG游戏
	 */
	public static List<AgGame> getAgGames(){
		 List<AgGame> agList=(List<AgGame>)Cache.get("ag_game_list");
		 if(agList==null){
			 agList=AgGame.getAll();
			 Cache.set("ag_game_list", agList,gameCacheTime);
		 }
		return agList;
	}
	
	
	/**
	 * 获取申博游戏
	 */
	public static List<SbGame> getShenBoGames(){
		 List<SbGame> sbList=(List<SbGame>)Cache.get("sb_game_list");
		 if(sbList==null){
			 sbList=SbGame.getAll();
			 Cache.set("sb_game_list", sbList,gameCacheTime);
		 }
		return sbList;
	}
	
	/**
	 * 获取MG游戏
	 */
	public static List<MgGame> getMgGames(){
		 List<MgGame> mgList=(List<MgGame>)Cache.get("mg_game_list");
		 if(mgList==null){
			 mgList=MgGame.getAll();
			 Cache.set("mg_game_list", mgList,gameCacheTime);
		 }
		return mgList;
	}
	
	/**
	 * 获取PP游戏
	 */
	public static List<PpGame> getPpGames(){
		 List<PpGame> ppList=(List<PpGame>)Cache.get("pp_game_list");
		 if(ppList==null){
			 ppList=PpGame.getAll();
			 Cache.set("pp_game_list", ppList,gameCacheTime);
		 }
		return ppList;
	}
	
	
	public static List<WinList> getWinList(){
		 List<WinList> winList=(List<WinList>)Cache.get("win_list");
		 if(winList==null){
			 winList=WinList.getNewPublished();
			 Cache.set("win_list", winList, gameCacheTime);
		 }
		return winList;
	}	
	
	
	/**
	 * 获取公告
	 * @return
	 */
	public static List<Notice> getNotices(){
		 List<Notice> noticeList=(List<Notice>)Cache.get("notice_list");
		 if(noticeList==null){
			 String sql ="select * from mb_notice where available=1 and start_date<=now() and now()<=end_date order by priority";
			 noticeList=Sp.get().getDefaultJdbc().query(sql,new NoticeRowMap());
			 Cache.set("notice_list", noticeList,cacheTime);
		 }
		return noticeList;
	}
	
	
	public static boolean NTexist(String login_name) {
		String sql = "select count(1) from mb_customer where login_name =?";
		int count = Sp.get().getDefaultJdbc()
				.queryForObject(sql, new Object[] { login_name },Integer.class);
		return count > 0;
	}
	
	public static boolean NTexistPhone(String phone) {
		String sql = "select count(1) from mb_customer where phone =?";
		int count = Sp.get().getDefaultJdbc()
				.queryForObject(sql, new Object[] { phone },Integer.class);
		return count > 0;
	}
	
	  public static Bank getBank(Integer cust_level,String bank_name){
		   String sql="select * from mb_bank where cust_level <=? and bank_name=? and available=1 and specifiedname is null  order by cust_level desc limit 1";
		   List<Bank> bankList = Sp.get().getDefaultJdbc().query(sql,new Object[]{cust_level,bank_name},new BankRowMap());
		   if(bankList.size()>0){
			   return bankList.get(0);
		   }
		   return null;
	   }
	  
	  public static Bank getBank(Integer cust_level){
		   String sql="select * from mb_bank where cust_level <=?  and available=1 and specifiedname is null  order by cust_level desc limit 1";
		   List<Bank> bankList = Sp.get().getDefaultJdbc().query(sql,new Object[]{cust_level},new BankRowMap());
		   if(bankList.size()>0){
			   return bankList.get(0);
		   }
		   return null;
	   }
	  
	  public static Bank getSpecifiedBank(String login_name,String bank_name){
		   String sql="select * from mb_bank where specifiedname like ? and bank_name=? and available=1 order by cust_level desc limit 1";
		   List<Bank> bankList = Sp.get().getDefaultJdbc().query(sql,new Object[]{"%"+login_name+"%",bank_name},new BankRowMap());
		   if(bankList.size()>0){
			   return bankList.get(0);
		   }
		   return null;
	   }
	  
	  public static Bank getBank(Long bank_id){
		   String sql="select * from mb_bank where bank_id =? and  available=1";
		   List<Bank> bankList = Sp.get().getDefaultJdbc().query(sql,new Object[]{bank_id},new BankRowMap());
		   if(bankList.size()>0){
			   return bankList.get(0);
		   }
		   return null;
	   }
	  
	  
	public static Integer depositCnt(Long cust_id){
		 final String sql="select count(1) from  sb_deposit where cust_id=? and status=1";
		 int count=Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{cust_id},Integer.class);
		 return count;
			  
	}
	 /**
	  * 获取当前客户的提款单数
	  * @param cust_id
	  * @return
	  */
	public static Integer withdrawCnt(Long cust_id){
			 final String sql="select count(1) from  sb_withdraw where cust_id=? and (status=1 or status=3)";
			 int count=Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{cust_id},Integer.class);
			 return count;
				  
   }
	
   /**
    * 删除用户名
    * @return
    */
   public static boolean deleteLoginName(String login_name){
	   String sql="delete from sb_preg where login_name=? ";
	   int count=Sp.get().getDefaultJdbc().update(sql,new Object[]{login_name});
	   if(count>0){
		   return true;
	   }
	   return false;
   }
   
   public static boolean pregExist(String login_name){
	   final String sql="select count(1) from  sb_preg where login_name=? ";
	   int count=Sp.get().getDefaultJdbc().queryForObject(sql,new Object[]{login_name},Integer.class);
	   return count>0;
   }
   
   
   
   public static Integer getListCnt(String key){
	   String sql="select count(1) from sb_message_board where reply_date is not null ";
	   return Sp.get().getDefaultJdbc().queryForObject(sql,Integer.class);
   }
   
   public static boolean creatphonerecord(String login_name,String phone){
		  String sql="insert into mb_cus_phonerecord(login_name,content,create_date)" +
		 		" values(?,?,now())";
		  int count=Sp.get().getDefaultJdbc().update(sql, new Object[]{login_name,phone});
		  return count>0;
   }
   
   public static boolean createOrderNumber(String ordernumber){
		  String sql="insert into mb_ordernumber(ordernumber) values(?)";
		 
		  int count= Sp.get().getDefaultJdbc().update(sql, new Object[]{ordernumber});
		  return count>0;
   }
   
   
   public static List<MessageBoard> getMessageBoard(Integer page,Integer size){
	   String key ="message_board_list_"+page+"_"+size;
	   List<MessageBoard> msgList=(List<MessageBoard>)Cache.get(key);
	   if(msgList==null){
		   String sql="select * from mb_message_board where reply_date is not null order by show_date desc limit ? offset ?";
		   msgList=Sp.get().getDefaultJdbc().query(sql,new Object[]{size,page*size},new MessageBoardRowMap());
		   Cache.set(key, msgList,cacheTime);
	   }
	  return msgList;
	   
   }
   
   
   public static String createOrderNo(){
	   String sql = "{call sb_gon(?)}";
	   final String o_Msg;
	   o_Msg =(String) Sp.get().getDefaultJdbc().execute(sql,new CallableStatementCallback(){
	       public String doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
	    	   cs.registerOutParameter(1,Types.VARCHAR);
	    	   cs.executeUpdate();
	           return cs.getString(1);
	       }
	    });
	   return o_Msg;
   }
   
   public static String createLocalNo(final String pre) {

		return  pre+System.nanoTime();
	}

   public static boolean createYeeOrder(String order_no,
		   String login_name,
		   String phone,
		   String p1_MerId,
		   String p3_Amt,
		   String p4_Cur,
		   String p5_Pid,
		   String p6_Pcat,
		   String p7_Pdesc,
		   String p8_Url,
		   String p9_SAF,
		   String  pd_FrpId,
		   String pr_NeedResponse,
		   String hac){
	       String sql="insert into sb_yee_order(login_name,phone,mer_id,order_no,credit,cur,pid,"
	       		+ "pcat,pdesc,purl,psaf,frpid,needResponse,hmac,create_date)"
	       		+ "value(?,?,?,?,?,?,?,?,?,?,?,?,?,?,now())";
	      return Sp.get().getDefaultJdbc().update(sql,new Object[]{login_name,phone,
	    		  p1_MerId,order_no,p3_Amt,p4_Cur,p5_Pid,p6_Pcat,p7_Pdesc,p8_Url,
	    		  p9_SAF,pd_FrpId,pr_NeedResponse,hac})>0;
   }
   
   public static boolean isYeeOrderFinishBS(String order_no){
	   String sql="select count(1) from sb_yee_order where order_no=? and done_bs =1";
	   return Sp.get().getDefaultJdbc().queryForObject(sql, new Object[]{order_no},Integer.class)>0;
   }
   
   public static boolean saveYeeOrderResult(String order_no,String code,String trxId,String r_cur,BigDecimal pay_credit,
		   String uid,String bankId,String bank_order,
		   String pay_date,BigDecimal source_fee,BigDecimal target_fee,String pay_hmac){
	   String sql="update sb_yee_order set code=?,trxId=?,r_cur=?,pay_credit=?, "+
		   "uid=?,bankId=?,bank_order=?,pay_date=?,source_fee=?,target_fee=?,"
		   + "pay_hmac=?,done_bs=1 where order_no=?";
	   return Sp.get().getDefaultJdbc().update(sql,new Object[]{code,trxId,r_cur,
			   pay_credit,uid,bankId,bank_order,pay_date,source_fee,target_fee,pay_hmac,
			   order_no})>0;
   }
  
   
   public static boolean getZhongqiu(){
	   String sql="select count(1) from mb_config where config_name='zhongqiu' and config_value='开'";
	   return Sp.get().getDefaultJdbc().queryForObject(sql,Integer.class)>0;
   }
   
   
   public static boolean getOnlinePay01(){
	   String sql="select count(1) from mb_config where config_name='online_pay_01' and config_value='开'";
	   return Sp.get().getDefaultJdbc().queryForObject(sql,Integer.class)>0;
   }
   
   public static boolean getDepositwayFlag(String config_name,int cust_level){
	   String sql="select count(1) from mb_config where config_name='"+config_name+"' and config_level like '%"+cust_level+"%' and config_value='开'";
	   return Sp.get().getDefaultJdbc().queryForObject(sql,Integer.class)>0;
   }
   
   public static boolean getPointPay(){
	   String sql="select count(1) from mb_config where config_name='point_pay' and config_value='开'";
	   return Sp.get().getDefaultJdbc().queryForObject(sql,Integer.class)>0;
   }
   
   public static boolean getOnlinePay02(){
	   String sql="select count(1) from mb_config where config_name='online_pay_02' and config_value='开'";
	   return Sp.get().getDefaultJdbc().queryForObject(sql,Integer.class)>0;
   }
   
   public static boolean getSpeedPay(){
	   String sql="select count(1) from mb_config where config_name='speedpay' and config_value='开'";
	   return Sp.get().getDefaultJdbc().queryForObject(sql,Integer.class)>0;
   }
   
   public static boolean getYingbaoPay(){
	   String sql="select count(1) from mb_config where config_name='yingbaopay' and config_value='开'";
	   return Sp.get().getDefaultJdbc().queryForObject(sql,Integer.class)>0;
   }
   
   public static boolean getLeyingPay1(){
	   String sql="select count(1) from mb_config where config_name='leyingpay1' and config_value='开'";
	   return Sp.get().getDefaultJdbc().queryForObject(sql,Integer.class)>0;
   }
   
   public static boolean getLeyingPay2(){
	   String sql="select count(1) from mb_config where config_name='leyingpay2' and config_value='开'";
	   return Sp.get().getDefaultJdbc().queryForObject(sql,Integer.class)>0;
   }
   
   public static boolean getYingbaoPay2(){
	   String sql="select count(1) from mb_config where config_name='yingbaopay2' and config_value='开'";
	   return Sp.get().getDefaultJdbc().queryForObject(sql,Integer.class)>0;
   }
   
   public static boolean getDinPayWeixin(){
	   String sql="select count(1) from mb_config where config_name='dinpay_wexin' and config_value='开'";
	   return Sp.get().getDefaultJdbc().queryForObject(sql,Integer.class)>0;
   }
   
   public static boolean getYingbaoOnlineBank(){
	   String sql="select count(1) from mb_config where config_name='yingbaopay' and config_value='开'";
	   return Sp.get().getDefaultJdbc().queryForObject(sql,Integer.class)>0;
   }
   
   public static boolean getYinbaoOnlineBank(){
	   String sql="select count(1) from mb_config where config_name='yinbaopay' and config_value='开'";
	   return Sp.get().getDefaultJdbc().queryForObject(sql,Integer.class)>0;
   }
   
   public static boolean getXinbeiQQ(){
	   String sql="select count(1) from mb_config where config_name='xinbeiqq' and config_value='开'";
	   return Sp.get().getDefaultJdbc().queryForObject(sql,Integer.class)>0;
   }
   
   public static boolean getDpayOnlineBank(){
	   String sql="select count(1) from mb_config where config_name='dponlinebank' and config_value='开'";
	   return Sp.get().getDefaultJdbc().queryForObject(sql,Integer.class)>0;
   }
   
   public static boolean getJvbaoPay(){
	   String sql="select count(1) from mb_config where config_name='jvbaopay' and config_value='开'";
	   return Sp.get().getDefaultJdbc().queryForObject(sql,Integer.class)>0;
   }
   
   public static boolean getTonghuiPay(){
	   String sql="select count(1) from mb_config where config_name='tonghuipay' and config_value='开'";
	   return Sp.get().getDefaultJdbc().queryForObject(sql,Integer.class)>0;
   }
   
   public static boolean getHuiboPay(){
	   String sql="select count(1) from mb_config where config_name='huibowangyin' and config_value='开'";
	   return Sp.get().getDefaultJdbc().queryForObject(sql,Integer.class)>0;
   }
   
   public static boolean getYingTongBaoPay(){
	   String sql="select count(1) from mb_config where config_name='yingtongbaowangyin' and config_value='开'";
	   return Sp.get().getDefaultJdbc().queryForObject(sql,Integer.class)>0;
   }
   
   public static boolean getRPNPay(){
	   String sql="select count(1) from mb_config where config_name='rpnpay' and config_value='开'";
	   return Sp.get().getDefaultJdbc().queryForObject(sql,Integer.class)>0;
   }
   
   
   public static boolean getXunhuibaoWeixinPay(){
	   String sql="select count(1) from mb_config where config_name='xunhuibaowexin' and config_value='开'";
	   return Sp.get().getDefaultJdbc().queryForObject(sql,Integer.class)>0;
   }
   
   public static boolean getXunhuibaoZhifubaoPay(){
	   String sql="select count(1) from mb_config where config_name='xunhuibaoalipay' and config_value='开'";
	   return Sp.get().getDefaultJdbc().queryForObject(sql,Integer.class)>0;
   }
   
   
   
   public static boolean getTenpay(){
	   String sql="select count(1) from mb_config where config_name='tenpay' and config_value='开'";
	   return Sp.get().getDefaultJdbc().queryForObject(sql,Integer.class)>0;
   }
   
   public static boolean getAlipay(){
	   String sql="select count(1) from mb_config where config_name='alipay' and config_value='开'";
	   return Sp.get().getDefaultJdbc().queryForObject(sql,Integer.class)>0;
   }

   public static boolean getAgStatus(){
	   String sql="select count(1) from mb_config where config_name='ag_game' and config_value='开'";
	   return Sp.get().getDefaultJdbc().queryForObject(sql,Integer.class)>0;
   }
   public static boolean getBbinStatus(){
	   String sql="select count(1) from mb_config where config_name='bbin_game' and config_value='开'";
	   return Sp.get().getDefaultJdbc().queryForObject(sql,Integer.class)>0;
   }
   
   public static boolean getSbStatus(){
	   String sql="select count(1) from mb_config where config_name='vs_game' and config_value='开'";
	   return Sp.get().getDefaultJdbc().queryForObject(sql,Integer.class)>0;
   }
   
   public static boolean getPpStatus(){
	   String sql="select count(1) from mb_config where config_name='pp_game' and config_value='开'";
	   return Sp.get().getDefaultJdbc().queryForObject(sql,Integer.class)>0;
   }
   
   public static boolean getPtStatus(){
	   String sql="select count(1) from mb_config where config_name='pt_game' and config_value='开'";
	   return Sp.get().getDefaultJdbc().queryForObject(sql,Integer.class)>0;
   }
   public static boolean getKgStatus(){
	   String sql="select count(1) from mb_config where config_name='kg_game' and config_value='开'";
	   return Sp.get().getDefaultJdbc().queryForObject(sql,Integer.class)>0;
   }
   
   public static boolean getPpenfireStatus(){
	   String sql="select count(1) from mb_config where config_name='openfire' and config_value='开'";
	   return Sp.get().getDefaultJdbc().queryForObject(sql,Integer.class)>0;
   }
   
   public static boolean getcallPhoneStatus(){
	   String sql="select count(1) from mb_config where config_name='callPhone' and config_value='开'";
	   return Sp.get().getDefaultJdbc().queryForObject(sql,Integer.class)>0;
   }
   
   public static boolean getMgStatus(){
	   String sql="select count(1) from mb_config where config_name='mg_game' and config_value='开'";
	   return Sp.get().getDefaultJdbc().queryForObject(sql,Integer.class)>0;
   }
   
   public static Customer getCustomerByUserName(String login_name){
	  return Customer.getCustomer(login_name);
   }

	public static List<BbinGame> getBbinGames(){
		 List<BbinGame> bbinList=(List<BbinGame>)Cache.get("bbin_game_list");
		 if(bbinList==null){
			 bbinList=BbinGame.getAll();
			 Cache.set("bbin_game_list", bbinList,gameCacheTime);
		 }
		return bbinList;
	}
}
