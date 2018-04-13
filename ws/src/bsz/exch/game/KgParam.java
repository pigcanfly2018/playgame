package bsz.exch.game;

import java.util.List;

/**
 *  <?xml version="1.0" encoding="iso-8859-1"?>
 <methodCall>
	 <methodName>PlayerLanding</methodName>
	 <params>
	 <param>
	 <value>
	 <struct>
	   <member><name>VendorSite</name><value><string>XXX</string></value></member>
	   <member><name>FundLink</name><value><string>XXX</string></value></member>
	   <member><name>VendorId</name><value><string>XXX</string></value></member>
	   <member><name>PlayerId</name><value><string>XXX</string></value></member>
	   <member><name>PlayerRealName</name><value><string>XXX</string></value></member>
	   <member><name>PlayerCurrency</name><value><string>XXX</string></value></member>
	   <member><name>PlayerCredit</name><value><string>XXX</string></value></member>
	   <member><name>PlayerAllowStake</name><value><string>XXX</string></value></member>
	   <member><name>Trial</name><value><string>XXX</string></value></member>
	   <member><name>Language</name><value><string>XXX</string></value></member>
	   <member><name> RebateLevel </name><value><string>XXX</string></value></member>
	   <member><name>PlayerIP</name><value><string>XXX</string></value></member>
	   <member><name>VendorRef</name><value><string>XXX</string></value></member>
	   <member><name>Remarks</name><value><string>XXX</string></value></member>
	 </struct>
	 </value>
	 </param>
	 </params>
	 </methodCall>
 *
 */

public class KgParam {
	public String name;
	public String value;
	
	public KgParam(String name,String value){
		this.name=name;
		this.value=value;
		
	}
	

}
