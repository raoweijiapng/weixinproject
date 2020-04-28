<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
    <meta http-equiv="content-type" content="text/html;charset=utf-8"/>
    <title>hello</title>
 
</head>
<body>
    <div><h1>登录成功</h1></div>
    
    <div>
    	<ul>
    		<li><span>openid:&nbsp;</span>${userInfo.openid}</li>
    		<li><span>昵称:&nbsp;</span>${userInfo.nickname}</li>
	    	<li>
	    		<span>性别:&nbsp;</span>
		    	<c:if test="${userInfo.sex == 1}">男</c:if>
		    	<c:if test="${userInfo.sex == 2}">女</c:if>
		    	<c:if test="${userInfo.sex == 0}">未知</c:if>
	    	</li>
	    	<li><span>省份:&nbsp;</span>${userInfo.province}</li>
	    	<li><span>城市:&nbsp;</span>${userInfo.city}</li>
    		<li><span>国家:&nbsp;</span>${userInfo.country}</li>
    		<li>
	    		<span>用户头像:&nbsp;</span>
	    		${userInfo.headimgurl}
	    		<img alt="" src="${userInfo.headimgurl}">
    		</li>
    		<li><span>unionid:&nbsp;</span>${userInfo.unionid}</li>
    	</ul>    
    </div>
</body>
</html>
