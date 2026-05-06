package com.aimedia.media.controller;

import com.aimedia.media.entity.MediaAsset;
import com.aimedia.media.repository.MediaAssetRepository;
import com.aimedia.media.service.BlobStorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/media")
@CrossOrigin(origins = "*")
public class MediaController {
    private final BlobStorageService blobStorageService;
    private final MediaAssetRepository mediaAssetRepository;

    public MediaController(BlobStorageService blobStorageService, MediaAssetRepository mediaAssetRepository) {
        this.blobStorageService = blobStorageService;
        this.mediaAssetRepository = mediaAssetRepository;
    }

    @PostMapping(value = "/upload", consumes = {"multipart/form-data"})
    public ResponseEntity<Map<String, String>> upload(@RequestParam("file") MultipartFile file) throws IOException {
        MediaAsset asset = blobStorageService.upload(file);
        asset.setCreatedAt(LocalDateTime.now());
        MediaAsset savedAsset = mediaAssetRepository.save(asset);

        return ResponseEntity.ok(Map.of(
                "id", savedAsset.getId().toString(),
                "url", savedAsset.getUrl(),
                "type", savedAsset.getType(),
                "filename", savedAsset.getBlobName()
        ));
    }
}
