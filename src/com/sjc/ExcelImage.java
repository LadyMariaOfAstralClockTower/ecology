package com.sjc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.krysalis.barcode4j.impl.code39.Code39Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

import weaver.general.BaseBean;
import weaver.general.GCONST;



import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class ExcelImage {

	BaseBean log = new BaseBean();
	private String rootPath =GCONST.getRootPath();//webRoot路径

	//在Excel中插入图片
	public void insertImage(String path){
		
		File file = new File(path);
		try {
			Workbook wb=Workbook.getWorkbook(file);
			WritableWorkbook ww=wb.createWorkbook(file,wb);
			Sheet[] sheets=ww.getSheets();
			if(sheets!=null&&sheets.length>0){
				for(int i=0;i<sheets.length;i++){
					WritableSheet sheet=ww.getSheet(i);
					Cell[] cell=sheet.getRow(2);
					String codeNum=cell[1].getContents();
					createBarCode(codeNum);
					File imgFile = new File(rootPath+"sjc/QRCode/"+codeNum+".png");
					WritableImage img = new WritableImage(4,0,3,2,imgFile);
					sheet.addImage(img);
					WritableImage imgex = new WritableImage(4,23,3,2,imgFile);
					sheet.addImage(imgex);
				}
			}
			ww.write();   
			ww.close();
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.writeLog(e.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.writeLog(e.toString());
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.writeLog(e.toString());
		}
		
	}
	
	
	   //生成条形码
	   public void createBarCode(String codeNum){
			
			try {
	            Code39Bean bean = new Code39Bean();
	             
	            final int dpi = 300;
	          
	            bean.setModuleWidth(UnitConv.in2mm(1.5f / dpi)); 
	                                                             
	            bean.setWideFactor(3);
	            bean.doQuietZone(false);
	             
	            File outputFile = new File(rootPath+"sjc/QRCode/"+codeNum+".png");
	            OutputStream out = new FileOutputStream(outputFile);
	            try {
	 
	                BitmapCanvasProvider canvas = new BitmapCanvasProvider(out, "image/png", dpi, BufferedImage.TYPE_BYTE_BINARY, false, 0);

	                bean.generateBarcode(canvas, codeNum);
	             
	                canvas.finish();
	                
	            } finally {
	                out.close();
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			
		}
	
	
	
}
