package com.hs;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 回盛-封装K3标准接口Help类
 * @author Administrator
 *
 */
public class InvokeHelper {

        // K3 Cloud WebSite URL Example "http://58.49.53.230:5000/K3Cloud/"
        public static String POST_K3CloudURL = "http://58.49.53.230:5000/K3Cloud/";

        // Cookie 值
        private static String CookieVal = null;

        private static Map<String,String> map = new HashMap<String,String>();
        static {
        		//保存
                map.put("Save","Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.Save.common.kdsvc");
                //查看
                map.put("View","Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.View.common.kdsvc");
                //提交
                map.put("Submit","Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.Submit.common.kdsvc");
                //审核
                map.put("Audit","Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.Audit.common.kdsvc");
                //反审核
                map.put("UnAudit","Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.UnAudit.common.kdsvc");
                //状态转换
                map.put("StatusConvert","Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.StatusConvert.common.kdsvc");
                //列表查询
                map.put("ViewList","Kingdee.BOS.WebApi.ServicesStub.DynamicFormService.ExecuteBillQuery.common.kdsvc");
                
                
        }

        // HttpURLConnection
        private static HttpURLConnection initUrlConn(String url, JSONArray paras)
                        throws Exception {
                URL postUrl = new URL(POST_K3CloudURL.concat(url));
                HttpURLConnection connection = (HttpURLConnection) postUrl
                                .openConnection();
                if (CookieVal != null) {
                        connection.setRequestProperty("Cookie", CookieVal);
                }
                if (!connection.getDoOutput()) {
                        connection.setDoOutput(true);
                }
                connection.setRequestMethod("POST");
                connection.setUseCaches(false);
                connection.setInstanceFollowRedirects(true);
                connection.setRequestProperty("Content-Type", "application/json");
                DataOutputStream out = new DataOutputStream(
                                connection.getOutputStream());

                UUID uuid = UUID.randomUUID();
                int hashCode = uuid.toString().hashCode();

                JSONObject jObj = new JSONObject();

                jObj.put("format", 1);
                jObj.put("useragent", "ApiClient");
                jObj.put("rid", hashCode);
                jObj.put("parameters", chinaToUnicode(paras.toString()));
                jObj.put("timestamp", new Date().toString());
                jObj.put("v", "1.0");

               // out.write(jObj.toString());
                out.write(jObj.toString().getBytes("UTF-8"));
                out.flush();
                out.close();

                return connection;
        }

        // Login
        public static boolean Login(String dbId, String user, String pwd, int lang)
                        throws Exception {

                boolean bResult = false;
                String sUrl = "Kingdee.BOS.WebApi.ServicesStub.AuthService.ValidateUser.common.kdsvc";

                JSONArray jParas = new JSONArray();
                jParas.put(dbId);// 帐套Id
                jParas.put(user);// 用户名
                jParas.put(pwd);// 密码
                jParas.put(lang);// 语言

                HttpURLConnection connection = initUrlConn(sUrl, jParas);
                // 获取Cookie
                String key = null;
                for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
                        if (key.equalsIgnoreCase("Set-Cookie")) {
                                String tempCookieVal = connection.getHeaderField(i);
                                if (tempCookieVal.startsWith("kdservice-sessionid")) {
                                        CookieVal = tempCookieVal;
                                        break;
                                }
                        }
                }

                BufferedReader reader = new BufferedReader(new InputStreamReader(
                                connection.getInputStream()));
                String line;
                System.out.println(" ============================= ");
                System.out.println(" Contents of post request ");
                System.out.println(" ============================= ");
                while ((line = reader.readLine()) != null) {
                        String sResult = new String(line.getBytes(), "utf-8");
                        System.out.println(sResult);
                        bResult = line.contains("\"LoginResultType\":1");
                }
                System.out.println(" ============================= ");
                System.out.println(" Contents of post request ends ");
                System.out.println(" ============================= ");
                reader.close();

                connection.disconnect();

                return bResult;
        }

        // Save
        public static String Save(String formId, String content) throws Exception {
        	return Invoke("Save", formId, content);
        }

        // View
        public static String View(String formId, String content) throws Exception {
               return  Invoke("View", formId, content);
        }

        // Submit
        public static String Submit(String formId, String content) throws Exception {
        	return Invoke("Submit", formId, content);
        }

        // Audit
        public static String Audit(String formId, String content) throws Exception {
        	return Invoke("Audit", formId, content);
        }

        // UnAudit
        public static String UnAudit(String formId, String content) throws Exception {
        	return Invoke("UnAudit", formId, content);
        }

        // StatusConvert
        public static String StatusConvert(String formId, String content)
                        throws Exception {
        	return Invoke("StatusConvert", formId, content);
        }

        // ViewList
        public static String ViewList(String deal,String content)
                        throws Exception {
        	String sResult =null;
        	String sUrl = map.get(deal).toString();
            JSONArray jParas = new JSONArray();
            jParas.put(content);

            HttpURLConnection connectionInvoke = initUrlConn(sUrl, jParas);
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                            connectionInvoke.getInputStream(), "UTF-8"));

            String line;
            System.out.println(" ============================= ");
            System.out.println(" Contents of post request ");
            System.out.println(" ============================= ");
            while ((line = reader.readLine()) != null) {
                    sResult = new String(line.getBytes());
                    System.out.println(sResult);
            }
            System.out.println(" ============================= ");
            System.out.println(" Contents of post request ends ");
            System.out.println(" ============================= ");
            reader.close();

            connectionInvoke.disconnect();
            return sResult;
        }
        
        private static String Invoke(String deal, String formId, String content)
                        throws Exception {
        		String sResult =null;
                String sUrl = map.get(deal).toString();
                JSONArray jParas = new JSONArray();
                jParas.put(formId);
                jParas.put(content);

                HttpURLConnection connectionInvoke = initUrlConn(sUrl, jParas);

                BufferedReader reader = new BufferedReader(new InputStreamReader(
                                connectionInvoke.getInputStream(), "UTF-8"));

                String line;
                System.out.println(" ============================= ");
                System.out.println(" Contents of post request ");
                System.out.println(" ============================= ");
                while ((line = reader.readLine()) != null) {
                        sResult = new String(line.getBytes());
                        System.out.println(sResult);
                }
                System.out.println(" ============================= ");
                System.out.println(" Contents of post request ends ");
                System.out.println(" ============================= ");
                reader.close();

                connectionInvoke.disconnect();
                return sResult;
        }

        /**
         * 把中文转成Unicode码
         * 
         * @param str
         * @return
         */
        public static String chinaToUnicode(String str) {
                String result = "";
                for (int i = 0; i < str.length(); i++) {
                        int chr1 = (char) str.charAt(i);
                        if (chr1 >= 19968 && chr1 <= 171941) {// 汉字范围 \u4e00-\u9fa5 (中文)
                                result += "\\u" + Integer.toHexString(chr1);
                        } else {
                                result += str.charAt(i);
                        }
                }
                return result;
        }
        
        public static void main(String[] args) {
        	try {
        		Login("5ac1baad215cd3","Administrator","hs159357",2052);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
        
}
