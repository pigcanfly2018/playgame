package bsz.exch.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import bsz.exch.bean.Result;
import bsz.exch.bean.Task;
import bsz.exch.test.db.JdbcDao;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Handler {
	public String name();
}
