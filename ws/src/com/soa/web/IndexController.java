package com.soa.web;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import bsz.exch.cache.ConfigCache;
import bsz.exch.core.InterFace;

@Controller
public class IndexController {

	
	
	 @RequestMapping(path = "/reload", method = RequestMethod.GET)
    public String reloadInterfaces(Model model){
		    ConfigCache.instance().reload();
			model.addAttribute("inter", "aa");
			Hashtable <String, InterFace> interfaces=ConfigCache.instance().getInterfaces();
			model.addAttribute("interfaces", interfaces);
        return "index";
    }
	 
	 @RequestMapping(path="/error", produces="application/json")
	    @ResponseBody
	    public Map<String, Object> handle(HttpServletRequest request) {
	        Map<String, Object> map = new HashMap<String, Object>();
	        map.put("status", request.getAttribute("javax.servlet.error.status_code"));
	        map.put("reason", request.getAttribute("javax.servlet.error.message"));

	        return map;
	    }

	
}
