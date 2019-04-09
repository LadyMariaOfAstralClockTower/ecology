package com.lw.webservice;

import net.sf.json.JSONObject;

public class LwTest {

	public static void main(String[] args) {
		
		try {

			CAdxWebServiceXmlCCProxy proxy=new CAdxWebServiceXmlCCProxy();
			
			CAdxCallContext context=new CAdxCallContext();
			context.setPoolAlias("WSTEST");
			context.setCodeUser("admin");
			context.setPassword("");
			context.setCodeLang("CHI");
			context.setRequestConfig("adxwss.optreturn=json");
			
			CAdxParamKeyValue[] objKey=new CAdxParamKeyValue[1];
			
			objKey[0]=new CAdxParamKeyValue("CCE","300");
			//objKey[1]=new CAdxParamKeyValue("PRQDAT","11/05/2017");
			//objKey[2]=new CAdxParamKeyValue("3","*");
			
			CAdxResultXml result;
			result=proxy.query(context, "XOACCE", objKey, 200); //查询采购申请单列表
			
			objKey[0]=new CAdxParamKeyValue("POHNUM","WH011707PO001");
			
			//objKey[0]=new CAdxParamKeyValue("CCE","300");
			
			result=proxy.read(context, "XOACCE", objKey); //读取采购申请单详细
			
			//proxy.modify(context, "XOAPSH", objectKeys, objectXml); //回写ERP
			
			System.out.println("result:"+result.getResultXml()+" status:"+result.getStatus());
			
			//JSONObject json=JSONObject.fromObject(result.getResultXml());
			

			//System.out.println("POH0_1(POHNUM):"+json.getJSONObject("POH0_1").get("POHNUM"));

			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	
	
}
