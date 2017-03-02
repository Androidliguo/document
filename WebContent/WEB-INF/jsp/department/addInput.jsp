<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  <%@taglib prefix="s" uri="/struts-tags" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加部门</title>
</head>
<body>
  <jsp:include page="inc/inc.jsp"></jsp:include><br/>
  <s:form action="department_add" method="post">
  <s:textfield label="部门名称" name="name"/>
  <s:submit value="添加"></s:submit>
  </s:form>

</body>
</html>