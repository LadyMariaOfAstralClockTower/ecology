package com.ltzy.webservices;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.jws.WebService;

import com.ltzy.DES;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.company.DepartmentComInfo;
import weaver.hrm.company.SubCompanyComInfo;
import weaver.hrm.resource.ResourceComInfo;

@WebService
public class HrmOrgServiceImpl extends BaseBean implements HrmOrgService
{
	
	@Override
	public String fetchHrmOrg(String params) {
		
		JSONObject paramObj=JSONObject.fromObject(params); 
		
		String subject=paramObj.getString("subject");
		String userName=paramObj.getString("userName");
		String data=paramObj.getString("data");
		
		log("subject:"+subject+" userName:"+userName+" data:"+data);
		
		JSONArray dataList=JSONArray.fromObject(data);
		
		if (subject.equalsIgnoreCase("FetchOrg")) {
			
			RecordSet recordSet=new RecordSet();
			for(int i=0;i<dataList.size();i++){
				
				JSONObject obj=(JSONObject)dataList.get(i);
				
				String parentId = obj.getString("parentId");
				
				if(!parentId.equals("")){
					String sql="select * from HrmCompany where fid='"+parentId+"'"; //检查上级是否属于顶级分部
					recordSet.execute(sql);
					if(recordSet.next()){
						SynSubCompany(obj);
					}else{
						SynDept(obj);
					}
				}
			}
			
			try
	        {
	          SubCompanyComInfo subCompanyComInfo = new SubCompanyComInfo();
	          DepartmentComInfo departmentComInfo=new DepartmentComInfo();
	          subCompanyComInfo.removeCompanyCache();
	          departmentComInfo.removeCompanyCache();
	        }
	        catch (Exception e)
	        {
	          e.printStackTrace();
	        }
			
	  }else if(subject.equalsIgnoreCase("FetchUser")){
			
			for (int i = 0; i < dataList.size(); i++) {
				JSONObject hrmObj = (JSONObject)dataList.get(i); 
				synHrmResource(hrmObj);
			}
			
			try
	        {
	          ResourceComInfo resourceComInfo = new ResourceComInfo();
	          resourceComInfo.removeResourceCache();
	        }
	        catch (Exception e)
	        {
	          e.printStackTrace();
	        }
				
	   }
		
		Map<String, String> result=new HashMap<String, String>();
		result.put("userName", userName);
		result.put("subject", subject);
		result.put("status", "true");
		result.put("info", "成功同步"+dataList.size()+"条");
		
		JSONObject resultObj=JSONObject.fromObject(result);
		
		return resultObj.toString();
	}
	
