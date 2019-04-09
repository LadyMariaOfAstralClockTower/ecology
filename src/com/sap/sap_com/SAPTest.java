package com.sap.sap_com;

import com.sap.sap_com.holders.TABLE_OF_MAKTHolder;

public class SAPTest {

	public static void main(String[] args) {
		
		/*
		ZOA_TEST_Service service = new ZOA_TEST_Service(); 
		ZWSTEST ad = service.
		System.out.println(ad.zwsGetmaktx("A-0001"));
		*/
		
		try {
		
			ZOA_TEST_Service service = new ZOA_TEST_ServiceLocator();
			 
			ZOA_TEST_BindingStub serviceStub=(ZOA_TEST_BindingStub)service.getZOA_TEST();
			 
			TABLE_OF_MAKTHolder IT_ITEM=new TABLE_OF_MAKTHolder();
			String IV_STR="0001";
			
			String result=serviceStub.ZTEST_001(IT_ITEM, IV_STR);
			
			System.out.println("result:"+result);
			System.out.println("IT_ITEM:"+IT_ITEM);
			
			MAKT[] makts=IT_ITEM.value;
			
			for(int i=0;i<makts.length;i++){
				
				MAKT makt=makts[i];
				System.out.println("MAKTG:"+makt.getMAKTG()+" MAKTX:"+makt.getMAKTX()+
						" MANDT:"+makt.getMANDT()+" MATNR:"+makt.getMATNR()+" SPRAS:"+makt.getSPRAS());
				
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		
	}
	
}
