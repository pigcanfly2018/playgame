package com.product.bda.handler;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.product.bda.service.CashGift;
import com.product.bda.service.CashGiftService;
import com.product.bda.service.CreditService;
import com.product.bda.service.Customer;
import com.product.bda.service.CustomerService;
import com.product.bda.service.HitMole;
import com.product.bda.service.MoleHitService;
import com.product.bda.service.OrderNoService;


import bsz.exch.bean.FobbinInfo;
import bsz.exch.bean.Result;
import bsz.exch.bean.Task;
import bsz.exch.core.Before;
import bsz.exch.core.Handler;
import bsz.exch.core.InterFace;
import bsz.exch.core.JdbcResource;
import bsz.exch.core.Params;
import bsz.exch.core.Service;
import bsz.exch.core.Tx;


@Handler(name="molehitactive")
public class MoleHitHandler {
	
	private static Logger logger=Logger.getLogger(MoleHitHandler.class);
	@Before
	public FobbinInfo before(Task task, InterFace inter){
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		if(template==null){
			return new FobbinInfo(true,"the datasource is null!");
		}
		return new FobbinInfo(false,"");
	}
	
	
	/**
	 * 砸地鼠抽奖
	 * @param task
	 * @param inter
	 * @return
	 */
	@Service(name="dohit")
	@Params(validateField={"login_name","hittime"})
	@Tx
	public Result wuyi(Task task,InterFace inter){
		
		String login_name=task.getParam("login_name");
		Integer hittime=Integer.valueOf(task.getParam("hittime"));
		String ds=inter.getDataSource();
		
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(ds);
		MoleHitService hitService =new MoleHitService(template,ds);
		CustomerService custService =new CustomerService(template,ds);
		Customer cust= custService.getCustomer(login_name);
		//插入砸地鼠记录
		int giftid=hitService.doHitActive(login_name,cust.cust_id,hittime);
		//获取用户等级
		Integer cust_level = cust.cust_level;
		//领奖成功
		if(giftid!=-1 && hittime>0){
			
			//发送礼金
			CashGiftService  cashGiftService  =new CashGiftService(template,ds);
			CreditService creditService = new CreditService(template,ds);
			String cash = null;
			
			String giftCode=String.valueOf(giftid);//MyRandom.getRandom(8);
			StringBuffer sb = new StringBuffer();
			if(hittime==1){
				//插入记录
				if(cust_level==0){
					 cash = HitMole.luckLevel();
				}else if(cust_level==1){
					cash = HitMole.luckLevel1();
				}else if(cust_level==2){
					cash = HitMole.luckLevel2();
				}else if(cust_level==3){
					cash = HitMole.luckLevel3();
				}else if(cust_level==4){
					cash = HitMole.luckLevel4();
				}else if(cust_level==5){
					cash = HitMole.luckLevel5();
				}else if(cust_level==6){
					cash = HitMole.luckLevel6();
				}
				
				insertRecord(cash,giftCode,cashGiftService,creditService,cust);
				sb.append("\\n本次共砸中1次，恭喜您获得了"+cash+"元 \\n奖金，钱已打入您的账户。");
			}else{
				sb.append("\\n本次共砸中"+hittime+"次，分别奖励\\n");
				StringBuffer sbcash = new StringBuffer();
				for(int i=0;i<hittime;i++){
					//插入记录
					if(cust_level==0){
						 cash = HitMole.luckLevel();
					}else if(cust_level==1){
						cash = HitMole.luckLevel1();
					}else if(cust_level==2){
						cash = HitMole.luckLevel2();
					}else if(cust_level==3){
						cash = HitMole.luckLevel3();
					}else if(cust_level==4){
						cash = HitMole.luckLevel4();
					}else if(cust_level==5){
						cash = HitMole.luckLevel5();
					}else if(cust_level==6){
						cash = HitMole.luckLevel6();
					}
					
					insertRecord(cash,giftCode,cashGiftService,creditService,cust);
					sbcash.append(cash+"、");
				}
				sb.append(sbcash.toString().substring(0, sbcash.toString().length()-1)).append("元，钱已打入您的账户。");
			}
			String msg = sb.toString();
			
			Result result =Result.create(task.getId(), task.getFunId());
			result.setFlag("-1");
			result.setIsList(false);
			result.setRes(msg);
			return result;
		}else{
			//领奖失败
			String msg = "\\n很遗憾!\\n您没能砸中，明天再来吧。";
			Result result =Result.create(task.getId(), task.getFunId());
			result.setFlag("-1");
			result.setIsList(false);
			result.setRes(msg);
			return result;
			
		}
		
	
	}


	private void insertRecord(String cash,String giftCode, CashGiftService cashGiftService, CreditService creditService,Customer cust) {
		//修改积分
		String dep_no = OrderNoService.createLocalNo("Hit");
		
		// TODO Auto-generated method stub
		 CashGift gift =new CashGift();
       	 gift.gift_code=giftCode;
       	 gift.login_name=cust.login_name;
       	 gift.deposit_credit=new BigDecimal(0);
       	 gift.valid_credit=new BigDecimal(0);
       	 gift.net_credit=new BigDecimal(0);
       	 gift.rate=Float.valueOf(1);
       	 gift.payout=new BigDecimal(cash);
       	 gift.gift_type="砸地鼠活动礼金";
       	 gift.remarks="砸地鼠活动礼金";
       	 gift.status=1;
       	 gift.gift_no=dep_no;
    	 gift.cust_id=cust.cust_id;
       	 gift.cs_date=new Date(System.currentTimeMillis());
       	 gift.real_name=cust.real_name;
       	 gift.cust_level=cust.cust_level;
         gift.kh_date=cust.create_date;
       	 gift.create_user=cust.login_name;
       	 gift.create_date=new Date(System.currentTimeMillis());
       	 
       	 Long giftId= cashGiftService.NTcreat(gift);//.creat(gift.kh_date, gift.login_name, gift.real_name, gift.cust_level, gift.gift_type, gift.gift_code, gift.deposit_credit,
       			 //gift.net_credit, gift.valid_credit, gift.rate, gift.payout, gift.cs_date, gift.remarks, gift.create_date, gift.create_user,
       			 //gift.status, gift.audit_date, gift.audit_user, gift.audit_msg, gift.cust_id, dep_no);
       	 cashGiftService.NTAuditGift(giftId, 3, "系统",giftCode);
       	 //修改玩家额度
       	 boolean k=creditService.add(cust.login_name, new BigDecimal(cash),"系统",  "添加礼金"+gift.gift_type, cust.login_name, gift.gift_no);
	}
	
}
