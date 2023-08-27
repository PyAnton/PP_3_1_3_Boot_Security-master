package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@Controller
public class HomeController {
    private final UserService userService;

    @Autowired
    public HomeController(UserService userService) {
        this.userService = userService;
    }
    @GetMapping("/")
    public String getHome() {
        return "redirect:/user";
    }
    @GetMapping("/user")
    public String getUser(Principal principal, Model model) {
        User user = userService.findUserByEmail(principal.getName());
        model.addAttribute(user);
        return "user";
    }
    @GetMapping("/admin")
    public String getAdmin(@RequestParam(name = "count", defaultValue = "15") int count, Model model) {
        model.addAttribute("users", userService.listUsersCount(count));
        return "admin";
    }
    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }


    @PostMapping("/logout") // POST-запрос для фактического выхода
    public String customLogout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/login?logout"; // После выхода перенаправляет на страницу логина с сообщением

    }
}