package bsz.exch.bank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

//type=exceptionWithdrawApply 
//exception_order_num=TSM20150907026632 
//company_id=10 
//exact_payment_bank= 
//pay_card_num= 
//pay_card_name=伍泓 
//receiving_bank=2 
//receiving_account_name=唐靖海 
//channel=汇入汇款 
//note=跨行实时转账 
//area= 
//exact_time=20150907000000 
//amount=0.15 
//fee=0.00 
//transaction_charge=2.00 
//key=5600e5390a3fbe77c062530fbdccff87 

//~ type=addTransfer 


public class APITest {
	public static void main(String[] args) {
		
		String product =PayResource.instance().findProduct("BDD20".substring(0, 2));
		System.out.println(product);
		
		String exception_order_num="TSM20150907026632";
		String company_id="10";
		String exact_payment_bank= "";
		String pay_card_num= "";
		String pay_card_name="伍泓";
		String receiving_bank="2"; 
		String receiving_account_name="唐靖海"; 
		String channel="汇入汇款"; 
		String note="跨行实时转账"; 
		String area= "";
		String exact_time="20150907000000";
		String amount="0.15"; 
		String fee="0.00"; 
		String transaction_charge="2.00"; 
		String key="5600e5390a3fbe77c062530fbdccff87"; 

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("type", "exceptionWithdrawApply"));
		nvps.add(new BasicNameValuePair("exception_order_num", exception_order_num));
		nvps.add(new BasicNameValuePair("company_id", company_id));
		nvps.add(new BasicNameValuePair("exact_payment_bank", exact_payment_bank));
		nvps.add(new BasicNameValuePair("pay_card_num", pay_card_num));
		nvps.add(new BasicNameValuePair("pay_card_name", pay_card_name));
		nvps.add(new BasicNameValuePair("receiving_bank", receiving_bank));
		nvps.add(new BasicNameValuePair("receiving_account_name", receiving_account_name));
		nvps.add(new BasicNameValuePair("channel", channel));
		nvps.add(new BasicNameValuePair("note", note));
		nvps.add(new BasicNameValuePair("area", area));
		nvps.add(new BasicNameValuePair("exact_time", exact_time));
		nvps.add(new BasicNameValuePair("amount", amount));
		nvps.add(new BasicNameValuePair("fee", fee));
		nvps.add(new BasicNameValuePair("transaction_charge", transaction_charge));
		nvps.add(new BasicNameValuePair("key", key));
		

				
		List<NameValuePair> nvps2 = new ArrayList<NameValuePair>();
		nvps2.add(new BasicNameValuePair("type", "addTransfer"));
		nvps2.add(new BasicNameValuePair("pay_time", "20150907162808"));
		nvps2.add(new BasicNameValuePair("bank_id", "30"));
		nvps2.add(new BasicNameValuePair("amount", "0.13"));
		nvps2.add(new BasicNameValuePair("mownecum_order_num", "SM2015090702014568790"));
		nvps2.add(new BasicNameValuePair("company_order_num", "BDD201509071755000013"));
		nvps2.add(new BasicNameValuePair("pay_card_num", "259***@qq.com"));
		nvps2.add(new BasicNameValuePair("pay_card_name", "明星"));
		nvps2.add(new BasicNameValuePair("channel", ""));
		nvps2.add(new BasicNameValuePair("area", ""));
		nvps2.add(new BasicNameValuePair("fee", "0.00"));
		nvps2.add(new BasicNameValuePair("transaction_charge", "0.03"));
		nvps2.add(new BasicNameValuePair("deposit_mode", "3"));
		nvps2.add(new BasicNameValuePair("base_info", "2015090720004001110027004497"));
		nvps2.add(new BasicNameValuePair("key", "b549ebc29213e7f3c76febdf396b7ec4"));
		

		
		List<NameValuePair> nvps3 = new ArrayList<NameValuePair>();
		nvps3.add(new BasicNameValuePair("type", "requestWithdrawApproveInformation"));
		nvps3.add(new BasicNameValuePair("company_order_num", "BDW580720145398836"));
		nvps3.add(new BasicNameValuePair("mownecum_order_num", "SM2015090901014683883"));
		nvps3.add(new BasicNameValuePair("amount", "100.00"));
		nvps3.add(new BasicNameValuePair("card_num", "450923198652411"));
		nvps3.add(new BasicNameValuePair("card_name", "潘明星"));
		nvps3.add(new BasicNameValuePair("company_user", "lance008"));
		nvps3.add(new BasicNameValuePair("key", "2b3ff553447dbfc3bc3a5c571a275dde"));
		

		List<NameValuePair> nvps4 = new ArrayList<NameValuePair>();
		nvps4.add(new BasicNameValuePair("type", "withdrawalResult"));
		nvps4.add(new BasicNameValuePair("mownecum_order_num", "SM2015090901014683883"));
		nvps4.add(new BasicNameValuePair("company_order_num", "BDW580720145398836"));
		nvps4.add(new BasicNameValuePair("status", "1"));
		nvps4.add(new BasicNameValuePair("detail", "N手动关闭"));
		nvps4.add(new BasicNameValuePair("amount", "100.00"));
		nvps4.add(new BasicNameValuePair("exact_transaction_charge", "8.00"));
		nvps4.add(new BasicNameValuePair("key", "b0889ba134ca1f25e0b4e0107a6a6b69"));
		
		String s=APITest.get().post("http://124.248.247.150:6060/ws/web/dpay.do", nvps3);
		
		System.out.println(s);
		//String key0=MD5Util.MD5(MD5Util.MD5("123qwe").toLowerCase()+sb.toString()).toLowerCase();
	}

	private static Map<String, APITest> map = new HashMap<String, APITest>();

	public static APITest get() {
		String product = "8da";
		APITest api = map.get(product);
		if (api == null) {
			api = new APITest();

			RegistryBuilder<ConnectionSocketFactory> registryBuilder = RegistryBuilder
					.<ConnectionSocketFactory> create();
			ConnectionSocketFactory plainSF = new PlainConnectionSocketFactory();
			registryBuilder.register("http", plainSF);
			Registry<ConnectionSocketFactory> registry = registryBuilder
					.build();
			api.connManager = new PoolingHttpClientConnectionManager(registry);
			api.requestConfig = RequestConfig.custom().setSocketTimeout(35000)
					.setConnectTimeout(35000).build();
			api.build = HttpClientBuilder.create().setConnectionManager(
					api.connManager);
			map.put(product, api);
		}
		return api;
	}

	private PoolingHttpClientConnectionManager connManager;
	private RequestConfig requestConfig;
	private HttpClientBuilder build;

	public String post(String url, List<NameValuePair> nvps) {
		CloseableHttpClient hc = build.build();
		HttpPost httppost = new HttpPost(url);
		httppost.setConfig(requestConfig);
		try {

			httppost.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
			HttpResponse response = hc.execute(httppost);
			if (response.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(response.getEntity());
				return result;

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return "";
	}

}
