package com.lw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.TimeUtil;
import weaver.general.Util;
import weaver.workflow.webservices.WorkflowBaseInfo;
import weaver.workflow.webservices.WorkflowDetailTableInfo;
import weaver.workflow.webservices.WorkflowMainTableInfo;
import weaver.workflow.webservices.WorkflowRequestInfo;
import weaver.workflow.webservices.WorkflowRequestTableField;
import weaver.workflow.webservices.WorkflowRequestTableRecord;
import weaver.workflow.webservices.WorkflowServiceImpl;

/**
 * 用代码触发流程
 * @author GodWei
 *
 */
public class WorkflowManager extends BaseBean {
	
	
	/**
	 * 创建流程主方法
	 * @param list     需要触发流程的数据链表
	 * @param wfid	   需要触发哪个流程
	 */
	public void createWorkflow(List<Map<String, String>> list,String wfid){
		
		try {

				for(int i=0;i<list.size();i++){
					Map<String, String> paraMap=list.get(i);
					String NUM="";
					if(wfid.equals("203")){
						NUM = paraMap.get("PSHNUM");
					}else if(wfid.equals("204")){
						NUM = paraMap.get("POHNUM");
					}else if(wfid.equals("205")){
						NUM = paraMap.get("NUM");
					}
					
					String loginid=paraMap.get("LOGIN");
					String creatorid=getUserId(loginid);
					paraMap.put("REQUSR", creatorid);
					
					String creatorName=getUserName(loginid);
					String wfName=getWorkflowName(wfid);
					
					String mainFields=JSONObject.fromObject(paraMap).toString();
					
					writeLog("[WorkflowManager.createWorkflow] "+wfName+"-"+creatorName+" NUM:"+NUM);
					
					Map<String, String> map=doCreateWorkflowRequest(mainFields, "", wfid, creatorid, 
							wfName+"-"+creatorName+"-"+TimeUtil.getCurrentDateString(), 0);//触发流程
					if(map.get("status").equals("000")){
						addNote(NUM, wfid);//流程触发成功后，将对应的编号添加到记录中
					}
					
				}
				
		} catch (Exception e) {
			e.printStackTrace();
			writeLog("[WorkflowManager.createWorkflow] error:"+e.getMessage());
		}
		
	}

	
	/**
	 * 创建流程执行方法
	 * @param mainFields       主表字段
	 * @param detailFields     明细表字段
	 * @param workflowid       流程id
	 * @param creatorid        创建者id
	 * @param requestname      请求名称
	 * @param requestid		   请求id
	 * @return
	 */
	public Map<String, String> doCreateWorkflowRequest(String mainFields,String detailFields,String workflowid,String creatorid,String requestname,int requestid){
		
		String status="000";
		String msg="流程触发成功";
		
		writeLog("==============create workflow start================");
		
		writeLog("mainFields:"+mainFields);
		
		
		//请求日志获取流程ID与创建人
		WorkflowBaseInfo workflowBaseInfo=new WorkflowBaseInfo();
		workflowBaseInfo.setWorkflowId(workflowid); //流程ID
		
		//流程主表
		writeLog("==============IT_PR_HEAD start================");
		
		//构建主表数据
		//JSONObject headObj=JSONArray.fromObject(mainFields).getJSONObject(0); 
		JSONObject headObj=JSONObject.fromObject(mainFields);
		Object[] headObjKeys=headObj.keySet().toArray(); 
		
		WorkflowMainTableInfo workflowMainTableInfo=new WorkflowMainTableInfo();
		WorkflowRequestTableRecord[] workflowRequestTableRecords=new WorkflowRequestTableRecord[1];
		workflowRequestTableRecords[0]=new WorkflowRequestTableRecord();
		WorkflowRequestTableField[] workflowRequestTableFields=new WorkflowRequestTableField[headObjKeys.length]; //根据字段数判断需要创建的字段数组长度
		
		for(int i=0;i<headObjKeys.length;i++){
			
			String key=((String)headObjKeys[i]); //字段名称
			String value=headObj.getString(key); //字段值
			
			WorkflowRequestTableField workflowRequestTableField=new WorkflowRequestTableField();
			workflowRequestTableField.setFieldName(key);
			workflowRequestTableField.setFieldValue(value);
			workflowRequestTableField.setView(true);
			workflowRequestTableField.setEdit(true);
			workflowRequestTableFields[i]=workflowRequestTableField;
			
			writeLog("key:"+key+" value:"+value);
		}
		
		//将主表信息字段放入workflowRequestTableFields中
		workflowRequestTableRecords[0].setWorkflowRequestTableFields(workflowRequestTableFields);
		//将workflowRequestTableRecords放入主表对象中
		workflowMainTableInfo.setRequestRecords(workflowRequestTableRecords);
		
		writeLog("==============IT_PR_HEAD end======================");
		
		//流程明细
		writeLog("==============IT_PR_DETAIL start================");
		
		//JSONArray detailArrs=JSONArray.fromObject(IT_PR_DETAIL);
		
		//WorkflowDetailTableInfo[] workflowDetailTableInfos=new WorkflowDetailTableInfo[detailArrs.size()]; //明细表对象，存在多个明细表
		
		List<String> detailList=new ArrayList<String>();
		if(!detailFields.equals("")){
			detailList.add(detailFields);
		}
		
		WorkflowDetailTableInfo[] workflowDetailTableInfos=new WorkflowDetailTableInfo[detailList.size()];
		//明细表数据
		for(int i=0;i<detailList.size();i++){
			
			//JSONArray detailArrRows=detailArrs.getJSONArray(i);
			JSONArray detailArrRows=JSONArray.fromObject(detailList.get(i));
			
			WorkflowDetailTableInfo workflowDetailTableInfo=new WorkflowDetailTableInfo();
			WorkflowRequestTableRecord[] workflowRequestTableRecords2=new WorkflowRequestTableRecord[detailArrRows.size()]; //行对象，存在多行
			
			//行数据
			for(int k=0;k<detailArrRows.size();k++){
				
				JSONObject detailObj=detailArrRows.getJSONObject(k);
				Object[] detailObjKeys=detailObj.keySet().toArray();
				
				workflowRequestTableRecords2[k]=new WorkflowRequestTableRecord();
				WorkflowRequestTableField[] workflowRequestTableFields2=new WorkflowRequestTableField[detailObjKeys.length]; //列对象，存在多列
				
				//列数据
				for(int j=0;j<detailObjKeys.length;j++){
					
					String key=(String)detailObjKeys[j]; //字段名称
					String value=detailObj.getString(key); //字段值
					
					if(key.equalsIgnoreCase("afnam")||key.equalsIgnoreCase("ektel")){
						value=getUseridByCode(value);
					}
					
					WorkflowRequestTableField workflowRequestTableField2=new WorkflowRequestTableField();
					workflowRequestTableField2.setFieldName(key);
					workflowRequestTableField2.setFieldValue(value);
					workflowRequestTableField2.setView(true);
					workflowRequestTableField2.setEdit(true);
					workflowRequestTableFields2[j]=workflowRequestTableField2;
					
					writeLog("key:"+key+" value:"+value);
				}
				//将明细表信息字段放入workflowRequestTableRecords2中
				workflowRequestTableRecords2[k].setWorkflowRequestTableFields(workflowRequestTableFields2);
			}
			
			//将workflowRequestTableRecords放入明细表对象中
			workflowDetailTableInfo.setWorkflowRequestTableRecords(workflowRequestTableRecords2);
			workflowDetailTableInfos[i]=workflowDetailTableInfo;
		}
		writeLog("==============IT_PR_HEAD end================");
		
		if(!creatorid.equals("-1")){
			
			//将参数放入 WorkflowRequestInfo 对象
			WorkflowRequestInfo workflowRequestInfo=new WorkflowRequestInfo();
			workflowRequestInfo.setCreatorId(creatorid); //创建人
			workflowRequestInfo.setRequestName(requestname); //流程标题
			workflowRequestInfo.setWorkflowBaseInfo(workflowBaseInfo); //流程信息
			workflowRequestInfo.setWorkflowMainTableInfo(workflowMainTableInfo); //流程主表信息
			workflowRequestInfo.setWorkflowDetailTableInfos(null); //流程明细表信息
			
			writeLog("[WorkflowManager.doCreateWorkflowRequest] creatorid:"+creatorid);
			
			//创建流程
			WorkflowServiceImpl workflowServiceImpl = new WorkflowServiceImpl();
			if(requestid>0){
				//重新提交流程
				workflowServiceImpl.submitWorkflowRequest(workflowRequestInfo,requestid,Util.getIntValue(creatorid),"submit","");
			}else{
				//创建流程
				requestid=Util.getIntValue(workflowServiceImpl.doCreateWorkflowRequest(workflowRequestInfo,Util.getIntValue(creatorid)));
			}
			
			
			if(requestid<0){
				status="002";
				msg="流程创建失败，请查看OA日志 error:"+requestid;
			}
			
		}else{
			status="001";
			msg="OA中不存在";
		}
		
		Map<String, String> resultMap=new HashMap<String, String>();
		resultMap.put("status", status);
		resultMap.put("msg", msg);
		
		writeLog("creatorid:"+creatorid+" workflowid:"+workflowid+" requestid:"+requestid);
		writeLog("status:"+status+" msg:"+msg);
		
		writeLog("==============create workflow end================");
	    
		return resultMap;
	}
	
	
	/**
	 * 将已触发的流程记录在案
	 * @param map
	 * @param wfid
	 */
	public void addNote(String noteNum,String wfid){
		RecordSet rs=new RecordSet();
		String sql="";
		if(wfid.equals("203")){
			sql="select * from RequestNote where PSHNUM='"+noteNum+"'";
		}else if(wfid.equals("204")){
			sql="select * from OrderNote where POHNUM='"+noteNum+"'";
		}else if(wfid.equals("205")){
			sql="select * from PayNote where PAYNUM='"+noteNum+"'";
		}
		rs.execute(sql);
		if(rs.next()){//若流程以前触发过，则更改状态即可
			if(wfid.equals("203")){
				sql="update RequestNote set status='1' where PSHNUM='"+noteNum+"'";
			}else if(wfid.equals("204")){
				sql="update OrderNote set status='1' where POHNUM='"+noteNum+"'";
			}else if(wfid.equals("205")){
				sql="update PayNote set status='1' where PAYNUM='"+noteNum+"'";
			}
		}else{//若流程没有触发过，则新插入一条记录
			if(wfid.equals("203")){
				sql="insert into RequestNote(PSHNUM,status) values('"+noteNum+"','1')";
			}else if(wfid.equals("204")){
				sql="insert into OrderNote(POHNUM,status) values('"+noteNum+"','1')";
			}else if(wfid.equals("205")){
				sql="insert into PayNote(PAYNUM,status) values('"+noteNum+"','1')";
			}
		}
		writeLog("[WorkflowManager.addNote] 添加记录的SQL:"+sql);
		rs.execute(sql);
	}
	
