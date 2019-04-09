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
 * ����������POI��ȡexcelģ����Ϣ
 * 
 */
public class ReadExcelTemplate extends BaseBean{

	private String rootPath =GCONST.getRootPath();//webRoot·��
	
	/**
     * ����������������Ӧ��Ϣ
     */
    public void setResponseHeader(String title,HttpServletResponse response){  
    	
        try{
        	//response.setContentType("application/vnd.ms-excel;charset=GBK");
        	response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment;filename="+URLEncoder.encode(title, "UTF-8"));
            //�ͻ��˲�����
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
        }catch(Exception ex){
            ex.printStackTrace();  
        }
    }
	
    /**
     * ��������ڷ�����Ϣ
     */
	public void readExcelTemplate(HttpServletResponse response){  
	        
	 try {  
		 	setResponseHeader("������������.xls",response);  
		 	//readExcelTemplateOut(response.getOutputStream());  
            response.getOutputStream().flush();  
            response.getOutputStream().close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
	 }
	
	/**
	 * ����������ģ��Excel��Ϣ
	 */
	public void readExcelTemplate1(OutputStream os){
		
		try{
			FileInputStream in = new FileInputStream(rootPath+"fileTemplate/retirement_examine/�������������ᣨ�鲢�����ã�.xls");
			POIFSFileSystem fs = new POIFSFileSystem(in);//POI��ȡ������
			HSSFWorkbook wb = new HSSFWorkbook(fs);//��������excel
			HSSFSheet sheet = wb.getSheetAt(0);//�õ�excel��ĵ�һ��sheet�� ���ж�������Ըı���������
			HSSFRow row = sheet.getRow(5);//�õ�sheet���ĵڼ��У�ע��excel�൱��һ����ά���飬�±��Ǵ�0��ʼ��
											//�����ȡ���ǵ�6�У��ҵ�ģ��ǰ5�ж��Ǳ�ͷ�˵�
			//����ֵ
			HSSFCell cell = row.getCell((short)0);//����
			cell.setCellValue("");
			cell = row.getCell((short)1);//�Ա�
			cell.setCellValue("��");
			cell = row.getCell((short)2);//��������o
			cell.setCellValue("1990-04-02");
			cell = row.getCell((short)3);//�μӹ�������
			cell.setCellValue("2012-07");
			cell = row.getCell((short)4);//��������
			cell.setCellValue(1);
			cell = row.getCell((short)5);//ְ��
			cell.setCellValue("java����Գ");
			cell = row.getCell((short)6);//��������
			cell.setCellValue("2033-06");
			
			wb.write(os);//��response��Ӧ������������
		}catch(Exception e){
			e.printStackTrace();//����������־�����ø�ʽ�����쳣��ӵ���־��
		}
	}
	
	public void setCellValue(HSSFRow row,int cellindex,String value){
		
		HSSFCell cell = row.getCell((short)cellindex);//���ݺ�
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);// ����cell���ı��룻
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(value);
		
	}
	public void setCellValue(HSSFRow row,int cellindex,double value){
		
		HSSFCell cell = row.getCell((short)cellindex);//���ݺ�
		cell.setEncoding(HSSFCell.ENCODING_UTF_16);// ����cell���ı��룻
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
			POIFSFileSystem fs = new POIFSFileSystem(in);//POI��ȡ������
			HSSFWorkbook wb = new HSSFWorkbook(fs);//��������excel
			
		
			
			//HSSFWorkbook wb = new HSSFWorkbook();//��������excel
			
			String templateid="21";
			int recordindex=0;
			int sheetindex=0;
			double hjje=0;
			
			Map<String, Object> dataMap=new HashMap<String, Object>();
			
			dataMap.put("�ͻ����", "SJC-001");
			dataMap.put("���ݱ��", "SJC-20170511-0001");
			dataMap.put("�ͻ�����", "�������ҵ����˾");
			dataMap.put("�������", 100.00);
			
			//��ϸ��¼
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
				
				map.put("������Ŀ", manager.getFymc(fymc));
				map.put("�����ڼ�", Util.getDoubleValue(manager.getFyqjMc(fyqj+""),0));
				map.put("Ӧ�շ���", Util.getDoubleValue(je+""));
				map.put("δ�շ���", Util.getDoubleValue(ye+""));
				map.put("����˵��", sm);
				
				recordList.add(map);
				
				//ÿ10������һ��Sheet
				if(recordindex%10==0||recordCount==recordindex){
					
					sheetindex++;
					
					String rmbhj=""+ (hjje); 
					
					dataMap.put("����Һϼ�", Util.numtochinese(rmbhj));
					dataMap.put("����Һϼ�Сд",Util.getDoubleValue(ye+""));
					dataMap.put("recordList", recordList);
					
					HSSFSheet sheet=wb.cloneSheet(0);
					
					wb.setSheetName(sheetindex,"�������ҵ����˾_"+sheetindex, HSSFWorkbook.ENCODING_UTF_16);
					
					setSheetData(sheet,templateid,dataMap);
					
					recordList.clear();
					
					hjje=0;
				}
			}
			
