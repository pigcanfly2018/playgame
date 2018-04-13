package bsz.exch.utils;

import java.security.MessageDigest;

public class SignUtil {
	private static String signType ="MD5";
	private static final char[] bcdLookup = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	public static final String bytesToHexStr(byte[] bcd) {
		StringBuffer s = new StringBuffer(bcd.length * 2);

		for (int i = 0; i < bcd.length; i++) {
			s.append(bcdLookup[(bcd[i] >>> 4 & 0xF)]);
			s.append(bcdLookup[(bcd[i] & 0xF)]);
		}

		return s.toString();
	}

	public static final byte[] hexStrToBytes(String s) {
		byte[] bytes = new byte[s.length() / 2];

		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = ((byte) Integer.parseInt(s.substring(2 * i, 2 * i + 2), 16));
		}

		return bytes;
	}

	
	/**
	 * bruce
	 * 20180209
	 * @param signData
	 * @param srcData
	 * @return
	 * @throws Exception
	 */
	public static boolean verifyData(String signData, String srcData,String key)
			throws Exception {
		
			if (signData.equalsIgnoreCase(signByMD5(srcData, key))) {
				System.out.println("md5验证签名成功=============");
				return true;
			} 
		return false;
	}
	
    /*
     *md5签名
     * 
     */
	public static String signByMD5(String sourceData, String mer_key)
			throws Exception {
		String data = sourceData + mer_key;
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		byte[] sign = md5.digest(data.getBytes("UTF-8"));

		return Bytes2HexString(sign).toUpperCase();
	}
	
	/**
	 * 将byte数组转成十六进制的字符串
	 * 
	 * @param b
	 *            byte[]
	 * @return String
	 */
	public static String Bytes2HexString(byte[] b) {
		StringBuffer ret = new StringBuffer(b.length);
		String hex = "";
		for (int i = 0; i < b.length; i++) {
			hex = Integer.toHexString(b[i] & 0xFF);

			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			ret.append(hex.toUpperCase());
		}
		return ret.toString();
	}
}