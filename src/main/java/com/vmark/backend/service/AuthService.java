package com.vmark.backend.service;

import com.vmark.backend.entity.User;
import com.vmark.backend.mapper.UserMapper;
import com.vmark.backend.utils.JsonMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Service
public class AuthService {
    @Autowired
    private UserMapper userMapper;

    public String login(String account, String password, HttpSession session) {
        // ===== Check session =====
        // If login
        if (session.getAttribute("login") != null &&
                (boolean)session.getAttribute("login")) {
            long timestamp = (long)session.getAttribute("timestamp");

            // Check timeout (1 day)
            if (new Date().getTime() - timestamp > 86400L)
                session.setAttribute("login", false);
            else
                return JsonMsg.failed("Already logined");
        }

        // Find user
        User user = userMapper.findByAccount(account);
        if (user == null)
            return JsonMsg.failed("login.message.fail");

        // Check password
        if (user.getPassword().compareTo(password) != 0)
            return JsonMsg.failed("login.message.fail");

        // ===== Success =====
        // Update session
        session.setAttribute("login", true);
        session.setAttribute("timestamp", new Date().getTime());
        session.setAttribute("user", user);
        // Return user info
        return JsonMsg.success(user);
    }

    public String logout(HttpSession session) {
        session.setAttribute("login", false);
        return JsonMsg.success();
    }

    public String info(HttpSession session) {
        // If no login
        if (session.getAttribute("login") == null ||
                !(boolean)session.getAttribute("login"))
            return JsonMsg.failed("Please login first");

        // If timeout
        long timestamp = (long)session.getAttribute("timestamp");
        if (new Date().getTime() - timestamp > 86400L) {
            session.setAttribute("login", false);
            return JsonMsg.failed("Please login first");
        }

        // Return user info
        return JsonMsg.success(session.getAttribute("user"));
    }
}
