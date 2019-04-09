package com.gglh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.hrm.job.JobTitlesComInfo;
import weaver.hrm.resource.ResourceComInfo;
import weaver.hrm.webservice.DepartmentBean;
import weaver.hrm.webservice.HrmServiceImpl;
import weaver.hrm.webservice.SubCompanyBean;

public class SycHrmOrg extends BaseBean {
	
	String baseurl="http://my.ovuni.com:8010";    

	public List<Map<String, String>> getHrmSubList(String orgids){
		
		List<Map<String, String>> hrmOrgList=new ArrayList<Map<String,String>>();
		try {
			RecordSet recordSet=new RecordSet();
			String sql="select * from HrmSubCompany where id in("+orgids+") order by id   ";
			recordSet.execute(sql);
			
			while(recordSet.next()){
				
				String orgId="sub_"+recordSet.getString("id");
				String orgName=recordSet.getString("subcompanyname");
				String parentId="sub_"+recordSet.getString("supsubcomid");
				String isCompany="1";
				String status=recordSet.getString("canceled").equals("1")?"0":"1";
				
				Map<String, String> map=new HashMap<String, String>();
				map.put("orgId", orgId);
				map.put("orgName", orgName);
				map.put("parentId", parentId);
				map.put("isCompany", isCompany);
				map.put("status", status);
				
				hrmOrgList.add(map);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hrmOrgList;
	}
	
	public List<Map<String, String>> getHrmDeptList(String orgids){
		
		List<Map<String, String>> hrmOrgList=new ArrayList<Map<String,String>>();
		try {
			
			RecordSet recordSet=new RecordSet();
			String sql="select * from hrmdepartment where id in("+orgids+") order by id";
			recordSet.execute(sql);
			
			while(recordSet.next()){
				
				String orgId="dept_"+recordSet.getString("id");
				String orgName=recordSet.getString("departmentname");
				String parentId="dept_"+recordSet.getString("supdepid");
				parentId=parentId.equals("dept_0")?("sub_"+recordSet.getString("subcompanyid1")):parentId;
				String isCompany="0";
				String status=recordSet.getString("canceled").equals("1")?"0":"1";
				
				Map<String, String> map=new HashMap<String, String>();
				map.put("orgId", orgId);
				map.put("orgName", orgName);
				map.put("parentId", parentId);
				map.put("isCompany", isCompany);
				map.put("status", status);
				
				hrmOrgList.add(map);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return hrmOrgList;
	}
	
	public List<Map<String, String>> getHrmOrgList(){
		
		List<Map<String, String>> hrmOrgList=new ArrayList<Map<String,String>>();
		try {
			HrmServiceImpl hrmServiceImpl=new HrmServiceImpl();
			SubCompanyBean[] subCompanyinfo=hrmServiceImpl.getHrmSubcompanyInfo("127.0.0.1");
			for(SubCompanyBean hrmSub:subCompanyinfo){
				
				String orgId="sub_"+hrmSub.get_subcompanyid();
				String orgName=hrmSub.get_fullname();
				String parentId="sub_"+hrmSub.get_supsubcompanyid();
				String isCompany="1";
				String status=hrmSub.get_canceled().equals("1")?"0":"1";
				
				Map<String, String> map=new HashMap<String, String>();
				map.put("orgId", orgId);
				map.put("orgName", orgName);
				map.put("parentId", parentId);
				map.put("isCompany", isCompany);
				map.put("status", status);
				
				hrmOrgList.add(map);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {
			HrmServiceImpl hrmServiceImpl=new HrmServiceImpl();
			DepartmentBean[] departmentBeans=hrmServiceImpl.getHrmDepartmentInfo("127.0.0.1", "");
			for(DepartmentBean departmentBean:departmentBeans){
				
				String orgId="dept_"+departmentBean.get_departmentid();
				String orgName=departmentBean.get_fullname();
				String parentId="dept_"+departmentBean.get_supdepartmentid();
				parentId=parentId.equals("0")?("sub_"+departmentBean.get_subcompanyid()):parentId;
				String isCompany="0";
				String status=departmentBean.get_canceled().equals("1")?"0":"1";
				
				Map<String, String> map=new HashMap<String, String>();
				map.put("orgId", orgId);
				map.put("orgName", orgName);
				map.put("parentId", parentId);
				map.put("isCompany", isCompany);
				map.put("status", status);
				
				hrmOrgList.add(map);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return hrmOrgList;
	}
	
	public List<Map<String, String>> getHrmList(String deptids){
		
		List<Map<String, String>> hrmList=new ArrayList<Map<String,String>>();
		try {
			
			RecordSet recordSet=new RecordSet();
			String sql="select * from hrmresource where departmentid in("+deptids+") order by id";
			recordSet.execute(sql);
			
			ResourceComInfo resourceComInfo=new ResourceComInfo();
			JobTitlesComInfo jobTitlesComInfo=new JobTitlesComInfo();
			resourceComInfo.setTofirstRow();
			while(recordSet.next()){
				
				String number=recordSet.getString("workcode");
				String name=recordSet.getString("lastname");
				String gender=recordSet.getString("sex").equals("1")?"2":"1";
				String mobile=recordSet.getString("mobile");
				String id=recordSet.getString("certificatenum");
				String tel=recordSet.getString("telephone");
				String email=recordSet.getString("email");
				String orgId="dept_"+recordSet.getString("departmentid");
				String position=jobTitlesComInfo.getJobTitlesname(recordSet.getString("jobtitleid"));
				String status=recordSet.getString("status");
				status=status.equals("0")||status.equals("1")||status.equals("2")||status.equals("3")?"1":"0";
				String ts="";
				
				Map<String, String> map=new HashMap<String, String>();
				map.put("number", number);
				map.put("name", name);
				map.put("gender", gender);
				map.put("mobile", mobile);
				map.put("id", id);
				map.put("tel", tel);
				map.put("email", email);
				map.put("orgId", orgId);
				map.put("position", position);
				map.put("status", status);
				map.put("ts", ts);
				
				hrmList.add(map);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return hrmList;
		
	}
	
	/**
	 * 同步组织结构
	 * @return
	 */
	public boolean SynHrmOrg(String synctype,String orgids){
		
		Map<String, Object> dataMap=new HashMap<String, Object>();
		dataMap.put("userName","明源");
		dataMap.put("subject","FetchOrg");
		
		if(synctype.equals("2")){
			dataMap.put("data",getHrmSubList(orgids));
		}else if(synctype.equals("3")){
			dataMap.put("data",getHrmDeptList(orgids));
		}
		
		String JsonData=JSONObject.fromObject(dataMap).toString();
		
		Map<String, String> params=new HashMap<String, String>();
		params.put("JsonData",JsonData);
		
		writeLog("[SycHrmOrg.SynHrm] 正在同步组织结构 JsonData:"+JsonData);
		
		String url=baseurl+"/ZDGGMHJC.asmx/FetchOrg";
		String result=doPost(url,params);
		
		JSONObject resultObj=JSONObject.fromObject(result);
        String status=resultObj.getString("status");
        
        if(status.equals("false")){
        	writeLog("[SycHrmOrg.SynHrm] 同步组织失败 result:"+result);
        	return false;
        }else {
        	writeLog("[SycHrmOrg.SynHrm] 同步组织成功");
        	return true;
		}
		
	}
	
	/**
	 * 同步人员
	 * @return
	 */
	public boolean SynHrm(String deptids){
		
		Map<String, Object> dataMap=new HashMap<String, Object>();
		dataMap.put("userName","明源");
		dataMap.put("subject","FetchUser");
		dataMap.put("data",getHrmList(deptids));
		
		String JsonData=JSONObject.fromObject(dataMap).toString();
		
		Map<String, String> params=new HashMap<String, String>();
		params.put("JsonData",JsonData);
		
		writeLog("[SycHrmOrg.SynHrm] 正在同步人员 JsonData:"+JsonData);
		
		String url=baseurl+"/ZDGGMHJC.asmx/FetchUser";
		String result=doPost(url,params);
		
		JSONObject resultObj=JSONObject.fromObject(result);
        String status=resultObj.getString("status");
        
        if(status.equals("false")){
        	writeLog("[SycHrmOrg.SynHrm] 同步人员失败 result:"+result);
        	return false;
        }else {
        	writeLog("[SycHrmOrg.SynHrm] 同步人员成功");
        	return true;
		}
		
	}
	
	public static void main(String[] args) {
		
		Map<String, String> dataMap=new HashMap<String, String>();
		dataMap.put("userName","泛微");
		dataMap.put("subject","FetchOrg");
		dataMap.put("data","[]");
		
		Map<String, String> params=new HashMap<String, String>();
		params.put("JsonData",JSONObject.fromObject(dataMap).toString());
		
		System.out.println("正在发送请求......");
		
		//String url="http://my.ovuni.com:8010/ZDGGMHJC.asmx/FetchOrg";
		
		params.put("OAUserCode","ovu01029");
		
		String url="http://my.ovuni.com:8010/ZDGGMHJC.asmx/GetMyERPInfo";
		
		SycHrmOrg sycHrmOrg=new SycHrmOrg();
		
		String rslt=sycHrmOrg.doPost(url,params);
		
		JSONObject json= JSONObject.fromObject(rslt);
		
		String msg=json.getString("msg");
		
		JSONArray msgArray=JSONArray.fromObject(msg);
        
		System.out.println(msgArray.getJSONObject(0).get("Subject"));
		
	}
	
	public String doPost(String url, Map<String, String> _params) {  
        
        String result ="";  
        HttpClient client = new HttpClient();  
        PostMethod method = new PostMethod(url);  
        // 设置Http Post数据  
        if (_params != null) {  
            for (Map.Entry<String, String> entry : _params.entrySet()) {  
                method.setParameter(entry.getKey(), String.valueOf(entry.getValue()));  
            }  
        }  
          
        try {  
        	method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
            client.executeMethod(method); 
            result =method.getResponseBodyAsString();
            result = result.substring(result.indexOf("{"), result.lastIndexOf("}") + 1);
            
        } catch (Exception e) {  
        	writeLog("[SycHrmOrg.doPost] 执行HTTP Post请求" + url + "时，发生异常！ error:"+e.getMessage());
            e.printStackTrace();  
        } finally {  
            method.releaseConnection();  
        }  
        
        writeLog("[SycHrmOrg.doPost] result:"+result);
        return result;  
    }  
	
	
	
}