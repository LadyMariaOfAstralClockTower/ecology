package com.sjc;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.hrm.company.DepartmentComInfo;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;


/**
 * 创建用户
 * @author peng
 *
 */


public class CreateHrmAction extends BaseBean implements Action {
	
	public String execute(RequestInfo info){
		
		writeLog("****************************进入 CreateHrmAction ****************************");
		
		boolean flag = true;
		
		String requestid = info.getRequestid();
	    String workflowid = info.getWorkflowid();
	    
	    String formtable = DataUtil.getFormtable(workflowid);
	    
	    RecordSet recordSet = new RecordSet();
	    
	    String sql="";
		
        writeLog("***********************用户创建开始***********************");
    	
    	sql="select * from "+formtable+" where requestid="+requestid;
    	recordSet.execute(sql);
    	if(recordSet.next()){
    		String wxczsjh1=recordSet.getString("wxczsjh1");//获取微信查账手机号1
    		String wxczsjh2=recordSet.getString("wxczsjh2");//获取微信查账手机号2
    		String wxczsjh3=recordSet.getString("wxczsjh3");//获取微信查账手机号3
    		
    		try {
    			//调用方法查看是否已存在该手机号的用户，若不存在则创建一个新用户
				createHrm(wxczsjh1,"1",formtable,requestid);
				createHrm(wxczsjh2,"2",formtable,requestid);
	    		createHrm(wxczsjh3,"3",formtable,requestid);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
    	}
    	
    	writeLog("***********************用户创建结束***********************");
		
    	return ((flag) ? "1" : "0");
	  }

	
	public void createHrm(String num,String index,String formtable,String requestid) throws Exception{
		
		  RecordSet rs = new RecordSet();
		  RecordSet rds = new RecordSet();
		  String sql = "select * from hrmresource where loginid='"+num+"' or lastname='"+num+"' or mobile='"+num+"'";
		  
		  writeLog("查询手机号的SQL："+sql);
		  
		  rs.execute(sql);
		  if(!rs.next()){
			  
			  writeLog("未查到该帐号！");
			  writeLog("开始创建新帐号。。。");
			  
			  rds.executeProc("HrmResourceMaxId_Get", "");
			  rds.next();
			  String id = String.valueOf(rds.getInt(1));
			  
			  String subcmpanyid1 = new DepartmentComInfo().getSubcompanyid1("72");
			  
			  sql = "insert into hrmresource(id,loginid,password,lastname,mobile,departmentid,subcompanyid1,status,seclevel,dsporder) values('"+id+"','"+num+"','C033EBAEDF3061AFEF1B7ACB94DE8DE0','"+num+"','"+num+"','72','"+subcmpanyid1+"','0','5','9999')";
			  
			  writeLog("创建帐号的SQL："+sql);
			  
			  rs.execute(sql);
			  
			  writeLog("帐号创建完毕！");
			  
			  sql="select * from hrmresource where id='"+id+"'";
			  
			  rs.execute(sql);
			  if(rs.next()){
				  
				  sql="update "+formtable+" set wxczzh"+index+"="+id+" where requestid="+requestid;
				  rds.execute(sql);
				  
				  writeLog("反馈的SQL为："+sql);
				  
			  } 
		  }
	  }
	
}
