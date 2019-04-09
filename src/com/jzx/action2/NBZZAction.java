package com.jzx.action2;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;

import com.jzx.EASServices.WSGLWebServiceFacade.wsvoucher.WSWSVoucher;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.TimeUtil;
import weaver.general.Util;
import weaver.hrm.resource.ResourceComInfo;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
import com.jzx.action.EASUtil;

public class NBZZAction extends BaseBean implements Action {

	public String execute(RequestInfo request) {

		String fromtable = request.getRequestManager().getBillTableName(); // 表单名称
		String requestid = request.getRequestid(); // 请求ID
		String workflowid = request.getWorkflowid();// 流程ID

		boolean flag = true;

		writeLog("******************************* NBZZAction start *******************************");
		writeLog("workflowid:" + workflowid + " requestid:" + requestid);

		EASUtil easUtil = new EASUtil();
		ResourceComInfo resourceComInfo = easUtil.getResourceComInfo();

		String sqr = ""; // 申请人
		String bookedDate = TimeUtil.getCurrentDateString(); // 记账日期,当前归档日期
		String bizDate = "";// 业务日期->日期
		int PeriodYear = 0; // 会计期间-年
		int PeriodNumber = 0; // 会计期间-编码
		String voucherType = ""; // 凭证字（凭证类型）->
		String voucherNumber = requestid; // 凭证号=流程ID
		String currencyNumber = "BB01"; // 币种编码
		double dforiginalAmount = 0; // 贷方原币金额
		double dfcreditAmount = 0; // 贷方金额
		String creator = ""; // 制单人
		String lcbh = "";// 流程编号

		RecordSet recordSet = new RecordSet();
		RecordSet rs = new RecordSet();

		String sql = "select * from " + fromtable + " where requestid="
				+ requestid;
		recordSet.execute(sql);
		if (recordSet.next()) {

			sqr = recordSet.getString("sqr");

			voucherType = "记账";

			bizDate = recordSet.getString("sqrq");
			bookedDate = bizDate;

			PeriodYear = Util
					.getIntValue(Util.TokenizerString2(bizDate, "-")[0]);
			PeriodNumber = Util
					.getIntValue(Util.TokenizerString2(bizDate, "-")[1]);

			creator = resourceComInfo.getLastname(sqr);

			lcbh = recordSet.getString("lcbh");

		}

		// 获取明细表数据
		sql = "select t2.* from " + fromtable + " t1," + fromtable
				+ "_dt1 t2 where requestId=" + requestid
				+ " and t1.id=t2.mainid";
		recordSet.execute(sql);

		int index = 0;

		while (recordSet.next()) {

			index++;

			String mxid = recordSet.getString("id");
			String pzzt = recordSet.getString("pzzt");

			if (pzzt.equals("1")) {
				continue;
			}

			List<WSWSVoucher> voucherList = new ArrayList<WSWSVoucher>();

			// 借方记录
			WSWSVoucher voucherJf = new WSWSVoucher();// 借方
			voucherJf.setCompanyNumber(recordSet.getString("gs1")); // 公司编码
			// voucherJf.setCompanyNumber("1123.01.05");

			voucherJf.setBookedDate(bookedDate); // 记账日期,流程归档日期
			voucherJf.setBizDate(bizDate); // 业务日期
			voucherJf.setPeriodYear(PeriodYear); // 会计期间-年
			voucherJf.setPeriodNumber(PeriodNumber); // 会计期间-编码
			voucherJf.setVoucherType(voucherType); // 凭证字（凭证类型）
			voucherJf.setVoucherNumber(voucherNumber); // 凭证号
			voucherJf.setEntrySeq(index); // 分录行号 借方为1 贷方为2
			voucherJf.setAccountNumber(getJFkm(recordSet.getString("zzlx"))); // 科目编码
			// voucherJf.setAccountNumber("1002.01");

			voucherJf.setCurrencyNumber(currencyNumber); // 币种编码
			voucherJf.setEntryDC(1); // 分录行方向 1 借方 -1贷方

			double jzje = Util.getDoubleValue(recordSet.getString("je"));

			dforiginalAmount = jzje;
			dfcreditAmount = jzje;

			voucherJf.setOriginalAmount(jzje); // 原币金额
			voucherJf.setDebitAmount(jzje); // 借方金额
			voucherJf.setAttaches(0); // 附件数
			voucherJf.setCreator(creator); // 制单人

			String VoucherAbstract = getDesc(recordSet.getString("zzlx")); // 摘要

			voucherJf.setVoucherAbstract(VoucherAbstract); // 描述

			String dr_zh = recordSet.getString("dr_zh1");// 调入账户

			if (!dr_zh.equals("")) {
				voucherJf.setAsstActType1("银行账户");
				voucherJf.setAsstActNumber1(dr_zh);
			}

			String dr_kh = recordSet.getString("dr_kh");// 调入客户
			String dr_gys = recordSet.getString("dr_gys");// 调入供应商

			if (!dr_kh.equals("")) {
				dr_kh = Util.TokenizerStringNew(getSelectName(10937, dr_kh),
						"_")[1];
				voucherJf.setAsstActType1("客户");
				voucherJf.setAsstActNumber1(dr_kh);
			} else if (!dr_gys.equals("")) {
				dr_gys = Util.TokenizerStringNew(getSelectName(10938, dr_gys),
						"_")[1];
				voucherJf.setAsstActType1("供应商");
				voucherJf.setAsstActNumber1(dr_gys);
			}

			voucherList.add(voucherJf);

			index++;

			// 贷方记录
			WSWSVoucher voucherDf = new WSWSVoucher();
			voucherDf.setCompanyNumber(recordSet.getString("gs1")); // 公司编码

			// voucherDf.setCompanyNumber("1002.14");

			voucherDf.setBookedDate(bookedDate); // 记账日期
			voucherDf.setBizDate(bizDate); // 业务日期
			voucherDf.setPeriodYear(PeriodYear); // 会计期间-年
			voucherDf.setPeriodNumber(PeriodNumber); // 会计期间-编码
			voucherDf.setVoucherType(voucherType); // 凭证字（凭证类型）
			voucherDf.setVoucherNumber(voucherNumber); // 凭证号
			voucherDf.setEntrySeq(index); // 分录行号 借方为1 贷方为2
			voucherDf.setAccountNumber(Util.TokenizerStringNew(getSelectName(
					10934, recordSet.getString("zcgs")), "_")[1]); // 贷方科目编码
			// voucherDf.setAccountNumber("1002.01");
			voucherDf.setCurrencyNumber(currencyNumber); // 币种编码
			voucherDf.setEntryDC(-1); // 分录行方向 1 借方 -1贷方
			voucherDf.setOriginalAmount(dforiginalAmount); // 原币金额
			voucherDf.setCreditAmount(dfcreditAmount);// 贷方金额
			voucherDf.setAttaches(0); // 附件数
			voucherDf.setCreator(creator); // 制单人
			voucherDf.setVoucherAbstract(VoucherAbstract + "(" + lcbh + ")"); // 描述

			String dc_zh = recordSet.getString("dc_zh1");// 调出账户

			voucherDf.setAsstActType1("银行账户");// 核算项目1
			voucherDf.setAsstActNumber1(dc_zh);// 核算对象编码1

			voucherList.add(voucherDf);

			WSWSVoucher[] vouchers = new WSWSVoucher[voucherList.size()]; // 1个借方，1个贷方

			for (int i = 0; i < voucherList.size(); i++) {
				vouchers[i] = voucherList.get(i);
			}

			JSONArray jsArray = JSONArray.fromObject(vouchers);

			writeLog("vouchers:" + jsArray.toString());

			easUtil.doLogin();

			boolean result = easUtil.importVoucher(vouchers, workflowid,
					requestid); // 导入凭证

			if (result) {
				writeLog("凭证导入成功 workflowid:" + workflowid + " requestid:"
						+ requestid);
				String str = "update " + fromtable
						+ "_dt1 set pzzt='1' where id=" + mxid;
				rs.execute(str);
			} else {
				writeLog("凭证导入失败 workflowid:" + workflowid + " requestid:"
						+ requestid);
				String str = "update " + fromtable
						+ "_dt1 set pzzt='0' where id=" + mxid;
				rs.execute(str);
				flag = false;
			}

		}

		writeLog("******************************* NBZZAction over *******************************");

		return flag ? "1" : "0";
	}

