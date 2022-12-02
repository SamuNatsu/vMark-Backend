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
    // ===== Logger =====
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    // ===== End of Logger =====


    // ===== Mappers =====
    private final UserMapper userMapper;
    // ===== End of Mappers =====


    // ===== Constructor =====
    @Autowired
    public AuthService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    // ===== End of Constructor =====


    // ===== Services =====
    // Generate captcha
    public byte[] genCaptcha(HttpSession session, HttpServletResponse httpServletResponse) {
        // ===== Generate captcha =====
        byte[] image = CaptchaUtil.generate(session);
        if (image == null) {
            logger.warn("Fail to generate captcha on session {}", session.getId());
            httpServletResponse.setStatus(500);
            return null;
        }

        // ===== Return image =====
        logger.info(
                "Successfully generated captcha on session {}: captcha='{}', timestamp={}",
                session.getId(),
                session.getAttribute("captcha"),
                session.getAttribute("captcha_timestamp")
        );
        return image;
    }

    // Login
    public String login(String account, String password, HttpSession session) {
        // ===== Check login =====
        if (checkLogin(session) == LoginStatus.PASS) {
            logger.warn("Already logined: account='{}'", account);
            return JsonMsg.failed("message.auth.already_login");
        }

        // ===== Find user =====
        User user = userMapper.findByAccount(account);
        if (user == null) {
            logger.warn("Fail to login: account='{}'", account);
            return JsonMsg.failed("message.fail.login");
        }

        // ===== Check password =====
        if (user.getPassword().compareTo(password) != 0) {
            logger.warn("Fail to login: account='{}'", account);
            return JsonMsg.failed("message.fail.login");
        }

        // ===== Update session =====
        session.setAttribute("login", true);
        session.setAttribute("login_timestamp", new Date().getTime());
        session.setAttribute("user", user);

        // ===== Return user info =====
        logger.info("Successfully logined: account='{}'", account);
        return JsonMsg.success(user);
    }

    // Logout
    public String logout(HttpSession session) {
        // ===== Update session =====
        session.setAttribute("login", false);

        // ===== Return success =====
        logger.info("Successfully logout on session '{}'", session.getId());
        return JsonMsg.success();
    }

    // Get logined user info
    public String info(HttpSession session) {
        // ===== Check login =====
        if (checkLogin(session) != LoginStatus.PASS) {
            logger.warn("Fail to get session info on session '{}'", session.getId());
            return JsonMsg.failed("message.auth.no_login");
        }

        // ===== Return user info =====
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

    // Check privilege
    public short checkPrivilege(HttpSession session) {
        // No login
        if (checkLogin(session) != LoginStatus.PASS)
            return 0;

        // Logined
        return ((User)session.getAttribute("user")).getPrivilege();
    }
}