	public void synHrmResource(JSONObject hrmOrg)
	  {
	    RecordSet rs1 = new RecordSet();
	    RecordSet rs3 = new RecordSet();
	    //RecordSetDataSource hrrs = new RecordSetDataSource("hr");
	    char separator = Util.getSeparator();
	    Calendar todaycal = Calendar.getInstance();
	    String today = Util.add0(todaycal.get(1), 4) + "-" + 
	    Util.add0(todaycal.get(2) + 1, 2) + "-" +Util.add0(todaycal.get(5), 2);
	    String userpara = "1" + separator + today;
	    
	    
    	String loginid = hrmOrg.getString("number"); //用户账号
    	String userCode = hrmOrg.getString("userCode"); //人员编号
		String lastname = hrmOrg.getString("name"); //用户名称
		String password =hrmOrg.getString("password"); //密码
		String sex = hrmOrg.getString("gender"); //性别
		String mobile =hrmOrg.getString("mobile"); //手机号
		String idcard = hrmOrg.getString("id"); //身份证
		String telephone =hrmOrg.getString("tel"); //办公电话
		String email =hrmOrg.getString("email"); //电子邮箱
		String orgId = hrmOrg.getString("orgId"); //所属组织Id
		String position = hrmOrg.getString("position"); //职位
		String status = hrmOrg.getString("status"); //是否启用
		String isHidePhone = hrmOrg.getString("isHidePhone"); //是否隐藏手机号
		String ts = hrmOrg.getString("ts"); //更新时间

	    sex = sex.equals("1")? "1" : "0";
	    //String safelevel = Util.null2String(hrrs.getString("safelevel"));
	    //String safelevel="";
	    
	    String manager = Util.null2String(hrmOrg.getString("parentUserCode")); //上级
	    
	    String sql="";
	      
	    if (!"".equals(manager))
	      {
	        rs3.executeSql("select id from hrmresource where workcode='" + manager + "'");
	        if (rs3.next()) {
	          manager = rs3.getString("id");
	        } else {
	          manager = "";
	        }
	    }
	      //String PK_POST = Util.null2String(hrrs.getString("PK_POST"));
	      String subcompanyid = "";
	      String deptid = "";
	      sql = "select id,subcompanyid1 from hrmdepartment where fid ='" +orgId + "' and  departmentcode!='' and departmentcode is not null ";
	      
	      log("sql13===" + sql);
	      
	      rs1.executeSql(sql);
	      if (rs1.next())
	      {
	        deptid = rs1.getString("id");
	        subcompanyid = rs1.getString("subcompanyid1");
	        
	        //岗位处理
	        String jobtitle = "";
	        sql = "select id from hrmjobtitles where jobtitlename='" + position + "'";
	        rs1.executeSql(sql);
	        if (rs1.next()) {
	        	jobtitle = Util.null2String(rs1.getString("id"));
	        }else{
	        	sql = "insert into HrmJobTitles(jobtitlename,jobtitlemark) values('" + position +"','" +position +"')";
	        	rs1.executeSql(sql);
	        	sql="select max(id) as id from HrmJobTitles";
	        	if(rs1.next()){
	        		jobtitle = Util.null2String(rs1.getString("id"));
	        	}
	        }
	        
	        log("获取岗位id sql14===" + sql);
	        sql = "select id from hrmresource where workcode='" + userCode + "' and workcode is not null and workcode!='' ";
	        rs1.executeSql(sql);
	        if (rs1.next())
	        {
	          int hrmid = rs1.getInt("id");
	          sql = "update hrmresource set lastname='" + lastname + "' ,sex='" + sex + "' ";
	          
	          if (!"".equals(email.trim())) {
	            sql = sql + " ,email='" + email + "'";
	          }
	          if (!"".equals(idcard.trim())) {
	            sql = sql + " ,certificatenum='" + idcard + "'";
	          }
	          if (!"".equals(mobile.trim())) {
	            sql = sql + " ,mobile='" + mobile + "'";
	          }
	          if (status.equals("0")) {
	            sql = sql + ",status=5,loginid='',password=''";
	          }
	          
	          //原接口中岗位 
	          if (!"".equals(jobtitle)) {
		         sql = sql + ",jobtitle='"+jobtitle+"'";
		      }
	          
	          sql = sql + " ,managerid='" + manager + "'";
	          sql = sql + " ,departmentid='" + deptid + 
	            "',subcompanyid1='" + subcompanyid + 
	            "',telephone='"+telephone+"'  where id='" + 
	            hrmid + "'";
	          
	          rs1.executeSql(sql);
	          log("更新人员的sql:" + sql);
	        }else{
	        	
	          rs1.executeProc("HrmResourceMaxId_Get", "");
	          rs1.next();
	          int maxid = rs1.getInt(1);
	          if (maxid > 0)
	          {
	            sql ="insert into hrmresource(id,loginid,password,workcode,lastname,departmentid,subcompanyid1,systemlanguage,sex,email,mobile,certificatenum,status,managerid,dsporder,jobtitle,telephone) " +
	            	 "values (" + maxid + ",'" + loginid + "','202CB962AC59075B964B07152D234B70','" + userCode + "','" + lastname + "','" + deptid + "','" + subcompanyid + "' ,7,'" + sex + "','" + email + "','" + mobile + "','" + idcard + "','1','" + manager + "','900','" + jobtitle + "','"+telephone+"')";
	            rs1.executeSql(sql);
	            log("新增人员的sql:" + sql);
	            rs1.executeProc("HrmResource_CreateInfo", maxid + separator + userpara + separator + userpara);
	            
	            String para = maxid + separator + "-1" + separator + deptid + separator + subcompanyid + separator + "10" + separator;
	            rs1.executeProc("HrmResource_Trigger_Insert", para);
	            
	            String sql_1 = "insert into HrmInfoStatus (itemid,hrmid,status) values(1," + maxid + ",1)";
	            rs1.executeSql(sql_1);
	            String sql_2 = "insert into HrmInfoStatus (itemid,hrmid) values (2," + maxid + ")";
	            rs1.executeSql(sql_2);
	            String sql_3 = "insert into HrmInfoStatus (itemid,hrmid) values (3," + maxid + ")";
	            rs1.executeSql(sql_3);
	            String sql_10 = "insert into HrmInfoStatus (itemid,hrmid) values(10," + maxid + ")";
	            rs1.executeSql(sql_10);
	          }
	        }
	      }
	    
	    log("同步完成");
	  }
	
	
	/**
	 * 同步分部
	 * @param subObj
	 */
	public void SynSubCompany(JSONObject orgObj){
		
		String dept_code = orgObj.getString("orgNumber");
		String dept_name = orgObj.getString("orgName");
		String orgId = orgObj.getString("orgId");
		String parentId = orgObj.getString("parentId");
		String isDeleted = orgObj.getString("isDeleted");
		String status = orgObj.getString("status");
		
		log("同步分部：dept_code:"+dept_code+" orgName:"+dept_name+" parentId:"+parentId+" isDeleted:"+isDeleted+" status:"+status);
		
		String subcomid="0"; //分部ID
		String supsubcomid="0"; //上级分部ID
		
		//查询上级分部ID
		RecordSet recordSet=new RecordSet();
		String sql="select id from HrmSubCompany where fid='"+parentId+"'";
		recordSet.execute(sql);
		if(recordSet.next()){
			supsubcomid=recordSet.getString("id");
		}
		
		//根据编码查询当前分部是否存在
		sql="select id from HrmSubCompany where fid='"+orgId+"' and supsubcomid='"+supsubcomid+"'";
		recordSet.execute(sql);
		
		if(recordSet.next()){ //如果存在就更新
			
			sql = "update HrmSubCompany set subcompanyname='" + dept_name + "',subcompanydesc='" + dept_name + "',supsubcomid='" + supsubcomid + "',canceled='0' where fid='" + orgId + "'";
			log("更新分部 sql:"+sql);
			recordSet.executeSql(sql);
			
		}else{
			
			sql="insert into hrmsubcompany(subcompanyname,subcompanydesc,companyid,supsubcomid,subcompanycode,fid) values ('" + dept_name + "','" + dept_name + "',1," + supsubcomid + ",'" + dept_code + "','"+orgId+"')";
			recordSet.executeSql(sql);
			
			log("新增分部 sql:"+sql);
			
			sql="select id from HrmSubCompany where fid = '" +orgId + "'"; //查询新增的分部ID
			recordSet.executeSql(sql);
			if(recordSet.next()){
				subcomid=recordSet.getString("id"); //分部ID
				//设置权限
				recordSet.executeSql("insert into leftmenuconfig (userid,infoid,visible,viewindex,resourceid,resourcetype,locked,lockedbyid,usecustomname,customname,customname_e)  select  distinct  userid,infoid,visible,viewindex," + 
						subcomid + ",2,locked,lockedbyid,usecustomname,customname,customname_e from leftmenuconfig where resourcetype=1  and resourceid=1");
				recordSet.executeSql("insert into mainmenuconfig (userid,infoid,visible,viewindex,resourceid,resourcetype,locked,lockedbyid,usecustomname,customname,customname_e)  select  distinct  userid,infoid,visible,viewindex," + 
						subcomid + ",2,locked,lockedbyid,usecustomname,customname,customname_e from mainmenuconfig where resourcetype=1  and resourceid=1");
			}		
		}
		
		//delSubCompany(orgId, dept_name);
        if ((isDeleted.trim().equals("1")) || (status.trim().equals("0")))
        {
          log("该分部在NC中属于撤销或未启用状态，进行封存,分部ID" + dept_code);
          recordSet.executeSql("update HrmSubCompany set canceled='1' where fid='" + orgId + "'");
        }
	
	}
	
