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
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    // ===== End of Log =====


    // ===== MyBatis Mappers =====
    @Autowired
    private UserMapper userMapper;
    // ===== End of MyBatis Mappers =====


    // ===== Services =====
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
        if (userMapper.deleteById(uid) == 1) {
            logger.info("User deleted: uid={}", uid);
            return JsonMsg.success();
        }
        else {
            logger.warn("Fail to delete user: uid={}", uid);
            return JsonMsg.failed("message.user.delete.fail");
        }
    }

    // Find user
    public String findById(int uid) {
        // Find in database
        User user = userMapper.findById(uid);

        // Return
        return user == null ? JsonMsg.failed("message.user.find.id.fail") : JsonMsg.success(user);
    }
    public String findByAccount(String account) {
        // Find in database
        User user = userMapper.findByAccount(account);

        // Return
        return user == null ? JsonMsg.failed("message.user.find.account.fail") : JsonMsg.success(user);
    }
    public String findLimit(int offset, int rows) {
        // Find in database
        List<User> user = userMapper.findLimit(offset, rows);

        // Return
        return user == null ? JsonMsg.failed("message.user.find.limit.fail") : JsonMsg.success(user);
    }
}