	public String checkNote(String str,String wfid){
		RecordSet rs=new RecordSet();
		String sql="";
		
		if(wfid.equals("203")){
			sql="select * from RequestNote where PSHNUM='"+str+"'";
		}else if(wfid.equals("204")){
			sql="select * from OrderNote where POHNUM='"+str+"'";
		}else if(wfid.equals("205")){
			sql="select * from PayNote where PAYNUM='"+str+"'";
		}
		
		rs.execute(sql);
		
		return null;
	}
	
	/**
	 * 判断该流程是否被触发过 返回0是未触发过，返回1是已触发过
	 * @param str
	 * @param wfid
	 * @return
	 */
	public String checkWorkflow(String str,String wfid){
		String is="0";
		
		RecordSet rs=new RecordSet();
		String sql="";
		if(wfid.equals("203")){
			sql="select * from RequestNote where PSHNUM='"+str+"' and status='1'";
		}else if(wfid.equals("204")){
			sql="select * from OrderNote where POHNUM='"+str+"' and status='1'";
		}else if(wfid.equals("205")){
			sql="select * from PayNote where PAYNUM='"+str+"' and status='1'";
		}
		
		rs.execute(sql);
		if(rs.next()){
			is="1";
		}
		
		return is;
	}
	
	
	/**
	 * 通过流程id获取流程名
	 * @param wfid
	 * @return
	 */
	public String getWorkflowName(String wfid){
		String wfName="";
		RecordSet rs=new RecordSet();
		String sql="select * from workflow_base where id='"+wfid+"'";
		rs.execute(sql);
		if(rs.next()){
			wfName=rs.getString("workflowname");
		}
		return wfName;
	}
	
