package bsz.exch.utils;

import java.io.IOException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/* 
 * DesEncrypt.java 
 * 
 * Created on 2007-9-20, 16:10:47 
 * 
 * To change this template, choose Tools | Template Manager 
 * and open the template in the editor. 
 */
//思路： 因为 任意一个字符串，都是由若干字节表示的，每个字节实质就是一个 
// 有8位的进进制数， 
// 又因为 一个8位二进制数，可用两位16进制字符串表示. 
// 因此 任意一个字符串可以由两位16进制字符串表示。 
// 而 DES是对8位二进制数进行加密，解密。 
// 所以 用DES加密解密时，可以把加密所得的8位进进制数，转成 
// 两位16进制数进行保存，传输。 
// 具体方法：1 把一个字符串转成8位二进制数，用DES加密，得到8位二进制数的 
// 密文 
// 2 然后把（由1）所得的密文转成两位十六进制字符串 
// 3 解密时，把（由2)所得的两位十六进制字符串，转换成8位二进制 
// 数的密文 
// 4 把子3所得的密文，用DES进行解密，得到8位二进制数形式的明文， 
// 并强制转换成字符串。 
// 思考：为什么要通过两位16进制数字符串保存密文呢？ 
// 原因是：一个字符串加密后所得的8位二进制数，通常不再时字符串了，如果 
// 直接把这种密文所得的8位二进制数强制转成字符串，有许多信息因为异 
// 常而丢失，导制解密失败。因制要把这个8位二制数，直接以数的形式 
// 保存下来，而通常是用两位十六进制数表示。 

/**
 * 
 * 使用DES加密与解密,可对byte[],String类型进行加密与解密 密文可使用String,byte[]存储.
 * 
 * 方法: void getKey(String strKey)从strKey的字条生成一个Key
 * 
 * String getEncString(String strMing)对strMing进行加密,返回String密文 String
 * getDesString(String strMi)对strMin进行解密,返回String明文
 * 
 * byte[] getEncCode(byte[] byteS)byte[]型的加密 byte[] getDesCode(byte[]
 * byteD)byte[]型的解密
 */
public class DESEncrypt
{
  String key;

  public DESEncrypt(){
  }

  public DESEncrypt(String key)
  {
    this.key = key;
  }

  public byte[] desEncrypt(byte[] plainText) throws Exception {
    SecureRandom sr = new SecureRandom();
    DESKeySpec dks = new DESKeySpec(this.key.getBytes());
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
    SecretKey key = keyFactory.generateSecret(dks);
    Cipher cipher = Cipher.getInstance("DES");
    cipher.init(1, key, sr);
    byte[] data = plainText;
    byte[] encryptedData = cipher.doFinal(data);
    return encryptedData;
  }

  public byte[] desDecrypt(byte[] encryptText) throws Exception {
    SecureRandom sr = new SecureRandom();
    DESKeySpec dks = new DESKeySpec(this.key.getBytes());
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
    SecretKey key = keyFactory.generateSecret(dks);
    Cipher cipher = Cipher.getInstance("DES");
    cipher.init(2, key, sr);
    byte[] encryptedData = encryptText;
    byte[] decryptedData = cipher.doFinal(encryptedData);
    return decryptedData;
  }

  public String encrypt(String input) throws Exception {
    return base64Encode(desEncrypt(input.getBytes())).replaceAll("\\s*", "");
  }

  public String decrypt(String input) throws Exception {
    byte[] result = base64Decode(input);
    return new String(desDecrypt(result));
  }

  public String base64Encode(byte[] s) {
    if (s == null)
      return null;
    BASE64Encoder b = new BASE64Encoder();
    return b.encode(s);
  }

  public byte[] base64Decode(String s) throws IOException {
    if (s == null) {
      return null;
    }
    BASE64Decoder decoder = new BASE64Decoder();
    byte[] b = decoder.decodeBuffer(s);
    return b;
  }

  public static void main(String[] args)
  {
    try {
      DESEncrypt d = new DESEncrypt("jsn72ksm");
      String p = d.encrypt("cagent=B18_AG/\\\\/loginname=ptest98/\\\\/method=ice");
      System.out.println("密文:" + p);
      System.out.println(d.decrypt(p));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String getKey()
  {
    return this.key;
  }

  public void setKey(String key) {
    this.key = key;
  }
}