package bsz.exch.game;

import java.util.ResourceBundle;

public class GameResource {
	private ResourceBundle rb;
	
	public static GameResource instance(){
		   if(resource==null){
			   resource = new GameResource();
		   }
		   return resource;
	}
	
	private static GameResource resource = new GameResource();
	
	private  GameResource(){
		rb= ResourceBundle.getBundle("game");
		
	}
	
	public String getConfig(String name){
		if(rb.containsKey(name)){
		   return rb.getString(name);
		}else{
			return null;
		}
	}
	
	
	public static void main(String []args){
		
		System.out.print(GameResource.instance().getConfig("game.ag.agurl"));
		
	}
	
}
