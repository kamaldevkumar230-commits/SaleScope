package com.salescope.controller;

import com.salescope.entity.User;
import com.salescope.repository.UserRepository;
import com.salescope.service.ImageUploadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/seller")   // 🔥 ONLY seller here
public class SellerProfileController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ImageUploadService imageUploadService;

    // 🔹 Open Seller Profile
    @GetMapping("/profile")   // 🔥 ONLY profile here
    public String sellerProfile(HttpSession session, Model model) {

        User user = (User) session.getAttribute("currentUser");

        if(user == null || !user.getRole().equalsIgnoreCase("seller")) {
            return "redirect:/login";
        }

        model.addAttribute("currentUser", user);
        return "sellerprofile";   // template name
    }

    @PostMapping("/profile")
    public String updateSellerProfileImage(
            @RequestParam("profileImage") MultipartFile file,
            HttpSession session) {

        User user = (User) session.getAttribute("currentUser");

        if (user == null) {
            return "redirect:/login";
        }

        try {
            if (!file.isEmpty()) {

                // 🔥 Upload to Cloudinary
                String imageUrl = imageUploadService.uploadImage(file);

                // Save Cloud URL
                user.setProfileImage(imageUrl);
                userRepository.save(user);

                session.setAttribute("currentUser", user);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/seller/profile";
    }
}