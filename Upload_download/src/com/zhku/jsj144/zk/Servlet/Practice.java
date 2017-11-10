package com.zhku.jsj144.zk.Servlet;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Practice extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		//1.路径1  getContextPath() 得到工程名
		String contextPath = request.getContextPath();
		//--路径：/Upload_download
		System.out.println("contextPath:"+contextPath);
		
		//2.路径2  request.getSession().getServletContext().getContextPath()
		ServletContext servletContext2 = request.getSession().getServletContext();
		//--路径：/Upload_download 得到工程名
		System.out.println("getContextPath2:"+servletContext2.getContextPath());
		
		//3.路径3  getServletContext().getContextPath()
		ServletContext servletContext = this.getServletContext();
		//--路径：/Upload_download 得到工程名
		System.out.println("getContextPath:"+servletContext.getContextPath());
		
		//4.路径4   servletContext.getRealPath("/WEB-INF/info.txt")
		String realPath = servletContext.getRealPath("/WEB-INF/info.txt");
		//--路径：E:\apache-tomcat-7.0.73\webapps\Upload_download\WEB-INF\info.txt
		System.out.println("realPath:"+realPath);
		
		//5.路径5   getServletPath()
		//返回当前页面所在目录下全名称
		String servletPath = request.getServletPath();
		//路径：/upload
		System.out.println("servletPath:"+servletPath);
		
		//6、返回包含工程名的当前页面全路径
		String requestURI = request.getRequestURI();
		//路径：/Upload_download/upload
		System.out.println("requestURI:"+requestURI);
		
		//7、返回IE地址栏地址
		StringBuffer requestURL = request.getRequestURL();
		//路径：http://localhost:8080/Upload_download/upload
		System.out.println("requestURL:"+requestURL);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);

	}

}
