package com.vmark.backend.mapper;

import com.vmark.backend.entity.Item;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface ItemMapper {
    int add(String name, int cid, int price);

    int update(int iid,
               String name,
               int cid,
               int price,
               int remain,
               Integer aid,
               Integer sale,
               String description);

    int delete(int iid);

    Item findById(int iid);
    List<Item> searchAll(HashMap<String, Object> options);
}
