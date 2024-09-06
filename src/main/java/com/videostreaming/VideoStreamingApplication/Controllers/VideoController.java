package com.videostreaming.VideoStreamingApplication.Controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.videostreaming.VideoStreamingApplication.Dao.VideoRepository;
import com.videostreaming.VideoStreamingApplication.Entities.Video;
import com.videostreaming.VideoStreamingApplication.Helper.Message;
import com.videostreaming.VideoStreamingApplication.Services.VideoService;

@CrossOrigin("*")
@Controller
@RequestMapping("/api/v1/videos")
public class VideoController {
    private VideoService videoService;

    VideoController(VideoService videoService){
        this.videoService=videoService;
    }
    
    @Autowired
    VideoRepository videorepository;

    private static final Logger logger = LoggerFactory.getLogger(VideoController.class);
    
    
    
    @GetMapping("/upload")
    public String showUploadForm() {
        return "upload";
    }


    
    @PostMapping(value = "/save", produces = "application/json")
    public String save(@RequestParam("file") MultipartFile file,
                       @RequestParam("title") String title,
                       @RequestParam("desc") String desc,
                       Model model) {
        Video video = new Video();
        video.setVideoTitle(title);
        video.setVideoDesc(desc);
        Video savedVideo = videoService.save(file, video);

        if (savedVideo != null) {
            model.addAttribute("message", "File uploaded to Google Drive successfully");
            return "uploadSuccess";  // Returns a new Thymeleaf template called "uploadSuccess.html"
        }

        model.addAttribute("message", "Something went wrong");
        return "uploadFailure";  // Returns a template for failure, if needed
    }
    

    @GetMapping(value = "/get", produces = "application/json")
    public ResponseEntity<?> getVideo(@RequestParam("title") String title) {
        Video video = videoService.get(title);
        if (video != null) {
            return ResponseEntity.ok(video);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(Message.builder().message("Video not found").type("error").build());
    }
    
    
  
    
    
}
