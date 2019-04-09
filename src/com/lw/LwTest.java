package com.lw;


import com.lw.webservice.CAdxCallContext;
import com.lw.webservice.CAdxParamKeyValue;
import com.lw.webservice.CAdxResultXml;
import com.lw.webservice.CAdxWebServiceXmlCCProxy;

public class LwTest {
	
	private static String xmlHeader="<?xml version='1.0' encoding='UTF-8'?>";//xmlÎÄ¼þÍ·

	public static void main(String[] args) {
		
		try {
			
			CAdxWebServiceXmlCCProxy proxy=new CAdxWebServiceXmlCCProxy();
			
			CAdxCallContext context=new CAdxCallContext();
			context.setPoolAlias("WSNEWAY");
			context.setCodeUser("admin");
			context.setPassword("");
			context.setCodeLang("CHI");
			context.setRequestConfig("adxwss.optreturn=json");
			
			String xml="";
			xml+="<PARAM>";
			xml+="<FLD NAME='XDIE' TYPE='Char'>EMP</FLD>";
			xml+="<FLD NAME='XCCE' TYPE='Char'>OA001</FLD>";
			xml+="</PARAM>";
			
			CAdxParamKeyValue[] objKeys=new CAdxParamKeyValue[1];		
			objKeys[0]=new CAdxParamKeyValue("1","*");

			CAdxResultXml result;
			//result=proxy.modify(context, "XPOHUPD", objKeys, xml);
			
			//System.out.println("reqport:"+result.getTechnicalInfos().getProcessReport()+" status:"+result.getStatus());
			
			//CAdxMessage[] msg=result.getMessages();
			
			//result=proxy.run(context, "XOAPSH", xml);
			
			//System.out.println("result:"+result.getResultXml()+" status:"+result.getStatus());
			
			objKeys[0]=new CAdxParamKeyValue("NUM","BKPAY1710WH01000002");
			
			result=proxy.read(context, "XOAPAY", objKeys);
			
			System.out.println("result:"+result.getResultXml()+" status:"+result.getStatus());
			
			//for(int i=0;i<msg.length;i++){
				//System.out.println("message:"+msg[i].getMessage());
			//}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	public static String getHrmXml(String loginid,String Dept,String lastname,String status,String erpgsbm){
		
		String xml=xmlHeader;
		xml+="<PARAM>";
		xml+="<GRP ID='CCE0_1' >";
		xml+="<FLD NAME='DIE' TYPE='Char' >EMP</FLD>";
		xml+="<FLD NAME='CCE' TYPE='Char' >"+loginid+"</FLD>";
		xml+="<FLD NAME='YDPT' TYPE='Char' >"+Dept+"</FLD>";
		xml+="<FLD NAME='DESTRA' TYPE='Char' >"+lastname+"</FLD>'";
		xml+="</GRP>";
		xml+="<GRP ID='CCE1_2' >";
		if(status.equals("0")||status.equals("1")||status.equals("2")||status.equals("3")){
			xml+="<FLD NAME='ENAFLG' TYPE='Integer' >2</FLD>";
		}else if(status.equals("4")||status.equals("5")||status.equals("6")||status.equals("7")){
			xml+="<FLD NAME='ENAFLG' TYPE='Integer' >1</FLD>";
		}
		xml+="<FLD NAME='FCY' TYPE='Char' >"+erpgsbm+"</FLD>";
		xml+="</GRP>";
		xml+="</PARAM>";
		return xml;
	}
}
