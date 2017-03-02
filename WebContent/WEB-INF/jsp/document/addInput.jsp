<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>  
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>添加部门公文</title>
<!--注意这里的话，我们是加入了标准的文本框的输入。所以这里的话，我们需要引入相应的javaScript的文件.才能使用xheditor-->
<script type="text/javascript" src="<%=request.getContextPath() %>/xheditor/jquery/jquery-1.4.4.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/xheditor/xheditor-1.1.14-zh-cn.min.js"></script>
<script type="text/javascript">
	$(function(){
		$("#content").xheditor({tools:"full"})
	});
</script>
<!-- 这个是使用javascript来上传多个附件 -->
<script type="text/javascript">
	$(function(){
		$("#content").xheditor({tools:"full"});
		var ab = document.getElementById("addAttach");
		var ac = document.getElementById("attachContainer");
		ab.onclick = function(){
			var node = "<span><br/><input type='file' name='atts'/><input type='button' onclick='remove(this)' value='移除'/></span>";
			ac.innerHTML = ac.innerHTML+node;
		};
	});
	
	function remove(obj) {
		var ac = document.getElementById("attachContainer");
		ac.removeChild(obj.parentNode);
	}
</script>
</head>
<body>
<jsp:include page="inc/inc.jsp"></jsp:include> 
<s:fielderror></s:fielderror>
<form action="document_add.action" method="post" enctype="multipart/form-data">
<table class="ct" width="800" align="center" border="1" cellpadding="0" cellspacing="0">
 <tr>
    <td>公文标题</td>
     <td><input type="text" size="40" name="title"/></td>
</tr>
<tr>
    <td colspan="2">选择部门 :</td>

</tr>

<tr>
     <td colspan="2">
     <s:if test="#deps.size()>0">
       <s:checkboxlist list="#deps" listKey="id" listValue="name" name="depIds" theme="simple"></s:checkboxlist>
     </s:if>
     <s:else>
      没有部门可以选择
     </s:else>
     </td>
</tr>
<!-- 下面是添加附件的实现 -->
<tr>
		<td colspan="2">添加附件:</td>
	</tr>
	<tr>
		<td colspan="2">
		<input type="button" value="添加附件" id="addAttach"/>
			<div id="attachContainer">
			<input type="file" name="atts"/>
			</div>
		</td>
	</tr>


<tr>
  <td  colspan="2">公文内容</td>
</tr>

<tr>
   <td colspan="2">
   <!-- 要想使用文本框的话，这里的话，必须要设置好id和content，否则的话，是不能够使用这个文本框的-->
      <textarea rows="50" cols="130" id="content" name="content"></textarea>
    </td>
</tr>

<tr>
  <td colspan="2">
  <!--注意这里是使用了OGNL的形式来进行的，这是要注意的 注意size应该写成size(),而不是size. -->
  <s:if test="#deps.size()>0">
  <input type="submit" value="添加" align="bottom"/>
  </s:if>
  
  </td>
</tr>

</table>
</form>
  
</body>
</html>