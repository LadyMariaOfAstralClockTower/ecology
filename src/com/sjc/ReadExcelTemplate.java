package com.sjc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import org.apache.poi.hssf.usermodel.HSSFRow;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.GCONST;
import weaver.general.TimeUtil;
import weaver.general.Util;

/**
 * 描述：利用POI读取excel模版信息
 * 
 */
public class ReadExcelTemplate extends BaseBean{

	private String rootPath =GCONST.getRootPath();//webRoot路径
	
	/**
     * 描述：设置下载响应信息
     */
    public void setResponseHeader(String title,HttpServletResponse response){  
    	
        try{
        	//response.setContentType("application/vnd.ms-excel;charset=GBK");
        	response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(title, "UTF-8"));
            //客户端不缓存
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        }catch(Exception ex){
            ex.printStackTrace();  
        }
    }
	
    /**
     * 描述：入口方法信息
     */
	public void readExcelTemplate(HttpServletResponse response){  
	        
	 try {  
		 	setResponseHeader("退休审批名册.xls",response);  
		 	//readExcelTemplateOut(response.getOutputStream());  
            response.getOutputStream().flush();  
            response.getOutputStream().close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
	 }
	
	/**
	 * 描述：操作模版Excel信息
	 */
	public void readExcelTemplate1(OutputStream os){
		
		try{
			FileInputStream in = new FileInputStream(rootPath+"fileTemplate/retirement_examine/新退休审批名册（归并后启用）.xls");
			POIFSFileSystem fs = new POIFSFileSystem(in);//POI读取输入流
			HSSFWorkbook wb = new HSSFWorkbook(fs);//代表整个excel
			HSSFSheet sheet = wb.getSheetAt(0);//得到excel里的第一个sheet表单 如有多个表单可以改变参数来获得
			HSSFRow row = sheet.getRow(5);//得到sheet表单的第几行，注意excel相当于一个二维数组，下标是从0开始的
											//这里获取的是第6行，我的模版前5行都是表头菜单
			//设置值
			HSSFCell cell = row.getCell((short)0);//姓名
			cell.setCellValue("");
			cell = row.getCell((short)1);//性别
			cell.setCellValue("男");
			cell = row.getCell((short)2);//出生年月o
			cell.setCellValue("1990-04-02");
			cell = row.getCell((short)3);//参加工作年月
			cell.setCellValue("2012-07");
			cell = row.getCell((short)4);//连续工龄
			cell.setCellValue(1);
			cell = row.getCell((short)5);//职务
			cell.setCellValue("java程序猿");
			cell = row.getCell((short)6);//退休年月
			cell.setCellValue("2033-06");
			
			wb.write(os);//在response响应中生成下载流
		}catch(Exception e){
			e.printStackTrace();//按照你们日志的设置格式，把异常添加到日志里
		}
	}
	
	public void setCellValue(HSSFRow row,int cellindex,String value){
		
		HSSFCell cell = row.getCell((short)cellindex);//单据号
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);// 设置cell中文编码；
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(value);
		
	}
	public void setCellValue(HSSFRow row,int cellindex,double value){
		
		HSSFCell cell = row.getCell((short)cellindex);//单据号
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);// 设置cell中文编码；
		cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
		cell.setCellValue(value);
		
	}
	
	public void setCellValue(HSSFRow row,int cellindex,Object value){
		
		try {
			
			HSSFCell cell = row.getCell((short)cellindex);
			cell.setEncoding(HSSFCell.ENCODING_UTF_16);
			if (value instanceof Double) {
				cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				cell.setCellValue((Double)value);
			}else if (value instanceof String) {
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue((String)value);
			}else if (value instanceof Date) { 
				cell.setCellValue((Date)value);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			writeLog(e);
		}
		
	}
	
	public String readExcelTemplateOut(){
		
		String outPath="";
		try{
			
			CostManager manager=new CostManager();
			
			FileInputStream in = new FileInputStream(rootPath+"/sjc/template/20160331112104_228.xls");
			POIFSFileSystem fs = new POIFSFileSystem(in);//POI读取输入流
			HSSFWorkbook wb = new HSSFWorkbook(fs);//代表整个excel
			
		
			
			//HSSFWorkbook wb = new HSSFWorkbook();//代表整个excel
			
			String templateid="21";
			int recordindex=0;
			int sheetindex=0;
			double hjje=0;
			
			Map<String, Object> dataMap=new HashMap<String, Object>();
			
			dataMap.put("客户编号", "SJC-001");
			dataMap.put("单据编号", "SJC-20170511-0001");
			dataMap.put("客户名称", "世界城物业管理公司");
			dataMap.put("建筑面积", 100.00);
			
			//明细记录
			List<Map<String, Object>> recordList=new ArrayList<Map<String,Object>>();
			
			RecordSet recordSet=new RecordSet();
			String sql="select * from uf_zwzx where htbh='SJCDSP2017051200030'";
			recordSet.execute(sql);
			
			int recordCount=recordSet.getCounts();
			
			while(recordSet.next()){
				
				recordindex++;
				Map<String, Object> map=new HashMap<String, Object>();
				String fymc=recordSet.getString("fymc");
				String fyqj=recordSet.getString("fyqj");
				double je=Util.getDoubleValue(recordSet.getString("je"),0);
				double ye=Util.getDoubleValue(recordSet.getString("ye"),0);
				String sm=recordSet.getString("sm");
				
				hjje+=ye;
				
				map.put("费用项目", manager.getFymc(fymc));
				map.put("费用期间", Util.getDoubleValue(manager.getFyqjMc(fyqj+""),0));
				map.put("应收费用", Util.getDoubleValue(je+""));
				map.put("未收费用", Util.getDoubleValue(ye+""));
				map.put("费用说明", sm);
				
				recordList.add(map);
				
				//每10条数据一个Sheet
				if(recordindex%10==0||recordCount==recordindex){
					
					sheetindex++;
					
					String rmbhj=""+ (hjje); 
					
					dataMap.put("人民币合计", Util.numtochinese(rmbhj));
					dataMap.put("人民币合计小写",Util.getDoubleValue(ye+""));
					dataMap.put("recordList", recordList);
					
					HSSFSheet sheet=wb.cloneSheet(0);
					
					wb.setSheetName(sheetindex,"世界城物业管理公司_"+sheetindex, HSSFWorkbook.ENCODING_UTF_16);
					
					setSheetData(sheet,templateid,dataMap);
					
					recordList.clear();
					
					hjje=0;
				}
			}
			
			wb.removeSheetAt(0); //删除模板
			
			outPath=rootPath+"sjc/excel/缴费单_"+System.currentTimeMillis()+".xls";
			File outFile=new File(outPath);
			outFile.createNewFile();
			
			FileOutputStream out = new FileOutputStream(outFile);  
			wb.write(out);
			
	        System.out.println("创建成功");
		}catch(Exception e){
			writeLog("[ReadExcelTemplate.readExcelTemplateOut] 生成Excel数据出错 error:"+e.toString());
			e.printStackTrace();//按照你们日志的设置格式，把异常添加到日志里
		}
		return outPath;
	}
	
	public Map<String, String> getTempInfo(String tempid){
		
		Map<String, String> map=new HashMap<String, String>();
		String tempName="";
		String tempdaid="";
		String sql="select * from uf_yhzh where id="+tempid;
		RecordSet recordSet=new RecordSet();
		recordSet.execute(sql);
		if(recordSet.next()){
			tempName=recordSet.getString("mbmc");
			tempdaid=recordSet.getString("mbbh");
		}
		
		map.put("tempName", tempName);
		map.put("tempdaid", tempdaid);
		
		return map;
	}
	
	/**
	 * 获取费用类型Map
	 * @param tempid
	 * @return
	 */
	public Map<String, String> getFyxmInfo(String xmid){
		
		Map<String, String> map=new HashMap<String, String>();
		String fymc="";
		String tempdaid="";
		String sql="select * from uf_fyxm where id="+xmid;
		RecordSet recordSet=new RecordSet();
		recordSet.execute(sql);
		if(recordSet.next()){
			fymc=recordSet.getString("fymc");
		}
		map.put("fymc", fymc);
		
		return map;
	}
	
	/**
	 * 获得商铺编号
	 * @param tempid
	 * @return
	 */
	public String getSpbh(String spid){
		
		String sph="";
		String sql="select * from uf_spjc where id in("+spid+")";
		RecordSet recordSet=new RecordSet();
		recordSet.execute(sql);
		while(recordSet.next()){
			sph+=","+recordSet.getString("sph");
		}
		sph=sph.length()>0?sph.substring(1):"";
		
		return sph;
	}
	
	
	//生成缴费通知单Excel
	public String createJfdExcel(List<String> htbhList,String fymc,String fyqj,String tempid){
			
			String outPath="";
			try{
				
				CostManager manager=new CostManager();
				
				Map<String, String> tempMap=getTempInfo(tempid);
				String tempName=tempMap.get("tempName");
				String tempdaid=tempMap.get("tempdaid");
				
				String tempPath=rootPath+"sjc"+File.separator+"template"+File.separator+tempName;
				
				writeLog("[ReadExcelTemplate.createJfdExcel] tempPath:"+tempPath+" tempid:"+tempid);
				
				FileInputStream in = new FileInputStream(tempPath);
				POIFSFileSystem fs = new POIFSFileSystem(in);//POI读取输入流
				HSSFWorkbook wb = new HSSFWorkbook(fs);//代表整个excel
				
				String dks=",1085,1087,1089,1091,1093,1095,1097,1099,";
				
				int sheetindex=0;
				int htbhindex=0;
				
				RecordSet recordSet=new RecordSet();
				RecordSet recordSet2=new RecordSet();
				
				Map<String, String> fyqjMap=manager.getFyqjMap(fyqj);
				String qjksrq=fyqjMap.get("qjksrq");//期间开始日期
				String qjjsrq=fyqjMap.get("qjjsrq");//期间结束日期
				
				for(String htbh:htbhList){
					
					String fymcids="";
					String sql="select * from uf_htjcxx where djbh='"+htbh+"'"; 
				
					writeLog("打印合同的SQL:"+sql);
					recordSet.execute(sql);
					recordSet.next();
					
					Map<String, Object> dataMap=new HashMap<String, Object>();
					
					String jftzd="SJC-"+tempid+"-"+System.currentTimeMillis(); //通知单编号
					String qydw=recordSet.getString("qydw");//签约单位
					String spbh=recordSet.getString("spbh");//商铺编号
					String zlmj=recordSet.getString("zlmj");//租赁面积 
					String dk=recordSet.getString("dk");//地块
					
					String wyid=recordSet.getString("wyid");//微音id，不为空时说明该合同是从微音导过来的
					
					String sph=getSpbh(spbh);
					
					if(dks.contains(","+dk+",")){
						if(!wyid.equals("")){
							sph=sph.replace("GC", "");
						}
					}
					
					dataMap.put("铺位号", sph); //商铺编号
					dataMap.put("单据编号", jftzd);
					dataMap.put("商铺名称", qydw); //签约单位
					dataMap.put("面积", zlmj); //租赁面积	
					dataMap.put("合同编号", htbh); //合同编号

					
					//明细记录
					List<Map<String, Object>> recordList=new ArrayList<Map<String,Object>>();
					
					int mainid=0; //缴费通知单主数据ID
					double hjje=0; //未收合计金额
					double wshjxx=0;//未收合计金额
					
					sql="select sum(ye) as hjje from uf_zwzx where htbh='"+htbh+"' and fymc in("+fymc+") and ye<>0 and ysrq<='"+qjjsrq+"'";
					recordSet.execute(sql);
					if(recordSet.next()){
						hjje=Util.getDoubleValue(recordSet.getString("hjje"),0);
						wshjxx=Util.getDoubleValue(hjje+"");
					}
					
					if(hjje==0||hjje<0){
						continue;
					}
					
					//应收日期<=期间结束日期
					sql="select * from uf_zwzx where htbh='"+htbh+"' and fymc in("+fymc+") and ye<>0 and ysrq<='"+qjjsrq+"' order by ksrq asc";
					
					writeLog("[ReadExcelTemplate.createJfdExcel] sql:"+sql);
					
					recordSet2.execute(sql);
					
					int recordCount=recordSet2.getCounts();
					
					//缴费通知单主表
					if(recordCount>0){
						
						sql="insert into uf_jftzd(jftzdh,qydw,pwh,mj,htbh,fyqj,wshjxx,wshjdx,dk,dyrq) values("+
							"'"+jftzd+"','"+qydw+"','"+spbh+"','"+zlmj+"','"+htbh+"','"+fyqj+"','"+wshjxx+"','"+wshjxx+"','"+dk+"','"+TimeUtil.getCurrentDateString()+"')";
						
						recordSet.execute(sql);
						
						writeLog("[ReadExcelTemplate.createJfdExcel] 缴费通知单 sql1:"+sql);
						
						sql="select max(id) as maxid from uf_jftzd where jftzdh='"+jftzd+"'";
						recordSet.execute(sql);
						
						if(recordSet.next()){
							mainid=recordSet.getInt("maxid");
						}
						
						ModeUtil.addFormmodeRight(95,mainid,"uf_jftzd");
						
						//有数据则 +1
						htbhindex++;
					}
					
					int htbhsheetindex=0;
					int recordindex=0;
					
					while(recordSet2.next()){
						
						recordindex++;
						Map<String, Object> map=new HashMap<String, Object>();
						
						String zwzxid=recordSet2.getString("id");
						String htfymxid=recordSet2.getString("htfymxid"); //合同费用明细ID
						
						String fymcTemp=recordSet2.getString("fymc");
						//String fyqjTemp=recordSet2.getString("fyqj");
						double je=Util.getDoubleValue(recordSet2.getString("je"),0);
						double ye=Util.getDoubleValue(recordSet2.getString("ye"),0);
						String sm=recordSet2.getString("sm");
						
						//Map<String, String> fyqjMapTemp=manager.getFyqjMap(fyqjTemp);
						String qjksrqTemp=recordSet2.getString("ksrq");//期间开始日期
						String qjjsrqTemp=recordSet2.getString("jsrq");//期间结束日期
						
						if(!(","+fymcids+",").contains(","+fymc+",")){
							fymcids+=","+fymc;
						}
						
						map.put("收费项目", manager.getFymc(fymcTemp));
						map.put("收费期间", qjksrqTemp+"~"+qjjsrqTemp);
						map.put("应收金额", Util.getDoubleValue(je+""));
						map.put("欠款金额", Util.getDoubleValue(ye+""));
						map.put("说明", sm);
						
						recordList.add(map);
						
						writeLog("recordindex:"+recordindex+" recordCount:"+recordCount);
						
						//每10条数据一个Sheet
						if(recordindex%10==0||recordCount==recordindex){
							
							sheetindex++;
							htbhsheetindex++;
							
							String rmbhj=""+(hjje); 
							
							dataMap.put("未收合计(大写)", Util.numtochinese(rmbhj));
							dataMap.put("未收合计(小写)",Util.getDoubleValue(hjje+""));
							dataMap.put("recordList", recordList);
							
							HSSFSheet sheet=wb.cloneSheet(0);
							
							wb.setSheetName(sheetindex,"世界城物业管理公司-"+htbhindex+"-"+htbhsheetindex, HSSFWorkbook.ENCODING_UTF_16);
							
							setSheetData(sheet,tempdaid,dataMap);
							
							recordList.clear();
							
						}
						
						//缴费通知单明细
						sql="insert uf_jftzd_dt1(mainid,zwzxid,fymc,ksrq,jsrq,ysfy,wsfy,fysm) values("+
						"'"+mainid+"','"+zwzxid+"','"+fymc+"','"+qjksrqTemp+"','"+qjjsrqTemp+"','"+je+"','"+ye+"','"+sm+"')";
						recordSet.execute(sql);
						writeLog("[ReadExcelTemplate.createJfdExcel] 缴费通知单 sql2:"+sql);
						
						sql="update uf_zwzx set dyrq='"+TimeUtil.getCurrentDateString()+"' where id="+zwzxid;
						recordSet.execute(sql);
						
						if(!"".equals(htfymxid)){
							sql="update uf_htfymx set dyrq='"+TimeUtil.getCurrentDateString()+"' where id="+htfymxid;
							recordSet.execute(sql);
						}
						
					}
					
					//将费用名称更新到 缴费单 主表
					if(recordCount>0){
						sql="update uf_jftzd set fymc='"+fymcids+"' where id="+mainid;
						recordSet.execute(sql);
					}
					
				}
				
				//避免没有数据的情况Excle出错
				if(sheetindex>0){
					wb.removeSheetAt(0); //删除模板
					
					outPath=rootPath+"/sjc/excel/缴费单_"+tempid+"_"+System.currentTimeMillis()+".xls";
					File outFile=new File(outPath);
					outFile.createNewFile();
					
					FileOutputStream out = new FileOutputStream(outFile);  
					wb.write(out);
					
					out.close();
					in.close();
					
					writeLog("[ReadExcelTemplate.createJfdExcel] filePath:"+outPath);
					
			        System.out.println("创建成功");
				}else{
					writeLog("[ReadExcelTemplate.createJfdExcel] 没有账务明细 tempid:"+tempid);
				}
				
			}catch(Exception e){
				writeLog("[ReadExcelTemplate.createJfdExcel] 生成Excel数据出错 error:"+e.toString());
				e.printStackTrace();//按照你们日志的设置格式，把异常添加到日志里
				writeLog(e);
			}
			return outPath;
	}
	
	public boolean setSheetData(HSSFSheet sheet,String templateid,Map<String, Object> dataMap){
		
		try {
			
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
			
			RecordSet recordSet=new RecordSet();
			
			//String sql="select * from uf_bbmbjl where daid="+templateid+" and cell is not null and dzlx='资料定制'";
			
			String sql="select t2.* from uf_jftzd_excel t1,uf_jftzd_excel_dt1 t2 where t1.id=t2.mainid and t1.id="+templateid+" and lx=0";
			
			writeLog("[ReadExcelTemplate.setSheetData] sql:"+sql);
			
			recordSet.execute(sql);
			
			while(recordSet.next()){
				
				int rowindex=recordSet.getInt("hs")-1;
				int cellindex=recordSet.getInt("ls")-1;
				
				String zdmc=recordSet.getString("excelmc");
				Object value=dataMap.get(zdmc);
				
				HSSFRow row = sheet.getRow(rowindex);//得到sheet表单的第几行，注意excel相当于一个二维数组，下标是从0开始的
				
				setCellValue(row,cellindex,value);
				
			}
			
			
			//设置打印日期
			//HSSFRow row = sheet.getRow(2);
			//setCellValue(row,5,new Date());
			
			//明细记录
			List<Map<String, Object>> recordList=(List<Map<String, Object>>)dataMap.get("recordList");
			
			writeLog("recordList:"+dataMap.toString());
			
			int rowindex=0;
			for(Map<String, Object> recordMap:recordList){
				
				writeLog("rowindex:"+rowindex);
				
				//sql="select * from uf_bbmbjl where daid="+templateid+" and cell is not null and dzlx='资料记录定制'";
				sql="select t2.* from uf_jftzd_excel t1,uf_jftzd_excel_dt1 t2 where t1.id=t2.mainid and t1.id="+templateid+" and lx=1";
				
				recordSet.execute(sql);
				
				while(recordSet.next()){
					
					if(rowindex==0){
						rowindex=recordSet.getInt("hs")-1;
					}
					int cellindex=recordSet.getInt("ls")-1;
					
					String zdmc=recordSet.getString("excelmc");
					Object value=recordMap.get(zdmc);
					
					HSSFRow recordrow = sheet.getRow(rowindex);//得到sheet表单的第几行，注意excel相当于一个二维数组，下标是从0开始的
					
					writeLog("zdmc:"+zdmc+" cellindex:"+cellindex+" value:"+value);
					
					setCellValue(recordrow,cellindex,value);
					
				}
				
				rowindex++;
				
			}
			
		} catch (Exception e) {
			writeLog("[ReadExcelTemplate.setSheetData] 设置Excel数据出错 error:"+e.toString());
			e.printStackTrace();
			writeLog(e);
		}
		
		return true;
		
	}
	
	
	public void download(HttpServletResponse response,String filepath) {  
        try {  
        	
            // path是指欲下载的文件的路径。  
        	//String filepath="E:/weaver/需求/世界城/报表模板/export.xls";
            File file = new File(filepath);
            // 取得文件名。  
            String filename=file.getName();
            
            // 以流的形式下载文件。  
            InputStream fis = new BufferedInputStream(new FileInputStream(file)); 
            byte[] buffer = new byte[fis.available()];  
            fis.read(buffer);  
            fis.close();  
            // 清空response  
            response.reset();  
            // 设置response的Header  
            response.addHeader("Content-Disposition", "attachment;filename="+ URLEncoder.encode(filename, "UTF-8"));  
            response.addHeader("Content-Length", "" + file.length());  
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());  
            response.setContentType("application/vnd.ms-excel;charset=gb2312");  
            toClient.write(buffer);  
            toClient.flush();  
            toClient.close();  
        } catch (Exception ex) {  
        	writeLog(ex);
            ex.printStackTrace();  
        }  
    }
	
	

	
	public static void main(String[] args) {
		
		ReadExcelTemplate template=new ReadExcelTemplate();
		//template.readExcelTemplateOut(null);
		
	}
	
}
