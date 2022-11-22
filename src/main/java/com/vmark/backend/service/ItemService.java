package com.vmark.backend.service;

import com.vmark.backend.entity.Item;
import com.vmark.backend.mapper.ItemMapper;
import com.vmark.backend.utils.JsonMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    // ===== Log =====
    private static final Logger logger = LoggerFactory.getLogger(ItemService.class);
    // ===== End of Log =====


    // ===== Mappers =====
    @Autowired
    private ItemMapper itemMapper;
    // ===== End of Mappers =====


    // ===== Services =====
    // Find item by ID
    public String findById(int iid) {
        // ===== Find in database =====
        Item item = itemMapper.findById(iid);
        if (item == null)
            return JsonMsg.failed("message.not_found.item.id");

        // ===== Return =====
        return JsonMsg.success(item);
    }


}
