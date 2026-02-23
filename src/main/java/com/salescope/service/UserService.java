package com.salescope.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.salescope.repository.UserRepository;
import com.salescope.entity.User;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    // Register
    public boolean registerUser(User user) {

        Optional<User> existing = userRepository.findByEmailIgnoreCase(user.getEmail());

        if(existing.isPresent()) {
            return false; // email already exists
        }

        if(user.getProfileImage() == null || user.getProfileImage().isEmpty()) {
            user.setProfileImage("default.png");
        }

        userRepository.save(user);
        return true;
    }

    // Login
    public User login(String email, String password) {

        Optional<User> optionalUser = userRepository.findByEmailIgnoreCase(email);

        if(optionalUser.isPresent()) {

            User user = optionalUser.get();

            if(user.getPassword().equals(password)) {
                return user;
            }
        }

        return null;
    }
}