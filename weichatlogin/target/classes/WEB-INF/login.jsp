<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="content-type" content="text/html;charset=utf-8"/>
    <title>微信登录</title>
 
    <script src="http://res.wx.qq.com/connect/zh_CN/htmledition/js/wxLogin.js"></script>
    <script src="http://code.jquery.com/jquery-latest.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
 
            var obj = new WxLogin({
            	//div标签的id
                id:"login_container",
                //这里的appid是需要服务类型的微信公众号的
                appid: "wxe6774713a448fb6f",
             	// 固定参数，微信官方提供的解析参数
                scope: "snsapi_login",
             	// 回调url，就是你扫码后，微信官方返回一些参数（code，state），然后调用你这里指定的方法
                redirect_uri: "/callBackLogin",   
                state: "",   // 该参数可以不写
                style: "black",  //二维码黑白风格，该参数可以不写
                href: ""   // 该参数可以不写
            });
 
        });
 
    </script>
 
</head>
<body>
	<div><h1>微信登录</h1></div>
    <div id="login_container"></div>
</body>
</html>
