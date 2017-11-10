package com.zhku.jsj144.zk.Servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
/*
 * 分析：
 * Request对象提供了一个getInputStream方法，通过这个方法可以读取到客户端提交过来的数据。
 * 但由于用户可能会同时上传多个文件，在servlet端编程直接读取上传数据，并分别解析出相应的文件数据是一项非常麻烦的工作.
 * 
 * 使用：Commons-fileupload组件
 * 为方便用户处理文件上传数据，Apache 开源组织提供了一个用来处理表单文件上传的一个开源组件（ Commons-fileupload ），
 * 该组件性能优异，并且其API使用极其简单，可以让开发人员轻松实现web文件上传功能，因此在web开发中实现文件上传功能，
 * 通常使用Commons-fileupload组件实现。
 * 
 * jar包：
 * 使用Commons-fileupload组件实现文件上传，需要导入该组件相应的支撑jar包：Commons-fileupload和commons-io
 */
public class UploadServlet extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String name=request.getParameter("name");
		String file=request.getParameter("file");
		System.out.println("name:"+name);//null 获取参数为空，说明此种获取上传文件的相关参数，获取失败
		System.out.println("file:"+file);//null
		/*
		 * 浏览器在上传文件时，将把文件数据附带在http请求消息体中，并使用MIME协议对上传的文件进行描述，
		 * 以方便接收方对上传数据进行解析和处理。
		 */
		try {
			//1.创建工厂
			DiskFileItemFactory factory=new DiskFileItemFactory();
			//2.创建ServletFileUpload
			ServletFileUpload parse=new ServletFileUpload(factory);
			//3.获得解析器
			List<FileItem> list = parse.parseRequest(request);
			
			for (FileItem fileItem : list) {
				
				//普通字段
				if(fileItem.isFormField()){//是普通表单字段
					String fieldName = fileItem.getFieldName();//字段名
//					String name2 = fileItem.getName();
					String value = fileItem.getString();//字段值
					System.out.println("fieldName:"+fieldName);
//					System.out.println("name2:"+name2);
					System.out.println("value:"+value);
				}
				//文件
				else{
					String name3 = fileItem.getName();//文件名
					System.out.println("name3:"+name3);
					InputStream in = fileItem.getInputStream();//获取读取流
					
					OutputStream out=new FileOutputStream("d:\\"+name3);
//					OutputStream out=new FileOutputStream(new File("d:\\"+name3));
					
					byte[] buf=new byte[1024];//缓冲区
					int len=0;//读取字节长度
						
					//读取的数据没有放到缓冲区中
//					while((len=in.read())>0){
//					while((len=in.read())!=-1){
					
					while((len=in.read(buf))>0){
						out.write(buf, 0, len);//将读取到的内容写到输出流中，保存在文件中
					}
					in.close();
					out.close();
				}
				
			}
			request.setAttribute("message", "上传文件成功");
			request.getRequestDispatcher("/message.jsp").forward(request, response);
			
		} catch (FileUploadException e) {
			request.setAttribute("message", "上传文件失败");
			request.getRequestDispatcher("/message.jsp").forward(request, response);
			e.printStackTrace();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);

	}

}
