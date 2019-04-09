/**
 * EASLoginProxy.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.jzx.action2;

import java.net.URL;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.client.Stub;
import org.codehaus.xfire.client.Client;

import com.jzx.EASServices.WSGLWebServiceFacade.wsvoucher.WSWSVoucher;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.TimeUtil;
import weaver.general.Util;
import weaver.hrm.company.DepartmentComInfo;
import weaver.hrm.resource.ResourceComInfo;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;


/**
 * 费用报销
 * @author feng
 *
 */
public class FYBXAction extends BaseBean implements Action{
	
	
	public String execute(RequestInfo request) {
		
	    String fromtable = request.getRequestManager().getBillTableName(); //表单名称
	    String requestid=request.getRequestid(); //请求ID
	    String workflowid=request.getWorkflowid();//流程ID
	    String lastoperator=request.getLastoperator(); //最后操作人
	    
	    writeLog("******************************* FYBXAction *******************************");
		writeLog("workflowid:"+workflowid+" requestid:"+requestid);
	    
	    EASUtil easUtil=new EASUtil();
	    ResourceComInfo resourceComInfo=easUtil.getResourceComInfo();
	    DepartmentComInfo departmentComInfo=easUtil.getDepartmentComInfo();
	    
	    String sfpzpz="";//是否抛转凭证
	    
	    String sqr=""; //申请人
	    String fygsbm=""; //费用归属部门
	    
	    String skr="";//收款人
	    
	    String companyNumber=""; //公司编码->费用归属 fygs
	    String bookedDate=TimeUtil.getCurrentDateString(); //记账日期,当前归档日期
	    String bizDate="";//业务日期->日期
	    int PeriodYear=0; //会计期间-年
	    int PeriodNumber=0; //会计期间-编码
	    String voucherType=""; //凭证字（凭证类型）->
	    String voucherNumber=requestid; //凭证号=流程ID
	    String currencyNumber="BB01";   //币种编码
	    double dforiginalAmount=0; //贷方原币金额
	    double dfcreditAmount=0; //贷方金额
	    String creator=""; //制单人
	    //String creator="雍洁丽"; 
	    
	    //Calendar calendar=Calendar.getInstance();
	    //PeriodYear=calendar.get(Calendar.YEAR); 
	    //PeriodNumber=calendar.get(Calendar.MONTH)+1; 
	    
	    String zckm=""; //贷方支出科目
	    String zfyhzh=""; //贷方支付银行账户
	    String dfVoucherAbstract=""; //贷方摘要
	    String dfzy="";//贷方职员
	    
	    String pzzdr="";
	    
	    String mainid="";
	    
	    RecordSet recordSet=new RecordSet();
	    RecordSet rs=new RecordSet();
	    
	    String sql="select * from "+fromtable+" where requestid="+requestid;
	    recordSet.execute(sql);
	    if(recordSet.next()){
	    	
	    	sqr=resourceComInfo.getLastname(recordSet.getString("sqr"));
	    	fygsbm=departmentComInfo.getDepartmentname(recordSet.getString("fygsbm1"));
	    	
	    	companyNumber=recordSet.getString("ztgs1");
	    	voucherType="记账";
	    	bizDate=recordSet.getString("ywfsrq");
	    	bookedDate=bizDate;
	    	
	    	PeriodYear=Util.getIntValue(Util.TokenizerString2(bizDate, "-")[0]); 
		    PeriodNumber=Util.getIntValue(Util.TokenizerString2(bizDate, "-")[1]);
	    	
	    	dforiginalAmount=recordSet.getDouble("sjfkje");
	    	dfcreditAmount=dforiginalAmount;
	    	
	    	zckm=easUtil.getSelectName("10732", recordSet.getString("dfkm"));

	    	zfyhzh=recordSet.getString("zfyxbm");
	    	
	    	dfzy=recordSet.getString("d_zy1");
	    	 	
	    	pzzdr=recordSet.getString("pzzdr");
	    	creator=resourceComInfo.getLastname(pzzdr);
	    	
	    	mainid=recordSet.getString("id");
	    	sfpzpz=recordSet.getString("sfpzpz");//0为是，1为否
	    	
	    	skr=recordSet.getString("skr");
	    	
	    	writeLog("是否抛转凭证："+sfpzpz);
	    }
		
	    if(sfpzpz.equals("1")){
	    	
	    	return "1";
	    }
	    
	    String abstractType=easUtil.getAbstractType(pzzdr);
	    
	    //获取明细表数据
	    sql="select t2.* from "+fromtable+" t1,"+fromtable+"_dt2 t2 where requestId="+requestid+" and t1.id=t2.mainid";
	    recordSet.execute(sql);
	    
	    List<WSWSVoucher> voucherList=new ArrayList<WSWSVoucher>();
	    
	    //借方明细
	    int index=0;
	    while(recordSet.next()){
	    	
	    	index++;
	    	
	    	WSWSVoucher voucher= new WSWSVoucher();
	    	voucher.setCompanyNumber(companyNumber); //公司编码
			voucher.setBookedDate(bookedDate); //记账日期,流程归档日期
			voucher.setBizDate(bizDate); //业务日期
			voucher.setPeriodYear(PeriodYear); //会计期间-年
			voucher.setPeriodNumber(PeriodNumber); //会计期间-编码
			voucher.setVoucherType(voucherType); //凭证字（凭证类型）
			voucher.setVoucherNumber(voucherNumber); //凭证号
			voucher.setEntrySeq(index); //分录行号 借方为1 贷方为2
			//voucher.setAccountNumber("6601.04.04"); //科目编码
			voucher.setAccountNumber(recordSet.getString("EAShjkm1")); //科目编码
			
			voucher.setCurrencyNumber(currencyNumber); //币种编码
			voucher.setEntryDC(1); //分录行方向 1 借方  -1贷方
			
			double se=Util.getDoubleValue(recordSet.getString("se"), 0); //税额
			double jzje=recordSet.getDouble("jzje");//记账金额
			
			jzje=jzje-se;
			
			voucher.setOriginalAmount(jzje); //原币金额
			voucher.setDebitAmount(jzje); //借方金额
			voucher.setAttaches(Util.getIntValue(recordSet.getString("fjs"), 0)); //附件数
			voucher.setCreator(creator); //制单人

			String VoucherAbstract="";
			
			if(abstractType.equals("办公室")){
				VoucherAbstract=fygsbm+sqr+"报销"+recordSet.getString("zy")+"费用"; //摘要
			}else if(abstractType.equals("店铺")){
				VoucherAbstract=sqr+skr+"报销"+fygsbm+recordSet.getString("zy")+"费用"; //摘要
			}
			
			
			if(index==1){
				sql="select * from workflow_codeseqrecord where requestId='"+requestid+"'";
				rs.execute(sql);
				if(rs.next()){
					String workflowCode=rs.getString("workflowCode");
					dfVoucherAbstract=VoucherAbstract+"("+workflowCode+")";
				}
			}
			voucher.setVoucherAbstract(VoucherAbstract); //描述
			
			String fzzlx=recordSet.getString("fzzlx1"); //辅助账类型
			String EASzzdw=recordSet.getString("EASzzdw1"); //EAS组织单元
			String EASkh=recordSet.getString("EASkh1"); //EAS组织单元	
			
			
			String AsstActType1=""; //核算项目1
			String AsstActNumber1="";//核算对象编码1
			String AsstActType2=""; //核算项目2
			String AsstActNumber2="";//核算对象编码2
			
			if(fzzlx.equals("01")){
				AsstActType1="成本中心";
				AsstActNumber1=EASzzdw;
			}else if(fzzlx.equals("04")){
				
				AsstActType1="客户";
				AsstActNumber1=EASkh;
			}else if(fzzlx.equals("05")){
				
				AsstActType1="成本中心";
				AsstActNumber1=EASzzdw;
				
				AsstActType2="客户";
				AsstActNumber2=EASkh;
			}
			
			voucher.setAsstActType1(AsstActType1);//核算项目1
			voucher.setAsstActNumber1(AsstActNumber1);//核算对象编码1
	    	
			if(fzzlx.equals("05")){
				voucher.setAsstActType2(AsstActType2);//核算项目1
				voucher.setAsstActNumber2(AsstActNumber2);//核算对象编码1
			}
			
			//vouchers[index]=voucher;
			voucherList.add(voucher);
			
			if(se>0){
				
				index++;
				
				voucher= new WSWSVoucher();
				
				voucher.setCompanyNumber(companyNumber); //公司编码
				voucher.setBookedDate(bookedDate); //记账日期,流程归档日期
				voucher.setBizDate(bizDate); //业务日期
				voucher.setPeriodYear(PeriodYear); //会计期间-年
				voucher.setPeriodNumber(PeriodNumber); //会计期间-编码
				voucher.setVoucherType(voucherType); //凭证字（凭证类型）
				voucher.setVoucherNumber(voucherNumber); //凭证号
				voucher.setEntrySeq(index); //分录行号 借方为1 贷方为2
			
				String slkjkm=easUtil.getSlkjkm("10734", recordSet.getString("sl1")); //税率会计客户
				
				writeLog("slkjkm:"+slkjkm);
				
				voucher.setAccountNumber(slkjkm); //科目编码
				
				voucher.setCurrencyNumber(currencyNumber); //币种编码
				voucher.setEntryDC(1); //分录行方向 1 借方  -1贷方
				voucher.setOriginalAmount(se); //原币金额
				voucher.setDebitAmount(se); //借方金额
				voucher.setAttaches(0); //附件数
				voucher.setCreator(creator); //制单人
				
				voucher.setVoucherAbstract(VoucherAbstract+"进项税"); //描述
				
				voucherList.add(voucher);
				//vouchers[index]=voucher;
				//index++;
				
			}
			
			
	    }
	    
	    index++;
		
	    //贷方记录
	    WSWSVoucher voucher= new WSWSVoucher();
		voucher.setCompanyNumber(companyNumber); //公司编码
		voucher.setBookedDate(bookedDate); //记账日期
		voucher.setBizDate(bizDate); //业务日期
		voucher.setPeriodYear(PeriodYear); //会计期间-年
		voucher.setPeriodNumber(PeriodNumber); //会计期间-编码
		voucher.setVoucherType(voucherType); //凭证字（凭证类型）
		voucher.setVoucherNumber(voucherNumber); //凭证号
		voucher.setEntrySeq(index); //分录行号	
		voucher.setAccountNumber(zckm); //贷方科目编码
		voucher.setCurrencyNumber(currencyNumber); //币种编码
		voucher.setEntryDC(-1); //分录行方向 1 借方  -1贷方
		voucher.setOriginalAmount(dforiginalAmount); //原币金额
		voucher.setCreditAmount(dfcreditAmount);//贷方金额
		voucher.setAttaches(0); //附件数
		voucher.setCreator(creator); //制单人
		voucher.setVoucherAbstract(dfVoucherAbstract); //描述
		
		
		if(zckm.equals("1221.01")||zckm.equals("9110.01")){
			voucher.setAsstActType1("职员");//核算项目1
			voucher.setAsstActNumber1(dfzy);//核算对象编码1
		}else{
			voucher.setAsstActType1("银行账户");//核算项目1
			voucher.setAsstActNumber1(zfyhzh);//核算对象编码1
		}
		
		//vouchers[index]=voucher;
		voucherList.add(voucher);
		
		WSWSVoucher[] vouchers = new WSWSVoucher[voucherList.size()]; //多个借方，1个贷方
		
		for(int i=0;i<voucherList.size();i++){
			vouchers[i]=voucherList.get(i);
		}
		
		JSONArray jsArray=JSONArray.fromObject(vouchers);
		
		writeLog("vouchers:"+jsArray.toString());
		
		String sessionid=easUtil.doLogin();
		
		sql="select * from "+fromtable+"_dt1 where mainid="+mainid;
		recordSet.execute(sql);
		if(recordSet.next()){
			String bxxm=EASUtil.getSelectName("10692", recordSet.getString("bxxm2"));
			writeLog("报销流程查询到的费用项目为："+bxxm);
			if("固定资产".equals(bxxm)){
				return "1";
			}
		}
		
		
		boolean result=easUtil.importVoucher(vouchers,workflowid,requestid); //导入凭证
		
		if(result){
			writeLog("凭证导入成功 workflowid:"+workflowid+" requestid:"+requestid);
		}else{
			writeLog("凭证导入失败 workflowid:"+workflowid+" requestid:"+requestid);
		}
		
		return result?"1":"0";
	}
	
	public static void main(String[] args) {
		
		Calendar calendar=Calendar.getInstance();
	    calendar.add(Calendar.MONTH, -2);
	    calendar.get(Calendar.YEAR);
	    calendar.get(Calendar.MONTH);
	    
	    System.out.println("year:"+calendar.get(Calendar.YEAR)+" month:"+calendar.get(Calendar.MONTH));
		
	}
	
}
