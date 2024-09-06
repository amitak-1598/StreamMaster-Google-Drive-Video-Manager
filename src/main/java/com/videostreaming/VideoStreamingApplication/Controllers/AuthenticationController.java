package com.videostreaming.VideoStreamingApplication.Controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import com.videostreaming.VideoStreamingApplication.Entities.Users;
import com.videostreaming.VideoStreamingApplication.Services.AuthenticationService;
import com.videostreaming.VideoStreamingApplication.Services.TokenBlacklistService;

@Controller
@RequestMapping("/api/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;
    
    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody Users user ) {
        boolean isRegistered = authenticationService.registerUser(user.getUsername(), user.getPassword(), user.getRole());
        if (isRegistered) {
            return ResponseEntity.ok("User registered successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User already exists");
        }
    }
    
    
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody Users user) {
//        String token = authenticationService.authenticate(user.getUsername(), user.getPassword());
//        if (token != null) {
//            return ResponseEntity.ok(token);
//        }
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
//    }
    
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String username, @RequestParam String password) {
        String token = authenticationService.authenticate(username, password);
        if (token != null) {
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }
    
    @GetMapping("/login")
    public String getLoginPage() {
        return "login"; 
    }
    
    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }
    
    @PostMapping("/logout")
    public RedirectView logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        tokenBlacklistService.blacklistToken(token);
        return new RedirectView("/api/auth/login");
    }
    
}
