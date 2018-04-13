package com.ws.service;

import bsz.exch.service.Result;
import bsz.exch.service.Task;

public class SiteService {
	
	public static Result siteList(){
		Task task =Task.create("notice_list_8d");
		return task.perform();
	}

	public static Result getAds() {
		// TODO Auto-generated method stub
		Task task =Task.create("ad_list_8d");
		return task.perform();
	}

	public static Result getdic() {
		// TODO Auto-generated method stub
		
		Task task =Task.create("query_dist_bygroupcode_8d");
		task.addParam("groupcode", "gift_type");
		
		
		return task.perform();
		
	}

	public static Result querySelfAppBylogin_name(String login_name) {
		// TODO Auto-generated method stub
		Task task =Task.create("querySelfAppBylogin_name");
		task.addParam("login_name", login_name);
		task.setPage(0, 8);
		
		return task.perform();
	}

	public static Boolean getdic(String app_name) {
		// TODO Auto-generated method stub
		Task task =Task.create("query_dist_check_bygroupcode_8d");
		task.addParam("app_name", app_name);
		Result result = task.perform();
		if(result.success()){
			return result.getIntOfRes()>0;
		}
		return false;
		
		
	}

}
