package com.vmark.backend.mapper;

import com.vmark.backend.entity.Attachment;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface AttachmentMapper {
    int add(String name, String path, long timestamp);

    int updateName(int aid, String name);

    int delete(int aid);

    Attachment findById(int aid);
    List<Attachment> findAll(HashMap<String, Object> optoins);
}
