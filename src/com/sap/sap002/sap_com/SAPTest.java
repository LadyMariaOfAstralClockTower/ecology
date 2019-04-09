package com.sap.sap002.sap_com;

import com.sap.sap002.sap_com.holders.TABLE_OF_ZSINTTURNHolder;
import com.sap.sap002.sap_com.holders.TABLE_OF_ZSPRETURNHolder;

import weaver.conn.RecordSet;

public class SAPTest {

	public static void main(String[] args) {
		
		try {
		
			System.out.println("正在调用接口");
			
			ZSAP_OA_002_Service service = new ZSAP_OA_002_ServiceLocator();
			OA_002Stub serviceStub=(OA_002Stub)service.getOA_002();
			
			String BANFN="0010000000"; //采购申请号
			String BNFPO="0001"; //行项目
			String FRGZU="X"; //审批状态
			ZSPRETURN zspreturn=new ZSPRETURN(BANFN, BNFPO, FRGZU);
			ZSPRETURN[] zspreturns=new ZSPRETURN[1];
			zspreturns[0]=zspreturn;
			TABLE_OF_ZSPRETURNHolder IT_PRETURN=new TABLE_OF_ZSPRETURNHolder(zspreturns);
			
			//100000000000000001d
			int logid=10001;
			ZSINTTURN[] zsintturns=new ZSINTTURN[1];
			ZSINTTURN zsintturn=new ZSINTTURN((100000000000000001d+logid)+"", "000010", "", "M", "SAP_OA_002",BANFN+"/"+BNFPO+"/"+FRGZU,"00000001", "", "", "", "", "", "", "", "", "");
			zsintturns[0]=zsintturn;
			TABLE_OF_ZSINTTURNHolder IT_NECETR=new TABLE_OF_ZSINTTURNHolder(zsintturns);
			
			serviceStub.ZSAP_OA_002(IT_NECETR, IT_PRETURN);
			
			System.out.println("调用成功");
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
		
	}
	
}
