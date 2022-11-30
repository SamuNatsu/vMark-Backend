package com.vmark.backend.service;

import com.vmark.backend.entity.Option;
import com.vmark.backend.mapper.OptionMapper;
import com.vmark.backend.utils.JsonMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

@Service
public class OptionService {
    // ===== Log =====
    private static final Logger logger = LoggerFactory.getLogger(OptionService.class);
    // ===== End of Log =====


    // ===== Mappers =====
    private final OptionMapper optionMapper;
    // ===== End of Mappers =====


    // ===== Constructor =====
    @Autowired
    public OptionService(OptionMapper optionMapper) {
        this.optionMapper = optionMapper;
    }
    // ===== End of Constructor =====


    // ===== Services =====
    // Update option
    public String update(String name, Serializable data) {
        // ===== Serialize =====
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(data);
        }
        catch (Exception e) {
            logger.warn("Fail to serialize data: name='{}'", name);
            return JsonMsg.failed("message.fail.database");
        }

        // ===== Update database =====
        int result = optionMapper.update(name, baos.toByteArray());

        // ===== Fail =====
        if (result != 1) {
            logger.warn("Fail to update option: name='{}'", name);
            return JsonMsg.failed("message.faill.database");
        }

        // ===== Success =====
        logger.info("Updated option: name='{}'", name);
        return JsonMsg.success();
    }

    // Find option
    public byte[] find(String name) {
        // ===== Find in database =====
        Option option = optionMapper.find(name);

        // ===== Fail =====
        if (option == null)
            return null;

        // ===== Success =====
        try {
            return option.getData();
        }
        catch (Exception e) {
            return null;
        }
    }
    // ===== End of Services =====
}
