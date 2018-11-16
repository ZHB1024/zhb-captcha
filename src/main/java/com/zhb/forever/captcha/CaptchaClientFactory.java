package com.zhb.forever.captcha;

import com.zhb.forever.captcha.Constants;
import com.zhb.forever.captcha.jcaptcha.client.JCaptchaClient;
import com.zhb.forever.framework.spring.bean.locator.SpringBeanLocator;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年11月15日下午2:41:52
*/

public class CaptchaClientFactory {

    public static JCaptchaClient getJCaptchaClientBean() {
        Object bean = SpringBeanLocator.getInstance(
                Constants.JCAPTCHA_CLIENT_CONF).getBean(
                        Constants.JCAPTCHA_CLIENT);
        return (JCaptchaClient) bean;
    }

}


