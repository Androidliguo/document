<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
  <%@taglib prefix="s" uri="/struts-tags" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设置可发文部门</title>
</head>
<body>
<jsp:include page="inc/inc.jsp"></jsp:include>
<form  action="department_addDepScope.action" method="post">
<s:hidden name="id"></s:hidden>
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
   <!--这是一个复选框的内容。我们可以通过这个复选框来传值。这是要注意的。listValue="name"
         表示在  复选框中显示的内容是部门的name.而listkey="id",表示通过表单传过去的是部门的id
    这样的话，我们就可以在Action中通过一组整型的id来获取一组的部门的id了。-->
   <s:checkboxlist theme="simple" name="scopeDepIds" list="#ds" listKey="id" listValue="name" value="#ids"/>
   </td>
  </tr>
  <tr>
  <td colspan="2">
  <input type="submit" value="提交" />
  </td>
  </tr>
  </table>
  </form>
</body>
</html>