	public void delSubCompany(String orgId, String shortname){
	    try
	    {
	      RecordSet rs = new RecordSet();
	      String sql = "";
	      sql = "select count(id) from hrmdepartment where EXISTS (select 1 from hrmsubcompany b where hrmdepartment.subcompanyid1=b.id and b.fid='" + 
	      orgId + "') and (canceled='0' or canceled is null)";
	      rs.executeSql(sql);
	      int rows = 0;
	      while (rs.next()) {
	        rows += Util.getIntValue(rs.getString(1), 0);
	      }
	      if (rows > 0)
	      {
	        log("封存分部失败，该分部下有正常的部门，分部名称：" + shortname);
	        sql = "update HrmSubCompany set canceled='0' where fid='" + orgId + "'";
	        rs.executeSql(sql);
	      }
	      else
	      {
	        sql = "update HrmSubCompany set canceled='1' where fid='" + orgId + "'";
	        rs.executeSql(sql);
	        log("封存分部成功," + sql);
	      }
	    }
	    catch (Exception e)
	    {
	      log("封存分部失败," + e);
	    }
	  }
	
	
	public void SynDept(JSONObject orgObj)
	  {
	    try
	    {
	      RecordSet rs = new RecordSet();
	      RecordSet rs1 = new RecordSet();
	      RecordSet rs2 = new RecordSet();
	      
	      String dept_code = orgObj.getString("orgNumber");
		  String dept_name = orgObj.getString("orgName");
		  String orgId = orgObj.getString("orgId");
		  String parentId = orgObj.getString("parentId");
		  String isDeleted = orgObj.getString("isDeleted");
		  String status = orgObj.getString("status");
	      
	      int subcomid = 0;
	      int supdeptid = 0;
	      
	      String sql="";
	      
	      //查询上级部门
	      if (!"".equals(parentId)){
	        	
	          sql="select id from HrmSubCompany where fid = '" + parentId + "' and subcompanycode !='' and subcompanycode is not null ";
	          rs1.executeSql(sql);
	          
	          log("查询上级分部--sql:"+sql);
	          
	          if (rs1.next()){
	             subcomid = Util.getIntValue(rs1.getString("id"), 0);
	          }else{
	        	
	        	sql="select id,subcompanyid1 from hrmdepartment where fid = '" +parentId + "'";
	            rs2.executeSql(sql);
	            if (rs2.next())
	            {
	              supdeptid = Util.getIntValue(rs2.getString("id"), 0);
	              subcomid = Util.getIntValue(rs2.getString("subcompanyid1"), 0);
	            }
	          }
	        }
	        
	        log("supdeptid:" + supdeptid + ",subcomid:" + subcomid);
	        
	        //查询当前部门是否存在
	        sql="select id from hrmdepartment where fid = '" +orgId +"' and departmentcode!='' and departmentcode is not null and supdepid="+supdeptid;
	        rs.executeSql(sql);
	        
	        if(rs.next()){
	        	
	        	sql = "update hrmdepartment set subcompanyid1=" + subcomid + ", departmentname='" + dept_name + 
		          "',departmentmark='" + dept_name + "',supdepid=" + supdeptid + ",canceled='0' where fid='" + orgId + "'";
		        rs.executeSql(sql);
		        
		        log("更新OA部门的sql:" + sql);
		        
		        if ((isDeleted.trim().equals("1")) || (status.trim().equals("0")))
		        {
		          log("该部门在NC中属于撤销或未启用状态，进行封存,部门ID" + dept_code);
		          rs.executeSql("update hrmdepartment set canceled='1' where departmentcode='"+dept_code + "'");
		        }
	        	
	        }else{
	        	
	        	String sql_insert = "insert into hrmdepartment(departmentname,departmentmark,subcompanyid1,supdepid,showorder,departmentcode,fid) values ('" + 
	            dept_name + 
	            "','" + 
	            dept_name + 
	            "'," + 
	            subcomid + 
	            "," + supdeptid + ",'','" + dept_code + "','"+orgId+"')";
	        	
	            rs1.executeSql(sql_insert);
	          
	            log("新增部门成功," + sql_insert);
	        	
	        }
	        
	        //delDepartment(orgId, dept_name);
	        
	        if ((isDeleted.trim().equals("1")) || (status.trim().equals("0")))
	        {
	          log("该部门在NC中属于撤销或未启用状态，进行封存,部门ID" + dept_code);
	          rs.executeSql("update hrmdepartment set canceled='1' where departmentcode='" +orgId + "'");
	        }
	      
	      return;
	    }
	    catch (Exception e)
	    {
	      log("同步部门失败," + e);
	    }
	  }
	
