package com.vmark.backend.mapper;

import com.vmark.backend.entity.Attachment;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface AttachmentMapper {
    int add(String name, String path, long timestamp);

    int delete(int aid);

    int count();

    Attachment findById(int aid);

    // Options: [name], [order_name, order_type], <offset>
    List<Attachment> findByOptions(HashMap<String, Object> options);

    int updateName(int aid, String name);
}
