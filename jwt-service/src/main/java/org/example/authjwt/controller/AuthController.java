package org.example.authjwt.controller;

import org.example.authjwt.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private InMemoryUserDetailsManager userDetailsManager;

    @PostMapping("/token")
    public String generateToken() {

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        UserDetails userDetails = userDetailsManager.loadUserByUsername(username);
        return jwtUtils.generateToken(userDetails);
    }
}