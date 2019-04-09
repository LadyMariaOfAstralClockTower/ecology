package com.sjc;

import java.math.BigDecimal;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;


public class DataUtil {

	public static int insertdata(String tableName,String[] fieldNames,String[] fieldValues){
		
		RecordSet recordSet=new RecordSet();
		
		BaseBean baseBean=new BaseBean();
		
		String insertsql="";
		String insertfield="";
		String insertvalue="";
		
		for(int i=0;i<fieldNames.length;i++){
			String fieldName=fieldNames[i];
			String filedVlue=fieldValues[i];
			insertfield+=fieldName+",";
			insertvalue+="'"+filedVlue+"',";
		}
		
		insertfield=insertfield.length()>0?insertfield.substring(0,insertfield.length()-1):"";
		insertvalue=insertvalue.length()>0?insertvalue.substring(0,insertvalue.length()-1):"";
		
		insertsql="insert into "+tableName+"("+insertfield+") values("+insertvalue+")";
		recordSet.execute(insertsql);
		
		int dataid=0;
		recordSet.execute("select max(id) as maxid from "+tableName);
		if(recordSet.next()){
			dataid=recordSet.getInt("maxid");
		}
		
		baseBean.writeLog("insertsql:"+insertsql);
		
		return dataid;
	}
	
	public static int insertdata(String tableName,String[] fieldNames,RecordSet rs){
		
		RecordSet recordSet=new RecordSet();
		
		BaseBean baseBean=new BaseBean();
		baseBean.writeLog("[DataUtil.insertdata] tableName:"+tableName+" fieldNames:"+fieldNames.toString());
		
		String insertsql="";
		String insertfield="";
		String insertvalue="";
		
		for(String fieldName:fieldNames){
			String filedVlue=rs.getString(fieldName);
			insertfield+=fieldName+",";
			insertvalue+="'"+filedVlue+"',";
		}
		
		insertfield=insertfield.length()>0?insertfield.substring(0,insertfield.length()-1):"";
		insertvalue=insertvalue.length()>0?insertvalue.substring(0,insertvalue.length()-1):"";
		
		insertsql="insert into "+tableName+"("+insertfield+") values("+insertvalue+")";
		
		baseBean.writeLog("insertSQL:"+insertsql);
		
		recordSet.execute(insertsql);
		
		int dataid=0;
		recordSet.execute("select max(id) as maxid from "+tableName);
		if(recordSet.next()){
			dataid=recordSet.getInt("maxid");
		}
		
		baseBean.writeLog("insertsql:"+insertsql);
		
		return dataid;
	}
	
	public static boolean updateData(String tableName,String[] fieldNames,RecordSet rs,String dataid){
		
		String updatesql="";
		
		RecordSet recordSet=new RecordSet();
		
		BaseBean baseBean=new BaseBean();
		baseBean.writeLog("[YdUtil.convertDetailData] tableName:"+tableName+" fieldNames:"+fieldNames.toString());
			
		String updatefield="";
		
		for(String fieldName:fieldNames){
			String filedVlue=rs.getString(fieldName);
			updatefield+=fieldName+"='"+filedVlue+"',";
		}
		
		updatefield=updatefield.length()>0?updatefield.substring(0,updatefield.length()-1):"";
		
		updatesql="update "+tableName+" set "+updatefield+" where id="+dataid;
		recordSet.execute(updatesql);
		
		baseBean.writeLog("updatesql:"+updatesql);
			
		
		return true;
	}

	public static boolean convertDetailData(String tableName,String mainid,String[] fieldNames,RecordSet rs,String keyField){
	
		String insertsql="";
		String updatesql="";
		
		RecordSet recordSet=new RecordSet();
		
		BaseBean baseBean=new BaseBean();
		baseBean.writeLog("[YdUtil.convertDetailData] tableName:"+tableName+" mainid:"+mainid+" fieldNames:"+fieldNames.toString());
			
		String keyValue=rs.getString(keyField); //对应历史数据中的明细ID
		
		String insertfield="";
		String insertvalue="";
		String updatefield="";
		
		for(String fieldName:fieldNames){
			String filedVlue=rs.getString(fieldName);
			insertfield+=fieldName+",";
			insertvalue+="'"+filedVlue+"',";
			updatefield+=fieldName+"='"+filedVlue+"',";
		}
		
		insertfield=insertfield.length()>0?insertfield.substring(0,insertfield.length()-1):"";
		insertvalue=insertvalue.length()>0?insertvalue.substring(0,insertvalue.length()-1):"";
		updatefield=updatefield.length()>0?updatefield.substring(0,updatefield.length()-1):"";
		
		insertsql="insert into "+tableName+"("+insertfield+") values("+insertvalue+")";
		updatesql="update "+tableName+" set "+updatefield+" where id="+keyValue;
		
		String sql="select * from "+tableName+" where id='"+keyValue+"'";
		recordSet.execute(sql);
		
		if(recordSet.next()){
			recordSet.execute(updatesql);
		}else {
			recordSet.execute(insertsql);
			recordSet.execute(sql);
		}
		
		baseBean.writeLog("insertsql:"+insertsql);
		baseBean.writeLog("updatesql:"+updatesql);
			
		
		return true;
	}
	
	/**
	 * 获得表单table
	 * @param workflowid 流程id
	 * @return
	 */
	public static String getFormtable(String workflowid){
		String formtable="";
		String sql="select tablename from workflow_base t1,workflow_bill t2 where t1.id="+workflowid+" and t2.id=t1.formid";
		RecordSet recordSet=new RecordSet();
		recordSet.execute(sql);
		if(recordSet.next())
			formtable=recordSet.getString("tablename");
		return formtable;
	}
	
	/**
	 * 格式化为四舍五入
	 * @param value
	 * @return
	 */
	public static String getFormateNum(double value){
		return ""+new BigDecimal(value).setScale(0, BigDecimal.ROUND_HALF_UP);
	}
	
	
}
