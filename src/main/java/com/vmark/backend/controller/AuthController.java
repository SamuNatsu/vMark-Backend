package com.vmark.backend.controller;

import com.vmark.backend.service.AuthService;
import com.vmark.backend.utils.CaptchaUtil;
import com.vmark.backend.utils.JsonMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    // ===== Services =====
    @Autowired
    private AuthService authService;
    // ===== End of Services =====


    // ===== Validators =====
    // Account validator (6 <= length <= 30, composed of alphas, digits and underlines)
    private static final Pattern accountRegex = Pattern.compile("^[0-9a-zA-Z_]{6,30}$");

    // Password validator (SHA 256 string, lowercase)
    private static final Pattern passwordRegex = Pattern.compile("^[0-9a-z]{64}$");

    // Captcha validator (length = 4, composed of alphas and digits)
    private static final Pattern captchaRegex = Pattern.compile("^[0-9a-zA-Z]{4}$");
    // ===== End of Validators =====


    // ===== Http Servlet =====
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private HttpServletResponse httpServletResponse;
    // ===== End of Http Servlet =====


    // ===== Mappings =====
    // Captcha
    @GetMapping(value = "/captcha", produces = MediaType.IMAGE_GIF_VALUE)
    public byte[] captcha() {
        // Call service
        return authService.genCaptcha(httpServletRequest.getSession(), httpServletResponse);
    }

    // Register
    @PostMapping("/register")
    public String register(@RequestParam("account") String account,
                           @RequestParam("password") String password,
                           @RequestParam("captcha") String captcha) {
        // Validate params
        Matcher accountMatcher = accountRegex.matcher(account);
        Matcher passwordMatcher = passwordRegex.matcher(password);
        Matcher captchaMatcher = captchaRegex.matcher(captcha);
        if (!accountMatcher.matches())
            return JsonMsg.failed("message.auth.register.invalid_account");
        if (!passwordMatcher.matches())
            return JsonMsg.failed("message.auth.register.invalid_password");
        if (!captchaMatcher.matches())
            return JsonMsg.failed("message.auth.register.invalid_captcha");

        // Check captcha
        switch (CaptchaUtil.check(captcha, httpServletRequest.getSession())) {
            case PASS -> {
                // Call service
                return authService.register(account, password);
            }
            case REJECT -> {
                // Reject
                return JsonMsg.failed("message.auth.register.wrong_captcha");
            }
            case TIMEOUT -> {
                // Timeout
                return JsonMsg.failed("message.auth.register.captcha_timeout");
            }
        }
        return null;
    }

    // Login
    @PostMapping("/login")
    public String login(@RequestParam("account") String account,
                        @RequestParam("password") String password,
                        @RequestParam("captcha") String captcha) {
        // Validate params
        Matcher accountMatcher = accountRegex.matcher(account);
        Matcher passwordMatcher = passwordRegex.matcher(password);
        Matcher captchaMatcher = captchaRegex.matcher(captcha);
        if (!accountMatcher.matches())
            return JsonMsg.failed("message.auth.login.invalid_account");
        if (!passwordMatcher.matches())
            return JsonMsg.failed("message.auth.login.invalid_password");
        if (!captchaMatcher.matches())
            return JsonMsg.failed("message.auth.login.invalid_captcha");

        // Call service
        return authService.login(account, password, httpServletRequest.getSession());
    }

    // Logout
    @GetMapping("/logout")
    public String logout() {
        // Call service
        return authService.logout(httpServletRequest.getSession());
    }

    // Info
    @GetMapping("/info")
    public String info() {
        // Call service
        return authService.info(httpServletRequest.getSession());
    }
    // ===== End of Mappings =====
}
