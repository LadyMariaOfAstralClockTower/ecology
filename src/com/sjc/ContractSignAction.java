package com.sjc;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;

import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.MainTableInfo;
import weaver.soa.workflow.request.Property;
import weaver.soa.workflow.request.RequestInfo;

/**
 * 合同签订
 * @author feng
 *
 */

public class ContractSignAction extends BaseBean implements Action
{
  public String execute(RequestInfo info)
  {
    writeLog("****************************进入 ContractSignAction ****************************");
    boolean flag = true;

    String requestid = info.getRequestid();
    String workflowid = info.getWorkflowid();
    
    String formtable = DataUtil.getFormtable(workflowid);
    
    writeLog("[ContractSignAction.execute] workflowid:"+workflowid+" formtable:"+formtable+" requestid:"+requestid);

    MainTableInfo mainTableInfo = info.getMainTableInfo();
    Property[] properties = mainTableInfo.getProperty();
    
    CostManager costManager=new CostManager();
    
    String djbh = ""; //合同编号


    RecordSet recordSet = new RecordSet();

    
    String sql="select * from "+formtable+" where requestid="+requestid;
    recordSet.execute(sql);
    if(recordSet.next()){
    	djbh=recordSet.getString("djbh");
    }
    
    //合同签订
    if(workflowid.equals("150")){
    	
    	writeLog("*************************拼接开始*************************");
    	
    	String qyids=",";
    	String lcids=",";
    	String dkids=",";
    	
    	sql = "select t2.* from " + formtable + " t1," + formtable+"_dt1 t2 where requestId=" + requestid + " and t1.id=t2.mainid";
    	recordSet.execute(sql);
    	while(recordSet.next()){
    		
    		String dk=CostUtil.getPubid("8857",recordSet.getString("dk"));
    		String qy=CostUtil.getPubid("8858",recordSet.getString("qy"));
    		String lc=CostUtil.getPubid("8859",recordSet.getString("lc"));
    		
    		if(!qyids.contains(","+qy+",")){
    			qyids+=qy+",";
    		}
    		if(!lcids.contains(","+lc+",")){
    			lcids+=lc+",";
    		}
    		if(!dkids.contains(","+dk+",")){
    			dkids+=dk+",";
    		}
    	}
    	qyids=qyids.length()>1?qyids.substring(1, qyids.length()-1):"";
    	lcids=lcids.length()>1?lcids.substring(1, lcids.length()-1):"";
    	dkids=dkids.length()>1?dkids.substring(1, dkids.length()-1):"";
    	
    	sql="update "+formtable+" set qy='"+qyids+"',lc='"+lcids+"',dk='"+dkids+"' where requestid="+requestid;
    	recordSet.execute(sql);
    	
    	writeLog("[ContractSignAction.execute] 更新流程区域、楼层 sql:"+sql);
    	
    	sql="update uf_htjcxx set qy='"+qyids+"',lc='"+lcids+"' where djbh='"+djbh+"'";
    	recordSet.execute(sql);
    	
    	writeLog("[ContractSignAction.execute] 更新合同区域、楼层 sql:"+sql);
    	writeLog("*************************拼接结束*************************");
    	
    	
    }
    
    //合同变更
    if(workflowid.equals("142")){
    	
    	String qyids=",";
    	String lcids=",";
    	String dkids=",";
    	
    	sql = "select t2.* from " + formtable + " t1," + formtable+"_dt4 t2 where requestId=" + requestid + " and t1.id=t2.mainid";
    	recordSet.execute(sql);
    	while(recordSet.next()){
    		
    		String dk=CostUtil.getPubid("11129",recordSet.getString("dk1"));
    		String qy=CostUtil.getPubid("11138",recordSet.getString("qy1"));
    		String lc=CostUtil.getPubid("11130",recordSet.getString("lc1"));
    		
    		if(!qyids.contains(","+qy+",")){
    			qyids+=qy+",";
    		}
    		if(!lcids.contains(","+lc+",")){
    			lcids+=lc+",";
    		}
    		if(!dkids.contains(","+dk+",")){
    			dkids+=dk+",";
    		}
    	}
    	qyids=qyids.length()>1?qyids.substring(1, qyids.length()-1):"";
    	lcids=lcids.length()>1?lcids.substring(1, lcids.length()-1):"";
    	dkids=dkids.length()>1?dkids.substring(1, dkids.length()-1):"";
    	
    	sql="update "+formtable+" set qy1='"+qyids+"',lc1='"+lcids+"',dk1='"+dkids+"' where requestid="+requestid;
    	recordSet.execute(sql);
    	
    	writeLog("[ContractSignAction.execute] 更新流程区域、楼层 sql1:"+sql);
    	
    	sql="update uf_htjcxx set qy='"+qyids+"',lc='"+lcids+"' where djbh='"+djbh+"'";
    	recordSet.execute(sql);
    	
    	writeLog("[ContractSignAction.execute] 更新合同区域、楼层 sql2:"+sql);
    	
    }
    
    //合同签订&合同续签
    if(workflowid.equals("150")||workflowid.equals("123")){
    	
    	String zsxzhj=",";
    	
    	sql = "select t2.* from " + formtable + " t1," + formtable+"_dt1 t2 where requestId=" + requestid + " and t1.id=t2.mainid";
    	recordSet.execute(sql);
    	while(recordSet.next()){
    		
    		String zsxz=CostUtil.getPubid("11894",recordSet.getString("zsxz"));
    		
    		if(!zsxzhj.contains(","+zsxz+",")){
    			zsxzhj+=zsxz+",";
    		}
    	}
    	
    	zsxzhj=zsxzhj.length()>1?zsxzhj.substring(1, zsxzhj.length()-1):"";
    	
		sql="update "+formtable+" set zsxzhj='"+zsxzhj+"' where requestid="+requestid;
		
		writeLog("[ContractSignAction.execute] 更新流程租售性质合计 sql:"+sql);
		
		recordSet.execute(sql);
    	
    }
    
    
    //合同变更
    if(workflowid.equals("142")){
    	
    	String zsxzhj1=",";
    	
    	sql = "select t2.* from " + formtable + " t1," + formtable+"_dt4 t2 where requestId=" + requestid + " and t1.id=t2.mainid";
    	recordSet.execute(sql);
    	while(recordSet.next()){
    		
    		String zsxz1=CostUtil.getPubid("11936",recordSet.getString("zsxz1"));
    		
    		if(!zsxzhj1.contains(","+zsxz1+",")){
    			zsxzhj1+=zsxz1+",";
    		}
    	}

		zsxzhj1=zsxzhj1.length()>1?zsxzhj1.substring(1, zsxzhj1.length()-1):"";
		
		sql="update "+formtable+" set zsxzhj1='"+zsxzhj1+"' where requestid="+requestid;
		
		writeLog("[ContractSignAction.execute] 更新流程租售性质合计 sql:"+sql);
		
		recordSet.execute(sql);
    	
    }
    

    return ((flag) ? "1" : "0");
  }

}