package com.vmark.backend.controller;

import com.vmark.backend.entity.User;
import com.vmark.backend.service.AuthService;
import com.vmark.backend.service.UserService;
import com.vmark.backend.utils.CaptchaUtil;
import com.vmark.backend.utils.JsonMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/user")
public class UserController {
    // ===== Services =====
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;
    // ===== End of Services =====

    // ===== Validators =====
    // Password validator (SHA 256 string, lowercase)
    private static final Pattern passwordRegex = Pattern.compile("^[0-9a-z]{64}$");

    // Captcha validator (length = 4, composed of alphas and digits)
    private static final Pattern captchaRegex = Pattern.compile("^[0-9A-Z]{4}$");
    // ===== End of Validators =====


    // ===== Http Servlet =====
    @Autowired
    private HttpServletRequest httpServletRequest;
    // ===== End of Http Servlet =====


    // ===== Mappings =====
    // Update user info
    @PostMapping("/update_info")
    public String updateInfo(@RequestParam("uid") String suid,
                             @RequestParam(value = "new_name", required = false) String newName,
                             @RequestParam(value = "new_password", required = false) String newPassword,
                             @RequestParam("password") String password,
                             @RequestParam("captcha") String captcha) {
        // ===== Parse uid =====
        int uid;
        try {
            uid = Integer.parseInt(suid);
        }
        catch (NumberFormatException e) {
            return JsonMsg.failed("message.user.update_info.invalid_uid");
        }

        // ===== Validate params =====
        if (uid < 1)
            return JsonMsg.failed("message.invalid.uid");

        if (newName != null) {
            newName = newName.trim();
            if (newName.isEmpty() || newName.length() > 30)
                return JsonMsg.failed("message.invalid.new_name");
        }

        if (newPassword != null) {
            Matcher passwordMatcher = passwordRegex.matcher(newPassword);
            if (!passwordMatcher.matches())
                return JsonMsg.failed("message.invalid.new_password");
        }

        Matcher passwordMatcher = passwordRegex.matcher(password);
        if (!passwordMatcher.matches())
            return JsonMsg.failed("message.invalid.password");

        Matcher captchaMatcher = captchaRegex.matcher(captcha);
        if (!captchaMatcher.matches())
            return JsonMsg.failed("message.invalid.captcha");

        // ===== Check captcha =====
        HttpSession session = httpServletRequest.getSession();
        CaptchaUtil.CheckStatus checkStatus = CaptchaUtil.check(captcha, session);
        if (checkStatus == CaptchaUtil.CheckStatus.WRONG)
            return JsonMsg.failed("message.wrong.captcha");
        if (checkStatus == CaptchaUtil.CheckStatus.TIMEOUT)
            return JsonMsg.failed("message.timeout.captcha");

        // ===== Check login =====
        if (authService.checkLogin(session) != AuthService.LoginStatus.PASS)
            return JsonMsg.failed("message.auth.no_login");

        // ===== Check password =====
        User user = (User)session.getAttribute("user");
        if (password.compareTo(user.getPassword()) != 0)
            return JsonMsg.failed("message.wrong.password");

        // ===== Call service =====
        return userService.updateInfo(uid, newName, newPassword, user.getUid(), user.getPrivilege());
    }

    // Update user privilege (Super admin ONLY)
    @PostMapping("/update_privilege")
    public String updatePrivilege(@RequestParam("uid") String suid,
                                  @RequestParam("privilege") String sprivilege,
                                  @RequestParam("password") String password,
                                  @RequestParam("captcha") String captcha) {
        // ===== Parse uid =====
        int uid;
        try {
            uid = Integer.parseInt(suid);
        }
        catch (NumberFormatException e) {
            return JsonMsg.failed("message.invalid.uid");
        }

        // ===== Parse privilege =====
        short privilege;
        try {
            privilege = Short.parseShort(sprivilege);
        }
        catch (NumberFormatException e) {
            return JsonMsg.failed("message.invalid.privilege");
        }

        // ===== Validate params =====
        if (uid < 2)
            return JsonMsg.failed("message.invalid.uid");

        if (privilege < 0 || privilege > 1)
            return JsonMsg.failed("message.invalid.privilege");

        Matcher passwordMatcher = passwordRegex.matcher(password);
        if (!passwordMatcher.matches())
            return JsonMsg.failed("message.invalid.password");

        Matcher captchaMatcher = captchaRegex.matcher(captcha);
        if (!captchaMatcher.matches())
            return JsonMsg.failed("message.invalid.captcha");

        // ===== Check captcha =====
        HttpSession session = httpServletRequest.getSession();
        CaptchaUtil.CheckStatus checkStatus = CaptchaUtil.check(captcha, session);
        if (checkStatus == CaptchaUtil.CheckStatus.WRONG)
            return JsonMsg.failed("message.wrong.captcha");
        if (checkStatus == CaptchaUtil.CheckStatus.TIMEOUT)
            return JsonMsg.failed("message.timeout.captcha");

        // ===== Check login =====
        if (authService.checkLogin(session) != AuthService.LoginStatus.PASS)
            return JsonMsg.failed("message.auth.no_login");

        // ===== Check password =====
        User op = (User)httpServletRequest.getSession().getAttribute("user");
        if (password.compareTo(op.getPassword()) != 0)
            return JsonMsg.failed("message.wrong.password");

        // ===== Check privilege =====
        if (op.getPrivilege() != 2)
            return JsonMsg.failed("message.fail.permission");

        // ===== Call service =====
        return userService.updatePrivilege(uid, privilege);
    }
    // ===== End of Mappings =====
}
