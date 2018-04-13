package bsz.exch.core;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class SpringListener implements BeanPostProcessor{

	@Override
	public Object postProcessAfterInitialization(Object arg0, String arg1)
			throws BeansException {
		if(arg0 instanceof BasicDataSource ){
			System.out.println("found datasource :"+arg1);
			JdbcResource.instance().addDataSource(arg1, (BasicDataSource)arg0);
		}
		return arg0;
	}

	@Override
	public Object postProcessBeforeInitialization(Object arg0, String arg1)
			throws BeansException {

		return arg0;
	}

}
