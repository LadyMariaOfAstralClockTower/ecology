package com.jgj;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class Intergration extends BaseBean implements Action {

	@Override
	public String execute(RequestInfo arg0) {
		// TODO Auto-generated method stub
		
		writeLog("**********************************  [Intergration]开始  ************************************");
		
		String wfid=arg0.getWorkflowid();
		String requestid=arg0.getRequestid();
		
		String formtable=getFormtable(wfid);
		
		writeLog("**********************************  [Intergration]开始  ************************************");
		
		return Action.SUCCESS;
	}

	
	/**
	 * 获得表单table
	 * @param workflowid 流程id
	 * @return
	 */
	public static String getFormtable(String workflowid){
		String formtable="";
		String sql="select tablename from workflow_base t1,workflow_bill t2 where t1.id="+workflowid+" and t2.id=t1.formid";
		RecordSet recordSet=new RecordSet();
		recordSet.execute(sql);
		if(recordSet.next()){
			formtable=recordSet.getString("tablename");
		}
		return formtable;
	}
	
}
