package com.sjc;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.TimeUtil;
import weaver.general.Util;

public class CostManager extends BaseBean {
	
	/**
	 * 流程退回时删除之前的收费设置
	 * @param requestid
	 * @param wfid
	 
	public void delSfsz(String requestid,String wfid){
		String formtable = DataUtil.getFormtable(wfid);
		RecordSet rs=new RecordSet();
		RecordSet rds=new RecordSet();
		
		String sql="";
		
		sql="insert into uf_htsfszbak select * from uf_htsfsz where xglc="+requestid;
		rs.execute(sql);
		
		sql="select * from uf_htsfsz where xglc="+requestid;
		rs.execute(sql);
		while(rs.next()){
			
			String sfszid=rs.getString("id");
			String mxid=rs.getString("mxid");
			
			String sql1="delete from uf_htfymx where sfszid='"+sfszid+"' and je=ye"; //删除该收费设置 金额=余额 的收费设置明细
			boolean f1=rds.execute(sql1);
			writeLog("[CostManager.delSfsz] 删除合同费用明细中的收费设置的SQL："+sql1+" 执行结果："+f1);
			String sql2="delete from uf_zwzx where fyszid='"+sfszid+"' and je=ye and htfymxid>0 and htfymxid is not null";   //删除该收费设置 金额=余额 的账务中心明细
			boolean f2=rds.execute(sql2);
			writeLog("[CostManager.delSfsz] 删除账务中心明细中的收费设置的SQL："+sql2+" 执行结果："+f2);
			
			String sql3="select * from uf_htfymx where sfszid='"+sfszid+"'";
			rds.execute(sql3);
			writeLog("[CostManager.delSfsz] 查询的SQL："+sql3+" 执行结果："+rds.getCounts());
			if(!rds.next()){
				String sql4="";
				if(wfid.equals("179")){//合同费用批量增加
					sql4="select * from "+formtable+"_dt1 where id="+mxid;
				}else if(wfid.equals("123")||wfid.equals("150")){//合同签订&合同续签
					sql4="select * from "+formtable+"_dt3 where id="+mxid;
				}else if(wfid.equals("142")||wfid.equals("143")){//合同终止&合同变更
					sql4="select * from "+formtable+"_dt2 where id="+mxid;
				}
				writeLog("[CostManager.delSfsz] 查询的SQL："+sql4+" 执行结果："+rds.getCounts());
				rds.execute(sql4);
				if(!rds.next()){
					if(wfid.equals("142")||wfid.equals("143")){
						String sql6="select * from "+formtable+"_dt1 where id="+mxid;
						rds.execute(sql6);
						if(!rds.next()){
							String sql5="delete from uf_htsfsz where id="+sfszid;
							writeLog("[CostManager.delSfsz] 删除收费设置的SQL："+sql5+" 执行结果："+rds.getCounts());
							rds.execute(sql5);
						}
					}else{
						String sql5="delete from uf_htsfsz where id="+sfszid;
						writeLog("[CostManager.delSfsz] 删除收费设置的SQL："+sql5+" 执行结果："+rds.getCounts());
						rds.execute(sql5);
					}

				}
				
			}
			
		}
		
	}
	
	*/
	
	
	/**
	 * 流程退回时删除之前的收费设置
	 * @param requestid
	 * @param wfid
	 */
	public void delSfsz(String requestid,String wfid){
		String formtable = DataUtil.getFormtable(wfid);
		RecordSet rs=new RecordSet();
		RecordSet rds=new RecordSet();
		
		String sql="";
		
		sql="insert into uf_htsfszbak select * from uf_htsfsz where xglc="+requestid;
		rs.execute(sql);
		
		sql="select * from uf_htsfsz where xglc="+requestid;
		rs.execute(sql);
		while(rs.next()){
			
			String sfszid=rs.getString("id");
			String mxid=rs.getString("mxid");
			
			String sql4="";
			
			if(wfid.equals("179")){//合同费用批量增加
				sql4="select * from "+formtable+"_dt1 where id="+mxid;
			}else if(wfid.equals("123")||wfid.equals("150")){//合同签订&合同续签
				sql4="select * from "+formtable+"_dt3 where id="+mxid;
			}else if(wfid.equals("142")||wfid.equals("143")){//合同终止&合同变更
				sql4="select * from "+formtable+"_dt2 where id="+mxid;
			}
			boolean mxFlag=true;
			writeLog("[CostManager.delSfsz] 查询主表的SQL："+sql4+" 执行结果："+rds.getCounts());
			rds.execute(sql4);
			if(!rds.next()){
				if(wfid.equals("142")||wfid.equals("143")){
					String sql6="select * from "+formtable+"_dt1 where id="+mxid;
					rds.execute(sql6);
					if(!rds.next()){
						mxFlag=false;
					}
				}else{
					mxFlag=false;
				}
			}
			
			writeLog("[CostManager.delSfsz] 明细表是否存在："+mxFlag);
			
			if(!mxFlag){
				String sql3="select * from uf_htfymx where je<>ye and sfszid='"+sfszid+"'";
				writeLog("[CostManager.delSfsz] 查询合同费用明细的SQL："+sql3);
				rds.execute(sql3);
				if(!rds.next()){
					String sql2="select * from uf_zwzx where fyszid='"+sfszid+"' and je<>ye and htfymxid>0 and htfymxid is not null";
					writeLog("[CostManager.delSfsz] 查询账务中心的SQL："+sql2);
					rds.execute(sql2);
					if(!rds.next()){
						String delSql="delete from uf_zwzx where fyszid='"+sfszid+"' and je=ye and htfymxid>0 and htfymxid is not null";
						writeLog("[CostManager.delSfsz] 删除账务中心的SQL："+delSql);
						rds.execute(delSql);
						delSql="delete from uf_htfymx where je=ye and sfszid='"+sfszid+"'";
						writeLog("[CostManager.delSfsz] 删除合同费用明细的SQL："+delSql);
						rds.execute(delSql);
						delSql="delete from uf_htsfsz where id="+sfszid;
						writeLog("[CostManager.delSfsz] 删除收费设置的SQL："+delSql);
						rds.execute(delSql);
					}
				}
			}
			
		}
		
	}

