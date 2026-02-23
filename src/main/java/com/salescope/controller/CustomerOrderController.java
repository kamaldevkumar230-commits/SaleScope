package com.salescope.controller;

import com.salescope.entity.Order;
import com.salescope.entity.User;
import com.salescope.repository.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerOrderController {

    @Autowired
    private OrderRepository orderRepository;

    // 🔹 My Orders Page
    @GetMapping("/orders")
    public String myOrders(HttpSession session, Model model) {

        User user = (User) session.getAttribute("currentUser");

        if(user == null || 
           !user.getRole().equalsIgnoreCase("customer")){
            return "redirect:/login";
        }

        List<Order> orders =
            orderRepository.findByUser_IdAndStatus(user.getId(), "PLACED");

        model.addAttribute("orders", orders);

        return "my-orders";
    }

    // 🔹 Cancel Order
    @PostMapping("/cancel-order")
    public String cancelOrder(@RequestParam Long orderId,
                              HttpSession session){

        User user = (User) session.getAttribute("currentUser");

        if(user == null){
            return "redirect:/login";
        }

        Order order =
            orderRepository.findById(orderId).orElse(null);

        if(order != null &&
           order.getUser().getId().equals(user.getId()) &&
           order.getStatus().equalsIgnoreCase("PLACED")){

            order.setStatus("CANCELLED");
            orderRepository.save(order);
        }

        return "redirect:/customer/orders";
    }
}