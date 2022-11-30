package com.vmark.backend.mapper;

import com.vmark.backend.entity.Option;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionMapper {
    int update(String name, byte[] data);

    Option find(String name);
}
