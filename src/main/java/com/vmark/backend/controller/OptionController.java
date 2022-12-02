package com.vmark.backend.controller;

import com.vmark.backend.service.AuthService;
import com.vmark.backend.service.OptionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

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
    // Update skin
    @PostMapping("/update/skin")
    public String updateSkin(@RequestParam("skin") String skin) {
        return optionService.update("skin", skin);
    }

    // Get skin
    @GetMapping("/get/skin")
    public String getSkin() {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(optionService.find("skin"));

            ObjectInputStream ois = new ObjectInputStream(bais);

            return (String)ois.readObject();
        }
        catch (Exception e) {
            logger.warn("Fail to unserialize");
            e.printStackTrace();
            return "";
        }
    }

    // ===== End of Mappings =====
}
