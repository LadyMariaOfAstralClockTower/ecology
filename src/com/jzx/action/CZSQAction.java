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
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;
import weaver.general.TimeUtil;
import weaver.general.Util;
import weaver.hrm.company.DepartmentComInfo;
import weaver.hrm.resource.ResourceComInfo;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * ��������
 * @author feng
 *
 */
public class CZSQAction extends BaseBean implements Action{
	
	
	public String execute(RequestInfo request) {
		
	    String fromtable = request.getRequestManager().getBillTableName(); //������
	    String requestid=request.getRequestid(); //����ID
	    String workflowid=request.getWorkflowid();//����ID
	    String lastoperator=request.getLastoperator(); //��������
	    
	    writeLog("******************************* CZSQAction *******************************");
		writeLog("workflowid:"+workflowid+" requestid:"+requestid);
	    
	    EASUtil easUtil=new EASUtil();
	    ResourceComInfo resourceComInfo=easUtil.getResourceComInfo();
	    DepartmentComInfo departmentComInfo=easUtil.getDepartmentComInfo();
	    
	    String sfpzpz="";//�Ƿ���תƾ֤
	    
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
	    String rzpzh="";//����ƾ֤��
	    
	    String zckm=""; //����֧����Ŀ
	    String zfyhzh=""; //����֧�������˻�
	    String dfVoucherAbstract=""; //����ժҪ
	    
	    
	    String fzzlx="";
	    String EASzzdw="";
	    String EASkh="";
	    String EASgys="";
	    String EASzy="";
	    
	    
	    RecordSet recordSet=new RecordSet();
	    RecordSet rs=new RecordSet();
	    RecordSetDataSource rsds = new RecordSetDataSource("EAS");
	    
	    String sql="select * from "+fromtable+" where requestid="+requestid;
	    recordSet.execute(sql);
	    if(recordSet.next()){
	    	
	    	sqr=resourceComInfo.getLastname(recordSet.getString("sqr"));
	    	fygsbm=departmentComInfo.getDepartmentname(recordSet.getString("fygsbm"));
	    	
	    	companyNumber=recordSet.getString("ztgs");
	    	voucherType="����";
	    	//bizDate=recordSet.getString("ywfsrq");
	    	bizDate=TimeUtil.getCurrentDateString();
	    	bookedDate=bizDate;
	    	
	    	PeriodYear=Util.getIntValue(Util.TokenizerString2(bizDate, "-")[0]); 
		    PeriodNumber=Util.getIntValue(Util.TokenizerString2(bizDate, "-")[1]);
	    	
	    	dforiginalAmount=recordSet.getDouble("czje");
	    	dfcreditAmount=dforiginalAmount;
	    	
	    	//zckm=easUtil.getSelectName("10586", recordSet.getString("dfkm"));
	    	zckm=recordSet.getString("EAShjkm");
	    	//zfyhzh=recordSet.getString("zfyxbm");
	    	
	    	fzzlx=recordSet.getString("fzzlx"); //����������
			EASzzdw=recordSet.getString("EASzzdw"); //EAS��֯��Ԫ
			EASkh=recordSet.getString("EASkh"); //EAS�ͻ�
			EASgys=recordSet.getString("EASgys"); //EAS��Ӧ��
			EASzy=recordSet.getString("EASzy"); //EASְԱ
			
			rzpzh=recordSet.getString("rzpz");
			String pzzdr=recordSet.getString("pzzdr");
	    	creator=resourceComInfo.getLastname(pzzdr);
	    	
	    	sfpzpz=recordSet.getString("sfpzpz");//0Ϊ�ǣ�1Ϊ��

	    }
	    
	    if(sfpzpz.equals("1")){
	    	return "1";
	    }
		
	    //��ȡ��ϸ������
	    sql="select t2.* from "+fromtable+" t1,"+fromtable+"_dt2 t2 where requestId="+requestid+" and t1.id=t2.mainid";
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
			
			double se=Util.getDoubleValue(recordSet.getString("se"), 0); //˰��
			double jzje=recordSet.getDouble("jzje");//���˽��
			
			jzje=jzje-se;
			
			voucher.setOriginalAmount(jzje); //ԭ�ҽ��
			voucher.setDebitAmount(jzje); //�跽���
			voucher.setAttaches(Util.getIntValue(recordSet.getString("fjs"), 0)); //������
			voucher.setCreator(creator); //�Ƶ���
			
			String gys="";
			
			sql="select * from T_BD_Supplier where FNumber='"+EASgys+"'";
			boolean f=rsds.execute(sql);
			if(rsds.next()){
				gys=rsds.getString("FName_L2");
			}
			writeLog("��ѯ��Ӧ�̵�sql��"+sql+"  �Ƿ��ѯ�ɹ���"+f+" ��Ӧ��Ϊ��"+gys);

			
			String VoucherAbstract="��Ʊ����,"+sqr+"����"+gys+fygsbm+recordSet.getString("jdzysm")+"(����"+rzpzh+")"; //ժҪ
			if(index==1){
				sql="select * from workflow_codeseqrecord where requestId='"+requestid+"'";
				rs.execute(sql);
				if(rs.next()){
					String workflowCode=rs.getString("workflowCode");
					dfVoucherAbstract=VoucherAbstract+"("+workflowCode+")";
				}
			}
			voucher.setVoucherAbstract(VoucherAbstract); //����
			
			String dfzzlx=recordSet.getString("fzzlx"); //����������
			String dEASzzdw=recordSet.getString("EASzzdw"); //EAS��֯��Ԫ
			String dEASkh=recordSet.getString("EASkh"); //EAS�ͻ�
			String dEASgys=recordSet.getString("EASgys"); //EAS��Ӧ��
			String dEASzy=recordSet.getString("EASzy"); //EASְԱ
			
			setAsstAct(voucher,dfzzlx,dEASzzdw,dEASkh,dEASgys,dEASzy);
			
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
				
				String slkjkm=easUtil.getSlkjkm("10900", recordSet.getString("sl1")); //˰�ʻ�ƿͻ�
				
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
		
	    
	    if(!zckm.equals("")){
	    
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
			
			//voucher.setAsstActType1("�����˻�");//������Ŀ1
			//voucher.setAsstActNumber1(zfyhzh);//����������1
			
			setAsstAct(voucher,fzzlx,EASzzdw,EASkh,EASgys,EASzy);
			
			voucherList.add(voucher);
		
	    }
		
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
	
	public void setAsstAct(WSWSVoucher voucher,String fzzlx,String EASzzdw,String EASkh,String EASgys,String EASzy){
		
		String AsstActType1=""; //������Ŀ1
		String AsstActNumber1="";//����������1
		String AsstActType2=""; //������Ŀ2
		String AsstActNumber2="";//����������2
		
		writeLog("fzzlx:"+fzzlx+" EASzzdw:"+EASzzdw+" EASkh:"+EASkh+" EASgys:"+EASgys+" EASzy:"+EASzy);
		
		if(fzzlx.equals("01")){
			AsstActType1="�ɱ�����";
			AsstActNumber1=EASzzdw;
		}else if(fzzlx.equals("02")){
			
			AsstActType1="��Ӧ��";
			AsstActNumber1=EASgys;
		}else if(fzzlx.equals("03")){
			
			AsstActType1="�ɱ�����";
			AsstActNumber1=EASzzdw;
			
			AsstActType2="��Ӧ��";
			AsstActNumber2=EASgys;
		}else if(fzzlx.equals("04")){
			
			AsstActType1="�ͻ�";
			AsstActNumber1=EASkh;
		}else if(fzzlx.equals("05")){
			
			AsstActType1="�ɱ�����";
			AsstActNumber1=EASzzdw;
			
			AsstActType2="�ͻ�";
			AsstActNumber2=EASkh;
		}else if(fzzlx.equals("10")){
			
			AsstActType1="ְԱ";
			AsstActNumber1=EASzy;
			
		}else if(fzzlx.equals("11")){
			
			AsstActType1="�ɱ�����";
			AsstActNumber1=EASzzdw;
			
			AsstActType2="ְԱ";
			AsstActNumber2=EASzy;
			
		}
		
		if(!AsstActType1.equals("")){
		
			voucher.setAsstActType1(AsstActType1);//������Ŀ1
			voucher.setAsstActNumber1(AsstActNumber1);//����������1
			
			if(fzzlx.equals("05")||fzzlx.equals("03")||fzzlx.equals("11")){
				voucher.setAsstActType2(AsstActType2);//������Ŀ1
				voucher.setAsstActNumber2(AsstActNumber2);//����������1
			}
			
		}
			
	}
	
	public static void main(String[] args) {
		String bizDate="2017-05-31";
		int PeriodNumber=Util.getIntValue(Util.TokenizerString2(bizDate, "-")[1]);
		System.out.println("PeriodNumber:"+PeriodNumber);
	}
	
}
