package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MD5 {
	
	public static String md5(String str) {
        String s=str;
	if(s==null){
		return "";
	}else{
		String value = null;
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
			}catch (NoSuchAlgorithmException ex) {
				Logger.getLogger(MessageDigest.class.getName()).log(Level.SEVERE, null, ex);
			}
		sun.misc.BASE64Encoder baseEncoder = new sun.misc.BASE64Encoder();
		try {
			value = baseEncoder.encode(md5.digest(s.getBytes("utf-8")));
			} catch (Exception ex) {
			}
		return value;
		}
	}      
	public final static String MD5_SHA(String s) {  
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',  
                'a', 'b', 'c', 'd', 'e', 'f' };  
        try {  
            byte[] strTemp = s.getBytes();  
            //如果输入“SHA”，就是实现SHA加密。  
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");   
            mdTemp.update(strTemp);  
            byte[] md = mdTemp.digest();  
            int j = md.length;  
            char str[] = new char[j * 2];  
            int k = 0;  
            for (int i = 0; i < j; i++) {  
                byte byte0 = md[i];  
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];  
                str[k++] = hexDigits[byte0 & 0xf];  
            }  
            return new String(str);  
        } catch (Exception e) {  
            return null;  
        }  
    }  

	public static void main(String[] args){
		System.out.println(MD5_SHA("ddd"));
	}
}
