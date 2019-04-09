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
 * ���ñ���
 * @author feng
 *
 */
public class CLBXAction extends BaseBean implements Action{
	
	
	public String execute(RequestInfo request) {
		
	    String fromtable = request.getRequestManager().getBillTableName(); //������
	    String requestid=request.getRequestid(); //����ID
	    String workflowid=request.getWorkflowid();//����ID
	    String lastoperator=request.getLastoperator(); //��������
	    
	    writeLog("******************************* CLBXAction *******************************");
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
	    
	    String zckm=""; //����֧����Ŀ
	    String zfyhzh=""; //����֧�������˻�
	    String dfVoucherAbstract=""; //����ժҪ
	    String dfzy="";//����ְԱ
	    
	    String skr="";
	    
	    String pzzdr="";

	   
	    RecordSet recordSet=new RecordSet();
	    RecordSet rs=new RecordSet();
	    
	    String sql="select * from "+fromtable+" where requestid="+requestid;
	    recordSet.execute(sql);
	    if(recordSet.next()){
	    	
	    	sqr=resourceComInfo.getLastname(recordSet.getString("sqr"));
	    	fygsbm=departmentComInfo.getDepartmentname(recordSet.getString("fygsbm"));
	    	
	    	companyNumber=recordSet.getString("ztgs1");
	    	voucherType="����";
	    	bizDate=recordSet.getString("ywfsrq");
	    	//bizDate=TimeUtil.getCurrentDateString();
	    	bookedDate=bizDate;
	    	
	    	PeriodYear=Util.getIntValue(Util.TokenizerString2(bizDate, "-")[0]); 
		    PeriodNumber=Util.getIntValue(Util.TokenizerString2(bizDate, "-")[1]);
	    	
	    	dforiginalAmount=recordSet.getDouble("sjfkje");
	    	dfcreditAmount=dforiginalAmount;
	    	
	    	zckm=easUtil.getSelectName("10674", recordSet.getString("dfkm"));

	    	
	    	dfzy=recordSet.getString("d_zy1");
	    	
	    	//zckm=recordSet.getString("hjkm");
	    	zfyhzh=recordSet.getString("zfyxbm");
	    	
	    	pzzdr=recordSet.getString("pzzdr");
	    	creator=resourceComInfo.getLastname(pzzdr);
	    	
	    	skr=recordSet.getString("skr");

	    }
	    
	    String abstractType=easUtil.getAbstractType(pzzdr);
		
	    //��ȡ��ϸ������
	    sql="select t2.* from "+fromtable+" t1,"+fromtable+"_dt3 t2 where requestId="+requestid+" and t1.id=t2.mainid";
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
			voucher.setAccountNumber(recordSet.getString("EAShjkm1")); //��Ŀ����
			
			voucher.setCurrencyNumber(currencyNumber); //���ֱ���
			voucher.setEntryDC(1); //��¼�з��� 1 �跽  -1����
			
			double se=Util.getDoubleValue(recordSet.getString("se"), 0); //˰��
			double jzje=recordSet.getDouble("jzje");//���˽��
			
			jzje=jzje-se;
			
			voucher.setOriginalAmount(jzje); //ԭ�ҽ��
			voucher.setDebitAmount(jzje); //�跽���
			voucher.setAttaches(Util.getIntValue(recordSet.getString("fjs"), 0)); //������
			voucher.setCreator(creator); //�Ƶ���
			
			String VoucherAbstract="";
			
			if(abstractType.equals("�칫��")){
				VoucherAbstract=fygsbm+skr+"����"+recordSet.getString("jdzysm")+"���÷�"; //ժҪ
			}else if(abstractType.equals("����")){
				VoucherAbstract=sqr+skr+"����"+recordSet.getString("jdzysm")+"���÷�"; //ժҪ
			}
			
			if(index==1){
				sql="select * from workflow_codeseqrecord where requestId='"+requestid+"'";
				rs.execute(sql);
				if(rs.next()){
					String workflowCode=rs.getString("workflowCode");
					dfVoucherAbstract=VoucherAbstract+"("+workflowCode+")";
				}
				
			}
			voucher.setVoucherAbstract(VoucherAbstract); //����
			
			String fzzlx=recordSet.getString("fzzlx1"); //����������
			String EASzzdw=recordSet.getString("EAScbzx1"); //EAS��֯��Ԫ
			String EASzy=recordSet.getString("EASzy1"); //EASְԱ
			
			String AsstActType1=""; //������Ŀ1
			String AsstActNumber1="";//����������1
			String AsstActType2=""; //������Ŀ2
			String AsstActNumber2="";//����������2
			
			if(fzzlx.equals("01")){
				AsstActType1="�ɱ�����";
				AsstActNumber1=EASzzdw;
			}else if(fzzlx.equals("11")){
				
				AsstActType1="�ɱ�����";
				AsstActNumber1=EASzzdw;
				
				AsstActType2="ְԱ";
				AsstActNumber2=EASzy;
			}
			
			voucher.setAsstActType1(AsstActType1);//������Ŀ1
			voucher.setAsstActNumber1(AsstActNumber1);//����������1
	    	
			if(fzzlx.equals("11")){
				voucher.setAsstActType2(AsstActType2);//������Ŀ1
				voucher.setAsstActNumber2(AsstActNumber2);//����������1
			}
			
			voucherList.add(voucher);
			//vouchers[index]=voucher;
			
			if(se>0){
				
				index++;
				
				voucher= new WSWSVoucher();
				
				voucher.setCompanyNumber(companyNumber); //��˾����
				voucher.setBookedDate(bookedDate); //��������,���̹鵵����
				voucher.setBizDate(bizDate); //ҵ������
				voucher.setPeriodYear(PeriodYear); //����ڼ�-��
				voucher.setPeriodNumber(PeriodNumber); //����ڼ�-����
				voucher.setVoucherType(voucherType); //ƾ֤�֣�ƾ֤���ͣ�
				voucher.setVoucherNumber(voucherNumber); //ƾ֤��
				voucher.setEntrySeq(index); //��¼�к� �跽Ϊ1 ����Ϊ2
				
				String slkjkm=easUtil.getSlkjkm("10666", recordSet.getString("sl")); //˰�ʻ�ƿͻ�
				
				//slkjkm="2221.01.01";
				writeLog("slkjkm:"+slkjkm);
				
				voucher.setAccountNumber(slkjkm); //��Ŀ����
				
				voucher.setCurrencyNumber(currencyNumber); //���ֱ���
				voucher.setEntryDC(1); //��¼�з��� 1 �跽  -1����
				voucher.setOriginalAmount(se); //ԭ�ҽ��
				voucher.setDebitAmount(se); //�跽���
				voucher.setAttaches(0); //������
				voucher.setCreator(creator); //�Ƶ���
				
				voucher.setVoucherAbstract(VoucherAbstract+"����˰"); //����
				
				voucher.setAsstActType1("");//������Ŀ1
				voucher.setAsstActNumber1("");//����������1
				
				voucherList.add(voucher);
				//vouchers[index]=voucher;
			}
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
		
		if(zckm.equals("1221.01")||zckm.equals("9110.01")){
			voucher.setAsstActType1("ְԱ");//������Ŀ1
			voucher.setAsstActNumber1(dfzy);//����������1
		}else{
			voucher.setAsstActType1("�����˻�");//������Ŀ1
			voucher.setAsstActNumber1(zfyhzh);//����������1
		}
		
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
