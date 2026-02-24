package com.salescope.controller;

import com.salescope.entity.Product;
import com.salescope.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
public class SellerController {

    @Autowired
    private ProductService productService;

    @GetMapping("/seller/dashboard")
    public String sellerDashboard(HttpSession session, Model model) {

        String role = (String) session.getAttribute("role");
        Long sellerId = (Long) session.getAttribute("userId");

        if (!"seller".equalsIgnoreCase(role) || sellerId == null) {
            return "redirect:/login?error=loginfirst";
        }

        List<Product> products = productService.getProductsBySeller(sellerId);

        model.addAttribute("products", products);
        model.addAttribute("userName", session.getAttribute("userName"));

        Object profileImg = session.getAttribute("profileImage");
        model.addAttribute("profileImg", profileImg != null ? profileImg.toString() : "");

        return "seller_dashbord";
    }
}