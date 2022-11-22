package com.vmark.backend.controller;

import com.vmark.backend.service.ItemService;
import com.vmark.backend.utils.JsonMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/item")
public class ItemController {
    // ===== Services =====
    @Autowired
    private ItemService itemService;
    // ===== End of Services =====


    // ===== Mappings =====
    // Get item info by id
    @GetMapping("/info/{siid}")
    public String getInfoById(@PathVariable String siid) {
        // ===== Parse iid =====
        int iid;
        try {
            iid = Integer.parseInt(siid);
        }
        catch (NumberFormatException e) {
            return JsonMsg.failed("message.invalid.iid");
        }

        // ===== Validate params =====
        if (iid < 1)
            return JsonMsg.failed("message.invalid.iid");

        // ===== Call service =====
        return itemService.findById(iid);
    }
    // ===== End of Mappings =====
}
