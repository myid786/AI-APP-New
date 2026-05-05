package com.aimedia.media.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/media")
@CrossOrigin(origins = "*")
public class MediaController {
    @Value("${app.upload-dir}")
    private String uploadDir;

    @Value("${app.media-base-url:http://localhost:8085/uploads}")
    private String mediaBaseUrl;

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<Map<String, String>> upload(@RequestParam("file") MultipartFile file) throws IOException {
        Files.createDirectories(Paths.get(uploadDir));
        String filename = UUID.randomUUID() + "-" + file.getOriginalFilename();
        Path target = Paths.get(uploadDir, filename);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        String type = file.getContentType() != null && file.getContentType().startsWith("video") ? "video" : "image";
        String fileUrl = mediaBaseUrl.replaceAll("/$", "") + "/" + filename;

        return ResponseEntity.ok(Map.of(
                "url", fileUrl,
                "type", type,
                "filename", filename
        ));
    }
}
