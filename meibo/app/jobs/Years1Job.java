package jobs;

import java.util.Calendar;
import java.util.Date;

import net.sf.oval.internal.Log;
import play.Play;
import play.jobs.Every;
import play.jobs.Job;
import util.DateUtil;

import com.yeargift.YearGiftCache;
import com.yeargift.YearGiftCustBean;

import controllers.YearGiftApp;
//@Every("3s")
public class Years1Job extends Job{
	static Log log =Log.getLog(Years1Job.class);	
	private static Integer init =0;
	static int romRand(int length){
		return (int)(Math.random()*length);
	}
	public void doJob()throws Exception {
		
		//检查启动抢红包
		Date now =new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(now);
		int month=c.get(Calendar.MONTH);
		int hours=c.get(Calendar.HOUR_OF_DAY);
		int minutes=c.get(Calendar.MINUTE);
		if((month==7&&hours==8&&minutes==18)||(month==7&&hours==20&&minutes==18)){
			if(YearGiftApp.stop==1){
			   YearGiftApp.stop=0;
			   log.info(DateUtil.dateToString(now, "yyyyMMddHHmmss")+"抢红包任务开始");
			}
		}
		
		if((month==7&&hours==8&&minutes==33)||(month==7&&hours==20&&minutes==33)){
			if(YearGiftApp.stop==0){
			   YearGiftApp.stop=1;
			   log.info(DateUtil.dateToString(now, "yyyyMMddHHmmss")+"抢红包任务关闭");
			}
		}
		
		//只执行一次
		if(init.intValue()==0){

			YearGiftCustBean.ramdom1=8;
			YearGiftCustBean.ramdom2=8;
			Object o=Play.configuration.get("years.stop");
			 String stop="1";
			if(o!=null){
			    stop=o.toString();
			}
			YearGiftApp.stop=Integer.parseInt(stop);
			System.out.println("初始化stop标示 stop="+stop);
			YearGiftCache.get().reload();
			init=1;
		}
		
		//刷缓存
		String hours0=DateUtil.getDateString("HH");
		String mouth=DateUtil.getDateString("yyyyMM");
		if(mouth.equals("201608")){
			if(hours0.equals("08")||hours0.equals("20")){
				try{   
					long t1 =System.currentTimeMillis();
				    YearGiftCache.get().reload();
				    if(YearGiftApp.stop==0){
				    	//int r=romRand(8);
				    	//Random.update(r, r+1);
				    	YearGiftCustBean.ramdom1=8;
				    	YearGiftCustBean.ramdom2=8;
				    }
				    long t2 =System.currentTimeMillis();
				    log.info("本次刷新红包缓存消耗时间:"+((t2-t1)/1000)+"秒");
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		
	}
}