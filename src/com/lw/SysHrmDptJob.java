package com.lw;

import weaver.general.BaseBean;
import weaver.interfaces.schedule.BaseCronJob;

public class SysHrmDptJob extends BaseCronJob {

	public void execute(){
		BaseBean log=new BaseBean();
		SysHrmDpt shd=new SysHrmDpt();
		
		log.writeLog("************************************** SysHrmDptJob 定时同步人员部门任务开始 *************************************");
		shd.doSync();
		log.writeLog("************************************** SysHrmDptJob 定时同步人员部门任务结束 *************************************");
		
	}
	
}


