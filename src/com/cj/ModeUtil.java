package com.cj;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class ModeUtil {

	/**
	 * 获取表单selectname值
	 * @param fieldid
	 * @param selectvalue
	 * @return
	 */
	public String getSelectName(int fieldid,String selectvalue){
		if(selectvalue.equals("")){
			return "";
		}
		RecordSet recordSet=new RecordSet();
		String sql="SELECT selectname FROM workflow_SelectItem WHERE fieldid="+fieldid+" and selectvalue="+selectvalue;
		
		recordSet.execute(sql);
		String selectname="";
		if(recordSet.next())
			selectname=recordSet.getString("selectname");
		
		System.out.println("getSelectName sql:"+sql+" selectname:"+selectname);
		
		return selectname;
	}
	
	/**
	 * 获取人员对应的NC的编码
	 * @param resourceid
	 * @return
	 */
	public String getWorkCode(String resourceid){
		
		if(resourceid.equals("")){
			return "";
		}
		
		RecordSet recordSet=new RecordSet();
		String sql="select workcode from hrmresource where id="+resourceid;
		
		recordSet.execute(sql);
		String workcode="";
		if(recordSet.next()){
			workcode=recordSet.getString("workcode");
		}
		
		return workcode;
	}
	
	/**
	 * 获取人员姓名
	 * @param id
	 * @return
	 */
	public String getHrmName(String id){
		if(id.equals("")){
			return "";
		}
		
		RecordSet recordSet=new RecordSet();
		String sql="select lastname from hrmResource where id="+id;
		
		recordSet.execute(sql);
		String lastname="";
		if(recordSet.next()){
			lastname=recordSet.getString("lastname");
		}
		
		return lastname;
	}
	
	/**
	 * 获取部门名称
	 * @param id
	 * @return
	 */
	public String getDeptName(String id){
		if(id.equals("")){
			return "";
		}
		
		RecordSet recordSet=new RecordSet();
		String sql="select departmentname from hrmDepartment where id="+id;
		
		recordSet.execute(sql);
		String departmentName="";
		if(recordSet.next()){
			departmentName=recordSet.getString("departmentname");
		}
		
		return departmentName;
	}
	
	/**
	 * 获取部门编码
	 * @param id
	 * @return
	 */
	public String getDeptCode(String id){
		
		if(id.equals("")){
			return "";
		}
		
		RecordSet recordSet=new RecordSet();
		String sql="select departmentcode from hrmDepartment where id="+id;
		
		recordSet.execute(sql);
		String departmentcode="";
		if(recordSet.next()){
			departmentcode=recordSet.getString("departmentcode");
		}
		
		return departmentcode;
	}
	
	/**
	 * 获取多个部门
	 * @param id
	 * @return
	 */
	public String getMultiDeptCode(String id){
		String str=",";
		String[] ids=Util.TokenizerStringNew(id, ",");
		
		RecordSet recordSet=new RecordSet();
		
		for(String detpId:ids){
			String sql="select departmentcode from hrmDepartment where id="+detpId;
			recordSet.execute(sql);
			if(recordSet.next()){
				String tempDept=recordSet.getString("departmentcode");
				if(!str.contains(","+tempDept+",")){
					str+=tempDept+",";
				}
			}
		}
		
		str=str.length()>1?str.substring(1,str.length()-1):"";
		
		return str;
	}
	
	/**
	 * 获取客户名称
	 * @param id
	 * @return
	 */
	public String getCustomerName(String id){
		if(id.equals("")){
			return "";
		}
		
		RecordSet recordSet=new RecordSet();
		String sql="select khmc from uf_khxx where id="+id;
		
		recordSet.execute(sql);
		String name="";
		if(recordSet.next()){
			name=recordSet.getString("khmc");
		}
		
		return name;
		
	}
	
	
	/**
	 * 获取供应商名称
	 * @param id
	 * @return
	 */
	public String getSupplierName(String id){
		if(id.equals("")){
			return "";
		}
		
		RecordSet recordSet=new RecordSet();
		String sql="select fbfmc from uf_gysxxgl where id="+id;
		
		recordSet.execute(sql);
		String fbfmc="";
		if(recordSet.next()){
			fbfmc=recordSet.getString("fbfmc");
		}
		
		return fbfmc;
		
	}
	
}
