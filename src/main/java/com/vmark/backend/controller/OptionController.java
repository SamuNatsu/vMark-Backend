package com.vmark.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmark.backend.service.AuthService;
import com.vmark.backend.service.OptionService;
import com.vmark.backend.utils.JsonMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;

@CrossOrigin
@RestController
@RequestMapping("/api/option")
public class OptionController {
    // ===== Logger =====
    private final Logger logger = LoggerFactory.getLogger(OptionController.class);
    // ===== End of Logger =====

    // ===== External Autowired =====
    private final AuthService authService;
    private final OptionService optionService;
    // ===== End of External Autowired =====


    // ===== Contructor =====
    @Autowired
    public OptionController(AuthService authService,
                            OptionService optionService) {
        this.authService = authService;
        this.optionService = optionService;
    }
    // ===== End of Constructor =====


    // ===== Mappings =====
    // Update skin (Admin ONLY)
    @PostMapping("/update/skin")
    @SuppressWarnings("unchecked")
    public String updateSkin(@RequestParam("skin") String skin,
                             HttpServletRequest request) {
        // ===== Parse skin =====
        HashMap<String, String> options;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            options = (HashMap<String, String>)objectMapper.readValue(skin, HashMap.class);
        }
        catch (Exception e) {
            return JsonMsg.failed("message.invalid.order");
        }

        // ===== Check privilege =====
        if (authService.checkPrivilege(request.getSession()) < 1)
            return JsonMsg.failed("message.fail.permission");

        // ===== Call service =====
        return optionService.update("skin", options);
    }

    // Get skin (Everyone)
    @GetMapping("/get/skin")
    @SuppressWarnings("unchecked")
    public String getSkin() {
        try {
            byte[] bytes = optionService.find("skin");
            if (bytes == null)
                return JsonMsg.success(new HashMap<String, String>());

            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            HashMap<String, String> options = (HashMap<String, String>)ois.readObject();

            return JsonMsg.success(options);
        }
        catch (Exception e) {
            logger.warn("Fail to unserialize");
            return JsonMsg.failed("message.fail.database");
        }
    }

    // ===== End of Mappings =====
}
