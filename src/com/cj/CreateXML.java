package com.cj;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;  
import org.dom4j.DocumentHelper;  
import org.dom4j.Element;  
import org.dom4j.io.OutputFormat;  
import org.dom4j.io.XMLWriter;  
  
/**
 * 创建xml文件
 * @author GodWei
 *
 */ 
public class CreateXML {
	
        public static void main(String[] args) throws Exception{
        	Map<String,String> map=new HashMap<String,String>();
        	
        	map.put("code", "8005");
        	map.put("name", "中建十六局");
        	createXML("1",map);
        }
        
        
        
        /**
         * 创建xml文件
         * @param type 创建类型 ：1-客户 2-供应商 3-报销单 4-借款
         * @param map 需要添加的字段属性
         * @return
         * @throws Exception
         */
        public static String createXML(String type,Map<String,String> map) throws Exception{
        	
        	String xmlPath="F://send//test.xml";
        	
        	// 创建文档并设置文档的根元素节点   
            Element root = DocumentHelper.createElement("ufinterface");
            
            if(type.equals("1")){
            	xmlPath="F://xml//kh-"+System.currentTimeMillis()+".xml";
            	root=customerXML(root,map);
            }else if(type.equals("2")){
            	xmlPath="F://xml//gys-"+System.currentTimeMillis()+".xml";
            	root=supplierXML(root,map);
            }else if(type.equals("3")){
            	xmlPath="F://xml//bxd-"+System.currentTimeMillis()+".xml";
            	root=expenseXML(root, map);
            }else if(type.equals("4")){
            	xmlPath="F://xml//jk-"+System.currentTimeMillis()+".xml";
            	root=borrowXML(root,map);
            }
            
            Document doucment = DocumentHelper.createDocument(root);  
            
            
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
               System.out.println("生成XML文件成功");
             } catch (IOException e) {
               System.out.println("生成XML文件失败");
               e.printStackTrace();
             } 
        	
