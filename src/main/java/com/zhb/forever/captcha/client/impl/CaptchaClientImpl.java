package com.zhb.forever.captcha.client.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;
import com.zhb.forever.captcha.client.CaptchaClient;
import com.zhb.forever.framework.util.AjaxData;

/**
 * @author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
 * @createDate 2018年11月15日下午2:41:40
 */

public class CaptchaClientImpl implements CaptchaClient {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private ImageCaptchaService imageCaptchaService = new DefaultManageableImageCaptchaService(); ;

    @Override
    public void generateCaptchaImage(String tokenId,HttpServletRequest request, HttpServletResponse response) {
        try {
            ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
            BufferedImage challenge = imageCaptchaService.getImageChallengeForID(tokenId, request.getLocale());

            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0L);
            response.setContentType("image/jpeg");
            
            ImageIO.write(challenge, "jpeg", jpegOutputStream);
            byte[] captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
            ServletOutputStream respOs = response.getOutputStream();
            respOs.write(captchaChallengeAsJpeg);
            respOs.flush();
            respOs.close();
        } catch (IOException e) {
            logger.error("generate captcha image error: {}", e.getMessage());
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

}
