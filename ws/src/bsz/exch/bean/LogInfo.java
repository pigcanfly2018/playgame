package bsz.exch.bean;

import java.io.Serializable;

public class LogInfo implements Serializable {
    private static final long serialVersionUID       = 8852394572921412518L;
	public long timeout =0;
	public StringBuffer sb=new StringBuffer();
	
	public void addLog(String msg){
		if(timeout==0L){
			timeout=System.currentTimeMillis();
		}
		sb.append(msg);
	}
	public String getLog(){
	  Long timeout0=System.currentTimeMillis();
	  return "本次耗时:"+((timeout0-timeout)/1000)+"s "+sb.toString();
	}
}
