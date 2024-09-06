package com.videostreaming.VideoStreamingApplication.Services;

import org.springframework.web.multipart.MultipartFile;

import com.videostreaming.VideoStreamingApplication.Entities.Video;


public interface VideoService {

    Video save(MultipartFile file,Video video);
    Video get(String title);
    

    
}
