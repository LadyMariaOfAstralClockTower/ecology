package com.test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class AfterAction extends BaseBean implements Action {

	HttpServletRequest request;
	HttpServletResponse response;

	
	@Override
	public String execute(RequestInfo info){
		// TODO Auto-generated method stub
		
		writeLog("****************************进入 AfterAction ****************************");
		
		String hdbm = request.getParameter("hdbm");
		
		RecordSet rs = new RecordSet();
	
		String sql = "select * from uf_wjbhwh where sybm = '"+hdbm+"'";
		
		rs.execute(sql);
		
	    return "1";

}
}