package com.videostreaming.VideoStreamingApplication.Controllers;

import org.springframework.stereotype.Controller;

import com.google.api.services.drive.model.File;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.videostreaming.VideoStreamingApplication.Helper.Message;
import com.videostreaming.VideoStreamingApplication.ServicesImpl.GoogleDriveService;

import java.io.IOException;
import java.util.List;
@Controller
public class VideoListController {

	
	@Value("${google.drive.folder.id}")
    private String driveFolderId;

    private final GoogleDriveService googleDriveService;

    public VideoListController(GoogleDriveService googleDriveService) {
        this.googleDriveService = googleDriveService;
    }
    
    @GetMapping("/")
    public String showUploadAndViewPage() {
        return "uploadAndView";
    }
    
 
  
    @GetMapping("/uploadAndView")
    public String showUploadAndViewPage(@RequestParam(value = "fileId", required = false) String fileId, Model model) {
        try {
            List<com.google.api.services.drive.model.File> videos = googleDriveService.listFilesInFolder(driveFolderId);
            model.addAttribute("videos", videos);
            model.addAttribute("fileId", fileId);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Unable to fetch video list.");
        }
        return "uploadAndView";
    }


    @GetMapping("/listVideos")
    public String listVideos(Model model) {
        try {
            List<com.google.api.services.drive.model.File> files = googleDriveService.listFilesInFolder(driveFolderId);
            model.addAttribute("videos", files);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Unable to retrieve video list");
        }
        return "listVideos";
    }

    @GetMapping("/viewVideo")
    public String viewVideo(@RequestParam(name = "fileId", required = true) String fileId, Model model) throws IOException {
    	List<com.google.api.services.drive.model.File> files = googleDriveService.listFilesInFolder(driveFolderId);
        model.addAttribute("fileId", fileId);
        model.addAttribute("videos", files);
        return "viewVideo";
    }
    
    @PostMapping(value = "/delete", produces = "application/json")
    public ResponseEntity<?> deleteVideo(@RequestParam("fileId") String fileId) {
        try {
            googleDriveService.deleteFile(fileId);
            return ResponseEntity.ok(Message.builder()
                .message("Video deleted successfully")
                .type("success")
                .build());
        } catch (IOException e) {
            System.out.println("Error deleting video " + e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Message.builder()
                    .message("Failed to delete video")
                    .type("error")
                    .build());
        }
    }
}
