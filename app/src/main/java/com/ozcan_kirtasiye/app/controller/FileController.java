package com.ozcan_kirtasiye.app.controller;

import com.ozcan_kirtasiye.app.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;

@RestController
@RequestMapping("/uploads")
public class FileController {

    @Autowired
    FileStorageService fileStorageService;

    public FileController(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @GetMapping("/{fileName}")
    public ResponseEntity<Resource> serveFile(@PathVariable String fileName) {
        try {
            // Get the file path
            Path filePath = fileStorageService.getFilePath(fileName);

            // Load the file as a resource
            Resource resource = new UrlResource(filePath.toUri());

            // Return the file as a response
            return ResponseEntity.ok()
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}

