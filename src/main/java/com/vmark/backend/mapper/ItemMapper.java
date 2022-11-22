package com.vmark.backend.mapper;

import com.vmark.backend.entity.Item;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface ItemMapper {
    int add(String name, int cid, int price);

    int update(String name, Integer aid, float price, Float sale, String description, int remain);

    int deleteById(int iid);

    Item findById(int iid);
    List<Item> findByKeyword(String keyword, int offset, int rows);
    List<Item> findLimit(int offset, int rows);

    List<Item> searchAll(HashMap<String, Object> options);
}
