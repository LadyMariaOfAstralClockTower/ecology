package com.sap.action;

import com.sap.sap002.sap_com.OA_002Stub;
import com.sap.sap002.sap_com.ZSAP_OA_002_Service;
import com.sap.sap002.sap_com.ZSAP_OA_002_ServiceLocator;
import com.sap.sap002.sap_com.ZSINTTURN;
import com.sap.sap002.sap_com.ZSPRETURN;
import com.sap.sap002.sap_com.holders.TABLE_OF_ZSINTTURNHolder;
import com.sap.sap002.sap_com.holders.TABLE_OF_ZSPRETURNHolder;
import com.sap.sap004.sap_com.ZOA2SAP_PORELEASE_DEV_Service;
import com.sap.sap004.sap_com.ZOA2SAP_PORELEASE_DEV_ServiceLocator;
import com.sap.sap004.sap_com.ZSAP_OA_004Stub;
import com.sap.sap004.sap_com.ZSPOAPRV;
import com.sap.sap004.sap_com.holders.TABLE_OF_ZSPOAPRVHolder;
import com.sap.sap006.sap_com.ZOA2SAP_YFKAPOV_DEV_Service;
import com.sap.sap006.sap_com.ZOA2SAP_YFKAPOV_DEV_ServiceLocator;
import com.sap.sap006.sap_com.ZSAP_OA_006Stub;
import com.sap.sap006.sap_com.ZSYFKRETURN;
import com.sap.sap006.sap_com.holders.TABLE_OF_ZSYFKRETURNHolder;
import com.sap.sap008.sap_com.ZSAP_OA_008_BindingStub;
import com.sap.sap008.sap_com.ZSAP_OA_008_Service;
import com.sap.sap008.sap_com.ZSAP_OA_008_ServiceLocator;
import com.sap.sap008.sap_com.ZSLYRETURN;
import com.sap.sap008.sap_com.holders.TABLE_OF_ZSLYRETURNHolder;

import net.sf.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.MainTableInfo;
import weaver.soa.workflow.request.Property;
import weaver.soa.workflow.request.RequestInfo;

/**
 * SAP Action
 * @author user
 *
 */

public class SapAction extends BaseBean implements Action
{
  public String execute(RequestInfo info)
  {
	
    int billid = info.getRequestManager().getBillid();
    String tablename = info.getRequestManager().getBillTableName();
    String requestid=info.getRequestid();
    String workflowid=info.getWorkflowid();
    MainTableInfo maininfo = info.getMainTableInfo();
    int count = maininfo.getPropertyCount();
    
    if(workflowid.equals("49")){
    	ZSAP_OA_002(info);
    }else if(workflowid.equals("50")){
    	ZSAP_OA_004(info);
    }
    
    
    RecordSet rs = new RecordSet();
    
    return "1";
  }
  
