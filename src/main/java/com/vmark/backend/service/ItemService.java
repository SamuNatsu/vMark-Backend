package com.vmark.backend.service;

import com.vmark.backend.entity.Item;
import com.vmark.backend.mapper.ItemMapper;
import com.vmark.backend.utils.JsonMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class ItemService {
    // ===== Logger =====
    private static final Logger logger = LoggerFactory.getLogger(ItemService.class);
    // ===== End of Logger =====


    // ===== Mappers =====
    private final ItemMapper itemMapper;
    // ===== End of Mappers =====


    // ===== Constructor =====
    @Autowired
    public ItemService(ItemMapper itemMapper) {
        this.itemMapper = itemMapper;
    }
    // ===== End of Constructor =====


    // ===== Services =====
    // Add item
    public String add(String name, int cid, int price) {
        // ===== Insert into database =====
        int result = itemMapper.add(name, cid, price);

        // ===== Fail =====
        if (result != 1) {
            logger.warn("Fail to add item: name='{}', cid={}, price={}", name, cid, price);
            return JsonMsg.failed("message.fail.database");
        }

        // ===== Success =====
        return JsonMsg.success();
    }

    // Update item info
    public String update(int iid,
                         String name,
                         Integer cid,
                         Integer price,
                         Integer remain,
                         Integer aid,
                         Integer sale,
                         String description) {
        // ===== Update item =====
        int result = itemMapper.update(iid, name, cid, price, remain, aid, sale, description);

        // ===== Fail =====
        if (result != 1) {
            logger.warn("Fail to update item: iid={}", iid);
            return JsonMsg.failed("message.fail.database");
        }

        // ===== Success =====
        return JsonMsg.success();
    }

    // Delete item
    public String delete(int iid) {
        // ===== Delete from database =====
        int result = itemMapper.delete(iid);

        // ===== Fail =====
        if (result != 1) {
            logger.warn("Fail to delete item: iid={}", iid);
            return JsonMsg.failed("message.fail.database");
        }

        // ===== Success =====
        return JsonMsg.success();
    }

    // Count item
    public String count() {
        return JsonMsg.success(itemMapper.count());
    }

    // Find item by ID
    public String findById(int iid) {
        // ===== Find in database =====
        Item item = itemMapper.findById(iid);
        if (item == null)
            return JsonMsg.failed("message.not_found.item.id");

        // ===== Return =====
        return JsonMsg.success(item);
    }

    // Find all
    public String findAll(HashMap<String, Object> options) {
        // ===== Find in database =====
        List<Item> items = itemMapper.findAll(options);
        if (items == null)
            return JsonMsg.failed("message.fail.database");

        // ===== Return =====
        return JsonMsg.success(items);
    }
    // ===== End of Services =====
}
