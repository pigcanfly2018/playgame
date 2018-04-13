package bsz.exch.handlers;

import bsz.exch.bean.Result;
import bsz.exch.bean.Task;
import bsz.exch.core.Handler;
import bsz.exch.core.InterFace;
import bsz.exch.core.Processor;


@Handler(name="DICT")
public class DictHandler implements Processor{

	
	public Result process(Task task, InterFace inter) {
		Result result =Result.create(task.getId(), task.getFunId());
		result.addFields(inter.getReplyEn());
		String statement=inter.getStatement();
		String[] sts=statement.split("[,]{1,1}");
		for(String ss:sts){
			String[] s=ss.split("[|]{1,1}");
			result.addRecord(s);
		}
		return result;
	}
	
	
	
	
	

}