        	return xmlPath;
        } 
        
        
        /**
         * 设置同步客户的xml的属性
         * @param root
         * @return
         */
        public static Element customerXML(Element root,Map<String, String> map){
        	
        	//根节点  
            root.addAttribute("account","test");
            root.addAttribute("billtype","customer");
            root.addAttribute("filename","");
            root.addAttribute("groupcode","CSAID");
            root.addAttribute("isexchange","Y");
            root.addAttribute("roottag","");
            root.addAttribute("receiver","N");
            root.addAttribute("replace","N");
            root.addAttribute("sender","cjy");
            
            //子节点  
            Element bill = root.addElement("bill");
            Element billHead = bill.addElement("billhead");
            
            
            //字段属性
            Element pk_org = billHead.addElement("pk_org");
            pk_org.addText("E00");
            
            Element pk_fiorg = billHead.addElement("pk_fiorg");
            pk_fiorg.addText("E00");
        	
            Element pk_payorg = billHead.addElement("pk_payorg");
            pk_payorg.addText("E00");
            
            Element pk_supplierclass = billHead.addElement("pk_supplierclass");
            pk_supplierclass.addText("02");
            
            Element code = billHead.addElement("code");
            code.addText(map.get("code"));//客户编码
            
            Element name = billHead.addElement("name");
            name.addText(map.get("name"));//客户名称
            
            Element shortname = billHead.addElement("shortname");
            shortname.addText("");
            
            Element pk_custclass = billHead.addElement("pk_custclass");
            pk_custclass.addText("01");
            
            Element pk_areacl = billHead.addElement("pk_areacl");
            pk_areacl.addText("CN");
            
            
            Element iscustomer = billHead.addElement("iscustomer");
            iscustomer.addText("N");
            
            Element isfreecust = billHead.addElement("isfreecust");
            isfreecust.addText("N");
            
            Element isoutcheck = billHead.addElement("isoutcheck");
            isoutcheck.addText("N");
            
            Element pk_country = billHead.addElement("pk_country");
            pk_country.addText("CN");
            
            Element pk_format = billHead.addElement("pk_format");
            pk_format.addText("ZH-CN");
            
            Element enablestate = billHead.addElement("enablestate");
            enablestate.addText("2");
            
            Element pk_timezone = billHead.addElement("pk_timezone");
            pk_timezone.addText("P0800");
            
            Element pk_group = billHead.addElement("pk_group");
            pk_group.addText("CSAID");
            
            return root;
            
        }
        
        
        /**
         * 设置同步供应商的xml的属性
         * @param root
         * @return
         */
        public static Element supplierXML(Element root,Map<String, String> map){
        	
        	//根节点  
            root.addAttribute("account","test");
            root.addAttribute("billtype","supplier");
            root.addAttribute("filename","");
            root.addAttribute("groupcode","CSAID");
            root.addAttribute("isexchange","Y");
            root.addAttribute("roottag","");
            root.addAttribute("receiver","N");
            root.addAttribute("replace","N");
            root.addAttribute("sender","cjy");
            
            //子节点  
            Element bill = root.addElement("bill");
            Element billHead = bill.addElement("billhead");
            
            
            //字段属性
            Element pk_group = billHead.addElement("pk_group");
            pk_group.addText("CSAID");
            
            Element pk_org = billHead.addElement("pk_org");
            pk_org.addText("E00");
            
            Element code = billHead.addElement("code");
            code.addText(map.get("code"));//供应商编码
            
            Element name = billHead.addElement("name");
            name.addText(map.get("name"));//供应商名称
            
            Element shortname = billHead.addElement("shortname");
            shortname.addText("无");
            
            Element supprop = billHead.addElement("supprop");
            supprop.addText("0");
            
            Element pk_areacl = billHead.addElement("pk_areacl");
            pk_areacl.addText("CN");
            
            Element pk_supplierclass = billHead.addElement("pk_supplierclass");
            pk_supplierclass.addText("0201");
            
            Element iscustomer = billHead.addElement("iscustomer");
            iscustomer.addText("N");
            
            Element isfreecust = billHead.addElement("isfreecust");
            isfreecust.addText("N");
            
            Element isoutcheck = billHead.addElement("isoutcheck");
            isoutcheck.addText("N");
            
            Element registerfund = billHead.addElement("registerfund");
            registerfund.addText("0.0");
            
            Element zipcode = billHead.addElement("zipcode");
            zipcode.addText("N");
            
            Element url = billHead.addElement("url");
            url.addText("无");
            
            Element memo = billHead.addElement("memo");
            memo.addText("无");
            
            Element suplinkman = billHead.addElement("suplinkman");
            
            Element item = suplinkman.addElement("item");
            
            Element isdefault = item.addElement("isdefault");
            isdefault.addText("N");
            
            Element pk_linkman = item.addElement("pk_linkman");
            pk_linkman.addText("");
            
            Element linkmanvo = item.addElement("linkmanvo");
            
            Element code1 = linkmanvo.addElement("code");
            code1.addText("无");
            
            Element name1 = linkmanvo.addElement("name");
            name1.addText("无");
            
            Element sex = linkmanvo.addElement("sex");
            sex.addText("0");
            
            Element vjob = linkmanvo.addElement("vjob");
            vjob.addText("无");
            
            Element phone = linkmanvo.addElement("phone");
            phone.addText("无");
            
            Element cell = linkmanvo.addElement("cell");
            cell.addText("无");
            
            Element fax = linkmanvo.addElement("fax");
            fax.addText("无");
            
            Element email = linkmanvo.addElement("email");
            email.addText("无");
            
            Element webaddress = linkmanvo.addElement("webaddress");
            webaddress.addText("无");
            
            Element address = linkmanvo.addElement("address");
            address.addText("无");
            
            Element postcode = linkmanvo.addElement("postcode");
            postcode.addText("无");
            
            Element memo1 = linkmanvo.addElement("memo");
            memo1.addText("无");
            
            Element pk_country = billHead.addElement("pk_country");
            pk_country.addText("CN");
            
            Element pk_format = billHead.addElement("pk_format");
            pk_format.addText("ZH-CN");
            
            Element enablestate = billHead.addElement("enablestate");
            enablestate.addText("2");
            
            Element pk_timezone = billHead.addElement("pk_timezone");
            pk_timezone.addText("P0800");
            
            
            
            return root;
            
        }
        
        
        /**
         * 设置同步报销的xml文件的属性 ( 适用于 劳务成本报销流程 管理费用报销流程 销售费用报销流程 )
         * @param root
         * @return
         */
        public static Element expenseXML(Element root,Map<String, String> map){
        	
        	//根节点  
            root.addAttribute("account","test");
            root.addAttribute("billtype","264X");
            root.addAttribute("filename","");
            root.addAttribute("groupcode","CSAID");
            root.addAttribute("isexchange","Y");
            root.addAttribute("roottag","");
            root.addAttribute("receiver","N");
            root.addAttribute("replace","N");
            root.addAttribute("sender","cjy");
            
            //子节点  
            Element bill = root.addElement("bill");
            Element billHead = bill.addElement("billhead");
            
            
            //字段属性
            Element pk_org = billHead.addElement("pk_org");
            pk_org.addText("E00");
            
            Element pk_fiorg = billHead.addElement("pk_fiorg");
            pk_fiorg.addText("E00");
        	
            Element pk_payorg = billHead.addElement("pk_payorg");
            pk_payorg.addText("E00");
            
            Element pk_group = billHead.addElement("pk_group");
            pk_group.addText("CSAID");
            
            Element qcbz = billHead.addElement("qcbz");
            qcbz.addText("N");
            
            Element qzzt = billHead.addElement("qzzt");
            qzzt.addText("0");
            
            Element payflag = billHead.addElement("payflag");
            payflag.addText("1");
            
            Element paytarget = billHead.addElement("paytarget");
            paytarget.addText("0");
            
            Element djlxbm = billHead.addElement("djlxbm");
            djlxbm.addText(map.get("djlxbm"));//单据类型编码
            
            Element jobid = billHead.addElement("jobid");
            jobid.addText("");
            
            Element fjzs = billHead.addElement("fjzs");
            fjzs.addText("3");
            
            Element djbh = billHead.addElement("djbh");
            djbh.addText(map.get("djbh"));//单据编号
            
            Element djrq = billHead.addElement("djrq");
            djrq.addText(map.get("djrq"));//单据日期
            
            Element deptid = billHead.addElement("deptid");
            deptid.addText(map.get("deptcode"));//部门编码
            
            Element jkbxr = billHead.addElement("jkbxr");
            jkbxr.addText("A0000009");
            
            Element fydeptid = billHead.addElement("fydeptid");
            fydeptid.addText(map.get("deptcode"));//费用承担部门编码
            
            Element total = billHead.addElement("total");
            total.addText(map.get("zj"));//总计金额
            
            Element receiver = billHead.addElement("receiver");
            receiver.addText(map.get("receiver"));//接收人
            
            Element fydwbm = billHead.addElement("fydwbm");
            fydwbm.addText("E00");
            
            Element dwbm = billHead.addElement("dwbm");
            dwbm.addText("E00");
            
            Element bzbm = billHead.addElement("bzbm");
            bzbm.addText("CNY");
            
            Element djdl = billHead.addElement("djdl");
            djdl.addText("bx");
            
            Element djzt = billHead.addElement("djzt");
            djzt.addText("1");
            
            Element operator = billHead.addElement("operator");
            operator.addText("E0000042");
            
            Element ybje = billHead.addElement("ybje");
            ybje.addText(map.get("ybje"));//预报金额
            
            
            
            Element items = billHead.addElement("items");
            Element item = items.addElement("item");
            
            Element defitem4 = item.addElement("defitem4");
            defitem4.addText(map.get("fylx"));//费用类型
            
            Element defitem8 = item.addElement("defitem8");
            defitem8.addText(map.get("fymc"));//费用名称
            
            Element defitem6 = item.addElement("defitem6");
            defitem6.addText(map.get("yfje"));//应付金额
            
            Element defitem7 = item.addElement("defitem7");
            defitem7.addText(map.get("sfje"));//实付金额
            
            return root;
            
        }
        
        /**
         * 设置同步借款的xml文件的属性  (适用于 借支申请流程 保证金申请流程)
         * @param root
         * @return
         */
        public static Element borrowXML(Element root,Map<String, String> map){
        	
        	//根节点  
            root.addAttribute("account","test");
            root.addAttribute("billtype","263X");
            root.addAttribute("filename","");
            root.addAttribute("groupcode","CSAID");
            root.addAttribute("isexchange","Y");
            root.addAttribute("roottag","");
            root.addAttribute("receiver","N");
            root.addAttribute("replace","N");
            root.addAttribute("sender","cjy");
            
            //子节点  
            Element bill = root.addElement("bill");
            Element billHead = bill.addElement("billhead");
            
            
            //字段属性
            Element pk_org = billHead.addElement("pk_org");
            pk_org.addText("E00");
            
            Element pk_fiorg = billHead.addElement("pk_fiorg");
            pk_fiorg.addText("E00");
        	
            Element pk_payorg = billHead.addElement("pk_payorg");
            pk_payorg.addText("E00");
            
            Element pk_group = billHead.addElement("pk_group");
            pk_group.addText("CSAID");
            
            Element djlxbm = billHead.addElement("djlxbm");
            djlxbm.addText(map.get("djlxbm"));
            
            Element fjzs = billHead.addElement("fjzs");
            fjzs.addText("1");
            
            Element djbh = billHead.addElement("djbh");
            djbh.addText(map.get("djbh"));
            
            Element deptid = billHead.addElement("deptid");
            deptid.addText("E00-03");
            
            Element djrq = billHead.addElement("djrq");
            djrq.addText("E00-03");
            
            Element total = billHead.addElement("total");
            total.addText("2472.00");
            
            Element zy = billHead.addElement("zy");
            zy.addText("两江新区智能交通设计投标保证金");
            
            Element spzt = billHead.addElement("spzt");
            spzt.addText("4");
            
            Element jkbxr = billHead.addElement("jkbxr");
            jkbxr.addText("A0000009");
            
            Element operator = billHead.addElement("operator");
            operator.addText("E0000042");
            
            Element fydwbm = billHead.addElement("fydwbm");
            fydwbm.addText("E00");
            
            Element bbhl = billHead.addElement("bbhl");
            bbhl.addText("1");
            
            Element bbje = billHead.addElement("bbje");
            bbje.addText("500.00");
            
            Element bbye = billHead.addElement("bbye");
            bbye.addText("500.00");
            
            Element bzbm = billHead.addElement("bzbm");
            bzbm.addText("CNY");
            
            Element creationtime = billHead.addElement("creationtime");
            creationtime.addText("2017/11/30 15:37:30");
            
            Element creator = billHead.addElement("creator");
            creator.addText("E0000163");
            
            Element djdl = billHead.addElement("djdl");
            djdl.addText("jk");
            
            Element djzt = billHead.addElement("djzt");
            djzt.addText("1");
            
            Element dwbm = billHead.addElement("dwbm");
            dwbm.addText("E00");
            
            Element fydeptid = billHead.addElement("fydeptid");
            fydeptid.addText("E00-16");
            
            Element pk_billtype = billHead.addElement("pk_billtype");
            pk_billtype.addText("263X");
            
            Element receiver = billHead.addElement("receiver");
            receiver.addText("E0000163");
            
            Element sxbz = billHead.addElement("sxbz");
            sxbz.addText("E0000163");
            
            Element ybje = billHead.addElement("ybje");
            ybje.addText("2472.00");
            
            Element ybye = billHead.addElement("ybye");
            ybye.addText("2472.00");
            
            Element yjye = billHead.addElement("yjye");
            yjye.addText("2472.00");
            
            return root;
            
        }
          
}