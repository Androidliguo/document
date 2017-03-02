<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  <%@taglib prefix="s" uri="/struts-tags" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改用户个人信息</title>
</head>
<body>
  <s:form action="user_updateSelf" method="post">
  <s:hidden name="id"></s:hidden>
  <s:textfield label="用户名称" name="username"/>
  <s:textfield label="用户昵称" name="nickname"/>
   <s:textfield label="用户密码" name="password"/>
   <s:textfield label="用户邮箱" name="email"> </s:textfield>
   <s:submit value="更新"></s:submit>
  </s:form>
</body>
</html>