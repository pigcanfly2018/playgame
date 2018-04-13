package bsz.exch.game;

import java.math.BigDecimal;

public class MgXmlUtil {

	public static String getIsAuthenticateXml(String loginName,String pinCode){
		StringBuffer sb=new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		sb.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
		sb.append("<soap:Body>");
		sb.append("<IsAuthenticate xmlns=\"https://entservices.totalegame.net\">");
		sb.append("<loginName>"+loginName+"</loginName>");
		sb.append("<pinCode>"+pinCode+"</pinCode>");
		sb.append("</IsAuthenticate></soap:Body></soap:Envelope>");
		return sb.toString();
	}
	
	public static String getWithdrawalXml(String accountNumber,String transactionReferenceNumber,BigDecimal amount,String SessionGUID){
		StringBuffer sb=new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		sb.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
		sb.append("<soap:Header>");
		sb.append("<AgentSession xmlns=\"https://entservices.totalegame.net\">");
		sb.append("<SessionGUID>"+SessionGUID+"</SessionGUID>");
		sb.append("<ErrorCode>0</ErrorCode>");
		sb.append("<IPAddress>127.0.0.1</IPAddress>");
		sb.append("<IsExtendSession>true</IsExtendSession>");
		sb.append("</AgentSession></soap:Header>");
		sb.append("<soap:Body>");
		sb.append("<Withdrawal xmlns=\"https://entservices.totalegame.net\">");
		sb.append("<accountNumber>"+accountNumber+"</accountNumber>");
		sb.append("<amount>"+amount+"</amount>");
		sb.append("<transactionReferenceNumber>"+transactionReferenceNumber+"</transactionReferenceNumber>");
		sb.append("</Withdrawal>");
		sb.append("</soap:Body>");
		sb.append("</soap:Envelope>");
		return sb.toString();
	}
	
	public static String getWithdrawalAllXml(String accountNumber,String transactionReferenceNumber,BigDecimal amount,String SessionGUID){
		StringBuffer sb=new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		sb.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
		sb.append("<soap:Header>");
		sb.append("<AgentSession xmlns=\"https://entservices.totalegame.net\">");
		sb.append("<SessionGUID>"+SessionGUID+"</SessionGUID>");
		sb.append("<ErrorCode>0</ErrorCode>");
		sb.append("<IPAddress>127.0.0.1</IPAddress>");
		sb.append("<IsExtendSession>true</IsExtendSession>");
		sb.append("</AgentSession></soap:Header>");
		sb.append("<soap:Body>");
		sb.append("<WithdrawalAll xmlns=\"https://entservices.totalegame.net\">");
		sb.append("<accountNumber>"+accountNumber+"</accountNumber>");
		sb.append("<allRelatedAccounts>False</allRelatedAccounts>");
		sb.append("<transactionReferenceNumber>"+transactionReferenceNumber+"</transactionReferenceNumber>");
		sb.append("</WithdrawalAll>");
		sb.append("</soap:Body>");
		sb.append("</soap:Envelope>");
		return sb.toString();
	}
	
	public static String getDepositXml(String accountNumber,String transactionReferenceNumber,BigDecimal amount,String SessionGUID){
		StringBuffer sb=new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		sb.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
		sb.append("<soap:Header>");
		sb.append("<AgentSession xmlns=\"https://entservices.totalegame.net\">");
		sb.append("<SessionGUID>"+SessionGUID+"</SessionGUID>");
		sb.append("<ErrorCode>0</ErrorCode>");
		sb.append("<IPAddress>127.0.0.1</IPAddress>");
		sb.append("<IsExtendSession>true</IsExtendSession>");
		sb.append("</AgentSession></soap:Header>");
		sb.append("<soap:Body>");
		sb.append("<Deposit xmlns=\"https://entservices.totalegame.net\">");
		sb.append("<accountNumber>"+accountNumber+"</accountNumber>");
		sb.append("<amount>"+amount+"</amount>");
		sb.append("<currency>8</currency>");
		sb.append("<transactionReferenceNumber>"+transactionReferenceNumber+"</transactionReferenceNumber>");
		sb.append("</Deposit>");
		sb.append("</soap:Body>");
		sb.append("</soap:Envelope>");
		return sb.toString();
	}
	
	
	
