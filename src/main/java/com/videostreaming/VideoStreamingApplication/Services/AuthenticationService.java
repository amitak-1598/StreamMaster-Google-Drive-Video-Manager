package com.videostreaming.VideoStreamingApplication.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.videostreaming.VideoStreamingApplication.Entities.Users;
import com.videostreaming.VideoStreamingApplication.Helper.JwtUtil;
import com.videostreaming.VideoStreamingApplication.Repository.UserRepository;

@Service
public class AuthenticationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    

    public String authenticate(String username, String password) {
        Users user = userRepository.findByUsername(username);
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return JwtUtil.generateToken(username, user.getRole());
        }
        return null;
    }
    
    public boolean registerUser(String username, String password, String role) {
        
        Users existingUser = userRepository.findByUsername(username);
        if (existingUser != null) {
            return false; 
        }

       
        Users newUser = new Users();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setRole(role);

        userRepository.save(newUser);
        return true;
    }
    
   
}
