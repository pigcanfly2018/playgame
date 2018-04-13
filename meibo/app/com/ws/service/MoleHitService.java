package com.ws.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.yeargift.YearGiftCustBean;
import com.yeargift.YearStatBean;

import bsz.exch.service.Result;
import bsz.exch.service.Task;

public class MoleHitService {

	public static Result checkIfHit(String login_name, String startDate, String endDate) {
		// TODO Auto-generated method stub
		Task task =Task.create("checkMoleMitByLogin_name");
		task.addParam("login_name", login_name);
		task.addParam("startDate", startDate);
		task.addParam("endDate", endDate);
		task.addParam("product", task.getProduct());
		Result result = task.perform();
		
		if(result.success()){
			return result;
		}
		return null;
	}

	public static String hit(String hitcount,String login_name) {
		// TODO Auto-generated method stub
		Task task =Task.create("dohit");
		task.addParam("hittime", hitcount);
		task.addParam("login_name", login_name);
		task.addParam("product", task.getProduct());
		Result result = task.perform();
		if(result.success()){
			return result.getRes();
		}
		return null;
	}
	
}
