package com.cj;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import net.sf.json.JSONArray;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.xml.sax.SAXException;

import weaver.general.BaseBean;
import weaver.general.GCONST;

public class NCUtil extends BaseBean {
	
	public static Map<String, String> doPost(String billtype,Map<String,String> maindata,List<Map<String, String>> detaildata,String bdocid){
		
		BaseBean baseBean=new BaseBean();
		String xmlPath=createXML(billtype,maindata,detaildata,bdocid);
		Map<String, String> resultMap=new HashMap<String, String>();
		try {
			//xmlPath="D:/NCXmlFile/供应商.xml";
			resultMap=doSync(xmlPath);
		} catch (Exception e) {
			baseBean.writeLog("[NCUtil.doPost] 推送XML失败 error:"+e.toString());
			baseBean.writeLog(e);
			e.printStackTrace();
		}
		
		return resultMap;
	}
	
	public static Map<String, String> doPost(String billtype,Map<String,String> maindata,String bdocid){
		return doPost(billtype,maindata,null,bdocid);
	}
	
	/**
     * 创建xml文件
     * @param map 需要添加的字段属性
     * @return
     * @throws Exception
     */
    public static String createXML(String billtype,Map<String,String> maindata,List<Map<String, String>> detaildata,String bdocid){
    	
    	BaseBean baseBean=new BaseBean();
    	String xmlDir=GCONST.getRootPath()+"/NCXmlFile/";
    	
    	createDir(xmlDir);
    	
    	String xmlPath=xmlDir+billtype+"_"+System.currentTimeMillis()+".xml";
    	
    	//xmlPath="D:/NCXmlFile/customer_1513225753359.xml";
    	
    	//String xmlPath="F://xml//test.xml";
    	baseBean.writeLog("[NCUtil.createXML] xmlPath:"+xmlPath);
    	baseBean.writeLog("[NCUtil.createXML] maindata:"+maindata.toString());
    	//创建文档并设置文档的根元素节点
        Element root = DocumentHelper.createElement("ufinterface");
        
        String replace="N";
        if(!bdocid.equals("")){
        	replace="Y";
        }
        
        //根节点  
        root.addAttribute("account","test");
        //billtype 客户 customer 供应商 supplier 借款单 263X 还款单 264X 付款单 cmppaybill 报销单 264X
        root.addAttribute("billtype",billtype); 
        root.addAttribute("filename","");
        root.addAttribute("groupcode","CSAID");
        root.addAttribute("isexchange","Y");
        root.addAttribute("roottag","");
        root.addAttribute("receiver","N");
        root.addAttribute("replace",replace);
        root.addAttribute("sender","cjy");
        
        Document doucment = DocumentHelper.createDocument(root);  
        
        //子节点  
        Element bill = root.addElement("bill");
        
        if(!bdocid.equals("")){
        	bill.addAttribute("id", bdocid);
        }
        
        Element billHead = bill.addElement("billhead");
        
        //主字段属性
        for (Map.Entry<String, String> entry : maindata.entrySet()) {  
        	
        	String fieldName=entry.getKey();
        	String fieldValue=entry.getValue();
        	
        	billHead.addElement(fieldName).addText(fieldValue);
          
        } 
        
        if(detaildata!=null){
        	
        	Element items = billHead.addElement("er_busitem");
        	
        	for(Map<String, String> detailMap:detaildata){
        		
        		Element item = items.addElement("item");
        		
        		for (Map.Entry<String, String> entry : detailMap.entrySet()) {  
                	
                	String fieldName=entry.getKey();
                	String fieldValue=entry.getValue();
                	
                	item.addElement(fieldName).addText(fieldValue);
                  
                } 
        		
        	}
        }
        
        
        try {
            //添加  
            XMLWriter xmlwriter = new XMLWriter();  
            xmlwriter.write(doucment);  
            //创建文件  
            OutputFormat format = OutputFormat.createPrettyPrint();
            //设置xml文档的编码为utf-8
            format.setEncoding("UTF-8");
            //创建一个dom4j创建xml的对象
            XMLWriter writer = new XMLWriter(new FileOutputStream(new File(xmlPath)), format);
            //调用write方法将doc文档写到指定路径
            writer.write(doucment);
            writer.close();
            
            baseBean.writeLog("[NCUtil.createXML] 生成XML文件成功");
         } catch (IOException e) {
        	 baseBean.writeLog("[NCUtil.createXML] 生成XML文件失败 error:"+e.toString()+" xmlPath:"+xmlPath);
        	 baseBean.writeLog(e);
        	 e.printStackTrace();
         } 
    	
    	return xmlPath;
    } 
	
