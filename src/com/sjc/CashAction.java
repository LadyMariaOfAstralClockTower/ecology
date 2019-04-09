package com.sjc;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;

import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * 合同签订
 * @author feng
 *
 */

public class CashAction extends BaseBean implements Action
{
  public String execute(RequestInfo info)
  {
    writeLog("****************************进入 CashAction ****************************");
    boolean flag = true;

    String requestid = info.getRequestid();
    String workflowid = info.getWorkflowid();
    
    
    CostUtil costUtil=new CostUtil();
    
    String formtable = DataUtil.getFormtable(workflowid);
    
    writeLog("[CashAction.execute] workflowid:"+workflowid+" formtable:"+formtable+" requestid:"+requestid);

    RecordSet rs=new RecordSet();
    RecordSet rds=new RecordSet();
    
    
    String sql="select t1.*,t2.sfxm,t2.xj from "+formtable+" t1,"+formtable+"_dt1 t2 where t1.id=t2.mainid and t2.sfxm in (0,1) and t1.requestId='"+requestid+"'";
    
    writeLog("[CashAction.execute]查询装修明细的sql："+sql);
    
    rs.execute(sql);
    while(rs.next()){
    	String sfxm=rs.getString("sfxm");
    	if("0".equals(sfxm)||"1".equals(sfxm)){
    		
    		if("0".equals(sfxm)){
    			sfxm="1";
    		}else if("1".equals(sfxm)){
    			sfxm="3";
    		}
    		
    		String xj=rs.getString("xj");//小计
    		String htbh=rs.getString("htbh");//合同编号
    		String qy=rs.getString("quyu");//区域
    		String spbh=rs.getString("spbh");//商铺编号
    		String dk=rs.getString("dikuai");//地块
    		String skr=rs.getString("skr");//收款人
    		
    		sql="insert into uf_yjjcxx(modedatacreater,yjlx,cwssje,je,htbh,xglc,qy,spbh,dk) values('"+skr+"','"+sfxm+"','"+xj+"','"+xj+"','"+htbh+"','"+requestid+"','"+qy+"','"+spbh+"','"+dk+"')";
    	    writeLog("[CashAction.execute]新增押金基础信息的sql："+sql);
    		rds.execute(sql);
    		
    		costUtil.addFormmodeRight(73, formtable, skr);
    		
    	}
    }
    
    writeLog("****************************结束 CashAction ****************************");
    
    return ((flag) ? "1" : "0");
  }

  
  
  
  public static void main(String[] args){
	  String str=",1,2,3,";
	  System.out.println(str.substring(1,str.length()-1));
  }
}