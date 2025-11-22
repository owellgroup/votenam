package com.example.votenam.controllers;

import com.example.votenam.services.FileUploadService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class FileViewController {
    
    @Autowired
    private FileUploadService fileUploadService;
    
    // View Candidate Photo
    @GetMapping("/api/photos/view/{fileName:.+}")
    public ResponseEntity<Resource> viewPhoto(@PathVariable String fileName, HttpServletRequest request) {
        try {
            Resource resource = fileUploadService.loadPhotoAsResource(fileName);
            
            String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // View Party Logo
    @GetMapping("/api/logos/view/{fileName:.+}")
    public ResponseEntity<Resource> viewLogo(@PathVariable String fileName, HttpServletRequest request) {
        try {
            Resource resource = fileUploadService.loadLogoAsResource(fileName);
            
            String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Download Candidate Photo
    @GetMapping("/api/photos/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadPhoto(@PathVariable String fileName, HttpServletRequest request) {
        try {
            Resource resource = fileUploadService.loadPhotoAsResource(fileName);
            
            String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Download Party Logo
    @GetMapping("/api/logos/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadLogo(@PathVariable String fileName, HttpServletRequest request) {
        try {
            Resource resource = fileUploadService.loadLogoAsResource(fileName);
            
            String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}

