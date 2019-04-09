package com.lw;

import weaver.general.BaseBean;
import weaver.interfaces.schedule.BaseCronJob;

public class DoWorkflowJob extends BaseCronJob {
		
	public void execute(){
		
		BaseBean log=new BaseBean();
		ErpToOa eto=new ErpToOa();
		
		log.writeLog("************************************** DoWorkflowJob 定时触发流程任务开始 *************************************");
		eto.doSync();
		log.writeLog("************************************** DoWorkflowJob 定时触发流程任务结束 *************************************");
		
	}
	
}
