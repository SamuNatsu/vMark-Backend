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

@CrossOrigin
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    // ===== Services =====
    private final AuthService authService;
    // ===== End of Services =====


    // ===== Validators =====
    // Account validator (6 <= length <= 30, composed of alphas, digits and underlines)
    private static final Pattern accountPattern = Pattern.compile("^[0-9a-zA-Z_]{6,30}$");

    // Password validator (SHA-256 string in lowercase)
    private static final Pattern passwordPattern = Pattern.compile("^[0-9a-z]{64}$");

    // Captcha validator (length = 4, composed of uppercase alphas and digits)
    private static final Pattern captchaPattern = Pattern.compile("^[0-9A-Z]{4}$");
    // ===== End of Validators =====


    // ===== Constructor =====
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    // ===== End of Constructor =====


    // ===== Mappings =====
    // Get captcha
    @GetMapping(value = "/captcha", produces = MediaType.IMAGE_GIF_VALUE)
    public byte[] captcha(HttpServletRequest request,
                          HttpServletResponse response) {
        // ===== Call service =====
        return authService.genCaptcha(request.getSession(), response);
    }

    // Info
    @GetMapping("/info")
    public String info(HttpServletRequest request) {
        // ===== Call service =====
        return authService.info(request.getSession());
    }

    // Login
    @PostMapping("/login")
    public String login(@RequestParam("account") String account,
                        @RequestParam("password") String password,
                        @RequestParam("captcha") String captcha,
                        HttpServletRequest request) {
        // ===== Validate params =====
        Matcher accountMatcher = accountPattern.matcher(account);
        if (!accountMatcher.matches())
            return JsonMsg.failed("message.invalid.account");

        Matcher passwordMatcher = passwordPattern.matcher(password);
        if (!passwordMatcher.matches())
            return JsonMsg.failed("message.invalid.password");

        Matcher captchaMatcher = captchaPattern.matcher(captcha);
        if (!captchaMatcher.matches())
            return JsonMsg.failed("message.invalid.captcha");

        // ===== Check captcha =====
        CaptchaUtil.CheckStatus status = CaptchaUtil.check(captcha, request.getSession());
        if (status == CaptchaUtil.CheckStatus.TIMEOUT)
            return JsonMsg.failed("message.captcha.timeout");
        if (status == CaptchaUtil.CheckStatus.WRONG)
            return JsonMsg.failed("message.captcha.wrong");

        // ===== Call service =====
        return authService.login(account, password, request.getSession());
    }

    // Logout
    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        // ===== Call service =====
        return authService.logout(request.getSession());
    }
    // ===== End of Mappings =====
}
