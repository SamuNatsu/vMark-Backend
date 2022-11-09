package com.vmark.backend.entity;

import lombok.Data;

@Data
public class User {
    private long uid;
    private String account;
    private String username;
    private String password;
}
