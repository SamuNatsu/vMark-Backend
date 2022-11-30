package com.vmark.backend.mapper;

import com.vmark.backend.entity.OrderItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemMapper {
    int add(int oid, int iid, int price, int count);

    List<OrderItem> findById(int oid);
}
