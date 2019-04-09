package com.cj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;
import weaver.general.TimeUtil;
import weaver.general.Util;

/**
 * 同步预算数据
 * @author GodWei
 *
 */
public class DeptBudget extends BaseBean {
	
	private RecordSetDataSource rsds=new RecordSetDataSource("NC");
	
	
	/**
	 * 同步预算数据总方法
	 */
	public void doSync(){
		
		writeLog("*************************************** 同步预算科目开始 *****************************************");
		
		syncSubject();
		
		writeLog("*************************************** 同步预算科目结束 *****************************************");
		
		writeLog("*************************************** 同步预算数据开始 *****************************************");
		
		List<Map<String, String>> deptList=getDeptList("A22_1");//获取销售费用
		deptList.addAll(getDeptList("A23_6"));//获取管理费用
		deptList.addAll(getDeptList("A24_1"));//获取劳务预算
		
		RecordSet rs=new RecordSet();
		
		String sql="select * from fnayearsperiods";//获取预算年限，通过年限来获取数据
		rs.execute(sql);
		while(rs.next()){
			String year=rs.getString("fnayear");
			String yearid=rs.getString("id");
			writeLog("[DeptBudget.doSync] 开始同步"+year+"年的预算数据...");
			for(Map<String, String> map:deptList){
				syncBudgetData(getData(map.get("mainTable"), map.get("deptCode"), year), map.get("id"), yearid, map.get("organizationtype"));
			}
			
		}
		
		writeLog("*************************************** 同步预算科目结束 *****************************************");
	}
	
	
	/**
	 * 同步预算科目
	 */
	public void syncSubject(){
		
		String sql="";
		String[] codes={"6601","6602","5201"};
		
		RecordSet rs=new RecordSet();
		RecordSet rds=new RecordSet();
		
		for(String baseCode:codes){
			
			int feelevel=3;//科目等级
			for(int i=6;i<14;i=i+2){
				
				sql="select * from v_account where orgcode='E00' and code like '"+baseCode+"%' and len(code)="+i;
				writeLog("[DeptBudget.syncSubject] 获取科目的sql："+sql);
				rsds.execute(sql);
				while(rsds.next()){
					String code=rsds.getString("code");//预算科目编码
					String name=Util.TokenizerStringNew(rsds.getString("dispname"), "\\")[feelevel-1];//预算科目名称
					String fatherCode=code.substring(0,i-2);
					String fatherid="";//上级科目ID
					
					String str="select * from fnabudgetfeetype where codeName='"+fatherCode+"'";
					rs.execute(str);
					if(rs.next()){
						fatherid=rs.getString("id");
					}else{
						writeLog("[DeptBudget.syncSubject] 该科目找不到上级："+code+"-"+name);
						continue;
					}
					
					str="select * from fnabudgetfeetype where codeName='"+code+"'";
					rs.execute(str);
					if(rs.next()){
						str="update fnabudgetfeetype set name='"+name+"' where codeName='"+code+"'";
						writeLog("[DeptBudget.syncSubject] 更新科目的sql："+str);
						rs.execute(str);
					}else{
						str="insert into fnabudgetfeetype(name,feeperiod,feetype,feelevel,supsubject,Archive,codeName) values('"+name+"','4','1','"+feelevel+"','"+fatherid+"','0','"+code+"')";
						writeLog("[DeptBudget.syncSubject] 新增科目的sql："+str);
						rs.execute(str);
					}
					
				}
				
				feelevel++;

			}

		}
		
		//控制是否转结，统一费控，是否可编制
		sql="update fnabudgetfeetype set budgetAutoMove='0',GROUPCTRL='1',ISEDITFEETYPE='1' where id not in (select distinct supsubject from fnabudgetfeetype)";
		writeLog("[DeptBudget.syncSubject] 更新是否转结，统一费控，是否可编制的sql："+sql);
		rs.execute(sql);
		
		
		sql="select * from fnabudgetfeetype where id not in (select distinct supsubject from fnabudgetfeetype)";
		rs.execute(sql);
		while(rs.next()){
			String groupCtrlGUID = UUID.randomUUID().toString().replaceAll("-", "");
			String isEditfeetypeGUID = UUID.randomUUID().toString().replaceAll("-", "");
			String id=rs.getString("id");
			String tempSql="update fnabudgetfeetype set groupCtrlId='"+id+"',isEditFeeTypeId='"+id+"'";
			if(rs.getString("groupCtrlGUID").equals("")){
				tempSql+=",groupCtrlGUID='"+groupCtrlGUID+"'";
			}
			if(rs.getString("isEditfeetypeGUID").equals("")){
				tempSql+=",isEditfeetypeGUID='"+isEditfeetypeGUID+"'";
			}
			
			tempSql+=" where id="+id;
			writeLog("[DeptBudget.syncSubject] 更新统一费控，是否可编制的ID和GUID的sql："+tempSql);
			rds.execute(tempSql);
		}
		
		
		//更新allsubjectids字段
		sql="select * from FnaBudgetfeeType where LEN(codeName)>4";
		rs.execute(sql);
		while(rs.next()){
			String code=rs.getString("codeName");
			String subids="1068,";
			for(int i=4;i<=code.length();i=i+2){
				String tempCode=code.substring(0,i);
				String tempSql="select * from FnaBudgetfeeType where codeName='"+tempCode+"'";
				rds.execute(tempSql);
				if(rds.next()){
					subids+=rds.getString("id")+",";
				}else{
					writeLog("[DeptBudget.syncSubject] 更新全部科目时，该科目找不到上级："+tempCode);
					break;
				}
				
			}
			String updateSql="update FnaBudgetfeeType set ALLSUPSUBJECTIDS='"+subids+"' where codeName='"+code+"'";
			writeLog("[DeptBudget.syncSubject] 更新全部科目的sql："+updateSql);
			rds.execute(updateSql);
		}
		
	}
	
	
	/**
	 * 单独同步预算数据
	 * @param adminTable
	 * @param deptid
	 * @param deptCode
	 * @param budgetperiods
	 * @param year
	 * @param organizationtype
	 */
	public void doSyncForSingle(String adminTable,String deptid,String deptCode,String budgetperiods,String year,String organizationtype){
		syncBudgetData(getData(adminTable, deptCode, year), deptid, budgetperiods, organizationtype);
	}
	
	
	/**
	 * 获取部门信息
	 * @param 视图
	 * @return
	 */
	public List<Map<String, String>> getDeptList(String mainTable){
		List<Map<String, String>> list=new ArrayList<Map<String,String>>();
		
		RecordSet rs=new RecordSet();
		
		String sql="select DISTINCT DIM_ENTITY_CODE from ";
		sql+=mainTable;
		sql+=" where DIM_ENTITY_CODE like 'E0%' and DIM_YSLT_NAME='本年预算（部门）' and DIM_VERSION_NAME='二上'";
		rsds.execute(sql);
		while(rsds.next()){
			Map<String, String> map=new HashMap<String, String>();
			String deptCode=rsds.getString("DIM_ENTITY_CODE");
			String str="select * from hrmDepartment where departmentcode='"+deptCode+"'";
			rs.execute(str);
			if(rs.next()){
				map.put("id", rs.getString("id"));
				map.put("deptCode", deptCode);
				map.put("mainTable", mainTable);
				map.put("organizationtype", "2");//2为部门
			}else{
				str="select * from hrmSubCompany where subcompanycode='"+deptCode+"'";
				rs.execute(str);
				if(rs.next()){
					map.put("id", rs.getString("id"));
					map.put("deptCode", deptCode);
					map.put("mainTable", mainTable);
					map.put("organizationtype", "1");//1为分部
				}
			}
			
			if(map.size()>0){
				list.add(map);
			}
		}
		
		writeLog("[DeptBudget.getDeptList] 获取到的部门："+list.toString());
		
		return list;
	}
	
