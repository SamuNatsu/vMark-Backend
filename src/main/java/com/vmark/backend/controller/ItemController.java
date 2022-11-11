package com.vmark.backend.controller;

import com.vmark.backend.service.ItemService;
import com.vmark.backend.utils.JsonMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/item")
public class ItemController {
    // ===== Services =====
    @Autowired
    private ItemService itemService;

    // ===== Http Servlet Request =====
    @Autowired
    private HttpServletRequest httpServletRequest;

    // ===== Mappings =====
    @GetMapping("/info/{siid}")
    public String getItemInfoById(@PathVariable String siid) {
        // Parse iid
        int iid;
        try {
            iid = Integer.parseInt(siid);
        }
        catch (NumberFormatException e) {
            return JsonMsg.failed("item.message.invalid_item");
        }

        // Call service
        return itemService.getItemById(iid);
    }
}