	/**
	 * 根据合同编号和应收日期的费用设置明细转入 账务中心
	 */
	public void delZwzx(){
		writeLog("************************ 删除多余账务中心开始 ************************");
		String sql="DELETE  uf_zwzx where id in(select ID from uf_zwzx where isnull(htfymxid,0)>0  and je=ye and isnull(htfymxid,0) not in (select id from uf_htfymx))";
		RecordSet rs = new RecordSet();
		writeLog("执行删除的SQL："+sql);
		rs.execute(sql);
		writeLog("************************ 删除多余账务中心开始 ************************");
	}
	
	
	/**
	 * 根据合同编号和应收日期的费用设置明细转入 账务中心
	 * @param htbh 合同编号
	 * @param ysqj 应收期间
	 * @param createrid 创建人ID
	 */
	public void setZwmxByHtbh(String htbh,String ysqj,String createrid){
		writeLog("正在生成账务的合同编号为："+htbh+" 应收期间为："+ysqj);
		setzwmx(htbh,"",ysqj,createrid,1);

	}
	
	
	/**
	 * 根据合同编号将最小应收日期后一年的的费用设置明细转入 账务中心
	 * @param htbh 合同编号
	 */
	public void setZwmxByHtbh(String htbh,String createrid){
		
		String ysrq=TimeUtil.getCurrentDateString();
		/*
		RecordSet recordSet=new RecordSet();
		String sql="select min(ysrq) as ysrq from uf_htfymx where htbh='"+htbh+"'";
		recordSet.execute(sql);
		
		writeLog("[CostManager.setZwmxByHtbh] sql:"+sql);
		
		if(recordSet.next()){
			ysrq=recordSet.getString("ysrq");
		}
		*/
		setzwmx(htbh,"",TimeUtil.dateAdd(ysrq, 365),createrid,0);
		
	}
	
