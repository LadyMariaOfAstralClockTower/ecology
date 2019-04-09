package com.jzx.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.jzx.EASServices.*;
import com.jzx.EASServices.EASLogin.*;
import com.jzx.EASServices.EASLogin.client.*;
import com.jzx.EASServices.WSGLWebServiceFacade.WSGLWebServiceFacadeSoapBindingStub;
import com.jzx.EASServices.WSGLWebServiceFacade.WSGLWebServiceFacadeSrvProxyService;
import com.jzx.EASServices.WSGLWebServiceFacade.WSGLWebServiceFacadeSrvProxyServiceLocator;
import com.jzx.EASServices.WSGLWebServiceFacade.wsvoucher.WSWSVoucher;
import com.sun.org.apache.bcel.internal.generic.NEW;


import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;
import weaver.general.TimeUtil;
import weaver.general.Util;
import weaver.hrm.company.DepartmentComInfo;
import weaver.hrm.resource.ResourceComInfo;

public class EASUtil extends BaseBean {
	
	/**
	 * 获取摘要类型
	 * @param userid
	 * @return
	 */
	public String getAbstractType(String userid){
		RecordSet rs=new RecordSet();
		String subcompanyid="";
		String sql="select * from hrmResource where id="+userid;
		rs.execute(sql);
		if(rs.next()){
			subcompanyid=rs.getString("subcompanyid1");
		}
		return getSubcompanyType(subcompanyid);
	}
	
	/**
	 * 获取分部类型
	 * @param subcompanyid
	 * @return
	 */
	public String getSubcompanyType(String subcompanyid){
		RecordSet rs=new RecordSet();
		String sql="select * from hrmSubcompany where id="+subcompanyid;
		rs.execute(sql);
		if(rs.next()){
			String supsubcomid=rs.getString("supsubcomid");
			if(supsubcomid.equals("5")){
				return "办公室";
			}else if(supsubcomid.equals("26")){
				return "店铺";
			}else{
				return getSubcompanyType(supsubcomid);
			}
		}else{
			return "";
		}
		
	}
	
	/**
	 * 获取凭证字
	 * @param pzzid
	 * @return
	 */
	public String getVoucherType(String pzzid){
		
		String voucherType="";
		RecordSet recordSet=new RecordSet();
		String sql="select * from uf_eas_pzz where id="+pzzid;
		recordSet.execute(sql);
		if(recordSet.next()){
			voucherType=recordSet.getString("pzz");
		}
		
		writeLog("[EASUtil.getVoucherType] pzzid:"+pzzid+" voucherType:"+voucherType);
		
		return voucherType;
	}
	
	/**
	 * 获取会计科目
	 * @param kjkm
	 * @return
	 */
	public String getAccountNumber(String kjkm){
		
		String accountNumber="";
		RecordSet recordSet=new RecordSet();
		String sql="select * from uf_eas_km where sxid='"+kjkm+"'";
		recordSet.execute(sql);
		if(recordSet.next()){
			accountNumber=recordSet.getString("kmbm");
		}
		
		writeLog("[EASUtil.getAccountNumber] kjkm:"+kjkm+" accountNumber:"+accountNumber);
		
		return accountNumber;
	}
	
	/**
	 * 获取贷方科目编码
	 * @param kjkm
	 * @return
	 */
	public String getDFAccountNumber(String zckm){
		
		String accountNumber="";
		RecordSet recordSet=new RecordSet();
		String sql="select * from uf_eas_yjckkm where id='"+zckm+"'";
		recordSet.execute(sql);
		if(recordSet.next()){
			accountNumber=recordSet.getString("kmbm");
		}
		
		writeLog("[EASUtil.getAccountNumber] zckm:"+zckm+" accountNumber:"+accountNumber);
		
		return accountNumber;
	}
	
