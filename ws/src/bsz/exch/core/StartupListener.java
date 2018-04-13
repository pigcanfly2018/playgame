package bsz.exch.core;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bsz.exch.cache.ConfigCache;

public class StartupListener implements ServletContextListener { 
	public void contextDestroyed(ServletContextEvent sce) {
	}
	private static final Logger logger = LoggerFactory.getLogger(StartupListener.class);
	public static ServletContext servletContext = null;
	public static String configPath = null;
	public static long startedAt;
	public static boolean startedTrade = false;

	public void contextInitialized(ServletContextEvent event) {
		try {
			servletContext = event.getServletContext();
			WsCxtHelper.setServletContext(servletContext);
			HandlerFactory.loadClass0();
			configPath = servletContext.getRealPath("/") + "WEB-INF"+ File.separatorChar + "classes" + File.separatorChar+"config"+ File.separatorChar;
			ConfigCache.instance().load(new File(configPath));
			logger.info("*-config.xml加载成功!!");
		} catch (Exception ex) {
			logger.error("*-config.xml加载失败!!", ex);
		}
	} 
}

