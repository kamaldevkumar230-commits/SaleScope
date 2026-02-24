package com.salescope.controller;

import com.salescope.entity.Product;
import com.salescope.entity.User;
import com.salescope.entity.Order;   // ✅ Correct import
import com.salescope.repository.OrderRepository;  // ✅ Important
import com.salescope.repository.ProductRepository;
import com.salescope.repository.UserRepository;
import com.salescope.service.ImageUploadService;
import com.salescope.service.ProductService;

import jakarta.persistence.criteria.Path;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class CustomerController {

	@Autowired
	private ImageUploadService imageUploadService;
	
    @Autowired
    private ProductService productService;

    @Autowired
    private OrderRepository orderRepository;   // ✅ Injected
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
	private ProductRepository productRepository;
	
    
  
    

    @GetMapping("/customer/dashboard")
    public String customerDashboard(HttpSession session, Model model) {

        String email = (String) session.getAttribute("email");
        String role = (String) session.getAttribute("role");

        if (email == null || role == null || !role.equalsIgnoreCase("customer")) {
            return "redirect:/login?error=loginfirst";
        }

        List<Product> products = productRepository.findByActiveTrue();
        model.addAttribute("products", products);

        model.addAttribute("userName", session.getAttribute("userName"));
        model.addAttribute("profileImg", session.getAttribute("profileImage"));

        return "customer_dashboard";
    }
    
    @GetMapping("/customer/cart")
    public String showCart(HttpSession session, Model model) {

        String userName = (String) session.getAttribute("userName");
        model.addAttribute("userName", userName);

        return "cart";
    }
    
    @PostMapping("/customer/cart")
    public String addToCart(@RequestParam("productId") Long productId,
                            HttpSession session) {

        Long userId = (Long) session.getAttribute("userId");

        if(userId == null){
            return "redirect:/login";
        }

        Product product = productService.getProductById(productId);

        Order order = new Order();
        order.getUser().getId();
        order.setStatus("Pending");
        order.setTotalAmount(product.getPrice());
        order.setOrderDate(LocalDateTime.now());

        orderRepository.save(order);

        return "redirect:/customer/cart";
    }
    
    
    @GetMapping("/customerprofile")
    public String profile(HttpSession session, Model model) {

        User user = (User) session.getAttribute("currentUser");
        
        

        if (user == null) {
           return "redirect:/login";
      }
        
      

        model.addAttribute("currentUser", user);
        return "customerprofile";
    }
    
    
    @PostMapping("/customerprofile")
    public String updateProfileImage(
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

                // Save Cloud URL in DB
                user.setProfileImage(imageUrl);
                userRepository.save(user);

                // 🔥 Update session
                session.setAttribute("currentUser", user);
                session.setAttribute("profileImage", imageUrl);
                session.setAttribute("userName", user.getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/customerprofile";
    }
    
	
    
}