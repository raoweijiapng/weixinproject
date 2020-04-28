package com.mst.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

@Controller
public class TestController {
	
	
	@RequestMapping("/index") 
	public String tolTest() { 
		return "index"; 
	}
	 
	
	@RequestMapping("/login")
    public  String tologin() {
		return "login";
	}
	
	//微信扫码后的回调方法，里面用获取的参数code，state去请求微信官方地址获取access_token，openid，还有用户信息
    @RequestMapping("/callBackLogin")
    public String login(HttpServletRequest request, HttpServletResponse response, Model model){
    	
    	//获取微信扫码后的参数code与state
        String code = request.getParameter("code");
        String state = request.getParameter("state");        
        System.out.println("code=" + code);
        System.out.println("state=" + state);
        
        //用code,appid,secret请求微信官方地址获取access_token,openid
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?";
        url += "appid=你的appid";
        url += "&secret=你的secret";
        url += "&code=" + code + "&grant_type=authorization_code";
        //调用httpGet方法发送url并返回json对象
        JSONObject jsonObject = this.httpGet(url);
        // 获取微信开放平台票据号
        String at = jsonObject.getString("access_token"); 
        // 获取登录微信的唯一凭证号
        String openId = jsonObject.getString("openid");  
        System.out.println("at=" + at);
        System.out.println("openId=" + openId);
        
        //用access_token,openid请求微信官方地址获取用户数据
        url="https://api.weixin.qq.com/sns/userinfo?access_token="+at+"&openid="+openId;
        //调用httpGet方法发送url并返回json对象
        jsonObject = this.httpGet(url);
        System.out.println("用户信息"+jsonObject);
        // 把用户信息放进request，给前端页面调用显示
        model.addAttribute("userInfo", jsonObject);  
        // 查询数据库，判断这个微信的openid是否存在，如果不存在，新增到数据库表（自动创建一个用户,保存微信返回的openid），如果已存在，直接登录成功）
        
        return "loginSuccess";
    }
    
 
    // 使用htpClient发送url请求获取返回，用阿里的fastjson解析json
    public JSONObject httpGet(String url) {
        JSONObject jsonResult = null;
        try {
        	//目前HttpClient最新版的实现类为CloseableHttpClient
            CloseableHttpClient client = HttpClients.createDefault();
            HttpGet request = new HttpGet(url);
            
            //执行Request请求,CloseableHttpClient的execute方法返回的response都是CloseableHttpResponse类型            
            CloseableHttpResponse response = client.execute(request);
            
            //判断响应的状态码是否为200
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            	//用EntityUtils.toString()这个静态方法将HttpEntity转换成字符串,防止服务器返回的数据带有中文,所以在转换的时候将字符集指定成utf-8就可以了
                String strResult = EntityUtils.toString(response.getEntity(),"UTF-8");
                //将字符串转换json对象
                jsonResult = JSON.parseObject(strResult);
            } else {
                System.out.println("请求出错");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonResult;
    }

	
	
}
