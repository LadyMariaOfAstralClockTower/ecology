package com.cj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;
import weaver.hrm.company.DepartmentComInfo;
import weaver.hrm.company.SubCompanyComInfo;
import weaver.hrm.resource.ResourceComInfo;

/**
 * 同步人员组织架构
 * @author GodWei
 *
 */
public class SyncOrg extends BaseBean {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		

	}
	
	
	/**
	 * 执行同步
	 */
	public void doSync(){
		
		syncCrop();//同步分公司
		syncDept();//同步部门
		syncHrm();//同步人员
		
		
		//同步之后需要清理缓存才能在前台显示
		SubCompanyComInfo sc=null;
		DepartmentComInfo dc=null;
		ResourceComInfo rc=null;
		
		try {
			sc=new SubCompanyComInfo();
			sc.removeCompanyCache();//清理分公司缓存
			dc=new DepartmentComInfo();
			dc.removeCompanyCache();//清理部门缓存
			rc=new ResourceComInfo();
			rc.removeResourceCache();//清理人员缓存
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getStackTrace().toString();
		} finally {
			sc=null;
			dc=null;
			rc=null;
		}
		
		
	}
	
	
	
	/**
	 * 同步分公司
	 */
	public void syncCrop(){
		
		writeLog("[SyncOrg.java-syncCrop()] ************* 开始同步分公司 ************* ");
		
		List<Map<String, String>> ncList=getCorp();//获取分公司列表
		
		RecordSet rs=new RecordSet();
		RecordSet rds=new RecordSet();
		RecordSet rdst=new RecordSet();
		String sql="";
		
		writeLog("[SyncOrg.java-syncCrop()] ncList:"+ncList.toString());
		
		for(int i=0;i<ncList.size();i++){
			Map<String, String> map=ncList.get(i);
			
			String code = map.get("code");
			String name = map.get("name");
			String fathercode = map.get("fathercode");
			String status = map.get("enablestatus");
			
			String fatherID="";
			
			sql="select * from HrmSubCompany where subcompanycode='"+fathercode+"'";
			rdst.execute(sql);
			if(rdst.next()){
				fatherID=rdst.getString("id");
			}else{
				writeLog("[SyncOrg.java-syncCrop()] 查询不到该分部的上级部门，该分部同步失败：code:"+code+" name:"+name+" fathercode:"+fathercode);
			}
			
			sql="select * from HrmSubCompany where subcompanycode='"+code+"'";
			rs.execute(sql);
			if(rs.next()){
				if(status.equals("2")){//状态为2则为正常，状态为3则为封存
					sql="update HrmSubCompany set canceled='0',supsubcomid='"+fatherID+"' where subcompanycode='"+code+"'";
					rds.execute(sql);
					writeLog("[SyncOrg.java-syncCrop()] 更新分公司的SQL为："+sql);
				}else if(status.equals("3")){
					sql="update HrmSubCompany set canceled='1',supsubcomid='"+fatherID+"' where subcompanycode='"+code+"'";
					rds.execute(sql);
					writeLog("[SyncOrg.java-syncCrop()] 更新分公司的SQL为："+sql);
				}else{
					writeLog("[SyncOrg.java-syncCrop()] 分公司："+code+"-"+name+" 未知状态："+status);
				}
			}else{
				if(status.equals("2")){
					sql="insert into HrmSubCompany(subcompanyname,subcompanydesc,companyid,supsubcomid,showorder,subcompanycode) values('"+name+"','"+name+"','1','5','0','"+code+"')";
					rds.execute(sql);
					writeLog("[SyncOrg.java-syncCrop()] 新增分公司的SQL为："+sql);
					
					sql="select id from HrmSubCompany where subcompanycode = '"+code+"'"; //查询新增的分部ID
					rds.executeSql(sql);
					if(rds.next()){
						String subcomid=rds.getString("id"); //分部ID
						//设置权限
						rds.executeSql("insert into leftmenuconfig (userid,infoid,visible,viewindex,resourceid,resourcetype,locked,lockedbyid,usecustomname,customname,customname_e)  select  distinct  userid,infoid,visible,viewindex," + 
								subcomid + ",2,locked,lockedbyid,usecustomname,customname,customname_e from leftmenuconfig where resourcetype=1  and resourceid=1");
						rds.executeSql("insert into mainmenuconfig (userid,infoid,visible,viewindex,resourceid,resourcetype,locked,lockedbyid,usecustomname,customname,customname_e)  select  distinct  userid,infoid,visible,viewindex," + 
								subcomid + ",2,locked,lockedbyid,usecustomname,customname,customname_e from mainmenuconfig where resourcetype=1  and resourceid=1");
					}		
				}
				
			}
			
		}
		
		writeLog("[SyncOrg.java-syncCrop()] ************* 结束同步分公司 ************* ");
		
	}
	
	
	/**
	 * 同步部门
	 */
	public void syncDept(){
		
		writeLog("[SyncOrg.java-syncDept()] ************* 开始同步部门 ************* ");
		
		List<Map<String, String>> ncList=getDept();//获取分公司列表
		
		RecordSet rs=new RecordSet();
		RecordSet rds=new RecordSet();
		String sql="";
		
		for(int i=0;i<ncList.size();i++){
			
			Map<String, String> map=ncList.get(i);
			
			String code = map.get("code");
			String name = map.get("name");
			String status = map.get("enablestatus");
			
			if(name.contains("分公司")){
				continue;
			}
			
			sql="select * from hrmDepartment where departmentcode='"+code+"'";
			rs.execute(sql);
			if(rs.next()){
				if(status.equals("2")){//状态为2则为正常，状态为3则为封存
					sql="update hrmDepartment set canceled='0',departmentname='"+name+"',departmentmark='"+name+"' where departmentcode='"+code+"'";
					rds.execute(sql);
					writeLog("[SyncOrg.java-syncDept()] 更新部门的SQL为："+sql);
				}else if(status.equals("3")){
					sql="update HrmSubCompany set canceled='1',,departmentname='"+name+"',departmentmark='"+name+"' where departmentcode='"+code+"'";
					rds.execute(sql);
					writeLog("[SyncOrg.java-syncDept()] 更新部门的SQL为："+sql);
				}else{
					writeLog("[SyncOrg.java-syncDept()] 部门："+code+"-"+name+" 未知状态："+status);
				}
			}else{
				if(status.equals("2")){
					sql="insert into hrmDepartment(departmentmark,departmentname,subcompanyid1,supdepid,showorder,canceled,departmentcode,tlevel) values('"+name+"','"+name+"','5','0','0','0','"+code+"','2')";
					rds.execute(sql);
					writeLog("[SyncOrg.java-syncDept()] 新增部门的SQL为："+sql);
				}
			}
			
		}
		
		writeLog("[SyncOrg.java-syncDept()] ************* 结束同步部门 ************* ");
	}
	
	
	/**
	 * 同步人员
	 */
	public void syncHrm(){
		
		writeLog("[SyncOrg.java-syncHrm()] ************* 开始同步人员 ************* ");
		
		List<Map<String, String>> ncList=getHrm();//获取人员列表
		
		RecordSet rs=new RecordSet();
		RecordSet rds=new RecordSet();
		RecordSet rdst=new RecordSet();
		String sql="";
		
		for(int i=0;i<ncList.size();i++){

			Map<String, String> map=ncList.get(i);
			
			String code = map.get("code");
			String name = map.get("name");
			String dept = map.get("deptcode");
			String status = map.get("enablestatus");
			
			sql="select * from HrmResource where workcode='"+code+"'";
			rs.execute(sql);
			if(rs.next()){
				String str="select * from hrmDepartment where departmentcode='"+dept+"'";
				rds.execute(str);
				if(rds.next()){
					String deptID=rds.getString("id");
					if(status.equals("2")){//状态为2则为正常，状态为3则为封存
						str="update HrmResource set lastname='"+name+"',departmentid='"+deptID+"',status='1' where workcode='"+code+"'";
						writeLog("[SyncOrg.java-syncHrm()] 更新人员的SQL："+str);
						rdst.execute(str);
					}else if(status.equals("3")){
						str="update HrmResource set lastname='"+name+"',departmentid='"+deptID+"',status='7' where workcode='"+code+"'";
						writeLog("[SyncOrg.java-syncHrm()] 更新人员的SQL："+str);
						rdst.execute(str);
					}else{
						writeLog("[SyncOrg.java-syncHrm()] 未知状态："+status+" 该人员同步失败："+name+" code:"+code);
					}
					
				}else{
					writeLog("[SyncOrg.java-syncHrm()] 该编号的部门不存在，该人员同步失败： dept："+dept+" name:"+name+" code:"+code);
				}
					
			}else{
				if(status.equals("2")){
					
					String tempName=name.replace("城建", "");
					
					sql="select * from HrmResource where loginid='"+tempName+"'";
					
					rs.execute(sql);
					if(rs.next()){
						name=name.replace("城建", "");
						name=name+"1";
					}else{
						name=name.replace("城建", "");
					}
					
					rs.executeProc("HrmResourceMaxId_Get", "");
			        rs.next();
			        int maxid = rs.getInt(1);
			        
			        //char separator = Util.getSeparator();
				    //Calendar todaycal = Calendar.getInstance();
				    //String today = Util.add0(todaycal.get(1), 4)+"-"+Util.add0(todaycal.get(2) + 1, 2)+"-"+Util.add0(todaycal.get(5), 2);
				    
					
					String str="select * from hrmDepartment where departmentcode='"+dept+"'";
					rds.execute(str);
					if(rds.next()){
						String deptID=rds.getString("id");
						//String subcompanyid=rds.getString("subcompanyid1");
						str="insert into HrmResource(id,loginid,password,lastname,departmentid,workcode,status,seclevel) values('"+maxid+"','"+name+"','E10ADC3949BA59ABBE56E057F20F883E','"+name+"','"+deptID+"','"+code+"','1','10')";
						rs.execute(str);
						writeLog("[SyncOrg.java-syncHrm()] 新增人员的SQL："+str);
			            
			            String sql_1 = "insert into HrmInfoStatus (itemid,hrmid,status) values(1," + maxid + ",1)";
			            rs.executeSql(sql_1);
			            String sql_2 = "insert into HrmInfoStatus (itemid,hrmid) values (2," + maxid + ")";
			            rs.executeSql(sql_2);
			            String sql_3 = "insert into HrmInfoStatus (itemid,hrmid) values (3," + maxid + ")";
			            rs.executeSql(sql_3);
			            String sql_10 = "insert into HrmInfoStatus (itemid,hrmid) values(10," + maxid + ")";
			            rs.executeSql(sql_10);
					}
					
				}
				
			}	
			
		}
		
		writeLog("[SyncOrg.java-syncHrm()] ************* 结束同步人员 ************* ");
		
	}
		
	
	
	/**
	 * 获取分公司
	 * @return
	 */
	public List<Map<String, String>> getCorp(){
		List<Map<String, String>> list=new ArrayList<Map<String, String>>();
		
		RecordSetDataSource rsds=new RecordSetDataSource("NC");
		
		String sql="select * from v_corp where fathercode='E00'";
		rsds.execute(sql);
		while(rsds.next()){
			Map<String, String> map=new HashMap<String, String>();
			
			map.put("name", rsds.getString("name"));
			map.put("code", rsds.getString("code"));
			map.put("fathercode", rsds.getString("fathercode"));
			map.put("enablestatus", rsds.getString("enablestate"));
			
			list.add(map);
			
		}
		
		return list;
	}
	
	/**
	 * 获取部门
	 * @return
	 */
	public List<Map<String, String>> getDept(){
		List<Map<String, String>> list=new ArrayList<Map<String, String>>();
		
		RecordSetDataSource rsds=new RecordSetDataSource("NC");
		
		String sql="select * from v_deptm where fathercode='E00'";
		rsds.execute(sql);
		while(rsds.next()){
			Map<String, String> map=new HashMap<String, String>();
			
			map.put("name", rsds.getString("name"));
			map.put("code", rsds.getString("code"));
			map.put("fathercode", rsds.getString("fathercode"));
			map.put("enablestatus", rsds.getString("enablestate"));
			
			list.add(map);
			
		}
		
		return list;
	}
	
	/**
	 * 获取人员
	 * @return
	 */
	public List<Map<String, String>> getHrm(){
		List<Map<String, String>> list=new ArrayList<Map<String, String>>();
		
		RecordSetDataSource rsds=new RecordSetDataSource("NC");
		
		String sql="select * from v_person where orgcode='E00'";
		rsds.execute(sql);
		while(rsds.next()){
			Map<String, String> map=new HashMap<String, String>();
			
			map.put("name", rsds.getString("name"));
			map.put("code", rsds.getString("code"));
			map.put("deptcode", rsds.getString("deptcode"));
			map.put("enablestatus", rsds.getString("enablestate"));
			
			list.add(map);
			
		}
		
		return list;
	}

}
