package com.cj;

import java.util.HashMap;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.hrm.city.CityComInfo;
import weaver.hrm.city.CitytwoComInfo;

/**
 * 城建建模数据同步
 * @author feng & GodWei
 *
 */
public class ModeSync extends BaseBean {
	
	/**
	 * 供应商信息同步
	 * @return
	 */
	public boolean supplierSync(){
		
		writeLog("[ModeSync.supplierSync] 同步供应商开始....");
		try {
			
			RecordSet recordSet=new RecordSet();
			
			String sql="select * from uf_gysxxgl";
			recordSet.execute(sql);
			supplierSync(recordSet);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		writeLog("[ModeSync.supplierSync] 同步供应商结束....");
		
		return true;
	}
	
	/**
	 * 具体同步供应商信息
	 * @param recordSet
	 */
	public void supplierSync(RecordSet recordSet){
		try {
		
			while(recordSet.next()){
				
				String sql="";
				
				ModeUtil ModeUtil=new ModeUtil();
				
				CityComInfo cityComInfo=new CityComInfo();
				CitytwoComInfo citytwoComInfo=new CitytwoComInfo();
				
				RecordSet recordSet2=new RecordSet();
				
				String bdocid=recordSet.getString("bdocid");
				
				String code=recordSet.getString("id");
				String name=recordSet.getString("fbfmc");//分包方名称
				String pk_country=recordSet.getString("szgj"); //所在国家
				String def1=cityComInfo.getCityname(recordSet.getString("cs"));//所在城市
				String def2=citytwoComInfo.getCityname(recordSet.getString("sq"));//所在省市区
				String def7=ModeUtil.getSelectName(8074, recordSet.getString("gysjb"));//供应商级别
				String def6=ModeUtil.getSelectName(8075, recordSet.getString("xyjb"));//信用级别
				String def5=recordSet.getString("zz"); //资质
				String def4=recordSet.getString("yj"); //业绩
				String def3=ModeUtil.getSelectName(8078, recordSet.getString("sfrk")); //是否入库
				String supstate=ModeUtil.getSelectName(12979, recordSet.getString("zt"));//状态
				
				Map<String, String> dataMap=new HashMap<String, String>();
				
				dataMap.put("code", code);
				dataMap.put("name", name);
				dataMap.put("pk_country", pk_country);
				dataMap.put("def1", def1);
				dataMap.put("def2", def2);
				dataMap.put("def7", def7);
				dataMap.put("def6", def6);
				dataMap.put("def5", def5);
				dataMap.put("def4", def4);
				dataMap.put("def3", def3);
				dataMap.put("supstate", supstate);
				
				dataMap.put("pk_org", "E00");
				dataMap.put("supprop", "0");
				dataMap.put("pk_areacl", "CN");
				dataMap.put("pk_supplierclass", "0201");
				dataMap.put("iscustomer", "N");
				dataMap.put("isfreecust", "N");
				dataMap.put("isoutcheck", "N");
				
				dataMap.put("registerfund", "0.0");
				dataMap.put("pk_country", "CN");
				dataMap.put("pk_timezone", "P0800");
				dataMap.put("pk_format", "ZH-CN");
				dataMap.put("enablestate", "2");
				dataMap.put("pk_group", "CSAID");
				
				writeLog("[ModeSync.supplierSync] dataMap:"+dataMap.toString());
				
				Map<String, String> resultMap=NCUtil.doPost("supplier", dataMap, bdocid);
				
				String successful=resultMap.get("successful");
				//String resultcode=resultMap.get("resultcode");
				//String resultdescription=resultMap.get("resultdescription");
				
				if(successful.equals("Y")&&bdocid.equals("")){
					bdocid=resultMap.get("bdocid");
					sql="update uf_gysxxgl set bdocid='"+bdocid+"' where id="+code;
					writeLog("[ModeSync.supplierSync]更新供应商bdocid的sql："+sql);
					recordSet2.execute(sql);
				}
				
				//writeLog("[ModeSync.supplierSync] successful:"+successful+" bdocid:"+bdocid+" resultcode:"+resultcode+" resultdescription:"+resultdescription);
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	/**
	 * 客户信息同步
	 * @return
	 */
	public boolean customerSync(){
		
		writeLog("[ModeSync.customerSync] 同步客户开始....");
		try {
			
			RecordSet recordSet=new RecordSet();
			
			String sql="select * from uf_khxx";
			recordSet.execute(sql);
			customerSync(recordSet);
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		writeLog("[ModeSync.customerSync] 同步客户结束....");
		
		return true;
	}
	
	/**
	 * 同步具体客户信息
	 */
	public void customerSync(RecordSet recordSet){
		try {
			while(recordSet.next()){
				
				CityComInfo cityComInfo=new CityComInfo();
				CitytwoComInfo citytwoComInfo=new CitytwoComInfo();
				
				RecordSet recordSet2=new RecordSet();
				
				String sql="";
				
				ModeUtil ModeUtil=new ModeUtil();
				
				String bdocid=recordSet.getString("bdocid");
				
				String code=recordSet.getString("id");
				String name=recordSet.getString("khmc");
				String def1=ModeUtil.getSelectName(8011, recordSet.getString("khqt")); //客户群体
				String trade=ModeUtil.getSelectName(8014, recordSet.getString("sshy"));//所属行业
				String def6=citytwoComInfo.getCityname(recordSet.getString("szssq"));//所在省市区
				String def7=cityComInfo.getCityname(recordSet.getString("szgj"));//所在城市
				String def8=ModeUtil.getSelectName(12724, recordSet.getString("ssqy"));//所属区域
				String def15=ModeUtil.getWorkCode(recordSet.getString("xsfzr"));//销售负责人
				
				Map<String, String> customerMap=new HashMap<String, String>();
				
				customerMap.put("code", code);
				customerMap.put("name", name);
				customerMap.put("def1", def1);
				customerMap.put("trade", trade);
				customerMap.put("def6", def6);
				customerMap.put("def7", def7);
				customerMap.put("def8", def8);
				customerMap.put("def15", def15);
				
				customerMap.put("pk_org", "E00");
				customerMap.put("pk_fiorg", "E00");
				customerMap.put("pk_payorg", "E00");
				customerMap.put("pk_supplierclass", "02");
				customerMap.put("shortname", "");
				customerMap.put("pk_custclass", "01");
				customerMap.put("pk_areacl", "CN");
				customerMap.put("iscustomer", "N");
				customerMap.put("isfreecust", "N");
				customerMap.put("isoutcheck", "N");
				
				customerMap.put("pk_country", "CN");
				customerMap.put("pk_format", "ZH-CN");
				customerMap.put("enablestate", "2");
				customerMap.put("pk_timezone", "P0800");
				customerMap.put("pk_group", "CSAID");	
				
				writeLog("[ModelSync.customerSync] customerMap:"+customerMap.toString());
				
				Map<String, String> resultMap=NCUtil.doPost("customer", customerMap, bdocid);
				
				String successful=resultMap.get("successful");
				//String resultcode=resultMap.get("resultcode");
				//String resultdescription=resultMap.get("resultdescription");
				
				if(successful.equals("Y")&&bdocid.equals("")){
					bdocid=resultMap.get("bdocid");
					sql="update uf_khxx set bdocid='"+bdocid+"' where id="+code;
					writeLog("[ModeSync.customerSync]更新客户bdocid的sql："+sql);
					recordSet2.execute(sql);
				}
				
				//writeLog("[ModeSync.customerSync] successful:"+successful+" bdocid:"+bdocid+" resultcode:"+resultcode+" resultdescription:"+resultdescription);
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * 同步项目
	 * @return
	 */
	public boolean projectSync(){
		
		writeLog("[ModeSync.projectSync] 同步项目开始.....");
		
		try {
			
			RecordSet rs=new RecordSet();
			
			String sql="select * from uf_xmxx";
			rs.execute(sql);
			projectSync(rs);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		writeLog("[ModeSync.projectSync] 同步项目结束.....");
		
		return true;
	}
	
	/**
	 * 同步具体项目信息
	 * @param rs
	 */
	public void projectSync(RecordSet rs){
		try {
			while(rs.next()){
				CityComInfo cityComInfo=new CityComInfo();
				RecordSet rds=new RecordSet();
				
				ModeUtil modeUtil=new ModeUtil();
				
				String sql="";
				
				String bdocid=rs.getString("bdocid");
				
				if(!bdocid.equals("")){
					writeLog("[ModelSync.projectSync] 该项目("+bdocid+")已同步过，无需再同步！");
					continue;
				}
				
				String id=rs.getString("id");
				String def3=modeUtil.getCustomerName(rs.getString("jfmc"));//甲方名称
				String project_name=rs.getString("xmmc");//项目名称
				String project_code=rs.getString("xmbh");//项目编号
				String pk_projectclass=modeUtil.getSelectName(8028, rs.getString("ywlx"));//项目类型
				String pk_duty_dept=modeUtil.getDeptCode(rs.getString("fzbm"));//负责部门
				String pk_dutier=modeUtil.getWorkCode(rs.getString("fzr"));//负责人
				String creator=modeUtil.getWorkCode(rs.getString("djr"));//登记人
				String ts=rs.getString("djsj");//登记时间
				String def1=cityComInfo.getCityname("sssf");//所属省份
				String def2=modeUtil.getSelectName(11607,rs.getString("xmfl"));//所属行业
				
				Map<String, String> dataMap=new HashMap<String, String>();
				
				dataMap.put("def3", def3);
				dataMap.put("project_name", project_name);
				dataMap.put("project_code", project_code);
				dataMap.put("pk_projectclass", pk_projectclass);
				dataMap.put("pk_duty_dept", pk_duty_dept);
				dataMap.put("pk_dutier", pk_dutier);
				dataMap.put("creator", creator);
				dataMap.put("ts", ts);
				dataMap.put("def1", def1);
				dataMap.put("def2", def2);
				
				dataMap.put("pk_org", "E00");
				dataMap.put("creationstate", "02");
				dataMap.put("pk_projectstate", "02");
				dataMap.put("bill_type", "4D10");
				dataMap.put("pk_supplierclass", "02");
				dataMap.put("begin_flag", "Y");
				dataMap.put("enablestate", "2");
				dataMap.put("ordermethod", "1");
				dataMap.put("pk_currtype", "CNY");
				dataMap.put("pk_duty_org", "E00");
				dataMap.put("tax_flag", "N");
				dataMap.put("transi_type", "4D10-01");
				dataMap.put("upload_flag", "N");
				dataMap.put("pk_group", "0001A3100000000003ZB");

				writeLog("[ModelSync.projectSync] dataMap:"+dataMap.toString());
				
				Map<String, String> resultMap=NCUtil.doPost("4D10", dataMap, bdocid);
				
				
				String successful=resultMap.get("successful");
				//String resultcode=resultMap.get("resultcode");
				//String resultdescription=resultMap.get("resultdescription");
				
				if(successful.equals("Y")&&bdocid.equals("")){
					bdocid=resultMap.get("bdocid");
					sql="update uf_xmxx set bdocid='"+bdocid+"' where id="+id;
					writeLog("[ModeSync.projectSync]更新项目bdocid的sql："+sql);
					rds.execute(sql);
				}
				
				//writeLog("[ModeSync.projectSync] successful:"+successful+" bdocid:"+bdocid+" resultcode:"+resultcode+" resultdescription:"+resultdescription);
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * 同步合同
	 * @return
	 */
	public boolean contractSync(){
		
		writeLog("[ModeSync.contractSync] 同步合同开始.....");

		try {
			
			RecordSet rs=new RecordSet();
			
			String sql="select a.*,b.szgj,b.szssq from uf_htxx a left join uf_khxx b on a.jf=b.id where a.htzt='0'";
			rs.execute(sql);
			contractSync(rs);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		writeLog("[ModeSync.contractSync] 同步合同结束.....");
		
		return true;
	}
	
	/**
	 * 同步具体合同信息
	 * @param rs
	 */
	public void contractSync(RecordSet rs){
		try {
			while(rs.next()){
				
				RecordSet rds=new RecordSet();
				
				CityComInfo cityComInfo=new CityComInfo();
				CityComInfo cityComInfo1=new CityComInfo();
				
				ModeUtil modeUtil=new ModeUtil();
				String sql="";
				
				String bdocid=rs.getString("bdocid");
				
				if(!bdocid.equals("")){
					writeLog("[ModelSync.contractSync] 该合同("+bdocid+")已同步过，无需再同步！");
					continue;
				}
				
				String id=rs.getString("id");
				
				String bill_name=rs.getString("htmc");//合同名称
				String pk_customer=modeUtil.getCustomerName(rs.getString("jf"));//甲方
				String hdef91=modeUtil.getSelectName(8046, rs.getString("ssfzjg"));//所属分支机构
				String bill_code=rs.getString("htbh");//合同编号
				String hdef81=modeUtil.getSelectName(8048, rs.getString("ywlx"));//业务类型
				String pk_por_dept_v=modeUtil.getDeptCode(rs.getString("htqdbm"));//合同签订部门
				String hdef83=modeUtil.getDeptCode(rs.getString("cjbm"));//承接部门
				String hdef65=modeUtil.getWorkCode(rs.getString("xmfzr"));//项目负责人
				String hdef35=rs.getString("xxdd");//详细地点
				String promisetime=rs.getString("qdrq")+" "+rs.getString("modedatacreatetime");//签订日期
				String mny_curr=rs.getString("htje");//合同金额
				String hdef84=rs.getString("tze");//投资额
				String hdef19=modeUtil.getSelectName(8058, rs.getString("sfygd"));//是否已归档
				String hdef85=rs.getString("gzfs");//盖章份数
				String hdef4=modeUtil.getSelectName(8063,rs.getString("sfhzxm"));//是否合作项目
				String project_name=bill_name;//项目名称
				String pk_org_v="城建设计院";
				String hdef1="中国";//所在国家
				String hdef2=cityComInfo.getCityname(rs.getString("szssq"));//所在省市区
				String hdef3=cityComInfo1.getCityname(rs.getString("szgj"));//所在城市
				
				Map<String, String> dataMap=new HashMap<String, String>();
				
				dataMap.put("bill_name",bill_name);
				dataMap.put("pk_customer",pk_customer);
				dataMap.put("hdef91", hdef91);
				dataMap.put("bill_code",bill_code);
				dataMap.put("hdef81", hdef81);
				dataMap.put("pk_por_dept_v",pk_por_dept_v);
				dataMap.put("hdef83",hdef83);
				dataMap.put("hdef65",hdef65);
				dataMap.put("hdef35",hdef35);
				dataMap.put("promisetime",promisetime);
				dataMap.put("mny_curr",mny_curr);
				dataMap.put("hdef84",hdef84);
				dataMap.put("hdef19",hdef19);
				dataMap.put("hdef85",hdef85);
				dataMap.put("hdef4",hdef4);
				dataMap.put("project_name",project_name);
				dataMap.put("pk_org_v",pk_org_v);
				dataMap.put("hdef1",hdef1);
				dataMap.put("hdef2",hdef2);
				dataMap.put("hdef3",hdef3);
				
				dataMap.put("pk_org","E00");
				dataMap.put("begin_flag","N");
				dataMap.put("bill_status","3");
				dataMap.put("bill_type","4D60");
				dataMap.put("build_flag","N");
				dataMap.put("income_flag","N");
				dataMap.put("last_v_flag","Y");
				dataMap.put("pk_currtype","CNY");
				dataMap.put("pk_group","0001A3100000000003ZB");
				dataMap.put("pk_transitype","1001A31000000007L8P0");
				dataMap.put("transi_type","4D60-Cxx-01");
				dataMap.put("pk_org_v","0001A81000000000YWTI");
				
				writeLog("[ModelSync.contractSync] dataMap:"+dataMap.toString());
				
				Map<String, String> resultMap=NCUtil.doPost("4D60", dataMap, bdocid);
				
				
				String successful=resultMap.get("successful");
				//String resultcode=resultMap.get("resultcode");
				//String resultdescription=resultMap.get("resultdescription");
				
				if(successful.equals("Y")&&bdocid.equals("")){
					bdocid=resultMap.get("bdocid");
					sql="update uf_htxx set bdocid='"+bdocid+"' where id="+id;
					writeLog("[ModeSync.contractSync]更新合同的bdocid的sql："+sql);
					rds.execute(sql);
				}
				
				//writeLog("[ModeSync.contractSync] successful:"+successful+" bdocid:"+bdocid+" resultcode:"+resultcode+" resultdescription:"+resultdescription);
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/**
	 * 同步分包合同
	 * @return
	 */
	public boolean subcontractSync(){
		
		writeLog("[ModeSync.subcontractSync] 分包同步合同开始.....");
		
		try{
			
			RecordSet rs=new RecordSet();
			
			String sql="select * from uf_fbht";
			rs.execute(sql);
			subcontractSync(rs);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		writeLog("[ModeSync.subcontractSync] 分包同步合同结束.....");
		
		return true;
	}
	
	/**
	 * 同步具体分包合同
	 * @return
	 */
	public void subcontractSync(RecordSet rs){
		
		try {
			while(rs.next()){
				
				RecordSet rds=new RecordSet();
				
				ModeUtil modeUtil=new ModeUtil();
				
				String sql="";
				
				String bdocid=rs.getString("bdocid");
				
				if(!bdocid.equals("")){
					writeLog("[ModelSync.subcontractSync] 该分包合同("+bdocid+")已同步过，无需再同步！");
					continue;
				}
				
				String id=rs.getString("id");
				
				String bill_name=rs.getString("htmc");//合同名称
				String bill_code=rs.getString("htbh");//合同编号
				String pk_pro_dept_v=modeUtil.getDeptCode(rs.getString("fbbm"));//分包部门
				String pk_supplier=modeUtil.getSupplierName(rs.getString("fbf"));//分包方
				String hdef2=modeUtil.getSelectName(8087, rs.getString("sfglzht"));//是否关联主合同
				String hdef19=modeUtil.getSelectName(8088, rs.getString("htzt"));//合同状态
				String ncurrent_mny=rs.getString("htje");//合同金额
				String promisetime=rs.getString("qyrq")+" "+rs.getString("modedatacreatetime");//签约日期
				String hdef16=rs.getString("gzfs");//盖章份数
				String hdef15=modeUtil.getSelectName(8092, rs.getString("sfygd"));//是否已归档
				String hdef13=modeUtil.getMultiDeptCode(rs.getString("lxbm"));//练习部门
				String bz=rs.getString("bz");//备注
				String htje=rs.getString("htje");//合同金额
				
				Map<String, String> dataMap=new HashMap<String, String>();
				
				dataMap.put("bill_name", bill_name);
				dataMap.put("bill_code", bill_code);
				dataMap.put("pk_pro_dept_v", pk_pro_dept_v);
				dataMap.put("pk_supplier", pk_supplier);
				dataMap.put("hdef2", hdef2);
				dataMap.put("hdef19", hdef19);
				dataMap.put("ncurrent_mny", ncurrent_mny);
				dataMap.put("promisetime", promisetime);
				dataMap.put("hdef16", hdef16);
				dataMap.put("hdef15", hdef15);
				dataMap.put("hdef13", hdef13);
				dataMap.put("bz", bz);
				
				
				dataMap.put("pk_org","E00");
				dataMap.put("bill_type","4D42");
				dataMap.put("pk_currtype","CNY");
				dataMap.put("pk_group","0001A3100000000003ZB");
				dataMap.put("ncurrent_mny", htje);
				dataMap.put("orig_current_mny", htje);
				dataMap.put("curr_mny_group", htje);
				dataMap.put("curr_mny_global", htje);
				
				
				writeLog("[ModelSync.subcontractSync] dataMap:"+dataMap.toString());
				
				Map<String, String> resultMap=NCUtil.doPost("4D42", dataMap, bdocid);
				
				String successful=resultMap.get("successful");
				//String resultcode=resultMap.get("resultcode");
				//String resultdescription=resultMap.get("resultdescription");
				
				if(successful.equals("Y")&&bdocid.equals("")){
					bdocid=resultMap.get("bdocid");
					sql="update uf_fbht set bdocid='"+bdocid+"' where id="+id;
					writeLog("[ModeSync.subcontractSync]更新分包合同的bdocid的sql:"+sql);
					rds.execute(sql);
				}
				
				//writeLog("[ModeSync.subcontractSync] successful:"+successful+" bdocid:"+bdocid+" resultcode:"+resultcode+" resultdescription:"+resultdescription);
				
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
	
	public static void main(String[] args) {
		
		//ModeSync modelSync=new ModeSync();
		//modelSync.customerSync();
		try {
			//String xmlPath="D:/NCXmlFile/供应商.xml";
			//Map<String, String> resultMap=NCUtil.doSync(xmlPath);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
}
