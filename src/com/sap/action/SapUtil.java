package com.sap.action;

import java.util.Iterator;

import net.sf.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.TimeUtil;

public class SapUtil {

	/**
	 * 根据workcode获取人员ID
	 * @param workcode 工号
	 * @return
	 */
	public static String getUseridByCode(String workcode){
		
		String userid="-1";
		/*
		RecordSet recordSet=new RecordSet();
		String sql="select id from hrmresource where workcode='"+workcode;
		recordSet.execute(sql);
		if(recordSet.next()){
			userid=recordSet.getString("id");
		}
		*/
		userid="1";
		return userid;
	}
	
	/**
	 * 获取LOGID
	 * @return
	 */
	public static String getLogid(){
		
		String logid="-1";
		RecordSet recordSet=new RecordSet();
		String sql="select max(id) as maxid from SAP_OA_LOG ";
		recordSet.execute(sql);
		if(recordSet.next()){
			logid=(recordSet.getInt("maxid")+1)+"";
		}
		return logid;
	}
	
	/**
	 * 记录日志
	 * @param object
	 * @return
	 */
	public static boolean addLog(JSONObject object){
		
		
		
		String LOGID_O=object.getString("logid_o");    
		
		System.out.println("LOGID_O:"+LOGID_O);
		
		RecordSet recordSet=new RecordSet();
		
		String sql="insert into SAP_OA_LOG(id) values("+LOGID_O+")";
		
		return recordSet.execute(sql);
	}
	
	public static JSONObject transObject(JSONObject o1){
        JSONObject o2=new JSONObject();
         Iterator it = o1.keys();
            while (it.hasNext()) {
                String key = (String) it.next();
                Object object = o1.get(key);
                if(object.getClass().toString().endsWith("String")){
                    o2.accumulate(key.toLowerCase(), object);
                }
            }
            return o2;
    }
	
	
	/**
	 * 根据标记获取流程ID
	 * @param flag
	 * @return
	 */
	public static String getWorkflowidByFlag(String flag){
		String workflowid="-1";
		if(flag.equals("SAP_OA_001")){ //采购申请
			workflowid="49";
		}else if(flag.equals("SAP_OA_003")){ //采购订单
			workflowid="50";
		}else if(flag.equals("SAP_OA_005")){
			workflowid="54";
		}else if(flag.equals("SAP_OA_007")){
			workflowid="49";
		}else if(flag.equals("SAP_OA_009")){
			workflowid="49";
		}
		return workflowid;
	}
	
	/**
	 * 根据标记获取流程ID
	 * @param flag
	 * @return
	 */
	public static String getReqestNameByFlag(String flag){
		String reqestName="";
		if(flag.equals("SAP_OA_001")){ //采购申请
			reqestName="采购申请审批";
		}else if(flag.equals("SAP_OA_003")){ //采购订单
			reqestName="采购订单审批";
		}else if(flag.equals("SAP_OA_005")){
			reqestName="预付款审批";
		}else if(flag.equals("SAP_OA_007")){
			reqestName="49";
		}else if(flag.equals("SAP_OA_009")){
			reqestName="49";
		}
		reqestName+=TimeUtil.getCurrentDateString();
		return reqestName;
	}
}
