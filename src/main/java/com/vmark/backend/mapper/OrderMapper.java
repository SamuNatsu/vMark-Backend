package com.vmark.backend.mapper;

import com.vmark.backend.entity.Order;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderMapper {
    int add(int uid, long timestamp);

    int count(int uid, int op_uid, short op_privilege);

    Order findByOid(int oid, int op_uid, short op_privilege);
    Order findNew(int uid);
    List<Order> findByUid(int uid, int offset, int op_uid, short op_privilege);
}
