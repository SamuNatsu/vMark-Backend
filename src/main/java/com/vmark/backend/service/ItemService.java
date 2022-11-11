package com.vmark.backend.service;

import com.vmark.backend.entity.Item;
import com.vmark.backend.mapper.ItemMapper;
import com.vmark.backend.utils.JsonMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService {
    @Autowired
    private ItemMapper itemMapper;

    public String getItemById(int iid) {
        // Find in database
        Item item = itemMapper.findById(iid);
        if (item == null)
            return JsonMsg.failed("item.message.not_exists");

        // Return
        return JsonMsg.success(item);
    }
}
