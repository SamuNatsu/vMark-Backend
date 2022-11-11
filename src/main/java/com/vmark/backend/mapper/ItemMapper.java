package com.vmark.backend.mapper;

import com.vmark.backend.entity.Item;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemMapper {
    int add(Item item);

    int update(Item item);

    int deleteById(int iid);

    Item findById(int iid);
    Item findByKeyword(String keyword);
    List<Item> findAll();
}
