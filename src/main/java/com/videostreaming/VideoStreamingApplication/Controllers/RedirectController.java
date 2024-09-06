package com.videostreaming.VideoStreamingApplication.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.servlet.view.RedirectView;

import com.videostreaming.VideoStreamingApplication.Helper.JwtUtil;
import com.videostreaming.VideoStreamingApplication.Repository.UserRepository;
//import com.videostreaming.VideoStreamingApplication.Repository.UserRepository;

@Controller
public class RedirectController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    public RedirectView dashboard(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        if (!JwtUtil.validateToken(token)) {
            return new RedirectView("/login");
        }

        String role = JwtUtil.getRole(token);
        if ("admin".equals(role)) {
            return new RedirectView("/uploadAndView");
        } else {
            return new RedirectView("/listVideos");
        }
    }
}
