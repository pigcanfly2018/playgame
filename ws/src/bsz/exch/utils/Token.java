package bsz.exch.utils;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

/**
 * 一个简单的token可逆实现
 * @author 8da
 *
 */
public class Token {
	
	
	    private static final String PASSWORD_CRYPT_KEY = "B58lkj$3!";  
	    private final static String DES = "DES";  
	  
	    /** 
	     * 加密 
	     * @param sr 数据源 
	     * @param key 密钥，长度必须是8的倍数 
	     * @return 返回加密后的数据 
	     * @throws Exception 
	     */  
	    private static byte[] encrypt(byte[] src, byte[] key) throws Exception {  
	        // DES算法要求有一个可信任的随机数源  
	        SecureRandom sr = new SecureRandom();  
	        // 从原始密匙数据创建DESKeySpec对象  
	        DESKeySpec dks = new DESKeySpec(key);  
	        // 创建一个密匙工厂，然后用它把DESKeySpec转换成  
	        // 一个SecretKey对象  
	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);  
	        SecretKey securekey = keyFactory.generateSecret(dks);  
	        // Cipher对象实际完成加密操作  
	        Cipher cipher = Cipher.getInstance(DES);  
	        // 用密匙初始化Cipher对象  
	        cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);  
	        // 现在，获取数据并加密  
	        // 正式执行加密操作  
	        return cipher.doFinal(src);  
	    }  
	  
	    /** 
	     * 解密 
	     * @param src 数据源 
	     * @param key 密钥，长度必须是8的倍数 
	     * @return 返回解密后的原始数据 
	     * @throws Exception 
	     */  
	    private static byte[] decrypt(byte[] src, byte[] key) throws Exception {  
	        // DES算法要求有一个可信任的随机数源  
	        SecureRandom sr = new SecureRandom();  
	        // 从原始密匙数据创建一个DESKeySpec对象  
	        DESKeySpec dks = new DESKeySpec(key);  
	        // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成  
	        // 一个SecretKey对象  
	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);  
	        SecretKey securekey = keyFactory.generateSecret(dks);  
	        // Cipher对象实际完成解密操作  
	        Cipher cipher = Cipher.getInstance(DES);  
	        // 用密匙初始化Cipher对象  
	        cipher.init(Cipher.DECRYPT_MODE, securekey, sr);  
	        // 现在，获取数据并解密  
	        // 正式执行解密操作  
	        return cipher.doFinal(src);  
	    }  
	  
	    /** 
	     * 密码解密 
	     * @param data 
	     * @return 
	     * @throws Exception 
	     */  
	    private final static String decrypt(String data) {  
	        try {  
	            return new String(decrypt(hex2byte(data.getBytes()),PASSWORD_CRYPT_KEY.getBytes()));  
	        } catch (Exception e) {  
	        }  
	        return null;  
	    }  
	  
	    /** 
	     * 密码加密 
	     * @param password 
	     * @return 
	     * @throws Exception 
	     */  
	    private final static String encrypt(String password) {  
	        try {  
	            return byte2hex(encrypt(password.getBytes(), PASSWORD_CRYPT_KEY.getBytes()));  
	        } catch (Exception e) {  
	        }  
	        return null;  
	    }  
	  
	    /** 
	     * 二行制转字符串 
	     * @param b 
	     * @return 
	     */  
	    private static String byte2hex(byte[] b) {  
	        String hs = "";  
	        String stmp = "";  
	        for (int n = 0; n < b.length; n++) {  
	            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));  
	            if (stmp.length() == 1){  
	                hs = hs + "0" + stmp;  
	            }else{  
	                hs = hs + stmp;  
	            }  
	        }  
	        return hs.toUpperCase();  
	    }  
	  
	    private static byte[] hex2byte(byte[] b) {  
	        if ((b.length % 2) != 0)  
	            throw new IllegalArgumentException("长度不是偶数");  
	        byte[] b2 = new byte[b.length / 2];  
	        for (int n = 0; n < b.length; n += 2) {  
	            String item = new String(b, n, 2);  
	            b2[n / 2] = (byte) Integer.parseInt(item, 16);  
	        }  
	        return b2;  
	    }  
	  

	
	

	/**
	 * 获取一个Token
	 * @param s
	 * @return
	 */
	public static String get(String s){
		StringBuffer sb = new StringBuffer();
		sb.append((System.currentTimeMillis()/1000));
		sb.append(s);
		try {
			return new String(encrypt(sb.toString()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 校验token是否有效
	 * @param s
	 * @param token
	 * @return
	 */
	public static boolean validate(String s,String token){
		String q =decrypt(token);
		if(q==null){
			return false;
		}
		String t=q.substring(0, 10);
		String s2=q.substring(10,q.length());
		long now=System.currentTimeMillis()/1000;
		if(now>(Long.valueOf(t)+(24*3600))){
			return false;
		}
		if(!s2.equals(s)){
			return false;
		}
		return true;
	}

	
	public static void main(String []args){

		System.out.println(Token.get("lance150"));

		
		System.out.println(validate("lance150",Token.get("lance150")));
		
		
	}
	

}
