package com.ltzy.webservices;

import java.io.PrintStream;
import java.util.Calendar;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.company.DepartmentComInfo;
import weaver.hrm.company.SubCompanyComInfo;
import weaver.hrm.job.JobTitlesComInfo;
import weaver.hrm.resource.ResourceComInfo;
import weaver.interfaces.schedule.BaseCronJob;

public class HRSynDAO
  extends BaseCronJob
{
  private BaseBean log = new BaseBean();
  private String showsqllog = this.log.getPropValue("SHOWSQLLOG", "showsqllog");
  private String companyds = "";
  
  public void execute()
  {
    SynSubCompany();
    SynDept();
    SynPost();
    synHrmResource();
  }
  
  public void SynSubCompany()
  {
    try
    {
      RecordSet rs = new RecordSet();
      RecordSet rs1 = new RecordSet();
      RecordSetDataSource hrrs = new RecordSetDataSource("hr");
      

      rs.executeSql("select companyid from formtable_main_177");
      while (rs.next()) {
        this.companyds = 
          (this.companyds + " '" + rs.getString("companyid") + "' ,");
      }
      if (!"".equals(this.companyds)) {
        this.companyds = this.companyds
          .substring(0, this.companyds.length() - 1);
      }
      String sql = "select t1.dept_name,t1.dept_code,t1.OSTATUS,t1.enablestate,t2.dept_code as f_dept_code from NC63.ORG_DEPT_view1 t1 left join NC63.ORG_DEPT_view1 t2 on t1.pk_father_dept=t2.pk_dept  where t1.def3='Y' and t1.org_code IN (" + 
        this.companyds + ") ";
      hrrs.executeSql(sql);
      while (hrrs.next())
      {
        String deptOstatus =Util.null2String(hrrs.getString("OSTATUS"));
        String deptEnablestate =Util.null2String(hrrs.getString("enablestate"));
        
        String dept_name = Util.null2String(hrrs.getString("dept_name"));
        String dept_code = Util.null2String(hrrs.getString("dept_code"));
        String f_dept_code = Util.null2String(hrrs.getString("f_dept_code"));
        String supsubcomid = "0";
        
        rs1.executeSql("select id from HrmSubCompany where subcompanycode = '" + 
          f_dept_code + 
          "' and subcompanycode !='' and subcompanycode is not null ");
        if ("showsqllog".equals(this.showsqllog)) {
          this.log.writeLog("sql2====select id from HrmSubCompany where subcompanycode = '" + 
            f_dept_code + "'");
        }
        if (rs1.next()) {
          supsubcomid = Util.null2String(rs1.getString("id"));
        }
        rs.executeSql("select id from HrmSubCompany where subcompanycode = '" + 
          dept_code + "'");
        if ("showsqllog".equals(this.showsqllog)) {
          this.log.writeLog("sql1===select id from HrmSubCompany where subcompanycode = '" + 
            dept_code + "'");
        }
        if (!rs.next())
        {
          sql = 
          



            "insert into hrmsubcompany(subcompanyname,subcompanydesc,companyid,supsubcomid,subcompanycode) values ('" + dept_name + "','" + dept_name + "',1," + supsubcomid + ",'" + dept_code + "')";
          
          rs1.executeSql(sql);
          if ("showsqllog".equals(this.showsqllog)) {
            this.log.writeLog("sql3=" + sql);
          }
          int id = 0;
          rs1.executeSql("select id from HrmSubCompany where subcompanycode = '" + 
            dept_code + "'");
          if (rs1.next())
          {
            id = rs1.getInt(1);
            rs1.executeSql("insert into leftmenuconfig (userid,infoid,visible,viewindex,resourceid,resourcetype,locked,lockedbyid,usecustomname,customname,customname_e)  select  distinct  userid,infoid,visible,viewindex," + 
              id + 
              ",2,locked,lockedbyid,usecustomname,customname,customname_e from leftmenuconfig where resourcetype=1  and resourceid=1");
            rs1.executeSql("insert into mainmenuconfig (userid,infoid,visible,viewindex,resourceid,resourcetype,locked,lockedbyid,usecustomname,customname,customname_e)  select  distinct  userid,infoid,visible,viewindex," + 
              id + 
              ",2,locked,lockedbyid,usecustomname,customname,customname_e from mainmenuconfig where resourcetype=1  and resourceid=1");
          }
          this.log.writeLog("新增分部成功," + sql);
        }
        else
        {
          sql = 
            "update HrmSubCompany set subcompanyname='" + dept_name + "',subcompanydesc='" + dept_name + "',supsubcomid='" + supsubcomid + "',canceled='0' where subcompanycode='" + dept_code + "'";
          rs.executeSql(sql);
          this.log.writeLog("编辑分部成功," + sql);
        }
        delSubCompany(dept_code, dept_name);
        if ((deptOstatus.trim().equals("Y")) || (deptEnablestate.trim().equals("N")))
        {
          this.log.writeLog("该分部在NC中属于撤销或未启用状态，进行封存,分部ID" + dept_code);
          rs.executeSql("update HrmSubCompany set canceled='1' where subcompanycode='" + 
            dept_code + "'");
        }
      }
      if (hrrs.getCounts() > 0) {
        try
        {
          SubCompanyComInfo dci = new SubCompanyComInfo();
          dci.removeCompanyCache();
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
      return;
    }
    catch (Exception e)
    {
      this.log.writeLog("同步分部失败," + e);
    }
  }
  
  public void delSubCompany(String code, String shortname)
  {
    try
    {
      RecordSet rs = new RecordSet();
      String sql = "";
      sql = "select count(id) from hrmdepartment where EXISTS (select 1 from hrmsubcompany b where hrmdepartment.subcompanyid1=b.id and b.subcompanycode='" + 
        code + "') and (canceled='0' or canceled is null)";
      rs.executeSql(sql);
      int rows = 0;
      while (rs.next()) {
        rows += Util.getIntValue(rs.getString(1), 0);
      }
      if (rows > 0)
      {
        this.log.writeLog("封存分部失败，该分部下有正常的部门，分部名称：" + shortname);
        sql = "update HrmSubCompany set canceled='0' where subcompanycode='" + 
          code + "'";
        rs.executeSql(sql);
      }
      else
      {
        sql = 
          "update HrmSubCompany set canceled='1' where subcompanycode='" + code + "'";
        rs.executeSql(sql);
        this.log.writeLog("封存分部成功," + sql);
      }
    }
    catch (Exception e)
    {
      this.log.writeLog("封存分部失败," + e);
    }
  }
  
  public void SynDept()
  {
    try
    {
      RecordSet rs = new RecordSet();
      RecordSet rs1 = new RecordSet();
      RecordSet rs2 = new RecordSet();
      RecordSetDataSource hrrs = new RecordSetDataSource("hr");
      if ("".equals(this.companyds))
      {
        rs.executeSql("select companyid from formtable_main_177");
        while (rs.next()) {
          this.companyds = 
            (this.companyds + " '" + rs.getString("companyid") + "' ,");
        }
        if (!"".equals(this.companyds)) {
          this.companyds = this.companyds
            .substring(0, this.companyds.length() - 1);
        }
      }
      String sql = "select t1.dept_name ,t1.dept_code,t1.OSTATUS,t1.enablestate,t2.dept_code as f_dept_code,t1.ORG_CODE from NC63.ORG_DEPT_view1 t1 left join NC63.ORG_DEPT_view1 t2 on t1.pk_father_dept=t2.pk_dept  where t1.def3 != 'Y' and t1.DEPT_CODE not like '%a%' and t1.org_code IN (" + 
        this.companyds + ")  ";
      hrrs.executeSql(sql);
      while (hrrs.next())
      {
        String deptOstatus = 
          Util.null2String(hrrs.getString("OSTATUS"));
        String deptEnablestate = 
          Util.null2String(hrrs.getString("enablestate"));
        

        String dept_name = 
          Util.null2String(hrrs.getString("dept_name"));
        String dept_code = 
          Util.null2String(hrrs.getString("dept_code"));
        int subcomid = 0;
        int supdeptid = 0;
        rs.executeSql("select id from hrmdepartment where departmentcode = '" + 
          dept_code + 
          "' and departmentcode!='' and departmentcode is not null ");
        if (!rs.next())
        {
          String sql_insert = "insert into hrmdepartment(departmentname,departmentmark,subcompanyid1,supdepid,showorder,departmentcode) values ('" + 
            dept_name + 
            "','" + 
            dept_name + 
            "'," + 
            subcomid + 
            "," + supdeptid + ",'','" + dept_code + "')";
          rs1.executeSql(sql_insert);
          if ("showsqllog".equals(this.showsqllog)) {
            this.log.writeLog("新增部门成功," + sql_insert);
          }
        }
        delDepartment(dept_code, dept_name);
        if ((deptOstatus.trim().equals("Y")) || (deptEnablestate.trim().equals("N")))
        {
          this.log.writeLog("该部门在NC中属于撤销或未启用状态，进行封存,部门ID" + dept_code);
          rs.executeSql("update hrmdepartment set canceled='1' where departmentcode='" + 
            dept_code + "'");
        }
      }
      hrrs.beforFirst();
      while (hrrs.next())
      {
        String deptOstatus = 
          Util.null2String(hrrs.getString("OSTATUS"));
        String deptEnablestate = 
          Util.null2String(hrrs.getString("enablestate"));
        
        String dept_name = 
          Util.null2String(hrrs.getString("dept_name"));
        String dept_code = 
          Util.null2String(hrrs.getString("dept_code"));
        String f_dept_code = Util.null2String(hrrs
          .getString("f_dept_code"));
        String ORG_CODE = Util.null2String(hrrs.getString("ORG_CODE"));
        int subcomid = 0;
        int supdeptid = 0;
        if (!"".equals(f_dept_code))
        {
          rs1.executeSql("select id from HrmSubCompany where subcompanycode = '" + 
            f_dept_code + 
            "' and subcompanycode !='' and subcompanycode is not null ");
          if ("showsqllog".equals(this.showsqllog)) {
            this.log.writeLog("sql11====select id from HrmSubCompany where subcompanycode = '" + 
              f_dept_code + "'");
          }
          if (rs1.next())
          {
            subcomid = Util.getIntValue(rs1.getString("id"), 0);
          }
          else
          {
            rs2.executeSql("select id,subcompanyid1 from hrmdepartment where departmentcode = '" + 
              f_dept_code + "'");
            if (rs2.next())
            {
              supdeptid = 
                Util.getIntValue(rs2.getString("id"), 0);
              subcomid = Util.getIntValue(
                rs2.getString("subcompanyid1"), 0);
            }
          }
        }
        else
        {
          rs1.executeSql("select id from HrmSubCompany where subcompanycode = '" + 
            ORG_CODE + 
            "' and subcompanycode !='' and subcompanycode is not null ");
          if ("showsqllog".equals(this.showsqllog)) {
            this.log.writeLog("sql11====select id from HrmSubCompany where subcompanycode = '" + 
              ORG_CODE + "'");
          }
          if (rs1.next()) {
            subcomid = Util.getIntValue(rs1.getString("id"), 0);
          }
        }
        this.log.writeLog("supdeptid:" + supdeptid + ",subcomid:" + 
          subcomid);
        sql = "update hrmdepartment set subcompanyid1=" + subcomid + 
          ", departmentname='" + dept_name + 
          "',departmentmark='" + dept_name + "',supdepid=" + 
          supdeptid + ",canceled='0' where departmentcode='" + 
          dept_code + "'";
        rs.executeSql(sql);
        if ("showsqllog".equals(this.showsqllog)) {
          this.log.writeLog("更新OA部门的sql:" + sql);
        }
        if ((deptOstatus.trim().equals("Y")) || (deptEnablestate.trim().equals("N")))
        {
          this.log.writeLog("该部门在NC中属于撤销或未启用状态，进行封存,部门ID" + dept_code);
          rs.executeSql("update hrmdepartment set canceled='1' where departmentcode='" + 
            dept_code + "'");
        }
      }
      if (hrrs.getCounts() > 0) {
        try
        {
          DepartmentComInfo dci = new DepartmentComInfo();
          dci.removeCompanyCache();
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
      return;
    }
    catch (Exception e)
    {
      this.log.writeLog("同步部门失败," + e);
    }
  }
  
  public void delDepartment(String code, String shortname)
  {
    try
    {
      RecordSet rs = new RecordSet();
      String sql = "";
      int id = 0;
      rs.executeSql("select id from hrmdepartment where departmentcode='" + 
        code + "'");
      if (rs.next()) {
        id = Util.getIntValue(rs.getString(1), 0);
      }
      sql = 
      

        "select id from hrmresource where status in (0,1,2,3) and EXISTS (select 1 from hrmdepartment b where hrmresource.departmentid=b.id and b.id = " + id + ") union select id from hrmdepartment where (canceled = '0' or canceled is null) and id in (select id from hrmdepartment where supdepid =" + id + ")";
      rs.executeSql(sql);
      int rows = 0;
      while (rs.next()) {
        rows += Util.getIntValue(rs.getString(1), 0);
      }
      if (rows > 0)
      {
        this.log.writeLog("封存部门失败，该部门下有正常的人员，部门名称：" + shortname);
      }
      else
      {
        sql = 
          "update hrmdepartment set canceled='1' where departmentcode='" + code + "'";
        rs.executeSql(sql);
        this.log.writeLog("封存部门成功!" + sql);
      }
    }
    catch (Exception e)
    {
      this.log.writeLog("封存部门失败," + e);
    }
  }
  
  public String getSynDeptId(String hrpkcode)
  {
    RecordSet rs = new RecordSet();
    String Syndeptid = "";
    rs.executeSql("select id from hrmdepartment where departmentcode='" + 
      hrpkcode + "'");
    if (rs.next()) {
      Syndeptid = rs.getString("id");
    }
    return Syndeptid;
  }
  
  public void synHrmResource()
  {
    RecordSet rs1 = new RecordSet();
    RecordSet rs3 = new RecordSet();
    RecordSetDataSource hrrs = new RecordSetDataSource("hr");
    char separator = Util.getSeparator();
    Calendar todaycal = Calendar.getInstance();
    String today = Util.add0(todaycal.get(1), 4) + "-" + 
      Util.add0(todaycal.get(2) + 1, 2) + "-" + 
      Util.add0(todaycal.get(5), 2);
    String userpara = "1" + separator + today;
    String sql = "select t2.dept_code , t1.user_code, t1.id as idcard , t1.user_name, t1.mobile , t1.Email,  t1.isopen ,t1.ISOVER, t1.safelevel, ( select user_code from NC63.ORG_USER t3 where t3.PK_USER= t1.pk_father) as manager,t1.PK_POST  from NC63.ORG_USER t1, NC63.ORG_DEPT_view1 t2   where t1.PK_DEPT=t2.PK_DEPT";
    hrrs.executeSql(sql);
    while (hrrs.next())
    {
      String pk_deptdoc = Util.null2String(hrrs.getString("dept_code"));
      String rybm = Util.null2String(hrrs.getString("user_code"));
      String idcard = Util.null2String(hrrs.getString("idcard"));
      String psnname = Util.null2String(hrrs.getString("user_name"));
      String mobile = Util.null2String(hrrs.getString("mobile"));
      String mail = Util.null2String(hrrs.getString("mail"));
      

      int sexIdcardNumber = 0;
      if (idcard.trim().length() == 18) {
        sexIdcardNumber = Integer.parseInt(idcard.substring(16, 17));
      } else if (idcard.trim().length() == 15) {
        sexIdcardNumber = Integer.parseInt(idcard.substring(14, 15));
      }
      String sex = sexIdcardNumber % 2 == 0 ? "1" : "0";
      




      String isopen = Util.null2String(hrrs.getString("isopen"));
      String safelevel = Util.null2String(hrrs.getString("safelevel"));
      String manager = Util.null2String(hrrs.getString("manager"));
      String ISOVER = Util.null2String(hrrs.getString("ISOVER"));
      int status = 1;
      if ("Y".equals(ISOVER)) {
        status = 5;
      }
      if (!"".equals(manager))
      {
        rs3.executeSql("select id from hrmresource where workcode='" + 
          manager + "'");
        if (rs3.next()) {
          manager = rs3.getString("id");
        } else {
          manager = "";
        }
      }
      String PK_POST = Util.null2String(hrrs.getString("PK_POST"));
      String subcompanyid = "";
      String deptid = "";
      sql = "select id,subcompanyid1 from hrmdepartment where departmentcode ='" + 
        pk_deptdoc + 
        "' and  departmentcode!='' and departmentcode is not null ";
      if ("showsqllog".equals(this.showsqllog)) {
        this.log.writeLog("sql13===" + sql);
      }
      rs1.executeSql(sql);
      if (rs1.next())
      {
        deptid = rs1.getString("id");
        subcompanyid = rs1.getString("subcompanyid1");
        
        String jobtitle = "";
        sql = "select id from hrmjobtitles where jobtitlecode='" + 
          PK_POST + "'";
        rs1.executeSql(sql);
        if (rs1.next()) {
          jobtitle = Util.null2String(rs1.getString("id"));
        }
        if ("showsqllog".equals(this.showsqllog)) {
          this.log.writeLog("获取岗位id sql14===" + sql);
        }
        sql = 
          "select id from hrmresource where workcode='" + rybm + "' and workcode is not null and workcode!='' ";
        rs1.executeSql(sql);
        if (rs1.next())
        {
          int hrmid = rs1.getInt("id");
          sql = "update hrmresource set lastname='" + psnname + 
            "' ,sex='" + sex + "' ";
          if (!"".equals(safelevel.trim())) {
            sql = sql + " ,seclevel='" + safelevel + "'";
          }
          if (!"".equals(mail.trim())) {
            sql = sql + " ,email='" + mail + "'";
          }
          if (!"".equals(idcard.trim())) {
            sql = sql + " ,certificatenum='" + idcard + "'";
          }
          if (!"".equals(mobile.trim())) {
            sql = sql + " ,mobile='" + mobile + "'";
          }
          if (status == 5) {
            sql = sql + ",status=5,loginid='',password=''";
          }
          sql = sql + " ,managerid='" + manager + "'";
          sql = sql + " ,departmentid='" + deptid + 
            "',subcompanyid1='" + subcompanyid + 
            "',jobtitle='" + jobtitle + "'  where id='" + 
            hrmid + "'";
          
          rs1.executeSql(sql);
          if ("showsqllog".equals(this.showsqllog)) {
            this.log.writeLog("更新人员的sql:" + sql);
          }
        }
        else if (("Y".equals(isopen)) && (status < 5))
        {
          rs1.executeProc("HrmResourceMaxId_Get", "");
          rs1.next();
          int maxid = rs1.getInt(1);
          if (maxid > 0)
          {
            sql = 
            






















              "insert into hrmresource(id,loginid,password,workcode,lastname,departmentid,subcompanyid1,systemlanguage,sex, seclevel,email,mobile,certificatenum,status,managerid,dsporder,jobtitle) values (" + maxid + ",'" + rybm + "','202CB962AC59075B964B07152D234B70','" + rybm + "','" + psnname + "','" + deptid + "','" + subcompanyid + "' ,7,'" + sex + "','" + safelevel + "','" + mail + "','" + mobile + "','" + idcard + "','1','" + manager + "','900','" + jobtitle + "')";
            rs1.executeSql(sql);
            if ("showsqllog".equals(this.showsqllog)) {
              this.log.writeLog("新增人员的sql:" + sql);
            }
            rs1.executeProc("HrmResource_CreateInfo", maxid + 
              separator + userpara + separator + userpara);
            String para = maxid + separator + "-1" + separator + 
              deptid + separator + subcompanyid + separator + 
              "10" + separator;
            rs1.executeProc("HrmResource_Trigger_Insert", para);
            String sql_1 = "insert into HrmInfoStatus (itemid,hrmid,status) values(1," + 
              maxid + ",1)";
            rs1.executeSql(sql_1);
            String sql_2 = "insert into HrmInfoStatus (itemid,hrmid) values (2," + 
              maxid + ")";
            rs1.executeSql(sql_2);
            String sql_3 = "insert into HrmInfoStatus (itemid,hrmid) values (3," + 
              maxid + ")";
            rs1.executeSql(sql_3);
            String sql_10 = "insert into HrmInfoStatus (itemid,hrmid) values(10," + 
              maxid + ")";
            rs1.executeSql(sql_10);
          }
        }
      }
    }
    if (hrrs.getCounts() > 0) {
      try
      {
        ResourceComInfo rci = new ResourceComInfo();
        rci.removeResourceCache();
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    this.log.writeLog("同步完成");
  }
  
  public void SynPost()
  {
    try
    {
      RecordSet rs = new RecordSet();
      RecordSet rs1 = new RecordSet();
      
      RecordSetDataSource hrrs = new RecordSetDataSource("hr");
      
      String sql = "select PK_ORG,PK_POST,POSTCODE,POSTNAME,PK_JOB,JOBNAME,PK_DEPT,postcode from NC63.ORG_OMPOST ";
      hrrs.executeSql(sql);
      while (hrrs.next())
      {
        String jobtitle_name = Util.null2String(hrrs
          .getString("POSTNAME"));
        String jobtitle_code = Util.null2String(hrrs
          .getString("PK_POST"));
        
        rs.executeSql("select id from HrmJobTitles where jobtitlecode = '" + 
          jobtitle_code + 
          "' and jobtitlecode!='' and jobtitlecode is not null ");
        if (!rs.next())
        {
          String sql_insert = "insert into HrmJobTitles(jobtitlename,jobtitlemark,jobtitlecode) values('" + 
            jobtitle_name + 
            "','" + 
            jobtitle_name + 
            "','" + 
            jobtitle_code + "')";
          
          rs1.executeSql(sql_insert);
          if ("showsqllog".equals(this.showsqllog)) {
            this.log.writeLog("新增岗位成功," + sql_insert);
          }
        }
        else
        {
          String sql_update = "update HrmJobTitles set jobtitlename='" + 
            jobtitle_name + 
            "',jobtitlemark='" + 
            jobtitle_name + 
            "' where jobtitlecode='" + 
            jobtitle_code + "'";
          rs1.executeSql(sql_update);
          if ("showsqllog".equals(this.showsqllog)) {
            this.log.writeLog("更新岗位成功," + sql_update);
          }
        }
      }
      if (hrrs.getCounts() > 0) {
        try
        {
          JobTitlesComInfo jobTitlesComInfo = new JobTitlesComInfo();
          jobTitlesComInfo.removeJobTitlesCache();
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
      return;
    }
    catch (Exception e)
    {
      this.log.writeLog("同步岗位失败," + e);
    }
  }
  
  public static void main(String[] args)
  {
    String x = "1234567890";
    

    System.out.println(x.substring(1, 2));
  }
}
