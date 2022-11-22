package com.vmark.backend.service;

import com.vmark.backend.entity.Attachment;
import com.vmark.backend.mapper.AttachmentMapper;
import com.vmark.backend.utils.JsonMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class AttachmentService {
    // ===== Log =====
    private static final Logger logger = LoggerFactory.getLogger(AttachmentService.class);
    // ===== End of Log =====


    // ===== External Values =====
    @Value("${web.upload-path}")
    private String uploadPath;
    // ===== End of External Values =====


    // ===== External Autowired =====
    private final AttachmentMapper attachmentMapper;
    // ===== End of External Autowired =====


    // ===== Constructor =====
    @Autowired
    public AttachmentService(AttachmentMapper attachmentMapper) {
        this.attachmentMapper = attachmentMapper;
    }
    // ===== End of Constructor =====


    // ===== Services =====
    // Store attachment
    public String store(MultipartFile file) {
        // ===== Set folder =====
        String subPath = new SimpleDateFormat("yyyy/MM/dd/").format(new Date());
        File folder = new File(new File(uploadPath + subPath).getAbsolutePath());
        if (!folder.isDirectory()) {
            logger.warn("Directory not found: path={}", folder.getAbsolutePath());
            if (!folder.mkdirs()) {
                logger.warn("Failed to create folder: attachment='{}'", file.getOriginalFilename());
                return null;
            }
        }

        // ===== Store attachment =====
        String newName = UUID.randomUUID().toString();
        try {
            file.transferTo(new File(folder, newName));
            String path = folder.getAbsolutePath() + "/" + newName;
            logger.info("Successfully stored attachment: path='{}'", path);
            return path;
        }
        catch (IOException e) {
            logger.warn("Failed to store attachment: exception='{}'", e.getMessage());
            return null;
        }
    }

    // Add attachment
    public String add(String name, String path) {
        // ===== Insert into database =====
        int result = attachmentMapper.add(name, path, new Date().getTime());

        // ===== Failed =====
        if (result != 1) {
            logger.warn("Failed to add attachment: name='{}', path='{}'", name, path);
            return JsonMsg.failed("message.fail.add_attachment");
        }

        // ===== Success =====
        logger.info("Successfully added attachment: name='{}', path='{}'", name, path);
        return JsonMsg.success();
    }

    // Rename attachment
    public String rename(int aid, String name) {
        // ===== Update database =====
        int result = attachmentMapper.updateName(aid, name);

        // ===== Failed =====
        if (result != 1) {
            logger.warn("Failed to rename attachment: aid={}", aid);
            return JsonMsg.failed("message.fail.rename_attachment");
        }

        // ===== Success =====
        logger.info("Successfully renamed attachment: aid={}, name='{}'", aid, name);
        return JsonMsg.success();
    }

    // Delete attachment
    public String delete(int aid) {
        // ===== Delete from database =====
        Attachment attachment = attachmentMapper.findById(aid);
        int result = attachmentMapper.delete(aid);
        if (attachment == null || result != 1) {
            logger.warn("Failed to delete attachment: aid={}", aid);
            return JsonMsg.failed("mesage.fail.delete_attachment");
        }

        // ===== Delete local =====
        File localFile = new File(attachment.getPath());
        if (localFile.isFile()) {
            if (!localFile.delete())
                logger.warn("Fail to delete local file: path='{}'", attachment.getPath());
        }

        // ===== Success =====
        logger.info("Successfully deleted attachxment: aid={}", aid);
        return JsonMsg.success();
    }

    // Find attachment by ID
    public String findById(int aid) {
        // ===== Find in database =====
        Attachment attachment = attachmentMapper.findById(aid);

        // ===== Failed =====
        if (attachment == null) {
            logger.warn("Failed to find attachment: aid={}", aid);
            return JsonMsg.failed("message.not_found.attachment.id");
        }

        // ===== Success =====
        logger.info("Successfully found attachment: aid={}", aid);
        return JsonMsg.success(attachment);
    }

    // Find attachments by options
    public String findByOptions(HashMap<String, Object> options) {
        // ===== Find in database =====
        List<Attachment> attachments = attachmentMapper.findAll(options);

        // ===== Failed =====
        if (attachments == null) {
            logger.warn("Failed to find attachments: options={}", options);
            return JsonMsg.failed("message.not_found.attachment.options");
        }

        // ===== Success =====
        logger.info("Successfully found attachment: options={}", options);
        return JsonMsg.success(attachments);
    }

    // Load file by ID
    public byte[] loadById(int aid, HttpServletResponse httpServletResponse) {
        // ===== Find in database =====
        Attachment attachment = attachmentMapper.findById(aid);
        if (attachment == null) {
            logger.warn("Failed to find attachment: aid={}", aid);
            httpServletResponse.setStatus(404);
            return null;
        }

        // ===== Read local =====
        File file = new File(attachment.getPath());
        if (!file.isFile() || !file.canRead()) {
            logger.warn("Fail to read attachment: aid={}, path='{}'", aid, attachment.getPath());
            httpServletResponse.setStatus(404);
            return null;
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            return fis.readAllBytes();
        }
        catch (Exception err) {
            logger.warn("Fail to read attachment: aid={}, err='{}'", aid, err.getMessage());
            httpServletResponse.setStatus(404);
            return null;
        }
    }
    // ===== End of Services =====
}