  /**
   * SAP_OA_008 成本中心审批回传
   * @param info
   */
  public void ZSAP_OA_008(RequestInfo info){
	  
	  try {
			
			System.out.println("正在调用接口");
			
			RecordSet recordSet=new RecordSet();
			String workflowid=info.getWorkflowid();
			
			ZSAP_OA_008_Service service = new ZSAP_OA_008_ServiceLocator();
			ZSAP_OA_008_BindingStub serviceStub=(ZSAP_OA_008_BindingStub)service.getZSAP_OA_008();
			
			MainTableInfo maininfo = info.getMainTableInfo();
			int count = maininfo.getPropertyCount();
			
			//获取编号
			String RSNUM=""; //编号
			for (int i = 0; i < count; i++){
				Property p = maininfo.getProperty(i);
			    String name = p.getName().toUpperCase();
			    String value = p.getValue();
			    if ("RSNUM".equalsIgnoreCase(name)) {
			    	RSNUM = value; 
			    }
			}
			
			String tablename=getFormtable(workflowid); //流程表名
			String detailTablename=tablename+"_dt1"; //明细表名
			
			/*
			String sql="select * from "+detailTablename;
			recordSet.execute(sql);
			
			
			int records=recordSet.getCounts();
			ZSPRETURN[] zspreturns=new ZSPRETURN[records];
			int index=0;
			//构建返回明细
			while(recordSet.next()){
				
				String BNFPO=recordSet.getString("BNFPO"); //行项目
				String FRGZU="X"; //审批状态
				ZSPRETURN zspreturn=new ZSPRETURN(BANFN, BNFPO, FRGZU);
				zspreturns[index]=zspreturn;
				index++;
				
			}
			TABLE_OF_ZSPRETURNHolder IT_PRETURN=new TABLE_OF_ZSPRETURNHolder(zspreturns);
			*/
			
			ZSLYRETURN zspoaprv=new ZSLYRETURN(RSNUM, "X");
			ZSLYRETURN[] zspoaprvs=new ZSLYRETURN[1];
			zspoaprvs[0]=zspoaprv;
			TABLE_OF_ZSLYRETURNHolder IT_YFK_RETURN=new TABLE_OF_ZSLYRETURNHolder(zspoaprvs);
			
			//构建日志
			String logid=SapUtil.getLogid();
			com.sap.sap008.sap_com.ZSINTTURN[] zsintturns=new com.sap.sap008.sap_com.ZSINTTURN[1];
			com.sap.sap008.sap_com.ZSINTTURN zsintturn=new com.sap.sap008.sap_com.ZSINTTURN(logid, "000010", "", "M", "SAP_OA_008",RSNUM,"00000001", "", "", "", "", "", "", "", "", "AT-LUZHF/OA");
			zsintturns[0]=zsintturn;
			com.sap.sap008.sap_com.holders.TABLE_OF_ZSINTTURNHolder IT_NECETR=new com.sap.sap008.sap_com.holders.TABLE_OF_ZSINTTURNHolder(zsintturns);
			
			//调用SAP接口
			serviceStub.ZSAP_OA_008(IT_YFK_RETURN,IT_NECETR);
			
			//返回处理结果状态 S 是处理成功  E是处理失败
			String returnid=IT_NECETR.value[0].getRETURNID();
			
			System.out.println("returnid:"+returnid);
			
			writeLog("[SapAction.ZSAP_OA_008] SAP调用成功");
			
			//保存日志
			JSONObject logObj=SapUtil.transObject(JSONObject.fromObject(zsintturn));
			logObj.element("logid_o", logid);
			SapUtil.addLog(logObj);
			
			
		} catch (Exception error) {
			writeLog("[SapAction.ZSAP_OA_008] SAP调用失败 error:"+error.getMessage());
			error.printStackTrace();
			
		}
	  
  }
  
