package com.vmark.backend.service;

import com.vmark.backend.entity.User;
import com.vmark.backend.mapper.UserMapper;
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
                return "{" +
                        "\"status\":\"failed\"," +
                        "\"message\":\"Already logined\"" +
                        "}";
        }

        // Find user
        User user = userMapper.findByAccount(account);
        if (user == null)
            return "{" +
                    "\"status\":\"failed\"," +
                    "\"message\":\"login.message.fail\"" +
                    "}";

        // Check password
        if (user.getPassword().compareTo(password) != 0)
            return "{" +
                    "\"status\":\"failed\"," +
                    "\"message\":\"login.message.fail\"" +
                    "}";

        // ===== Success =====
        // Update session
        session.setAttribute("login", true);
        session.setAttribute("uid", user.getUid());
        session.setAttribute("account", user.getAccount());
        session.setAttribute("username", user.getUsername());
        session.setAttribute("timestamp", new Date().getTime());
        // Return user info
        return "{" +
                "\"status\":\"success\"," +
                "\"data\":{" +
                "\"uid\":" + user.getUid() + "," +
                "\"account\":" + user.getAccount() + "," +
                "\"username\":" + user.getUsername() + "," +
                "}" +
                "}";
    }

    public String logout(HttpSession session) {
        session.setAttribute("login", false);
        return "{\"status\":\"success\"}";
    }

    public String info(HttpSession session) {
        // If no login
        if (session.getAttribute("login") == null ||
                !(boolean)session.getAttribute("login"))
            return "{" +
                    "\"status\":\"failed\"," +
                    "\"message\":\"Please login first\"" +
                    "}";

        // If timeout
        long timestamp = (long)session.getAttribute("timestamp");
        if (new Date().getTime() - timestamp > 86400L) {
            session.setAttribute("login", false);
            return "{" +
                    "\"status\":\"failed\"," +
                    "\"message\":\"Please login first\"" +
                    "}";
        }

        // Return user info
        return "{" +
                "\"status\":\"success\"," +
                "\"data\":{" +
                "\"uid\":" + session.getAttribute("uid") + "," +
                "\"account\":" + session.getAttribute("account") + "," +
                "\"username\":" + session.getAttribute("username") + "," +
                "}" +
                "}";
    }
}
