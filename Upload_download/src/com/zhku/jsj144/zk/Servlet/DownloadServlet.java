package com.zhku.jsj144.zk.Servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
// 下载d:/aa.txt 特定文件的实现
public class DownloadServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//file=aaa.txt   
		
		String file = request.getParameter("file");
		System.out.println(file);
		
		File f=new File("d:/"+file);//要下载的文件存存放位置
		String filename =URLEncoder.encode(file, "utf-8");//给文件名编码
		//将中文的 文件名 编码 后 再 放到  http的响应头  中去 , 编码 之后   浏览器 收到 后 会自动的解码
		
		//通知浏览器以下载的方式打开文件
		response.setHeader("content-disposition", "attachement;filename="+filename);
		
		//还需要将  要下载的 文件 当作 一个 inputStream 流 读进来, 
		// 读进来 再 写到   response.getOutputStream中去就可以了. 
		InputStream in=new FileInputStream(f);//文件
		OutputStream out=response.getOutputStream();
		byte[] buf=new byte[1024];
		int len=0;
		while((len=in.read(buf))!=-1){
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
		
		System.out.println("下载文件成功");
//		response.getWriter().write("下载文件成功");
		
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);

	}

}
