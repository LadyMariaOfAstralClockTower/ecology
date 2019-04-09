package com.lw;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;

import com.lw.webservice.CAdxCallContext;
import com.lw.webservice.CAdxParamKeyValue;
import com.lw.webservice.CAdxResultXml;
import com.lw.webservice.CAdxWebServiceXmlCCProxy;

/**
 * OA同步人员和部门到ERP
 * @author GodWei
 *
 */
public class SysHrmDpt extends BaseBean{
	
	private static String xmlHeader="<?xml version='1.0' encoding='UTF-8'?>";//xml文件头
	
	
	public static void main(String[] args) {
		
		CAdxWebServiceXmlCCProxy proxy=new CAdxWebServiceXmlCCProxy();
		
		CAdxCallContext callContext=new CAdxCallContext();
		callContext.setPoolAlias("WSNEWAY");
		callContext.setCodeUser("admin");
		callContext.setPassword("");
		callContext.setCodeLang("CHI");
		callContext.setRequestConfig("adxwss.optreturn=json");
		
		System.out.println(":::"+checkHrm(proxy, callContext, "OA001"));
	}
	
	
	/**
	 * 主同步方法 同步人员
	 */
	public void doSync(){
		
		List<Map<String, String>> hrmList=new ArrayList<Map<String,String>>();

		RecordSet rs=new RecordSet();		
		
		String sql="select * from hrmResource";
		rs.execute(sql);
		while(rs.next()){
			Map<String, String> map=new HashMap<String, String>();
			if(rs.getString("loginid").equals("")){
				continue;
			}
			map.put("loginid", rs.getString("loginid"));
			map.put("dept", getDeptName(rs.getString("departmentid")));
			map.put("lastname",rs.getString("lastname"));
			map.put("erpgsbm",getERPSubCompanyCode(rs.getString("subcompanyid1")));
			map.put("status", rs.getString("status"));
			hrmList.add(map);
		}
		
		writeLog("***************************** 同步人员开始 ****************************");
		syncHrm(hrmList);
		writeLog("***************************** 同步人员结束 ****************************");
		
	}
	
	
	/**
	 * 同步人员
	 * @param loginid   用户名
	 * @param Dept      部门
	 * @param lastname  姓名
	 */
	public void syncHrm(List<Map<String, String>> list){
			
			CAdxWebServiceXmlCCProxy proxy=new CAdxWebServiceXmlCCProxy();
		
			CAdxCallContext callContext=new CAdxCallContext();
			callContext.setPoolAlias("WSNEWAY");
			callContext.setCodeUser("admin");
			callContext.setPassword("");
			callContext.setCodeLang("CHI");
			callContext.setRequestConfig("adxwss.optreturn=json");
			
			for(Map<String, String> map: list){
				
				String YENAFLG=checkHrm(proxy, callContext, map.get("loginid"));//检测是否已存在该人员
				
				if(YENAFLG.equals("2")||YENAFLG.equals("1")){//2为已激活，若已存在则不必再次插入，更新后进入下一次循环
					writeLog("[SysHrmDpt.syncHrm-同步人员]该人员已同步！执行更新操作！  用户名:"+map.get("loginid")+" 部门:"+map.get("dept")+" 姓名:"+map.get("lastname")+" 分部:"+map.get("erpgsbm")+" 状态:"+map.get("status"));
					editHrm(proxy, callContext, map);
					continue;
				}
				
				if(YENAFLG.equals("0")){//0为不存在，则插入
					insertHrm(proxy, callContext, map);
				}
			}
	}
	
	
	/**
	 * 同步部门
	 * @param deptCode 部门编码
	 * @param deptName 部门名
	 */
	public void syncDept(List<Map<String, String>> list){
			
			CAdxWebServiceXmlCCProxy proxy=new CAdxWebServiceXmlCCProxy();
			
			CAdxCallContext callContext=new CAdxCallContext();
			callContext.setPoolAlias("WSNEWAY");
			callContext.setCodeUser("admin");
			callContext.setPassword("");
			callContext.setCodeLang("CHI");
			callContext.setRequestConfig("adxwss.optreturn=json");
			
			for(Map<String, String> map: list){
				
				String YENAFLG=checkDept(proxy, callContext, map.get("deptCode"));
				if(YENAFLG.equals("2")){//2为已激活，若已存在则不必再次插入，直接进入下一次循环
					writeLog("[SysHrmDpt.syncDept-同步部门]该部门已同步！无需再次同步！ 部门编码:"+map.get("deptCode")+" 部门名称:"+map.get("deptName"));
					continue;
				}
				
				if(YENAFLG.equals("0")){//0为不存在，则插入
					insertDept(proxy, callContext, map);
				}
				
			}
		
	}
	
	
	/**
	 * 插入部门
	 * @param proxy
	 * @param callContext
	 * @param map
	 */
	public void insertDept(CAdxWebServiceXmlCCProxy proxy,CAdxCallContext callContext,Map<String, String> map){
		
		try {
		
			String xml=getDeptXml(map.get("deptCode"), map.get("deptName"));//生成对应的xml
			
			writeLog("[SysHrmDpt.insertDept-同步部门] 部门编码:"+map.get("deptCode")+" 部门名称:"+map.get("deptName"));
			
			CAdxResultXml result;
			
			result = proxy.save(callContext, "XOACCE", xml);
			
			if(result.getStatus()==1){
				writeLog("[SysHrmDpt.insertDept-同步部门] 同步成功！ result:"+result.getResultXml()+" status:"+result.getStatus());
			}else{
				writeLog("[SysHrmDpt.insertDept-同步部门] 部门编码:"+map.get("deptCode")+" 部门名称:"+map.get("deptName")+" -该部门同步不成功！");
				writeLog("[SysHrmDpt.insertDept-同步部门] 错误信息:"+result.getTechnicalInfos().getProcessReport());
			}
		
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//执行同步
		
	}
	
	/**
	 * 插入人员
	 * @param proxy
	 * @param callContext
	 * @param map
	 */
	public void insertHrm(CAdxWebServiceXmlCCProxy proxy,CAdxCallContext callContext,Map<String, String> map){
		
		try {
		
			String xml=getHrmXml(map.get("loginid"),map.get("dept"),map.get("lastname"),map.get("erpgsbm"),map.get("status"));//生成对应的xml
			
			writeLog("[SysHrmDpt.insertHrm-同步人员] 用户名:"+map.get("loginid")+" 部门:"+map.get("dept")+" 姓名:"+map.get("lastname"));
			
			CAdxResultXml result=proxy.save(callContext, "XOACCE", xml);//执行同步
			
			if(result.getStatus()==1){
				writeLog("[SysHrmDpt.insertHrm-同步人员]result:"+result.getResultXml()+" status:"+result.getStatus());
			}else{
				writeLog("[SysHrmDpt.insertHrm-同步人员] 用户名:"+map.get("loginid")+" 部门:"+map.get("dept")+" 姓名:"+map.get("lastname")+" -该人员同步不成功！");
				writeLog("[SysHrmDpt.insertHrm-同步部门] 错误信息:"+result.getTechnicalInfos().getProcessReport());
			}
		
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//执行同步
		
	}
	
	/**
	 * 调整人员信息
	 * @param proxy
	 * @param callContext
	 * @param map
	 */
	public void editHrm(CAdxWebServiceXmlCCProxy proxy,CAdxCallContext callContext,Map<String, String> map){
		
		try {
		
			String xml=getHrmXml(map.get("loginid"),map.get("dept"),map.get("lastname"),map.get("erpgsbm"),map.get("status"));//生成对应的xml
			
			CAdxParamKeyValue[] objKey=new CAdxParamKeyValue[2];
			
			objKey[0]=new CAdxParamKeyValue("DIE","EMP");
			objKey[1]=new CAdxParamKeyValue("CCE",map.get("loginid"));
			
			CAdxResultXml result=proxy.modify(callContext, "XOACCE", objKey, xml);//执行同步
			
			if(result.getStatus()==1){
				writeLog("[SysHrmDpt.editHrm-同步人员]result:"+result.getResultXml()+" status:"+result.getStatus());
			}else{
				writeLog("[SysHrmDpt.editHrm-同步人员] 用户名:"+map.get("loginid")+" 部门:"+map.get("dept")+" 姓名:"+map.get("lastname")+" -该人员更新失败！");
				writeLog("[SysHrmDpt.editHrm-同步部门] 错误信息:"+result.getTechnicalInfos().getProcessReport());
			}
		
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//执行同步
		
	}
	
	
	/**
	 * 获取ERP部分编码
	 * @param subcompanyid1
	 * @return
	 */
	public String getERPSubCompanyCode(String subcompanyid1){
		
		String ERPCode="";
		RecordSet rs=new RecordSet();
		String sql="select * from HrmSubcompanyDefined where subcomid="+subcompanyid1;
		rs.execute(sql);
		if(rs.next()){
			ERPCode=rs.getString("erpgsbm");
		}
		
		return ERPCode;
	}
	
	/**
	 * 生成人员的xml
	 * @param loginid   用户名
	 * @param Dept      部门
	 * @param lastname  姓名
	 * @return
	 */
	public String getHrmXml(String loginid,String Dept,String lastname,String erpgsbm,String status){
		
		String xml=xmlHeader;
		xml+="<PARAM>";
		xml+="<GRP ID='CCE0_1' >";
		xml+="<FLD NAME='DIE' TYPE='Char' >EMP</FLD>";
		xml+="<FLD NAME='CCE' TYPE='Char' >"+loginid+"</FLD>";
		xml+="<FLD NAME='YDPT' TYPE='Char' >"+Dept+"</FLD>";
		xml+="<FLD NAME='DESTRA' TYPE='Char' >"+lastname+"</FLD>'";
		xml+="</GRP>";
		xml+="<GRP ID='CCE1_2' >";
		if(status.equals("0")||status.equals("1")||status.equals("2")||status.equals("3")){
			xml+="<FLD NAME='ENAFLG' TYPE='Integer' >2</FLD>";
		}else if(status.equals("4")||status.equals("5")||status.equals("6")||status.equals("7")){
			xml+="<FLD NAME='ENAFLG' TYPE='Integer' >1</FLD>";
		}
		xml+="<FLD NAME='FCY' TYPE='Char' >"+erpgsbm+"</FLD>";
		xml+="</GRP>";
		xml+="</PARAM>";
		return xml;
	}

	
	/**
	 * 生成部门的xml
	 * @param deptCode 部门编码
	 * @param deptName 部门名
	 * @return
	 */
	public static String getDeptXml(String deptCode,String deptName){
		
		String xml=xmlHeader;
		xml+="<PARAM>";
		xml+="<GRP ID='CCE0_1' >";
		xml+="<FLD NAME='DIE' TYPE='Char' >DPT</FLD>";
		xml+="<FLD NAME='CCE' TYPE='Char' >"+deptCode+"</FLD>";
		xml+="<FLD NAME='DESTRA' TYPE='Char' >"+deptName+"</FLD>'";
		xml+="</GRP>";
		xml+="<GRP ID='CCE1_2' >";
		xml+="<FLD NAME='ENAFLG' TYPE='Integer' >2</FLD>";
		xml+="</GRP>";
		xml+="</PARAM>";
		
		return xml;
	}
	
	/**
	 * 检测人员是否存在 返回2为已激活
	 * @param proxy
	 * @param callContext
	 * @param loginid
	 * @return
	 */
	public static String checkHrm(CAdxWebServiceXmlCCProxy proxy,CAdxCallContext callContext,String loginid){
		String YENAFLG="";
		try {
			
			String xml="";
			xml+="<PARAM>";
			xml+="<FLD NAME='XDIE' TYPE='Char'>EMP</FLD>";
			xml+="<FLD NAME='XCCE' TYPE='Char'>"+loginid+"</FLD>";
			xml+="</PARAM>";
			
			CAdxResultXml result=proxy.run(callContext, "XOACCE_RED", xml);
			if(result.getResultXml()!=null){
				JSONObject json=JSONObject.fromObject(result.getResultXml());
				YENAFLG=json.getJSONObject("GRP1").get("YENAFLG")+"";
			}
	
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return YENAFLG;
	}
	
	/**
	 * 检测部门是否存在 返回2为已激活
	 * @param proxy
	 * @param callContext
	 * @param deptCode
	 * @return
	 */
	public String checkDept(CAdxWebServiceXmlCCProxy proxy,CAdxCallContext callContext,String deptCode){
		String YENAFLG="";
		try {
			
			String xml="";
			xml+="<PARAM>";
			xml+="<FLD NAME='XDIE' TYPE='Char'>DPT</FLD>";
			xml+="<FLD NAME='XCCE' TYPE='Char'>"+deptCode+"</FLD>";
			xml+="</PARAM>";
			
			CAdxResultXml result=proxy.run(callContext, "XOACCE_RED", xml);
			JSONObject json=JSONObject.fromObject(result.getResultXml());
			YENAFLG=json.getJSONObject("GRP1").get("YENAFLG")+"";
			
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return YENAFLG;
	}
	
	/**
	 * 通过部门id获取部门名
	 * @param deptId
	 * @return
	 */
	public String getDeptName(String deptId){
		String deptName="";
		RecordSet rs=new RecordSet();
		String sql="select * from hrmDepartment where id="+deptId;
		rs.execute(sql);
		if(rs.next()){
			deptName=rs.getString("departmentname");
		}
		return deptName;
	}
	
}
