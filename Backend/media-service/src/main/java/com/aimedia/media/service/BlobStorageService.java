package com.aimedia.media.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobHttpHeaders;
import com.aimedia.media.entity.MediaAsset;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

@Service
public class BlobStorageService {
    private final BlobContainerClient containerClient;
    private final String mediaBaseUrl;
    private final String uploadDir;

    public BlobStorageService(
            @Value("${app.azure-storage.connection-string}") String connectionString,
            @Value("${app.azure-storage.container}") String containerName,
            @Value("${app.media-base-url}") String mediaBaseUrl,
            @Value("${app.upload-dir}") String uploadDir) {
        if (StringUtils.hasText(connectionString)) {
            BlobServiceClient serviceClient = new BlobServiceClientBuilder()
                    .connectionString(connectionString)
                    .buildClient();
            this.containerClient = serviceClient.getBlobContainerClient(containerName);
            this.containerClient.createIfNotExists();
        } else {
            this.containerClient = null;
        }
        this.mediaBaseUrl = mediaBaseUrl;
        this.uploadDir = uploadDir;
    }

    public MediaAsset upload(MultipartFile file) throws IOException {
        String originalFilename = StringUtils.cleanPath(
                file.getOriginalFilename() == null ? "upload" : file.getOriginalFilename());
        String contentType = file.getContentType() == null ? "application/octet-stream" : file.getContentType();
        String type = contentType.toLowerCase(Locale.ROOT).startsWith("video") ? "video" : "image";
        String blobName = buildBlobName(type, originalFilename);

        MediaAsset asset = new MediaAsset();
        asset.setBlobName(blobName);
        asset.setUrl(uploadToConfiguredStorage(file, blobName, contentType));
        asset.setType(type);
        asset.setOriginalFilename(originalFilename);
        asset.setContentType(contentType);
        asset.setSizeBytes(file.getSize());
        return asset;
    }

    private String uploadToConfiguredStorage(MultipartFile file, String blobName, String contentType) throws IOException {
        if (containerClient == null) {
            Path target = Paths.get(uploadDir, blobName);
            Files.createDirectories(target.getParent());
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return mediaBaseUrl.replaceAll("/$", "") + "/" + blobName;
        }

        BlobClient blobClient = containerClient.getBlobClient(blobName);
        BlobHttpHeaders headers = new BlobHttpHeaders().setContentType(contentType);
        blobClient.upload(file.getInputStream(), file.getSize(), true);
        blobClient.setHttpHeaders(headers);
        return buildUrl(blobClient, blobName);
    }

    private String buildBlobName(String type, String originalFilename) {
        LocalDate today = LocalDate.now();
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex >= 0 && dotIndex < originalFilename.length() - 1) {
            extension = originalFilename.substring(dotIndex).toLowerCase(Locale.ROOT);
        }

        return "%s/%d/%02d/%s%s".formatted(
                type,
                today.getYear(),
                today.getMonthValue(),
                UUID.randomUUID(),
                extension);
    }

    private String buildUrl(BlobClient blobClient, String blobName) {
        if (StringUtils.hasText(mediaBaseUrl)) {
            return mediaBaseUrl.replaceAll("/$", "") + "/" + blobName;
        }
        return blobClient.getBlobUrl();
    }
}
