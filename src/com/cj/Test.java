package com.cj;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Test {
	
		public static void main(String[] args) throws Exception {
				// xml数据
			
				String xmlPath="F://send//jzsq.xml";
				
				File file = new File(xmlPath);
				// 创建Http连接，发送数据
				String url ="http://nc.csadi.cn:9000/service/XChangeServlet?account=test&groupcode=CSAID";
				HttpURLConnection connection;
				BufferedOutputStream out = null;
				BufferedInputStream input = null;
				try {
					URL realURL = new URL(url);
					connection = (HttpURLConnection) realURL.openConnection();
					connection.setDoOutput(true);
					connection.setRequestProperty("Content-type", "text/xml");
					connection.setRequestMethod("POST");
					out = new BufferedOutputStream(connection.getOutputStream());
					input = new BufferedInputStream(new FileInputStream(file));
					int length;
					byte[] buffer = new byte[1000];
					while ((length = input.read(buffer, 0, 1000)) != -1) {
						out.write(buffer, 0, length);
					}
				}catch (Exception e) {
					System.out.println("推送到NC失败 error:"+e.toString());
					throw new IOException();
				}finally {
					if (input != null) {
						input.close();
					}
					if (out != null) {
						out.close();
					}
				}
				// 获取回执
				InputStream in = connection.getInputStream();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
				StringBuilder temp = new StringBuilder();
				String line = bufferedReader.readLine();
				while (line != null) {
					temp.append(line).append("\r\n");
					line = bufferedReader.readLine();
				}
					
					String result = new String(temp); // result即为回执内容
					String str=new String(result.getBytes("gbk"),"UTF-8");
					System.out.println(str);
					bufferedReader.close();
		}
		
}