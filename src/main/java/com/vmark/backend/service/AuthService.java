package com.vmark.backend.service;

import com.vmark.backend.entity.User;
import com.vmark.backend.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {
    @Autowired
    private UserMapper userMapper;

    public String login(String account, String password) {
        return "";
    }

}
