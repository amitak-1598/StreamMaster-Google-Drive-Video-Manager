package com.videostreaming.VideoStreamingApplication.ServicesImpl;

import com.google.api.services.drive.model.File;
import java.io.IOException;
import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.videostreaming.VideoStreamingApplication.Dao.VideoRepository;
import com.videostreaming.VideoStreamingApplication.Entities.Video;
import com.videostreaming.VideoStreamingApplication.Services.VideoService;

import ch.qos.logback.core.util.StringUtil;
import jakarta.annotation.PostConstruct;

//@Service
//public class VideoServiceImpl implements VideoService {
//    private static final Logger logger = LoggerFactory.getLogger(VideoServiceImpl.class);
//
//    @Value("${file.video}")
//    String DIR;
//
//    @Autowired
//    private VideoRepository videoRepository;
//
//
//    @PostConstruct
//    public void init(){
//        try{
//        File directory=new File(DIR);
//        if(directory.exists()){
//            logger.info("Video Directory is already exist.");
//
//        }
//        else{
//            directory.mkdir();
//            logger.info("Video Directory has been created successfully.");
//        }
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public Video save(MultipartFile file,Video video) {
//        logger.info("save video method is running.");
//        try{
//        String originalFilename=file.getOriginalFilename();
//        String contentType=file.getContentType();
//        if(contentType.equals("video/mp4")){
//
//        }
//       InputStream inputStream= file.getInputStream();
//       System.out.println(originalFilename);
//       System.out.println(contentType);
//       String cleanFilename=StringUtils.cleanPath(originalFilename);
//       String cleanDir=StringUtils.cleanPath(DIR);
//       Path filePath=Path.of(cleanDir, cleanFilename);
//       video.setVideoPath(filePath.toString());
//       Files.copy(inputStream, filePath,StandardCopyOption.REPLACE_EXISTING);
//       videoRepository.save(video);
//       
//       logger.info("File Saved Successfully..");
//
//    
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
//        return video;
//    }

@Service
public class VideoServiceImpl implements VideoService {
	private static final Logger logger = LoggerFactory.getLogger(VideoServiceImpl.class);

	@Value("${google.drive.folder.id}")
	String DRIVE_FOLDER_ID;

	@Autowired
	private VideoRepository videoRepository;

	private final GoogleDriveService googleDriveService;

	@Autowired
	public VideoServiceImpl(GoogleDriveService googleDriveService) {
		this.googleDriveService = googleDriveService;
	}

	@Override
	public Video save(MultipartFile file, Video video) {
		logger.info("save video method is running.");
		try {
			String originalFilename = file.getOriginalFilename();
			String contentType = file.getContentType();

			if (contentType != null && contentType.equals("video/mp4")) {
				// Save the video to Google Drive
				File googleFile = googleDriveService.uploadFile(DRIVE_FOLDER_ID, file.getInputStream(),
						originalFilename, contentType);
				video.setVideoPath(googleFile.getId()); // Store the file ID from Google Drive

				videoRepository.save(video);
				logger.info("File Saved Successfully to Google Drive.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return video;
	}

	@Override
	public Video get(String title) {
		logger.info("get video method is running.");
		Video video = videoRepository.findByVideoTitle(title);

		if (video != null) {
			try {
				// Generate a shareable link for the video file
				String fileId = video.getVideoPath(); // The Google Drive file ID
				File googleFile = googleDriveService.getDriveService().files().get(fileId)
						.setFields("webViewLink, webContentLink").execute();

				String videoUrl = googleFile.getWebViewLink(); // Direct download link for video
				video.setVideoPath(videoUrl); // Update the video path with the shareable link

				return video;
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("Error retrieving video from Google Drive.", e);
			}
		} else {
			logger.warn("Video not found with title: " + title);
		}

		return null;
	}

}
