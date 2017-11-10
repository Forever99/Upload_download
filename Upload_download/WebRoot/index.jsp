<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  </head>
  
  <body>
<!--   <form action="${pageContext.request.contextPath }/uploadMore" method="post" enctype="multipart/form-data"> -->
  <form action="${pageContext.request.contextPath}/upload" method="post" enctype="multipart/form-data" >
<!-- <form action="/Upload_download/upload" method="post" enctype="multipart/form-data" > -->
上传人：<input type="text" name="name"><br/>
上传文件：<input type="file" name="file"><br/>
<input type="submit" value="上传">
</form>    


下载文件<a href="/Upload_download/download?file=aa.txt">aa.txt</a>
  </body>
</html>
