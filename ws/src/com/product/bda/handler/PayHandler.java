package com.product.bda.handler;

import net.sf.json.JSONObject;

import org.springframework.jdbc.core.JdbcTemplate;

import com.product.bda.service.Customer;
import com.product.bda.service.CustomerService;
import com.product.bda.service.DPayService;
import com.product.bda.service.Withdraw;
import com.product.bda.service.WithdrawService;

import bsz.exch.bank.BankInfo;
import bsz.exch.bank.DPApi;
import bsz.exch.bank.PayResource;
import bsz.exch.bean.FobbinInfo;
import bsz.exch.bean.Result;
import bsz.exch.bean.Task;
import bsz.exch.core.Before;
import bsz.exch.core.Handler;
import bsz.exch.core.InterFace;
import bsz.exch.core.JdbcResource;
import bsz.exch.core.Params;
import bsz.exch.core.Service;
import bsz.exch.utils.RandomUtil;

@Handler(name="PAY")
public class PayHandler {
	
	@Before
	public FobbinInfo before(Task task,InterFace inter){
		String product=task.getParam("product");
		if(!"8da".equals(product)){
			return  new FobbinInfo(true,"No product support!");
		}
		return new FobbinInfo(false,"");
	}

	@Service(name="deposit")
	@Params(validateField={"login_name","product","amount","from_bank","deposit_type"})
	public Result deposit(Task task,InterFace inter){
		String login_name = task.getParam("login_name");
		String product = task.getParam("product");
		String amount = task.getParam("amount");
		String fromBank = task.getParam("from_bank");
		String deposit_type = task.getParam("deposit_type");
		
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		String ds=inter.getDataSource();
		CustomerService custService = new CustomerService(template,ds);
		Customer cust =custService.getCustomer(login_name);
		if(cust.flag==1){
			String order_no=PayResource.instance().getConfig("dpay."+product+".pre")+"D"+System.nanoTime();
			String note=RandomUtil.getRandom(9);
		    String bank_id="1";
			String company_order_num=order_no;
			String company_user=login_name;
			String estimated_payment_bank=fromBank;
			String deposit_mode="1";
			String group_id="0";
			String memo="0";
			String note_model="2";
			String terminal="1";
			//银行
			if("1".equals(deposit_type)){
				if(BankInfo.getBank(fromBank)==null){
					return Result.createError(task.getId(), task.getFunId(), "6000", "Null Result Error!");
				}
				bank_id=fromBank;
			}
			//财付通
			if("2".equals(deposit_type)){
				bank_id="31"; 
				fromBank="31";
			}
			//二维码
			if("3".equals(deposit_type)){
				bank_id="30";
				fromBank="30";
				note_model="1";
				deposit_mode="3";
				if(cust.alipay_name!=null&&(cust.alipay_name.length()>0)){
					note=cust.alipay_name.substring(cust.alipay_name.length()-1, cust.alipay_name.length());
				}else{
				    note=cust.real_name.substring(cust.real_name.length()-1, cust.real_name.length());
				}
				
				if(cust.alipay_account.contains("@")){
					String [] args=cust.alipay_account.split("@");
				    note=note+args[0].substring(0, 3)+"@"+args[1];
				}else{
					note=note+cust.alipay_account.substring(0,3);
					note=note+cust.alipay_account.substring(cust.alipay_account.length()-4,cust.alipay_account.length());
				}
			}
	
			if("4".equals(deposit_type)){
				bank_id=fromBank;
				deposit_mode="2";
			}
			
			if(BankInfo.getBank(bank_id)==null){
				return Result.createError(task.getId(), task.getFunId(), "6000", "Null Result Error!");
			}
			DPayService dpService =new DPayService(template,ds);
			DPApi dpApi=DPApi.get(product);
			
			Long dp_id=dpService.createDepositOrder(dpApi.getCompanyId(), bank_id, BankInfo.getBank(bank_id).getAbbr(), order_no, amount, login_name, 
					fromBank, BankInfo.getBank(fromBank).getAbbr(), deposit_mode, group_id, dpApi.getDepositNotifyUrl(),
					memo, note, note_model, terminal);
			JSONObject js=DPApi.get(product).deposit(bank_id, amount, company_order_num, company_user,
					estimated_payment_bank, deposit_mode, group_id, memo, note, note_model, terminal);
			if(js==null){
				return Result.createError(task.getId(), task.getFunId(), "6000", "Null Result Error!");
			}
			int status =js.getInt("status");
			if(status==0){
				dpService.addErrorDepositResult(dp_id, "0", js.getString("error_msg"));
				return Result.createError(task.getId(), task.getFunId(), "6001", js.getString("error_msg"));
			}else{
				
				String break_url="";
				Result r =Result.create(task.getId(), task.getFunId());
				r.addFields(new String[]{"order_no","bank_id","account","account_name","bank_dot","email","mode","break_url","note"});
				r.setFlag("-1");
				r.setIsList(true);
				r.setLength(1);
				if(js.containsKey("break_url")){
					break_url=js.getString("break_url");
					dpService.addDepositResult(dp_id, "",
							"",js.getString("amount") , 
							"", js.getString("company_order_num"),
							js.getString("datetime"), "", 
							js.getString("mownecum_order_num"),
							String.valueOf(status),
							String.valueOf(js.getInt("mode")) , 
							"",
							break_url,
							"","");
					r.addRecord(new String[]{order_no,"","","",
							"","",String.valueOf(js.getInt("mode")),
							break_url,""});
				}else{
					dpService.addDepositResult(dp_id, js.getString("bank_card_num"),
							js.getString("bank_acc_name"),js.getString("amount") , 
							js.getString("email"), js.getString("company_order_num"),
							js.getString("datetime"), js.getString("note"), 
							js.getString("mownecum_order_num"),
							String.valueOf(status),
							String.valueOf(js.getInt("mode")) , 
							js.getString("issuing_bank_address"),
							break_url,
							js.getString("collection_bank_id"),"");
					r.addRecord(new String[]{order_no,js.getString("collection_bank_id"),js.getString("bank_card_num"),js.getString("bank_acc_name"),
							js.getString("issuing_bank_address"),js.getString("email"),String.valueOf(js.getInt("mode")),
							break_url,js.getString("note")});
				}
				
				
				
				
				return r;
			}
		}
		return Result.createError(task.getId(), task.getFunId(), "6000", "Null Result Error!");
	}
	