	/**
	 * 应收日期 期间 设置账务中心明细
	 * @param htbh 合同编号
	 * @param ksrq 开始日期
	 * @param jsrq 结束如期
	 * @param flag 是否为定时任务 0为是，1为否
	 */
	public void setzwmx(String htbh,String ksrq,String jsrq,String createrid,int flag){
		
		RecordSet recordSet=new RecordSet();
		RecordSet recordSet1=new RecordSet();
		RecordSet rs=new RecordSet();
		
		writeLog("是否更新账务中心："+flag);
		
		String sql="";
		if(flag==0){
			//sql="select t1.* from uf_htfymx t1 left join uf_zwzx t2 on t1.id=t2.htfymxid where t1.je=t1.ye and t1.ysrq<='"+jsrq+"'";
			sql="select t1.* from uf_htfymx t1 where t1.je=t1.ye and t1.ysrq<='"+jsrq+"'";
		}else{
			sql="select t1.* from (select * from uf_htfymx where htbh='"+htbh+"') t1 left join uf_zwzx t2 on t1.id=t2.htfymxid where t1.je=t1.ye and t1.ysrq<='"+jsrq+"' and t2.htfymxid is null";
		}
		
		
		if(!ksrq.equals("")){
			sql+=" and t1.ysrq>='"+ksrq+"'";
		}
		
		if(!htbh.equals("")){
			sql+=" and t1.htbh='"+htbh+"'";
		}
		
		writeLog("[CostManager.setzwmx] sql:"+sql);
		
		recordSet.execute(sql);
		while(recordSet.next()){
			if(createrid.equals("")){
				createrid=recordSet.getString("modedatacreater");
			}
			setzwmx(recordSet,recordSet1,createrid,flag);
		}
		
		writeLog("*****************************设置账务明细结束****************************");
	}
	
	/**
	 * 将合同费用设置明细 转入 账务中心明细
	 * @param userid
	 * @param ksrq
	 * @param jsrq
	 * @param flag 需要更新是0，不需要更新是1
	 */
	public void setzwmx(RecordSet recordSet,RecordSet recordSet1,String createrid,int flag){
		
		String[] fieldNames=new String[]{"modedatacreater","htbh","fymc","ksrq","jsrq","je","ye","sm","ysrq","dk","pp","qymc","spbh"};
		
		String htfymxid=recordSet.getString("id");
		String sfszid=recordSet.getString("sfszid"); //收费设置ID
		
		String sql="select * from uf_zwzx where htfymxid="+htfymxid;
		writeLog("查询账务中心的SQL:"+sql);
		recordSet1.execute(sql);
		if(recordSet1.next()){
			if(flag==0){
				String dataid=recordSet1.getString("id");
				double je=recordSet1.getDouble("je");
				double ye=recordSet1.getDouble("ye");
				if(je==ye){
					DataUtil.updateData("uf_zwzx",fieldNames,recordSet,dataid);
				}
			}
		}else{
			//writeLog("新增的合同费用明细id:"+htfymxid);
			
			String htbh=recordSet.getString("htbh");
			String fymc=recordSet.getString("fymc");
			String ksrq=recordSet.getString("ksrq");
			String jsrq=recordSet.getString("jsrq");
			String je=recordSet.getString("je");
			String ye=recordSet.getString("ye");
			String sm=recordSet.getString("sm");
			String ysrq=recordSet.getString("ysrq");
			String dk=recordSet.getString("dk");
			String pp=recordSet.getString("pp");
			String qymc=recordSet.getString("qymc");
			String spbh=recordSet.getString("spbh");
			
			//int dataid=DataUtil.insertdata("uf_zwzx",fieldNames,"modedatacreater",recordSet); 
			String year=Util.TokenizerString2(ysrq, "-")[0];
			String month=Util.TokenizerString2(ysrq, "-")[1];
			String fyqj=getFyqj(year,month);
			
			sql="insert into uf_zwzx(htbh,fymc,ksrq,jsrq,je,ye,sm,ysrq,dk,pp,qymc,spbh,htfymxid,fyqj,fyszid,modedatacreater)"+
			    "values('"+htbh+"','"+fymc+"','"+ksrq+"','"+jsrq+"','"+je+"','"+je+"','"+sm+"','"+ysrq+"','"+dk+"','"+pp+"','"+qymc+"','"+spbh+"','"+htfymxid+"','"+fyqj+"','"+sfszid+"','"+createrid+"')";
			
			//sql="update uf_zwzx set ye=je,htfymxid="+htfymxid+",fyqj='"+fyqj+"',fyszid='"+sfszid+"' where id="+dataid;
			
			writeLog("[CostManager.setzwmx] 账务中心新增 sql:"+sql);
			
			recordSet1.execute(sql);
			CostUtil.addFormmodeRight(75,"uf_zwzx",createrid);
		}
		
	}
	
