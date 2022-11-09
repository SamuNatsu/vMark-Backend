package com.vmark.backend.mapper;

import com.vmark.backend.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    public int add(User user);
    public int update(User user);
    public int deleteById(long uid);
    public User findById(long uid);
    public List<User> findAll();
}
