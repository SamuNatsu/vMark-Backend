package com.vmark.backend.controller;

import com.vmark.backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/auth")
public class AuthController {
    // Services
    @Autowired
    private AuthService authService;

    // ===== Validators =====
    // Account validator (6 <= length <= 20, composed of alphas, digits and underlines)
    private final static Pattern accountRegex =
            Pattern.compile("^[0-9a-zA-Z_]{6,20}$");
    // Password validator (8 <= length <= 20, must contain alpha, digit and special character)
    private final static Pattern passwordRegex =
            Pattern.compile("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[._~!@#$^&*])[A-Za-z0-9._~!@#$^&*]{8,20}$");

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
            return "{\"status\":\"failed\",\"message\":\"login.message.invalid_account\"}";

        // Call service
        return authService.login(account, password);
    }

    // Logout

}
