package com.vmark.backend.mapper;

import com.vmark.backend.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    int add(String account, String password);

    int updateInfo(int uid, String name, String password, int opUid, short opPrivilege);
    int updatePrivilege(int uid, short privilege);

    int delete(int uid);

    int count();

    User findById(int uid);
    User findByAccount(String account);
    List<User> findAll(String keyword, int offset);
}
