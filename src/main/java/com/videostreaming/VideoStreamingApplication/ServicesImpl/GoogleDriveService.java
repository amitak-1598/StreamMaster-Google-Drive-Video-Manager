package com.videostreaming.VideoStreamingApplication.ServicesImpl;

import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;


import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.Permission;
import com.google.api.services.drive.model.FileList;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class GoogleDriveService {
    private static final String APPLICATION_NAME = "videoStreaming";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();

    private final Drive driveService;

    public GoogleDriveService() throws IOException {
        Credential credential = authorize();
        this.driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME).build();
    }

    private Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in = GoogleDriveService.class.getResourceAsStream("/credentials.json");
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY,
                clientSecrets, Collections.singleton(DriveScopes.DRIVE_FILE))
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File("tokens"))).setAccessType("offline")
                .build();

        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    public File uploadFile(String folderId, InputStream inputStream, String fileName, String mimeType) throws IOException {
        File fileMetadata = new File();
        fileMetadata.setName(fileName);
        fileMetadata.setParents(Collections.singletonList(folderId));

        AbstractInputStreamContent mediaContent = new InputStreamContent(mimeType, inputStream);
        return driveService.files().create(fileMetadata, mediaContent)
                .setFields("id")
                .execute();
    }
    
   
    public List<File> listFilesInFolder(String folderId) throws IOException {
    	          
        List<File> result = new ArrayList<>();
        String query = "'" + folderId + "' in parents and mimeType='video/mp4' and trashed = false";

        FileList files = driveService.files().list()
                .setQ(query)
                .setFields("files(id, name)")
                .execute();

        result.addAll(files.getFiles());
        return result;
    }
    
    public void deleteFile(String fileId) throws IOException {
        driveService.files().delete(fileId).execute();
    }
    
    public Drive getDriveService() {
        return driveService;
    }
}