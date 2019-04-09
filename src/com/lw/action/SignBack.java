package com.lw.action;

import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;
import weaver.general.TimeUtil;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class SignBack extends BaseBean implements Action {

	@Override
	public String execute(RequestInfo arg0) {
		// TODO Auto-generated method stub
		
		writeLog("**********************************  [SignBack]开始回写到CRM  ************************************");
		
		String wfid=arg0.getWorkflowid();
		String requestid=arg0.getRequestid();
		
		String formtable=getFormtable(wfid);
		
		RecordSet rs=new RecordSet();
		RecordSet rds=new RecordSet();
		RecordSetDataSource rsds=new RecordSetDataSource("CRM");
		
		String exam_time="";//审批时间
		String exam_user="";//审批人
		String exam_status="";//审批状态
		String exam_opinion="";//审批意见
		String exam_code="";//单据编号
		
		String sql="";
		
		sql="select * from workflow_requestBase where requestid="+requestid;
		
		rs.execute(sql);
		if(rs.next()){
			sql="select * from users where user_id="+rs.getString("lastoperator");
			rds.execute(sql);
			rds.next();
			exam_user=rds.getString("user_userid");
		}

		sql="select * from "+formtable+" where requestid="+requestid;
		
		writeLog("[SignBack]CRM触发的流程:"+sql);
		
		rs.execute(sql);
		if(rs.next()){
			
			exam_time=TimeUtil.getCurrentTimeString()+".000";
			exam_opinion=rs.getString("spyj");
			
			if(wfid.equals("176")){//报价审批
				exam_code=rs.getString("nequ_opprtunityid");
				exam_status=rs.getString("nequ_status");
			}else if(wfid.equals("207")){//服务报告
				exam_code=rs.getString("news_serviceno");
				exam_status=rs.getString("news_state");
			}else if(wfid.equals("206")){//服务请求
				exam_code=rs.getString("work_name");
				exam_status=rs.getString("news_state");
			}else if(wfid.equals("174")){//项目审批
				
				exam_code=rs.getString("oppo_code");
				exam_status=rs.getString("oppo_status");
				
			}
			
		}
		
		sql="insert into examine(exam_time,exam_user,exam_opinion,exam_code,exam_status,requestid) values(N'"+exam_time+"',N'"+exam_user+"',N'"+exam_opinion+"',N'"+exam_code+"',N'"+exam_status+"',N'"+requestid+"')";
		
		writeLog("[SignBack]回写到CRM的SQL:"+sql);
		
		rsds.execute(sql);
		
		writeLog("**********************************  [SignBack]完成回写到CRM  ************************************");
		
		return "1";
	}
	
	
	/**
	 * 获得表单table
	 * @param workflowid 流程id
	 * @return
	 */
	public static String getFormtable(String workflowid){
		String formtable="";
		String sql="select tablename from workflow_base t1,workflow_bill t2 where t1.id="+workflowid+" and t2.id=t1.formid";
		RecordSet recordSet=new RecordSet();
		recordSet.execute(sql);
		if(recordSet.next()){
			formtable=recordSet.getString("tablename");
		}
		return formtable;
	}
	


}
