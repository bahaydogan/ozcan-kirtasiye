package com.ozcan_kirtasiye.app.service;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Data
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    // Store the file locally
    public String storeFile(MultipartFile file) throws IOException {
        // Generate a unique file name
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // Create the target file path
        Path targetLocation = Paths.get(uploadDir).resolve(fileName);

        // Ensure the directory exists
        Files.createDirectories(targetLocation.getParent());

        // Copy the file to the target location
        Files.copy(file.getInputStream(), targetLocation);

        // Return the relative path (to be stored in the database)
        return "/uploads/" + fileName;
    }

    // Retrieve the file as a Path object
    public Path getFilePath(String fileName) {
        return Paths.get(uploadDir).resolve(fileName).normalize();
    }

    public Resource loadFileAsResource(String fileName) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists()) {
            return resource;
        } else {
            throw new RuntimeException("File not found: " + fileName);
        }
    }

    public void deleteFile(String fileName) throws IOException {
        Path filePath = Paths.get(uploadDir).resolve(fileName).normalize();
        Files.deleteIfExists(filePath);
    }
}

