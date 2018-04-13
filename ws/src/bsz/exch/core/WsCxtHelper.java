package bsz.exch.core;

import javax.servlet.ServletContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;



public class WsCxtHelper {
	public static ServletContext servletContext = null;
	public static WebApplicationContext ctx = null;

	public static Object getBean(String name) {
		if (ctx != null) {
			return ctx.getBean(name);
		}
		return ctx.getBean(name);
	}
	public static void setServletContext(ServletContext pservletContext) {
		servletContext = pservletContext;
		ctx = WebApplicationContextUtils.getWebApplicationContext(servletContext);
	}
	
	
	
	
	
}
