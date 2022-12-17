package com.vmark.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vmark.backend.entity.OrderItem;
import com.vmark.backend.entity.User;
import com.vmark.backend.service.AuthService;
import com.vmark.backend.service.OrderService;
import com.vmark.backend.utils.JsonMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    // ===== Services =====
    private final AuthService authService;
    private final OrderService orderService;
    // ===== End of Services =====


    // ===== Contructor =====
    @Autowired
    public OrderController(AuthService authService,
                           OrderService orderService) {
        this.authService = authService;
        this.orderService = orderService;
    }
    // ===== End of Constructor =====


    // ===== Mappings =====
    // Add order (Self)
    @PostMapping("/add")
    public String add(@RequestParam("uid") int uid,
                      @RequestParam("order") String order,
                      @RequestParam("address") String address,
                      HttpServletRequest request) {
        // ===== Parse order =====
        OrderItem[] orderItems;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            orderItems = objectMapper.readValue(order, OrderItem[].class);
        }
        catch (Exception e) {
            return JsonMsg.failed("message.invalid.order");
        }

        // ===== Validate params =====
        if (uid < 1)
            return JsonMsg.failed("message.invalid.uid");

        // ===== Check login =====
        if (authService.checkLogin(request.getSession()) != AuthService.LoginStatus.PASS)
            return JsonMsg.failed("message.auth.no_login");
        User user = (User)request.getSession().getAttribute("user");
        if (user.getUid() != uid)
            return JsonMsg.failed("messsage.auth.no_login");

        // ===== Call service =====
        return orderService.addOrder(uid, orderItems, address);
    }

    // Count order (Self or Admin)
    @GetMapping("/count")
    public String count(@RequestParam("uid") int uid,
                        HttpServletRequest request) {
        // ===== Validate params =====
        if (uid < 1)
            return JsonMsg.failed("message.invalid.uid");

        // ====== Check login =====
        if (authService.checkLogin(request.getSession()) != AuthService.LoginStatus.PASS)
            return JsonMsg.failed("message.auth.no_login");

        // ===== Call service =====
        User op = (User)request.getSession().getAttribute("user");
        return orderService.count(uid, op.getUid(), op.getPrivilege());
    }

    // Get order
    @GetMapping("/")
    public String get(@RequestParam(value = "oid", required = false) Integer oid,
                      @RequestParam(value = "uid", required = false) Integer uid,
                      @RequestParam(value = "p", required = false) Integer page,
                      HttpServletRequest request) {
        // ===== oid first =====
        if (oid != null) {
            if (oid < 1)
                return JsonMsg.failed("message.invalid.oid");

            if (authService.checkLogin(request.getSession()) != AuthService.LoginStatus.PASS)
                return JsonMsg.failed("message.auth.no_login");

            User op = (User)request.getSession().getAttribute("user");
            return orderService.findByOid(oid, op.getUid(), op.getPrivilege());
        }

        // ===== Validate params =====
        if (uid == null || uid < 1)
            return JsonMsg.failed("message.invalid.uid");
        if (page != null && page < 1)
            return JsonMsg.failed("message.invalid.page");

        // ====== Check login =====
        if (authService.checkLogin(request.getSession()) != AuthService.LoginStatus.PASS)
            return JsonMsg.failed("message.auth.no_login");

        // ====== Call service =====
        User op = (User)request.getSession().getAttribute("user");
        return orderService.findOrderByUid(uid, page == null ? 0 : (page - 1) * 20, op.getUid(), op.getPrivilege());
    }
    // ===== End of Mappings =====
}