	/**
	 * 定时根据当前日期向后推一年或者根据期间设置账务中心明细
	 * @param userid
	 * @param fyqj
	 */
	public void setzw(String createrid,String fyqj){
		
		writeLog("*****************************设置账务明细开始****************************");
		writeLog("费用期间为："+fyqj);
		RecordSet recordSet=new RecordSet();
		
		String qjksrq="";
		String qjjsrq=TimeUtil.dateAdd(TimeUtil.getCurrentDateString(), 365);
		
		//根据费用期间设置账务
		if(!fyqj.equals("")){
			String sql="select * from uf_fyqj where id="+fyqj;
			writeLog("查询费用期间的SQL："+sql);
			recordSet.execute(sql);
			if(recordSet.next()){
				qjksrq=recordSet.getString("qjksrq");
				qjjsrq=recordSet.getString("qjjsrq");
			}
		}
		setzwmx("",qjksrq,qjjsrq,createrid,0);
		
	}
	
	/**
	   * 新增合同费用明细
	   * @param requestid
	   * @param djbh
	   */
	public void addHtfymx(String djbh,String createrid)
	  {
		RecordSet recordSet = new RecordSet();
		
		String sql = "select * from uf_htsfsz where htbh='" + djbh+"'";
	    recordSet.execute(sql);
		
	    writeLog("[CostManager.addHtfymx] sql:"+sql+" count:"+recordSet.getCounts());
	    
	    while(recordSet.next()){
	    	setHtfymx(recordSet,djbh,createrid);
	    }
	    
	  }
	
	/**
	   * 新增合同费用明细
	   * @param requestid
	   * @param djbh
	   */
	public void addHtfymx(String requestid, String djbh,String createrid)
	  {
		RecordSet recordSet = new RecordSet();
		
		String sql = "select * from uf_htsfsz where xglc=" + requestid;
	    recordSet.execute(sql);
		
	    writeLog("[CostManager.addHtfymx] sql:"+sql+" count:"+recordSet.getCounts());
	    
	    while(recordSet.next()){
	    	setHtfymx(recordSet,djbh,createrid);
	    }
	    
	  }
	
	/**
	   * 多新增合同费用明细
	   * @param requestid
	   * @param djbh
	   */
	public void addMulHtfymx(String requestid, String djbh,String createrid)
	  {
		RecordSet recordSet = new RecordSet();
		
		String sql = "select * from uf_htsfsz where xglc=" + requestid+" and htbh='"+djbh+"'";
	    recordSet.execute(sql);
		
	    writeLog("[CostManager.addHtfymx] sql:"+sql+" count:"+recordSet.getCounts());
	    
	    while(recordSet.next()){
	    	setHtfymx(recordSet,djbh,createrid);
	    }
	    
	  }
	
	/**
	 * 根据应收年月获取费用期间ID
	 * @param year
	 * @param month
	 * @return
	 */
	public String getFyqj(String year,String month){
		
		String fyqj="";
		if(!month.equals("10")){
			month=month.replace("0", "");
		}
		
		RecordSet recordSet=new RecordSet();
		String sql="select id from uf_fyqj where nf='"+year+"' and yf='"+month+"'";
		writeLog("查询应收期间的SQL："+sql);
		recordSet.execute(sql);
		if(recordSet.next()){
			fyqj=recordSet.getString("id");
		}
		
		return fyqj;
	}
	
	/**
	 * 根据ID获取费用期间名称
	 * @param fyqjid
	 * @return
	 */
	public String getFyqjMc(String fyqjid){
		
		String qjmc="";
		RecordSet recordSet=new RecordSet();
		String sql="select qjmc from uf_fyqj where id="+fyqjid;
		recordSet.execute(sql);
		if(recordSet.next()){
			qjmc=recordSet.getString("qjmc");
		}
		
		return qjmc;
	}
	
