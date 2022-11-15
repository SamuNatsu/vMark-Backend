package com.vmark.backend.mapper;

import com.vmark.backend.entity.ItemPic;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPicMapper {
    int add(ItemPic itemPic);

    int deleteByIpid(int ipid);
    int deleteByIid(int iid);

    ItemPic findByIpid(int ipid);
    List<ItemPic> findByIid(int iid);
}
