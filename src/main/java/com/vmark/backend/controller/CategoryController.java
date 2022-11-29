package com.vmark.backend.controller;

import com.vmark.backend.service.AuthService;
import com.vmark.backend.service.CategoryService;
import com.vmark.backend.utils.JsonMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/category")
public class CategoryController {
    // ===== External Autowired =====
    private final AuthService authService;
    private final CategoryService categoryService;
    // ===== End of External Autowired =====


    // ===== Validators =====
    // Category name validator
    private static final Pattern nameValidator = Pattern.compile("^\\w+$");
    // ===== End of Validators =====


    // ===== Constructor =====
    @Autowired
    public CategoryController(AuthService authService,
                              CategoryService categoryService) {
        this.authService = authService;
        this.categoryService = categoryService;
    }
    // ===== End of Constructor =====


    // ===== Mappings =====
    // Add
    @PostMapping("/add")
    public String add(@RequestParam("name") String name,
                      @RequestParam(value = "parent", required = false) Integer parent,
                      HttpServletRequest request) {
        // ===== Validate params =====
        Matcher nameMatcher = nameValidator.matcher(name);
        if (!nameMatcher.matches())
            return JsonMsg.failed("message.invalid.name");

        if (parent != null && parent < 1)
            return JsonMsg.failed("message.invalid.parent");

        // ===== Check privilege =====
        if (authService.checkPrivilege(request.getSession()) < 1)
            return JsonMsg.failed("message.fail.permission");

        // ===== Call service =====
        return categoryService.add(name, parent);
    }

    // Delete
    @PostMapping("/delete")
    public String delete(@RequestParam("cid") int cid,
                         HttpServletRequest request) {
        // ===== Validate params =====
        if (cid < 1)
            return JsonMsg.failed("message.invalid.cid");

        // ===== Check privilege =====
        if (authService.checkPrivilege(request.getSession()) < 1)
            return JsonMsg.failed("message.fail.permission");

        // ===== Call service =====
        return categoryService.delete(cid);
    }

    // Get all category info
    @GetMapping("/get")
    public String get() {
        return categoryService.findAll();
    }

    // Update
    @PostMapping("/update")
    public String update(@RequestParam("cid") int cid,
                         @RequestParam(value = "name", required = false) String name,
                         @RequestParam(value = "parent", required = false) Integer parent,
                         HttpServletRequest request) {
        // ===== Validate params =====
        if (cid < 1)
            return JsonMsg.failed("message.invalid.cid");

        if (name != null) {
            Matcher nameMatcher = nameValidator.matcher(name);
            if (!nameMatcher.matches())
                return JsonMsg.failed("message.invalid.name");
        }

        if (parent != null && parent < 1)
            return JsonMsg.failed("message.invalid.parent");

        // ===== Check privilege =====
        if (authService.checkPrivilege(request.getSession()) < 1)
            return JsonMsg.failed("message.fail.permission");

        // ===== Call service =====
        return categoryService.update(cid, name, parent);
    }
    // ===== End of Mappings =====
}
