package com.salescope.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.salescope.entity.Product;
import com.salescope.repository.ProductRepository;
import com.salescope.service.ProductService;

@Controller
public class ProductController {

    @Autowired
    private ProductService service;
    
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/")
    public String home(Model model){
    	List<Product> products = productRepository.findByActiveTrue();

        model.addAttribute("products", products);

        return "index";   // 👈 templates/index.html
    }
    
  
  
    
    @GetMapping("/help")
    public String helpPage() {
        return "help";
    }
}