  /**
   * SAP_OA_006 预付款审批回传
   * @param info
   */
  public void ZSAP_OA_006(RequestInfo info){
	  
	  try {
			
			System.out.println("正在调用接口");
			
			RecordSet recordSet=new RecordSet();
			String workflowid=info.getWorkflowid();
			
			ZOA2SAP_YFKAPOV_DEV_Service service = new ZOA2SAP_YFKAPOV_DEV_ServiceLocator();
			ZSAP_OA_006Stub serviceStub=(ZSAP_OA_006Stub)service.getZSAP_OA_006();
			
			MainTableInfo maininfo = info.getMainTableInfo();
			int count = maininfo.getPropertyCount();
			
			//获取预付款编号
			String ZHZH=""; //预付款编号
			for (int i = 0; i < count; i++){
				Property p = maininfo.getProperty(i);
			    String name = p.getName().toUpperCase();
			    String value = p.getValue();
			    if ("ZHZH".equalsIgnoreCase(name)) {
			    	ZHZH = value; 
			    }
			}
			
			String tablename=getFormtable(workflowid); //流程表名
			String detailTablename=tablename+"_dt1"; //明细表名
			
			/*
			String sql="select * from "+detailTablename;
			recordSet.execute(sql);
			
			
			int records=recordSet.getCounts();
			ZSPRETURN[] zspreturns=new ZSPRETURN[records];
			int index=0;
			//构建返回明细
			while(recordSet.next()){
				
				String BNFPO=recordSet.getString("BNFPO"); //行项目
				String FRGZU="X"; //审批状态
				ZSPRETURN zspreturn=new ZSPRETURN(BANFN, BNFPO, FRGZU);
				zspreturns[index]=zspreturn;
				index++;
				
			}
			TABLE_OF_ZSPRETURNHolder IT_PRETURN=new TABLE_OF_ZSPRETURNHolder(zspreturns);
			*/
			
			ZSYFKRETURN zspoaprv=new ZSYFKRETURN(ZHZH, "X");
			ZSYFKRETURN[] zspoaprvs=new ZSYFKRETURN[1];
			zspoaprvs[0]=zspoaprv;
			TABLE_OF_ZSYFKRETURNHolder IT_YFK_RETURN=new TABLE_OF_ZSYFKRETURNHolder(zspoaprvs);
			
			//构建日志
			String logid=SapUtil.getLogid();
			com.sap.sap006.sap_com.ZSINTTURN[] zsintturns=new com.sap.sap006.sap_com.ZSINTTURN[1];
			com.sap.sap006.sap_com.ZSINTTURN zsintturn=new com.sap.sap006.sap_com.ZSINTTURN(logid, "000010", "", "M", "SAP_OA_006",ZHZH,"00000001", "", "", "", "", "", "", "", "", "AT-LUZHF/OA");
			zsintturns[0]=zsintturn;
			com.sap.sap006.sap_com.holders.TABLE_OF_ZSINTTURNHolder IT_NECETR=new com.sap.sap006.sap_com.holders.TABLE_OF_ZSINTTURNHolder(zsintturns);
			
			//调用SAP接口
			serviceStub.ZSAP_OA_006(IT_NECETR, IT_YFK_RETURN);
			
			//返回处理结果状态 S 是处理成功  E是处理失败
			String returnid=IT_NECETR.value[0].getRETURNID();
			
			System.out.println("returnid:"+returnid);
			
			writeLog("[SapAction.ZSAP_OA_006] SAP调用成功");
			
			//保存日志
			JSONObject logObj=SapUtil.transObject(JSONObject.fromObject(zsintturn));
			logObj.element("logid_o", logid);
			SapUtil.addLog(logObj);
			
			
		} catch (Exception error) {
			writeLog("[SapAction.ZSAP_OA_006] SAP调用失败 error:"+error.getMessage());
			error.printStackTrace();
			
		}
	  
  }
  
