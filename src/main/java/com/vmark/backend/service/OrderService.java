package com.vmark.backend.service;

import com.vmark.backend.entity.Item;
import com.vmark.backend.entity.Order;
import com.vmark.backend.entity.OrderItem;
import com.vmark.backend.mapper.ItemMapper;
import com.vmark.backend.mapper.OrderItemMapper;
import com.vmark.backend.mapper.OrderMapper;
import com.vmark.backend.utils.JsonMsg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class OrderService {
    // ===== Log =====
    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);
    // ===== End of Log =====


    // ===== Mappers =====
    private final ItemMapper itemMapper;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    // ===== End of Mappers =====


    // ===== Constructor =====
    @Autowired
    public OrderService(ItemMapper itemMapper,
                        OrderMapper orderMapper,
                        OrderItemMapper orderItemMapper) {
        this.itemMapper = itemMapper;
        this.orderMapper = orderMapper;
        this.orderItemMapper = orderItemMapper;
    }
    // ===== End of Constructor =====


    // ===== Services =====
    // Add order
    public String addOrder(int uid, OrderItem[] orderItems, String address) {
        // ===== Check remains =====
        int cnt = 0;
        for (OrderItem i : orderItems) {
            Item item = itemMapper.findById(i.getIid());
            if (item == null || item.getRemain() < i.getCount()) {
                for (int j = 0; j < cnt; ++j) {
                    item = itemMapper.findById(orderItems[j].getIid());
                    itemMapper.update(orderItems[j].getIid(),
                            null,
                            null,
                            null,
                            item.getRemain() + orderItems[j].getCount(),
                            null,
                            null,
                            null);
                }
                return JsonMsg.failed("message.fail.remain");
            }
            itemMapper.update(i.getIid(),
                    null,
                    null,
                    null,
                    item.getRemain() - i.getCount(),
                    null,
                    null,
                    null);
            ++cnt;
        }

        // ===== Insert into database =====
        int result = orderMapper.add(uid, new Date().getTime(), address);

        // ===== Fail =====
        if (result != 1) {
            logger.warn("Fail to add order: uid={}", uid);
            return JsonMsg.failed("message.fail.database");
        }

        // ===== Success =====
        Order noder = orderMapper.findNew(uid);
        for (OrderItem i : orderItems)
            orderItemMapper.add(noder.getOid(), i.getIid(), i.getPrice(), i.getCount());

        return JsonMsg.success(noder);
    }

    // Count
    public String count(int uid, int op_uid, short op_privilege) {
        return JsonMsg.success(orderMapper.count(uid, op_uid, op_privilege));
    }

    // Find by oid
    public String findByOid(int oid, int op_uid, short op_privilege) {
        // ===== Select from database =====
        Order order = orderMapper.findByOid(oid, op_uid, op_privilege);
        List<OrderItem> orderItems = orderItemMapper.findById(oid);

        // ===== Fail =====
        if (order == null)
            return JsonMsg.failed("message.not_found.order.id");
        if (orderItems == null)
            return JsonMsg.failed("message.fail.database");

        // ===== Success =====
        return JsonMsg.success(orderItems);
    }

    // Find by uid
    public String findOrderByUid(int uid, int offset, int op_uid, short op_privilege) {
        // ===== Select from database =====
        List<Order> orders = orderMapper.findByUid(uid, offset, op_uid, op_privilege);

        // ===== Fail =====
        if (orders == null)
            return JsonMsg.failed("message.fail.database");

        // ===== Success =====
        return JsonMsg.success(orders);
    }
    // ===== End of Services =====
}