	/**
	 * 根据ID获取费用期间名称
	 * @param fyqjid
	 * @return
	 */
	public Map<String, String> getFyqjMap(String fyqjid){
		
		Map<String, String> map=new HashMap<String, String>();
		
		RecordSet recordSet=new RecordSet();
		String sql="select * from uf_fyqj where id="+fyqjid;
		recordSet.execute(sql);
		if(recordSet.next()){
			map.put("qjmc", recordSet.getString("qjmc"));
			map.put("qjksrq", recordSet.getString("qjksrq"));
			map.put("qjjsrq", recordSet.getString("qjjsrq"));
			map.put("nf", recordSet.getString("nf"));
			map.put("yf", recordSet.getString("yf"));
		}
		
		return map;
	}
	
	/**
	 * 根据ID获取费用期间名称
	 * @param fyqjid
	 * @return
	 */
	public String getFymc(String fyxmid){
		
		String fymc="";
		RecordSet recordSet=new RecordSet();
		String sql="select fymc from uf_fyxm where id="+fyxmid;
		recordSet.execute(sql);
		if(recordSet.next()){
			fymc=recordSet.getString("fymc");
		}
		
		return fymc;
	}
	
	/**
	 * 设置合同费用明细
	 * @param recordSet
	 * @param djbh
	 */
	public void setHtfymx(RecordSet recordSet,String djbh,String createrid){
		
		RecordSet recordSet2=new RecordSet();
		
		int sfszid = Util.getIntValue(recordSet.getString("sfszid"));
		if(sfszid==-1){
			sfszid=recordSet.getInt("id");
		}
		
	    String fymc = recordSet.getString("fymc");
	    String jflx = recordSet.getString("jflx");
	    String sflx = recordSet.getString("sflx");
	    String ksrq = recordSet.getString("ksrq");
	    String jsrq = recordSet.getString("jsrq");
	    double je = Util.getDoubleValue(recordSet.getString("je"));
	    //writeLog("转换前的金额："+recordSet.getString("je")+" 转换后的金额："+je);
	    String sm = recordSet.getString("sm");
	    String dk = recordSet.getString("dk");
	    
	    String qymc=""; //签约名称
	    String pp=""; //品牌
	    String qyrq=""; //签约日期
	    String spbh="";//商铺编号
	    
	    String sql="select * from uf_htjcxx where djbh='"+djbh+"'";
	    recordSet2.execute(sql);
	    if(recordSet2.next()){
	    	qymc=recordSet2.getString("qymc");
	    	pp=recordSet2.getString("ppmcxz");
	    	qyrq=recordSet2.getString("qyrq");
	    	spbh=recordSet2.getString("spbh");
	    }
	    
	    boolean isFullMonth = CostUtil.isFullMonth(ksrq, jsrq);

	    List<Map<String, String>> dateList = CostUtil.getDateList(ksrq, jsrq);
	    
	    writeLog("dateList:" + dateList.toString());
	    
	    if(TimeUtil.dateInterval(ksrq, jsrq)>=0){
		    //按月
		    if (jflx.equals("0"))
		    {
		      double firstMonthje = 0F;
	
		      for (Map<String, String> map:dateList) { 
		    	  
		    	int dayInterval;
	
		        String begindate = (String)map.get("begindate");
		        String enddate = (String)map.get("enddate");
	
		        String monthBegindate = TimeUtil.getMonthBeginDay(begindate);
		        String monthEnddate = TimeUtil.getMonthEndDay(begindate);
	
		        int monthCount = CostUtil.getMonthCount(ksrq, jsrq);
	
		        if(monthCount==0&&isFullMonth){
		        	monthCount=1;
		        }
	
		        double fyje = 0d;
		        double lastMonthje = 0d;
		        String sfje="";
	
		        if (monthCount > 0)
		        {
		          if ((begindate.equals(monthBegindate)) && (enddate.equals(monthEnddate))) {
		            sfje = ""+je;
		            //writeLog("收费金额:"+sfje+" 金额:"+je);
		          } else if (enddate.equals(monthEnddate)) {
		            dayInterval = TimeUtil.dateInterval(begindate, enddate) + 1;
		            firstMonthje = Util.getDoubleValue(DataUtil.getFormateNum(je*12.0/365.0)) * dayInterval;
		            fyje = firstMonthje;
		            sfje=""+new BigDecimal(fyje).setScale(0, BigDecimal.ROUND_HALF_UP); //四舍五入后收费金额
		          } else {
		            if (isFullMonth) {
		              lastMonthje = je - firstMonthje;
		            } else {
		              dayInterval = TimeUtil.dateInterval(begindate, enddate) + 1;
		              lastMonthje = Util.getDoubleValue(DataUtil.getFormateNum(je*12.0/365.0)) * dayInterval;
		            }
		            fyje = lastMonthje;
		            sfje=""+new BigDecimal(fyje).setScale(0, BigDecimal.ROUND_HALF_UP); //四舍五入后收费金额
		          }
		        }
		        else
		        {
		          dayInterval = TimeUtil.dateInterval(begindate, enddate) + 1;
		          fyje = Util.getDoubleValue(DataUtil.getFormateNum(je*12.0/365.0))* dayInterval;
		          sfje=""+new BigDecimal(fyje).setScale(0, BigDecimal.ROUND_HALF_UP); //四舍五入后收费金额
		        }
	
		        String ysrq =getYsrq(begindate);//应收日期
		        
		        sql="select * from uf_htfymx where sfszid="+sfszid+" and ksrq='"+begindate+"' and jsrq='"+enddate+"'";
		        writeLog("查询是否重复的sql："+sql+"   开始日期:"+ksrq+"  结束日期:"+jsrq);
		    	recordSet2.execute(sql);
		    	if(recordSet2.next()){
		    		
		    		writeLog("[CostManager.setHtfymx] 收费明细已经存在 sql:"+sql);
		    		
		    		String dataid=recordSet2.getString("id");
		    		double tempje=recordSet2.getDouble("je"); //金额
		    		double tempye=recordSet2.getDouble("ye"); //余额
		    		if(tempje==tempye){ //金额 等于 余额 表示还未收费
		    			sql="update uf_htfymx set fymc='"+fymc+"',je='"+sfje+"',ysrq='"+ysrq+"',sm='"+sm+"',ye='"+sfje+"',htbh='"+djbh+"' where id="+dataid;
		    			writeLog("已存在的更新的SQL："+sql);
			        	recordSet2.execute(sql);
		    		}
		    	}else{
		        
		        	sql ="insert into uf_htfymx(modedatacreater,htbh,fymc,ksrq,jsrq,je,sm,ysrq,sfszid,ye,qymc,pp,qyrq,dk,spbh)" +
		        		 "values('"+createrid+"','"+djbh+"','"+fymc+"','"+begindate+"','"+enddate+"','"+sfje+"','"+sm+"','"+ysrq+"','"+sfszid+ "','"+sfje+"','"+qymc+"','"+pp+"','"+qyrq+"','"+dk+"','"+spbh+"')";
		        	writeLog("不存在的新插入的SQL："+sql);
			        recordSet2.execute(sql);
			        
			        CostUtil.addFormmodeRight(74, "uf_htfymx",createrid);
		    	}
		        
		        writeLog("sql4:" + sql);
		        
		        //writeLog("Htfymx.createrid:"+createrid);
		        
		      }
		    }
		    else if (jflx.equals("2")) //按天
		    {
		      for (Map<String, String> map:dateList) {
	
		        String begindate = (String)map.get("begindate");
		        String enddate = (String)map.get("enddate");
		        double fyje = 0F;
		        int totalDays = TimeUtil.dateInterval(begindate, enddate) + 1;
		        fyje = je * totalDays;
	
		        String sfje=""+new BigDecimal(fyje).setScale(0, BigDecimal.ROUND_HALF_UP);
		        String ysrq =getYsrq(begindate);
	
		        sql="select * from uf_htfymx where sfszid="+sfszid+" and ksrq='"+begindate+"' and jsrq='"+enddate+"'";
		    	recordSet2.execute(sql);
		    	if(recordSet2.next()){
		    		String dataid=recordSet2.getString("id");
		    		double tempje=recordSet2.getDouble("je"); //金额
		    		double tempye=recordSet2.getDouble("ye"); //余额
		    		if(tempje==tempye){ //金额 等于 余额 表示还未收费
		    			sql="update uf_htfymx set fymc='"+fymc+"',je='"+sfje+"',ysrq='"+ysrq+"',sm='"+sm+"',ye='"+sfje+"',htbh='"+djbh+"' where id="+dataid;
			        	recordSet2.execute(sql);
		    		}
		    	}else{
		        	sql ="insert into uf_htfymx(modedatacreater,htbh,fymc,ksrq,jsrq,je,sm,ysrq,sfszid,ye,qymc,pp,qyrq,dk,spbh)" +
		        		 "values('"+createrid+"','"+djbh+"','"+fymc+"','"+begindate+"','"+enddate+"','"+sfje+"','"+sm+"','"+ysrq+"','"+sfszid+ "','"+sfje+"','"+qymc+"','"+pp+"','"+qyrq+"','"+dk+"','"+spbh+"')";
			        recordSet2.execute(sql);
			        
			        CostUtil.addFormmodeRight(74, "uf_htfymx",createrid);
		    	}
	
		        writeLog("sql5:" + sql);
		        //writeLog("Htfymx.createrid:"+createrid);
		        
		      }
		    }
		    else if (jflx.equals("1")) { //按次
		    	
		      double sfje = je;
		      String begindate = ksrq;
		      String enddate = jsrq;
	
		      String ysrq =getYsrq(begindate);
	
	    	  sql="select * from uf_htfymx where sfszid="+sfszid+" and ksrq='"+begindate+"' and jsrq='"+enddate+"'";
	    	  recordSet2.execute(sql);
	    	  if(recordSet2.next()){
	    		String dataid=recordSet2.getString("id");
	    		double tempje=recordSet2.getDouble("je"); //金额
	    		double tempye=recordSet2.getDouble("ye"); //余额
	    		if(tempje==tempye){ //金额 等于 余额 表示还未收费
	    			sql="update uf_htfymx set fymc='"+fymc+"',je='"+sfje+"',ysrq='"+ysrq+"',sm='"+sm+"',ye='"+sfje+"',htbh='"+djbh+"' where id="+dataid;
		        	recordSet2.execute(sql);
	    		}
	    	  }else{
	        
	        	sql ="insert into uf_htfymx(modedatacreater,htbh,fymc,ksrq,jsrq,je,sm,ysrq,sfszid,ye,qymc,pp,qyrq,dk,spbh)" +
	        		 "values('"+createrid+"','"+djbh+"','"+fymc+"','"+begindate+"','"+enddate+"','"+sfje+"','"+sm+"','"+ysrq+"','"+sfszid+ "','"+sfje+"','"+qymc+"','"+pp+"','"+qyrq+"','"+dk+"','"+spbh+"')";
		        recordSet2.execute(sql);
		        
		        CostUtil.addFormmodeRight(74, "uf_htfymx",createrid);
	    	  } 
		      writeLog("sql6:" + sql);
		      //writeLog("Htfymx.createrid:"+createrid);
		    }

	    }
	}
	