  /**
   * SAP_OA_004 采购订单审批回传
   * @param info
   */
  public void ZSAP_OA_004(RequestInfo info){
	  
	  try {
			
			System.out.println("正在调用接口");
			
			RecordSet recordSet=new RecordSet();
			String workflowid=info.getWorkflowid();
			
			ZOA2SAP_PORELEASE_DEV_Service service = new ZOA2SAP_PORELEASE_DEV_ServiceLocator();
			ZSAP_OA_004Stub serviceStub=(ZSAP_OA_004Stub)service.getZSAP_OA_004();
			
			MainTableInfo maininfo = info.getMainTableInfo();
			int count = maininfo.getPropertyCount();
			
			//获取采购订单号
			String EBELN=""; //采购订单号
			for (int i = 0; i < count; i++){
				Property p = maininfo.getProperty(i);
			    String name = p.getName().toUpperCase();
			    String value = p.getValue();
			    if ("EBELN".equalsIgnoreCase(name)) {
			    	EBELN = value; 
			    }
			}
			
			String tablename=getFormtable(workflowid); //流程表名
			String detailTablename=tablename+"_dt1"; //明细表名
			
			/*
			String sql="select * from "+detailTablename;
			recordSet.execute(sql);
			
			
			int records=recordSet.getCounts();
			ZSPRETURN[] zspreturns=new ZSPRETURN[records];
			int index=0;
			//构建返回明细
			while(recordSet.next()){
				
				String BNFPO=recordSet.getString("BNFPO"); //行项目
				String FRGZU="X"; //审批状态
				ZSPRETURN zspreturn=new ZSPRETURN(BANFN, BNFPO, FRGZU);
				zspreturns[index]=zspreturn;
				index++;
				
			}
			TABLE_OF_ZSPRETURNHolder IT_PRETURN=new TABLE_OF_ZSPRETURNHolder(zspreturns);
			*/
			
			ZSPOAPRV zspoaprv=new ZSPOAPRV(EBELN, "X");
			ZSPOAPRV[] zspoaprvs=new ZSPOAPRV[1];
			zspoaprvs[0]=zspoaprv;
			TABLE_OF_ZSPOAPRVHolder zspoaprvHolder=new TABLE_OF_ZSPOAPRVHolder(zspoaprvs);
			
			//构建日志
			String logid=SapUtil.getLogid();
			com.sap.sap004.sap_com.ZSINTTURN[] zsintturns=new com.sap.sap004.sap_com.ZSINTTURN[1];
			com.sap.sap004.sap_com.ZSINTTURN zsintturn=new com.sap.sap004.sap_com.ZSINTTURN(logid, "000010", "", "M", "SAP_OA_004",EBELN,"00000001", "", "", "", "", "", "", "", "", "AT-LUZHF/OA");
			zsintturns[0]=zsintturn;
			com.sap.sap004.sap_com.holders.TABLE_OF_ZSINTTURNHolder IT_NECETR=new com.sap.sap004.sap_com.holders.TABLE_OF_ZSINTTURNHolder(zsintturns);
			
			//调用SAP接口
			serviceStub.ZSAP_OA_004(IT_NECETR, zspoaprvHolder);
			
			//返回处理结果状态 S 是处理成功  E是处理失败
			String returnid=IT_NECETR.value[0].getRETURNID();
			
			System.out.println("returnid:"+returnid);
			
			writeLog("[SapAction.ZSAP_OA_004] SAP调用成功");
			
			//保存日志
			JSONObject logObj=SapUtil.transObject(JSONObject.fromObject(zsintturn));
			logObj.element("logid_o", logid);
			SapUtil.addLog(logObj);
			
			
		} catch (Exception error) {
			writeLog("[SapAction.ZSAP_OA_004] SAP调用失败 error:"+error.getMessage());
			error.printStackTrace();
			
		}
	  
  }
  
  /**
   * SAP_OA_002 采购申请审批回传
   * @param info
   */
  public void ZSAP_OA_002(RequestInfo info){
	  
	  try {
			
			System.out.println("正在调用接口");
			
			RecordSet recordSet=new RecordSet();
			String workflowid=info.getWorkflowid();
			
			ZSAP_OA_002_Service service = new ZSAP_OA_002_ServiceLocator();
			OA_002Stub serviceStub=(OA_002Stub)service.getOA_002();
			
			MainTableInfo maininfo = info.getMainTableInfo();
			int count = maininfo.getPropertyCount();
			
			//获取采购申请号
			String BANFN=""; //采购申请号
			for (int i = 0; i < count; i++){
				Property p = maininfo.getProperty(i);
			    String name = p.getName().toUpperCase();
			    String value = p.getValue();
			    if ("BANFN".equalsIgnoreCase(name)) {
			    	BANFN = value; 
			    }
			}
			
			String tablename=getFormtable(workflowid); //流程表名
			String detailTablename=tablename+"_dt1"; //明细表名
			
			String sql="select * from "+detailTablename;
			recordSet.execute(sql);
			
			int records=recordSet.getCounts();
			ZSPRETURN[] zspreturns=new ZSPRETURN[records];
			int index=0;
			//构建返回明细
			while(recordSet.next()){
				
				String BNFPO=recordSet.getString("BNFPO"); //行项目
				String FRGZU="X"; //审批状态
				ZSPRETURN zspreturn=new ZSPRETURN(BANFN, BNFPO, FRGZU);
				zspreturns[index]=zspreturn;
				index++;
				
			}
			TABLE_OF_ZSPRETURNHolder IT_PRETURN=new TABLE_OF_ZSPRETURNHolder(zspreturns);
			
			//构建日志
			String logid=SapUtil.getLogid();
			ZSINTTURN[] zsintturns=new ZSINTTURN[1];
			ZSINTTURN zsintturn=new ZSINTTURN(logid, "000010", "", "M", "SAP_OA_002",BANFN,"0000000"+records, "", "", "", "", "", "", "", "", "AT-LUZHF/OA");
			zsintturns[0]=zsintturn;
			TABLE_OF_ZSINTTURNHolder IT_NECETR=new TABLE_OF_ZSINTTURNHolder(zsintturns);
			
			//调用SAP接口
			serviceStub.ZSAP_OA_002(IT_NECETR, IT_PRETURN);
			
			//返回处理结果状态 S 是处理成功  E是处理失败
			String returnid=IT_NECETR.value[0].getRETURNID();
			
			System.out.println("returnid:"+returnid);
			
			writeLog("[SapAction.ZSAP_OA_002] SAP调用成功");
			
			//保存日志
			JSONObject logObj=SapUtil.transObject(JSONObject.fromObject(zsintturn));
			logObj.element("logid_o", logid);
			SapUtil.addLog(logObj);
			
			
		} catch (Exception error) {
			writeLog("[SapAction.ZSAP_OA_002] SAP调用失败 error:"+error.getMessage());
			error.printStackTrace();
			
		}
	  
  }
  
