package com.salescope.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import com.salescope.entity.User;
import com.salescope.service.UserService;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    // Register Page
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    // Register Process
    @PostMapping("/register")
    public String registerUser(User user) {

        boolean status = userService.registerUser(user);

        if(status) {
            return "redirect:/login?registered";
        } else {
            return "redirect:/register?error";
        }
    }

    // Login Page
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // Login Process
    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        HttpSession session,
                        Model model) {

        User user = userService.login(email, password);

        if(user == null) {
            model.addAttribute("error", "Invalid Credentials");
            return "login";
        }
        
        session.setAttribute("currentUser", user);

        session.setAttribute("email", user.getEmail());
        session.setAttribute("role", user.getRole());
        session.setAttribute("userId", user.getId());
        session.setAttribute("userName", user.getName());
        session.setAttribute("profileImage", user.getProfileImage());

        if(user.getRole().equalsIgnoreCase("seller"))
            return "redirect:/seller/dashboard";
        else
            return "redirect:/customer/dashboard";
    }
    // Logout
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}