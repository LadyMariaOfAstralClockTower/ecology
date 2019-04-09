package com.sjc;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class FinanceAction extends BaseBean
  implements Action
{
  public String execute(RequestInfo info)
  {
    writeLog("****************************进入 FinanceAction ****************************");

    String requestid = info.getRequestid();
    String workflowid = info.getWorkflowid();

    String formtable = DataUtil.getFormtable(workflowid);

    writeLog("[FinanceAction.execute] workflowid:" + workflowid + " formtable:" + formtable + " requestid:" + requestid);

    String htbh = "";
    String sflx = "";

    RecordSet rs = new RecordSet();

    String sql = "select * from " + formtable + " where requestid=" + requestid;

    rs.execute(sql);

    while (rs.next()) {
      htbh = rs.getString("htbh_2");
      sflx = rs.getString("sflx");
    }

    String fyxms = ",";
    String ysqjs = ",";
    if (sflx.equals("0")){
      sql = "select t2.* from " + formtable + " t1," + formtable + "_dt1 t2 where requestid=" + requestid + " and t1.id=t2.mainid";
    }else if (sflx.equals("1")){
      sql = "select t2.* from " + formtable + " t1," + formtable + "_dt2 t2 where requestid=" + requestid + " and t1.id=t2.mainid";
    }
      
    writeLog("[FinanceAction.execute] 查询收费项目，应收日期 sql:" + sql);
    rs.execute(sql);
    while (rs.next()) {
      String fyxm = rs.getString("fymc");
      String ysqj = rs.getString("fyqj");

      if (!(fyxms.contains("," + fyxm + ","))){
        fyxms = fyxms + fyxm + ",";
      }  

      if (!(ysqjs.contains("," + ysqj + ","))){
        ysqjs = ysqjs + ysqj + ",";
      }  

    }

    fyxms = (fyxms.length() > 1) ? fyxms.substring(1, fyxms.length() - 1) : "";
    ysqjs = (ysqjs.length() > 1) ? ysqjs.substring(1, ysqjs.length() - 1) : "";

    if (sflx.equals("1")){
      sql = "update " + formtable + " set fyxm ='" + fyxms + "',ysqj = '" + ysqjs + "' where requestid=" + requestid;
    }else if (sflx.equals("0")) {
      sql = "update " + formtable + " set fyxm ='" + fyxms + "',ysqj = '" + ysqjs + "',htbh = '" + htbh + "' where requestid=" + requestid;
    }

    writeLog("[FinanceAction.execute] 更新收费项目，应收日期 sql:" + sql);

    rs.execute(sql);
    
    writeLog("****************************结束 FinanceAction ****************************");

    return "1";
  }
}