	public String getJFkm(String zzlx) {
		String km = "";
		if (zzlx.equals("0")) {
			km = "1002.01";
		} else if (zzlx.equals("1")) {
			km = "1122.03";
		} else if (zzlx.equals("2")) {
			km = "2202";
		} else if (zzlx.equals("3")) {
			km = "2202";
		} else if (zzlx.equals("4")) {
			km = "1122.03";
		}

		return km;
	}

	public String getDesc(String zzlx) {
		String desc = "";
		if (zzlx.equals("0")) {
			desc = "银行账户间转账";
		} else if (zzlx.equals("1")) {
			desc = "湖北杰之行支付子公司款项";
		} else if (zzlx.equals("2")) {
			desc = "支付湖北杰之行款项";
		} else if (zzlx.equals("3")) {
			desc = "湖北杰之行支付孝昌款项";
		} else if (zzlx.equals("4")) {
			desc = "孝昌支付湖北杰之行款项";
		}

		return desc;
	}

	public String getLastName(String id) {
		String name = "";
		RecordSet recordSet = new RecordSet();
		String sql = "select * from hrmresource where id=" + id;
		recordSet.execute(sql);
		if (recordSet.next()) {
			name = recordSet.getString("lastname");
		}
		return name;
	}

	public String getSelectName(int fieldid, String selectvalue) {
		if (selectvalue.equals("")) {
			return "";
		}
		RecordSet recordSet = new RecordSet();
		String sql = "SELECT selectname FROM workflow_SelectItem WHERE fieldid="
				+ fieldid + " and selectvalue=" + selectvalue;

		recordSet.execute(sql);
		String selectname = "";
		if (recordSet.next())
			selectname = recordSet.getString("selectname");

		System.out.println("getSelectName sql:" + sql + " selectname:"
				+ selectname);

		return selectname;
	}

	public static void main(String[] args) {

	}

}