	/**
	 * 同步预算数据
	 * @param list 需要同步的预算数据
	 * @param deptid 组织id
	 * @param budgetperiods 预算年限
	 * @param organizationtype 组织类型
	 */
	public void syncBudgetData(List<Map<String, String>> list,String deptid,String budgetperiods,String organizationtype){
		
		if(list.size()>0){
			RecordSet rs=new RecordSet();
			String sql="select * from fnabudgetinfo where organizationtype='"+organizationtype+"' and budgetorganizationid='"+deptid+"' and budgetperiods='"+budgetperiods+"'";
			String infoId="";
			rs.execute(sql);
			if(rs.next()){
				infoId=rs.getString("id");
			}else{
				sql="insert into fnabudgetinfo(budgetstatus,createrid,budgetorganizationid,organizationtype,budgetperiods,revision,status,createdate) values('0','1','"+deptid+"','"+organizationtype+"','"+budgetperiods+"','0','0','"+TimeUtil.getCurrentTimeString()+"')";
				rs.execute(sql);
				sql="select * from fnabudgetinfo where organizationtype='2' and budgetorganizationid='"+deptid+"'";
				rs.execute(sql);
				if(rs.next()){
					infoId=rs.getString("id");
				}
			}

			for(Map<String,String> map:list){
				String detailSql="select * from fnabudgetinfodetail where budgetinfoid='"+infoId+"' and budgettypeid='"+map.get("id")+"'";
				rs.execute(detailSql);
				if(rs.next()){
					detailSql="update fnabudgetinfodetail set budgetaccount='"+map.get("budget")+"' where budgetinfoid='"+infoId+"' and budgettypeid='"+map.get("id")+"'";
					writeLog("[DeptBudget.syncBudgetData] 该科目的预算已存在，执行更新操作："+detailSql);
					rs.execute(detailSql);
				}else{
					detailSql="insert into fnabudgetinfodetail(budgetinfoid,budgetperiods,budgettypeid,budgetresourceid,budgetcrmid,budgetprojectid,budgetaccount,budgetperiodslist)";
					detailSql+=" values('"+infoId+"','"+budgetperiods+"','"+map.get("id")+"','0','0','0','"+map.get("budget")+"','1')";
					writeLog("[DeptBudget.syncBudgetData] 该科目的预算不存在，执行新增操作："+detailSql);
					rs.execute(detailSql);
				}
			}
		}
		
	}
	
	
	/**
	 * 获取预算数据
	 * @param mainTable NC中的预算表
	 * @param deptCode 部门编号
	 * @return
	 */
	public List<Map<String, String>> getData(String mainTable,String deptCode,String year){
		List<Map<String, String>> list=new ArrayList<Map<String,String>>();
		String sqlBase="select * from ";
		String sqlwhere=" where dim_yslt_name='本年预算（部门）' and DIM_VERSION_NAME='二上' and DIM_YEAR_PK='"+year+"' and DIM_ENTITY_CODE='"+deptCode+"'";
		String sql=sqlBase+mainTable+sqlwhere;
		writeLog("[DeptBudget.getData] 从NC中获取数据的sql："+sql);
		rsds.execute(sql);
		while(rsds.next()){
			Map<String, String> map=new HashMap<String, String>();
			String id=getSubjectId(mainTable, rsds.getString("DIM_MEASURE_NAME"));
			if(id.equals("")){
				writeLog("[DeptBudget.getData] "+rsds.getString("DIM_MEASURE_NAME")+" - 该科目无法在OA中找到！");
				continue;
			}
			map.put("id", id);
			map.put("budget", (rsds.getDouble("budget")*10000)+"");
			list.add(map);
		}
		writeLog("[DeptBudget.getData] 获取到的数据："+list.toString());
		return list;
		
	}
	
	
	
	
	/**
	 * 获取科目id
	 * @param mainTable 通过表单来确定科目大类
	 * @param name 通过名称来确定科目
	 * @return
	 */
	public String getSubjectId(String mainTable,String name){
		
		String subject="";
		String id="";
		
		if(mainTable.equals("A22_1")){//销售费用
			subject="6601";
		}else if(mainTable.equals("A23_6")){//管理费用
			subject="6602";
		}else if(mainTable.equals("A24_1")){//劳务成本
			subject="5201";
		}
		
		if(name.equals("职工通讯补贴")){
			name="邮电通讯费";
		}else if(name.equals("职工交通补贴")){
			name="交通运输费";
		}else if(name.equals("办公费用")&&mainTable.equals("A24_1")){
			name="办公费";
		}else if(name.equals("招标服务费")&&mainTable.equals("A22_1")){
			name="招投标费用";
		}else if(name.equals("中介机构服务费")&&mainTable.equals("A22_1")){
			name="其他费用";
		}
		
		RecordSet rs=new RecordSet();
		
		String sql="select * from fnabudgetfeetype where codeName like '"+subject+"%' and name='"+name+"' and  id not in (select distinct supsubject from fnabudgetfeetype)";
		rs.execute(sql);
		if(rs.next()){
			id=rs.getString("id");
		}
		
		return id;
		
	}
	
}
