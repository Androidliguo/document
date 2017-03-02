<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户列表</title>
</head>
<body>
<jsp:include page="inc/inc.jsp"></jsp:include> 
<table class="ct" width="800" align="center" border="1" cellpadding="0" cellspacing="0">
<tr>
 <td colspan="8">
 <form action="user_list.action" method="get">
 <s:select theme="simple" list="#ds" listKey="id" listValue="name"  name="depId" headerKey="0" headerValue="所有部门">
 </s:select>
  <s:submit  theme="simple" value="筛选"></s:submit>
 </form>
 </td>
 </tr>
  <tr>
  <td>用户标识</td>
  <td>用户名称</td>
  <td>用户昵称</td>
  <td>所属部门</td>
  <td>用户密码</td>
  <td>用户邮件</td>
  <td>用户类型</td>
  <td>操作</td>
  </tr>
  <s:if test="#pages.totalRecord=0">
  <tr>
  <td colspan="8">
  当前还没有任何用户
  </td>
  </tr>
  </s:if>
  <s:else>
   <s:iterator value="#pages.datas">
     <tr>
     <td>${id }</td>
     <td><a href="user_show.action?id=${id}">${username }</a></td>
      <td>${nickname}</td>
      <td>${department.name}</td>
      <td>${password}</td>
      <td>${email}</td>
      <!--这里的话，需要注意这种写法这里是没有$符号的。  -->
      <s:if test="type<1">
      <td>普通用户</td>
      </s:if>
      <s:else>
      <td>管理员</td>
      </s:else>
     <td>
     <a href="user_delete.action?id=${id}">删除</a>&nbsp;
     <a href="user_updateInput.action?id=${id}">更新</a>&nbsp;
     <a href="user_show.action?id=${id}">查看</a>&nbsp;
     </td>
     </tr>
     </s:iterator>
  
  </s:else>

     <tr>
     <td colspan="8">
   <jsp:include page="/inc/pager.jsp">
	<jsp:param value="user_list.action" name="url"/>
	<jsp:param value="${totalRecord }" name="items"/>
	<jsp:param value="depId" name="params"/> 
</jsp:include>
     
     </td>
     </tr>
     </table>
  
</body>
</html>