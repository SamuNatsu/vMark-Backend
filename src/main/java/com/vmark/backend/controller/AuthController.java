package com.vmark.backend.controller;

import com.vmark.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/auth")
public class AuthController {
    // ===== Services =====
    @Autowired
    private AuthService authService;

    // ===== Validators =====
    // Account validator (6 <= length <= 20, composed of alphas, digits and underlines)
    private final static Pattern accountRegex =
            Pattern.compile("^[0-9a-zA-Z_]{6,20}$");
    // Password validator (SHA 256 string)
    private final static Pattern passwordRegex =
            Pattern.compile("^[0-9a-z]{64}$");

    // ===== Http Servelet Request =====
    @Autowired
    private HttpServletRequest httpServeletRequest;

    // ===== Mappings =====
    // Login
    @PostMapping("login")
    public String login(@RequestParam("account") String account,
                        @RequestParam("password") String password) {
        // Validate params
        Matcher accountMatcher = accountRegex.matcher(account);
        Matcher passwordMatcher = passwordRegex.matcher(password);
        if (!accountMatcher.matches())
            return "{\"status\":\"failed\",\"message\":\"login.message.invalid_account\"}";
        if (!passwordMatcher.matches())
            return "{\"status\":\"failed\",\"message\":\"login.message.invalid_password\"}";

        // Call service
        HttpSession session = httpServeletRequest.getSession();
        session.setMaxInactiveInterval(86400);
        return authService.login(account, password, session);
    }

    // Logout
    @GetMapping("logout")
    public String logout() {
        // Call service
        HttpSession session = httpServeletRequest.getSession();
        session.setMaxInactiveInterval(86400);
        return authService.logout(session);
    }

    // Info
    @GetMapping("info")
    public String info() {
        // Call service
        HttpSession session = httpServeletRequest.getSession();
        session.setMaxInactiveInterval(86400);
        return authService.info(session);
    }
}
