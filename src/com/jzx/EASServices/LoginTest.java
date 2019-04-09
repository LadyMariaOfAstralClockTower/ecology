package com.jzx.EASServices;

import java.text.SimpleDateFormat;
import java.util.Date;

import net.sf.json.JSONArray;
import weaver.general.BaseBean;
import weaver.general.TimeUtil;

import com.jzx.EASServices.EASLogin.EASLoginProxyService;
import com.jzx.EASServices.EASLogin.EASLoginProxyServiceLocator;
import com.jzx.EASServices.EASLogin.EASLoginSoapBindingStub;
import com.jzx.EASServices.EASLogin.client.WSContext;
import com.jzx.EASServices.WSGLWebServiceFacade.WSGLWebServiceFacadeSoapBindingStub;
import com.jzx.EASServices.WSGLWebServiceFacade.WSGLWebServiceFacadeSrvProxyService;
import com.jzx.EASServices.WSGLWebServiceFacade.WSGLWebServiceFacadeSrvProxyServiceLocator;
import com.jzx.EASServices.WSGLWebServiceFacade.wsvoucher.WSWSVoucher;

public class LoginTest extends BaseBean{

	public static void main(String[] args) {
		
		try {
			
			LoginTest test=new LoginTest();
			test.doLogin();
			
			
			WSGLWebServiceFacadeSrvProxyService service=new WSGLWebServiceFacadeSrvProxyServiceLocator();
			WSGLWebServiceFacadeSoapBindingStub serviceStub=(WSGLWebServiceFacadeSoapBindingStub)service.getWSGLWebServiceFacade();
			String[] results=serviceStub.importVoucher(test.getvochers(), 0, 1, 1);
			
			for(int i=0;i<results.length;i++){
				 System.out.println("result:"+results[i]);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void doImportVoucher(){
		
		try {
					
					LoginTest test=new LoginTest();
					test.doLogin();
					
					
					WSGLWebServiceFacadeSrvProxyService service=new WSGLWebServiceFacadeSrvProxyServiceLocator();
					WSGLWebServiceFacadeSoapBindingStub serviceStub=(WSGLWebServiceFacadeSoapBindingStub)service.getWSGLWebServiceFacade();
					String[] results=serviceStub.importVoucher(test.getvochers(), 0, 1, 1);
					
					for(int i=0;i<results.length;i++){
						 System.out.println("result:"+results[i]);
					}
					
					
			} catch (Exception e) {
				e.printStackTrace();
			}
		
	}
	
	public String doLogin(){
		
		String sessionid="";
		try {
			EASLoginProxyService service = new EASLoginProxyServiceLocator();
			EASLoginSoapBindingStub serviceStub= (EASLoginSoapBindingStub) service.getEASLogin();
			
			WSContext ctx = serviceStub.login("000070", "kaoqin", "eas", "03", "L2", 0);
			
			sessionid=ctx.getSessionId();
			
			//�ɹ���¼���ӡsessionId
			System.out.println(ctx.getSessionId());
			System.out.println(ctx.getUserName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sessionid;
	}
	

	public WSWSVoucher[] getvochers()
	{
		
		if(true){
			//WSWSVoucher[] data = new WSWSVoucher[3];
			WSWSVoucher[] data = new WSWSVoucher[2];
			String vochersStr="[{\"accountNumber\":\"6601.16\",\"assistBizDate\":\"\",\"assistEndDate\":\"\",\"asstActName1\":\"\",\"asstActName2\":\"\",\"asstActName3\":\"\",\"asstActName4\":\"\",\"asstActName5\":\"\",\"asstActName6\":\"\",\"asstActName7\":\"\",\"asstActName8\":\"\",\"asstActNumber1\":\"00.02.01.04.02\",\"asstActNumber2\":\"\",\"asstActNumber3\":\"\",\"asstActNumber4\":\"\",\"asstActNumber5\":\"\",\"asstActNumber6\":\"\",\"asstActNumber7\":\"\",\"asstActNumber8\":\"\",\"asstActType1\":\"������֯\",\"asstActType2\":\"\",\"asstActType3\":\"\",\"asstActType4\":\"\",\"asstActType5\":\"\",\"asstActType6\":\"\",\"asstActType7\":\"\",\"asstActType8\":\"\",\"asstSeq\":0,\"attaches\":0,\"auditor\":\"\",\"bizDate\":\"2016-11-02\",\"bizNumber\":\"\",\"bookedDate\":\"2016-11-02\",\"cashflowAmountLocal\":0,\"cashflowAmountOriginal\":0,\"cashflowAmountRpt\":0,\"companyNumber\":\"00.02\",\"creator\":\"�µ�\",\"creditAmount\":0,\"currencyNumber\":\"BB01\",\"cussent\":0,\"customerNumber\":\"\",\"debitAmount\":1000,\"description\":\"\",\"entryDC\":1,\"entrySeq\":1,\"icketNumber\":\"\",\"invoiceNumber\":\"\",\"isCheck\":false,\"itemFlag\":0,\"localRate\":0,\"measurement\":\"\",\"oppAccountSeq\":0,\"originalAmount\":1000,\"periodNumber\":9,\"periodYear\":2016,\"poster\":\"\",\"price\":0,\"primaryCoef\":0,\"primaryItem\":\"\",\"qty\":0,\"settlementNumber\":\"\",\"settlementType\":\"\",\"supplyCoef\":0,\"supplyItem\":\"\",\"tempNumber\":\"\",\"voucherAbstract\":\"2016-11-02 �� xxxxx\",\"voucherNumber\":\"1133\",\"voucherType\":\"��ZJ-SMƷ��\"},"+
							  " {\"accountNumber\":\"6601.15\",\"assistBizDate\":\"\",\"assistEndDate\":\"\",\"asstActName1\":\"\",\"asstActName2\":\"\",\"asstActName3\":\"\",\"asstActName4\":\"\",\"asstActName5\":\"\",\"asstActName6\":\"\",\"asstActName7\":\"\",\"asstActName8\":\"\",\"asstActNumber1\":\"00.02.01.04.02\",\"asstActNumber2\":\"\",\"asstActNumber3\":\"\",\"asstActNumber4\":\"\",\"asstActNumber5\":\"\",\"asstActNumber6\":\"\",\"asstActNumber7\":\"\",\"asstActNumber8\":\"\",\"asstActType1\":\"������֯\",\"asstActType2\":\"\",\"asstActType3\":\"\",\"asstActType4\":\"\",\"asstActType5\":\"\",\"asstActType6\":\"\",\"asstActType7\":\"\",\"asstActType8\":\"\",\"asstSeq\":0,\"attaches\":0,\"auditor\":\"\",\"bizDate\":\"2016-11-02\",\"bizNumber\":\"\",\"bookedDate\":\"2016-11-02\",\"cashflowAmountLocal\":0,\"cashflowAmountOriginal\":0,\"cashflowAmountRpt\":0,\"companyNumber\":\"00.02\",\"creator\":\"�µ�\",\"creditAmount\":0,\"currencyNumber\":\"BB01\",\"cussent\":0,\"customerNumber\":\"\",\"debitAmount\":2000,\"description\":\"\",\"entryDC\":1,\"entrySeq\":1,\"icketNumber\":\"\",\"invoiceNumber\":\"\",\"isCheck\":false,\"itemFlag\":0,\"localRate\":0,\"measurement\":\"\",\"oppAccountSeq\":0,\"originalAmount\":2000,\"periodNumber\":9,\"periodYear\":2016,\"poster\":\"\",\"price\":0,\"primaryCoef\":0,\"primaryItem\":\"\",\"qty\":0,\"settlementNumber\":\"\",\"settlementType\":\"\",\"supplyCoef\":0,\"supplyItem\":\"\",\"tempNumber\":\"\",\"voucherAbstract\":\"2016-11-02 �� yyyyy\",\"voucherNumber\":\"1133\",\"voucherType\":\"��ZJ-SMƷ��\"},"+
							  " {\"accountNumber\":\"1002.10\",\"assistBizDate\":\"\",\"assistEndDate\":\"\",\"asstActName1\":\"\",\"asstActName2\":\"\",\"asstActName3\":\"\",\"asstActName4\":\"\",\"asstActName5\":\"\",\"asstActName6\":\"\",\"asstActName7\":\"\",\"asstActName8\":\"\",\"asstActNumber1\":\"0241\",\"asstActNumber2\":\"\",\"asstActNumber3\":\"\",\"asstActNumber4\":\"\",\"asstActNumber5\":\"\",\"asstActNumber6\":\"\",\"asstActNumber7\":\"\",\"asstActNumber8\":\"\",\"asstActType1\":\"�����˻�\",\"asstActType2\":\"\",\"asstActType3\":\"\",\"asstActType4\":\"\",\"asstActType5\":\"\",\"asstActType6\":\"\",\"asstActType7\":\"\",\"asstActType8\":\"\",\"asstSeq\":0,\"attaches\":0,\"auditor\":\"\",\"bizDate\":\"2016-11-02\",\"bizNumber\":\"\",\"bookedDate\":\"2016-11-02\",\"cashflowAmountLocal\":0,\"cashflowAmountOriginal\":0,\"cashflowAmountRpt\":0,\"companyNumber\":\"00.02\",\"creator\":\"�µ�\",\"creditAmount\":3000,\"currencyNumber\":\"BB01\",\"cussent\":0,\"customerNumber\":\"\",\"debitAmount\":0,\"description\":\"\",\"entryDC\":-1,\"entrySeq\":2,\"icketNumber\":\"\",\"invoiceNumber\":\"\",\"isCheck\":false,\"itemFlag\":0,\"localRate\":0,\"measurement\":\"\",\"oppAccountSeq\":0,\"originalAmount\":3000,\"periodNumber\":9,\"periodYear\":2016,\"poster\":\"\",\"price\":0,\"primaryCoef\":0,\"primaryItem\":\"\",\"qty\":0,\"settlementNumber\":\"\",\"settlementType\":\"\",\"supplyCoef\":0,\"supplyItem\":\"\",\"tempNumber\":\"\",\"voucherAbstract\":\"PZ2016-11-02 15:24:21\",\"voucherNumber\":\"1133\",\"voucherType\":\"��ZJ-SMƷ��\"}]";
			
			       vochersStr="[{\"accountNumber\":\"6601.16\",\"assistBizDate\":\"\",\"assistEndDate\":\"\",\"asstActName1\":\"\",\"asstActName2\":\"\",\"asstActName3\":\"\",\"asstActName4\":\"\",\"asstActName5\":\"\",\"asstActName6\":\"\",\"asstActName7\":\"\",\"asstActName8\":\"\",\"asstActNumber1\":\"00.01.01.04\",\"asstActNumber2\":\"\",\"asstActNumber3\":\"\",\"asstActNumber4\":\"\",\"asstActNumber5\":\"\",\"asstActNumber6\":\"\",\"asstActNumber7\":\"\",\"asstActNumber8\":\"\",\"asstActType1\":\"������֯\",\"asstActType2\":\"\",\"asstActType3\":\"\",\"asstActType4\":\"\",\"asstActType5\":\"\",\"asstActType6\":\"\",\"asstActType7\":\"\",\"asstActType8\":\"\",\"asstSeq\":0,\"attaches\":0,\"auditor\":\"\",\"bizDate\":\"2017-03-10\",\"bizNumber\":\"\",\"bookedDate\":\"2017-03-10\",\"cashflowAmountLocal\":0,\"cashflowAmountOriginal\":0,\"cashflowAmountRpt\":0,\"companyNumber\":\"00.01\",\"creator\":\"Ԥ���û�\",\"creditAmount\":0,\"currencyNumber\":\"BB01\",\"cussent\":0,\"customerNumber\":\"\",\"debitAmount\":120,\"description\":\"\",\"entryDC\":1,\"entrySeq\":1,\"icketNumber\":\"\",\"invoiceNumber\":\"\",\"isCheck\":false,\"itemFlag\":0,\"localRate\":0,\"measurement\":\"\",\"oppAccountSeq\":0,\"originalAmount\":120,\"periodNumber\":3,\"periodYear\":2017,\"poster\":\"\",\"price\":0,\"primaryCoef\":0,\"primaryItem\":\"\",\"qty\":0,\"settlementNumber\":\"\",\"settlementType\":\"\",\"supplyCoef\":0,\"supplyItem\":\"\",\"tempNumber\":\"\",\"voucherAbstract\":\"2017-03-10 �� ��ͨ��\",\"voucherNumber\":\"1951\",\"voucherType\":\"��-Ͷ�ʹ�˾\"}," +
					          " {\"accountNumber\":\"1002.01\",\"assistBizDate\":\"\",\"assistEndDate\":\"\",\"asstActName1\":\"\",\"asstActName2\":\"\",\"asstActName3\":\"\",\"asstActName4\":\"\",\"asstActName5\":\"\",\"asstActName6\":\"\",\"asstActName7\":\"\",\"asstActName8\":\"\",\"asstActNumber1\":\"0006\",\"asstActNumber2\":\"\",\"asstActNumber3\":\"\",\"asstActNumber4\":\"\",\"asstActNumber5\":\"\",\"asstActNumber6\":\"\",\"asstActNumber7\":\"\",\"asstActNumber8\":\"\",\"asstActType1\":\"�����˻�\",\"asstActType2\":\"\",\"asstActType3\":\"\",\"asstActType4\":\"\",\"asstActType5\":\"\",\"asstActType6\":\"\",\"asstActType7\":\"\",\"asstActType8\":\"\",\"asstSeq\":0,\"attaches\":0,\"auditor\":\"\",\"bizDate\":\"2017-03-10\",\"bizNumber\":\"\",\"bookedDate\":\"2017-03-10\",\"cashflowAmountLocal\":0,\"cashflowAmountOriginal\":0,\"cashflowAmountRpt\":0,\"companyNumber\":\"00.01\",\"creator\":\"Ԥ���û�\",\"creditAmount\":120,\"currencyNumber\":\"BB01\",\"cussent\":0,\"customerNumber\":\"\",\"debitAmount\":0,\"description\":\"\",\"entryDC\":-1,\"entrySeq\":2,\"icketNumber\":\"\",\"invoiceNumber\":\"\",\"isCheck\":false,\"itemFlag\":0,\"localRate\":0,\"measurement\":\"\",\"oppAccountSeq\":0,\"originalAmount\":120,\"periodNumber\":3,\"periodYear\":2017,\"poster\":\"\",\"price\":0,\"primaryCoef\":0,\"primaryItem\":\"\",\"qty\":0,\"settlementNumber\":\"\",\"settlementType\":\"\",\"supplyCoef\":0,\"supplyItem\":\"\",\"tempNumber\":\"\",\"voucherAbstract\":\"PZ2017-03-10 10:05:29\",\"voucherNumber\":\"1951\",\"voucherType\":\"��-Ͷ�ʹ�˾\"}]";
			
			       vochersStr="[{\"accountNumber\":\"9410.09\",\"assistBizDate\":\"\",\"assistEndDate\":\"\",\"asstActName1\":\"\",\"asstActName2\":\"\",\"asstActName3\":\"\",\"asstActName4\":\"\",\"asstActName5\":\"\",\"asstActName6\":\"\",\"asstActName7\":\"\",\"asstActName8\":\"\",\"asstActNumber1\":\"01.01.009.002\",\"asstActNumber2\":\"\",\"asstActNumber3\":\"\",\"asstActNumber4\":\"\",\"asstActNumber5\":\"\",\"asstActNumber6\":\"\",\"asstActNumber7\":\"\",\"asstActNumber8\":\"\",\"asstActType1\":\"�ɱ�����\",\"asstActType2\":\"\",\"asstActType3\":\"\",\"asstActType4\":\"\",\"asstActType5\":\"\",\"asstActType6\":\"\",\"asstActType7\":\"\",\"asstActType8\":\"\",\"asstSeq\":0,\"attaches\":2,\"auditor\":\"\",\"bizDate\":\"2017-05-16\",\"bizNumber\":\"\",\"bookedDate\":\"2017-05-16\",\"cashflowAmountLocal\":0,\"cashflowAmountOriginal\":0,\"cashflowAmountRpt\":0,\"companyNumber\":\"01.01\",\"creator\":\"Ԥ���û�\",\"creditAmount\":0,\"currencyNumber\":\"BB01\",\"cussent\":0,\"customerNumber\":\"\",\"debitAmount\":1000,\"description\":\"\",\"entryDC\":1,\"entrySeq\":1,\"icketNumber\":\"\",\"invoiceNumber\":\"\",\"isCheck\":false,\"itemFlag\":0,\"localRate\":0,\"measurement\":\"\",\"oppAccountSeq\":0,\"originalAmount\":1000,\"periodNumber\":5,\"periodYear\":2017,\"poster\":\"\",\"price\":0,\"primaryCoef\":0,\"primaryItem\":\"\",\"qty\":0,\"settlementNumber\":\"\",\"settlementType\":\"\",\"supplyCoef\":0,\"supplyItem\":\"\",\"tempNumber\":\"\",\"voucherAbstract\":\"֧�������ձ���-1\",\"voucherNumber\":\"75691\",\"voucherType\":\"����\"}" +
							 " ,{\"accountNumber\":\"1002.01\",\"assistBizDate\":\"\",\"assistEndDate\":\"\",\"asstActName1\":\"\",\"asstActName2\":\"\",\"asstActName3\":\"\",\"asstActName4\":\"\",\"asstActName5\":\"\",\"asstActName6\":\"\",\"asstActName7\":\"\",\"asstActName8\":\"\",\"asstActNumber1\":\"YH008-028\",\"asstActNumber2\":\"\",\"asstActNumber3\":\"\",\"asstActNumber4\":\"\",\"asstActNumber5\":\"\",\"asstActNumber6\":\"\",\"asstActNumber7\":\"\",\"asstActNumber8\":\"\",\"asstActType1\":\"�ɱ�����\",\"asstActType2\":\"\",\"asstActType3\":\"\",\"asstActType4\":\"\",\"asstActType5\":\"\",\"asstActType6\":\"\",\"asstActType7\":\"\",\"asstActType8\":\"\",\"asstSeq\":0,\"attaches\":0,\"auditor\":\"\",\"bizDate\":\"2017-05-16\",\"bizNumber\":\"\",\"bookedDate\":\"2017-05-16\",\"cashflowAmountLocal\":0,\"cashflowAmountOriginal\":0,\"cashflowAmountRpt\":0,\"companyNumber\":\"01.01\",\"creator\":\"Ԥ���û�\",\"creditAmount\":1000,\"currencyNumber\":\"BB01\",\"cussent\":0,\"customerNumber\":\"\",\"debitAmount\":0,\"description\":\"\",\"entryDC\":-1,\"entrySeq\":2,\"icketNumber\":\"\",\"invoiceNumber\":\"\",\"isCheck\":false,\"itemFlag\":0,\"localRate\":0,\"measurement\":\"\",\"oppAccountSeq\":0,\"originalAmount\":1000,\"periodNumber\":5,\"periodYear\":2017,\"poster\":\"\",\"price\":0,\"primaryCoef\":0,\"primaryItem\":\"\",\"qty\":0,\"settlementNumber\":\"\",\"settlementType\":\"\",\"supplyCoef\":0,\"supplyItem\":\"\",\"tempNumber\":\"\",\"voucherAbstract\":\"PZ2017-05-16 12:50:05\",\"voucherNumber\":\"75691\",\"voucherType\":\"����\"}]";       
			       
			       vochersStr="[{\"accountNumber\":\"9410.09.03\",\"assistBizDate\":\"\",\"assistEndDate\":\"\",\"asstActName1\":\"\",\"asstActName2\":\"\",\"asstActName3\":\"\",\"asstActName4\":\"\",\"asstActName5\":\"\",\"asstActName6\":\"\",\"asstActName7\":\"\",\"asstActName8\":\"\",\"asstActNumber1\":\"01.01.009.002\",\"asstActNumber2\":\"\",\"asstActNumber3\":\"\",\"asstActNumber4\":\"\",\"asstActNumber5\":\"\",\"asstActNumber6\":\"\",\"asstActNumber7\":\"\",\"asstActNumber8\":\"\",\"asstActType1\":\"�ɱ�����\",\"asstActType2\":\"\",\"asstActType3\":\"\",\"asstActType4\":\"\",\"asstActType5\":\"\",\"asstActType6\":\"\",\"asstActType7\":\"\",\"asstActType8\":\"\",\"asstSeq\":0,\"attaches\":2,\"auditor\":\"\",\"bizDate\":\"2017-05-16\",\"bizNumber\":\"\",\"bookedDate\":\"2017-05-16\",\"cashflowAmountLocal\":0,\"cashflowAmountOriginal\":0,\"cashflowAmountRpt\":0,\"companyNumber\":\"01.01\",\"creator\":\"Ԥ���û�\",\"creditAmount\":0,\"currencyNumber\":\"BB01\",\"cussent\":0,\"customerNumber\":\"\",\"debitAmount\":1000,\"description\":\"\",\"entryDC\":1,\"entrySeq\":1,\"icketNumber\":\"\",\"invoiceNumber\":\"\",\"isCheck\":false,\"itemFlag\":0,\"localRate\":0,\"measurement\":\"\",\"oppAccountSeq\":0,\"originalAmount\":1000,\"periodNumber\":5,\"periodYear\":2017,\"poster\":\"\",\"price\":0,\"primaryCoef\":0,\"primaryItem\":\"\",\"qty\":0,\"settlementNumber\":\"\",\"settlementType\":\"\",\"supplyCoef\":0,\"supplyItem\":\"\",\"tempNumber\":\"\",\"voucherAbstract\":\"֧�������ձ���-1\",\"voucherNumber\":\"75691\",\"voucherType\":\"����\"}," +
			       			   "{\"accountNumber\":\"1002.01\",\"assistBizDate\":\"\",\"assistEndDate\":\"\",\"asstActName1\":\"\",\"asstActName2\":\"\",\"asstActName3\":\"\",\"asstActName4\":\"\",\"asstActName5\":\"\",\"asstActName6\":\"\",\"asstActName7\":\"\",\"asstActName8\":\"\",\"asstActNumber1\":\"YH008-028\",\"asstActNumber2\":\"\",\"asstActNumber3\":\"\",\"asstActNumber4\":\"\",\"asstActNumber5\":\"\",\"asstActNumber6\":\"\",\"asstActNumber7\":\"\",\"asstActNumber8\":\"\",\"asstActType1\":\"�����˻�\",\"asstActType2\":\"\",\"asstActType3\":\"\",\"asstActType4\":\"\",\"asstActType5\":\"\",\"asstActType6\":\"\",\"asstActType7\":\"\",\"asstActType8\":\"\",\"asstSeq\":0,\"attaches\":0,\"auditor\":\"\",\"bizDate\":\"2017-05-16\",\"bizNumber\":\"\",\"bookedDate\":\"2017-05-16\",\"cashflowAmountLocal\":0,\"cashflowAmountOriginal\":0,\"cashflowAmountRpt\":0,\"companyNumber\":\"01.01\",\"creator\":\"Ԥ���û�\",\"creditAmount\":1000,\"currencyNumber\":\"BB01\",\"cussent\":0,\"customerNumber\":\"\",\"debitAmount\":0,\"description\":\"\",\"entryDC\":-1,\"entrySeq\":2,\"icketNumber\":\"\",\"invoiceNumber\":\"\",\"isCheck\":false,\"itemFlag\":0,\"localRate\":0,\"measurement\":\"\",\"oppAccountSeq\":0,\"originalAmount\":1000,\"periodNumber\":5,\"periodYear\":2017,\"poster\":\"\",\"price\":0,\"primaryCoef\":0,\"primaryItem\":\"\",\"qty\":0,\"settlementNumber\":\"\",\"settlementType\":\"\",\"supplyCoef\":0,\"supplyItem\":\"\",\"tempNumber\":\"\",\"voucherAbstract\":\"PZ2017-05-16 12:50:05\",\"voucherNumber\":\"75691\",\"voucherType\":\"����\"}]";
			       
			       vochersStr="[{\"accountNumber\":\"9410.09.13\",\"assistBizDate\":\"\",\"assistEndDate\":\"\",\"asstActName1\":\"\",\"asstActName2\":\"\",\"asstActName3\":\"\",\"asstActName4\":\"\",\"asstActName5\":\"\",\"asstActName6\":\"\",\"asstActName7\":\"\",\"asstActName8\":\"\",\"asstActNumber1\":\"01.01.009.002\",\"asstActNumber2\":\"\",\"asstActNumber3\":\"\",\"asstActNumber4\":\"\",\"asstActNumber5\":\"\",\"asstActNumber6\":\"\",\"asstActNumber7\":\"\",\"asstActNumber8\":\"\",\"asstActType1\":\"�ɱ�����\",\"asstActType2\":\"\",\"asstActType3\":\"\",\"asstActType4\":\"\",\"asstActType5\":\"\",\"asstActType6\":\"\",\"asstActType7\":\"\",\"asstActType8\":\"\",\"asstSeq\":0,\"attaches\":2,\"auditor\":\"\",\"bizDate\":\"2017-05-16\",\"bizNumber\":\"\",\"bookedDate\":\"2017-05-16\",\"cashflowAmountLocal\":0,\"cashflowAmountOriginal\":0,\"cashflowAmountRpt\":0,\"companyNumber\":\"01.01\",\"creator\":\"Ӻ����\",\"creditAmount\":0,\"currencyNumber\":\"BB01\",\"cussent\":0,\"customerNumber\":\"\",\"debitAmount\":1000,\"description\":\"\",\"entryDC\":1,\"entrySeq\":1,\"icketNumber\":\"\",\"invoiceNumber\":\"\",\"isCheck\":false,\"itemFlag\":0,\"localRate\":0,\"measurement\":\"\",\"oppAccountSeq\":0,\"originalAmount\":1000,\"periodNumber\":5,\"periodYear\":2017,\"poster\":\"\",\"price\":0,\"primaryCoef\":0,\"primaryItem\":\"\",\"qty\":0,\"settlementNumber\":\"\",\"settlementType\":\"\",\"supplyCoef\":0,\"supplyItem\":\"\",\"tempNumber\":\"\",\"voucherAbstract\":\"֧�������ձ�������칫��ͶӰ����\",\"voucherNumber\":\"75691\",\"voucherType\":\"����\"},{\"accountNumber\":\"1002.01\",\"assistBizDate\":\"\",\"assistEndDate\":\"\",\"asstActName1\":\"\",\"asstActName2\":\"\",\"asstActName3\":\"\",\"asstActName4\":\"\",\"asstActName5\":\"\",\"asstActName6\":\"\",\"asstActName7\":\"\",\"asstActName8\":\"\",\"asstActNumber1\":\"YH008-028\",\"asstActNumber2\":\"\",\"asstActNumber3\":\"\",\"asstActNumber4\":\"\",\"asstActNumber5\":\"\",\"asstActNumber6\":\"\",\"asstActNumber7\":\"\",\"asstActNumber8\":\"\",\"asstActType1\":\"�����˻�\",\"asstActType2\":\"\",\"asstActType3\":\"\",\"asstActType4\":\"\",\"asstActType5\":\"\",\"asstActType6\":\"\",\"asstActType7\":\"\",\"asstActType8\":\"\",\"asstSeq\":0,\"attaches\":0,\"auditor\":\"\",\"bizDate\":\"2017-05-16\",\"bizNumber\":\"\",\"bookedDate\":\"2017-05-16\",\"cashflowAmountLocal\":0,\"cashflowAmountOriginal\":0,\"cashflowAmountRpt\":0,\"companyNumber\":\"01.01\",\"creator\":\"Ӻ����\",\"creditAmount\":1000,\"currencyNumber\":\"BB01\",\"cussent\":0,\"customerNumber\":\"\",\"debitAmount\":0,\"description\":\"\",\"entryDC\":-1,\"entrySeq\":2,\"icketNumber\":\"\",\"invoiceNumber\":\"\",\"isCheck\":false,\"itemFlag\":0,\"localRate\":0,\"measurement\":\"\",\"oppAccountSeq\":0,\"originalAmount\":1000,\"periodNumber\":5,\"periodYear\":2017,\"poster\":\"\",\"price\":0,\"primaryCoef\":0,\"primaryItem\":\"\",\"qty\":0,\"settlementNumber\":\"\",\"settlementType\":\"\",\"supplyCoef\":0,\"supplyItem\":\"\",\"tempNumber\":\"\",\"voucherAbstract\":\"PZ2017-05-16 13:49:22\",\"voucherNumber\":\"75691\",\"voucherType\":\"����\"}]";
			       
			       vochersStr="[{\"accountNumber\":\"6601.22.01\",\"assistBizDate\":\"\",\"assistEndDate\":\"\",\"asstActName1\":\"\",\"asstActName2\":\"\",\"asstActName3\":\"\",\"asstActName4\":\"\",\"asstActName5\":\"\",\"asstActName6\":\"\",\"asstActName7\":\"\",\"asstActName8\":\"\",\"asstActNumber1\":\"01.01.009.002\",\"asstActNumber2\":\"\",\"asstActNumber3\":\"\",\"asstActNumber4\":\"\",\"asstActNumber5\":\"\",\"asstActNumber6\":\"\",\"asstActNumber7\":\"\",\"asstActNumber8\":\"\",\"asstActType1\":\"�ɱ�����\",\"asstActType2\":\"\",\"asstActType3\":\"\",\"asstActType4\":\"\",\"asstActType5\":\"\",\"asstActType6\":\"\",\"asstActType7\":\"\",\"asstActType8\":\"\",\"asstSeq\":0,\"attaches\":1,\"auditor\":\"\",\"bizDate\":\"2017-05-30\",\"bizNumber\":\"\",\"bookedDate\":\"2017-05-30\",\"cashflowAmountLocal\":0,\"cashflowAmountOriginal\":0,\"cashflowAmountRpt\":0,\"companyNumber\":\"01.01\",\"creator\":\"Ӻ����\",\"creditAmount\":0,\"currencyNumber\":\"BB01\",\"cussent\":0,\"customerNumber\":\"\",\"debitAmount\":98300,\"description\":\"\",\"entryDC\":1,\"entrySeq\":1,\"icketNumber\":\"\",\"invoiceNumber\":\"\",\"isCheck\":false,\"itemFlag\":0,\"localRate\":0,\"measurement\":\"\",\"oppAccountSeq\":0,\"originalAmount\":98300,\"periodNumber\":5,\"periodYear\":2017,\"poster\":\"\",\"price\":0,\"primaryCoef\":0,\"primaryItem\":\"\",\"qty\":0,\"settlementNumber\":\"\",\"settlementType\":\"\",\"supplyCoef\":0,\"supplyItem\":\"\",\"tempNumber\":\"\",\"voucherAbstract\":\"֧�������ձ���\",\"voucherNumber\":\"75705\",\"voucherType\":\"����\"},{\"accountNumber\":\"2221.01.01\",\"assistBizDate\":\"\",\"assistEndDate\":\"\",\"asstActName1\":\"\",\"asstActName2\":\"\",\"asstActName3\":\"\",\"asstActName4\":\"\",\"asstActName5\":\"\",\"asstActName6\":\"\",\"asstActName7\":\"\",\"asstActName8\":\"\",\"asstActNumber1\":\"\",\"asstActNumber2\":\"\",\"asstActNumber3\":\"\",\"asstActNumber4\":\"\",\"asstActNumber5\":\"\",\"asstActNumber6\":\"\",\"asstActNumber7\":\"\",\"asstActNumber8\":\"\",\"asstActType1\":\"\",\"asstActType2\":\"\",\"asstActType3\":\"\",\"asstActType4\":\"\",\"asstActType5\":\"\",\"asstActType6\":\"\",\"asstActType7\":\"\",\"asstActType8\":\"\",\"asstSeq\":0,\"attaches\":0,\"auditor\":\"\",\"bizDate\":\"2017-05-30\",\"bizNumber\":\"\",\"bookedDate\":\"2017-05-30\",\"cashflowAmountLocal\":0,\"cashflowAmountOriginal\":0,\"cashflowAmountRpt\":0,\"companyNumber\":\"01.01\",\"creator\":\"Ӻ����\",\"creditAmount\":0,\"currencyNumber\":\"BB01\",\"cussent\":0,\"customerNumber\":\"\",\"debitAmount\":1700,\"description\":\"\",\"entryDC\":1,\"entrySeq\":2,\"icketNumber\":\"\",\"invoiceNumber\":\"\",\"isCheck\":false,\"itemFlag\":0,\"localRate\":0,\"measurement\":\"\",\"oppAccountSeq\":0,\"originalAmount\":1700,\"periodNumber\":5,\"periodYear\":2017,\"poster\":\"\",\"price\":0,\"primaryCoef\":0,\"primaryItem\":\"\",\"qty\":0,\"settlementNumber\":\"\",\"settlementType\":\"\",\"supplyCoef\":0,\"supplyItem\":\"\",\"tempNumber\":\"\",\"voucherAbstract\":\"֧�������ձ�������˰\",\"voucherNumber\":\"75705\",\"voucherType\":\"����\"},{\"accountNumber\":\"1002.01\",\"assistBizDate\":\"\",\"assistEndDate\":\"\",\"asstActName1\":\"\",\"asstActName2\":\"\",\"asstActName3\":\"\",\"asstActName4\":\"\",\"asstActName5\":\"\",\"asstActName6\":\"\",\"asstActName7\":\"\",\"asstActName8\":\"\",\"asstActNumber1\":\"YH008-023\",\"asstActNumber2\":\"\",\"asstActNumber3\":\"\",\"asstActNumber4\":\"\",\"asstActNumber5\":\"\",\"asstActNumber6\":\"\",\"asstActNumber7\":\"\",\"asstActNumber8\":\"\",\"asstActType1\":\"�����˻�\",\"asstActType2\":\"\",\"asstActType3\":\"\",\"asstActType4\":\"\",\"asstActType5\":\"\",\"asstActType6\":\"\",\"asstActType7\":\"\",\"asstActType8\":\"\",\"asstSeq\":0,\"attaches\":0,\"auditor\":\"\",\"bizDate\":\"2017-05-30\",\"bizNumber\":\"\",\"bookedDate\":\"2017-05-30\",\"cashflowAmountLocal\":0,\"cashflowAmountOriginal\":0,\"cashflowAmountRpt\":0,\"companyNumber\":\"01.01\",\"creator\":\"Ӻ����\",\"creditAmount\":100000,\"currencyNumber\":\"BB01\",\"cussent\":0,\"customerNumber\":\"\",\"debitAmount\":0,\"description\":\"\",\"entryDC\":-1,\"entrySeq\":3,\"icketNumber\":\"\",\"invoiceNumber\":\"\",\"isCheck\":false,\"itemFlag\":0,\"localRate\":0,\"measurement\":\"\",\"oppAccountSeq\":0,\"originalAmount\":100000,\"periodNumber\":5,\"periodYear\":2017,\"poster\":\"\",\"price\":0,\"primaryCoef\":0,\"primaryItem\":\"\",\"qty\":0,\"settlementNumber\":\"\",\"settlementType\":\"\",\"supplyCoef\":0,\"supplyItem\":\"\",\"tempNumber\":\"\",\"voucherAbstract\":\"֧�������ձ���\",\"voucherNumber\":\"75705\",\"voucherType\":\"����\"}]";
			       
			       vochersStr="[{\"accountNumber\":\"9410.12.01\",\"assistBizDate\":\"\",\"assistEndDate\":\"\",\"asstActName1\":\"\",\"asstActName2\":\"\",\"asstActName3\":\"\",\"asstActName4\":\"\",\"asstActName5\":\"\",\"asstActName6\":\"\",\"asstActName7\":\"\",\"asstActName8\":\"\",\"asstActNumber1\":\"01.01.011.005\",\"asstActNumber2\":\"\",\"asstActNumber3\":\"\",\"asstActNumber4\":\"\",\"asstActNumber5\":\"\",\"asstActNumber6\":\"\",\"asstActNumber7\":\"\",\"asstActNumber8\":\"\",\"asstActType1\":\"�ɱ�����\",\"asstActType2\":\"\",\"asstActType3\":\"\",\"asstActType4\":\"\",\"asstActType5\":\"\",\"asstActType6\":\"\",\"asstActType7\":\"\",\"asstActType8\":\"\",\"asstSeq\":0,\"attaches\":2,\"auditor\":\"\",\"bizDate\":\"2017-08-07\",\"bizNumber\":\"\",\"bookedDate\":\"2017-08-07\",\"cashflowAmountLocal\":0,\"cashflowAmountOriginal\":0,\"cashflowAmountRpt\":0,\"companyNumber\":\"01.01\",\"creator\":\"����\",\"creditAmount\":0,\"currencyNumber\":\"BB01\",\"cussent\":0,\"customerNumber\":\"\",\"debitAmount\":97,\"description\":\"\",\"entryDC\":1,\"entrySeq\":1,\"icketNumber\":\"\",\"invoiceNumber\":\"\",\"isCheck\":false,\"itemFlag\":0,\"localRate\":0,\"measurement\":\"\",\"oppAccountSeq\":0,\"originalAmount\":97,\"periodNumber\":8,\"periodYear\":2017,\"poster\":\"\",\"price\":0,\"primaryCoef\":0,\"primaryItem\":\"\",\"qty\":0,\"settlementNumber\":\"\",\"settlementType\":\"\",\"supplyCoef\":0,\"supplyItem\":\"\",\"tempNumber\":\"\",\"voucherAbstract\":\"֧��Ӻ����������Ʋ�ʮ��Ѳ����÷ѻ�����֧\",\"voucherNumber\":\"121700\",\"voucherType\":\"����\"},"+
			       			  "{\"accountNumber\":\"2221.01.14\",\"assistBizDate\":\"\",\"assistEndDate\":\"\",\"asstActName1\":\"\",\"asstActName2\":\"\",\"asstActName3\":\"\",\"asstActName4\":\"\",\"asstActName5\":\"\",\"asstActName6\":\"\",\"asstActName7\":\"\",\"asstActName8\":\"\",\"asstActNumber1\":\"\",\"asstActNumber2\":\"\",\"asstActNumber3\":\"\",\"asstActNumber4\":\"\",\"asstActNumber5\":\"\",\"asstActNumber6\":\"\",\"asstActNumber7\":\"\",\"asstActNumber8\":\"\",\"asstActType1\":\"\",\"asstActType2\":\"\",\"asstActType3\":\"\",\"asstActType4\":\"\",\"asstActType5\":\"\",\"asstActType6\":\"\",\"asstActType7\":\"\",\"asstActType8\":\"\",\"asstSeq\":0,\"attaches\":0,\"auditor\":\"\",\"bizDate\":\"2017-08-07\",\"bizNumber\":\"\",\"bookedDate\":\"2017-08-07\",\"cashflowAmountLocal\":0,\"cashflowAmountOriginal\":0,\"cashflowAmountRpt\":0,\"companyNumber\":\"01.01\",\"creator\":\"����\",\"creditAmount\":0,\"currencyNumber\":\"BB01\",\"cussent\":0,\"customerNumber\":\"\",\"debitAmount\":3,\"description\":\"\",\"entryDC\":1,\"entrySeq\":2,\"icketNumber\":\"\",\"invoiceNumber\":\"\",\"isCheck\":false,\"itemFlag\":0,\"localRate\":0,\"measurement\":\"\",\"oppAccountSeq\":0,\"originalAmount\":3,\"periodNumber\":8,\"periodYear\":2017,\"poster\":\"\",\"price\":0,\"primaryCoef\":0,\"primaryItem\":\"\",\"qty\":0,\"settlementNumber\":\"\",\"settlementType\":\"\",\"supplyCoef\":0,\"supplyItem\":\"\",\"tempNumber\":\"\",\"voucherAbstract\":\"֧��Ӻ����������Ʋ�ʮ��Ѳ����÷ѻ�����֧����˰\",\"voucherNumber\":\"121700\",\"voucherType\":\"����\"},"+
			       			  "{\"accountNumber\":\"9110.01\",\"assistBizDate\":\"\",\"assistEndDate\":\"\",\"asstActName1\":\"\",\"asstActName2\":\"\",\"asstActName3\":\"\",\"asstActName4\":\"\",\"asstActName5\":\"\",\"asstActName6\":\"\",\"asstActName7\":\"\",\"asstActName8\":\"\",\"asstActNumber1\":\"YH004-901\",\"asstActNumber2\":\"\",\"asstActNumber3\":\"\",\"asstActNumber4\":\"\",\"asstActNumber5\":\"\",\"asstActNumber6\":\"\",\"asstActNumber7\":\"\",\"asstActNumber8\":\"\",\"asstActType1\":\"�����˻�\",\"asstActType2\":\"\",\"asstActType3\":\"\",\"asstActType4\":\"\",\"asstActType5\":\"\",\"asstActType6\":\"\",\"asstActType7\":\"\",\"asstActType8\":\"\",\"asstSeq\":0,\"attaches\":0,\"auditor\":\"\",\"bizDate\":\"2017-08-07\",\"bizNumber\":\"\",\"bookedDate\":\"2017-08-07\",\"cashflowAmountLocal\":0,\"cashflowAmountOriginal\":0,\"cashflowAmountRpt\":0,\"companyNumber\":\"01.01\",\"creator\":\"����\",\"creditAmount\":0,\"currencyNumber\":\"BB01\",\"cussent\":0,\"customerNumber\":\"\",\"debitAmount\":0,\"description\":\"\",\"entryDC\":-1,\"entrySeq\":3,\"icketNumber\":\"\",\"invoiceNumber\":\"\",\"isCheck\":false,\"itemFlag\":0,\"localRate\":0,\"measurement\":\"\",\"oppAccountSeq\":0,\"originalAmount\":0,\"periodNumber\":8,\"periodYear\":2017,\"poster\":\"\",\"price\":0,\"primaryCoef\":0,\"primaryItem\":\"\",\"qty\":0,\"settlementNumber\":\"\",\"settlementType\":\"\",\"supplyCoef\":0,\"supplyItem\":\"\",\"tempNumber\":\"\",\"voucherAbstract\":\"֧��Ӻ����������Ʋ�ʮ��Ѳ����÷ѻ�����֧(CL-20170807001)\",\"voucherNumber\":\"121700\",\"voucherType\":\"����\"}]";
			       
			JSONArray jArray=JSONArray.fromObject(vochersStr); 
			
			data=(WSWSVoucher[])JSONArray.toArray(jArray, WSWSVoucher.class);
			
			System.out.println("CompanyNumber:"+data[0].getCompanyNumber());
			return data;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String voucherNumber="PZ" + sdf.format( new Date() );
		
		WSWSVoucher[] data = new WSWSVoucher[2]; 
		data[0] = new WSWSVoucher();
		data[0].setCompanyNumber("01.01"); //��˾����
		data[0].setBookedDate( "2017-04-21"); //��������,���̹鵵����
		data[0].setBizDate("2017-04-21"); //ҵ������
		data[0].setPeriodYear(2017); //����ڼ�-��
		data[0].setPeriodNumber(4); //����ڼ�-����
		data[0].setVoucherType("����"); //ƾ֤�֣�ƾ֤���ͣ�
		data[0].setVoucherNumber("1088"); //ƾ֤��
		data[0].setEntrySeq(1); //��¼�к�
		//data[0].setAccountNumber("1001"); //��Ŀ����
		data[0].setAccountNumber("9410.12.01"); //��Ŀ����
		
		data[0].setCurrencyNumber("BB01"); //���ֱ���
		data[0].setEntryDC(1); //��¼�з��� 1 �跽  -1����
		data[0].setOriginalAmount(299); //ԭ�ҽ��
		data[0].setDebitAmount(299); //�跽���
		data[0].setAttaches(0); //������
		//data[0].setCreditAmount(-102); //�������
		//data[0].setCreator("Ԥ���û�"); //�Ƶ���
		data[0].setCreator("Ӻ����"); //�Ƶ���
		//data[0].setItemFlag(0);
		data[0].setVoucherAbstract("�������ݣ�PZ"+TimeUtil.getCurrentTimeString()); //����
		
		data[0].setAsstActType1("�ɱ�����");//������Ŀ1
		data[0].setAsstActNumber1("01.05.01.07");//����������1
		//data[0].setAsstActName1("��������");//�����������1
		/*
		data[1] = new WSWSVoucher();
		data[1].setCompanyNumber("01.01"); //��˾����
		data[1].setBookedDate( "2017-04-21"); //��������,���̹鵵����
		data[1].setBizDate("2017-04-21"); //ҵ������
		data[1].setPeriodYear(2017); //����ڼ�-��
		data[1].setPeriodNumber(4); //����ڼ�-����
		data[1].setVoucherType("����"); //ƾ֤�֣�ƾ֤���ͣ�
		data[1].setVoucherNumber("1088"); //ƾ֤��
		data[1].setEntrySeq(1); //��¼�к�
		//data[0].setAccountNumber("1001"); //��Ŀ����
		data[1].setAccountNumber("1002.01"); //��Ŀ����
		
		data[1].setCurrencyNumber("BB01"); //���ֱ���
		data[1].setEntryDC(1); //��¼�з��� 1 �跽  -1����
		data[1].setOriginalAmount(299); //ԭ�ҽ��
		data[1].setDebitAmount(299); //�跽���
		//data[0].setCreditAmount(-102); //�������
		//data[0].setCreator("Ԥ���û�"); //�Ƶ���
		data[1].setCreator("Ӻ����"); //�Ƶ���
		//data[0].setItemFlag(0);
		data[1].setVoucherAbstract("�������ݣ�PZ"+TimeUtil.getCurrentTimeString()); //����
		
		data[1].setAsstActType1("������֯");//������Ŀ1
		data[1].setAsstActNumber1("00.02.01.04.02");//����������1
		*/
		
		data[1] = new WSWSVoucher();
		data[1].setCompanyNumber("01.01"); //��˾����
		data[1].setBookedDate( "2017-04-21"); //��������
		data[1].setBizDate("2017-04-21"); //ҵ������
		data[1].setPeriodYear(2017); //����ڼ�-��
		data[1].setPeriodNumber(4); //����ڼ�-����
		data[1].setVoucherType("����"); //ƾ֤�֣�ƾ֤���ͣ�
		data[1].setVoucherNumber("1088"); //ƾ֤��
		data[1].setEntrySeq(2); //��¼�к�	
		//data[1].setAccountNumber("1012.02"); //��Ŀ����
		data[1].setAccountNumber("1002.01"); //��Ŀ����
		data[1].setCurrencyNumber("BB01"); //���ֱ���
		data[1].setEntryDC(-1); //��¼�з��� 1 �跽  -1����
		data[1].setOriginalAmount(299); //ԭ�ҽ��
		//data[1].setDebitAmount(101); //�跽���
		data[1].setCreditAmount(299);//�������
		data[1].setAttaches(0); //������
		data[1].setCreator("Ӻ����"); //�Ƶ���
		//data[1].setItemFlag(0);
		data[1].setVoucherAbstract("�������ݣ�PZ"+TimeUtil.getCurrentTimeString()); //����
		
		
		
		//data[1].setAsstSeq(1);
		
		data[1].setAsstActType1("�����˻�");//������Ŀ1
		//data[1].setAsstActNumber1("0011");//����������1
		//data[1].setAsstActNumber1("0240");//����������1
		data[1].setAsstActNumber1("YH003-9001");//����������1
		
		/*
		WSWSVoucher[] data = new WSWSVoucher[2]; 
		data[0] = new WSWSVoucher();
		data[0].setCompanyNumber("00.01"); //��˾����
		data[0].setBookedDate( "2016-05-31"); //��������,���̹鵵����
		data[0].setBizDate("2016-08-09"); //ҵ������
		data[0].setPeriodYear(2016); //����ڼ�-��
		data[0].setPeriodNumber(03); //����ڼ�-����
		data[0].setVoucherType("��CD-����"); //ƾ֤�֣�ƾ֤���ͣ�
		data[0].setVoucherNumber(voucherNumber); //ƾ֤��
		data[0].setEntrySeq(1); //��¼�к�
		//data[0].setAccountNumber("1001"); //��Ŀ����
		data[0].setAccountNumber("6601.04.04"); //��Ŀ����
		
		data[0].setCurrencyNumber("BB01"); //���ֱ���
		data[0].setEntryDC(1); //��¼�з��� 1 �跽  -1����
		data[0].setOriginalAmount(105); //ԭ�ҽ��
		data[0].setDebitAmount(105); //�跽���
		//data[0].setCreditAmount(-102); //�������
		data[0].setCreator("Ԥ���û�"); //�Ƶ���
		//data[0].setItemFlag(0);
		data[0].setVoucherAbstract("PZ"+TimeUtil.getCurrentTimeString()); //����
		
		data[0].setAsstActType1("������֯");//������Ŀ1
		data[0].setAsstActNumber1("00.02.01.03");//����������1
		//data[0].setAsstActName1("��������");//�����������1
		
		
		data[1] = new WSWSVoucher();
		data[1].setCompanyNumber("00.02"); //��˾����
		data[1].setBookedDate( "2016-05-31"); //��������
		data[1].setBizDate("2016-08-09"); //ҵ������
		data[1].setPeriodYear(2016); //����ڼ�-��
		data[1].setPeriodNumber(03); //����ڼ�-����
		data[1].setVoucherType("��CD-����"); //ƾ֤�֣�ƾ֤���ͣ�
		data[1].setVoucherNumber(voucherNumber); //ƾ֤��
		data[1].setEntrySeq(2); //��¼�к�	
		//data[1].setAccountNumber("1012.02"); //��Ŀ����
		data[1].setAccountNumber("1002.02"); //��Ŀ����
		data[1].setCurrencyNumber("BB01"); //���ֱ���
		data[1].setEntryDC(-1); //��¼�з��� 1 �跽  -1����
		data[1].setOriginalAmount(105); //ԭ�ҽ��
		//data[1].setDebitAmount(101); //�跽���
		data[1].setCreditAmount(105);//�������
		data[1].setCreator("Ԥ���û�"); //�Ƶ���
		//data[1].setItemFlag(0);
		data[1].setVoucherAbstract("PZ"+TimeUtil.getCurrentTimeString()); //����
		
		
		
		//data[1].setAsstSeq(1);
		
		data[1].setAsstActType1("�����˻�");//������Ŀ1
		data[1].setAsstActNumber1("0011");//����������1
		
		*/
		//data[1].setAsstActName1("˴������˻�1422");//�����������1
		
		//data[0].accountNumber = "1001";
		////data[0].assistBizDate = 111;
		////data[0].assistEndDate = "";
		//data[0].asstSeq = 1;
		//data[0].attaches = 0;
		//data[0].auditor = "��ԪԪ";
		//data[0].bizDate = "2010-04-30";
		//data[0].bizNumber = "1";
		//data[0].bookedDate = "2010-04-30";
		//data[0].cashflowAmountLocal = 1001;
		//data[0].cashflowAmountOriginal = 1001;
		//data[0].cashflowAmountRpt = 1001;
		//data[0].companyNumber = "01.008";
		//data[0].cashflowAmountLocal = 1001.01;
		//data[0].setCashflowAmountOriginal(1001.01);
		//data[0].cashflowAmountRpt = 1001.01;
		return data;
	}
	
}
