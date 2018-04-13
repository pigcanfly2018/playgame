package controllers;

import java.util.Calendar;
import java.util.List;

import models.StatView;
import models.StatViewRowMap;
import play.cache.Cache;
import play.mvc.Controller;
import util.ExtPage;
import util.JSONResult;
import util.PageUtil;
import util.Sp;
import util.Sqler;

public class StatApp extends Controller{
	
	static String mysql="SELECT m0.index_date index_date,m1.kh account_cnt,m2.ck account_deposit_cnt,m3.dm deposit_sum,m3.dc deposit_cnt,"
			+ "m4.wm withdraw_sum,m4.wc withdraw_cnt,m5.po payout FROM "
			+ "(SELECT index_date FROM mb_index_date) m0 "
			+ "LEFT JOIN "
			+ "(SELECT COUNT(1) kh,DATE(create_date) index_date FROM mb_customer GROUP BY DATE(create_date)) m1 "
			+ "ON m0.index_date=m1.index_date "
			+ "LEFT JOIN "
			+ "(SELECT COUNT(1) ck,DATE(first_deposit_date) index_date FROM mb_customer WHERE cust_level >0  GROUP BY  DATE(first_deposit_date)) m2 "
			+ "ON m0.index_date=m2.index_date "
			+ "LEFT JOIN "
			+ "(SELECT SUM(t1.amount) dm,COUNT(1) dc,DATE(t2.create_date) index_date   FROM mb_deposit t1 LEFT JOIN mb_deposit_log t2 ON t1.deposit_id =t2.deposit_id  WHERE STATUS =3 "
			+ "AND  t2.deposit_id IS NOT NULL AND t2.after_status=3 GROUP BY  DATE(t2.create_date))  m3 "
			+ "ON m0.index_date=m3.index_date "
			+ "LEFT JOIN "
			+ "(SELECT SUM(t1.amount) wm,COUNT(1) wc,DATE(t2.create_date) index_date FROM mb_withdraw t1 LEFT JOIN mb_withdraw_log t2 ON t1.withdraw_id =t2.withdraw_id "
			+ "WHERE STATUS =5  AND  t2.withdraw_id IS NOT NULL  AND t2.after_status=5   GROUP BY  DATE(t2.create_date)) m4 "
			+ "ON m0.index_date=m4.index_date "
			+ "LEFT JOIN "
			+ "(SELECT SUM(payout) po,DATE(audit_date) index_date FROM mb_cash_gift WHERE STATUS =3 GROUP BY  DATE(audit_date)) m5 "
			+ "ON m0.index_date=m5.index_date "
			+ "WHERE m0.index_date <=CURDATE() order by m0.index_date desc";
	
	
	static String sqlcnt="SELECT count(1) FROM "
			+ "(SELECT index_date FROM mb_index_date) m0 "
			+ "LEFT JOIN "
			+ "(SELECT COUNT(1) kh,DATE(create_date) index_date FROM mb_customer GROUP BY DATE(create_date)) m1 "
			+ "ON m0.index_date=m1.index_date "
			+ "LEFT JOIN "
			+ "(SELECT COUNT(1) ck,DATE(first_deposit_date) index_date FROM mb_customer WHERE cust_level >0  GROUP BY  DATE(first_deposit_date)) m2 "
			+ "ON m0.index_date=m2.index_date "
			+ "LEFT JOIN "
			+ "(SELECT SUM(t1.amount) dm,COUNT(1) dc,DATE(t2.create_date) index_date   FROM mb_deposit t1 LEFT JOIN mb_deposit_log t2 ON t1.deposit_id =t2.deposit_id  WHERE STATUS =3 "
			+ "AND  t2.deposit_id IS NOT NULL AND t2.after_status=3 GROUP BY  DATE(t2.create_date))  m3 "
			+ "ON m0.index_date=m3.index_date "
			+ "LEFT JOIN "
			+ "(SELECT SUM(t1.amount) wm,COUNT(1) wc,DATE(t2.create_date) index_date FROM mb_withdraw t1 LEFT JOIN mb_withdraw_log t2 ON t1.withdraw_id =t2.withdraw_id "
			+ "WHERE STATUS =5  AND  t2.withdraw_id IS NOT NULL  AND t2.after_status=5   GROUP BY  DATE(t2.create_date)) m4 "
			+ "ON m0.index_date=m4.index_date "
			+ "LEFT JOIN "
			+ "(SELECT SUM(payout) po,DATE(audit_date) index_date FROM mb_cash_gift WHERE STATUS =3 GROUP BY  DATE(audit_date)) m5 "
			+ "ON m0.index_date=m5.index_date "
			+ "WHERE m0.index_date <=CURDATE()";
	
	 
	public static void getList(int start,int limit,int page,String sdate,String edate,String sort,String queryKey)throws Exception{
		String cacheTime="600s";
		limit = 30;
		Calendar cal = Calendar.getInstance();
		if(cal.get(Calendar.MONTH )+1 ==2 || cal.get(Calendar.MONTH )+1 == 4 || cal.get(Calendar.MONTH )+1 == 6 ||cal.get(Calendar.MONTH )+1 ==8 || cal.get(Calendar.MONTH )+1 ==9 || cal.get(Calendar.MONTH )+1 == 11 || cal.get(Calendar.MONTH )+1 == 1)
			limit = 31;
		if(cal.get(Calendar.MONTH )+1 == 3)
			limit = 29;
		
		limit = limit +cal.get(Calendar.DAY_OF_MONTH );
		ExtPage p =new ExtPage();
		List<StatView> roleList = (List<StatView>)Cache.get("StatView_list");
		Integer listCount = (Integer)Cache.get("StatView_listCount");
		if(roleList==null){
			System.out.println("get StatView_list for first time");
			Sqler sql =new Sqler(mysql);
			Sqler cntsql =new Sqler(sqlcnt);
			roleList=Sp.get().getDefaultJdbc().query(PageUtil.page(mysql,start,limit),sql.getParams().toArray(new Object[0]),new StatViewRowMap());
			int count=Sp.get().getDefaultJdbc().queryForObject(sqlcnt,cntsql.getParams().toArray(new Object[0]),Integer.class);
			Cache.set("StatView_list", roleList,cacheTime);
			Cache.set("StatView_listCount", count,cacheTime);
			p.data=JSONResult.conver(roleList,true);
			p.total=count; 
			renderJSON(p);
		}else{
			System.out.println("get StatView_list from cache");
			p.data=JSONResult.conver(roleList,true);
			p.total=listCount; 
			renderJSON(p);
		}
		
	}
	
	
	
	
	
	
	 
	 
	
	
	
	
	
	
	
	
	

}
