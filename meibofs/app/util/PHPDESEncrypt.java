package util;

import java.io.IOException;
import java.io.PrintStream;
import java.security.SecureRandom;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class PHPDESEncrypt
{
  String key;

  public String getKey()
  {
    return this.key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public PHPDESEncrypt()
  {
  }

  public PHPDESEncrypt(String productId, String type)
  {
    StringBuffer tempKey = new StringBuffer("");
    if ((type == null) || ("".equals(type))) {
      tempKey.append(productId).append(productId).append(productId);
    }
    String productKey = CommonUtilReadMsgInit.getMsg(type + "." + productId + ".KEY");

    if ("01".equals(type)) {
      tempKey.append(productId).append("R_01").append(productKey.substring(1, productKey.length() - 1));
    }

    if ("02".equals(type)) {
      tempKey.append(productId).append("A_02").append(productKey.substring(2, productKey.length() - 2));
    }

    if ("03".equals(type)) {
      tempKey.append(productId).append("P_03").append(productKey.substring(1, productKey.length() - 1));
    }

    if ("04".equals(type)) {
      tempKey.append(productId).append("E_04").append(productKey.substring(1, productKey.length() - 2));
    }

    if ("05".equals(type)) {
      tempKey.append(productId).append("O_05").append(productKey.substring(3, productKey.length() - 3));
    }

    if ("06".equals(type)) {
      tempKey.append(productId).append("AN_06").append(productKey.substring(1, productKey.length() - 2));
    }

    if ("07".equals(type)) {
      tempKey.append(productId).append("AN_07").append(productKey.substring(2, productKey.length() - 1));
    }

    if ("08".equals(type)) {
      tempKey.append(productId).append("D_08").append(productKey.substring(2, productKey.length() - 2));
    }
    this.key = tempKey.toString();
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

  public String encrypt(String input)
    throws Exception
  {
    if ((input == null) || ("".equals(input.trim()))) {
      return input;
    }
    String str1 = getRandomNum(1);
    String str2 = getRandomStr(str1, 2);
    String str3 = getRandomStr(str1, 3);
    StringBuffer sb = new StringBuffer(str2);
    sb.append(base64Encode(desEncrypt(input.getBytes())));
    sb.append(str3);
    return sb.toString();
  }

  public String decrypt(String input)
    throws Exception
  {
    if ((input == null) || ("".equals(input.trim()))) {
      return input;
    }
    input = input.substring(3, input.length() - 4);
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
    try
    {
      PHPDESEncrypt dd2 = new PHPDESEncrypt("A01", "05");
      System.out.println(dd2.encrypt("QQ:12344564"));
      dd2 = new PHPDESEncrypt("A02", "01");
      System.out.println(dd2.decrypt("6iJoXIZJkgnzO3QHyybEVnTFg==6aTk"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String getRandomStr(String prefix, int bodyLength)
  {
    char[] c = { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'q', 'w', 'e', 'r', 't', 'y', 'u', 'i', 'o', 
      'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c', 'b', 'n', 'm', 'Q', 'W', 'E', 'R', 
      'T', 'Y', 'U', 'I', 'O', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J', 'K', 'L', 'Z', 'X', 'C', 'V', 'B', 
      'N', 'M', '=' };
    Random random = new Random();
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < bodyLength; ++i) {
      sb.append(c[(java.lang.Math.abs(random.nextInt()) % c.length)]);
    }
    return prefix + sb.toString();
  }

  public String getRandomNum(int bodyLength)
  {
    Random random = new Random();
    String sRand = "";
    for (int i = 0; i < bodyLength; ++i) {
      String rand = String.valueOf(random.nextInt(10));
      sRand = sRand + rand;
    }
    return sRand;
  }
}