package com.vmark.backend.utils;

import com.yufeixuan.captcha.Captcha;
import com.yufeixuan.captcha.GifCaptcha;

import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.util.Date;

public class CaptchaUtil {
    // Generate captcha
    public static byte[] generate(HttpSession session) {
        // Generate captcha
        GifCaptcha gifCaptcha = new GifCaptcha(100, 40, 4);
        gifCaptcha.setCharType(Captcha.TYPE_NUM_AND_UPPER);

        // Clear session
        session.removeAttribute("captcha");
        session.removeAttribute("captcha_timestamp");

        // Generate picture
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (!gifCaptcha.out(baos))
            return null;

        // Return picture byte
        session.setAttribute("captcha", gifCaptcha.text());
        session.setAttribute("captcha_timestamp", new Date().getTime());
        return baos.toByteArray();
    }

    // Check status
    public enum CheckStatus {
        PASS,
        WRONG,
        TIMEOUT
    }

    // Check captcha
    public static CheckStatus check(String captcha, HttpSession session) {
        // Check timeout
        Long timeout = (Long)session.getAttribute("captcha_timestamp");
        if (timeout == null || new Date().getTime() - timeout > 60000L)
            return CheckStatus.TIMEOUT;

        // Check match
        return captcha.compareTo((String)session.getAttribute("captcha")) == 0 ?
                CheckStatus.PASS : CheckStatus.WRONG;
    }
}