	/**
	 * 获取贷方辅助账科目编码
	 * @param kjkm
	 * @return
	 */
	public String getDFAsstActNumber(String zfyhzh){
		
		String yhzhkm=""; //银行辅助账科目编码
		RecordSet recordSet=new RecordSet();
		String sql="select * from uf_eas_yhzhxx where id="+zfyhzh;
		recordSet.execute(sql);
		
		if(recordSet.next()){
			yhzhkm=recordSet.getString("yhzhkm"); //银行存款科目ID
		}
		
		writeLog("[EASUtil.getAccountNumber] zfyhzh:"+zfyhzh+" yhzhkm:"+yhzhkm);
		
		return yhzhkm;
	}
	
	/**
	 * 获取核算项目组织单元
	 * @param zzdy
	 * @return
	 */
	public Map<String, String> getAsstAct(String fid,String fzzlx){
		
		String ActType="";
		String ActNumber="";
		RecordSetDataSource dataSource = new RecordSetDataSource("EAS-TEST2"); //创建外部数据源
		fid=fid.substring(2);
		String sql="select * from T_ORG_BaseUnit where FID='"+fid+"'";
		if(fzzlx.equals("1")){
			sql="select * from T_BD_Customer where FID='"+fid+"'";
		}
		dataSource.execute(sql);
		if(dataSource.next()){
			ActType=dataSource.getString("FNAME_L2");
			ActNumber=dataSource.getString("FNumber");
		}
		
		Map<String, String> result=new HashMap<String, String>();
		result.put("ActType", ActType);
		result.put("ActNumber", ActNumber);
		
		writeLog("[EASUtil.getAsstAct] fzzlx:"+fzzlx+" fid:"+fid+" ActType:"+ActType+" ActNumber:"+ActNumber);
		
		return result;
	}
	
	public ResourceComInfo getResourceComInfo(){
		ResourceComInfo resourceComInfo=null;
		try {
			resourceComInfo=new ResourceComInfo();
		} catch (Exception e) {}
		return resourceComInfo;
	}
	
	public DepartmentComInfo getDepartmentComInfo(){
		DepartmentComInfo departmentComInfo=null;
		try {
			departmentComInfo=new DepartmentComInfo();
		} catch (Exception e) {}
		return departmentComInfo;
	}
	
