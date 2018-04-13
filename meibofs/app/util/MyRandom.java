package util;

import java.util.Random;

public class MyRandom {
	public static final String numberChar = "0123456789";
	public static final String apchar="abcdefghijklmnopqrstuvwxyz0123456789";
	public static final String purechar="abcdefghijklmnopqrstuvwxyz";
	/**
	 * 根据系统时间获得指定位数的随机数
	 * 
	 * @return 获得的随机数
	 */
	public static String getRandom(int num) {

		Long seed = System.currentTimeMillis();// 获得系统时间，作为生成随机数的种子

		StringBuffer sb = new StringBuffer();// 装载生成的随机数
	
		Random random = new Random(seed);// 调用种子生成随机数

		for (int i = 0; i < num; i++) {

			sb.append(numberChar.charAt(random.nextInt(numberChar.length())));
		}

		return sb.toString();
	}
	public static String getRandomChar(int num) {

		Long seed = System.currentTimeMillis();// 获得系统时间，作为生成随机数的种子

		StringBuffer sb = new StringBuffer();// 装载生成的随机数
	
		Random random = new Random(seed);// 调用种子生成随机数

		for (int i = 0; i < num; i++) {
			sb.append(apchar.charAt(random.nextInt(apchar.length())));
		}
		return sb.toString();
	}
	
	
	public static String getRandomPureChar(int num) {

		Long seed = System.currentTimeMillis();// 获得系统时间，作为生成随机数的种子

		StringBuffer sb = new StringBuffer();// 装载生成的随机数
	
		Random random = new Random(seed);// 调用种子生成随机数

		for (int i = 0; i < num; i++) {
			sb.append(purechar.charAt(random.nextInt(purechar.length())));
		}
		return sb.toString();
	}
	
	
	public static void main(String[] args){
		System.out.println(getRandomPureChar(1));
	}


}
