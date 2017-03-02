<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>接收到的私人信件列表</title>
</head>
<body>
<jsp:include page="inc/inc.jsp"></jsp:include> 
<table class="ct" width="800" align="center" border="1" cellpadding="0" cellspacing="0">
<tr>
 <td colspan="5">
 <form action="message_listReceive.action" method="get">
 <!--注意这里的话，我们需要将查询的条件和是否已读传过去。如果我们不将isRead传过去的话，那么在默认的情况下，
 isRead的值就为0.这样的话，我们在查找的时候，就会找不到这个值。这是要注意的。这里为什么需要使用
 隐藏域呢?因为对于isRead的话，我们根本不可能自己输入。所以我们需要使用隐藏域 -->
 <input type="text" name="con" value="${con}"/>
  <input  type="hidden" name="isRead" value="${param.isRead}"/>
<input type="submit" value="筛选">
 </form>
 <a href="message_listReceive.action?isRead=1">已读信件</a>
 <a href="message_listReceive.action?isRead=0">未读信件</a>
 </td>
 </tr>
  <tr>
  <td>信件标识</td>
  <td>信件标题</td>
  <td>接收时间</td>
  <td>发送人</td>
  <td>操作</td>
  </tr>
  <s:if test="#pages.totalRecord<=0">
  <tr>
  <td colspan="5">
     当前没有未读信件
  </td>
  </tr>
  </s:if>
  <s:else>
   <s:iterator value="#pages.datas">
     <tr>
     <td>${id }</td>
      <td>
      <a href="message_updateRead.action?id=${id}&&isRead=${isRead}">${title}</a>
      </td>
      <!--这里的话，可以指定日期的格式-->
      <td>
      <s:date name="createDate" format="yyyy-MM-dd"/>
      </td>
      <td>
      ${user.username}
      </td>
     <td>
     <a href="message_deleteReceive.action?id=${id}">删除</a>&nbsp;
     <a href="message_updateRead.action?id=${id}&&isRead=${isRead}">查看</a>&nbsp;
     </td>
     </tr>
     </s:iterator>
  
  </s:else>

     <tr>
     <td colspan="5">
   <jsp:include page="/inc/pager.jsp">
	<jsp:param value="message_listReceive.action" name="url"/>
	<jsp:param value="${pages.totalRecord}" name="items"/>
	<jsp:param value="con,isRead" name="params"/> 
</jsp:include>
     
     </td>
     </tr>
     </table>
  
</body>
</html>