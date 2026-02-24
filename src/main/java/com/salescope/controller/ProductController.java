package com.salescope.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import com.salescope.entity.Product;
import com.salescope.repository.ProductRepository;
import com.salescope.service.ImageUploadService;
import com.salescope.service.ProductService;

import jakarta.servlet.http.HttpSession;

@Controller
public class ProductController {

    private final ProductService service;
    private final ProductRepository productRepository;
    private final ImageUploadService imageUploadService;

    public ProductController(ProductService service,
                             ProductRepository productRepository,
                             ImageUploadService imageUploadService) {
        this.service = service;
        this.productRepository = productRepository;
        this.imageUploadService = imageUploadService;
    }

    @GetMapping("/")
    public String home(Model model){
        List<Product> products = productRepository.findByActiveTrue();
        model.addAttribute("products", products);
        return "index";
    }

    @GetMapping("/help")
    public String helpPage() {
        return "help";
    }

    @PostMapping("/add")
    public String addProduct(
            @RequestParam("name") String name,
            @RequestParam("price") Double price,
            @RequestParam("image") MultipartFile image,
            HttpSession session
    ) {

        Long sellerId = (Long) session.getAttribute("userId");

        if (sellerId == null) {
            return "redirect:/login";
        }

        String imageUrl = imageUploadService.uploadImage(image);

        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setImage(imageUrl);
        product.setSellerId(sellerId);

        productRepository.save(product);

        return "redirect:/seller/dashboard";
    }
}