			wb.removeSheetAt(0); //ɾ��ģ��
			
			outPath=rootPath+"sjc/excel/�ɷѵ�_"+System.currentTimeMillis()+".xls";
			File outFile=new File(outPath);
			outFile.createNewFile();
			
			FileOutputStream out = new FileOutputStream(outFile);  
			wb.write(out);
			
	        System.out.println("�����ɹ�");
		}catch(Exception e){
			writeLog("[ReadExcelTemplate.readExcelTemplateOut] ����Excel���ݳ��� error:"+e.toString());
			e.printStackTrace();//����������־�����ø�ʽ�����쳣��ӵ���־��
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
	 * ��ȡ��������Map
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
	 * ������̱��
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
	
	
	//���ɽɷ�֪ͨ��Excel
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
				POIFSFileSystem fs = new POIFSFileSystem(in);//POI��ȡ������
				HSSFWorkbook wb = new HSSFWorkbook(fs);//��������excel
				
				String dks=",1085,1087,1089,1091,1093,1095,1097,1099,";
				
				int sheetindex=0;
				int htbhindex=0;
				
				RecordSet recordSet=new RecordSet();
				RecordSet recordSet2=new RecordSet();
				
				Map<String, String> fyqjMap=manager.getFyqjMap(fyqj);
				String qjksrq=fyqjMap.get("qjksrq");//�ڼ俪ʼ����
				String qjjsrq=fyqjMap.get("qjjsrq");//�ڼ��������
				
				for(String htbh:htbhList){
					
					String fymcids="";
					String sql="select * from uf_htjcxx where djbh='"+htbh+"'"; 
				
					writeLog("��ӡ��ͬ��SQL:"+sql);
					recordSet.execute(sql);
					recordSet.next();
					
					Map<String, Object> dataMap=new HashMap<String, Object>();
					
					String jftzd="SJC-"+tempid+"-"+System.currentTimeMillis(); //֪ͨ�����
					String qydw=recordSet.getString("qydw");//ǩԼ��λ
					String spbh=recordSet.getString("spbh");//���̱��
					String zlmj=recordSet.getString("zlmj");//������� 
					String dk=recordSet.getString("dk");//�ؿ�
					
					String wyid=recordSet.getString("wyid");//΢��id����Ϊ��ʱ˵���ú�ͬ�Ǵ�΢����������
					
					String sph=getSpbh(spbh);
					
					if(dks.contains(","+dk+",")){
						if(!wyid.equals("")){
							sph=sph.replace("GC", "");
						}
					}
					
					dataMap.put("��λ��", sph); //���̱��
					dataMap.put("���ݱ��", jftzd);
					dataMap.put("��������", qydw); //ǩԼ��λ
					dataMap.put("���", zlmj); //�������	
					dataMap.put("��ͬ���", htbh); //��ͬ���

					
					//��ϸ��¼
					List<Map<String, Object>> recordList=new ArrayList<Map<String,Object>>();
					
					int mainid=0; //�ɷ�֪ͨ��������ID
					double hjje=0; //δ�պϼƽ��
					double wshjxx=0;//δ�պϼƽ��
					
					sql="select sum(ye) as hjje from uf_zwzx where htbh='"+htbh+"' and fymc in("+fymc+") and ye<>0 and ysrq<='"+qjjsrq+"'";
					recordSet.execute(sql);
					if(recordSet.next()){
						hjje=Util.getDoubleValue(recordSet.getString("hjje"),0);
						wshjxx=Util.getDoubleValue(hjje+"");
					}
					
					if(hjje==0||hjje<0){
						continue;
					}
					
					//Ӧ������<=�ڼ��������
					sql="select * from uf_zwzx where htbh='"+htbh+"' and fymc in("+fymc+") and ye<>0 and ysrq<='"+qjjsrq+"' order by ksrq asc";
					
					writeLog("[ReadExcelTemplate.createJfdExcel] sql:"+sql);
					
					recordSet2.execute(sql);
					
					int recordCount=recordSet2.getCounts();
					
					//�ɷ�֪ͨ������
					if(recordCount>0){
						
						sql="insert into uf_jftzd(jftzdh,qydw,pwh,mj,htbh,fyqj,wshjxx,wshjdx,dk,dyrq) values("+
							"'"+jftzd+"','"+qydw+"','"+spbh+"','"+zlmj+"','"+htbh+"','"+fyqj+"','"+wshjxx+"','"+wshjxx+"','"+dk+"','"+TimeUtil.getCurrentDateString()+"')";
						
						recordSet.execute(sql);
						
						writeLog("[ReadExcelTemplate.createJfdExcel] �ɷ�֪ͨ�� sql1:"+sql);
						
						sql="select max(id) as maxid from uf_jftzd where jftzdh='"+jftzd+"'";
						recordSet.execute(sql);
						
						if(recordSet.next()){
							mainid=recordSet.getInt("maxid");
						}
						
						ModeUtil.addFormmodeRight(95,mainid,"uf_jftzd");
						
						//�������� +1
						htbhindex++;
					}
					
					int htbhsheetindex=0;
					int recordindex=0;
					
					while(recordSet2.next()){
						
						recordindex++;
						Map<String, Object> map=new HashMap<String, Object>();
						
						String zwzxid=recordSet2.getString("id");
						String htfymxid=recordSet2.getString("htfymxid"); //��ͬ������ϸID
						
						String fymcTemp=recordSet2.getString("fymc");
						//String fyqjTemp=recordSet2.getString("fyqj");
						double je=Util.getDoubleValue(recordSet2.getString("je"),0);
						double ye=Util.getDoubleValue(recordSet2.getString("ye"),0);
						String sm=recordSet2.getString("sm");
						
						//Map<String, String> fyqjMapTemp=manager.getFyqjMap(fyqjTemp);
						String qjksrqTemp=recordSet2.getString("ksrq");//�ڼ俪ʼ����
						String qjjsrqTemp=recordSet2.getString("jsrq");//�ڼ��������
						
						if(!(","+fymcids+",").contains(","+fymc+",")){
							fymcids+=","+fymc;
						}
						
						map.put("�շ���Ŀ", manager.getFymc(fymcTemp));
						map.put("�շ��ڼ�", qjksrqTemp+"~"+qjjsrqTemp);
						map.put("Ӧ�ս��", Util.getDoubleValue(je+""));
						map.put("Ƿ����", Util.getDoubleValue(ye+""));
						map.put("˵��", sm);
						
						recordList.add(map);
						
						writeLog("recordindex:"+recordindex+" recordCount:"+recordCount);
						
						//ÿ10������һ��Sheet
						if(recordindex%10==0||recordCount==recordindex){
							
							sheetindex++;
							htbhsheetindex++;
							
							String rmbhj=""+(hjje); 
							
							dataMap.put("δ�պϼ�(��д)", Util.numtochinese(rmbhj));
							dataMap.put("δ�պϼ�(Сд)",Util.getDoubleValue(hjje+""));
							dataMap.put("recordList", recordList);
							
							HSSFSheet sheet=wb.cloneSheet(0);
							
							wb.setSheetName(sheetindex,"�������ҵ����˾-"+htbhindex+"-"+htbhsheetindex, HSSFWorkbook.ENCODING_UTF_16);
							
							setSheetData(sheet,tempdaid,dataMap);
							
							recordList.clear();
							
						}
						
						//�ɷ�֪ͨ����ϸ
						sql="insert uf_jftzd_dt1(mainid,zwzxid,fymc,ksrq,jsrq,ysfy,wsfy,fysm) values("+
						"'"+mainid+"','"+zwzxid+"','"+fymc+"','"+qjksrqTemp+"','"+qjjsrqTemp+"','"+je+"','"+ye+"','"+sm+"')";
						recordSet.execute(sql);
						writeLog("[ReadExcelTemplate.createJfdExcel] �ɷ�֪ͨ�� sql2:"+sql);
						
						sql="update uf_zwzx set dyrq='"+TimeUtil.getCurrentDateString()+"' where id="+zwzxid;
						recordSet.execute(sql);
						
						if(!"".equals(htfymxid)){
							sql="update uf_htfymx set dyrq='"+TimeUtil.getCurrentDateString()+"' where id="+htfymxid;
							recordSet.execute(sql);
						}
						
					}
					
					//���������Ƹ��µ� �ɷѵ� ����
					if(recordCount>0){
						sql="update uf_jftzd set fymc='"+fymcids+"' where id="+mainid;
						recordSet.execute(sql);
					}
					
				}
				
				//����û�����ݵ����Excle����
				if(sheetindex>0){
					wb.removeSheetAt(0); //ɾ��ģ��
					
					outPath=rootPath+"/sjc/excel/�ɷѵ�_"+tempid+"_"+System.currentTimeMillis()+".xls";
					File outFile=new File(outPath);
					outFile.createNewFile();
					
					FileOutputStream out = new FileOutputStream(outFile);  
					wb.write(out);
					
					out.close();
					in.close();
					
					writeLog("[ReadExcelTemplate.createJfdExcel] filePath:"+outPath);
					
			        System.out.println("�����ɹ�");
				}else{
					writeLog("[ReadExcelTemplate.createJfdExcel] û��������ϸ tempid:"+tempid);
				}
				
			}catch(Exception e){
				writeLog("[ReadExcelTemplate.createJfdExcel] ����Excel���ݳ��� error:"+e.toString());
				e.printStackTrace();//����������־�����ø�ʽ�����쳣��ӵ���־��
				writeLog(e);
			}
			return outPath;
	}
	
	public boolean setSheetData(HSSFSheet sheet,String templateid,Map<String, Object> dataMap){
		
		try {
			
			SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
			
			RecordSet recordSet=new RecordSet();
			
			//String sql="select * from uf_bbmbjl where daid="+templateid+" and cell is not null and dzlx='���϶���'";
			
			String sql="select t2.* from uf_jftzd_excel t1,uf_jftzd_excel_dt1 t2 where t1.id=t2.mainid and t1.id="+templateid+" and lx=0";
			
			writeLog("[ReadExcelTemplate.setSheetData] sql:"+sql);
			
			recordSet.execute(sql);
			
			while(recordSet.next()){
				
				int rowindex=recordSet.getInt("hs")-1;
				int cellindex=recordSet.getInt("ls")-1;
				
				String zdmc=recordSet.getString("excelmc");
				Object value=dataMap.get(zdmc);
				
				HSSFRow row = sheet.getRow(rowindex);//�õ�sheet���ĵڼ��У�ע��excel�൱��һ����ά���飬�±��Ǵ�0��ʼ��
				
				setCellValue(row,cellindex,value);
				
			}
			
			
			//���ô�ӡ����
			//HSSFRow row = sheet.getRow(2);
			//setCellValue(row,5,new Date());
			
			//��ϸ��¼
			List<Map<String, Object>> recordList=(List<Map<String, Object>>)dataMap.get("recordList");
			
			writeLog("recordList:"+dataMap.toString());
			
			int rowindex=0;
			for(Map<String, Object> recordMap:recordList){
				
				writeLog("rowindex:"+rowindex);
				
				//sql="select * from uf_bbmbjl where daid="+templateid+" and cell is not null and dzlx='���ϼ�¼����'";
				sql="select t2.* from uf_jftzd_excel t1,uf_jftzd_excel_dt1 t2 where t1.id=t2.mainid and t1.id="+templateid+" and lx=1";
				
				recordSet.execute(sql);
				
				while(recordSet.next()){
					
					if(rowindex==0){
						rowindex=recordSet.getInt("hs")-1;
					}
					int cellindex=recordSet.getInt("ls")-1;
					
					String zdmc=recordSet.getString("excelmc");
					Object value=recordMap.get(zdmc);
					
					HSSFRow recordrow = sheet.getRow(rowindex);//�õ�sheet���ĵڼ��У�ע��excel�൱��һ����ά���飬�±��Ǵ�0��ʼ��
					
					writeLog("zdmc:"+zdmc+" cellindex:"+cellindex+" value:"+value);
					
					setCellValue(recordrow,cellindex,value);
					
				}
				
				rowindex++;
				
			}
			
		} catch (Exception e) {
			writeLog("[ReadExcelTemplate.setSheetData] ����Excel���ݳ��� error:"+e.toString());
			e.printStackTrace();
			writeLog(e);
		}
		
		return true;
		
	}
	
	
	public void download(HttpServletResponse response,String filepath) {  
        try {  
        	
            // path��ָ�����ص��ļ���·����  
        	//String filepath="E:/weaver/����/�����/����ģ��/export.xls";
            File file = new File(filepath);
            // ȡ���ļ�����  
            String filename=file.getName();
            
            // ��������ʽ�����ļ���  
            InputStream fis = new BufferedInputStream(new FileInputStream(file)); 
            byte[] buffer = new byte[fis.available()];  
            fis.read(buffer);  
            fis.close();  
            // ���response  
            response.reset();  
            // ����response��Header  
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
