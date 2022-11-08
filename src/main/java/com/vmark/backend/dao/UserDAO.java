package com.vmark.backend.dao;

import java.util.List;

public interface UserDAO {
    public int add(UserInfo user);
    public int update(UserInfo user);
    public int deleteById(long uid);
    public UserInfo findById(long uid);
    public List<UserInfo> findAll();
}
