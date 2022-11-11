package com.vmark.backend.mapper;

import com.vmark.backend.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    int add(User user);

    int update(User user);

    int deleteById(int uid);

    User findById(int uid);
    User findByAccount(String account);
    List<User> findAll();
}
