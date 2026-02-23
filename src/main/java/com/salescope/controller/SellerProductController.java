package com.salescope.controller;

import com.salescope.entity.Product;
import com.salescope.entity.User;
import com.salescope.repository.ProductRepository;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

@Controller
@RequestMapping("/seller")
public class SellerProductController {

    @Autowired
    private ProductRepository productRepository;

    // 🔹 Open Add Product Page
    @GetMapping("/add-product")
    public String openAddProduct(HttpSession session) {

        User user = (User) session.getAttribute("currentUser");

        if(user == null || !user.getRole().equalsIgnoreCase("seller")){
            return "redirect:/login";
        }

        return "seller_add_product";
    }

    // 🔹 Save Product
    @PostMapping("/add-product")
    public String saveProduct(@RequestParam String name,
                              @RequestParam String description,
                              @RequestParam double price,
                              @RequestParam("image") MultipartFile file,
                              HttpSession session) {

        User user = (User) session.getAttribute("currentUser");

        if(user == null){
            return "redirect:/login";
        }

        try {

            String fileName = null;

            if(!file.isEmpty()) {

                String uploadDir = System.getProperty("user.home") 
                        + "/salescope/uploads/products/";

                File dir = new File(uploadDir);
                if(!dir.exists()) dir.mkdirs();

                fileName = System.currentTimeMillis() 
                        + "_" + file.getOriginalFilename();

                file.transferTo(new File(uploadDir + fileName));
            }

            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setSellerId(user.getId());
            

            if(fileName != null){
                product.setImage("uploads/products/" + fileName);
            }

            productRepository.save(product);

        } catch(IOException e){
            e.printStackTrace();
        }

        return "redirect:/seller/dashboard";
    }
    
    
    
    
    
    @PostMapping("/delete-product")
    public String deleteProduct(@RequestParam("productId") Long productId,
                                HttpSession session) {

        User user = (User) session.getAttribute("currentUser");

        if(user == null || !user.getRole().equalsIgnoreCase("seller")) {
            return "redirect:/login";
        }

        Product product = productRepository
                .findByIdAndSellerId(productId, user.getId())
                .orElse(null);

        if(product != null) {
            product.setActive(false);   // 🔥 SOFT DELETE
            productRepository.save(product);
        }

        return "redirect:/seller/dashboard";
    }
    
    
    @GetMapping("/edit-product/{id}")
    public String editProduct(@PathVariable Long id,
                              HttpSession session,
                              Model model) {

        User user = (User) session.getAttribute("currentUser");

        if(user == null || !user.getRole().equalsIgnoreCase("seller")){
            return "redirect:/login";
        }

        Product product = productRepository
                .findByIdAndSellerId(id, user.getId())
                .orElse(null);

        if(product == null){
            return "redirect:/seller/dashboard";
        }

        model.addAttribute("product", product);
        return "seller_edit_product";
    }
    
    
    
    @PostMapping("/update-product")
    public String updateProduct(@RequestParam Long productId,
                                @RequestParam String name,
                                @RequestParam Double price,
                                @RequestParam String existingImage,
                                @RequestParam("image") MultipartFile file,
                                HttpSession session) {

        User user = (User) session.getAttribute("currentUser");

        if(user == null){
            return "redirect:/login";
        }

        Product product = productRepository
                .findByIdAndSellerId(productId, user.getId())
                .orElse(null);

        if(product == null){
            return "redirect:/seller/dashboard";
        }

        try {

            String imagePath = existingImage;

            if(!file.isEmpty()){

                String uploadDir = System.getProperty("user.home")
                        + "/salescope/uploads/products/";

                File dir = new File(uploadDir);
                if(!dir.exists()) dir.mkdirs();

                String fileName = System.currentTimeMillis()
                        + "_" + file.getOriginalFilename();

                file.transferTo(new File(uploadDir + fileName));

                imagePath = "uploads/products/" + fileName;
            }

            product.setName(name);
            product.setPrice(price);
            product.setImage(imagePath);

            productRepository.save(product);

        } catch(IOException e){
            e.printStackTrace();
        }

        return "redirect:/seller/dashboard";
    }
    
}