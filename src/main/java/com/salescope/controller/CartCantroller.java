package com.salescope.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.salescope.entity.CartItem;
import com.salescope.entity.Product;
import com.salescope.repository.ProductRepository;

import org.springframework.ui.Model;
import jakarta.servlet.http.HttpSession;

@Controller
public class CartCantroller {
	
	@Autowired
	private ProductRepository productRepository;
	
	
	@PostMapping("/add-to-cart")
	public String addToCart(@RequestParam Long productId,
	                        HttpSession session) {

	    System.out.println("ADD TO CART CALLED 🔥");

	    Product product = productRepository.findById(productId).orElse(null);

	    if(product == null) {
	        return "redirect:/products";
	    }

	    List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

	    if(cart == null) {
	        cart = new ArrayList<>();
	    }

	    cart.add(new CartItem(product, 1));

	    session.setAttribute("cart", cart);

	    return "redirect:/cart";
	}
	
	@GetMapping("/cart")
	public String viewCart(HttpSession session, Model model) {

	  
		List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

	    if(cart == null) {
	        cart = new ArrayList<>();
	    }

	    double grandTotal = cart.stream()
	            .mapToDouble(CartItem::getTotalPrice)
	            .sum();

	    model.addAttribute("cart", cart);
	    model.addAttribute("grandTotal", grandTotal);

	    return "cart";
	}
	
	
	@GetMapping("/remove-from-cart")
	public String removeFromCart(@RequestParam Long productId,
	                             HttpSession session) {

	
		List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

	    if(cart != null) {
	        cart.removeIf(item -> item.getProduct().getId().equals(productId));
	        session.setAttribute("cart", cart);
	    }

	    return "redirect:/cart";
	}
	
	
	
	@GetMapping("/increase-qty")
	public String increaseQty(@RequestParam Long productId,
	                          HttpSession session) {

	    List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

	    if(cart != null) {
	        for(CartItem item : cart) {
	            if(item.getProduct().getId().equals(productId)) {
	                item.setQuantity(item.getQuantity() + 1);
	                break;
	            }
	        }
	        session.setAttribute("cart", cart);
	    }

	    return "redirect:/cart";
	}
	
	
	
	@GetMapping("/decrease-qty")
	public String decreaseQty(@RequestParam Long productId,
	                          HttpSession session) {

	    List<CartItem> cart = (List<CartItem>) session.getAttribute("cart");

	    if(cart != null) {
	        cart.removeIf(item -> {
	            if(item.getProduct().getId().equals(productId)) {

	                if(item.getQuantity() > 1) {
	                    item.setQuantity(item.getQuantity() - 1);
	                    return false;
	                }

	                return true; // remove if quantity 1
	            }
	            return false;
	        });

	        session.setAttribute("cart", cart);
	    }

	    return "redirect:/cart";
	}
	
	


}
