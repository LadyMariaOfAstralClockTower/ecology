package com.sjc;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.TimeUtil;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class ContractTerminate extends BaseBean implements Action {

	//合同终止&合同变更
	@Override
	public String execute(RequestInfo info) {
		// TODO Auto-generated method stub
		
    writeLog("****************************进入 ContractTerminate ****************************");
		
		boolean flag = true;

	    String requestid = info.getRequestid();
	    String workflowid = info.getWorkflowid();
	    
	    String formtable = DataUtil.getFormtable(workflowid);
	    
	    writeLog("[ContractTerminate.execute] workflowid:"+workflowid+" formtable:"+formtable+" requestid:"+requestid);
	    
	    CostManager costManager=new CostManager();
	    
	    String djbh = ""; //合同编号
	    String jbxm="";   //经办姓名
	    
	    RecordSet recordSet = new RecordSet();
	    RecordSet recordSet1 = new RecordSet();
	    RecordSet recordSet2 = new RecordSet();
	    
	    String sql="select * from "+formtable+" where requestid="+requestid;
	    writeLog("[ContractTerminate.execute] 查询主表的SQL："+sql);
	    recordSet.execute(sql);
	    if(recordSet.next()){
	    	djbh=recordSet.getString("htbh");
	    	jbxm=recordSet.getString("jbxm");
	    }
	    
	  //合同终止&合同变更
	    if(workflowid.equals("143")||workflowid.equals("142")){

	    	costManager.delSfsz(requestid, workflowid);
	    	
	    	writeLog("[ContractTerminate.execute] 经办人姓名："+jbxm);
	    	
	    	sql = "select t2.* from " + formtable + " t1," + formtable+"_dt1 t2 where requestId=" + requestid + " and t1.id=t2.mainid";
	    	
	    	writeLog("[ContractTerminate.execute] 合同终止查询的SQL："+sql);
	    	recordSet.execute(sql);
	    	while(recordSet.next()){
	    		
	    		String sfszid=recordSet.getString("sfszid"); //收费设置ID
	    		String jdrq=recordSet.getString("jdrq"); //截断日期
	    		String jsrq=recordSet.getString("jsrq"); //结束日期
	    		String ksrq=recordSet.getString("ksrq"); //开始日期
	    		
	    		writeLog("jdrq:"+jdrq+" jsrq:"+jsrq+" requestid:"+requestid);
	    		writeLog(!jdrq.equals("2099-12-31")&&TimeUtil.dateInterval(jdrq, jsrq)>0);
	    		//日期不为 2099-12-31 并且 结束日期>截断日期
	    		if(!jdrq.equals("2099-12-31")&&TimeUtil.dateInterval(jdrq, jsrq)>0){
	    			
	    			String temp=getLastMonthLastDay(jdrq);
	    			
	    			sql="delete from uf_htfymx where sfszid="+sfszid+" and je=ye and ksrq>'"+temp+"'"; //删除该收费设置 金额=余额 的收费设置明细
	    			writeLog("[ContractTerminate.execute] 删除收费设置明细中的收费设置的SQL："+sql);
	        		recordSet2.execute(sql);
	        		sql="delete from uf_zwzx where fyszid="+sfszid+" and je=ye and ksrq>'"+temp+"' and htfymxid is not null and htfymxid>0";   //删除该收费设置 金额=余额 的账务中心明细
	        		writeLog("[ContractTerminate.execute] 删除账务中心明细中的收费设置的SQL："+sql);
	        		recordSet2.execute(sql);
	        		
	        		sql="update uf_htsfsz set jsrq='"+jdrq+"',jdrq='"+jdrq+"' where id="+sfszid;
	        		writeLog("[ContractTerminate.execute] 更新的合同收费设置的SQL："+sql);
	        		recordSet2.execute(sql);
	        		
	        		if(TimeUtil.dateInterval(ksrq, jdrq)>0){ //截断日期必须大于开始日期
	        		
		        		//根据收费设置ID，重新设置费用明细
		        		sql="select * from uf_htsfsz where id="+sfszid;
		        		writeLog("[ContractTerminate.execute] 通过id查询合同收费设置："+sql);
		        		recordSet2.execute(sql);
		        		
		        		if(recordSet2.next()){
		        			writeLog("[ContractTerminate.execute] 设置合同费用明细，收费设置id："+sfszid);
		        			jbxm=recordSet2.getString("modedatacreater");
		        			costManager.setHtfymx(recordSet2, djbh,jbxm); //重新设置费用明细
		        		}
		        		
		        		//根据收费设置ID获取对应的费用设置明细，并将明细转入账务中心
		        		sql="select * from uf_htfymx where sfszid="+sfszid;
		        		recordSet2.execute(sql);
		        		writeLog("[ContractTerminate.execute] 通过收费设置id查询费用设置明细："+sql);
		        		while(recordSet2.next()){
		        			writeLog("[ContractTerminate.execute] 设置账务明细，收费设置id："+sfszid);
		        			costManager.setzwmx(recordSet2,recordSet1,jbxm,0); //重新设置 账务明细
		        		}
	        		}
	    		}
	    	  
	    	}
	    	writeLog("************************设置合同费用明细和账务明细结束************************");
	    }
	    
	    
	    writeLog("[ContractSign.execute] 新增合同费用明细 workflowid:"+workflowid+" requestid:"+requestid+" djbh:"+djbh);
        
        //新增合同费用明细
        costManager.addHtfymx(requestid,djbh,jbxm);
        
        writeLog("[ContractSign.execute] 设置账务明细 workflowid:"+workflowid+" requestid:"+requestid+" djbh:"+djbh);
        
        //根据合同编号将最小应收日期后一年的的费用设置明细转入 账务中心
        costManager.setZwmxByHtbh(djbh,jbxm);
	    
	    
	    writeLog("****************************结束 ContractTerminate ****************************");
	    return ((flag) ? "1" : "0");
	}

	
	public static void main(String[] args) {
		System.out.println(getLastMonthLastDay("2017-01-15"));
	}
	
	
	 /**
     * 
     * 描述:获取上个月的最后一天.
     * 
     * @return
     */
    public static String getLastMonthLastDay(String jdrq) {
    	String[] temp=Util.TokenizerStringNew(jdrq, "-");
		int year=Util.getIntValue(temp[0]);
		int month=Util.getIntValue(temp[1]);
		int day=Util.getIntValue(temp[2]);
        SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(year,month,day);
        calendar.add(Calendar.MONTH, -2);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return dft.format(calendar.getTime());
    }
    
}
