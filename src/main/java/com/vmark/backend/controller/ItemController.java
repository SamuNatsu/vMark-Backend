package com.vmark.backend.controller;

import com.vmark.backend.service.AuthService;
import com.vmark.backend.service.ItemService;
import com.vmark.backend.utils.JsonMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@CrossOrigin
@RestController
@RequestMapping("/api/item")
public class ItemController {
    // ===== Services =====
    private final AuthService authService;
    private final ItemService itemService;
    // ===== End of Services =====


    // ===== Contructor =====
    @Autowired
    public ItemController(AuthService authService,
                          ItemService itemService) {
        this.authService = authService;
        this.itemService = itemService;
    }
    // ===== End of Constructor =====


    // ===== Mappings =====
    // Add item (Admin ONLY)
    @PostMapping("/add")
    public String add(@RequestParam("name") String name,
                      @RequestParam("cid") int cid,
                      @RequestParam("price") int price,
                      HttpServletRequest request) {
        // ===== Validate params ======
        if (cid < 1)
            return JsonMsg.failed("message.invalid.cid");

        if (price < 0)
            return JsonMsg.failed("message.invalid.price");

        // ===== Check privilege =====
        if (authService.checkPrivilege(request.getSession()) < 1)
            return JsonMsg.failed("message.fail.permission");

        // ===== Call service =====
        return itemService.add(name, cid, price);
    }

    // Delete item (Admin ONLY)
    @PostMapping("/delete")
    public String delete(@RequestParam("iid") int iid,
                         HttpServletRequest request) {
        // ===== Validate params =====
        if (iid < 1)
            return JsonMsg.failed("message.invalid.cid");

        // ===== Check privilege =====
        if (authService.checkPrivilege(request.getSession()) < 1)
            return JsonMsg.failed("message.fail.permission");

        // ===== Call service =====
        return itemService.delete(iid);
    }

    // Update item (Admin ONLY)
    @PostMapping("/update")
    public String update(@RequestParam("iid") int iid,
                         @RequestParam(value = "name", required = false) String name,
                         @RequestParam(value = "cid", required = false) Integer cid,
                         @RequestParam(value = "price", required = false) Integer price,
                         @RequestParam(value = "remain", required = false) Integer remain,
                         @RequestParam(value = "aid", required = false) Integer aid,
                         @RequestParam(value = "sale", required = false) Integer sale,
                         @RequestParam(value = "description", required = false) String description,
                         HttpServletRequest request) {
        // ===== Validate params =====
        if (iid < 1)
            return JsonMsg.failed("message.invalid.cid");

        // ===== Check privilege =====
        if (authService.checkPrivilege(request.getSession()) < 1)
            return JsonMsg.failed("message.fail.permission");

        // ===== Call service =====
        return itemService.update(iid, name, cid, price, remain, aid, sale, description);
    }

    // Count items (Everyone)
    @GetMapping("/count")
    public String count() {
        return itemService.count();
    }

    // Get item info by options (Everyone)
    @GetMapping("/")
    public String get(@RequestParam(value = "iid", required = false) Integer iid,
                      @RequestParam(value = "s", required = false) String name,
                      @RequestParam(value = "cid", required = false) Integer cid,
                      @RequestParam(value = "h", required = false) Integer hide,
                      @RequestParam(value = "p", required = false) Integer page,
                      @RequestParam(value = "on", required = false) String orderName,
                      @RequestParam(value = "ot", required = false) String orderType) {
        // ===== If request iid =====
        if (iid != null) {
            if (iid < 1)
                return JsonMsg.failed("message.invalid.iid");

            return itemService.findById(iid);
        }

        // ===== Generate options =====
        HashMap<String, Object> options = new HashMap<>();

        if (name != null)
            options.put("name", name);

        if (cid != null)
            options.put("cid", cid);

        if (hide != null && hide > 0)
            options.put("hideSoldOut", true);

        if (page != null) {
            if (page < 1)
                return JsonMsg.failed("message.invalid.page");

            options.put("offset", (page - 1) * 20);
        }
        else
            options.put("offset", 0);

        if (orderName != null) {

            options.put("orderName", "price");
        }

        if (orderType != null)
            options.put("orderType", "ASC");

        return itemService.findAll(options);
    }
    // ===== End of Mappings =====
}
