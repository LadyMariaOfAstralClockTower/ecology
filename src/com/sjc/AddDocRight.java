package com.sjc;

import weaver.conn.RecordSet;
import weaver.docs.docs.DocViewer;
import weaver.general.BaseBean;
import weaver.general.Util;

public class AddDocRight extends BaseBean {

	
	public void addRight(String projectid){
		RecordSet rs=new RecordSet();
		String members="";
		String s="select * from prj_ProjectInfo where id="+projectid;
		rs.execute(s);
		if(rs.next()){
			members=rs.getString("members");//获取该项目的成员
		}
		
		String docids=",";
		String sqlStr="select * from Exchange_Info where sortid in (select id from prj_TaskProcess where prjid="+projectid+") and ISNULL(docids,'')<>'' ";
		rs.execute(sqlStr);
		while(rs.next()){
			docids+=rs.getString("docids")+",";//获取该项目在交流下的文档
			
		}
		sqlStr="select * from Exchange_Info where sortid in (select id from prj_TaskProcess where prjid="+projectid+") and ISNULL(accessory,'')<>'' ";
		rs.execute(sqlStr);
		while(rs.next()){
			docids+=rs.getString("accessory").substring(1,rs.getString("accessory").length())+",";//获取该项目在交流下的附件
			
		}
		sqlStr="select * from Prj_Doc where prjid="+projectid;
		rs.execute(sqlStr);
		while(rs.next()){
			docids+=rs.getString("docid")+",";//获取该项目在项目文档下的文档
			
		}
		
		docids=docids.length()>1?docids.substring(1,docids.length()-1):"";	
		
		String[] ids=Util.TokenizerStringNew(docids,",");
		String[] hrmids=Util.TokenizerStringNew(members,",");
		for(int i=0;i<ids.length;i++){
			for(int j=0;j<hrmids.length;j++){
				addRight(ids[i],hrmids[j]);//将该项目下的每个文档共享给改项目下的每个成员
			}
		}
	}
	
	public void addRight(String docid,String userid){
		
		String str="select * from DocShare where docid='"+docid+"' and userid='"+userid+"'";
		RecordSet rds=new RecordSet();
		rds.execute(str);
		if(!rds.next()){
		
			String insertfield="docid,sharetype,seclevel,rolelevel,sharelevel,userid,subcompanyid,departmentid,roleid,foralluser,crmid,orgGroupId,downloadlevel,seclevelmax,joblevel,jobdepartment,jobsubcompany,jobids";
			String insertData="'"+docid+"','1','0','0','1','"+userid+"','0','0','0','0','0','0','1','255','0','0','0','0'";
			
			RecordSet rs=new RecordSet();
			String sql="insert into DocShare ("+insertfield+") values("+insertData+")";
			writeLog("新增文档共享权限的SQL："+sql);
			rs.execute(sql);
			DocViewer docViewer=new DocViewer();
			try {
				docViewer.setDocShareByDoc(docid);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				writeLog("文档共享权限出错："+e.toString());
			}
		}
	}
	
}
