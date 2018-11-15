package com.zhb.forever.captcha.client.impl;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.octo.captcha.component.image.backgroundgenerator.BackgroundGenerator;
import com.octo.captcha.component.image.backgroundgenerator.UniColorBackgroundGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.RandomTextPaster;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.word.FileDictionary;
import com.octo.captcha.component.word.wordgenerator.ComposeDictionaryWordGenerator;
import com.octo.captcha.engine.GenericCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;
import com.octo.captcha.service.multitype.GenericManageableCaptchaService;
import com.zhb.forever.captcha.client.CaptchaClient;
import com.zhb.forever.framework.util.AjaxData;

/**
 * @author   zhanghb<a href="mailto:zhb20111503@126.com">zhanghb</a>
 * @createDate 2018年11月15日下午2:41:40
 */

public class CaptchaClientImpl implements CaptchaClient {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private GenericManageableCaptchaService imageCaptchaService;

    @Override
    public void generateCaptchaImage(String tokenId, HttpServletRequest request, HttpServletResponse response) {
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

    public static GenericManageableCaptchaService generateImageCaptchaService() {
        Integer WIDTH = 60;// 验证码宽
        Integer HEIGHT = 25;// 验证码高
        Integer MINLENGTH = 4;// 字符最短
        Integer MAXLENGTH = 4;// 字符最长
        Integer MINFONTSIZE = 10;// 字符最小
        Integer MAXFONTSIZE = 15;// 字符最大
        Integer LIVETIME = 5 * 60;// 验证码存活时间
        Integer MAXCAPTCHASTORESIZE = 200000;// 最大存储大小
        // 随机颜色
//        RandomRangeColorGenerator textColor = new RandomRangeColorGenerator(new int[]{0,0},new int[]{0,0},new int[]{0,0},new int[]{255,255});
        // 验证码字符
//        RandomTextPaster randomTextPaster = new RandomTextPaster(MINLENGTH, MAXLENGTH, textColor,true);
        RandomTextPaster randomTextPaster = new RandomTextPaster(MINLENGTH, MAXLENGTH, Color.gray);

        // 背景(渐变色)
//        BackgroundGenerator colorbgGen = new GradientBackgroundGenerator(WIDTH, HEIGHT,new Color(46,195,231),Color.WHITE);
        // 背景大小及样式设置,UniColorBackgroundGenerator类生成的是统一背景，这里背景统一是lightGray颜色
        // 宽度为180，高度为50。
        BackgroundGenerator colorbgGen = new UniColorBackgroundGenerator(WIDTH, HEIGHT);
        /*
         * //FunkyBackgroundGenerator类生成的是炫酷背景 BackgroundGenerator back = new
         * FunkyBackgroundGenerator(new Integer( 180), new Integer(50));
         */

        // 随机生成的字体大小和字体类型
        RandomFontGenerator randomFontGenerator = new RandomFontGenerator(MINFONTSIZE, MAXFONTSIZE,
                new Font[] { new Font("Arial", 0, 10), new Font("Tahoma", 0, 10) });
        // 生成图片对象
        ComposedWordToImage cwti = new ComposedWordToImage(randomFontGenerator, colorbgGen, randomTextPaster);
        // 随机文本的字典
        ComposeDictionaryWordGenerator cdwg = new ComposeDictionaryWordGenerator(new FileDictionary("toddlist"));
        GimpyFactory gf = new GimpyFactory(cdwg, cwti);

        GenericCaptchaEngine gce = new GenericCaptchaEngine(new GimpyFactory[] { gf });
        // 返回一个对象,
        return new GenericManageableCaptchaService(gce, LIVETIME, MAXCAPTCHASTORESIZE);
    }

    public GenericManageableCaptchaService getImageCaptchaService() {
        return imageCaptchaService;
    }

    public void setImageCaptchaService(GenericManageableCaptchaService imageCaptchaService) {
        this.imageCaptchaService = imageCaptchaService;
    }

}
