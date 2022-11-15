package com.vmark.backend.service;

import com.vmark.backend.entity.Item;
import com.vmark.backend.entity.ItemPic;
import com.vmark.backend.mapper.ItemMapper;
import com.vmark.backend.mapper.ItemPicMapper;
import com.vmark.backend.utils.JsonMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService {
    // ===== Log =====
    private static final Logger logger = LoggerFactory.getLogger(ItemService.class);
    // ===== End of Log =====


    // ===== Mappers =====
    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ItemPicMapper itemPicMapper;
    // ===== End of Mappers =====


    public String getItemById(int iid) {
        // Find in database
        Item item = itemMapper.findById(iid);
        if (item == null)
            return JsonMsg.failed("item.message.not_exists");

        // Return
        return JsonMsg.success(item);
    }

    public byte[] getItemPicByIpid(int ipid) {
        logger.info("Request item picture: ipid={}", ipid);

        // Find in database
        ItemPic itemPic = itemPicMapper.findByIpid(ipid);
        if (itemPic == null) {
            logger.warn("Item picture not found: ipid={}", ipid);
            return null;
        }

        // Return
        return itemPic.getData();
    }

    public String storeItmePic(int iid, byte[] data) {
        logger.info("Upload item picture: iid={}", iid);

        // Create object
        ItemPic itemPic = new ItemPic();
        itemPic.setIid(iid);
        itemPic.setData(data);

        // Insert database
        if (itemPicMapper.add(itemPic) == 1)
            return JsonMsg.success();
        else
            return JsonMsg.failed("message.item.upload_pic.db_fail");
    }
}
