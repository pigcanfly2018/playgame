package util;

import java.util.ArrayList;
import java.util.List;

public class GiftRandom {
	/**
	 * 判断是否中奖
	 * @param rate 概率
	 * @return 是否中奖
	 */
	public static boolean isLuck(int rate){
		int a=(int)( Math.random() * 100*100);
		return a<(rate*100);
	} 
	
	public static void main(String[]args){
		int flag=0;
		for(int z=0;z<10000;z++){
			if(isLuck(30)){
				flag++;
			}
		}
		
		System.out.println(flag/10000.0);
	}

}
