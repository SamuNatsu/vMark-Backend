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
    // ===== Logger =====
    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);
    // ===== End of Logger =====


    // ===== Mapper =====
    private final CategoryMapper categoryMapper;
    // ===== End of Mapper =====


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
        if (categoryMapper.add(name, parent) == 1) {
            logger.info("Category added: name'{}', parent={}", name, parent);
            return JsonMsg.success();
        }

        // ===== Fail =====
        logger.warn("Fail to add category: name='{}', parent={}", name, parent);
        return JsonMsg.failed("message.fail.database");
    }

    // Update category info
    public String update(int cid, String name, Integer parent) {
        // ===== Update database =====
        if (categoryMapper.update(cid, name, parent) == 1) {
            logger.info("Category updated: cid={}, name='{}', parent={}", cid, name, parent);
            return JsonMsg.success();
        }

        // ===== Fail =====
        logger.warn("Fail to update category: cid={}, name='{}', parent={}", cid, name, parent);
        return JsonMsg.failed("message.fail.database");
    }

    // Delete category
    public String delete(int cid) {
        // ===== Delete from database =====
        if (categoryMapper.delete(cid) == 1) {
            logger.info("Category deleted: cid={}", cid);
            return JsonMsg.success();
        }

        // ===== Fail =====
        logger.warn("Fail to delete category: cid={}", cid);
        return JsonMsg.failed("message.fail.database");
    }

    // Get all category
    public String findAll() {
        // ===== Select from database =====
        List<Category> result = categoryMapper.findAll();

        return result == null ?
                JsonMsg.failed("message.fail.database") : JsonMsg.success(result);
    }
    // ===== End of Services =====
}
