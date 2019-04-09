package com.cj;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.TimeUtil;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
import weaver.workflow.request.RequestManager;

/**
 * 费控流程Action
 * @author GodWei
 *
 */

public class BillAction extends BaseBean implements Action{
	
  public String execute(RequestInfo info){
	
		boolean flag=true;
	    //int billid = info.getRequestManager().getBillid();
	    RequestManager requestManager= info.getRequestManager();
	    String requestid=info.getRequestid();
	    String workflowid=info.getWorkflowid();
	    int nodeid=requestManager.getNodeid();
	    
	    writeLog("*********************************** 进入BillAction ***********************************"); 
	    writeLog("[.BillAction.execute] workflowid:"+workflowid+" requestid:"+requestid+" nodeid:"+nodeid);
	    
	    if(workflowid.equals("176")){
	    	flag=billXML264(info,"264X-Cxx-23");
	    }else if(workflowid.equals("177")){
	    	flag=billXML264(info,"264X-Cxx-24");
	    }else if(workflowid.equals("178")){
	    	flag=billXML264(info,"264X-Cxx-21");
	    }else if(workflowid.equals("179")){
	    	flag=billXML264(info,"264X-Cxx-22");
	    }
	    
	    writeLog("*********************************** 结束BillAction ***********************************"); 
	    //return flag;
	    return flag?"1":"0";
  }
  
  
  public boolean billXML264(RequestInfo info,String djlxbm){
	  
	  writeLog("[BillAction.billXML264] 报销开始....");
		
	  Map<String, String> maindata=new HashMap<String, String>();
	  List<Map<String,String>> detaildata=new ArrayList<Map<String,String>>();
	  
	  maindata.put("pk_group", "CSAID");
	  maindata.put("pk_org", "E00");
	  maindata.put("pk_fiorg", "E00");
	  maindata.put("pk_payorg", "E00");
	  
	  maindata.put("djlxbm", djlxbm);
	  
	  maindata.put("qcbz", "N");
	  maindata.put("qzzt", "0");
	  maindata.put("payflag", "1");
	  maindata.put("paytarget", "0");
	  maindata.put("jobid", "");//项目编号
	  maindata.put("fjzs", "3");
	  
	  maindata.put("fydwbm", "E00");
	  maindata.put("dwbm", "E00");
	  maindata.put("bzbm", "CNY");
	  maindata.put("djdl", "bx");
	  maindata.put("djzt", "1");
	  maindata.put("operator", "E0000042");
	  maindata.put("szxmid", "ZX9904");
	  maindata.put("approver", "uftyq");
		
	  maindata=getMainData(maindata, info);
	  

	  detaildata=getDetailData(detaildata, info);
		
	  Map<String, String> resultMap=NCUtil.doPost("264X", maindata, detaildata, "");
	  String successful=resultMap.get("successful");
	  writeLog("[BillAction.billXML264] 报销结束....");
	  
	  return successful.equals("Y")?true:false;
  }
  
