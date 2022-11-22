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
    // ===== Log =====
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    // ===== End of Log =====


    // ===== External Autowired =====
    private final UserMapper userMapper;
    // ===== End of External Autowired =====


    // ===== Constructor =====
    @Autowired
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    // ===== End of Constructor =====


    // ===== Services =====
    // Register
    public String register(String account, String password) {
        // ===== Update database =====
        if (userMapper.add(account, password) == 1) {
            logger.info("Successfully registered: account='{}'", account);
            return JsonMsg.success();
        }

        // ===== Account duplicated =====
        logger.warn("Dumplicated register: account='{}'", account);
        return JsonMsg.failed("message.fail.account_duplicated");
    }

    // Update info
    public String updateInfo(int uid, String name, String password, int opUid, short opPrivilege) {
        // ===== Success =====
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
        // ===== Success =====
        if (userMapper.updatePriviledge(uid, privilege) == 1) {
            logger.info("Change user privilege: uid={}, privilege={}", uid, privilege);
            return JsonMsg.success();
        }

        // ===== Fail =====
        logger.warn("Fail to change user privilege: uid={}", uid);
        return JsonMsg.failed("message.fail.permission");
    }

    // Delete user
    public String delete(int uid) {
        // ===== Success =====
        if (userMapper.delete(uid) == 1) {
            logger.info("User deleted: uid={}", uid);
            return JsonMsg.success();
        }

        // ===== Fail =====
        logger.warn("Fail to delete user: uid={}", uid);
        return JsonMsg.failed("message.fail.delete_user");
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
    public String findAll(Integer offset) {
        // ===== Find in database =====
        List<User> user = userMapper.findAll(offset);

        // ===== Return =====
        return user == null ?
                JsonMsg.failed("message.not_found.user.all") : JsonMsg.success(user);
    }
    // ===== End of Services =====
}
