package com.sjc;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class ContractSign extends BaseBean implements Action {

	@Override
	public String execute(RequestInfo info) {
		// TODO Auto-generated method stub
		
		writeLog("****************************进入 ContractSign ****************************");
		
		boolean flag = true;

	    String requestid = info.getRequestid();
	    String workflowid = info.getWorkflowid();
	    
	    String formtable = DataUtil.getFormtable(workflowid);
	    
	    writeLog("[ContractSign.execute] workflowid:"+workflowid+" formtable:"+formtable+" requestid:"+requestid);
	    
	    CostManager costManager=new CostManager();
	    
	    String djbh = ""; //合同编号
	    String djry="";   //登记人员
	    
	    RecordSet recordSet = new RecordSet();
	    RecordSet rs = new RecordSet();
	    
	    String sql="select * from "+formtable+" where requestid="+requestid;
	    writeLog("[ContractSign.execute] 查询主表的SQL："+sql);
	    recordSet.execute(sql);
	    if(recordSet.next()){
	    	djbh=recordSet.getString("djbh");
	    	djry=recordSet.getString("djry");
	    }
	    
	    //合同签订  合同续签
	    if(workflowid.equals("150")||workflowid.equals("123")){
			costManager.delSfsz(requestid, workflowid);
		}
	    
    	writeLog("[ContractSign.execute] 新增合同费用明细 workflowid:"+workflowid+" requestid:"+requestid+" djbh:"+djbh);
        
        //新增合同费用明细
        costManager.addHtfymx(requestid,djbh,djry);
        
        writeLog("[ContractSign.execute] 设置账务明细 workflowid:"+workflowid+" requestid:"+requestid+" djbh:"+djbh);
        
        //根据合同编号将最小应收日期后一年的的费用设置明细转入 账务中心
        costManager.setZwmxByHtbh(djbh,djry);
    
        writeLog("****************************结束 ContractSign ****************************");
		
	    return ((flag) ? "1" : "0");
	}

}
