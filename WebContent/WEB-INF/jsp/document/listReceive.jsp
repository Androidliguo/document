<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>接收到的公文列表</title>
</head>
<body>
<jsp:include page="inc/inc.jsp"></jsp:include> 
<table class="ct" width="800" align="center" border="1" cellpadding="0" cellspacing="0">
<tr>
 <td colspan="5">
 <form action="document_listReceive.action" method="get">
 <!--注意这里的话，我们需要将查询的条件和是否已读传过去。如果我们不将isRead传过去的话，那么在默认的情况下，
 isRead的值就为0.这样的话，我们在查找的时候，就会找不到这个值。这是要注意的。这里为什么需要使用
 隐藏域呢?因为对于isRead的话，我们根本不可能自己输入。所以我们需要使用隐藏域 -->
 <input type="text" name="con" value="${con}"/>
 <s:select theme="simple" list="#deps" name="depId" listKey="id" listValue="name" headerKey="0" headerValue="选择部门"></s:select>
  <input  type="hidden" name="isRead" value="${param.isRead}"/>
<input type="submit" value="筛选">
 </form>
 <a href="document_listReceive.action?isRead=1">已读公文</a>
 <a href="document_listReceive.action?isRead=0">未读公文</a>
 </td>
 </tr>
  <tr>
  <td>公文标识</td>
  <td>公文标题</td>
  <td>接收时间</td>
  <td>发送人</td>
  <td>操作</td>
  </tr>
  <s:if test="#pages.totalRecord<=0">
  <tr>
  <td colspan="5">
     当前没有相应的公文
  </td>
  </tr>
  </s:if>
  <s:else>
   <s:iterator value="#pages.datas">
     <tr>
     <td>${id }</td>
      <td>
      <a href="document_updateRead.action?id=${id}&&isRead=${isRead}">${title}</a>
      </td>
      <!--这里的话，可以指定日期的格式-->
      <td>
      <s:date name="createDate" format="yyyy-MM-dd"/>
      </td>
      <td>
      ${user.username}
      </td>
      <!--注意我们在这里的话，应该使用updateRead,否则的话，是会出现逻辑错误的。不应该适用show。这是要注意的，  -->
     <td>
     <a href="document_updateRead.action?id=${id}&&isRead=${isRead}">查看</a>&nbsp;
     </td>
     </tr>
     </s:iterator>
  
  </s:else>

     <tr>
     <td colspan="5">
   <jsp:include page="/inc/pager.jsp">
	<jsp:param value="document_listReceive.action" name="url"/>
	<jsp:param value="${pages.totalRecord}" name="items"/>
	<jsp:param value="con,isRead,depId" name="params"/> 
</jsp:include>
     
     </td>
     </tr>
     </table>
  
</body>
</html>