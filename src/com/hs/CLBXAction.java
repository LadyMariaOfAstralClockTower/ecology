package com.hs;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.Cell;
import weaver.soa.workflow.request.DetailTable;
import weaver.soa.workflow.request.Property;
import weaver.soa.workflow.request.RequestInfo;

public class CLBXAction extends BaseBean implements Action {

	@Override
	public String execute(RequestInfo requestInfo) {
		// TODO Auto-generated method stub
		
		writeLog("[CLBXAction.execute()]**********************  差旅费报销申请开始  **********************");
		
		//InvokeHelper.POST_K3CloudURL = "http://wai.tengchu.com:81/k3cloud/";
		String dbId = "5ac1baad215cd3";//金蝶测试环境账套  5ac1baad215cd3
		//String dbId = "58c794045e98d0";//金蝶正式环境的账套  58c794045e98d0
		String uid = "Administrator";
		String pwd = "hs159357";
		int lang = 2052;
		
		
		RecordSet rs = new RecordSet();
		RecordSet rs1 = new RecordSet();
		RecordSet rs2 = new RecordSet();
		RecordSet rs3 = new RecordSet();

		String sqr = "";// 申请人
		String lastname = "";// 申请人名称
		String sqsj = "";// 申请时间
		String lcbh="";//流程编号
		int pjzs1 =0;//票据张数1
		int pjzs2 =0;//票据张数2
		int pjzs3 =0;//票据张数3
		int pjzs4 =0;//票据张数4
		int pjzs5 =0;//票据张数5
		int pjzs = 0;//票据张数合计

		String requestname = "";// 请求标题
		String FDate = "";// 凭证日期(归档日期)
		String FYEAR = ""; // 会计年度(年份)
		String FPERIOD = ""; // 会计期间(月份)

		String yskm = "";// 预算科目
		String FEXPLANATION = "";// 摘要
		String FAccountName = "";// 预算科目名称
		String FAccountId = "";// 预算科目ID
		String codename = "";// OA预算科目编码
		String oayskm = "";// OA预算科目名称
		String cbzxname = "";// OA成本中心名称

		double je = 0d;// 借方金额
		double skje = 0d;// 贷方金额
		double hjje = 0d;// 贷方金额合计

		double debitTotal = 0d;// 借方总额
		double creditTotal = 0d;// 贷方总额
		double FAMOUNTFOR = 0d;// 原币金额
		double FAMOUNT = 0d;// 本位币金额

		String cdzt = "";// 承担主体部门
		String FDetailID = "";// 部门ID

		String pptgxm ="";//品牌推广项目
		String khtgxm="";//客户推广项目
		String khkfxm="";//客户开发项目
		String zzyfxm="";//自主研发项目
		String xyfl="";//行业分类
		
		int FDC = 0;
		int xh = 0;// 序号

		// *********************************************************************************************************************
		writeLog("[CLBXAction.execute()]**********************  处理主表数据开始  **********************");
		
		/**
		 * 凭证日期(获取归档日期)2017-09-01
		 */
		
		String requestid = requestInfo.getRequestid();
		writeLog("requestid：" + requestid);
		
		String sql = "select lastoperatedate from workflow_requestbase where requestId='"
				+ requestid + "'";
		rs1.executeSql("select lastoperatedate from workflow_requestbase where requestId='"
				+ requestid + "'");
		writeLog("查看最后操作日期的SQL语句：" + sql);
		
		if(rs1.next()){
			writeLog("111111");
			FDate=rs1.getString("lastoperatedate"); 
			writeLog("最后操作日期：" + FDate);//2017-09-21
		
			//会计年度 
			FYEAR=FDate.substring(0, 4);
			writeLog("会计年度：" +FYEAR);
		  
			//会计期间 
			String[] str = FDate.split("-"); FPERIOD = str[1]; 
			writeLog("会计期间：" + FPERIOD); 
		}
		
		//获取流程编号
		rs3.executeSql("select lcbh from formtable_main_33 where requestId='"+requestid+"'");
		if(rs3.next()){
			lcbh=rs3.getString("lcbh");
			writeLog("流程编号：" + lcbh);
		}

		// 获取表单主字段信息
		Property[] properties = requestInfo.getMainTableInfo().getProperty();
		for (int i = 0; i < properties.length; i++) {
			String name = properties[i].getName();// 主字段名称
			String value = Util.null2String(properties[i].getValue());// 主字段对应的值

			/**
			 * 申请人
			 */
			if (name.equals("sqr")) {
				sqr = value.trim();
				writeLog("申请人:" + sqr);
				continue;
			}
			
			/**
			 * 票据张数
			 */
			if (name.equals("pjzs1")) {
				String a=value.trim();
				if(!"".equals(a)){
					pjzs1 = Integer.parseInt(a);
				}
				writeLog("票据张数1:"+pjzs1);
				continue;
			}
			
			if (name.equals("pjzs2")) {
				String b=value.trim();
				if(!"".equals(b)){
					pjzs2 = Integer.parseInt(b);
				}
				writeLog("票据张数2:"+pjzs2);
				continue;
			}
			
			if (name.equals("pjzs3")) {
				String c=value.trim();
				if(!"".equals(c)){
					pjzs3 = Integer.parseInt(c);
				}
				writeLog("票据张数3:"+pjzs3);
				continue;
			}
			
			if (name.equals("pjzs4")) {
				String d=value.trim();
				if(!"".equals(d)){
					pjzs4 = Integer.parseInt(d);
				}
				writeLog("票据张数4:"+pjzs4);
				continue;
			}
			
			if (name.equals("pjzs5")) {
				String e=value.trim();
				if(!"".equals(e)){
					pjzs5 = Integer.parseInt(e);
				}
				writeLog("票据张数5:"+pjzs5);
				continue;
			}

			/**
			 * 申请时间(创建时间)
			 */
			if (name.equals("sqsj")) {
				sqsj = value.trim() + " 00:00:00.000";
				writeLog("申请时间：" + sqsj);
				continue;
			}
			
			/**
			 * 合计金额
			 */
			if (name.equals("hjje")) {
				hjje= Double.parseDouble(value.trim());
				writeLog("对象中存储的贷方金额合计为："+hjje);
			}

		}
		
		// 票据张数合计+3
		pjzs +=  (pjzs1+pjzs2+pjzs3+pjzs4+pjzs5)+4;
		writeLog("票据张数总计:"+pjzs);
		
		/**
		 * 申请人名称
		 */
		if (sqr != null && !"".equals(sqr)) {
			rs.executeSql("select lastname from HrmResource where id=" + sqr);

			if (rs.next()) {
				lastname = rs.getString("lastname");
				writeLog("申请人名称：" + lastname);
			}
		}
		
		// *********************************************************************************************************************
		writeLog("**********************调用K3Cloud接口开始");
		
		
		// 明细表1
		writeLog("进入处理明细数据");
		DetailTable detailTable0 = requestInfo.getDetailTableInfo().getDetailTable(0);
		int length1 = detailTable0.getRowCount();
		writeLog("length1:"+length1);
		for (int i = 0; i < length1; i++) {
			for (int a = 0; a < detailTable0.getRow()[i].getCell().length; a++) {
				Cell cell = detailTable0.getRow()[i].getCell()[a];
				String name = cell.getName();// 明细字段名称
				String value = cell.getValue();// 明细字段的值

				
			}
		}
		
		try {
			if (InvokeHelper.Login(dbId, uid, pwd, lang)) {	
				writeLog("进入接口");
				// 凭证保存测试
				// 业务对象Id
				String sFormId = "GL_VOUCHER";
				//需要保存的数据
				// 如下字段可能需要根据自己实际值做修改
				String sContent = "";
				StringBuffer sBuffer = new StringBuffer();
				sBuffer = sBuffer.append("{");
				sBuffer = sBuffer.append("    \"Creator\": \""+lastname+"\",");// 申请人\""+lastname+"\",
				sBuffer = sBuffer.append("    \"Model\": {");
				sBuffer = sBuffer.append("        \"FAccountBookID\": {");
				sBuffer = sBuffer.append("            \"FNumber\": \"001\"");//默认值
				sBuffer = sBuffer.append("        },");
				sBuffer = sBuffer.append("        \"FDate\": \""+FDate+"\",");//归档日期
				sBuffer = sBuffer.append("        \"FVOUCHERGROUPID\": {");
				sBuffer = sBuffer.append("            \"FNumber\": \"PRE001\"");//默认值
				sBuffer = sBuffer.append("        },");
				sBuffer = sBuffer.append("        \"FATTACHMENTS\": \""+pjzs+"\",");// 附件数（票据张数）
				sBuffer = sBuffer.append("        \"FBASECURRENCYID\": {");
				sBuffer = sBuffer.append("            \"FNUMBER\": \"PRE001\"");//默认值
				sBuffer = sBuffer.append("        },");
				sBuffer = sBuffer.append("        \"FDEBITTOTAL\": \""+debitTotal+"\",");// 借方总金额
				sBuffer = sBuffer.append("        \"FYEAR\": \""+FYEAR+"\",");// 会计年度(年度)
				sBuffer = sBuffer.append("        \"FCREDITTOTAL\":  \""+hjje+"\",");// 贷方总金额
				sBuffer = sBuffer.append("        \"FDocumentStatus\": \"A\",");//默认值
				sBuffer = sBuffer.append("        \"FCreateDate\": \""+sqsj+"\",");//创建日期
				sBuffer = sBuffer.append("        \"FACCBOOKORGID\": {");
				sBuffer = sBuffer.append("            \"FNumber\": \"100\"");//默认值
				sBuffer = sBuffer.append("        },");
				sBuffer = sBuffer.append("        \"FIsSplit\": 0,");//默认值
				sBuffer = sBuffer.append("        \"FPERIOD\": \""+FPERIOD+"\",");//当月的月份
				sBuffer = sBuffer.append("        \"FCreatorId\": {");
				sBuffer = sBuffer.append("            \"FUserAccount\": \""+lastname+"\"");//申请人 
				sBuffer = sBuffer.append("        },");
				sBuffer = sBuffer.append("        \"FEntity\": [");
				
//**************************************处理拼接明细表数据开始**************************************************************
				// 明细表1
				writeLog("进入处理明细数据");			
				// 明细表3
				DetailTable detailTable2 = requestInfo.getDetailTableInfo().getDetailTable(2);
				int length2 = detailTable2.getRowCount();
				writeLog("length2:"+length2);
				
				int length = length1 > length2 ? length1 : length2;// 获取最大的明细行
				writeLog("length:"+length);
				for (int i = 0; i < length; i++) {
					/**
					 * 序号
					 */
					xh +=i+1;
					writeLog("序号i:"+i);
					sBuffer = sBuffer.append("            {");
					// 循环第一个明细表
					if(length1>i){
						
						for (int a = 0; a < detailTable0.getRow()[i].getCell().length; a++) {
							Cell cell = detailTable0.getRow()[i].getCell()[a];
							String name = cell.getName();// 明细字段名称
							String value = cell.getValue();// 明细字段的值
							
							if (name.equals("yskm")) {// 预算科目
								yskm = value.trim();
								writeLog("预算科目：" + yskm);
								
								/**
								 * 摘要
								 */
								// 摘要：请求标题&预算科目&":"&流程编号(eg:李毅报销部门经费：QT20170206-002)
								// 如果是第一条明细，那么将这一条明细的摘要作为凭证表头的摘要
								// 摘要格式请求标题&预算科目&":"&流程编号(eg:李毅报销部门经费：QT20170206-002)
								// 获取请求标题   获取requestid
								rs2.executeSql("select requestname from workflow_requestbase where requestId='"+requestid+"'");
								if(rs2.next()){
									requestname=rs2.getString("requestname");
									writeLog("请求标题：" + requestname);
								}
								
								//获取OA的预算科目
								RecordSet oakm = new RecordSet();
								String rsoakm = "select codename from FnaBudgetfeeType where id ='"+ yskm + "'";
								oakm.executeSql(rsoakm);
								writeLog("查看OA的预算科目的SQL语句："+rsoakm);
								if (oakm.next()) {
									codename = oakm.getString("codename");
									writeLog("OA科目编码：" + codename);
								}
								
								continue;
							}
							
							if (name.equals("je")) {//金额(借方金额)
								// 借方金额
								je= Double.parseDouble(value.trim());
								writeLog("借方金额为："+je);
								
								// 计算借方总额
								debitTotal += je;
								writeLog("对象中存储的借方总金额为："+debitTotal);
								
								/**
								 * 借贷方向(1是借   -1是贷)
								 */
								FDC = je > 0 ? 1 : -1;// 动态借贷方向
								continue;
							}
							
							/**
							 * 承担主体部门(部门#编码  部门#名称)
							 */
							if (name.equals("cdzt")) {// 承担主体部门
								cdzt = value.trim();
								writeLog("承担主体部门:"+cdzt);
								
								//获取OA的成本中心的名称
								RecordSet oacbzx = new RecordSet();
								String rsoacbzx = "select * from FnaCostCenter where id='"+ cdzt + "'";
								oacbzx.executeSql(rsoacbzx);
								writeLog("查看OA的成本中心的SQL语句："+rsoacbzx);
								if (oacbzx.next()) {
									cbzxname = oacbzx.getString("name");
									writeLog("成本中心名称：" + cbzxname);
									
									// 1.通过ecology科目名称查到金蝶的科目名称
									// 2.通过ecology科目名称查到金蝶的科目编码
									RecordSet rskm = new RecordSet();
									String sqlkm = "select jdkmmc,jdkmbm,jdbmbm,jdbmmc,oayskm,pptgxm,khtgxm,khkfxm,zzyfxm,xyfl from uf_oajdysgx where oayskmbm ='"+ codename + "' and oacbzxmc ='"+ cbzxname + "'";
									rskm.executeSql(sqlkm);
									writeLog("查看金蝶的预算科目的SQL语句："+sqlkm);	
									
									if (rskm.next()) {
										oayskm = rskm.getString("oayskm");//OA科目名称
										FAccountName = rskm.getString("jdkmmc");//科目名称
										FAccountId= rskm.getString("jdkmbm");//科目编码
										FDetailID = rskm .getString("jdbmbm");//部门编码
										
										pptgxm=rskm .getString("pptgxm");//品牌推广项目
										khtgxm=rskm .getString("khtgxm");//客户推广项目
										khkfxm=rskm .getString("khkfxm");//客户开发项目
										zzyfxm=rskm .getString("zzyfxm");//自主研发项目
										xyfl=rskm .getString("xyfl");//行业分类
										
										writeLog("OA科目名称：" + oayskm);
										writeLog("科目名称：" + FAccountName);
										writeLog("科目编码：" + FAccountId);
										writeLog("部门编码：" + FDetailID);
										writeLog("品牌推广项目：" + pptgxm);
										writeLog("客户推广项目：" + khtgxm);
										writeLog("客户开发项目：" + khkfxm);
										writeLog("自主研发项目：" + zzyfxm);
										writeLog("行业分类：" + xyfl);
									}
									
								}
								FEXPLANATION=requestname+":"+oayskm+lcbh;
								writeLog("摘要：" + FEXPLANATION);
								
								sBuffer = sBuffer.append("                \"FEXPLANATION\": \""+FEXPLANATION+"\",");//摘要
								sBuffer = sBuffer.append("                \"FACCOUNTID\": {");
								sBuffer = sBuffer.append("                    \"FNumber\": \""+FAccountId+"\"");//科目编码
								sBuffer = sBuffer.append("                },");
								sBuffer = sBuffer.append("                \"FDetailID\": {");
								sBuffer = sBuffer.append("                    \"FDetailID__FFLEX5\": {");
								sBuffer = sBuffer.append("                        \"FNumber\": \""+FDetailID+"\"");//承担主体部门,核算维度
								sBuffer = sBuffer.append("                    },");
								
								sBuffer = sBuffer.append("                    \"FDetailID__FF100004\": {");
								sBuffer = sBuffer.append("                        \"FNumber\": \""+pptgxm+"\"");//品牌推广项目
								sBuffer = sBuffer.append("                    },");
								
								sBuffer = sBuffer.append("                    \"FDetailID__FF100005\": {");
								sBuffer = sBuffer.append("                        \"FNumber\": \""+khtgxm+"\"");//客户推广项目
								sBuffer = sBuffer.append("                    },");
								
								sBuffer = sBuffer.append("                    \"FDetailID__FF100007\": {");
								sBuffer = sBuffer.append("                        \"FNumber\": \""+khkfxm+"\"");//客户开发项目
								sBuffer = sBuffer.append("                    },");
								
								sBuffer = sBuffer.append("                    \"FDetailID__FF100008\": {");
								sBuffer = sBuffer.append("                        \"FNumber\": \""+zzyfxm+"\"");//自主研发项目
								sBuffer = sBuffer.append("                    },");
								
								sBuffer = sBuffer.append("                    \"FDetailID__FF100013\": {");
								sBuffer = sBuffer.append("                        \"FNumber\": \""+xyfl+"\"");//行业分类
								sBuffer = sBuffer.append("                    }");
								
								sBuffer = sBuffer.append("                },");
								continue;
							}
							
						}
					}else{
						sBuffer = sBuffer.append("                \"FEXPLANATION\": \"0\",");//摘要
						sBuffer = sBuffer.append("                \"FACCOUNTID\": {");
						sBuffer = sBuffer.append("                    \"FNumber\": \"0\"");//科目编码
						sBuffer = sBuffer.append("                },");
						sBuffer = sBuffer.append("                \"FDetailID\": {");
						sBuffer = sBuffer.append("                    \"FDetailID__FFLEX5\": {");
						sBuffer = sBuffer.append("                        \"FNumber\": \"0\"");//承担主体部门,核算维度
						sBuffer = sBuffer.append("                    }");
						
						sBuffer = sBuffer.append("                },");
					}
						
					if(length2<(i+1)){
						writeLog("123123");
						sBuffer = sBuffer.append("                \"FAMOUNTFOR\": \"0\",");//原币金额
						sBuffer = sBuffer.append("                \"FAMOUNT\": \"0\","); //本位币金额
						sBuffer = sBuffer.append("                \"FCURRENCYID\": {");
						sBuffer = sBuffer.append("                    \"FNUMBER\": \"PRE001\"");//币别编码(默认值)
						sBuffer = sBuffer.append("                },");
						sBuffer = sBuffer.append("                \"FEXCHANGERATETYPE\": {");
						sBuffer = sBuffer.append("                    \"FNumber\": \"HLTX01_SYS\"");//汇率类型编码(默认值)
						sBuffer = sBuffer.append("                },");
						sBuffer = sBuffer.append("                \"FEXCHANGERATE\": \"1.00000\",");//汇率(默认值)
						sBuffer = sBuffer.append("                \"FDC\": \""+FDC+"\",");//借贷方向(借：1  贷：-1)
						sBuffer = sBuffer.append("                \"FDEBIT\": \""+je+"\",");//借方金额
						sBuffer = sBuffer.append("                \"FCREDIT\": \"0\",");//贷方金额
						sBuffer = sBuffer.append("                \"fentryseq\": \""+xh+"\"");//序号
						sBuffer = sBuffer.append("            },");
					}else{
						// 循环第三个明细表
						writeLog("第三个明细表数组："+ detailTable2.getRow());
						
							for (int a = 0; a < detailTable2.getRow()[i].getCell().length; a++) {
								//writeLog("第"+i+"次进入明细表31111");
								Cell cell = detailTable2.getRow()[i].getCell()[a];
								String name = cell.getName();// 明细字段名称
								String value = cell.getValue();// 明细字段的值
								//writeLog("进入第三个明细表第二个循环");
								writeLog("第三个明细表name:"+name);
								if (name.equals("skje")) {//合计金额（收款金额的合计）
									/**
									 * 贷方金额
									 */
									//writeLog("第"+i+"次进入明细表32222");
									skje= Double.parseDouble(value.trim());
									writeLog("贷方金额为："+skje);
									
									// 计算贷方总额
									creditTotal += skje;
									writeLog("对象中存储的贷方总金额为："+creditTotal);	
									
									/**
									 * 原币金额【(单据体)原币金额=(单据体)借方金额+(单据体)贷方金额】
									 */
									FAMOUNTFOR=je+skje ;
									writeLog("对象中存储的原币金额为："+FAMOUNTFOR);
									
									/**
									 * 本位币金额
									 */
									FAMOUNT =FAMOUNTFOR;
									writeLog("对象中存储的本位币金额为："+FAMOUNT);
									sBuffer = sBuffer.append("                \"FAMOUNTFOR\": \""+FAMOUNTFOR+"\",");//原币金额
									sBuffer = sBuffer.append("                \"FAMOUNT\": \""+FAMOUNT+"\","); //本位币金额
									sBuffer = sBuffer.append("                \"FCURRENCYID\": {");
									sBuffer = sBuffer.append("                    \"FNUMBER\": \"PRE001\"");//币别编码(默认值)
									sBuffer = sBuffer.append("                },");
									sBuffer = sBuffer.append("                \"FEXCHANGERATETYPE\": {");
									sBuffer = sBuffer.append("                    \"FNumber\": \"HLTX01_SYS\"");//汇率类型编码(默认值)
									sBuffer = sBuffer.append("                },");
									sBuffer = sBuffer.append("                \"FEXCHANGERATE\": \"1.00000\",");//汇率(默认值)
									sBuffer = sBuffer.append("                \"FDC\": \""+FDC+"\",");//借贷方向(借：1  贷：-1)
									sBuffer = sBuffer.append("                \"FDEBIT\": \""+je+"\",");//借方金额
									sBuffer = sBuffer.append("                \"FCREDIT\": \"0\",");//贷方金额
									sBuffer = sBuffer.append("                \"fentryseq\": \""+xh+"\"");//序号
									sBuffer = sBuffer.append("            },");
									
									continue;
								}
							}	
						
					}

				
				}
				


				// 贷方
				sBuffer = sBuffer.append("            {");
				sBuffer = sBuffer.append("                \"FEXPLANATION\": \""+FEXPLANATION+"\",");
				sBuffer = sBuffer.append("                \"FACCOUNTID\": {");
				sBuffer = sBuffer.append("                    \"FNumber\": \"1002.01\"");////科目编码****（这个贷方科目是固定的）
				sBuffer = sBuffer.append("                },");
				sBuffer = sBuffer.append("                \"FDetailID\": {");
				sBuffer = sBuffer.append("                    \"FDetailID__FF100003\": {");
				sBuffer = sBuffer.append("                        \"FNumber\": \"127905435110333\"");//银行账号
				sBuffer = sBuffer.append("                    }");
				sBuffer = sBuffer.append("                },");
				sBuffer = sBuffer.append("                \"FAMOUNTFOR\":  \""+FAMOUNTFOR+"\",");
				sBuffer = sBuffer.append("                \"FAMOUNT\":\""+FAMOUNT+"\",");
				sBuffer = sBuffer.append("                \"FCURRENCYID\": {");
				sBuffer = sBuffer.append("                    \"FNUMBER\": \"PRE001\"");
				sBuffer = sBuffer.append("                },");
				sBuffer = sBuffer.append("                \"FEXCHANGERATETYPE\": {");
				sBuffer = sBuffer.append("                    \"FNumber\": \"HLTX01_SYS\"");
				sBuffer = sBuffer.append("                },");
				sBuffer = sBuffer.append("                \"FEXCHANGERATE\": \"1.00000\",");
				sBuffer = sBuffer.append("                \"FDC\": \"-1\",");//贷方-1
				sBuffer = sBuffer.append("                \"FDEBIT\":  \"0\",");
				sBuffer = sBuffer.append("                \"FCREDIT\":\""+hjje+"\",");
				sBuffer = sBuffer.append("                \"fentryseq\": \""+(xh+1)+"\"");
				sBuffer = sBuffer.append("            }");
	
				sBuffer = sBuffer.append("        ]");
				sBuffer = sBuffer.append("    }");
				sBuffer = sBuffer.append("}");
			
				sContent = sBuffer.toString();
				writeLog("数据内容sContent："+sContent);
				
				try {
					InvokeHelper.Save(sFormId, sContent);
				} catch (Exception e) {
					e.printStackTrace();
					writeLog("调用保存接口报错信息：" + e.getMessage());
				}

				writeLog("hola success");
			}
		} catch (Exception e) {
			e.printStackTrace();
			writeLog("调用登录接口报错信息：" + e.getMessage());
		}
	
		return Action.SUCCESS;
	
	}

}
