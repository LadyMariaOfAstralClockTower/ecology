package com.sjc;

import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.TimeUtil;
import weaver.general.Util;

public class ModeUtil {

	/**
	 * 表单建模数据增加权限
	 * @param creatorid
	 * @param modeid
	 * @param formtable
	 */
	public static int addFormmodeRight(String creatorid,int modeid,String formtable){
		
		RecordSet recordSet2=new RecordSet();
		int billid=0;
		String sql="select max(id) as maxid from "+formtable;
		recordSet2.execute(sql);
		if(recordSet2.next()){
			billid=recordSet2.getInt("maxid");
		}
		
		String modedatacreatedate=TimeUtil.getCurrentDateString();
		String modedatacreatetime=TimeUtil.getOnlyCurrentTimeString();
		
		sql="update "+formtable+" set formmodeid="+modeid+",modedatacreatertype=0,modedatacreater="+creatorid+",modedatacreatedate='"+modedatacreatedate+"',modedatacreatetime='"+modedatacreatetime+"' where id="+billid;
		recordSet2.execute(sql);
		
		//构建数据权限
		ModeRightInfo ModeRightInfo = new ModeRightInfo();
		ModeRightInfo.setNewRight(true);
		ModeRightInfo.editModeDataShare(Util.getIntValue(creatorid),modeid,billid);
		
		return billid;
	}
	
	
	/**
	 * 表单建模数据增加权限
	 * @param creatorid
	 * @param modeid
	 * @param formtable
	 */
	public static int addFormmodeRight(int modeid,int billid,String formtable){
		
		RecordSet recordSet2=new RecordSet();
		
		String modedatacreatedate=TimeUtil.getCurrentDateString();
		String modedatacreatetime=TimeUtil.getOnlyCurrentTimeString();
		
		String sql="update "+formtable+" set formmodeid="+modeid+",modedatacreatertype=0,modedatacreater=1,modedatacreatedate='"+modedatacreatedate+"',modedatacreatetime='"+modedatacreatetime+"' where id="+billid;
		recordSet2.execute(sql);
		
		//构建数据权限
		ModeRightInfo ModeRightInfo = new ModeRightInfo();
		ModeRightInfo.setNewRight(true);
		ModeRightInfo.editModeDataShare(1,modeid,billid);
		
		return billid;
	}
	
}
