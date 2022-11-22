package com.vmark.backend.controller;

import com.vmark.backend.service.AuthService;
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
    // ===== External Autowired =====
    private final AuthService authService;
    // ===== End of External Autowired =====


    // ===== Validators =====
    // Account validator (6 <= length <= 30, composed of alphas, digits and underlines)
    private static final Pattern accountRegex = Pattern.compile("^[0-9a-zA-Z_]{6,30}$");

    // Password validator (SHA 256 string, lowercase)
    private static final Pattern passwordRegex = Pattern.compile("^[0-9a-z]{64}$");

    // Captcha validator (length = 4, composed of alphas and digits)
    private static final Pattern captchaRegex = Pattern.compile("^[0-9A-Z]{4}$");
    // ===== End of Validators =====


    // ===== Constructor =====
    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    // ===== End of Constructor =====


    // ===== Mappings =====
    // Captcha
    @GetMapping(value = "/captcha", produces = MediaType.IMAGE_GIF_VALUE)
    public byte[] captcha(HttpServletRequest request,
                          HttpServletResponse response) {
        // ===== Call service =====
        return authService.genCaptcha(request.getSession(), response);
    }

    // Login
    @PostMapping("/login")
    public String login(@RequestParam("account") String account,
                        @RequestParam("password") String password,
                        @RequestParam("captcha") String captcha,
                        HttpServletRequest request) {
        // ===== Validate params =====
        Matcher accountMatcher = accountRegex.matcher(account);
        if (!accountMatcher.matches())
            return JsonMsg.failed("message.invalid.account");

        Matcher passwordMatcher = passwordRegex.matcher(password);
        if (!passwordMatcher.matches())
            return JsonMsg.failed("message.invalid.password");

        Matcher captchaMatcher = captchaRegex.matcher(captcha);
        if (!captchaMatcher.matches())
            return JsonMsg.failed("message.invalid.captcha");

        // ===== Call service =====
        return authService.login(account, password, request.getSession());
    }

    // Logout
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        // ===== Call service =====
        return authService.logout(request.getSession());
    }

    // Info
    @GetMapping("/info")
    public String info(HttpServletRequest request) {
        // ===== Call service =====
        return authService.info(request.getSession());
    }
    // ===== End of Mappings =====
}