	/**
	 * 取消订单
	 * @param task
	 * @param inter
	 * @return
	 */
	@Service(name="cancle_deposit")
	@Params(validateField={"product","order_no"})
	public Result cancelWithdraw(Task task,InterFace inter){

		String product = task.getParam("product");
		String order_no =task.getParam("order_no");
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		String ds=inter.getDataSource();
		DPayService dpService =new DPayService(template,ds);
		String plat_no=dpService.findDepositPlatNo(order_no);
		if(plat_no!=null){
			DPApi dpApi=DPApi.get(product);
			JSONObject js=dpApi.DepositCancel(plat_no, order_no);
			int status =js.getInt("status");
			if(status==1){
				if(dpService.cancleDeposit(order_no)){
					Result r =Result.create(task.getId(), task.getFunId());
					r.setFlag("-1");
					r.setIsList(false);
					r.setLength(1);
					r.setRes("1");
					return r;
				}
			}
			return Result.createError(task.getId(), task.getFunId(), "7001", "处理失败!");
		}else{
			return Result.createError(task.getId(), task.getFunId(), "7001", "平台订单号没找到!");
		}
	}
	
	
	/**
	 * 支付提款
	 * @param task
	 * @param inter
	 * @return
	 */
	@Service(name="withdraw")
	@Params(validateField={"login_name","product","order_no"})
	public Result withdraw(Task task,InterFace inter){
		
		String login_name = task.getParam("login_name");
		String product = task.getParam("product");
		String wit_no = String.valueOf(task.getParam("order_no"));	
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		String ds=inter.getDataSource();
		WithdrawService withdrawService =new WithdrawService(template,ds);
		Withdraw wit= withdrawService.getCanPayWithdraw(wit_no, login_name);
        if(wit==null){
        	return Result.createError(task.getId(), task.getFunId(), "7001", "没有发现匹配的提款订单!");
        }	
		Long withdraw_id=wit.withdraw_id;
        if(!withdrawService.updateWithdrawDpayStatus7(withdraw_id)){
        	return Result.createError(task.getId(), task.getFunId(), "7001", "更新订单状态失败!");
        }
        String order_no=PayResource.instance().getConfig("dpay."+product+".pre")+"W"+System.nanoTime();
        DPayService dpService =new DPayService(template,ds);
        DPApi dpApi=DPApi.get(product);
        Long dp_id=dpService.createWithdrawOrder (dpApi.getCompanyId(),wit.bank_id,BankInfo.getBank(wit.bank_id).getAbbr(), order_no, wit.amount,
        		wit.account, wit.account_name, login_name, wit.bank_name, wit.location, "");
        if(dp_id==null){
        	withdrawService.updateWithdrawDpayStatus8(withdraw_id,"创建订单失败");
        	return Result.createError(task.getId(), task.getFunId(), "7002", "创建订单失败！");
        }else{
        	withdrawService.updateWithdrawDpayInfo(withdraw_id, order_no);
        	JSONObject js =dpApi.withdraw(wit.bank_id, order_no, wit.amount.toString(), wit.account,  wit.account_name,
        			login_name, wit.bank_name, wit.location);
        	if(js==null){
				return Result.createError(task.getId(), task.getFunId(), "7003", "");
			}
        	int status =js.getInt("status");
        	if(status==0){
        		dpService.createFalseWithdraw(js.getString("error_msg"),dp_id);
				return Result.createError(task.getId(), task.getFunId(), "7004", js.getString("error_msg"));
        	}else{
        		//创建成功
        		dpService.createTrueWithdraw(js.getString("mownecum_order_num"),js.getString("transaction_charge"), dp_id);
        		Result r =Result.create(task.getId(), task.getFunId());
				r.setFlag("-1");
				r.setIsList(false);
				r.setLength(1);
				r.setRes("1");
				return r;
        	}
        }
	}
	
