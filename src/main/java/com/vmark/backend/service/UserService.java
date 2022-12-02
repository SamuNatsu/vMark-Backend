package com.vmark.backend.service;

import com.vmark.backend.entity.User;
import com.vmark.backend.mapper.UserMapper;
import com.vmark.backend.utils.JsonMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    // ===== Logger =====
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    // ===== End of Logger =====


    // ===== Mapper =====
    private final UserMapper userMapper;
    // ===== End of Mapper =====


    // ===== Constructor =====
    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    // ===== End of Constructor =====


    // ===== Services =====
    // Register
    public String register(String account, String password) {
        // ===== Intert into database =====
        if (userMapper.add(account, password) == 1) {
            logger.info("Successfully registered: account='{}'", account);
            return JsonMsg.success();
        }

        // ===== Fail =====
        logger.warn("Dumplicated register: account='{}'", account);
        return JsonMsg.failed("message.fail.account_duplicated");
    }

    // Update info
    public String updateInfo(int uid, String name, String password, int opUid, short opPrivilege) {
        // ===== Update database =====
        if (userMapper.updateInfo(uid, name, password, opUid, opPrivilege) == 1) {
            logger.info("Update user info: uid={}", uid);
            return JsonMsg.success();
        }

        // ===== Fail =====
        logger.warn("Fail to update user info: uid={}", uid);
        return JsonMsg.failed("message.fail.permission");
    }

    // Update privilege
    public String updatePrivilege(int uid, short privilege) {
        // ===== Update database =====
        if (userMapper.updatePrivilege(uid, privilege) == 1) {
            logger.info("Update user privilege: uid={}, privilege={}", uid, privilege);
            return JsonMsg.success();
        }

        // ===== Fail =====
        logger.warn("Fail to update user privilege: uid={}", uid);
        return JsonMsg.failed("message.fail.permission");
    }

    // Delete user
    public String delete(int uid) {
        // ===== Delete from database =====
        if (userMapper.delete(uid) == 1) {
            logger.info("User deleted: uid={}", uid);
            return JsonMsg.success();
        }

        // ===== Fail =====
        logger.warn("Fail to delete user: uid={}", uid);
        return JsonMsg.failed("message.fail.delete_user");
    }

    // Count users
    public String count() {
        return JsonMsg.success(userMapper.count());
    }

    // Find user
    public String findById(int uid) {
        // ===== Find in database =====
        User user = userMapper.findById(uid);

        // ===== Return =====
        return user == null ?
                JsonMsg.failed("message.not_found.user.id") : JsonMsg.success(user);
    }
    public String findByAccount(String account) {
        // ===== Find in database =====
        User user = userMapper.findByAccount(account);

        // ===== Return =====
        return user == null ?
                JsonMsg.failed("message.not_found.user.account") : JsonMsg.success(user);
    }
    public String findAll(int offset) {
        // ===== Find in database =====
        List<User> user = userMapper.findAll(offset);

        // ===== Return =====
        return user == null ?
                JsonMsg.failed("message.fail.database") : JsonMsg.success(user);
    }
    // ===== End of Services =====
}
