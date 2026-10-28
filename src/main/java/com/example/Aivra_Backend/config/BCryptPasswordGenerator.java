package com.example.Aivra_Backend.config;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptPasswordGenerator {
    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String getPassword(String password){
        String hash = passwordEncoder.encode(password);
        return hash;
    }

}
