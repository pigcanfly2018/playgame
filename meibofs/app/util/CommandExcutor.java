package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CommandExcutor {
	
	public static String excute(String command){
		try{
			System.out.println("线程"+Thread.currentThread().getId()+":执行命令：" + command);
			Process p = Runtime.getRuntime().exec(command);
			String errorMsg = readInputStream(p.getErrorStream());
			String outputMsg = readInputStream(p.getInputStream());
			int c = p.waitFor();
			 if (c != 0){
				 System.out.println("线程"+Thread.currentThread().getId()+":执行命令：" + command+",返回失败结果:"+errorMsg);
			    return "失败";
			 }else{
			    System.out.println("线程"+Thread.currentThread().getId()+":执行命令：" + command+",返回结果:"+outputMsg);
			    return outputMsg;
			 }
			}catch(Exception e){
				
			}
			
			return null;
	}
	private static String readInputStream(InputStream is) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(is,"utf-8"));
		StringBuffer lines = new StringBuffer();
		String line = null;
		while ((line = br.readLine()) != null) {
			lines.append(line);
		}
		return lines.toString();
	}
	
	
	public static void sleep(long t){
		try {
			Thread.currentThread().sleep(t);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