	/**
	 * 查询平台方 提款是否成功
	 * @param task
	 * @param inter
	 * @return
	 */
	@Service(name="queryWithdraw")
	@Params(validateField={"product","order_no"})
	public Result queryWithdraw(Task task,InterFace inter){
		String product = task.getParam("product");
		String order_no = String.valueOf(task.getParam("order_no"));
		JdbcTemplate template=JdbcResource.instance().getJdbcTemplate(inter.getDataSource());
		String ds=inter.getDataSource();
		DPayService dpService =new DPayService(template,ds);
	    DPApi dpApi=DPApi.get(product);
		String plat_no =dpService.findWithdrawPlatNo(order_no);
		if(plat_no==null){
			 return Result.createError(task.getId(), task.getFunId(), "7004", "Can't Find Order!");
		} 
		JSONObject  result=dpApi.QueryWithdrawal(plat_no, order_no);
		Result r =Result.create(task.getId(), task.getFunId());
		r.setFlag("-1");
		r.setIsList(true);
		r.setLength(1);
		r.addFields(new String[]{"order_no","plat_no","status","amount","detail","transaction_charge","error_msg"});
		r.addRecord(new String[]{result.getString("company_order_num"),result.getString("mownecum_order_num"),
				String.valueOf(result.getInt("status")),result.getString("amount"),
				result.getString("detail"),result.getString("exact_transaction_charge"),result.getInt("status")==9?result.getString("error_msg"):"",""});
		return r;
	}
		
	

}
