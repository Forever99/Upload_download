package com.zhku.jsj144.zk.Servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadServletmore extends HttpServlet {

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {
			
			//7.定义指定类型的后缀名，然后限制不在该范围内的后缀名
//			String[] late={".txt",".doc",".jpg",".jpeg",".properties",".wmv"};
			String[] late={"txt","doc","jpg","jpeg","properties","mv"};
			
			//5.判断一个请求是否是上传文件的请求；如果不是，给用户一个友好的提示【静态方法】
			if(!ServletFileUpload.isMultipartContent(request)){
				request.setAttribute("message", "对不起，不是文件上传的表单，请检查相关属性的设置");
				request.getRequestDispatcher("/message.jsp").forward(request, response);
				return;
			}
			
			
			DiskFileItemFactory factory=new DiskFileItemFactory();
			//3.设置临时缓冲区：文件所在的文件夹以及大小；为了加快文件的上传速度
			//临时缓冲区位置：/temp目录下
			factory.setRepository(new File(this.getServletContext().getRealPath("/temp")));
			factory.setSizeThreshold(1024*1024);//设置临时缓冲区的大小为1M
			//解析器
			ServletFileUpload parse=new ServletFileUpload(factory);
			
			//6.限制上传的文件大小【超过了不让上传】
			parse.setFileSizeMax(1024*1024*2);//2M  设置单个的上传文件的大小
			parse.setSizeMax(1024*1024*20);//20M 设置总的上传的文件的大小
			
			//11.设置一个进度监听器，设置了之后，解析器就会自动去调用监听器的方法
			parse.setProgressListener(new ProgressListener(){

				//pBytesRead, 当前读到的数据是多少
				//pContentLength,当前解析到的fileItem的长度是多少
				//pItems 当前解析到第几个item了
				private long megaBytes=-1;
				@Override
				public void update(long pBytesRead, long pContentLength,
						int pItems) {
				//第一次 :   400000　－－－－－－－   0　　－－－0　　　　    megaBytes　　    0
				//第二次 :　    800000　－－－－－－－　0　　－－－0　　　　　megaBytes　　　0
				//第三次 :　 1100000　－－－－－－－　1　　－－－1　　　　　megaBytes　　　1　
				//第四次 :　 1900000　－－－－－－－　1　　－－－1　　　　　megaBytes　　　1　
				//第五次 :　 2200000　－－－－－－－　2　　－－－2　　　　　megaBytes　　　2　
					
					long mBytes = pBytesRead / 1000000;//  0 0 1 1 2
					if (megaBytes == mBytes) {      //相等？  否  是  否   是  否  
						//(1)-1==0  (2)0==0  (3)0==1 (4)1==1 (5)1==2
						return;
					}
					megaBytes = mBytes;
					System.out.println("We are currently reading item "
							+ pItems);//正在读
					if (pContentLength == -1) {
						System.out.println("So far, " + pBytesRead
								+ " bytes have been read.");
					} else {
						System.out.println("So far, " + pBytesRead + " of "
								+ pContentLength + " bytes have been read.");//已经读完
					}
				}
				
			});
			
			//2.解决文件名的乱码问题
			parse.setHeaderEncoding("utf-8");
			List<FileItem> list= parse.parseRequest(request);//获得解析器
			
			
			for (FileItem fileItem : list) {
				//普通字段
				if(fileItem.isFormField()){
					String fieldName = fileItem.getFieldName();
//					//String value = fileItem.getString();
					//1.解决普通字段的中文乱码问题
					String value = fileItem.getString("utf-8");
					System.out.println("fieldName:"+fieldName);
					System.out.println("value:"+value);
				}
				//文件
				else{
					String name = fileItem.getName();//文件名
					
					//7.对文件的后缀名进行获取并且判断，是否符合要求，从而限制文件的上传类型
					//获取文件名的"."的位置
//					int lastindex=name.lastIndexOf('.');
					int lastindex=name.lastIndexOf(".");
					String lateName=name.substring(lastindex+1);//后缀名
					//将允许的后缀名数组转为字符串
					String lateStr=Arrays.toString(late);
					
					if(!lateStr.contains(lateName)){
						//当前后缀名不是允许的后缀名，进行信息提醒
						request.setAttribute("message", "该文件上传类型不符合要求，请长传其它类型的文件");
						request.getRequestDispatcher("/message.jsp").forward(request, response);
						System.out.println("文件类型错误。。。。失败。。。。。。。。。。。。。。。。。。。。");
//						return;
					}
					
					//8.对文件名进行处理  因为有些浏览器中文件名：C:\Users\Administrator\Desktop\aaa.txt或者aa.txt
					//所以需要对前面这种情况（C:\Users\Administrator\Desktop\aaa.txt）进行处理，
					//进行文件名的提取，让它不包括路径
					int wei=name.lastIndexOf('\\');
					if(wei!=-1){
						name=name.substring(wei+1);//转为最终的文件名  aa.txt
					}
					InputStream in = fileItem.getInputStream();
					
					//4.上传的时候，通常将文件保存在web应用相对应的文件夹下
					//原因：外界无法访问到WEB-INF文件夹下的内容【防恶意攻击，将上传的文件保护起来】
					String path=this.getServletContext().getRealPath("/WEB-INF/upload");//所有上传文件都在upload目录下
					
					//9.生成随机文件夹
					String savePath=generateRandomPath(path,name);
					
					//10.保证上传文件的名字唯一
					String uuidName=generateUUid(name);
					
					//路径：E:\apache-tomcat-7.0.73\webapps\Upload_download\WEB-INF\info.txt
//					OutputStream out=new FileOutputStream(file.getAbsoluteFile()+"/"+name);
					OutputStream out=new FileOutputStream(new File(savePath,uuidName));
//					
//					OutputStream out=new FileOutputStream("d:\\"+name);
					byte[] buf=new byte[1024];
					int len=0;
					while((len=in.read(buf))!=-1){
						out.write(buf, 0, len);
					}
					//关闭流
					in.close();
					out.close();
					//3.将上传时产生的临时文件删除掉
					fileItem.delete();
				}
			}
			request.setAttribute("message", "文件上传成功");
			request.getRequestDispatcher("/message.jsp").forward(request, response);
			
			
			
		} catch(FileUploadBase.FileSizeLimitExceededException e){
			request.setAttribute("message", "对不起，单个文件的大小不能超过2M");
			request.getRequestDispatcher("/message.jsp").forward(request, response);
			return;
			
		}catch(FileUploadBase.SizeLimitExceededException e){
			request.setAttribute("message", "对不起，总的文件的大小不能超过20M");
			request.getRequestDispatcher("/message.jsp").forward(request, response);
			return;
			
		}
		//catch (FileUploadException e) {
		catch (Exception e) {
			e.printStackTrace();
//			request.setAttribute("message", "文件上传失败");
//			request.getRequestDispatcher("/message.jsp").forward(request, response);
		}
		
	}

	//10.保证上传文件的名字唯一
	private String generateUUid(String name) {
		String uuidName=UUID.randomUUID().toString()+"_"+name;
		return uuidName;
	}

	//9.生成随机文件夹
	private String generateRandomPath(String path, String name) {

		//9.通过那到文件名的hashCode和二进制：0000 0000 0000 1111进行位运算，生成随机文件夹
//		int first=name.hashCode()&(0000000000001111);
//		int second=name.hashCode()&(0000000011110000);
		//文件名哈希值
		int hashCode=name.hashCode();
		int first=hashCode&(0xf);
		int second=(hashCode>>4)&(0xf);
		String savePath=path+"/"+first+"/"+second;//保存目录
		File f=new File(savePath);
		if(!f.exists()){
			f.mkdirs();//创建多级目录
		}
		return savePath;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);

	}

}
