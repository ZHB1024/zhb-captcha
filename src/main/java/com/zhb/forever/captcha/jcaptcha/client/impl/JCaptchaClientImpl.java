package com.zhb.forever.captcha.jcaptcha.client.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.octo.captcha.service.multitype.GenericManageableCaptchaService;
import com.zhb.forever.captcha.jcaptcha.client.JCaptchaClient;
import com.zhb.forever.framework.util.AjaxData;
import com.zhb.forever.framework.util.IOUtil;

/**
 * @author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
 * @createDate 2018年11月15日下午2:41:40
 */

public class JCaptchaClientImpl implements JCaptchaClient {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    
    public static final String CAPTCHA_IMAGE_FORMAT = "jpeg";

    private GenericManageableCaptchaService imageCaptchaService;

    @Override
    public void generateCaptchaImage(String tokenId, HttpServletRequest request, HttpServletResponse response) {
        ServletOutputStream respOs = null;
        try {
            //imageCaptchaService = generateImageCaptchaService();
            ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
            BufferedImage challenge = imageCaptchaService.getImageChallengeForID(tokenId);

            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0L);
            response.setContentType("image/jpeg");

            ImageIO.write(challenge, CAPTCHA_IMAGE_FORMAT, jpegOutputStream);
            byte[] captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
            respOs = response.getOutputStream();
            respOs.write(captchaChallengeAsJpeg);
            respOs.flush();
        } catch (IOException e) {
            logger.error("generate captcha image error: {}", e.getMessage());
        }finally {
            IOUtil.closeQuietly(respOs);
        }
    }

    @Override
    public AjaxData validateCaptchaImage(String captcha, String tokenId) {
        AjaxData ajaxData = new AjaxData();
        Boolean isResponseCorrect = imageCaptchaService.validateResponseForID(tokenId, captcha);
        ajaxData.setFlag(true);
        ajaxData.setData(isResponseCorrect);
        return ajaxData;
    }

    public GenericManageableCaptchaService getImageCaptchaService() {
        return imageCaptchaService;
    }

    public void setImageCaptchaService(GenericManageableCaptchaService imageCaptchaService) {
        this.imageCaptchaService = imageCaptchaService;
    }

}
