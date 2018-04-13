package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;



public class CommonUtilReadMsgInit
{
  public static final String MESSAGEPATH = "/init0.properties";
  private Properties prop = null;

  private static CommonUtilReadMsgInit instance = new CommonUtilReadMsgInit();

  private CommonUtilReadMsgInit() {
    InputStream is = super.getClass()
      .getResourceAsStream("/init0.properties");

    this.prop = new Properties();
    try
    {
      this.prop.load(is);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static String getMsg(String key)
  {
    return instance.prop.getProperty(key);
  }

  public static void main(String[] args) {
    System.out.println(getMsg("requestId.first.letter.begin"));
  }
}
