package controllers;

import play.Play;
import play.mvc.Before;
import play.mvc.Controller;

public class UrlApp extends Controller{
	
	@Before
	 public static void before(){
		if(IPApp.isxss()){
			renderText("您的输入存在非法字符!");
		}
	}
	
	public static void geturl() {
		renderText(Play.configuration.get("kehuduan.url"));
	}

}
