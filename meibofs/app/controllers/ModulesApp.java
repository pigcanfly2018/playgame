package controllers;

import java.util.List;

import models.Item;
import models.User;
import play.mvc.Controller;
import util.Constant;

public class ModulesApp extends Controller{
	
	public static void roleApp(String tabId){
		render(tabId);
	}
	public static void funcApp(String tabId){
		render(tabId);
	}
	public static void userApp(String tabId){
		render(tabId);
	}
	public static void itemApp(String tabId){
		render(tabId);
	}
	public static void huitianfuApp(String tabId){
		render(tabId);
	}

	
	public static void otherpayApp(String tabId){
		String userName=session.get(Constant.userName);
		render(userName,tabId);
	}
	/**
	 * 客户管理
	 * @param tabId
	 */
	public static void customerApp(String tabId){
		String userName=session.get(Constant.userName);
		render(userName,tabId);
		//User user = User.NTgetByLoginName(userName);
		//String phonecode = user.phonecode;
		//render(userName,tabId,phonecode);
	}
	
	/**
	 * 取款管理
	 * @param tabId
	 */
	public static void depositApp(String tabId){
		String userName=session.get(Constant.userName);
		render(tabId,userName);
	}
	
	/**
	 * 额度记录
	 * @param tabId
	 */
	public static void creditLogApp(String tabId){
		render(tabId);
	}
	
	/**
	 * 代理额度记录
	 * @param tabId
	 */
	public static void agentCreditLogApp(String tabId){
		render(tabId);
	}
	
	/**
	 * 平台转账记录
	 * @param tabId
	 */
	public static void transferlLogApp(String tabId){
		render(tabId);
	}
	
	/**
	 * 平台转账记录
	 * @param tabId
	 */
	public static void gameTransferDataApp(String tabId){
		render(tabId);
	}
	
	/**
	 * 提款管理
	 * @param tabId
	 */
	public static void withdrawApp(String tabId){
		String userName=session.get(Constant.userName);
		render(tabId,userName);
	}
	
	/**
	 * 代理提款管理
	 * @param tabId
	 */
	public static void agentwithdrawApp(String tabId){
		String userName=session.get(Constant.userName);
		render(tabId,userName);
	}
	
	public static void claimApp(String tabId){
		String userName=session.get(Constant.userName);
		render(tabId,userName);
	}
	
	public static void claimHisApp(String tabId){
		String userName=session.get(Constant.userName);
		render(tabId,userName);
	}
	public static void hitApp(String tabId){
		render(tabId);
	}
	
	/**
	 * 银改提案
	 * @param tabId
	 */
	public static void custformApp(String tabId){
		String userName=session.get(Constant.userName);
		render(tabId,userName);
	}
	/**
	 * 银行管理
	 * @param tabId
	 */
	public static void banksApp(String tabId){
		render(tabId);
	}
	/**
	 * 额度修正
	 * @param tabId
	 */
	public static void creditfixApp(String tabId){
		String userName=session.get(Constant.userName);
		render(tabId,userName);
	}
	
	/**
	 * 代理额度修正
	 * @param tabId
	 */
	public static void agentcreditfixApp(String tabId){
		String userName=session.get(Constant.userName);
		render(tabId,userName);
	}
	
	/**
	 * 代理分成修改
	 * @param tabId
	 */
	public static void sharemodifyApp(String tabId){
		String userName=session.get(Constant.userName);
		render(tabId,userName);
	}
	
	/**
	 * 代理佣金管理
	 * @param tabId
	 */
	public static void agentCommissionApp(String tabId){
		String userName=session.get(Constant.userName);
		render(tabId,userName);
	}
	
	/**
	  * 额度修正
	 * @param tabId
	 */
	public static void transferApp(String tabId){
		render(tabId);
	}
	
	/**
	 * 预开户
	 * @param tabId
	 */
	public static void pregApp(String tabId){
		render(tabId);
	}
	/**
	 * 银行
	 * @param tabId
	 */
	public static void bankApp(String tabId){
		render(tabId);
	}
	
	/**
	 * 投注记录
	 * @param tabId
	 */
	public static void betDetailApp(String tabId){
		render(tabId);	
	}
	/**
	 * 转账记录
	 * @param tabId
	 */
	public static void accountTransfer(String tabId){
		render(tabId);	
	}
	public static void depositHisApp(String tabId){
		String userName=session.get(Constant.userName);
		render(tabId,userName);	
	}
	public static void widthdrawHisApp(String tabId){
		String userName=session.get(Constant.userName);
		render(tabId,userName);	
	}
	public static void userLogApp(String tabId){
		String userName=session.get(Constant.userName);
		render(tabId,userName);	
	}
	public static void agentWidthdrawHisApp(String tabId){
		render(tabId);	
	}
	
	public static void phoneRecordApp(String tabId){
		render(tabId);	
	}
	
	
	//代理下线记录
	public static void subcustomerApp(String tabId){
		String userName=session.get(Constant.userName);
		render(tabId,userName);	
	}
	
	public static void giftHisApp(String tabId){
		String userName=session.get(Constant.userName);
		render(tabId,userName);	
	}
	
	/**
	 * 洗码任务
	 * @param tabId
	 */
	public static void washTaskApp(String tabId){
		render(tabId);	
	}
	/**
	 * 反水记录
	 * @param tabId
	 */
	public static void washRecApp(String tabId){
		render(tabId);	
	}
	/**
	 * 游戏登入记录
	 * @param tabId
	 */
	public static void gamelog(String tabId){
		render(tabId);
	}
	
