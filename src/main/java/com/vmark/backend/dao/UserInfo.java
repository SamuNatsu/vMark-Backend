package com.vmark.backend.dao;

import lombok.Data;

@Data
public class UserInfo {
    private long uid;
    private String account;
    private String username;
    private String password;

    public boolean auth(String pass) {
        return password.compareTo(pass) == 0;
    }
}
