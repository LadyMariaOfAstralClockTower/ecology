package com.cj;

import weaver.general.BaseBean;
import weaver.interfaces.schedule.BaseCronJob;

public class DeptBudgetTask extends BaseCronJob {

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
		BaseBean log=new BaseBean();
		
		log.writeLog("[DeptBudgetTask] *********************************  定时任务同步部门预算开始  ************************************");
		
		DeptBudget deptBudget=new DeptBudget();
		
		deptBudget.doSync();
		
		log.writeLog("[DeptBudgetTask] *********************************  定时任务同步部门预算结束  ************************************");
		
		
	}
	
}