	public static String getAddAccountXml(String accountNumber,String password,String ip,String SessionGUID){
		StringBuffer sb=new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		sb.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
		sb.append("<soap:Header>");
		sb.append("<AgentSession xmlns=\"https://entservices.totalegame.net\">");
		sb.append("<SessionGUID>"+SessionGUID+"</SessionGUID>");
		sb.append("<ErrorCode>0</ErrorCode>");
		sb.append("<IPAddress>"+ip+"</IPAddress>");
		sb.append("<IsExtendSession>true</IsExtendSession>");
		sb.append("</AgentSession></soap:Header>");
		sb.append("<soap:Body>");
		sb.append("<AddAccount xmlns=\"https://entservices.totalegame.net\">");
		sb.append("<accountNumber>"+accountNumber+"</accountNumber>");
		sb.append("<password>"+password+"</password>");
		sb.append("<firstName>firstName</firstName>");
		sb.append("<lastName>8dawin</lastName>");
		sb.append("<currency>8</currency>");
		sb.append("<mobileNumber>13410486253</mobileNumber>");
		sb.append("<isSendGame>false</isSendGame>");
		sb.append("<email>cs.8dabet@gmail.com</email>");
		sb.append("<BettingProfileId>178</BettingProfileId>");
		sb.append("<rngBettingProfileId>178</rngBettingProfileId>");
		sb.append("<moblieGameLanguageId>3</moblieGameLanguageId>");
		sb.append("<isProgressive>true</isProgressive>");
		sb.append("</AddAccount>");
		sb.append("</soap:Body>");
		sb.append("</soap:Envelope>");
		return sb.toString();
	}
	
	
	public static String getGetAccountDetailsXml(String accountNumber,String SessionGUID){
		StringBuffer sb=new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		sb.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
		sb.append("<soap:Header>");
		sb.append("<AgentSession xmlns=\"https://entservices.totalegame.net\">");
		sb.append("<SessionGUID>"+SessionGUID+"</SessionGUID>");
		sb.append("<ErrorCode>0</ErrorCode>");
		sb.append("<IPAddress>127.0.0.1</IPAddress>");
		sb.append("<IsExtendSession>true</IsExtendSession>");
		sb.append("</AgentSession></soap:Header>");
		sb.append("<soap:Body>");
		sb.append("<GetAccountDetails xmlns=\"https://entservices.totalegame.net\">");
		sb.append("<accountNumber>"+accountNumber+"</accountNumber>");
		sb.append("</GetAccountDetails>");
		sb.append("</soap:Body>");
		sb.append("</soap:Envelope>");
		return sb.toString();
	}
	
	public static String getGetAccountBalanceXml(String accountNumber,String SessionGUID){
		StringBuffer sb=new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		sb.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
		sb.append("<soap:Header>");
		sb.append("<AgentSession xmlns=\"https://entservices.totalegame.net\">");
		sb.append("<SessionGUID>"+SessionGUID+"</SessionGUID>");
		sb.append("<ErrorCode>0</ErrorCode>");
		sb.append("<IPAddress>127.0.0.1</IPAddress>");
		sb.append("<IsExtendSession>true</IsExtendSession>");
		sb.append("</AgentSession></soap:Header>");
		sb.append("<soap:Body>");
		sb.append("<GetAccountBalance xmlns=\"https://entservices.totalegame.net\">");
		sb.append("<delimitedAccountNumbers>"+accountNumber+"</delimitedAccountNumbers>");
		sb.append("</GetAccountBalance>");
		sb.append("</soap:Body>");
		sb.append("</soap:Envelope>");
		return sb.toString();
	}
	
	public static String getEditAccountXml(String accountNumber,String password,String SessionGUID){
		StringBuffer sb=new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		sb.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
		sb.append("<soap:Header>");
		sb.append("<AgentSession xmlns=\"https://entservices.totalegame.net\">");
		sb.append("<SessionGUID>"+SessionGUID+"</SessionGUID>");
		sb.append("<ErrorCode>0</ErrorCode>");
		sb.append("<IPAddress>127.0.0.1</IPAddress>");
		sb.append("<IsExtendSession>true</IsExtendSession>");
		sb.append("</AgentSession></soap:Header>");
		sb.append("<soap:Body>");
		sb.append("<EditAccount xmlns=\"https://entservices.totalegame.net\">");
		sb.append("<accountNumber>"+accountNumber+"</accountNumber>");
		sb.append("<firstName>firstName</firstName>");
		sb.append("<lastName>8dawin</lastName>");
		sb.append("<password>"+password+"</password>");
		sb.append("<mobileNumber>13410486253</mobileNumber>");
		sb.append("<BettingProfileId>178</BettingProfileId>");
		sb.append("<email>cs.8dabet@gmail.com</email>");
		sb.append("</EditAccount>");
		sb.append("</soap:Body>");
		sb.append("</soap:Envelope>");
		return sb.toString();
	}
	
	
	
	public static String getGetLanguageListXml(String SessionGUID){
		StringBuffer sb=new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		sb.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
		sb.append("<soap:Header>");
		sb.append("<AgentSession xmlns=\"https://entservices.totalegame.net\">");
		sb.append("<SessionGUID>"+SessionGUID+"</SessionGUID>");
		sb.append("<ErrorCode>0</ErrorCode>");
		sb.append("<IPAddress>127.0.0.1</IPAddress>");
		sb.append("<IsExtendSession>true</IsExtendSession>");
		sb.append("</AgentSession></soap:Header>");
		sb.append("<soap:Body>");
		sb.append("<GetBettingProfileList xmlns=\"https://entservices.totalegame.net\" />");
		sb.append("</soap:Body>");
		sb.append("</soap:Envelope>");
		return sb.toString();
	}
	
	public static String getGetCurrenciesForAddAccountXml(String SessionGUID){
		StringBuffer sb=new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		sb.append("<soap:Envelope xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\">");
		sb.append("<soap:Header>");
		sb.append("<AgentSession xmlns=\"https://entservices.totalegame.net\">");
		sb.append("<SessionGUID>"+SessionGUID+"</SessionGUID>");
		sb.append("<ErrorCode>0</ErrorCode>");
		sb.append("<IPAddress>127.0.0.1</IPAddress>");
		sb.append("<IsExtendSession>true</IsExtendSession>");
		sb.append("</AgentSession></soap:Header>");
		sb.append("<soap:Body>");
		sb.append("<GetCurrenciesForAddAccount xmlns=\"https://entservices.totalegame.net\" />");
		sb.append("</soap:Body>");
		sb.append("</soap:Envelope>");
		return sb.toString();
	}
}
