/**
 * EASLoginProxy.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.jzx.action;

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
 * �������
 * @author feng
 *
 */
public class JKSQAction extends BaseBean implements Action{
	
	
	public String execute(RequestInfo request) {
		
	    String fromtable = request.getRequestManager().getBillTableName(); //������
	    String requestid=request.getRequestid(); //����ID
	    String workflowid=request.getWorkflowid();//����ID
	    String lastoperator=request.getLastoperator(); //��������
	    
	    writeLog("******************************* JKSQAction *******************************");
		writeLog("workflowid:"+workflowid+" requestid:"+requestid);
	    
	    EASUtil easUtil=new EASUtil();
	    ResourceComInfo resourceComInfo=easUtil.getResourceComInfo();
	    DepartmentComInfo departmentComInfo=easUtil.getDepartmentComInfo();
	    
	    String sqr=""; //������
	    String fygsbm=""; //���ù�������
	    
	    String companyNumber=""; //��˾����->���ù��� fygs
	    String bookedDate=TimeUtil.getCurrentDateString(); //��������,��ǰ�鵵����
	    String bizDate="";//ҵ������->����
	    int PeriodYear=0; //����ڼ�-��
	    int PeriodNumber=0; //����ڼ�-����
	    String voucherType=""; //ƾ֤�֣�ƾ֤���ͣ�->
	    String voucherNumber=requestid; //ƾ֤��=����ID
	    String currencyNumber="BB01";   //���ֱ���
	    double dforiginalAmount=0; //����ԭ�ҽ��
	    double dfcreditAmount=0; //�������
	    String creator=""; //�Ƶ���
	    //String creator="Ӻ����"; 
	    

	    
	    String fksy=""; //��������
	    
	    /*
	    Calendar calendar=Calendar.getInstance();
	    PeriodYear=calendar.get(Calendar.YEAR); 
	    PeriodNumber=calendar.get(Calendar.MONTH)+1; 
	    */
	    
	    String zckm=""; //����֧����Ŀ
	    String zfyhzh=""; //����֧�������˻�
	    String dfVoucherAbstract=""; //����ժҪ
	    
	    RecordSet recordSet=new RecordSet();
	    RecordSet rs=new RecordSet();
	    
	    String sql="select * from "+fromtable+" where requestid="+requestid;
	    recordSet.execute(sql);
	    if(recordSet.next()){
	    	
	    	sqr=resourceComInfo.getLastname(recordSet.getString("sqr"));
	    	fygsbm=departmentComInfo.getDepartmentname(recordSet.getString("fyssbm"));
	    	
	    	companyNumber=recordSet.getString("ztgs");
	    	voucherType="����";
	    	bizDate=recordSet.getString("ywfsrq");
	    	bookedDate=bizDate;
	    	
	    	PeriodYear=Util.getIntValue(Util.TokenizerString2(bizDate, "-")[0]); 
		    PeriodNumber=Util.getIntValue(Util.TokenizerString2(bizDate, "-")[1]);
	    	
	    	dforiginalAmount=recordSet.getDouble("fkje");
	    	dfcreditAmount=dforiginalAmount;
	    	
	    	zckm=easUtil.getSelectName("10780", recordSet.getString("dfkm"));
	    	zfyhzh=recordSet.getString("zfyxbm");
	    	
	    	fksy=recordSet.getString("fksy");

			String pzzdr=recordSet.getString("pzzdr");
	    	creator=resourceComInfo.getLastname(pzzdr);

	    }
		
	    //��ȡ��ϸ������
	    sql="select t2.* from "+fromtable+" t1,"+fromtable+"_dt1 t2 where requestId="+requestid+" and t1.id=t2.mainid";
	    recordSet.execute(sql);
	    
	    List<WSWSVoucher> voucherList=new ArrayList<WSWSVoucher>();
	    
	    //�跽��ϸ
	    int index=0;
	    while(recordSet.next()){
	    	
	    	index++;
	    	
	    	WSWSVoucher voucher= new WSWSVoucher();
	    	voucher.setCompanyNumber(companyNumber); //��˾����
			voucher.setBookedDate(bookedDate); //��������,���̹鵵����
			voucher.setBizDate(bizDate); //ҵ������
			voucher.setPeriodYear(PeriodYear); //����ڼ�-��
			voucher.setPeriodNumber(PeriodNumber); //����ڼ�-����
			voucher.setVoucherType(voucherType); //ƾ֤�֣�ƾ֤���ͣ�
			voucher.setVoucherNumber(voucherNumber); //ƾ֤��
			//voucher.setEntrySeq(1); //��¼�к� �跽Ϊ1 ����Ϊ2
			voucher.setEntrySeq(index); //��¼�к� �跽Ϊ1 ����Ϊ2
			//voucher.setAccountNumber("6601.04.04"); //��Ŀ����
			voucher.setAccountNumber(recordSet.getString("EAShjkm")); //��Ŀ����
			
			voucher.setCurrencyNumber(currencyNumber); //���ֱ���
			voucher.setEntryDC(1); //��¼�з��� 1 �跽  -1����
			
			double jzje=recordSet.getDouble("jzje");//���˽��
			
			voucher.setOriginalAmount(jzje); //ԭ�ҽ��
			voucher.setDebitAmount(jzje); //�跽���
			voucher.setAttaches(Util.getIntValue(recordSet.getString("fjs"), 0)); //������
			voucher.setCreator(creator); //�Ƶ���
			
			String VoucherAbstract="֧��"+fygsbm+sqr+"��֧"+recordSet.getString("jdzysm"); //ժҪ
			if(index==1){
				sql="select * from workflow_codeseqrecord where requestId='"+requestid+"'";
				rs.execute(sql);
				if(rs.next()){
					String workflowCode=rs.getString("workflowCode");
					dfVoucherAbstract=VoucherAbstract+"("+workflowCode+")";
				}
			}
			voucher.setVoucherAbstract(VoucherAbstract); //����
			
			String fzzlx=recordSet.getString("fzzlx"); //����������
			String EASzy=recordSet.getString("EASzy"); //EASְԱ
			String EASzzdw=recordSet.getString("j_cbzx"); //EAS��֯��Ԫ
			
			String AsstActType1=""; //������Ŀ1
			String AsstActNumber1="";//����������1
			String AsstActType2=""; //������Ŀ2
			String AsstActNumber2="";//����������2
			
			if(fzzlx.equals("10")){
				AsstActType1="ְԱ";
				AsstActNumber1=EASzy;
			}
			
			voucher.setAsstActType1(AsstActType1);//������Ŀ1
			voucher.setAsstActNumber1(AsstActNumber1);//����������1
			
			easUtil.setAsstAct(voucher,fzzlx,EASzzdw,"","",EASzy);
			
			voucherList.add(voucher);
			//vouchers[index]=voucher;
			
	    }
		
	    index++;
	    
	    //������¼
	    WSWSVoucher voucher= new WSWSVoucher();
		voucher.setCompanyNumber(companyNumber); //��˾����
		voucher.setBookedDate(bookedDate); //��������
		voucher.setBizDate(bizDate); //ҵ������
		voucher.setPeriodYear(PeriodYear); //����ڼ�-��
		voucher.setPeriodNumber(PeriodNumber); //����ڼ�-����
		voucher.setVoucherType(voucherType); //ƾ֤�֣�ƾ֤���ͣ�
		voucher.setVoucherNumber(voucherNumber); //ƾ֤��
		voucher.setEntrySeq(index); //��¼�к�	
		voucher.setAccountNumber(zckm); //������Ŀ����
		voucher.setCurrencyNumber(currencyNumber); //���ֱ���
		voucher.setEntryDC(-1); //��¼�з��� 1 �跽  -1����
		voucher.setOriginalAmount(dforiginalAmount); //ԭ�ҽ��
		voucher.setCreditAmount(dfcreditAmount);//�������
		voucher.setAttaches(0); //������
		voucher.setCreator(creator); //�Ƶ���
		voucher.setVoucherAbstract(dfVoucherAbstract); //����
		
		voucher.setAsstActType1("�����˻�");//������Ŀ1
		voucher.setAsstActNumber1(zfyhzh);//����������1
		
		voucherList.add(voucher);
		//vouchers[index]=voucher;
		
		WSWSVoucher[] vouchers = new WSWSVoucher[voucherList.size()]; //����跽��1������
		
		for(int i=0;i<voucherList.size();i++){
			vouchers[i]=voucherList.get(i);
		}
		
		JSONArray jsArray=JSONArray.fromObject(vouchers);
		
		writeLog("vouchers:"+jsArray.toString());
		
		String sessionid=easUtil.doLogin();
		
		boolean result=easUtil.importVoucher(vouchers,workflowid,requestid); //����ƾ֤
		
		if(result){
			writeLog("ƾ֤����ɹ� workflowid:"+workflowid+" requestid:"+requestid);
		}else{
			writeLog("ƾ֤����ʧ�� workflowid:"+workflowid+" requestid:"+requestid);
		}
		
		return result?"1":"0";
	}
	
	public static void main(String[] args) {
		
		String bizDate="2017-05-31";
		int PeriodNumber=Util.getIntValue(Util.TokenizerString2(bizDate, "-")[1]);
		System.out.println("PeriodNumber:"+PeriodNumber);
	}
	
}
