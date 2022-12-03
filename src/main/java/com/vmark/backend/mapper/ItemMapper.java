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
               Integer cid,
               Integer price,
               Integer remain,
               Integer aid,
               Integer sale,
               String description);

    int delete(int iid);

    int count();

    Item findById(int iid);
    List<Item> findAll(HashMap<String, Object> options);
}
