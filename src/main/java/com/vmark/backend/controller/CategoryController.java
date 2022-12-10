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
    // ===== Services =====
    private final AuthService authService;
    private final CategoryService categoryService;
    // ===== End of Services =====


    // ===== Validators =====
    // Category name validator (Anti-inject)
    private static final Pattern namePattern = Pattern.compile("^[^']+$");
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
    // Add (Admin ONLY)
    @PostMapping("/add")
    public String add(@RequestParam("name") String name,
                      @RequestParam(value = "parent", required = false) Integer parent,
                      HttpServletRequest request) {
        // ===== Validate params =====
        Matcher nameMatcher = namePattern.matcher(name);
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

    // Delete (Admin ONLY)
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

    // Update (Admin ONLY)
    @PostMapping("/update")
    public String update(@RequestParam("cid") int cid,
                         @RequestParam(value = "name", required = false) String name,
                         @RequestParam(value = "parent", required = false) Integer parent,
                         HttpServletRequest request) {
        // ===== Validate params =====
        if (cid < 1)
            return JsonMsg.failed("message.invalid.cid");

        if (name != null) {
            Matcher nameMatcher = namePattern.matcher(name);
            if (!nameMatcher.matches())
                return JsonMsg.failed("message.invalid.name");
        }

        if (parent != null && parent < 1)
            return JsonMsg.failed("message.invalid.parent");

        if (name == null && parent == null)
            return JsonMsg.success();

        // ===== Check privilege =====
        if (authService.checkPrivilege(request.getSession()) < 1)
            return JsonMsg.failed("message.fail.permission");

        // ===== Call service =====
        return categoryService.update(cid, name, parent);
    }

    // Get all category info (Everyone)
    @GetMapping("/")
    public String getAll() {
        return categoryService.findAll();
    }
    // ===== End of Mappings =====
}
