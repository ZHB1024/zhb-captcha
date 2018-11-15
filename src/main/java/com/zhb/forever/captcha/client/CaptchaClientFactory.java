package com.zhb.forever.captcha.client;

import com.zhb.forever.captcha.Constants;
import com.zhb.forever.framework.spring.bean.locator.SpringBeanLocator;

/**
*@author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
*@createDate 2018年11月15日下午2:41:52
*/

public class CaptchaClientFactory {

    public static CaptchaClient getCaptchaClientBean() {
        Object bean = SpringBeanLocator.getInstance(
                Constants.CAPTCHA_CLIENT_CONF).getBean(
                        Constants.CAPTCHA_CLIENT);
        return (CaptchaClient) bean;
    }

}


