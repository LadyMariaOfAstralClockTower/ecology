package com.sjc;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;
import weaver.general.TimeUtil;
import weaver.general.Util;

public class CostUtil {
	
	/**
	 * 计算两个日期之间间隔月份数量
	 * @param ksrq
	 * @param jsrq
	 * @return
	 */
	public static int getMonthCount(String ksrq,String jsrq){
		
		int totalMonth=0;
		try {
			  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		      Calendar bef = Calendar.getInstance();
		      Calendar aft = Calendar.getInstance();
		      bef.setTime(sdf.parse(jsrq));
		      aft.setTime(sdf.parse(ksrq));
		      int result = aft.get(Calendar.MONTH) - bef.get(Calendar.MONTH);
		      int month = (aft.get(Calendar.YEAR) - bef.get(Calendar.YEAR)) * 12;
		      totalMonth=Math.abs(month + result);
		      System.out.println("totalMonth:"+totalMonth); 
		} catch (Exception e) {
			  e.printStackTrace();
		}
		  
		return totalMonth;  
  }
	
	public static void addFormmodeRight(int modeid,String formtable,String createrid){
		  addFormmodeRight(modeid,formtable,0,createrid);
	  }
	  
	/**
	 * 表单建模数据增加权限
	 * @param creatorid
	 * @param modeid
	 * @param formtable
	 */
	public static void addFormmodeRight(int modeid,String formtable,int billid,String createrid){
		
		String createdate=TimeUtil.getCurrentDateString();
		String createtime=TimeUtil.getOnlyCurrentTimeString();
		
		BaseBean log=new BaseBean();
		log.writeLog("增加权限的createrid:"+createrid);
		
		RecordSet recordSet2=new RecordSet();
		if(billid==0){
			String sql="select max(id) as maxid from "+formtable+" where modedatacreater="+createrid;
			
			recordSet2.execute(sql);
			if(recordSet2.next()){
				billid=recordSet2.getInt("maxid");
			}
		}
		String sql="update "+formtable+" set formmodeid='"+modeid+"',modedatacreatertype='0',modedatacreater='"+createrid+"',modedatacreatedate='"+createdate+"',modedatacreatetime='"+createtime+"' where id="+billid;
		recordSet2.execute(sql);
		
		//构建数据权限
		ModeRightInfo ModeRightInfo = new ModeRightInfo();
		ModeRightInfo.setNewRight(true);
		ModeRightInfo.editModeDataShare(Util.getIntValue(createrid),modeid,billid);
		
	}
	
	/**
	 * 时间段拆分
	 * @param ksrq
	 * @param jsrq
	 * @return
	 */
	public static List<Map<String, String>> getDateList(String ksrq,String jsrq){
		  
		  List<Map<String, String>> dateList=new ArrayList<Map<String,String>>();
		  
		  try {
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
				
				//jsrq=TimeUtil.dateAdd(jsrq, -1); //提前一天截止
				
				int monthCount=getMonthCount(ksrq,jsrq);
				
				if(monthCount>0){
					//开始时间段
					String ksMonthEnddate=TimeUtil.getMonthEndDay(ksrq);
					Map<String, String> ksdateMap=new LinkedHashMap<String, String>();
					ksdateMap.put("begindate", ksrq);
					ksdateMap.put("enddate", ksMonthEnddate);
					
					System.out.println(ksrq+"~"+ksMonthEnddate);
					
					dateList.add(ksdateMap);
					
					Calendar bef = Calendar.getInstance();
					Calendar aft = Calendar.getInstance();
					bef.setTime(sdf.parse(ksrq));
					bef.set(bef.MONTH, bef.get(bef.MONTH)+1);
					
					aft.setTime(sdf.parse(jsrq));
					//中间月份
					while(bef.getTimeInMillis()<aft.getTimeInMillis()){
						
						String beginMonthDate=sdf2.format(bef.getTime());
						bef.set(bef.MONTH, bef.get(bef.MONTH)+1);
						bef.set(bef.DATE, bef.get(bef.DATE)-1);
						String endMonthDate=sdf2.format(bef.getTime());
						System.out.println(beginMonthDate+"~"+endMonthDate);
						
						Map<String, String> dataMap=new LinkedHashMap<String, String>();
						dataMap.put("begindate", beginMonthDate);
						dataMap.put("enddate", endMonthDate);
						
						dateList.add(dataMap);
						
						bef.set(bef.DATE, bef.get(bef.DATE)+1);
					}
					
					String jsMonthBegindate=TimeUtil.getMonthBeginDay(jsrq);
					
					Map<String, String> jsdateMap=new LinkedHashMap<String, String>();
					jsdateMap.put("begindate", jsMonthBegindate);
					jsdateMap.put("enddate", jsrq);
					
					dateList.add(jsdateMap);
					System.out.println(jsMonthBegindate+"~"+jsrq);
					
					System.out.println("ksMonthEnddate:"+ksMonthEnddate+" jsMonthBegindate:"+jsMonthBegindate);
				
				}else{
					
					Map<String, String> dataMap=new LinkedHashMap<String, String>();
					dataMap.put("begindate", ksrq);
					dataMap.put("enddate", jsrq);
					dateList.add(dataMap);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			
			return dateList;
	  }
	
	public static boolean isFullMonth(String ksrq,String jsrq){
		
		jsrq=TimeUtil.dateAdd(jsrq,1);
		
		String ksrqday=Util.TokenizerString2(ksrq, "-")[2];
		String jsrqday=Util.TokenizerString2(jsrq, "-")[2];
		
		boolean isFull=ksrqday.equals(jsrqday);
		
		return isFull;
	}
	
	public static String getPubid(String fieldid,String selectvalue){
		RecordSet recordSet=new RecordSet();
		String sql="SELECT pubid FROM workflow_SelectItem WHERE fieldid="+fieldid+" and selectvalue="+selectvalue;
		
		recordSet.execute(sql);
		String pubid="";
		if(recordSet.next())
			pubid=recordSet.getString("pubid");
		
		System.out.println("getSelectName sql:"+sql+" pubid:"+pubid);
		
		return pubid;
	}
	
}
