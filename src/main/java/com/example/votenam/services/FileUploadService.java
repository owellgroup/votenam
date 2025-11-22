package com.example.votenam.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadService {
    
    @Value("${upload.path.images}")
    private String imagesUploadPath;
    
    @Value("${upload.path.documents}")
    private String documentsUploadPath;
    
    @Value("${app.base.url:https://vote.owellgraphics.com}")
    private String baseUrl;
    
    public String uploadPhoto(MultipartFile file) throws IOException {
        if (!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }
        
        File uploadDir = new File(imagesUploadPath);
        if (!uploadDir.exists()) uploadDir.mkdirs();
        
        String originalFileName = file.getOriginalFilename();
        String fileName = originalFileName.substring(0, originalFileName.lastIndexOf('.'))
                + "_" + System.currentTimeMillis()
                + originalFileName.substring(originalFileName.lastIndexOf('.'));
        Path filePath = Paths.get(imagesUploadPath).resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        
        return baseUrl + "/api/photos/view/" + fileName;
    }
    
    public String uploadLogo(MultipartFile file) throws IOException {
        if (!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }
        
        File uploadDir = new File(documentsUploadPath);
        if (!uploadDir.exists()) uploadDir.mkdirs();
        
        String originalFileName = file.getOriginalFilename();
        String fileName = originalFileName.substring(0, originalFileName.lastIndexOf('.'))
                + "_" + System.currentTimeMillis()
                + originalFileName.substring(originalFileName.lastIndexOf('.'));
        Path filePath = Paths.get(documentsUploadPath).resolve(fileName);
        Files.copy(file.getInputStream(), filePath);
        
        return baseUrl + "/api/logos/view/" + fileName;
    }
    
    public Resource loadFileAsResource(String fileName, String uploadPath) throws MalformedURLException {
        try {
            Path uploadPathObj = Paths.get(uploadPath).toAbsolutePath().normalize();
            Path filePath = uploadPathObj.resolve(fileName).normalize();
            
            if (!Files.exists(uploadPathObj)) {
                Files.createDirectories(uploadPathObj);
            }
            
            if (!filePath.startsWith(uploadPathObj)) {
                throw new RuntimeException("Invalid file path - attempted directory traversal");
            }
            
            if (!Files.exists(filePath)) {
                throw new RuntimeException("File not found: " + fileName);
            }
            
            if (!Files.isReadable(filePath)) {
                throw new RuntimeException("File is not readable: " + fileName);
            }
            
            Resource resource = new UrlResource(filePath.toUri());
            return resource;
            
        } catch (MalformedURLException e) {
            throw new RuntimeException("Malformed file URL for: " + fileName + ". Error: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Failed to load file: " + fileName + ". Error: " + e.getMessage());
        }
    }
    
    public Resource loadPhotoAsResource(String fileName) throws MalformedURLException {
        // Extract filename from URL if full URL is provided
        if (fileName.contains("/")) {
            fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
        }
        return loadFileAsResource(fileName, imagesUploadPath);
    }
    
    public Resource loadLogoAsResource(String fileName) throws MalformedURLException {
        // Extract filename from URL if full URL is provided
        if (fileName.contains("/")) {
            fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
        }
        return loadFileAsResource(fileName, documentsUploadPath);
    }
}

