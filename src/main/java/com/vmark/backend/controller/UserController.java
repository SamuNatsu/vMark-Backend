package com.vmark.backend.controller;

import com.vmark.backend.entity.User;
import com.vmark.backend.service.AuthService;
import com.vmark.backend.service.UserService;
import com.vmark.backend.utils.CaptchaUtil;
import com.vmark.backend.utils.JsonMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {
    // ===== Services =====
    private final AuthService authService;
    private final UserService userService;
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
    public UserController(AuthService authService,
                          UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }
    // ===== End of Constructor =====


    // ===== Mappings =====
    // Register user (Everyone)
    @PostMapping("/register")
    public String register(@RequestParam("account") String account,
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
        CaptchaUtil.CheckStatus checkStatus = CaptchaUtil.check(captcha, request.getSession());
        if (checkStatus == CaptchaUtil.CheckStatus.WRONG)
            return JsonMsg.failed("message.captcha.wrong");
        if (checkStatus == CaptchaUtil.CheckStatus.TIMEOUT)
            return JsonMsg.failed("message.captcha.timeout");

        // ===== Call service =====
        return userService.register(account, password);
    }

    // Update user info (Self or admin, privilege greater)
    @PostMapping("/update/info")
    public String updateInfo(@RequestParam("uid") int uid,
                             @RequestParam(value = "new_name", required = false) String newName,
                             @RequestParam(value = "new_password", required = false) String newPassword,
                             HttpServletRequest request) {
        // ===== Validate params =====
        if (uid < 1)
            return JsonMsg.failed("message.invalid.uid");

        if (newName != null) {
            newName = newName.trim();
            if (newName.length() < 6 || newName.length() > 30)
                return JsonMsg.failed("message.invalid.new_name");
        }

        if (newPassword != null) {
            Matcher passwordMatcher = passwordPattern.matcher(newPassword);
            if (!passwordMatcher.matches())
                return JsonMsg.failed("message.invalid.new_password");
        }

        if (newName == null && newPassword == null)
            return JsonMsg.success();

        // ===== Check login =====
        if (authService.checkLogin(request.getSession()) != AuthService.LoginStatus.PASS)
            return JsonMsg.failed("message.auth.no_login");

        // ===== Call service =====
        User user = (User)request.getSession().getAttribute("user");
        return userService.updateInfo(uid, newName, newPassword, user.getUid(), user.getPrivilege());
    }

    // Update user privilege (Super admin ONLY)
    @PostMapping("/update/privilege")
    public String updatePrivilege(@RequestParam("uid") int uid,
                                  @RequestParam("privilege") short privilege,
                                  HttpServletRequest request) {
        // ===== Validate params =====
        if (uid < 2)
            return JsonMsg.failed("message.invalid.uid");

        if (privilege < 0 || privilege > 1)
            return JsonMsg.failed("message.invalid.privilege");

        // ===== Check privilege =====
        if (authService.checkPrivilege(request.getSession()) != 2)
            return JsonMsg.failed("message.fail.permission");

        // ===== Call service =====
        return userService.updatePrivilege(uid, privilege);
    }

    // Delete user (Admin ONLY)
    @GetMapping("/delete")
    public String delete(@RequestParam("uid") int uid,
                         HttpServletRequest request) {
        // ===== Validate params =====
        if (uid < 2)
            return JsonMsg.failed("message.invalid.uid");

        // ===== Check privilege =====
        if (authService.checkPrivilege(request.getSession()) < 1)
            return JsonMsg.failed("message.fail.permission");

        // ===== Call service =====
        return userService.delete(uid);
    }

    // Get user count (Admin ONLY)
    @GetMapping("/count")
    public String count(HttpServletRequest request) {
        // ===== Check privilege =====
        if (authService.checkPrivilege(request.getSession()) < 1)
            return JsonMsg.failed("message.fail.permission");

        // ===== Call service =====
        return userService.count();
    }

    // Find user (Admin ONLY)
    @GetMapping("/")
    public String get(@RequestParam(value = "uid", required = false) Integer uid,
                      @RequestParam(value = "account", required = false) String account,
                      @RequestParam(value = "p", required = false) Integer page,
                      HttpServletRequest request) {
        // ===== Check privilege =====
        if (authService.checkPrivilege(request.getSession()) < 1)
            return JsonMsg.failed("message.fail.permission");

        // ===== Find by ID =====
        if (uid != null) {
            if (uid < 1)
                return JsonMsg.failed("message.invalid.uid");
            return userService.findById(uid);
        }

        // ===== Find by account =====
        if (account != null) {
            Matcher accountMatcher = accountPattern.matcher(account);
            if (!accountMatcher.matches())
                return JsonMsg.failed("message.invalid.account");
            return userService.findByAccount(account);
        }

        // ===== Find all =====
        if (page != null && page < 1)
            return JsonMsg.failed("message.invalid.page");
        page = (page == null ? 0 : (page - 1) * 20);
        return userService.findAll(page);
    }
    // ===== End of Mappings =====
}
