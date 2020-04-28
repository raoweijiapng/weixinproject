package com.mst.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mst.util.QRCodeGenerator;

@Controller
public class TestController {
	
	public String appid = "wxe6774713a448fb6f";
	public String secret = "1fec63da4f2a53c3224e6adf6dd4876e";
	
	@RequestMapping("login") 
	public String tolTest() { 
		return "login"; 
	}
	
	//把登录url生成二维码
    @RequestMapping("qrcode")
    public void login(HttpServletResponse response){
    	
    	String url = "https://open.weixin.qq.com/connect/oauth2/authorize?";
    	
    	String redirect_uri = URLEncoder.encode("http://eeee.free.idcfengye.com/getCode");
				
		String params = "appid="+appid+"&secret="+secret+"&redirect_uri="+redirect_uri+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
		
    	QRCodeGenerator.creatRrCode(url+params, 180,180,response);
    	
    }
    
    //扫描二维码登录的
    @RequestMapping("/getCode")
    public ModelAndView toSuccess(HttpServletRequest request){
    	String code = request.getParameter("code");
    	
    	String url = "https://api.weixin.qq.com/sns/oauth2/access_token?";
    	
    	String params = "appid="+appid+"&secret="+secret+"&code="+code+"&grant_type=authorization_code";
    	
    	//调用httpGet方法发送url并返回json对象,可以获取access_token和refresh_token
        JSONObject jsonObject = this.httpGet(url+params);
        
        //使用refresh_token刷新access_token  
        String refresh_token = jsonObject.getString("refresh_token");
        
        String url1 = "https://api.weixin.qq.com/sns/oauth2/refresh_token?";
        
        String params1 = "appid="+appid+"&grant_type=refresh_token&refresh_token="+refresh_token;
        
        //调用httpGet方法发送url并返回json对象,可以获取刷新后的access_token
        JSONObject jsonObject1 = this.httpGet(url1+params1);
        
        //使用刷新的access_token和openid获取用户信息
        String access_token = jsonObject.getString("access_token");
        
        String openid = jsonObject.getString("openid");
        
        String url2 = "https://api.weixin.qq.com/sns/userinfo?";
        
        String params2 = "access_token="+access_token+"&openid="+openid+"&lang=zh_CN";
        
        //调用httpGet方法发送url并返回json对象,获取用户信息
        JSONObject jsonObject2 = this.httpGet(url2+params2);		
    	
    	System.out.println(jsonObject2);
    	
    	ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("success");
        modelAndView.addObject("userInfo", jsonObject2);
    	
    	return modelAndView;
    	
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
