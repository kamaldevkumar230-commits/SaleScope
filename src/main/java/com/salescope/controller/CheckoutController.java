package com.salescope.controller;

import com.salescope.entity.*;
import com.salescope.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class CheckoutController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @GetMapping("/address")
    public String addressPage(HttpSession session) {
        if(session.getAttribute("currentUser") == null)
            return "redirect:/login";
        return "address";
    }

    @PostMapping("/payment")
    public String paymentPage(@RequestParam String address,
                              @RequestParam String city,
                              @RequestParam String state,
                              @RequestParam String pincode,
                              HttpSession session) {

        session.setAttribute("address", address);
        session.setAttribute("city", city);
        session.setAttribute("state", state);
        session.setAttribute("pincode", pincode);

        return "payment";
    }

    @PostMapping("/place-order")
    public String placeOrder(@RequestParam String paymentMethod,
                             HttpSession session) {

        User user = (User) session.getAttribute("currentUser");

        List<CartItem> cart =
            (List<CartItem>) session.getAttribute("cart");

        if(user == null || cart == null || cart.isEmpty())
            return "redirect:/cart";

        Order order = new Order();
        order.setUser(user);
        order.setAddress((String)session.getAttribute("address"));
        order.setCity((String)session.getAttribute("city"));
        order.setState((String)session.getAttribute("state"));
        order.setPincode((String)session.getAttribute("pincode"));
        order.setPaymentMethod(paymentMethod);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PLACED");

        double total = cart.stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();

        order.setTotalAmount(total);

        orderRepository.save(order);

        for(CartItem item : cart){
            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setProduct(item.getProduct());
            oi.setQuantity(item.getQuantity());
            oi.setPrice(item.getProduct().getPrice());
            orderItemRepository.save(oi);
        }

        session.removeAttribute("cart");

        return "order_success";
    }
}