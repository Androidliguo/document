<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>已发送私人信件列表</title>
</head>
<body>
<jsp:include page="inc/inc.jsp"></jsp:include> 
<table class="ct" width="800" align="center" border="1" cellpadding="0" cellspacing="0">
<tr>
 <td colspan="4">
 <form action="message_listSend.action" method="get">
 <input type="text" name="con" value="${con}"/>
 <input type="submit" value="筛选">
 </form>
 </td>
 </tr>
  <tr>
  <td>信件标识</td>
  <td>信件标题</td>
  <td>创建时间</td>
  <td>操作</td>
  </tr>
  <s:if test="#pages.totalRecord<=0">
  <tr>
  <td colspan="4">
  当前还没有发送任何私人信件
  </td>
  </tr>
  </s:if>
  <s:else>
   <s:iterator value="#pages.datas">
     <tr>
     <td>${id }</td>
      <td>
      <a href="message_show.action?id=${id}&isRead=1">${title}</a>
      </td>
      <!--这里的话，可以指定日期的格式-->
      <td>
      <s:date name="createDate" format="yyyy-MM-dd"/>
      </td>
      <!--在下面这里，如果我们想要查看发送的附件的信息的话，需要将isRead传过去。否则的话，是不行的。
                     这里的话， 一定要说明isRead等于1.否则的话，默认情况下，那么就会给isRead注入的值是0
       但是这样的话，肯定是不对的。可以通过debug来调试一下就知道了-->
     <td>
     <a href="message_deleteSend.action?id=${id}">删除</a>&nbsp;
     <a href="message_show.action?id=${id}&isRead=1">查看</a>&nbsp;
     </td>
     </tr>
     </s:iterator>
  
  </s:else>

     <tr>
     <td colspan="4">
   <jsp:include page="/inc/pager.jsp">
	<jsp:param value="message_listSend.action" name="url"/>
	<jsp:param value="${pages.totalRecord}" name="items"/>
	<jsp:param value="con" name="params"/> 
</jsp:include>
     
     </td>
     </tr>
     </table>
  
</body>
</html>