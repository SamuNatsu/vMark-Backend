package com.vmark.backend.controller;

import com.vmark.backend.service.AttachmentService;
import com.vmark.backend.service.AuthService;
import com.vmark.backend.utils.JsonMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/attachment")
public class AttachmentController {
    // ===== External Autowired =====
    private final AttachmentService attachmentService;
    private final AuthService authService;
    // ===== End of External Autowired =====


    // ===== Validators =====
    private static final Pattern nameValidator = Pattern.compile("^[\\w_]+$");
    // ===== End of Validators =====


    // ===== Constructor =====
    @Autowired
    public AttachmentController(AttachmentService attachmentService,
                                AuthService authService) {
        this.attachmentService = attachmentService;
        this.authService = authService;
    }
    // ===== End of Construtor =====


    // ===== Mappings =====
    // Upload
    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file,
                         HttpServletRequest request) {
        // ===== Check privilege =====
        if (authService.checkPrivilege(request.getSession()) < 1)
            return JsonMsg.failed("message.fail.permission");

        // ===== Convert file name =====
        String name = file.getOriginalFilename();
        if (name == null)
            name = "attachment";
        name = name.substring(0, name.indexOf('.')).replaceAll("[^\\w_]+", "");

        // ===== Store file =====
        String path = attachmentService.store(file);
        if (path == null)
            return JsonMsg.failed("message.fail.upload");

        // ===== Call service =====
        return attachmentService.add(name, path);
    }

    // Rename
    @GetMapping("/rename")
    public String rename(@RequestParam("aid") int aid,
                         @RequestParam("new_name") String newName,
                         HttpServletRequest request) {
        // ===== Validate params =====
        if (aid < 1)
            return JsonMsg.failed("message.invalid.aid");

        Matcher nameMatcher = nameValidator.matcher(newName);
        if (!nameMatcher.matches())
            return JsonMsg.failed("message.invalid.new_name");

        // ===== Check privilege =====
        if (authService.checkPrivilege(request.getSession()) < 1)
            return JsonMsg.failed("message.fail.permission");

        // ===== Call service =====
        return attachmentService.rename(aid, newName);
    }

    // Delete
    @GetMapping("/delete")
    public String delete(@RequestParam("aid") int aid,
                         HttpServletRequest request) {
        // ===== Validate params =====
        if (aid < 1)
            return JsonMsg.failed("message.invalid.aid");

        // ===== Check privilege =====
        if (authService.checkPrivilege(request.getSession()) < 1)
            return JsonMsg.failed("message.fail.permission");

        // ===== Call service =====
        return attachmentService.delete(aid);
    }

    // Get by ID
    @GetMapping(value = "/get", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getById(@RequestParam("aid") int aid,
                          HttpServletResponse response) {
        // ===== Validate params =====
        if (aid < 1) {
            response.setStatus(404);
            return null;
        }

        // ===== Call service =====
        return attachmentService.loadById(aid, response);
    }

    // Get info
    @GetMapping("/")
    public String getInfo(@RequestParam(value = "aid", required = false) Integer aid,
                          @RequestParam(value = "s", required = false) String name,
                          @RequestParam(value = "p", required = false) Integer page,
                          @RequestParam(value = "on", required = false) String orderName,
                          @RequestParam(value = "ot", required = false) String orderType,
                          HttpServletRequest request) {
        // ===== Check privilege =====
        if (authService.checkPrivilege(request.getSession()) < 1)
            return JsonMsg.failed("message.fail.permission");

        // ===== If aid given =====
        if (aid != null) {
            if (aid < 1)
                return JsonMsg.failed("message.invalid.aid");
            return attachmentService.findById(aid);
        }

        // ===== Generate options =====
        HashMap<String, Object> options = new HashMap<>();

        if (name != null)
            options.put("name", name);

        if (page != null) {
            if (page < 1)
                return JsonMsg.failed("message.invalid.page");
            options.put("page", (page - 1) * 20);
        }

        if (orderName != null) {
            if (orderName.compareTo("name") != 0 && orderName.compareTo("timestamp") != 0)
                return JsonMsg.failed("message.invalid.order_name");
            if (orderType == null)
                orderType = "asc";
            else if (orderType.compareTo("asc") != 0 && orderType.compareTo("desc") != 0)
                return JsonMsg.failed("message.invalid.order_type");
            options.put("order_name", orderName);
            options.put("order_type", orderType);
        }

        // ===== Call service =====
        return attachmentService.findByOptions(options);
    }
    // ===== End of Mappings =====
}
