package bsz.exch.core;

import bsz.exch.bean.Result;
import bsz.exch.bean.Task;
import bsz.exch.test.db.JdbcDao;

public interface Processor {
	public Result process(Task task,InterFace inter);
}