	/**
	 * 封存部门
	 * @param code
	 * @param shortname
	 */
	public void delDepartment(String orgId, String shortname)
	  {
	    try
	    {
	      RecordSet rs = new RecordSet();
	      String sql = "";
	      int id = 0;
	      rs.executeSql("select id from hrmdepartment where fid='" +orgId + "'");
	      if (rs.next()) {
	        id = Util.getIntValue(rs.getString(1), 0);
	      }
	      sql ="select id from hrmresource where status in (0,1,2,3) and EXISTS (select 1 from hrmdepartment b where hrmresource.departmentid=b.id and b.id = " + id + ") union select id from hrmdepartment where (canceled = '0' or canceled is null) and id in (select id from hrmdepartment where supdepid =" + id + ")";
	      rs.executeSql(sql);
	      int rows = 0;
	      while (rs.next()) {
	        rows += Util.getIntValue(rs.getString(1), 0);
	      }
	      if (rows > 0)
	      {
	        log("封存部门失败，该部门下有正常的人员，部门名称：" + shortname);
	      }
	      else
	      {
	        sql = "update hrmdepartment set canceled='1' where fid='" + orgId + "'";
	        rs.executeSql(sql);
	        log("封存部门成功!" + sql);
	      }
	    }
	    catch (Exception e)
	    {
	      log("封存部门失败," + e);
	    }
	  }
	
