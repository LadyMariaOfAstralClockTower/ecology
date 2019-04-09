package com.sjc;

import weaver.general.BaseBean;
import weaver.interfaces.schedule.BaseCronJob;

public class CostSyncJob extends BaseCronJob{

	public void execute() {
		BaseBean baseBean=new BaseBean();
		baseBean.writeLog("************************ CostSyncJob 定时同步任务开始 ************************");
		CostManager costManager=new CostManager();
		costManager.setzw("", "");
		costManager.delZwzx();
		baseBean.writeLog("************************ CostSyncJob 定时同步任务结束 ************************");
	}
	
}