	/**
	 * 礼金添加
	 * @param tabId
	 */
	public static void cashgiftApp(String tabId){
		String userName=session.get(Constant.userName);
		render(tabId,userName);
	}
	
	
	/**
	 * 排行榜数据添加
	 * @param tabId
	 */
	public static void billboardApp(String tabId){
		String userName=session.get(Constant.userName);
		render(tabId,userName);
	}
	
	/**
	 * 周年庆红包查询
	 * @param tabId
	 */
	public static void yearGiftApp(String tabId){
		String userName=session.get(Constant.userName);
		render(tabId,userName);
	}
	
	/**
	 * 活力红包查询
	 * @param tabId
	 */
	public static void huoliGiftApp(String tabId){
		String userName=session.get(Constant.userName);
		render(tabId,userName);
	}
	
	/**
	 * 红包查询
	 * @param tabId
	 */
	public static void hongbaoApp(String tabId){
		String userName=session.get(Constant.userName);
		render(tabId,userName);
	}
	
	
	/**
	 * 投注记录查询
	 * @param tabId
	 */
	public static void betRecordApp(String tabId){
		String userName=session.get(Constant.userName);
		render(tabId,userName);
	}
	
	
	/**
	 * 每月盈亏
	 * @param tabId
	 */
	public static void profit(String tabId){
		String userName=session.get(Constant.userName);
		render(tabId,userName);
	}
	
	/**
	 * 代理管理
	 * @param tabId
	 */
	public static void agentApp(String tabId){
		String userName=session.get(Constant.userName);
		render(tabId,userName);
	}
	
	/**
	 * 代理下线报表
	 * @param tabId
	 */
	public static void agentStatApp(String tabId){
		String userName=session.get(Constant.userName);
		render(tabId,userName);
	}
	
	/**
	 * 统计
	 * @param tabId
	 */
	public static void statApp(String tabId){
		render(tabId);
	}
	
	public static void stat1App(String tabId){
		render(tabId);
	}
	
	
	public static void customerViewApp(String tabId){
		render(tabId);
	}
	
	public static void customerAllViewApp(String tabId){
		render(tabId);
	}
	
	public static void jvbaoApp(String tabId){
		render(tabId);
	}
	
	public static void yingbaoApp(String tabId){
		render(tabId);
	}
	
	public static void yinbaoApp(String tabId){
		render(tabId);
	}
	
	public static void xinbeiApp(String tabId){
		render(tabId);
	}
	
	public static void sanvApp(String tabId){
		render(tabId);
	}
	
	public static void xunhuibaoApp(String tabId){
		render(tabId);
	}
	public static void yingtongbaoApp(String tabId){
		render(tabId);
	}
	public static void dingyiApp(String tabId){
		render(tabId);
	}
	public static void saomafuApp(String tabId){
		render(tabId);
	}
	public static void xunfutongApp(String tabId){
		render(tabId);
	}
	
	public static void tonghuiApp(String tabId){
		render(tabId);
	}
	
	public static void huiboApp(String tabId){
		render(tabId);
	}
	
	public static void rpnApp(String tabId){
		render(tabId);
	}

	
	public static void dict(){
		List items=Item.NTgetAll();
		render(items);
	}
	
	/**
	 * 公告管理
	 * @param tabId
	 */
	public static void announcementApp(String tabId){
		render(tabId);
	}
	
	/**
	 * adApp
	 * @param tabId
	 */
	public static void adApp(String tabId){
		render(tabId);
	}
	
	/**
	 * 
	 * @param tabId
	 */
	public static void noticeApp(String tabId){
		render(tabId);
	}
	
	
	public static void discountApp(String tabId){
		render(tabId);
	}
	
	/**
	 * 留言板
	 * @param tabId
	 */
	public static void boardApp(String tabId){
		render(tabId);
	}
	
	/**
	 * 系统配置
	 * @param tabId
	 */
	public static void configApp(String tabId){
		render(tabId);
	}
	
	/**
	 * 在线支付配置
	 * @param tabId
	 */
	public static void payOnlineApp(String tabId){
		render(tabId);
	}
	
	/**
	 * 修改密码
	 * @param tabId
	 */
	public static void passwordApp(String tabId){
		render(tabId);
	}
	
	/**
	 * 
	 * @param tabId
	 */
	public static void userMsgApp(String tabId){
		render(tabId);
	}
	
	/**
	 * 统计管理
	 * @param tabId
	 */
	public static void countApp(String tabId){
		render(tabId);
	}
	
	public static void yeeOrderApp(String tabId){
		render(tabId);
	}

	
	public static void dinpayViewApp(String tabId){
		render(tabId);
	}
	/**
	 * 
	 * @param tabId
	 */
	public static void letterApp(String tabId){
		render(tabId);
	}
	public static void scoreHisApp(String tabId){
		render(tabId);
	}
	
	public static void signHisApp(String tabId){
		render(tabId);
	}
	
	public static void trophyApp(String tabId){
		render(tabId);
	}
	public static void eggGiftApp(String tabId){
		String userName=session.get(Constant.userName);
		render(tabId,userName);
	}
	public static void rankingListApp(String tabId){
		render(tabId);
	}
	
	public static void ptGameApp(String tabId){
		String userName=session.get(Constant.userName);
		render(tabId,userName);
	}
	
	public static void agGameApp(String tabId){
		String userName=session.get(Constant.userName);
		render(tabId,userName);
	}
	
	public static void sbGameApp(String tabId){
		String userName=session.get(Constant.userName);
		render(tabId,userName);
	}
	
	public static void mgGameApp(String tabId){
		String userName=session.get(Constant.userName);
		render(tabId,userName);
	}
	
	public static void auditInfoApp(String tabId){
		render(tabId);
	}

	public static void winListApp(String tabId){
		String userName=session.get(Constant.userName);
		render(tabId,userName);
	}
}



