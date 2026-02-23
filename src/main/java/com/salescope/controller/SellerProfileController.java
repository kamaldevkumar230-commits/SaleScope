package com.salescope.controller;

import com.salescope.entity.User;
import com.salescope.repository.UserRepository;

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

    // 🔹 Update Profile Image
    @PostMapping("/profile")
    public String updateSellerProfileImage(@RequestParam("profileImage") MultipartFile file,
                                           HttpSession session) {

        User user = (User) session.getAttribute("currentUser");

        if(user == null) {
            return "redirect:/login";
        }

        try {
            if(!file.isEmpty()) {

                String uploadDir = System.getProperty("user.home") + "/salescope/uploads/profile/";
                File dir = new File(uploadDir);
                if(!dir.exists()) dir.mkdirs();

                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                file.transferTo(new File(uploadDir + fileName));

                user.setProfileImage("uploads/profile/" + fileName);
                userRepository.save(user);

                session.setAttribute("currentUser", user);
            }

        } catch(IOException e) {
            e.printStackTrace();
        }

        return "redirect:/seller/profile";  // 🔥 FIXED redirect
    }
}