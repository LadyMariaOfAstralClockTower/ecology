package com.sjc;  
import java.io.BufferedOutputStream;  
import java.io.IOException;  
import java.io.OutputStream;  
import java.util.ArrayList;  
import java.util.List;  
  
import javax.servlet.http.HttpServletRequest;  
import javax.servlet.http.HttpServletResponse;  
  
import org.apache.commons.logging.Log;  
import org.apache.commons.logging.LogFactory;  
import org.apache.poi.hssf.usermodel.HSSFCell;  
import org.apache.poi.hssf.usermodel.HSSFCellStyle;  
import org.apache.poi.hssf.usermodel.HSSFDataFormat;  
import org.apache.poi.hssf.usermodel.HSSFFont;  
import org.apache.poi.hssf.usermodel.HSSFRow;  
import org.apache.poi.hssf.usermodel.HSSFSheet;  
import org.apache.poi.hssf.usermodel.HSSFWorkbook;  
import org.apache.poi.hssf.util.HSSFColor;  

import weaver.conn.RecordSet;
  
public class PoiExportExcel {  
   
 /** 
  * 导出数据到多个sheet 
  * @param title 
  * @param list 
  * @param os 
  * add by bcao 2015-8-13 
  */  
public static void PoiWriteExcel(String title,HttpServletRequest request,HttpServletResponse response){  
     OutputStream os=null;    
  try{  
     int sheetNum=1;//工作薄sheet编号  
     int bodyRowCount=1;//正文内容行号  
     int currentRowCount=1;//当前的行号  
     int perPageNum = 50000;//每个工作薄显示50000条数据  
     os = new BufferedOutputStream(response.getOutputStream());//输出流  
     HSSFWorkbook workbook = new HSSFWorkbook();//创建excel  
     HSSFSheet sheet = workbook.createSheet(title+sheetNum);//创建一个工作薄  
     setSheetColumn(sheet);//设置工作薄列宽  
     HSSFRow row = null;//创建一行  
     HSSFCell cell = null;//每个单元格  
            
     HSSFCellStyle titleCellStyle = createTitleCellStyle(workbook);  
     writeTitleContent(sheet,titleCellStyle);//写入标题  
       
    //第二行开始写入数据  
    //ArrayPageList pageList = (ArrayPageList) list;  
    //ArrayList<PSGInfoSearchForReport> psgInfos = new ArrayList<PSGInfoSearchForReport>();  
    //psgInfos = (ArrayList<PSGInfoSearchForReport>) pageList.getList();       
    HSSFCellStyle bodyCellStyle = createBodyCellStyle(workbook);   
    HSSFCellStyle dateBobyCellStyle = createDateBodyCellStyle(workbook);
    
    RecordSet recordSet=new RecordSet();
    String sql="select * from uf_zwzx ";
    recordSet.execute(sql);
    
    while(recordSet.next()){
    	
        //正文内容  
        row = sheet.createRow(bodyRowCount);          
        //第二行写开始写入正文内容  
        cell = row.createCell((short)0);  
        cell.setCellStyle(bodyCellStyle);  
        cell.setCellValue(recordSet.getString("fyqj"));//序号 
        
        cell = row.createCell((short)1);  
        cell.setCellStyle(bodyCellStyle);  
        cell.setCellValue(recordSet.getString("htbh"));//姓名  
        
        cell = row.createCell((short)2);  
        cell.setCellStyle(bodyCellStyle);  
        cell.setCellValue(recordSet.getString("fymc"));//手机号  
        
        cell = row.createCell((short)3);  
        cell.setCellStyle(bodyCellStyle);  
        cell.setCellValue(recordSet.getString("ksrq"));//航班号  
        
        cell = row.createCell((short)4);  
        cell.setCellStyle(dateBobyCellStyle);  
        cell.setCellValue(recordSet.getString("jsrq"));//航班日期  
        
        cell = row.createCell((short)5);  
        cell.setCellStyle(bodyCellStyle);  
        cell.setCellValue(recordSet.getString("je"));//始发
        
        cell = row.createCell((short)6);  
        cell.setCellStyle(bodyCellStyle);  
        cell.setCellValue(recordSet.getString("ye"));//到达
        
        cell = row.createCell((short)7);  
        cell.setCellStyle(bodyCellStyle);  
        cell.setCellValue(recordSet.getString("sm"));//电子票号
        
        cell = row.createCell((short)8);  
        cell.setCellStyle(bodyCellStyle);  
        cell.setCellValue(recordSet.getString("ysrq"));//证件号  
        
        if(currentRowCount % perPageNum == 0){//每个工作薄显示50000条数据  
            sheet=null;  
            sheetNum++;//工作薄编号递增1  
            sheet = workbook.createSheet(title+sheetNum);//创建一个新的工作薄  
            setSheetColumn(sheet);//设置工作薄列宽  
            bodyRowCount = 0;//正文内容行号置位为0  
            writeTitleContent(sheet,titleCellStyle);//写入标题            
        }  
        bodyRowCount++;//正文内容行号递增1  
        currentRowCount++;//当前行号递增1  
    }  
       workbook.write(os);  
       os.flush();  
     }catch(Exception e){  
           e.printStackTrace();  
     } finally {  
        try {  
             os.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
           
     }  
           
    }  
    /** 
     * 设置正文单元样式 
     * @param workbook 
     * @return 
     */  
    public static HSSFCellStyle createBodyCellStyle(HSSFWorkbook workbook){   
        HSSFCellStyle cellStyle = workbook.createCellStyle();  
        HSSFFont font = workbook.createFont();  
        font.setFontHeightInPoints((short) 8);    
        font.setFontName(HSSFFont.FONT_ARIAL);//设置标题字体  
        cellStyle.setFont(font);  
        cellStyle = workbook.createCellStyle();  
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中         
        return cellStyle;  
    }  
    /** 
     * 设置正文单元时间样式 
     * @param workbook 
     * @return 
     */  
    public static HSSFCellStyle createDateBodyCellStyle(HSSFWorkbook workbook){   
        HSSFCellStyle cellStyle = workbook.createCellStyle();  
        HSSFFont font = workbook.createFont();  
        font.setFontHeightInPoints((short) 8);    
        font.setFontName(HSSFFont.FONT_ARIAL);//设置标题字体  
        cellStyle.setFont(font);  
        cellStyle = workbook.createCellStyle();  
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中     
        HSSFDataFormat format= workbook.createDataFormat();  
        cellStyle.setDataFormat(format.getFormat("yyyy-mm-dd"));          
        return cellStyle;  
    }     
  
    /** 
     * 设置标题单元样式 
     * @param workbook 
     * @return 
     */  
    public static HSSFCellStyle createTitleCellStyle(HSSFWorkbook workbook){   
        HSSFCellStyle cellStyle = workbook.createCellStyle();  
        HSSFFont font = workbook.createFont();  
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);  
        font.setFontHeightInPoints((short) 8);    
        font.setFontName(HSSFFont.FONT_ARIAL);//设置标题字体  
        cellStyle.setFont(font);  
        cellStyle = workbook.createCellStyle();  
        cellStyle.setFont(font);//设置列标题样式  
        cellStyle.setFillForegroundColor(HSSFColor.SKY_BLUE.index);// 设置背景色  
        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);  
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 居中         
        return cellStyle;  
    }     
    /** 
     * 写入标题行 
     * @param workbook 
     * @return 
     */  
    public static void writeTitleContent (HSSFSheet sheet,HSSFCellStyle cellStyle){   
        HSSFRow row = null;  
        HSSFCell cell = null;         
        //标题  
        row = sheet.createRow(0);          
        //第一行写入标题行  
        cell = row.createCell((short)0);//序号  
        cell.setCellStyle(cellStyle);  
        cell.setCellValue("费用期间");  
        cell = row.createCell((short)1);//姓名  
        cell.setCellStyle(cellStyle);  
        cell.setCellValue("合同编号");  
        cell = row.createCell((short)2);//手机号  
        cell.setCellStyle(cellStyle);  
        cell.setCellValue("费用名称");  
        cell = row.createCell((short)3);//航班号  
        cell.setCellStyle(cellStyle);  
        cell.setCellValue("开始日期");  
        cell = row.createCell((short)4);//航班日期  
        cell.setCellStyle(cellStyle);  
        cell.setCellValue("结束日期");  
        cell = row.createCell((short)5);//始发  
        cell.setCellStyle(cellStyle);  
        cell.setCellValue("金额");  
        cell = row.createCell((short)6);//到达  
        cell.setCellStyle(cellStyle);  
        cell.setCellValue("余额");  
        cell = row.createCell((short)7);//电子票号  
        cell.setCellStyle(cellStyle);  
        cell.setCellValue("说明");  
        cell = row.createCell((short)8);//证件号  
        cell.setCellStyle(cellStyle);  
        cell.setCellValue("应收日期");  
        
    }  
    public static void setSheetColumn(HSSFSheet sheet){  
         sheet.setColumnWidth((short) 2, (short) 3200);//设置手机号列宽  
         sheet.setColumnWidth((short) 4, (short) 3200);//设置航班日期列宽  
         sheet.setColumnWidth((short) 7, (short) 5250);//设置电子票号列宽  
         sheet.setColumnWidth((short) 8, (short) 6250);//设置证件号列宽  
    }  
          
}  