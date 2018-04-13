package com.ws.service;

import java.math.BigDecimal;

import bsz.exch.service.Plat;
import bsz.exch.service.Result;
import bsz.exch.service.Task;

public class PlatService {
	
	public static BigDecimal balance(Plat plat,String login_name,String login_ip){
		Task task =Task.create("plat_balance_8d");
		task.addParam("plat", plat.toString());
		task.addParam("login_name", login_name);
		task.addParam("login_ip", login_ip);
		task.addParam("product", task.getProduct());
		Result result= task.perform();
		BigDecimal balance = new BigDecimal(0);
		if(result.success()){
			balance= result.getBigDecimalOfRes();
		}
		balance=balance.setScale(2,BigDecimal.ROUND_HALF_EVEN);
		return balance;
	}
	
	//转出游戏厅 (需要判断厅是否可转)
	public static boolean transferOut(Plat plat,String login_name,String login_ip,String log_type,String remarks,String create_user,
			String order_no,BigDecimal amount){
		Task task =Task.create("plat_out_8d");
		task.addParam("plat", plat.toString());
		task.addParam("login_name", login_name);
		task.addParam("login_ip", login_ip);
		task.addParam("product", task.getProduct());
		task.addParam("log_type", log_type);
		task.addParam("remarks", "【"+plat+" -> 主账户 】"+remarks);
		task.addParam("create_user", create_user);
		task.addParam("order_no", order_no);
		task.addParam("amount", amount);
		Result result= task.perform();

		if(result.success()){
			return result.getIntOfRes()==1;
		}
		return false;
	}
	//转进游戏厅
	public static boolean transferIn(Plat plat,String login_name,String login_ip,String log_type,String remarks,String create_user,
			String order_no,BigDecimal amount){
		Task task =Task.create("plat_in_8d");
		task.addParam("plat", plat.toString());
		task.addParam("login_name", login_name);
		task.addParam("login_ip", login_ip);
		task.addParam("product", task.getProduct());
		task.addParam("log_type", log_type);
		task.addParam("remarks","【主账户 -> "+plat+"】 "+ remarks);
		task.addParam("create_user", create_user);
		task.addParam("order_no", order_no);
		task.addParam("amount", amount);
		Result result= task.perform();
		if(result.success()){
			return result.getIntOfRes()==1;
		}
		return false;
	}
	
	/**
	 * 转出游戏厅 除了之外/全部转出游戏厅
	 * @param plat
	 * @param login_name
	 * @param login_ip
	 * @param log_type
	 * @param remarks
	 * @param create_user
	 * @param order_no
	 * @param amount
	 * @return
	 */
	public static void transferOutEx(Plat plat,String login_name,String login_ip,String log_type,String remarks,String create_user,
			String order_no,BigDecimal amount){
		if(!plat.name().equals(Plat.AG.toString())){
			transferOut(Plat.AG,login_name,login_ip,log_type,remarks,create_user,order_no,amount);
		}
		if(!plat.name().equals(Plat.BBIN.toString())){
			transferOut(Plat.BBIN,login_name,login_ip,log_type,remarks,create_user,order_no,amount);
		}
		if(!plat.name().equals(Plat.PT.toString())){
			transferOut(Plat.PT,login_name,login_ip,log_type,remarks,create_user,order_no,amount);
		}
		if(!plat.name().equals(Plat.KG.toString())){
			transferOut(Plat.KG,login_name,login_ip,log_type,remarks,create_user,order_no,amount);
		}
		if(!plat.name().equals(Plat.MG.toString())){
			transferOut(Plat.MG,login_name,login_ip,log_type,remarks,create_user,order_no,amount);
		}		
	}
	
	public static void transferOutAll(String login_name,String login_ip,String log_type,String remarks,String create_user,
			String order_no,BigDecimal amount){
			transferOut(Plat.AG,login_name,login_ip,log_type,remarks,create_user,order_no,amount);
			transferOut(Plat.BBIN,login_name,login_ip,log_type,remarks,create_user,order_no,amount);
			transferOut(Plat.PT,login_name,login_ip,log_type,remarks,create_user,order_no,amount);
			transferOut(Plat.KG,login_name,login_ip,log_type,remarks,create_user,order_no,amount);
			transferOut(Plat.MG,login_name,login_ip,log_type,remarks,create_user,order_no,amount);
			transferOut(Plat.VS,login_name,login_ip,log_type,remarks,create_user,order_no,amount);
	}
	
	
	//plat|login_name|login_ip|product
	public static String forward(Plat plat,String login_name,String login_ip,String site,String vistor){
		Task task =Task.create("plat_forward_8d");
		task.addParam("plat", plat.toString());
		task.addParam("login_name", login_name);
		task.addParam("login_ip", login_ip);
		task.addParam("product", task.getProduct());
		task.addParam("site", site);
		task.addParam("vistor", vistor);
		Result result= task.perform();
		if(result.success()){
			return result.getRes();
		}
		return "";
	}
	
	
	
	public static boolean logout(Plat plat,String login_name,String login_ip){
		Task task =Task.create("plat_logout_8d");
		task.addParam("plat", plat.toString());
		task.addParam("login_name", login_name);
		task.addParam("login_ip", login_ip);
		task.addParam("product", task.getProduct());
		Result result= task.perform();
		if(result.success()){
			return result.getIntOfRes()==1;
		}
		return false;
	}
	
	public static boolean pwd(Plat plat,String login_name,String login_ip,String password){
		Task task =Task.create("plat_update_pwd");
		task.addParam("plat", plat.toString());
		task.addParam("login_name", login_name);
		task.addParam("password", password);
		task.addParam("login_ip", login_ip);
		task.addParam("product", task.getProduct());
		Result result= task.perform();
		if(result.success()){
			return result.getIntOfRes()==1;
		}
		return false;
	}
	
	
	
	
	public static void main(String []args){
		
		PlatService platService =new PlatService();
		System.out.println(platService.balance(Plat.AG, "lance008", "8.8.8.8"));
		System.out.println(platService.forward(Plat.BBIN, "lance008", "8.8.8.8","xd","0"));
		
	}
	

}
