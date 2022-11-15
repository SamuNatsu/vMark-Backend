package com.vmark.backend.mapper;

import com.vmark.backend.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    int add(String account, String password, short privilege);

    int updateInfo(int uid, String name, String password, int opUid, short opPrivilege);
    int updatePriviledge(int uid, short privilege);

    int deleteById(int uid);

    User findById(int uid);
    User findByAccount(String account);
    List<User> findLimit(int offset, int rows);
}