	/**
	* web service客户端登陆
	* userName 用户名
	* password 密码
	* slnName eas
	* dcName 数据中心,对应的是dataCenter中数据中心id
	* language 语言 L2简体
	* dbType 数据库类型 0:sqlServer 1:db2 2:oracle
	* authPattern 验证方式 默认 "BaseDB" ; 其他认证方式KEY可从easAuthPatterns.xml中获取
	*/
	public String doLogin(){
		
		String sessionid="";
		try {
			
			EASLoginProxyService service = new EASLoginProxyServiceLocator();
			EASLoginSoapBindingStub serviceStub= (EASLoginSoapBindingStub) service.getEASLogin();
			 
			//WSContext ctx = serviceStub.login("administrator", "123", "eas", "006", "L2", 0);
			
			//WSContext ctx = serviceStub.login("user", "kduser", "eas", "010", "L2", 0);
			
			WSContext ctx = serviceStub.login("000070", "kaoqin", "eas", "02", "L2", 0);
			
			//WSContext ctx = serviceStub.login("user", "kduser", "eas", "007", "L2", 0);
			 
			sessionid=ctx.getSessionId();
			
			//成功登录后打印sessionId
			System.out.println(ctx.getSessionId());
			System.out.println(ctx.getUserName());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return sessionid;
	}
	
	/**
	 * 导入凭证
	 * @param vouchers
	 * @return
	 */
	public boolean importVoucher(WSWSVoucher[] vouchers,String workflowid,String requestid) {
		
		boolean flag=true;
		String result="";
		try {
		 
			 WSGLWebServiceFacadeSrvProxyService service=new WSGLWebServiceFacadeSrvProxyServiceLocator();
			 WSGLWebServiceFacadeSoapBindingStub serviceStub=(WSGLWebServiceFacadeSoapBindingStub)service.getWSGLWebServiceFacade();
			 
			 String[] results=serviceStub.importVoucher(vouchers, 0, 1, 1);
			 result=results[0];
			 
			 if(!result.startsWith("0000||")){
				 flag=false;
				 writeLog("[EASUtil.importVoucher] 导入失败 error:"+result);
				 addWorkflowLog(workflowid, requestid,"1", result);
			 }else{
				 writeLog("[EASUtil.importVoucher] 导入成功 error:"+result);
				 addWorkflowLog(workflowid, requestid,"0", result);
			 }
			 
		}catch (Exception e) {
			flag=false;
			writeLog("[EASUtil.importVoucher] 导入失败 error:"+result+" exception:"+e.toString());
			addWorkflowLog(workflowid, requestid,"1", e.toString());
		}
		return flag;
	}
	
	/**
	 * 获取表单selectname值
	 * @param fieldid
	 * @param selectvalue
	 * @return
	 */
	public static String getSelectValue(String fieldid,String selectName){
		RecordSet recordSet=new RecordSet();
		String sql="SELECT selectvalue FROM workflow_SelectItem WHERE fieldid="+fieldid+" and selectName='"+selectName+"'";
		
		recordSet.execute(sql);
		String selectvalue="";
		if(recordSet.next()){
			selectvalue=recordSet.getString("selectvalue");
		}
		
		return selectvalue;
	}
	
	public static String getSelectName(String fieldid,String selectVlaue){
		RecordSet recordSet=new RecordSet();
		String sql="SELECT selectName FROM workflow_SelectItem WHERE fieldid="+fieldid+" and selectValue='"+selectVlaue+"'";
		
		recordSet.execute(sql);
		String selectvalue="";
		if(recordSet.next()){
			selectvalue=recordSet.getString("selectName");
		}
		
		return selectvalue;
	}
	/**
	 * 税率会计科目
	 * @param fieldid
	 * @param selectVlaue
	 * @return
	 */
	public static String getSlkjkm(String fieldid,String selectVlaue){
		
		String slkjkm="";
		String sl=getSelectName(fieldid,selectVlaue); //税率
		
		if(sl.equals("0.03")){
			slkjkm="2221.01.14";
		}else if(sl.equals("0.05")){
			slkjkm="2221.01.11";
		}else if(sl.equals("0.06")){
			slkjkm="2221.01.12";
		}else if(sl.equals("0.11")){
			slkjkm="2221.01.13";
		}else if(sl.equals("0.13")){
			slkjkm="2221.01.15";
		}else if(sl.equals("0.17")){
			slkjkm="2221.01.01";
		}
		
		return slkjkm;
	}
	
	/**
	 * 增加流程日志
	 * @param workflowid 流程id
	 * @param requestid 请求id
	 * @param flag 0为导入成功，1为导入失败
	 * @param exception 异常信息
	 */
	public void addWorkflowLog(String workflowid,String requestid,String flag,String exception){
		
		String jg="";  //结果
		String workflowname="";  //流程名称
		String jjff="";  //解决方法
		
		if("0".equals(flag)){
			jg="凭证导入成功";
		}else{
			jg="凭证导入失败";
		}
		
		jjff=getMethod(exception);
		
		RecordSet rs=new RecordSet();
		RecordSet rds=new RecordSet();
		
		String sql="select * from workflow_base where id="+workflowid;
		rs.execute(sql);
		if(rs.next()){
			workflowname=rs.getString("workflowname");
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date=sdf.format(new Date());
			String lcbh="";
			
			String str="select * from workflow_requestbase where requestid="+requestid;
			rs.execute(str);
			if(rs.next()){
				lcbh=rs.getString("requestmark");//获取流程编号
			}
			
			sql="insert into formtable_main_187(lcid,qqid,lcmc,qqmc,jg,ycrz,jjff,pzsj,lcbh) values('"+workflowid+"','"+requestid+"','"+workflowname+"','"+requestid+"','"+jg+"','"+exception+"','"+jjff+"','"+date+"','"+lcbh+"')";
			writeLog("新增流程日志的sql："+sql);
			rds.execute(sql);
			addFormmodeRight(2,"formtable_main_187",0,"1");
		}
	}
	
	
	/**
	 * 获取解决方法
	 * @param exception 异常信息
	 */
	public static String getMethod(String exception){
		String jjff="";
		
		if("java.lang.ClassCastException: org.apache.xerces.parsers.XML11Configuration cannot be cast to org.apache.xerces.xni.parser.XMLParserConfiguration".equals(exception)){
			jjff="手动重启OA服务";
		}
		
		return jjff;
	}
	
	
	/**
	 * 表单建模数据增加权限
	 * @param creatorid
	 * @param modeid
	 * @param formtable
	 */
	public static void addFormmodeRight(int modeid,String formtable,int billid,String createrid){
		
		String createdate=TimeUtil.getCurrentDateString();
		String createtime=TimeUtil.getOnlyCurrentTimeString();
		
		RecordSet recordSet2=new RecordSet();
		if(billid==0){
			String sql="select max(id) as maxid from "+formtable;
			recordSet2.execute(sql);
			if(recordSet2.next()){
				billid=recordSet2.getInt("maxid");
			}
		}
		String sql="update "+formtable+" set formmodeid='"+modeid+"',modedatacreatertype='0',modedatacreater='"+createrid+"',modedatacreatedate='"+createdate+"',modedatacreatetime='"+createtime+"' where id="+billid;
		recordSet2.execute(sql);
		
		//构建数据权限
		ModeRightInfo ModeRightInfo = new ModeRightInfo();
		ModeRightInfo.setNewRight(true);
		ModeRightInfo.editModeDataShare(Util.getIntValue(createrid),modeid,billid);
		
	}
	
	/**
	 * 根据辅助账类型设置
	 * @param creatorid
	 * @param modeid
	 * @param formtable
	 */
	public void setAsstAct(WSWSVoucher voucher,String fzzlx,String EASzzdw,String EASkh,String EASgys,String EASzy){
		
		String AsstActType1=""; //核算项目1
		String AsstActNumber1="";//核算对象编码1
		String AsstActType2=""; //核算项目2
		String AsstActNumber2="";//核算对象编码2
		
		writeLog("fzzlx:"+fzzlx+" EASzzdw:"+EASzzdw+" EASkh:"+EASkh+" EASgys:"+EASgys+" EASzy:"+EASzy);
		
		if(fzzlx.equals("01")){
			AsstActType1="成本中心";
			AsstActNumber1=EASzzdw;
		}else if(fzzlx.equals("02")){
			
			AsstActType1="供应商";
			AsstActNumber1=EASgys;
		}else if(fzzlx.equals("03")){
			
			AsstActType1="成本中心";
			AsstActNumber1=EASzzdw;
			
			AsstActType2="供应商";
			AsstActNumber2=EASgys;
		}else if(fzzlx.equals("04")){
			
			AsstActType1="客户";
			AsstActNumber1=EASkh;
		}else if(fzzlx.equals("05")){
			
			AsstActType1="成本中心";
			AsstActNumber1=EASzzdw;
			
			AsstActType2="客户";
			AsstActNumber2=EASkh;
		}else if(fzzlx.equals("10")){
			
			AsstActType1="职员";
			AsstActNumber1=EASzy;
			
		}else if(fzzlx.equals("11")){
			
			AsstActType1="成本中心";
			AsstActNumber1=EASzzdw;
			
			AsstActType2="职员";
			AsstActNumber2=EASzy;
			
		}
		
		if(!AsstActType1.equals("")){
		
			voucher.setAsstActType1(AsstActType1);//核算项目1
			voucher.setAsstActNumber1(AsstActNumber1);//核算对象编码1
			
			if(fzzlx.equals("05")||fzzlx.equals("03")||fzzlx.equals("11")){
				voucher.setAsstActType2(AsstActType2);//核算项目1
				voucher.setAsstActNumber2(AsstActNumber2);//核算对象编码1
			}
			
		}
			
	}
	
	
	
}
