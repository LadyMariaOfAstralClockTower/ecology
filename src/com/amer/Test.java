package com.amer;

import java.rmi.RemoteException;

import weaver.general.BaseBean;

import com.amer.webservice.FaseWebServiceProxy;
import com.amer.webservice.WebStoreModel;

public class Test {

	public static void main(String[] args) {
		
		
		System.out.println("status:"+getStatus());
		
		System.out.println("error:"+getError());
		
	}
	
	public static String getStatus(){
		String status="";
		try {
			
			FaseWebServiceProxy fsp=new FaseWebServiceProxy();
			WebStoreModel sm=fsp.getLocationData("testUser", "testPassword", "2018-01-01", "2018-01-01");
			status=sm.getStatus();
			//new BaseBean().writeLog("[getStatus()]status:"+status);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			new BaseBean().writeLog(e.getMessage());
		}
		return status;
	}
	
	public static String getError(){
		String error="";
		try {
			
			FaseWebServiceProxy fsp=new FaseWebServiceProxy();
			WebStoreModel sm=fsp.getLocationData("testUser", "testPassword", "2018-01-01", "2018-01-01");
			error=sm.getErrorValue();
			//new BaseBean().writeLog("[getError()]error:"+error);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			new BaseBean().writeLog(e.getMessage());
		}
		return error;
	}
}