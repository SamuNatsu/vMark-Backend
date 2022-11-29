package com.vmark.backend.service;

import com.vmark.backend.entity.Category;
import com.vmark.backend.mapper.CategoryMapper;
import com.vmark.backend.utils.JsonMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {
    // ===== Log =====
    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);
    // ===== End of Log =====


    // ===== External Autowired =====
    private final CategoryMapper categoryMapper;
    // ===== End od External Autowired =====


    // ===== Constructor =====
    @Autowired
    public CategoryService(CategoryMapper categoryMapper) {
        this.categoryMapper = categoryMapper;
    }
    // ===== End of Constructor =====


    // ===== Services =====
    // Add category
    public String add(String name, Integer parent) {
        // ===== Insert into database =====
        int result = categoryMapper.add(name, parent);

        // ===== Fail =====
        if (result != 1) {
            logger.warn("Fail to add category: name='{}', parent={}", name, parent);
            return JsonMsg.failed("message.fail.database");
        }

        // ===== Success =====
        return JsonMsg.success();
    }

    // Update category info
    public String update(int cid, String name, Integer parent) {
        // ===== Update database =====
        int result = categoryMapper.update(cid, name, parent);

        // ===== Fail =====
        if (result != 1) {
            logger.warn("Fail to update category: cid={}, name='{}', parent={}", cid, name, parent);
            return JsonMsg.failed("message.fail.database");
        }

        // ===== Success =====
        return JsonMsg.success();
    }

    // Delete category
    public String delete(int cid) {
        // ===== Delete from database =====
        int result = categoryMapper.delete(cid);

        // ===== Fail =====
        if (result != 1) {
            logger.warn("Fail to delete category: cid={}", cid);
            return JsonMsg.failed("message.fail.database");
        }

        // ===== Success =====
        return JsonMsg.success();
    }

    // Get all category
    public String findAll() {
        // ===== Select from database =====
        List<Category> result = categoryMapper.findAll();

        // ===== Fail =====
        if (result == null) {
            logger.warn("Fail to find all categories");
            return JsonMsg.failed("message.fail.database");
        }

        // ===== Success =====
        return JsonMsg.success(result);
    }
    // ===== End of Services =====
}
