package com.lw.action;

import java.rmi.RemoteException;

import com.lw.webservice.CAdxCallContext;
import com.lw.webservice.CAdxParamKeyValue;
import com.lw.webservice.CAdxResultXml;
import com.lw.webservice.CAdxWebServiceXmlCCProxy;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.TimeUtil;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class PayBack extends BaseBean implements Action {

	@Override
	public String execute(RequestInfo arg0) {
		// TODO Auto-generated method stub
		
		writeLog("**********************************  [PayBack]开始回写到ERP  ************************************");
		
		String flag="1";
		
		RecordSet rs=new RecordSet();
		
		String requestId=arg0.getRequestid();

		
		String YSTA="";//审批状态
		String NUM="";//请求号
		String YREMARK="";//审批意见
		
		String sql="select * from formtable_main_96 where requestid="+requestId;
		rs.execute(sql);
		if(rs.next()){
			YSTA=rs.getString("YSTA");
			NUM=rs.getString("NUM");
			YREMARK=rs.getString("YREMARK");
		}
		
		writeLog("[PayBack.execute] requestid:"+requestId+" YSTA:"+YSTA+" PAYNUM:"+NUM+" YREMARK:"+YREMARK);
		
		if(YSTA.equals("0")){//0为已退回 1为已通过
			sql="update PayNote set status='0' where PAYNUM='"+NUM+"'";
			rs.execute(sql);
			
			writeLog("[PayBack.execute] PAYNUM为:"+NUM+" 的流程被退回！");
			YSTA="3";
		}else if(YSTA.equals("1")){//回写到ERP中 3为退回 4为通过
			YSTA="4";
		}
		
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
			xml+="<FLD NAME='YSTA' TYPE='Integer' >"+YSTA+"</FLD>";
			xml+="<FLD NAME='YDAT' TYPE='Date' >"+TimeUtil.getCurrentDateString().replaceAll("-", "")+"</FLD>";
			xml+="<FLD NAME='YREMARK' TYPE='Char' >"+YREMARK+"</FLD>";
			xml+="</PARAM>";
			
			CAdxParamKeyValue[] objKeys=new CAdxParamKeyValue[1];		
			objKeys[0]=new CAdxParamKeyValue("NUM",NUM);

			CAdxResultXml result=proxy.modify(context, "XPAYUPD", objKeys, xml);
			
			writeLog("[PayBack.execute] result:"+result.getResultXml()+" status:"+result.getStatus());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		writeLog("**********************************  [PayBack]回写到ERP完成  ************************************");
		
		return flag;
	}

}
