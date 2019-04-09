package com.sjc;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class BatchCost extends BaseBean implements Action {

	@Override
	public String execute(RequestInfo info) {
		// TODO Auto-generated method stub
		
		writeLog("****************************进入 BatchCost ****************************");
	    boolean flag = true;

	    String requestid = info.getRequestid();
	    String workflowid = info.getWorkflowid();
	    
	    String formtable = DataUtil.getFormtable(workflowid);
	    
	    writeLog("[BatchCost.execute] workflowid:"+workflowid+" formtable:"+formtable+" requestid:"+requestid);
	    
	    CostManager costManager=new CostManager();
	    
	    RecordSet rs = new RecordSet(); 
	    RecordSet recordSet = new RecordSet();
	    RecordSet recordSet1 = new RecordSet();
	    RecordSet recordSet2 = new RecordSet();
	    
	    String djbh = ""; //合同编号
	    String tjr="";   //提交人
	    
	    String sql="select * from "+formtable+" where requestid="+requestid;
	    writeLog("查询主表的SQL："+sql);
	    recordSet.execute(sql);
	    if(recordSet.next()){
	    	djbh=recordSet.getString("htbh");
	    	tjr=recordSet.getString("tjr");
	    	
	    }
	    
	    String[] djbhs=Util.TokenizerStringNew(djbh, ",");
	    
	    if(workflowid.equals("179")){
	    	
	    	writeLog("************************扶持减免费用批量审批开始************************");
	    	
	    	costManager.delSfsz(requestid, workflowid);//先删除退回的收费设置
	    	
	    	//根据收费设置ID，重新设置费用明细
			sql="select * from uf_htsfsz where xglc="+requestid;
			
			writeLog("[BatchCost.execute] 查询收费设置的id："+sql);
			
			recordSet.execute(sql);
			
			while(recordSet.next()){
				
				String sfszid = recordSet.getString("id");
				String htbh = recordSet.getString("htbh");
				
				writeLog("[BatchCost.execute] 正在处理的合同编号："+htbh);
				
				costManager.setHtfymx(recordSet,htbh,tjr); //重新设置费用明细
				
				//根据收费设置ID获取对应的费用设置明细，并将明细转入账务中心
				sql="select * from uf_htfymx where sfszid="+sfszid;
				
				writeLog("[BatchCost.execute] 查询费用设置明细："+sql);
				
				recordSet2.execute(sql);
				while(recordSet2.next()){
					writeLog("[BatchCost.execute] 正在处理的合同编号："+htbh);
					costManager.setzwmx(recordSet2,recordSet1,tjr,0); //重新设置 账务明细
				}
			}
			
			writeLog("************************扶持减免费用批量审批结束************************");
	    }
	    
	    for(String bh:djbhs){
	    	writeLog("[BatchCost.execute] 新增合同费用明细 workflowid:"+workflowid+" requestid:"+requestid+" djbh:"+bh);
	        
	        //新增合同费用明细
	        costManager.addMulHtfymx(requestid,bh,tjr);
	        
	        writeLog("[BatchCost.execute] 设置账务明细 workflowid:"+workflowid+" requestid:"+requestid+" djbh:"+bh);
	        
	        //根据合同编号将最小应收日期后一年的的费用设置明细转入 账务中心
	        costManager.setZwmxByHtbh(bh,tjr);
	    }
		
   	    
	    
	    writeLog("****************************结束 BatchCost ****************************");
		return ((flag) ? "1" : "0");
	}

}
