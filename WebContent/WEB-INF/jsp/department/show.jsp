<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看部门信息</title>
</head>
<body>
<jsp:include page="inc/inc.jsp"></jsp:include>
 <table class="ct" width="800" align="center" border="1" cellpadding="0" cellspacing="0"> 
  <tr>
  <td>部门Id</td><td>部门名称</td>
  </tr>
  <tr>
  <td>${id}</td><td>${name}</td>
  </tr>
  <tr>
  <td colspan="2">可发文部门</td>
  </tr>
 

    <tr>
    <td colspan="2">
      <s:iterator value="#deps">
        ${name},
        </s:iterator> 
      
    </td>
    </tr>
  </table>
</body>
</html>