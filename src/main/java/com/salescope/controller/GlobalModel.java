package com.salescope.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.salescope.entity.User;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalModel {

    @ModelAttribute
    public void addUserToModel(HttpSession session, Model model) {
        User user = (User) session.getAttribute("currentUser");
        if(user != null) {
            model.addAttribute("currentUser", user);
        }
    }
}