	/**
	 * 通过登录名获取姓名
	 * @param loginid
	 * @return
	 */
	public String getUserName(String loginid){
		String id="0";
		RecordSet rs=new RecordSet();
		String sql="select * from hrmResource where loginid='"+loginid+"'";
		rs.execute(sql);
		if(rs.next()){
			id=rs.getString("lastname");
		}
		return id;
	}
	
	/**
	 * 通过登录名获取id
	 * @param loginid
	 * @return
	 */
	public String getUserId(String loginid){
		String id="-1";
		RecordSet rs=new RecordSet();
		String sql="select * from hrmResource where loginid='"+loginid+"'";
		rs.execute(sql);
		if(rs.next()){
			id=rs.getString("id");
		}
		return id;
	}
	
	/**
	 * 根据workcode获取人员ID
	 * @param workcode 工号
	 * @return
	 */
	public static String getUseridByCode(String workcode){
		
		String userid="-1";
		
		RecordSet recordSet=new RecordSet();
		String sql="select id from hrmresource where workcode='"+workcode+"'";
		recordSet.execute(sql);
		if(recordSet.next()){
			userid=recordSet.getString("id");
		}
		
		System.out.println("[SapUtil.getUseridByCode] sql:"+sql+" userid:"+userid);
		
		return userid;
	}
}
