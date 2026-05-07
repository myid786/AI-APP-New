package com.aimedia.user.service;

import com.aimedia.user.dto.CreateProfileRequest;
import com.aimedia.user.dto.UpdateProfileRequest;
import com.aimedia.user.entity.UserProfile;
import com.aimedia.user.repository.UserProfileRepository;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {
    private final UserProfileRepository repository;

    public UserProfileService(UserProfileRepository repository) {
        this.repository = repository;
    }

    public UserProfile create(CreateProfileRequest r) {
        return repository.findByUserId(r.userId()).orElseGet(() -> {
            UserProfile p = new UserProfile();
            p.setUserId(r.userId());
            p.setUsername(r.username());
            p.setFullName(r.fullName());
            p.setBio(r.bio());
            p.setProfileImageUrl(r.profileImageUrl());
            p.setRole(r.role());
            p.setLocation(r.location());
            p.setWebsite(r.website());
            return repository.save(p);
        });
    }

    public UserProfile get(Long userId) {
        return repository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Profile not found"));
    }

    public UserProfile update(Long userId, UpdateProfileRequest r) {
        UserProfile p = get(userId);
        p.setFullName(r.fullName());
        p.setBio(r.bio());
        p.setProfileImageUrl(r.profileImageUrl());
        p.setLocation(r.location());
        p.setWebsite(r.website());
        return repository.save(p);
    }
}
