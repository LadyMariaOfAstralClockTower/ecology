package com.sjc;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.TimeUtil;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.MainTableInfo;
import weaver.soa.workflow.request.Property;
import weaver.soa.workflow.request.RequestInfo;

public class ContractAction extends BaseBean implements Action
{
  public String execute(RequestInfo info)
  {
    writeLog("****************************进入 ContractAction ****************************");
    boolean flag = true;

    String requestid = info.getRequestid();
    String workflowid = info.getWorkflowid();
    
    String formtable = DataUtil.getFormtable(workflowid);
    
    writeLog("[ContractAction.execute] workflowid:"+workflowid+" formtable:"+formtable+" requestid:"+requestid);

    MainTableInfo mainTableInfo = info.getMainTableInfo();
    Property[] properties = mainTableInfo.getProperty();
    
    CostManager costManager=new CostManager();
    
    String djbh = ""; //合同编号
    String hykssj=""; //合约开始
    String djry="";   //登记人员

    RecordSet recordSet = new RecordSet();
    RecordSet recordSet2 = new RecordSet();
    RecordSet recordSet1 = new RecordSet();
    RecordSet rs = new RecordSet();

    
    
    String sql="select * from "+formtable+" where requestid="+requestid;
    writeLog("查询主表的SQL："+sql);
    recordSet.execute(sql);
    if(recordSet.next()){
    	djbh=recordSet.getString("djbh");
    	hykssj=recordSet.getString("hykssj");
    	djry=recordSet.getString("djry");
    	
    }
    
    if("".equals(djbh)){
		djbh=recordSet.getString("htbh");
	}
    
    if("".equals(djry)){
    	djry=recordSet.getString("jbxm");
    }
    
    if(workflowid.equals("150")||workflowid.equals("123")){
    	
		sql="insert into uf_htsfszbak select * from uf_htsfsz where xglc="+requestid;
		rs.execute(sql);
		
		String sql1="delete from uf_htfymx where htbh='"+djbh+"' and je=ye"; //删除该收费设置 金额=余额 的收费设置明细
		boolean f1=rs.execute(sql1);
		writeLog("删除收费设置明细中的收费设置的SQL："+sql1+" 执行结果："+f1);
		String sql2="delete from uf_zwzx where htbh='"+djbh+"' and je=ye";   //删除该收费设置 金额=余额 的账务中心明细
		boolean f2=rs.execute(sql2);
		writeLog("删除账务中心明细中的收费设置的SQL："+sql2+" 执行结果："+f2);
	}
    
    //合同终止&合同变更
    if(workflowid.equals("143")||workflowid.equals("142")){
    	
    	djbh=recordSet.getString("htbh");
    	String creater=recordSet.getString("jbxm");
    	
    	writeLog("经办人姓名："+creater);
    	
    	sql = "select t2.* from " + formtable + " t1," + formtable+"_dt1 t2 where requestId=" + requestid + " and t1.id=t2.mainid";
    	
    	writeLog("合同终止查询的SQL："+sql);
    	recordSet.execute(sql);
    	while(recordSet.next()){
    		
    		String sfszid=recordSet.getString("sfszid"); //收费设置ID
    		String jdrq=recordSet.getString("jdrq"); //截断日期
    		String jsrq=recordSet.getString("jsrq"); //结束日期
    		String ksrq=recordSet.getString("ksrq"); //开始日期    		
    		
    		writeLog("jdrq:"+jdrq+" jsrq:"+jsrq+" requestid:"+requestid);
    		writeLog(!jdrq.equals("2099-12-31")&&TimeUtil.dateInterval(jdrq, jsrq)>0);
    		//日期不为 2099-12-31 并且 结束日期>阶段日期
    		if(!jdrq.equals("2099-12-31")&&TimeUtil.dateInterval(jdrq, jsrq)>0){
    			sql="delete from uf_htfymx where sfszid="+sfszid+" and je=ye"; //删除该收费设置 金额=余额 的收费设置明细
    			writeLog("删除收费设置明细中的收费设置的SQL："+sql);
        		recordSet2.execute(sql);
        		sql="delete from uf_zwzx where fyszid="+sfszid+" and je=ye";   //删除该收费设置 金额=余额 的账务中心明细
        		writeLog("删除账务中心明细中的收费设置的SQL："+sql);
        		recordSet2.execute(sql);
        		
        		sql="update uf_htsfsz set jsrq='"+jdrq+"',jdrq='"+jdrq+"' where id="+sfszid;
        		writeLog("更新的合同收费设置的SQL："+sql);
        		recordSet2.execute(sql);
        		
        		if(TimeUtil.dateInterval(ksrq, jdrq)>0){ //截断日期必须大于开始日期
        		
	        		//根据收费设置ID，重新设置费用明细
	        		sql="select * from uf_htsfsz where id="+sfszid;
	        		writeLog("通过id查询合同收费设置："+sql);
	        		recordSet2.execute(sql);
	        		
	        		if(recordSet2.next()){
	        			writeLog("设置合同费用明细，收费设置id："+sfszid);
	        			creater=recordSet2.getString("modedatacreater");
	        			costManager.setHtfymx(recordSet2, djbh,creater); //重新设置费用明细
	        		}
	        		
	        		//根据收费设置ID获取对应的费用设置明细，并将明细转入账务中心
	        		sql="select * from uf_htfymx where sfszid="+sfszid;
	        		recordSet2.execute(sql);
	        		writeLog("通过收费设置id查询费用设置明细："+sql);
	        		while(recordSet2.next()){
	        			writeLog("设置账务明细，收费设置id："+sfszid);
	        			costManager.setzwmx(recordSet2,recordSet1,creater,0); //重新设置 账务明细
	        		}
        		}
    		}
    	  
    	}
    	writeLog("************************设置合同费用明细和账务明细结束************************");
    }

    
    if(workflowid.equals("179")){
    	
    	writeLog("************************扶持减免费用批量审批开始************************");
    	
    	sql="insert into uf_htsfszbak select * from uf_htsfsz where xglc="+requestid;
		rs.execute(sql);

		String sql1="delete from uf_htfymx where htbh='"+djbh+"' and je=ye"; //删除该收费设置 金额=余额 的收费设置明细
		boolean f1=rs.execute(sql1);
		writeLog("删除收费设置明细中的收费设置的SQL："+sql1+" 执行结果："+f1);
		String sql2="delete from uf_zwzx where htbh='"+djbh+"' and je=ye";   //删除该收费设置 金额=余额 的账务中心明细
		boolean f2=rs.execute(sql2);
		writeLog("删除账务中心明细中的收费设置的SQL："+sql2+" 执行结果："+f2);
		
    	costManager.addHtfymx(djbh, "0");

    	String tjr = recordSet.getString("tjr");
    	
    	//根据收费设置ID，重新设置费用明细
		sql="select * from uf_htsfsz where xglc="+requestid;
		
		writeLog("查询收费设置的id："+sql);
		
		recordSet.execute(sql);
		
		while(recordSet.next()){
			
			String sfszid = recordSet.getString("id");
			String htbh = recordSet.getString("htbh");
			
			writeLog("正在处理的合同编号："+htbh);
			
			costManager.setHtfymx(recordSet,htbh,tjr); //重新设置费用明细
			
			//根据收费设置ID获取对应的费用设置明细，并将明细转入账务中心
			sql="select * from uf_htfymx where sfszid="+sfszid;
			
			writeLog("查询费用设置明细："+sql);
			
			recordSet2.execute(sql);
			while(recordSet2.next()){
				writeLog("正在处理的合同编号："+htbh);
				costManager.setzwmx(recordSet2,recordSet1,tjr,0); //重新设置 账务明细
			}
		}
		
		writeLog("************************扶持减免费用批量审批结束************************");
		return ((flag) ? "1" : "0");
    }
    
    	writeLog("[ContractAction.execute] 新增合同费用明细 workflowid:"+workflowid+" requestid:"+requestid+" djbh:"+djbh);
        
        //新增合同费用明细
        costManager.addHtfymx(requestid,djbh,djry);
        
        writeLog("[ContractAction.execute] 设置账务明细 workflowid:"+workflowid+" requestid:"+requestid+" djbh:"+djbh);
        
        //根据合同编号将最小应收日期后一年的的费用设置明细转入 账务中心
        costManager.setZwmxByHtbh(djbh,djry);
    

    return ((flag) ? "1" : "0");
  }

  public static void main(String[] args)
  {
	  String str=",1,2,3,";
	  System.out.println(str.substring(1,str.length()-1));
  }
}