package com.gglh;

import java.util.ArrayList;
import java.util.List;

import weaver.general.BaseBean;
import weaver.interfaces.schedule.BaseCronJob;

public class SycHrmOrgJob extends BaseCronJob{

	public void execute() {
		
		BaseBean baseBean=new BaseBean();
		baseBean.writeLog("SycHrmOrgJob 定时同步任务开始");
		SycHrmOrg sycHrmOrg=new SycHrmOrg();
		
		//sycHrmOrg.SynHrmOrg();
		//sycHrmOrg.SynHrm();
		
		baseBean.writeLog("SycHrmOrgJob 定时同步任务结束");
	}
	
}

