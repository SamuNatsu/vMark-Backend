package com.vmark.backend.controller;

import com.vmark.backend.service.ItemService;
import com.vmark.backend.utils.JsonMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/api/item")
public class ItemController {
    // ===== Services =====
    @Autowired
    private ItemService itemService;

    // ===== Http Servlet Request =====
    @Autowired
    private HttpServletRequest httpServletRequest;

    // ===== Mappings =====
    // Get item info by id
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

    // Get item picture by ipid
    @GetMapping(value = "/pic/{sipid}", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getItemPicByIpid(@PathVariable String sipid) {
        // Parse ipid
        int ipid;
        try {
            ipid = Integer.parseInt(sipid);
        }
        catch (NumberFormatException e) {
            return null;
        }

        // Call service
        return itemService.getItemPicByIpid(ipid);
    }

    // Upload item picture
    @PostMapping("/upload_pic")
    public String uploadItemPic(@RequestParam("file")MultipartFile file) {
        // Check empty
        if (file.isEmpty())
            return JsonMsg.failed("message.item.upload_pic.empty");

        // Check content type (PNG only)
        String type = file.getContentType();
        if (type == null || type.compareTo("image/png") != 0)
            return JsonMsg.failed("message.item.upload_pic.invalid_type");

        // Call service
        try {
            return itemService.storeItmePic(1, file.getBytes());
        }
        catch (IOException e) {
            return JsonMsg.failed("message.item.upload_pic.io_fail");
        }
    }
}