  /**
   * SAP_OA_002 采购申请审批回传
   */
  public void ZSAP_OA_002Test(){
	  
	  try {
			
			System.out.println("正在调用接口");
			
			ZSAP_OA_002_Service service = new ZSAP_OA_002_ServiceLocator();
			OA_002Stub serviceStub=(OA_002Stub)service.getOA_002();
			
			String BANFN="0010001179"; //采购申请号
			String BNFPO="00010"; //行项目
			String FRGZU="X"; //审批状态
			ZSPRETURN zspreturn=new ZSPRETURN(BANFN, BNFPO, FRGZU);
			ZSPRETURN[] zspreturns=new ZSPRETURN[1];
			zspreturns[0]=zspreturn;
			TABLE_OF_ZSPRETURNHolder IT_PRETURN=new TABLE_OF_ZSPRETURNHolder(zspreturns);
			
			//100000000000000001d
			String logid="10002";
			ZSINTTURN[] zsintturns=new ZSINTTURN[1];
			ZSINTTURN zsintturn=new ZSINTTURN(logid+"", "000010", "", "M", "SAP_OA_002",BANFN+"/"+BNFPO+"/"+FRGZU,"00000001", "", "", "", "", "", "", "", "", "AT-LUZHF/OA");
			zsintturns[0]=zsintturn;
			TABLE_OF_ZSINTTURNHolder IT_NECETR=new TABLE_OF_ZSINTTURNHolder(zsintturns);
			
			serviceStub.ZSAP_OA_002(IT_NECETR, IT_PRETURN);
			
			String returnid=IT_NECETR.value[0].getRETURNID();
			
			JSONObject logObj=SapUtil.transObject(JSONObject.fromObject(zsintturn));
			logObj.element("logid_o", logid);
			
			SapUtil.addLog(logObj);
			
			System.out.println("returnid:"+returnid);
			
			writeLog("[SapAction.ZSAP_OA_002] SAP调用成功");
			
			
		} catch (Exception error) {
			writeLog("[SapAction.ZSAP_OA_002] SAP调用失败 error:"+error.getMessage());
			error.printStackTrace();
			
		}
	  
  }
  
  
  
  public static void main(String[] args) {
	
	  SapAction sapAction=new SapAction();
	  sapAction.ZSAP_OA_002Test();
	  
  }
  
  /**
	 * 获得表单table
	 * @param workflowid 流程id
	 * @return
	 */
	public String getFormtable(String workflowid){
		String formtable="";
		String sql="select tablename from workflow_base t1,workflow_bill t2 where t1.id="+workflowid+" and t2.id=t1.formid";
		RecordSet recordSet=new RecordSet();
		recordSet.execute(sql);
		if(recordSet.next())
			formtable=recordSet.getString("tablename");
		return formtable;
	}
  
}
