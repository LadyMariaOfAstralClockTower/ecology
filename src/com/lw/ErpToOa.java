package com.lw;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.lw.webservice.CAdxCallContext;
import com.lw.webservice.CAdxParamKeyValue;
import com.lw.webservice.CAdxResultXml;
import com.lw.webservice.CAdxWebServiceXmlCCProxy;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;


/**
 * 从ERP读取数据到OA触发流程
 * @author GodWei
 *
 */
public class ErpToOa extends BaseBean {
	
	private WorkflowManager wm=new WorkflowManager();
	
	/**
	 * 定时任务主任务
	 */
	public void doSync(){
		
		writeLog("[ErpToOa] 开始触发申请单流程.......................");
		List<Map<String, String>> requestLsit=getRequestLsit();
		writeLog("[ErpToOa.doSync] requestLsit:"+requestLsit.toString());
		wm.createWorkflow(requestLsit,"203");//触发申请单流程
		writeLog("[ErpToOa] 结束触发申请单流程.......................");
		
		writeLog("[ErpToOa] 开始触发订单流程.......................");
		List<Map<String, String>> orderList=getOrderLsit();
		writeLog("[ErpToOa.doSync] orderList:"+orderList.toString());
		wm.createWorkflow(orderList, "204");//触发订单流程
		writeLog("[ErpToOa] 结束触发订单流程.......................");
		
		writeLog("[ErpToOa] 开始触发付款流程.......................");
		List<Map<String, String>> payList=getPayLsit();
		writeLog("[ErpToOa.doSync] payList:"+payList.toString());
		wm.createWorkflow(payList, "205");//触发付款流程
		writeLog("[ErpToOa] 结束触发付款流程.......................");
		
	}
	
	
	/**
	 * 获取付款编号
	 * @return
	 */
	public List<Map<String, String>> getPayLsit(){
		
		List<Map<String, String>> payLsit=new ArrayList<Map<String,String>>();
		try {

			CAdxWebServiceXmlCCProxy proxy=new CAdxWebServiceXmlCCProxy();
			
			CAdxCallContext context=new CAdxCallContext();
			context.setPoolAlias("WSNEWAY");
			context.setCodeUser("admin");
			context.setPassword("");
			context.setCodeLang("CHI");
			context.setRequestConfig("adxwss.optreturn=json");
			
			CAdxParamKeyValue[] objKeys=new CAdxParamKeyValue[1];
			
			objKeys[0]=new CAdxParamKeyValue("1","*");
			
			CAdxResultXml results;
			results=proxy.query(context, "XOAPAY", objKeys, 9999);
			
			JSONArray jsonArrays=JSONArray.fromObject(results.getResultXml());
			
			writeLog("[ErpToOa.getPayLsit] 付款 result:"+results.getResultXml()+" status:"+results.getStatus());
			
			for(int i=0;i<jsonArrays.size();i++){
				JSONObject objs=jsonArrays.getJSONObject(i);
				
				CAdxParamKeyValue[] objKey=new CAdxParamKeyValue[1];
				objKey[0]=new CAdxParamKeyValue("NUM",(String)objs.get("NUM"));
				
				CAdxResultXml result=proxy.read(context, "XOAPAY", objKey);
				
				JSONObject obj=JSONObject.fromObject(result.getResultXml());
				
				JSONObject pshObj1=obj.getJSONObject("PAY0_1");
				JSONObject pshObj2=obj.getJSONObject("PAY1_2");
				JSONObject pshObj3=obj.getJSONObject("PAY1_1");
				JSONObject pshObj5=JSONArray.fromObject(obj.get("PAY1_4")).getJSONObject(0);
				
				Map<String, String> map=new HashMap<String, String>();
				
				map.put("LOGIN", ""+pshObj1.get("LOGIN"));
				
				if((""+pshObj1.get("LOGIN")).equals("")||checkUserId(""+pshObj1.get("LOGIN")).equals("-1")){//如果该loginid在OA不存在，则无法触发流程
					writeLog("[ErpToOa.getPayLsit] loginid："+pshObj1.get("LOGIN")+" 该用户在OA中不存在，无法触发流程！");
					continue;
				}
				
				map.put("NUM", ""+pshObj1.get("NUM"));
				
				if(checkWorkflow(""+pshObj1.get("NUM"), "205").equals("1")){//如果该编号已经触发过流程，则不再次触发
					continue;
				}
				
				map.put("YSBMDAT", ""+pshObj2.get("YSBMDAT"));
				map.put("CUR", ""+pshObj2.get("CUR"));
				map.put("ZCUR", ""+pshObj2.get("ZCUR"));
				map.put("AMTCUR", ""+pshObj2.get("AMTCUR"));
				
				map.put("FCY", ""+pshObj3.get("FCY"));
				map.put("ZFCY", ""+pshObj3.get("ZFCY"));
				map.put("BPR", ""+pshObj3.get("BPR"));
				map.put("ZBPR", ""+pshObj3.get("ZBPR"));
				
				map.put("DENCOD", ""+pshObj5.get("DENCOD"));
				map.put("VCRTYP", ""+pshObj5.get("VCRTYP"));
				map.put("VCRNUM", ""+pshObj5.get("VCRNUM"));
				map.put("AMTLIN", ""+pshObj5.get("AMTLIN"));
				
				payLsit.add(map);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return payLsit;
	}
	
	
	/**
	 * 获取订单编号
	 * @return
	 */
	public List<Map<String, String>> getOrderLsit(){
		
		List<Map<String, String>> orderLsit=new ArrayList<Map<String,String>>();
		try {

			CAdxWebServiceXmlCCProxy proxy=new CAdxWebServiceXmlCCProxy();
			
			CAdxCallContext context=new CAdxCallContext();
			context.setPoolAlias("WSNEWAY");
			context.setCodeUser("admin");
			context.setPassword("");
			context.setCodeLang("CHI");
			context.setRequestConfig("adxwss.optreturn=json");
			
			CAdxParamKeyValue[] objKeys=new CAdxParamKeyValue[1];
			
			objKeys[0]=new CAdxParamKeyValue("1","*");
			
			CAdxResultXml results;
			results=proxy.query(context, "XOAPOH", objKeys, 9999);
			
			JSONArray jsonArrays=JSONArray.fromObject(results.getResultXml());
			
			writeLog("[ErpToOa.getOrderLsit] 订单 result:"+results.getResultXml()+" status:"+results.getStatus());
			
			for(int i=0;i<jsonArrays.size();i++){
				JSONObject objs=jsonArrays.getJSONObject(i);
				
				CAdxParamKeyValue[] objKey=new CAdxParamKeyValue[1];
				objKey[0]=new CAdxParamKeyValue("POHNUM",(String)objs.get("POHNUM"));
				
				CAdxResultXml result=proxy.read(context, "XOAPOH", objKey);
				
				JSONObject obj=JSONObject.fromObject(result.getResultXml());
				
				JSONObject pshObj1=obj.getJSONObject("POH0_1");
				JSONObject pshObj2=obj.getJSONObject("POH1_2");
				JSONObject pshObj3=obj.getJSONObject("POH1_3");
				JSONObject pshObj4=obj.getJSONObject("POH1_4");
				JSONObject pshObj5=JSONArray.fromObject(obj.get("POH2_1")).getJSONObject(0);
				
				Map<String, String> map=new HashMap<String, String>();
				
				map.put("POHNUM", ""+pshObj1.get("POHNUM"));
				
				if(checkWorkflow(""+pshObj1.get("POHNUM"), "204").equals("1")){//如果该编号已经触发过流程，则不再次触发
					continue;
				}
				
				map.put("LOGIN", ""+pshObj1.get("LOGIN"));
				
				if((""+pshObj1.get("LOGIN")).equals("")||checkUserId(""+pshObj1.get("LOGIN")).equals("-1")){//如果该loginid在OA不存在，则无法触发流程
					writeLog("[ErpToOa.getOrderLsit] loginid："+pshObj1.get("LOGIN")+" 该用户在OA中不存在，无法触发流程！");
					continue;
				}
				
				map.put("POHFCY", ""+pshObj1.get("POHFCY"));
				map.put("ZPOHFCY", ""+pshObj1.get("ZPOHFCY"));
				map.put("POHNUM", ""+pshObj1.get("POHNUM"));
				map.put("YSBMDAT", ""+pshObj1.get("YSBMDAT"));
				map.put("ORDDAT", ""+pshObj1.get("ORDDAT"));
				map.put("BPSNUM", ""+pshObj1.get("BPSNUM"));
				map.put("BPRNAM", ""+pshObj1.get("BPRNAM"));
				
				map.put("PTE", ""+pshObj2.get("PTE"));
				map.put("ZPTE", ""+pshObj2.get("ZPTE"));
				
				String id=checkUserId(""+pshObj2.get("BUY"));
				if(id.equals("")||id.equals("-1")){
					writeLog("[ErpToOa.getOrderLsit] BUY："+pshObj2.get("BUY")+" 该采购员在OA中不存在，无法触发流程！");
					continue;
				}
				map.put("BUY", id);
				
				
				map.put("CUR", ""+pshObj3.get("CUR"));
				map.put("ZCUR", ""+pshObj3.get("ZCUR"));
				
				map.put("VACBPR", ""+pshObj4.get("VACBPR"));
				map.put("ZVACBPR", ""+pshObj4.get("ZVACBPR"));
				
				map.put("ITMREF", ""+pshObj5.get("ITMREF"));
				map.put("ITMDES", ""+pshObj5.get("ITMDES"));
				map.put("ITMDES1", ""+pshObj5.get("ITMDES1"));
				map.put("PUU", ""+pshObj5.get("PUU"));
				map.put("QTYPUU", ""+pshObj5.get("QTYPUU"));
				map.put("EXTRCPDAT", ""+pshObj5.get("EXTRCPDAT"));
				map.put("GROPRI", ""+pshObj5.get("GROPRI"));
				map.put("LINAMT", ""+pshObj5.get("LINAMT"));
				
				orderLsit.add(map);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return orderLsit;
	}
	
	
	/**
	 * 获取申请单编号
	 * @return
	 */
	public List<Map<String, String>> getRequestLsit(){
		
		List<Map<String, String>> RequestLsit=new ArrayList<Map<String,String>>();
		try {

			CAdxWebServiceXmlCCProxy proxy=new CAdxWebServiceXmlCCProxy();
			
			CAdxCallContext context=new CAdxCallContext();
			context.setPoolAlias("WSNEWAY");
			context.setCodeUser("admin");
			context.setPassword("");
			context.setCodeLang("CHI");
			context.setRequestConfig("adxwss.optreturn=json");
			
			CAdxParamKeyValue[] objKeys=new CAdxParamKeyValue[1];
			
			objKeys[0]=new CAdxParamKeyValue("1","*");
			
			CAdxResultXml results;
			results=proxy.query(context, "XOAPSH", objKeys, 9999);
			
			JSONArray jsonArrays=JSONArray.fromObject(results.getResultXml());
			
			writeLog("[ErpToOa.getRequestLsit] 申请单 result:"+results.getResultXml()+" status:"+results.getStatus());
			
			for(int i=0;i<jsonArrays.size();i++){
				
				JSONObject objs=jsonArrays.getJSONObject(i);
				
				CAdxParamKeyValue[] objKey=new CAdxParamKeyValue[1];
				objKey[0]=new CAdxParamKeyValue("PSHNUM",(String)objs.get("PSHNUM"));
				
				CAdxResultXml result=proxy.read(context, "XOAPSH", objKey);
				
				JSONObject obj=JSONObject.fromObject(result.getResultXml());
				
				JSONObject pshObj1=obj.getJSONObject("PSH0_1");
				JSONObject pshObj3=JSONArray.fromObject(obj.get("PSH1_1")).getJSONObject(0);
				
				Map<String, String> map=new HashMap<String, String>();
				
				map.put("PSHNUM", ""+pshObj1.get("PSHNUM"));
				
				if(checkWorkflow(""+pshObj1.get("PSHNUM"), "203").equals("1")){//如果该编号已经触发过流程，则不再次触发
					continue;
				}
				
				map.put("LOGIN", ""+pshObj1.get("LOGIN"));
				
				if((""+pshObj1.get("LOGIN")).equals("")||checkUserId(""+pshObj1.get("LOGIN")).equals("-1")){//如果该loginid在OA不存在，则无法触发流程
					writeLog("[ErpToOa.getRequestLsit] loginid："+pshObj1.get("LOGIN")+" 该用户在OA中不存在，无法触发流程！");
					continue;
				}
				
				map.put("REQUSR", ""+pshObj1.get("REQUSR"));
				map.put("ZREQUSR", ""+pshObj1.get("ZREQUSR"));
				map.put("PSHFCY", ""+pshObj1.get("PSHFCY"));
				map.put("ZPSHFCY", ""+pshObj1.get("ZPSHFCY"));
				map.put("PRQDAT", ""+pshObj1.get("PRQDAT"));
				map.put("XSOHNUM", ""+pshObj1.get("XSOHNUM"));
				map.put("YSBMDAT", ""+pshObj1.get("YSBMDAT"));
				
				map.put("ITMREF", ""+pshObj3.get("ITMREF"));
				map.put("ITMDES", ""+pshObj3.get("ITMDES"));
				map.put("ITMDES1", ""+pshObj3.get("ITMDES1"));
				map.put("EXTRCPDAT", ""+pshObj3.get("EXTRCPDAT"));
				map.put("PUU", ""+pshObj3.get("PUU"));
				map.put("QTYPUU", ""+pshObj3.get("QTYPUU"));
				map.put("BPSNUM", ""+pshObj3.get("BPSNUM"));
				map.put("LINAMT", ""+pshObj3.get("LINAMT"));
				map.put("NETPRI", ""+pshObj3.get("NETPRI"));
				
				RequestLsit.add(map);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return RequestLsit;
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
	 * 通过登录名获取id
	 * @param loginid
	 * @return
	 */
	public String checkUserId(String loginid){
		
		String id="-1";
		RecordSet rs=new RecordSet();
		String sql="select * from hrmResource where loginid='"+loginid+"'";
		rs.execute(sql);
		if(rs.next()){
			id=rs.getString("id");
		}
		return id;
	}
	
	
}
