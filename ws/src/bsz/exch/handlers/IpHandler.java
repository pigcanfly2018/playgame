package bsz.exch.handlers;

import bsz.exch.bean.Result;
import bsz.exch.bean.Task;
import bsz.exch.core.Handler;
import bsz.exch.core.InterFace;
import bsz.exch.core.Params;
import bsz.exch.core.Service;
import bsz.exch.utils.IPSeeker;

@Handler(name="IP")
public class IpHandler {
	
	
	/**
	 * 获取IP的详细信息
	 * @param task
	 * @param inter
	 * @return
	 */
	@Service(name="get")
	@Params(validateField={"ip"})
	public Result balance(Task task,InterFace inter){
		String ip=task.getParam("ip");
		IPSeeker seeker = IPSeeker.getInstance();
		Result result =Result.create(task.getId(), task.getFunId());
		result.setFlag("-1");
		result.setIsList(false);
		result.setRes(seeker.getAddress(ip));
		return result;
	}
	
	

}
