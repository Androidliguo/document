<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>查看公文</title>
</head>
<body>
<jsp:include page="inc/inc.jsp"></jsp:include> 
<h2>${title}</h2>
<p>
<s:date name="createDate" format="yyyy-MM-dd ss hh:ss"/>----
${user.nickname }[${user.department.name}]
</p>
<hr/>
<p>
 
</p>
 <h3>接收公文的部门</h3>
 <s:iterator value="#deps">
  [${name}]
 </s:iterator>
<p>
  <h3>附件信息</h3>
 <s:if test="#atts.size()<=0">
   没有相关的附件
 </s:if>
 <!-- 这里的话，我们需要通过newName来进行下载 -->
 <s:else>
  <s:iterator value="#atts">
    <a href="<%=request.getContextPath()%>/upload/${newName}">${oldName }</a><br/>
  </s:iterator>
 </s:else>
</p>
<p>
<h3>内容信息</h3>
${content }
</p>
</body>
</html>