	public String getYsrq(String begindate){
		
		String ysrq = "";
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		
	    try {
	    	 Calendar calendar=Calendar.getInstance();
			 calendar.setTime(dateFormat.parse(begindate));
			 calendar.set(Calendar.MONTH,calendar.get(Calendar.MONTH));
			 calendar.set(Calendar.DAY_OF_MONTH, 15);
			 ysrq=dateFormat.format(calendar.getTime());
	      }
	      catch (Exception e)
	      {
	    	  writeLog("[CostManager.getYsrq] error:"+e.toString());
	      }
		return ysrq;
	}
	
	public void createJfd(List<String> htbhList,Map<String, String> params,HttpServletResponse response){
		
		String fyqj=params.get("fyqj");
		String fymc=params.get("fymc");
		String tempid=params.get("tempid");
		
		writeLog("[CostManager.createJfd] tempid:"+tempid+" htbhList:"+htbhList.toString());
		if(htbhList.size()>0){
			ReadExcelTemplate template=new ReadExcelTemplate();
			String filePath=template.createJfdExcel(htbhList, fymc, fyqj, tempid); //创建缴费单Excel
			if(!filePath.equals("")){
                writeLog("打印通知单的路径是："+filePath);
				ExcelImage ei = new ExcelImage();
				ei.insertImage(filePath);
				
				template.download(response,filePath); 
			}
		}
	}
	
