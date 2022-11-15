package com.vmark.backend.service;

import com.vmark.backend.entity.User;
import com.vmark.backend.mapper.UserMapper;
import com.vmark.backend.utils.CaptchaUtil;
import com.vmark.backend.utils.JsonMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Service
public class AuthService {
    // ===== Log =====
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    // ===== End of Log =====


    // ===== MyBatis Mappers =====
    @Autowired
    private UserMapper userMapper;
    // ===== End of MyBatis Mappers =====


    // ===== Services =====
    // Generate captcha
    public byte[] genCaptcha(HttpSession session, HttpServletResponse response) {
        // Generate captcha
        byte[] image = CaptchaUtil.generate(session);
        if (image == null) {
            logger.warn("Fail to generate captcha on session {}", session.getId());
            response.setStatus(500);
            return null;
        }
        logger.info(
                "Successfully generated captcha on session {}: captcha='{}', timestamp={}",
                session.getId(),
                session.getAttribute("captcha"),
                session.getAttribute("captcha_timestamp")
        );

        // Return picture
        return image;
    }

    // Register
    public String register(String account, String password) {
        // Update database
        if (userMapper.add(account, password, (short)0) == 1) {
            logger.info("Successfully registered: account='{}'", account);
            return JsonMsg.success();
        }

        // Account duplicated
        logger.warn("Dumplicated register: account='{}'", account);
        return JsonMsg.failed("message.auth.register.account_duplicated");
    }

    // Login
    public String login(String account, String password, HttpSession session) {
        // Check login
        LoginStatus status = checkLogin(session);
        if (status == LoginStatus.PASS) {
            logger.warn("Already logined: account='{}'", account);
            return JsonMsg.failed("message.auth.login.already");
        }

        // Find user
        User user = userMapper.findByAccount(account);
        if (user == null) {
            logger.warn("Fail to login: account='{}'", account);
            return JsonMsg.failed("message.auth.login.fail");
        }

        // Check password
        if (user.getPassword().compareTo(password) != 0) {
            logger.warn("Fail to login: account='{}'", account);
            return JsonMsg.failed("message.auth.login.fail");
        }

        // ===== Success =====
        logger.info("Successfully logined: account='{}'", account);
        // Update session
        session.setAttribute("login", true);
        session.setAttribute("login_timestamp", new Date().getTime());
        session.setAttribute("user", user);
        // Return user info
        return JsonMsg.success(user);
    }

    // Logout
    public String logout(HttpSession session) {
        logger.info("Successfully logout on session '{}'", session.getId());
        session.setAttribute("login", false);
        return JsonMsg.success();
    }

    // Get logined user info
    public String info(HttpSession session) {
        // Check login
        LoginStatus status = checkLogin(session);
        if (status != LoginStatus.PASS) {
            logger.warn("Fail to get session info on session '{}'", session.getId());
            return JsonMsg.failed("message.auth.info.pls_login");
        }

        // Return user info
        logger.info("Successfully get session info on session '{}'", session.getId());
        return JsonMsg.success(session.getAttribute("user"));
    }

    // Login status
    public enum LoginStatus {
        PASS,
        REJECT,
        TIMEOUT
    }

    // Check login
    public LoginStatus checkLogin(HttpSession session) {
        Boolean login = (Boolean)session.getAttribute("login");
        Long timestamp = (Long)session.getAttribute("login_timestamp");

        // No login
        if (login == null || !login)
            return LoginStatus.REJECT;

        // Timeout
        if (new Date().getTime() - timestamp > 86400000L)
            return LoginStatus.TIMEOUT;

        // Pass
        return LoginStatus.PASS;
    }
}
