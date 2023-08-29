package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private static final String HOMEPAGE = "redirect:/admin";

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public String getAdmin(@RequestParam(name = "count", defaultValue = "15") int count, Model model) {
        model.addAttribute("users", userService.listUsersCount(count));
        return "admin";
    }

    @PostMapping("/create")
    public String processAddForm(@ModelAttribute User user, @RequestParam String role) {
        user.addRole(role);
        userService.add(user);
        return HOMEPAGE;
    }

    @PostMapping("/edit/{id}")
    public String processEditForm(@PathVariable Long id, @ModelAttribute User user, @RequestParam String role) {
        userService.edit(id, user, role);
        return HOMEPAGE;
    }

    @PostMapping("/delete/{id}")
    public String processDeleteForm(@PathVariable Long id) {
        userService.dell(id);
        return HOMEPAGE;
    }

}
