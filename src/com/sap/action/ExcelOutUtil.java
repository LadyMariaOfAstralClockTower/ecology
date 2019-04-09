package com.sap.action;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Hashtable;
import java.util.Set;

import javax.servlet.ServletException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.apache.ws.commons.util.Base64;

import weaver.file.*;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.soa.workflow.WorkFlowInit;
import weaver.workflow.request.WorkflowSpeechAppend;
import weaver.workflow.webservices.WorkflowServiceImpl;

public class ExcelOutUtil{

    private Hashtable styleht = null ;
	private String agent = "";

    public int doCreateFile(String details){
    	
    	/*
    	details="["+
		   "{ebeln: \"4500000285\", ebelp: \"00010\", line: \"000001\", matnr: \"000000070086000180\", txz01: \"AE 涤纶低弹线 TEX24   W32002   黑色\", menge: \"10.000 \", meins: \"EA\", waers: \"CNY\", brtwr3: \"170.00 \", j_3adat: \"00000000\"},"+
		   "{ebeln: \"4500000286\", ebelp: \"00010\", line: \"000001\", matnr: \"000000070086000180\", txz01: \"AE 涤纶低弹线 TEX24   W32002   黑色\", menge: \"10.000 \", meins: \"EA\", waers: \"CNY\", brtwr3: \"170.00 \", j_3adat: \"00000000\"},"+
		   "{ebeln: \"4500000287\", ebelp: \"00010\", line: \"000001\", matnr: \"000000070086000180\", txz01: \"AE 涤纶低弹线 TEX24   W32002   黑色\", menge: \"10.000 \", meins: \"EA\", waers: \"CNY\", brtwr3: \"170.00 \", j_3adat: \"00000000\"}"+
		   "]";
    	*/
    	
    	JSONArray detailArrRows=JSONArray.fromObject(details);
    	
    	ExcelFile excelFile=new ExcelFile();
    	excelFile.init() ;
    	ExcelSheet es = new ExcelSheet();
    	
    	ExcelStyle excelStyle = excelFile.newExcelStyle("Header");
    	excelStyle.setGroundcolor(ExcelStyle.WeaverHeaderGroundcolor);
    	excelStyle.setFontcolor(ExcelStyle.WeaverHeaderFontcolor);
    	excelStyle.setFontbold(ExcelStyle.WeaverHeaderFontbold);
    	excelStyle.setAlign(ExcelStyle.WeaverHeaderAlign);
    	excelStyle.setCellBorder(ExcelStyle.WeaverBorderThin);
    	
    	//设置标题
    	ExcelRow title = es.newExcelRow();
    	title.addStringValue("采购订单号", "Header");
    	es.addColumnwidth(5000);
    	
    	title.addStringValue("行项目号", "Header");
    	es.addColumnwidth(5000);
    	
    	title.addStringValue("行标识", "Header");
    	es.addColumnwidth(5000);
    	
    	title.addStringValue("物料编码", "Header");
    	es.addColumnwidth(5000);
    	
    	title.addStringValue("物料描述", "Header");
    	es.addColumnwidth(5000);
    	
    	title.addStringValue("数量", "Header");
    	es.addColumnwidth(5000);
    	
    	title.addStringValue("单位", "Header");
    	es.addColumnwidth(5000);
    	
    	title.addStringValue("币种", "Header");
    	es.addColumnwidth(5000);
    	
    	title.addStringValue("预付款金额", "Header");
    	es.addColumnwidth(5000);
    	
    	title.addStringValue("采购交期", "Header");
    	es.addColumnwidth(5000);
    	
    	//行数据
		for(int k=0;k<detailArrRows.size();k++){
			
			JSONObject detailObj=detailArrRows.getJSONObject(k);
			
			String EBELN=detailObj.getString("EBELN".toLowerCase()); //采购订单号
			String EBELP=detailObj.getString("EBELP".toLowerCase()); //行项目号
			String LINE=detailObj.getString("LINE".toLowerCase()); //行标识
			String MATNR=detailObj.getString("MATNR".toLowerCase()); //物料编码
			String TXZ01=detailObj.getString("TXZ01".toLowerCase()); //物料描述
			String MENGE=detailObj.getString("MENGE".toLowerCase()); //数量
			String MEINS=detailObj.getString("MEINS".toLowerCase()); //单位
			String WAERS=detailObj.getString("WAERS".toLowerCase()); //币种
			String BRTWR3=detailObj.getString("BRTWR3".toLowerCase()); //预付款金额
			String J_3ADAT=detailObj.getString("J_3ADAT".toLowerCase()); //采购交期
			
			ExcelRow er = es.newExcelRow();
			er.addStringValue(EBELN);
			er.addStringValue(EBELP);
			er.addStringValue(LINE);
			er.addStringValue(MATNR);
			er.addStringValue(TXZ01);
			er.addStringValue(MENGE);
			er.addStringValue(MEINS);
			er.addStringValue(WAERS);
			er.addStringValue(BRTWR3);
			er.addStringValue(J_3ADAT);
			
		}
    	
		excelFile.setFilename("预付款明细") ;
		excelFile.addSheet("预付款明细", es) ;

        ExcelFile ef = excelFile ;
        ExcelSheet sheetvalues = null ;
        ExcelRow rowvalues = null ;
        
        HSSFWorkbook wb = null ;
        HSSFSheet sheets = null ;
        HSSFRow rows = null ;
        HSSFCell cells = null ;
        HSSFCellStyle cellStyle = null;

        wb = new HSSFWorkbook();

        initStyle(ef, wb) ;

        int sheetindex = 0 ;
        
        Set<Integer> index = ef.getInfoindex();
        while( ef.next() )  {
            String sheetname = ef.getSheetname() ;
            sheetvalues = ef.getSheet() ;

            if(sheetvalues == null ) continue ;
            sheets = wb.createSheet();
            wb.setSheetName( sheetindex, Util.fromScreen2(handleSlash(sheetname),7), HSSFWorkbook.ENCODING_UTF_16 );
            sheetindex ++ ;

            for( int i=0 ; i< sheetvalues.size() ; i++)  {

                rowvalues = (ExcelRow) (sheetvalues.getExcelRow(i)) ;

                if(rowvalues == null ) continue ;
                short rowheight = rowvalues.getHight() ;

                rows = sheets.createRow((short)i);

                if(rowheight != (short)255)  rows.setHeightInPoints(rowheight) ;

                int rowcellindex = 0 ;
                boolean hasstyle = false ;
                boolean hasspan = false ;

                if(rowvalues.stylesize() == rowvalues.size()) hasstyle = true ;
                if(rowvalues.spansize() == rowvalues.size()) hasspan = true ;

                for(int j=0 ; j<rowvalues.size(); j++) {

                    cells = rows.createCell((short)rowcellindex);

                    String cellvalues = Util.null2String(rowvalues.getValue(j)) ;
                    String cellvalueh = cellvalues.substring(0,2) ;
                    String cellvaluev = cellvalues.substring(2) ;
                     if(cellvalueh.indexOf("s_") == 0) {
                        //新增的四句话，设置CELL格式为文本格式   
                        HSSFCellStyle cellStyle2 = wb.createCellStyle();   
                        HSSFDataFormat format = wb.createDataFormat();   
                        cellStyle2.setDataFormat(format.getFormat("@"));  
                        cellStyle2.setWrapText(true);
                        cells.setCellStyle(cellStyle2); 
                        cells.setEncoding( HSSFCell.ENCODING_UTF_16 );
                        cells.setCellType(HSSFCell.CELL_TYPE_STRING);
                        cells.setCellValue(Util.fromScreen4(cellvaluev,7)) ;
                    }
                    else if(cellvalueh.indexOf("i_") == 0) {
                        int tempvalue = Util.getIntValue(cellvaluev, 0) ;
                        if(tempvalue != 0)
                            cells.setCellValue(tempvalue) ;
                    }
                    else if(cellvalueh.indexOf("f_") == 0) {
                        float tempvalue = Util.getFloatValue(cellvaluev) ;
                        if(tempvalue != 0.0)
                            cells.setCellValue(tempvalue) ;
                    }
                    else if(cellvalueh.indexOf("d_") == 0) {
                        double tempvalue = Util.getDoubleValue(cellvaluev, 0) ;
                        if(tempvalue != 0.0)
                            cells.setCellValue(tempvalue) ;
                    }
                    else if(cellvalueh.indexOf("o_") == 0) {
                        cells.setCellFormula(cellvaluev) ;
                    }
                    else if(cellvalueh.indexOf("n_") == 0) {
                        //if(Util.getDoubleValue(cellvaluev,-9999.99) == -9999.99) {
                            cells.setEncoding( HSSFCell.ENCODING_UTF_16 );
                            cells.setCellValue(Util.fromScreen4(cellvaluev,7)) ;
                        //}
                        //else {
                        //    double tempvalue = Util.getDoubleValue(cellvaluev) ;
                            //if(tempvalue != 0.0)
                        //        cells.setCellValue(tempvalue) ;
                        //}
                    }

                    if( hasstyle ) {
                        String stylename = Util.null2String(rowvalues.getStyle(j)) ;
                        if( !stylename.equals("") ) {
                            cellStyle = getStyle(stylename) ;
                            if(cellStyle != null) cells.setCellStyle( cellStyle ) ;
                        }
                    }

                    if( hasspan ) {
                        int rowspan = rowvalues.getSpan(j) ;
                        if(rowspan > 1) {
                            for(int k=0 ; k< rowspan-1 ; k++) {
                                rowcellindex ++ ;
                                cells = rows.createCell((short)rowcellindex);
                                cells.setCellValue("") ;
                                if( hasstyle && cellStyle != null ) cells.setCellStyle( cellStyle ) ;
                            }
                            sheets.addMergedRegion(new Region(i,(short)(rowcellindex+1-rowspan),i,(short)rowcellindex));
                        }
                    }
                    rowcellindex ++ ;
                }
            }

            for(int i=0 ; i< sheetvalues.columnsize() ; i++) {
                sheets.setColumnWidth((short) i, sheetvalues.getColumnwidth(i)) ;
            }

        }
        String filename = ef.getFilename() ;
		filename = handleSlash(filename);
		
		//filename="预付款.xls";
		filename="YFKMX"+System.currentTimeMillis()+".xls";
        
    	ByteArrayOutputStream bout=new ByteArrayOutputStream();
    	try {
			wb.write(bout);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	byte[] fileBody = bout.toByteArray();
    	String fileData=Base64.encode(fileBody);
    	
    	int[] docCategory={50,79,32};
    	
    	System.out.println("fileBody:"+fileBody.length+" filename:"+filename);

    	WorkFlowInit workFlowInit=new WorkFlowInit();
    	User user=workFlowInit.getUser(1);
    	
    	int docid = WorkflowSpeechAppend.uploadAppdix(fileData, filename, user, docCategory, "127.0.0.1");
    	System.out.println("docid:"+docid);
    	
    	return docid;

    }


    private void initStyle(ExcelFile ef, HSSFWorkbook wb) {
        styleht = new Hashtable() ;
        HSSFCellStyle cellStyle = null;
        HSSFFont thefont = null ;

        while(ef.nextStyle()) {
            String stylename = ef.getStyleName() ;
            ExcelStyle est = ef.getStyleValue() ;

            if(est != null) {
                cellStyle = wb.createCellStyle ();
                thefont = wb.createFont() ;
                
                if(est.getGroundcolor() != (short)9 ) {
                    cellStyle.setFillPattern (  (short) 1 );
                    cellStyle.setFillForegroundColor ( est.getGroundcolor() );
                }
                cellStyle.setRotation ( est.getScale() );
                if(est.getAlign() != (short)10) cellStyle.setAlignment( est.getAlign() );
                if(est.getDataformart() != (short)0 ) cellStyle.setDataFormat( est.getDataformart() );
                cellStyle.setVerticalAlignment( est.getValign() );

                thefont.setColor( est.getFontcolor() );
                thefont.setBoldweight( est.getFontbold() );
                thefont.setFontHeightInPoints( est.getFontheight() );

                cellStyle.setFont( thefont );

                //设置单元格边框属性
                short cellborder = est.getCellBorder();
                cellStyle.setBorderTop(cellborder);
                cellStyle.setBorderLeft(cellborder);
                cellStyle.setBorderRight(cellborder);
                cellStyle.setBorderBottom(cellborder);
                
                styleht.put(stylename,cellStyle) ;
            }
        }
    }

    private HSSFCellStyle getStyle(String stylename) {
        return (HSSFCellStyle)styleht.get(stylename) ;
    }
        
    private String handleSlash(String str){
	    return str.replaceAll("/","");
	}
}