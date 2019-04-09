package com.cj;

import weaver.general.BaseBean;
import weaver.interfaces.schedule.BaseCronJob;

public class OrgPlanTask extends BaseCronJob {

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
		BaseBean log=new BaseBean();
		
		log.writeLog("[OrgPlanTask] *********************************  定时任务同步组织架构开始  ************************************");
		
		SyncOrg  syncOrg=new SyncOrg();
		
		syncOrg.doSync();
		
		log.writeLog("[OrgPlanTask] *********************************  定时任务同步组织架构结束  ************************************");
		
		
	}

	
	
	
	
}
