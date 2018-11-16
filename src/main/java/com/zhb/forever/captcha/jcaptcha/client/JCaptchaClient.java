package com.zhb.forever.captcha.jcaptcha.client;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhb.forever.framework.util.AjaxData;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年11月15日下午2:41:27
*/

public interface JCaptchaClient {
    
    void generateCaptchaImage(String tokenId,HttpServletRequest request, HttpServletResponse response);
    
    AjaxData validateCaptchaImage(String captcha,String tokenId);

}