  /**
   * 获取主表数据
   * @param maindata
   * @param info
   * @return
   */
  public Map<String, String> getMainData(Map<String, String> maindata,RequestInfo info){
	  
	  RecordSet recordSet=new RecordSet();
	  String formtable = info.getRequestManager().getBillTableName();
	  String requestid=info.getRequestid();
	  String workflowid=info.getWorkflowid();
	  ModeUtil modeUtil=new ModeUtil();
	  
	  String sql="select * from "+formtable+" where requestid="+requestid;
	  recordSet.execute(sql);
	  writeLog("[BillAction.getMainData]查询主表的SQL:"+sql);
	  if(recordSet.next()){
		  
		  if(workflowid.equals("178")){
			  
			  String djbh=recordSet.getString("lcbh");//单据编号
			  String deptid=modeUtil.getDeptCode(recordSet.getString("sqbm"));//申请部门编号
			  String jkbxr=modeUtil.getWorkCode(recordSet.getString("xm"));//借款报销人
			  String fydeptid=modeUtil.getDeptCode(recordSet.getString("cdbm1"));;//费用承担部门
			  String total=recordSet.getString("hj");//总计
			  String receiver=modeUtil.getWorkCode(recordSet.getString("xm"));//接收人
			  String ybje=recordSet.getString("hj");//预报金额
			  String djrq=recordSet.getString("sqsj");//申请时间
			  
			  writeLog("[BillAction.getMainData]workflowid:"+workflowid+" djbh:"+djbh+" djrq:"+djrq+" deptid:"+deptid+" jkbxr:"+jkbxr+" fydeptid:"+fydeptid+" total:"+total+" receiver:"+receiver+" ybje:"+ybje);
			  
			  maindata.put("djbh", djbh);
			  maindata.put("djrq", djrq);
			  maindata.put("deptid", deptid);
			  maindata.put("jkbxr", jkbxr);
			  maindata.put("fydeptid", fydeptid);
			  maindata.put("total", total);
			  maindata.put("receiver", receiver);
			  maindata.put("ybje", ybje);
			  
		  }else if(workflowid.equals("179")||workflowid.equals("177")){
			  
			  String djbh=recordSet.getString("lcbh");//单据编号
			  String deptid=modeUtil.getDeptCode(recordSet.getString("sqbm"));//申请部门编号
			  String jkbxr=modeUtil.getWorkCode(recordSet.getString("xm"));//借款报销人
			  String fydeptid=modeUtil.getDeptCode(recordSet.getString("sqbm"));;//费用承担部门
			  String total=recordSet.getString("hj");//总计
			  String receiver=modeUtil.getWorkCode(recordSet.getString("xm"));//接收人
			  String ybje=recordSet.getString("hj");//预报金额
			  String djrq=recordSet.getString("sqsj");//申请时间
			  
			  writeLog("[BillAction.getMainData]workflowid:"+workflowid+" djbh:"+djbh+" djrq:"+djrq+" deptid:"+deptid+" jkbxr:"+jkbxr+" fydeptid:"+fydeptid+" total:"+total+" receiver:"+receiver+" ybje:"+ybje);
			  
			  maindata.put("djbh", djbh);
			  maindata.put("djrq", djrq);
			  maindata.put("deptid", deptid);
			  maindata.put("jkbxr", jkbxr);
			  maindata.put("fydeptid", fydeptid);
			  maindata.put("total", total);
			  maindata.put("receiver", receiver);
			  maindata.put("ybje", ybje);
			  
		  }else if(workflowid.equals("176")){
			  
			  	String djbh=recordSet.getString("lcbh"); //单据编号
			  	String djrq=recordSet.getString("sqsj");//申请时间
				String deptid=modeUtil.getDeptCode(recordSet.getString("sqbm")); //部门编号
				String jkbxr=modeUtil.getWorkCode(recordSet.getString("xm")); //借款报销人
				String fydeptid=modeUtil.getDeptCode(recordSet.getString("cdbm1")); //费用承担部门
				String total=recordSet.getString("hj"); //合计
				String receiver=modeUtil.getWorkCode(recordSet.getString("xm"));//接收人
				String ybje=total;//原币金额
				
				writeLog("[BillAction.getMainData]workflowid:"+workflowid+" djbh:"+djbh+" djrq:"+djrq+" deptid:"+deptid+" jkbxr:"+jkbxr+" fydeptid:"+fydeptid+" total:"+total+" receiver:"+receiver+" ybje:"+ybje);
				  
				maindata.put("djbh", djbh);
				maindata.put("djrq", djrq);
				maindata.put("deptid", deptid);
				maindata.put("jkbxr", jkbxr);
				maindata.put("fydeptid", fydeptid);
				maindata.put("total", total);
				maindata.put("receiver", receiver);
				maindata.put("ybje", ybje);
		  }
		  
	  }
	  
	  
	  return maindata;
  }
  
  
  /**
   * 获取明细表数据
   * @param maindata
   * @param info
   * @return
   */
  public List<Map<String,String>> getDetailData(List<Map<String,String>> detaildata,RequestInfo info){
	  
	  RecordSet recordSet=new RecordSet();
	  String formtable = info.getRequestManager().getBillTableName();
	  String requestid=info.getRequestid();
	  String workflowid=info.getWorkflowid();
	  
	  String sql="select t2.* from "+formtable+" t1,"+formtable+"_dt1 t2 where requestId="+requestid+" and t1.id=t2.mainid";
	  recordSet.execute(sql);
	  writeLog("[BillAction.getData]查询明细表的SQL:"+sql);
	  int rowno=1;
	  while(recordSet.next()){
		  
		  if(workflowid.equals("178")||workflowid.equals("179")||workflowid.equals("177")){
			  
			  
			  String defitem50=recordSet.getString("kmbm"); //预算编码
			  String je=recordSet.getString("je"); //金额
			  String defitem4=recordSet.getString("jjsx");//经济事项
			  String defitem49=getBudgetName(defitem50);//科目名称
			  
			  writeLog("[BillAction.getMainData]workflowid:"+workflowid+" defitem50:"+defitem50+" je:"+je+" defitem4:"+defitem4+" defitem49:"+defitem49);
			  
			  Map<String,String> detailMap=new HashMap<String, String>();
			  detailMap.put("defitem4", defitem4);//说明
			  detailMap.put("szxmid", "ZX9904");
			  detailMap.put("defitem50", defitem50);//科目编号
			  detailMap.put("defitem49", defitem49);//科目名称
			  detailMap.put("defitem6", je);//金额
			  detailMap.put("defitem7", "");
			  detailMap.put("amount", je);//金额
			  detailMap.put("ybje", je);//金额
			  detailMap.put("bbje", je);//金额
			  detailMap.put("rowno", rowno+"");//明细下标
			  
			  detaildata.add(detailMap);
			  
		  }else if(workflowid.equals("176")){
			  
			  
			  String defitem50=getBudgetCode(recordSet.getString("fylx")); //预算编码
			  String je=recordSet.getString("je"); //金额
			  String defitem4=recordSet.getString("jjsx");//经济事项
			  String defitem49=getBudgetName(defitem50);//科目名称
			  
			  writeLog("[BillAction.getMainData]workflowid:"+workflowid+" defitem50:"+defitem50+" je:"+je+" defitem4:"+defitem4+" defitem49:"+defitem49);
			  
			  Map<String,String> detailMap=new HashMap<String, String>();
			  detailMap.put("defitem4", defitem4);//说明
			  detailMap.put("szxmid", "ZX9904");
			  detailMap.put("defitem50", defitem50);//科目编号
			  detailMap.put("defitem49", defitem49);//科目名称
			  detailMap.put("defitem6", je);//金额
			  detailMap.put("defitem7", "");
			  detailMap.put("amount", je);//金额
			  detailMap.put("ybje", je);//金额
			  detailMap.put("bbje", je);//金额
			  detailMap.put("rowno", rowno+"");//明细下标
			  
			  detaildata.add(detailMap);
			  
		  }
		  
		  rowno++;
		  
	  }
	  
	  
	  return detaildata;
  }
  
  
  /**
   * 劳务报销
   * @param info
   * @return
   */
  public boolean lwbx(RequestInfo info){
	  
	  writeLog("[BillAction.lwbx] 劳务费用报销开始....");
	  
	  String formtable = info.getRequestManager().getBillTableName();
	  String requestid=info.getRequestid();
		
	  ModeUtil modeUtil=new ModeUtil();
		
	  Map<String, String> maindata=new HashMap<String, String>();
	  List<Map<String,String>> detaildata=new ArrayList<Map<String,String>>();
		
	  RecordSet recordSet=new RecordSet();
	  
	  String sql="select * from "+formtable+" where requestid="+requestid;
	  recordSet.execute(sql);
	  writeLog("[BillAction.lwbx]查询主表的SQL:"+sql);
	  while(recordSet.next()){
		  
		  String djbh=recordSet.getString("lcbh");//单据编号
		  String deptid=modeUtil.getDeptCode(recordSet.getString("sqbm"));//申请部门编号
		  String jkbxr=modeUtil.getDeptName(recordSet.getString("cdbm1"));//借款报销人
		  String fydeptid=modeUtil.getDeptCode(recordSet.getString("cdbm1"));;//费用承担部门
		  String total=recordSet.getString("hj");//总计
		  String receiver=modeUtil.getWorkCode(recordSet.getString("xm"));//接收人
		  String ybje=recordSet.getString("hj");//预报金额
		  
		  
		  maindata.put("pk_group", "CSAID");
		  maindata.put("pk_org", "E00");
		  maindata.put("pk_fiorg", "E00");
		  maindata.put("pk_payorg", "E00");
		  
		  maindata.put("djlxbm", "264X-Cxx-21");
		  
		  maindata.put("qcbz", "N");
		  maindata.put("qzzt", "0");
		  maindata.put("payflag", "1");
		  maindata.put("paytarget", "0");
		  maindata.put("jobid", "");
		  maindata.put("fjzs", "3");
		  maindata.put("djbh", djbh);
		  maindata.put("djrq", TimeUtil.getCurrentTimeString());
		  maindata.put("deptid", deptid);
		  maindata.put("jkbxr", receiver);
		  maindata.put("fydeptid", fydeptid);
		  maindata.put("total", total);
		  maindata.put("receiver", receiver);
		  maindata.put("fydwbm", "E00");
		  maindata.put("dwbm", "E00");
		  maindata.put("bzbm", "CNY");
		  maindata.put("djdl", "bx");
		  maindata.put("djzt", "1");
		  maindata.put("operator", "E0000042");
		  maindata.put("ybje", ybje);
		  
	  }
	  

	  sql="select t2.* from "+formtable+" t1,"+formtable+"_dt1 t2 where requestId="+requestid+" and t1.id=t2.mainid";
	  recordSet.execute(sql);
	  writeLog("[BillAction.lwbx]查询明细表的SQL:"+sql);
	  while(recordSet.next()){
		  String defitem5=recordSet.getString("kmbm"); //预算编码
		  String je=modeUtil.getDeptCode(recordSet.getString("je")); //部门编号
		  String defitem4=recordSet.getString("jjsx");//经济事项
			
		  Map<String,String> detailMap=new HashMap<String, String>();
		  detailMap.put("defitem4", defitem4);
		  detailMap.put("defitem5", defitem5);
		  detailMap.put("defitem6", je);
		  detailMap.put("defitem7", je);
		  detaildata.add(detailMap);
	  }
		
	  Map<String, String> resultMap=NCUtil.doPost("264X", maindata, detaildata, "");
	  //NCUtil.doPost("264X", maindata, detaildata, "");
	  String successful=resultMap.get("successful");
	  writeLog("[BillAction.lwbx] 劳务费用报销结束....");
	  
	  return successful.equals("Y")?true:false;
  }
  
  
  /**
   * 劳务(交通费，通讯费)报销
   * @param info
   * @return
   */
  public boolean lwbxJtTx(RequestInfo info){
	  
	  writeLog("[BillAction.lwbxJtTx] 劳务费用（交通费，通讯费）报销开始....");
	  
	  String formtable = info.getRequestManager().getBillTableName();
	  String requestid=info.getRequestid();
		
	  ModeUtil modeUtil=new ModeUtil();
		
	  Map<String, String> maindata=new HashMap<String, String>();
	  List<Map<String,String>> detaildata=new ArrayList<Map<String,String>>();
		
	  RecordSet recordSet=new RecordSet();
	  
	  String sql="select * from "+formtable+" where requestid="+requestid;
	  recordSet.execute(sql);
	  writeLog("[BillAction.lwbxJtTx]查询主表的SQL:"+sql);
	  while(recordSet.next()){
		  
		  String djbh=recordSet.getString("lcbh");//单据编号
		  String deptid=modeUtil.getDeptCode(recordSet.getString("sqbm"));//申请部门编号
		  String jkbxr=modeUtil.getDeptName(recordSet.getString("sqbm"));//借款报销人
		  String fydeptid=modeUtil.getDeptCode(recordSet.getString("sqbm"));;//费用承担部门
		  String total=recordSet.getString("hj");//总计
		  String receiver=modeUtil.getWorkCode(recordSet.getString("xm"));//接收人
		  String ybje=recordSet.getString("hj");//预报金额
		  
		  
		  maindata.put("pk_group", "CSAID");
		  maindata.put("pk_org", "E00");
		  maindata.put("pk_fiorg", "E00");
		  maindata.put("pk_payorg", "E00");
		  
		  maindata.put("djlxbm", "264X-Cxx-22");
		  
		  maindata.put("qcbz", "N");
		  maindata.put("qzzt", "0");
		  maindata.put("payflag", "1");
		  maindata.put("paytarget", "0");
		  maindata.put("jobid", "");
		  maindata.put("fjzs", "3");
		  maindata.put("djbh", djbh);
		  maindata.put("djrq", TimeUtil.getCurrentTimeString());
		  maindata.put("deptid", deptid);
		  maindata.put("jkbxr", receiver);
		  maindata.put("fydeptid", fydeptid);
		  maindata.put("total", total);
		  maindata.put("receiver", receiver);
		  maindata.put("fydwbm", "E00");
		  maindata.put("dwbm", "E00");
		  maindata.put("bzbm", "CNY");
		  maindata.put("djdl", "bx");
		  maindata.put("djzt", "1");
		  maindata.put("operator", "E0000042");
		  maindata.put("ybje", ybje);
		  
	  }
	  

	  sql="select t2.* from "+formtable+" t1,"+formtable+"_dt1 t2 where requestId="+requestid+" and t1.id=t2.mainid";
	  recordSet.execute(sql);
	  writeLog("[BillAction.lwbxJtTx]查询明细表的SQL:"+sql);
	  while(recordSet.next()){
		  String defitem5=recordSet.getString("kmbm"); //预算编码
		  String je=modeUtil.getDeptCode(recordSet.getString("je")); //部门编号
		  String defitem4=recordSet.getString("jjsx");//经济事项
			
		  Map<String,String> detailMap=new HashMap<String, String>();
		  detailMap.put("defitem4", defitem4);
		  detailMap.put("defitem5", defitem5);
		  detailMap.put("defitem6", je);
		  detailMap.put("defitem7", je);
		  detaildata.add(detailMap);
	  }
		
	  Map<String, String> resultMap=NCUtil.doPost("264X", maindata, detaildata, "");
	  //NCUtil.doPost("264X", maindata, detaildata, "");
	  String successful=resultMap.get("successful");
	  writeLog("[BillAction.lwbxJtTx] 劳务费用（交通费，通讯费）报销结束....");
	  
	  return successful.equals("Y")?true:false;
  }
  
  
  /**
   * 管理费用报销
   * @param info
   * @return
   */
  public boolean glfybx(RequestInfo info){
	  
	  writeLog("[BillAction.glfybx] 管理费用报销开始....");
			
	  String formtable = info.getRequestManager().getBillTableName();
	  String requestid=info.getRequestid();
			
	  ModeUtil modeUtil=new ModeUtil();
			
	  Map<String, String> maindata=new HashMap<String, String>();
	  List<Map<String,String>> detaildata=new ArrayList<Map<String,String>>();
			
	  RecordSet recordSet=new RecordSet();
			
	  String sql="select * from "+formtable+" where requestid="+requestid;
	  recordSet.execute(sql);
	  writeLog("[BillAction.glfybx]费用报销的SQL:"+sql);
	  while(recordSet.next()){
		  
			String djbh=recordSet.getString("lcbh"); //单据编号
			String djrq=TimeUtil.getFormartString(new Date(), "yyyy/MM/dd HH:mm:ss");; //单据日期
			String deptid=modeUtil.getDeptCode(recordSet.getString("sqbm")); //部门编号
			String jkbxr=modeUtil.getWorkCode(recordSet.getString("xm")); //借款报销人
			String fydeptid=modeUtil.getDeptCode(recordSet.getString("cdbm1")); //费用承担部门
			String total=recordSet.getString("hj"); //合计
			String receiver=modeUtil.getWorkCode(recordSet.getString("xm"));//接收人
			//String operator=modeUtil.getWorkCode(recordSet.getString("xm")); //填报人编号，类似制单人
			String ybje=total;//原币金额
				
			maindata.put("djbh", djbh);
			maindata.put("djrq", djrq);
			maindata.put("deptid", deptid);
			maindata.put("jkbxr", receiver);
			maindata.put("fydeptid", fydeptid);
			maindata.put("total", total);
			maindata.put("receiver", receiver);
			maindata.put("operator", "E0000042");
			maindata.put("ybje", ybje);
				
			maindata.put("djlxbm", "264X-Cxx-23");
				
			maindata.put("pk_fiorg", "E00");
			maindata.put("pk_payorg", "E00");
			maindata.put("qcbz", "N");
			maindata.put("qzzt", "0");
			maindata.put("payflag", "1");
			maindata.put("paytarget", "0");
			maindata.put("jobid", "");
			maindata.put("fjzs", "3");
			
			maindata.put("fydwbm", "E00");
			maindata.put("dwbm", "E00");
			maindata.put("bzbm", "CNY");
			maindata.put("djdl", "bx");
			maindata.put("djzt", "1");	
				
			maindata.put("pk_group", "CSAID");
			maindata.put("pk_org", "E00");
				
		}
			
		sql="select t2.* from "+formtable+" t1,"+formtable+"_dt1 t2 where requestId="+requestid+" and t1.id=t2.mainid";
		recordSet.execute(sql);
		writeLog("sql2:"+sql);
		while(recordSet.next()){
			String defitem5=recordSet.getString("kmbm"); //预算编码
			String je=modeUtil.getDeptCode(recordSet.getString("je")); //部门编号
			String defitem4=recordSet.getString("jjsx");//经济事项
				
			Map<String,String> detailMap=new HashMap<String, String>();
			detailMap.put("defitem4", defitem4);
			detailMap.put("defitem5", defitem5);
			detailMap.put("defitem6", je);
			detailMap.put("defitem7", je);
			detaildata.add(detailMap);
		}
			
		Map<String, String> resultMap=NCUtil.doPost("264X", maindata, detaildata, "");
			
		String successful=resultMap.get("successful");
		//String bdocid=resultMap.get("bdocid");
		//String resultcode=resultMap.get("resultcode");
		//String resultdescription=resultMap.get("resultdescription");
			
		//writeLog("[BillAction.glfybx] successful:"+successful+" bdocid:"+bdocid+" resultcode:"+resultcode+" resultdescription:"+resultdescription);
		
		writeLog("[BillAction.glfybx] 管理费用报销结束....");
		
		return successful.equals("Y")?true:false;
  }
  