	public String createJfd2(List<String> htbhList,Map<String, String> params,HttpServletResponse response){
		
		String fyqj=params.get("fyqj");
		String fymc=params.get("fymc");
		String tempid=params.get("tempid");
		
		String filePath="";
		
		writeLog("[CostManager.createJfd] tempid:"+tempid+" htbhList:"+htbhList.toString());
		if(htbhList.size()>0){
			ReadExcelTemplate template=new ReadExcelTemplate();
			filePath=template.createJfdExcel(htbhList, fymc, fyqj, tempid); //创建缴费单Excel
			if(!filePath.equals("")){
                writeLog("打印通知单的路径是："+filePath);
				ExcelImage ei = new ExcelImage();
				ei.insertImage(filePath);
				
				//template.download(response,filePath); 
			}
		}
		return filePath;
	}
	
	public List<String> getHtbhList(List<String> htbhList,Map<String, String> params,String userid){
		
		String dk=params.get("dk");
		String qy=params.get("qy");
		String gs=params.get("gs");
		String htbh=params.get("htbh");
		String tempid=params.get("tempid");
		
		RecordSet recordSet=new RecordSet();
		String sql="select djbh from uf_htjcxx t1 left join uf_spjc t2 "+
		           " on cast(substring(cast(t1.spbh as varchar(4000))+',',0,(charindex(',',cast(t1.spbh as varchar(4000))+','))) as int)=t2.id ";
		
		String sqlwhere=" where  ','+CAST(t1.dk as varchar(8000))+',' like '%,"+dk+",%'";
		
		if(!qy.equals("")){
			String[] qyids=Util.TokenizerString2(qy, ",");
			String sqlstr="";
			for(String qyid:qyids){
				sqlstr+=" or ','+CAST(t1.qy as varchar(8000))+',' like '%,"+qyid+",%' ";
			}
			sqlstr=sqlstr.replaceFirst("or","");
			sqlwhere+=" and ("+sqlstr+")";
		}
		if(!gs.equals("")){
			sqlwhere+=" and t1.xmmc in("+gs+")";
		}
		if(!htbh.equals("")){
			String[] htbhs=Util.TokenizerString2(htbh, ",");
			String sqlstr="";
			for(String djbh:htbhs){
				sqlstr+=" or t1.djbh='"+djbh+"'";
			}
			sqlstr=sqlstr.replaceFirst("or","");
			sqlwhere+=" and ("+sqlstr+")";
		}
		if(!userid.equals("")){
			sqlwhere+=" and ','+cast(t1.dyfzr as varchar(8000))+',' like '%,"+userid+",%'";
		}
		sql+=sqlwhere+" order by sph asc ";
		
		writeLog("[CostManager.getHtbhList] sql:"+sql);
		
		recordSet.execute(sql);
		
		while(recordSet.next()){
			String htbhTemp=recordSet.getString("djbh");
			if(!htbhList.contains(htbhTemp)&&checkHtTemp(htbhTemp,tempid)){
				htbhList.add(htbhTemp);
			}
		}
		
		writeLog("[CostManager.getHtbhList] tempid:"+tempid+" htbhList:"+htbhList.toString());
		
		return htbhList;
	}
	
	/**
	 * 检查是否为合同制定银行模板
	 * @param htbh
	 * @param tempid
	 * @return
	 */
	public boolean checkHtTemp(String htbh,String tempid){
		
		boolean flag=true;
		RecordSet recordSet=new RecordSet();
		String sql="select * from uf_htzjdyyh where htbh='"+htbh+"'";
		recordSet.execute(sql);
		while(recordSet.next()){
			flag=false;
			String sfyh=recordSet.getString("sfyh");
			if(tempid.equals(sfyh)){
				flag=true;
				break;
			}
		}
		return flag;
	}
	
   
	
	
	public static void main(String[] args) {
		CostManager costManager=new CostManager();
		String ysrq=costManager.getYsrq("2017-01-25");
		
		//System.out.println("ysrq:"+ysrq);
		
		List<Map<String, String>> dateList = CostUtil.getDateList("2017-12-01", "2017-12-31");
	    
		System.out.println("dateList:" + dateList.toString());
		
		
	}
	
	
}
