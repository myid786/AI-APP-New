package com.aimedia.media.repository;

import com.aimedia.media.entity.MediaAsset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MediaAssetRepository extends JpaRepository<MediaAsset, Long> {
    Optional<MediaAsset> findByBlobName(String blobName);
}