    // 创建目录
	public static boolean createDir(String destDirName) {
		File dir = new File(destDirName);
		if (dir.exists()) {// 判断目录是否存在
			//System.out.println("创建目录失败，目标目录已存在！");
			return false;
		}
		if (!destDirName.endsWith(File.separator)) {// 结尾是否以"/"结束
			destDirName = destDirName + File.separator;
		}
		if (dir.mkdirs()) {// 创建目标目录
			System.out.println("创建目录成功！" + destDirName);
			return true;
		} else {
			System.out.println("创建目录失败！");
			return false;
		}
	}
    
	/**
	 * 同步请求
	 * @param xmlPath xml路径
	 * @param type 同步类型
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public static Map<String, String> doSync(String xmlPath) throws IOException,SAXException, ParserConfigurationException {
		
		BaseBean baseBean=new BaseBean();
		
		String successful="-1";
		String bdocid="-1";
		String resultcode="";
		String resultdescription="";
		
		// xml数据
		File file = new File(xmlPath);
		// 创建Http连接，发送数据
		String url ="http://nc.csadi.cn:9000/service/XChangeServlet?account=test&groupcode=CSAID";
		HttpURLConnection connection=null;
		BufferedOutputStream out = null;
		BufferedInputStream input = null;
		try {
			URL realURL = new URL(url);
			connection = (HttpURLConnection) realURL.openConnection();
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-type", "text/xml");
			connection.setRequestProperty("Charset", "UTF-8");
			connection.setRequestMethod("POST");
			out = new BufferedOutputStream(connection.getOutputStream());
			input = new BufferedInputStream(new FileInputStream(file));
			int length;
			byte[] buffer = new byte[1000];
			while ((length = input.read(buffer, 0, 1000)) != -1) {
				out.write(buffer, 0, length);
			}
		}catch (Exception e) {
			baseBean.writeLog("[NCUtil.doSync] 推送到NC失败 error:"+e.toString());
			//baseBean.writeLog(e);
			e.printStackTrace();
		}finally {
			if (input != null) {
				input.close();
			}
			if (out != null) {
				out.close();
			}
		}
		
		String result = "";
		try {
			// 获取回执
			InputStream in = connection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
			StringBuilder temp = new StringBuilder();
			String line = bufferedReader.readLine();
			while (line != null) {
				temp.append(line).append("\r\n");
				line = bufferedReader.readLine();
			}
				
			result = new String(temp); // result即为回执内容
			result=new String(result.getBytes("gbk"),"UTF-8");
			
			Document document = DocumentHelper.parseText(result);
			Element root = document.getRootElement();
			
			successful=root.attributeValue("successful");
			
			Element sendresult=root.element("sendresult");
			bdocid=sendresult.element("bdocid").getText();
			resultcode=sendresult.element("resultcode").getText();
			resultdescription=sendresult.element("resultdescription").getText();
			
			baseBean.writeLog("[NCUtil.doSync] result:"+result);
			
			if(successful.equals("N")){
				baseBean.writeLog("[NCUtil.doSync] NC处理失败 result:"+result);
			}
			
			bufferedReader.close();
		} catch (Exception e) {
			baseBean.writeLog("[NCUtil.doSync] NC返回数据解析失败 error:"+e.toString());
			baseBean.writeLog("[NCUtil.doSync] NC返回数据解析失败 result:"+result);
		}
		
		Map<String, String> resultMap=new HashMap<String, String>();
		resultMap.put("successful", successful);
		resultMap.put("bdocid", bdocid);
		resultMap.put("resultcode", resultcode);
		resultMap.put("resultdescription", resultdescription);
		
		return resultMap;
	}
	
	public static void updateCustomer(String xml){
		
		JSONArray jsonArrays=JSONArray.fromObject(xml);
		System.out.println(jsonArrays);
		
	}
	
}
