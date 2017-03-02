<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看个人信息</title>
</head>
<body>
 <table class="ct" width="800" align="center" border="1" cellpadding="0" cellspacing="0"> 
  <tr>
  <td>用户Id</td><td>${id }</td>
  </tr>
  <tr>
  <td>用户名称</td><td>${username}</td>
  </tr>
   <tr>
  <td>用户昵称</td><td>${nickname}</td>
  </tr>
   <tr>
  <td>用户密码</td><td>${password}</td>
  </tr>
   <tr>
  <td>所属部门</td><td>${department.name}</td>
  </tr>
  <tr>
   <td>用户邮箱</td><td>${email}</td>
  </tr>
  <tr>
  <td>用户类型</td>
  <s:if test="type<=0">
   <td>
         普通用户
   </td>
  </s:if>
  <s:else>
  <td>管理员</td>
  </s:else>
  </tr>
  
  </table>
</body>
</html>