	/**
	 * 更新密码
	 * @param number
	 * @param uiNumber
	 * @param newPassword
	 * @return
	 */
	public String updatePassword(String number,String uiNumber,String newPassword) {
		
		int flag=0;
		String info="成功";
		try {
			
			//MD5加密后的密码
			//String password=Util.getEncrypt(DES.decrypt(newPassword,"_myhome_"));
			String password=Util.getEncrypt(newPassword);
			
			RecordSet recordSet=new RecordSet();
			String sql="select id from hrmresource where workcode='"+uiNumber+"'";
			recordSet.execute(sql);
			
			if(recordSet.next()){
				sql="update hrmresource set password='"+password+"' where workcode='"+uiNumber+"'";
				recordSet.execute(sql);
			}else {
				flag=-1; //用户不存在
				info=uiNumber+"对应的用户在OA中不存在";
			}
			
			log("[HrmOrgServiceImpl.updatePassword] 密码更新成功 uiNumber:"+uiNumber+" newPassword:"+newPassword);
			
		} catch (Exception e) {
			flag=-2; //用户不存在
			info="密码更新失败 uiNumber:"+uiNumber+" newPassword:"+newPassword+" error:"+e.toString();
		}
		
		if(flag<0){
			log("[HrmOrgServiceImpl.updatePassword] info:"+info);
		}
		
		
		Map<String, String> result=new HashMap<String, String>();
		result.put("status",flag<0?"false":"true");
		result.put("info", info);
		
		JSONObject resultObj=JSONObject.fromObject(result);
		
		return resultObj.toString();
		
	}
	
	public void log(String logstr){
		
		String showsqllog = getPropValue("SHOWSQLLOG", "showsqllog");
		if ("showsqllog".equals(showsqllog)){
            writeLog(logstr);
        }
		
	}
	
	public static void testFetchHrmOrg(){
		
		String params="{\"userName\": \"fw\",\"subject\":\"FetchOrg\",\"data\": [{\"orgNumber\": \"01!0101!010101\",\"orgName\": \"综合管理部\",\"parentNumber\": \"01!0101\",\"isDeleted\": \"否\",\"status\": \"启用\"}" +
				"" +",{\"orgNumber\": \"01!0101!222222222\",\"orgName\": \"综合管理部\",\"parentNumber\": \"01!0101\",\"isDeleted\": \"否\",\"status\": \"启用\"}"+
				"]}";
		
		//String params = "{\"userName\": \"主数据\",\"subject\": \"FetchUser\",\"data\": [{\"number\": \"gaokuan\",\"name\": \"高宽\",\"password\": \"gaokuan\",\"gender\": \"1\",\"mobile\": \"13888888888\", \"id\": \"420984200001018888\"," +
		//" \"tel\": \"027-88888888\",\"email\":\"123@qq.com\",\"orgNumber\": \"01!0101!010101\", \"position\": \"管理员\",\"status\": \"1\",\"isHidePhone\": \"1\",\"ts\": \"2016-12-12 00:00:00\" }]}";
		
		HrmOrgServiceImpl hrmOrgServiceImpl=new HrmOrgServiceImpl();
		
		hrmOrgServiceImpl.fetchHrmOrg(params);
		
	}
	
	
	public static void main(String[] args) {
		
		String params="{\"userName\": \"fw\",\"subject\":\"FetchOrg\",\"data\": [{\"orgNumber\": \"01!0101!010101\",\"orgName\": \"综合管理部\",\"parentNumber\": \"01!0101\",\"isDeleted\": \"否\",\"status\": \"启用\"}" +
				"" +",{\"orgNumber\": \"01!0101!222222222\",\"orgName\": \"综合管理部\",\"parentNumber\": \"01!0101\",\"isDeleted\": \"否\",\"status\": \"启用\"}"+
				"]}";
		
		//String params = "{\"userName\": \"主数据\",\"subject\": \"FetchUser\",\"data\": [{\"number\": \"gaokuan\",\"name\": \"高宽\",\"password\": \"gaokuan\",\"gender\": \"1\",\"mobile\": \"13888888888\", \"id\": \"420984200001018888\"," +
		//" \"tel\": \"027-88888888\",\"email\":\"123@qq.com\",\"orgNumber\": \"01!0101!010101\", \"position\": \"管理员\",\"status\": \"1\",\"isHidePhone\": \"1\",\"ts\": \"2016-12-12 00:00:00\" }]}";
		
		HrmOrgServiceImpl hrmOrgServiceImpl=new HrmOrgServiceImpl();
		
		hrmOrgServiceImpl.fetchHrmOrg(params);
		
	}
	
}
