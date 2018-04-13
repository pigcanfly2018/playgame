package bsz.exch.bean;

public class FobbinInfo {
	public boolean fobbin;
	public String fmsg="";
	public FobbinInfo(boolean fobbin ,String fmsg){
		this.fobbin=fobbin;
		this.fmsg=fmsg;
	}
	
	public boolean isFobbin(){
		return fobbin;
	}
	
	public String getMsg(){
		return this.fmsg;
	}

}