  /**
   * 销售费用报销
   * @param info
   * @return
   */
  public boolean xsbx(RequestInfo info){
	  
	  writeLog("[BillAction.xsbx] 销售费用报销开始....");
	  
	  String formtable = info.getRequestManager().getBillTableName();
	  String requestid=info.getRequestid();
		
	  ModeUtil modeUtil=new ModeUtil();
		
	  Map<String, String> maindata=new HashMap<String, String>();
	  List<Map<String,String>> detaildata=new ArrayList<Map<String,String>>();
		
	  RecordSet recordSet=new RecordSet();
	  
	  String sql="select * from "+formtable+" where requestid="+requestid;
	  recordSet.execute(sql);
	  writeLog("[BillAction.xsbx]查询主表的SQL:"+sql);
	  while(recordSet.next()){
		  
		  String djbh=recordSet.getString("lcbh");//单据编号
		  String deptid=modeUtil.getDeptCode(recordSet.getString("sqbm"));//申请部门编号
		  String jkbxr=modeUtil.getDeptName(recordSet.getString("sqbm"));//借款报销人
		  String fydeptid=modeUtil.getDeptCode(recordSet.getString("sqbm"));;//费用承担部门
		  String total=recordSet.getString("hj");//总计
		  String receiver=modeUtil.getWorkCode(recordSet.getString("xm"));//接收人
		  String ybje=recordSet.getString("hj");//预报金额
		  
		  
		  maindata.put("pk_group", "CSAID");
		  maindata.put("pk_org", "E00");
		  maindata.put("pk_fiorg", "E00");
		  maindata.put("pk_payorg", "E00");
		  
		  maindata.put("djlxbm", "264X-Cxx-24");
		  
		  maindata.put("qcbz", "N");
		  maindata.put("qzzt", "0");
		  maindata.put("payflag", "1");
		  maindata.put("paytarget", "0");
		  maindata.put("jobid", "");
		  maindata.put("fjzs", "3");
		  maindata.put("djbh", djbh);
		  maindata.put("djrq", TimeUtil.getCurrentTimeString());
		  maindata.put("deptid", deptid);
		  maindata.put("jkbxr", receiver);
		  maindata.put("fydeptid", fydeptid);
		  maindata.put("total", total);
		  maindata.put("receiver", receiver);
		  maindata.put("fydwbm", "E00");
		  maindata.put("dwbm", "E00");
		  maindata.put("bzbm", "CNY");
		  maindata.put("djdl", "bx");
		  maindata.put("djzt", "1");
		  maindata.put("operator", "E0000042");
		  maindata.put("ybje", ybje);
		  
	  }
	  

	  sql="select t2.* from "+formtable+" t1,"+formtable+"_dt1 t2 where requestId="+requestid+" and t1.id=t2.mainid";
	  recordSet.execute(sql);
	  writeLog("[BillAction.xsbx]查询明细表的SQL:"+sql);
	  while(recordSet.next()){
		  String defitem5=recordSet.getString("kmbm"); //预算编码
		  String je=modeUtil.getDeptCode(recordSet.getString("je")); //部门编号
		  String defitem4=recordSet.getString("jjsx");//经济事项
			
		  Map<String,String> detailMap=new HashMap<String, String>();
		  detailMap.put("defitem4", defitem4);
		  detailMap.put("defitem5", defitem5);
		  detailMap.put("defitem6", je);
		  detailMap.put("defitem7", je);
		  detaildata.add(detailMap);
	  }
		
	  Map<String, String> resultMap=NCUtil.doPost("264X", maindata, detaildata, "");
	  //NCUtil.doPost("264X", maindata, detaildata, "");
	  String successful=resultMap.get("successful");
	  writeLog("[BillAction.xsbx] 销售费用报销结束....");
	  
	  return successful.equals("Y")?true:false;
  }
  
  public String getBudgetCode(String fylx){
	  
	  String code="";
	  RecordSet recordSet=new RecordSet();
	  String sql="select * from fnabudgetfeetype where id="+fylx;
	  recordSet.execute(sql);
	  if(recordSet.next()){
		  code=recordSet.getString("codename");
	  }
	  return code;
  }
  
  public String getBudgetName(String codename){
	  
	  String name="";
	  RecordSet recordSet=new RecordSet();
	  String sql="select * from fnabudgetfeetype where codename='"+codename+"'";
	  recordSet.execute(sql);
	  if(recordSet.next()){
		  name=recordSet.getString("name");
	  }
	  return name;
	  
  }

}
