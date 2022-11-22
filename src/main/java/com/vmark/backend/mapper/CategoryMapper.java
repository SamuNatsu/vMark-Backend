package com.vmark.backend.mapper;

import com.vmark.backend.entity.Category;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryMapper {
    int add(String name, Integer parent);

    int update(int cid, String name, Integer parent);

    int delete(int cid);

    List<Category> findAll();
}
