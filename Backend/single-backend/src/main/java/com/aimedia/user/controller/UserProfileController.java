package com.aimedia.user.controller;

import com.aimedia.user.dto.CreateProfileRequest;
import com.aimedia.user.dto.UpdateProfileRequest;
import com.aimedia.user.entity.UserProfile;
import com.aimedia.user.service.UserProfileService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserProfileController {
    private final UserProfileService service;

    public UserProfileController(UserProfileService service) {
        this.service = service;
    }

    @PostMapping("/profile")
    public UserProfile create(@Valid @RequestBody CreateProfileRequest request) {
        return service.create(request);
    }

    @GetMapping("/profile/{userId}")
    public UserProfile get(@PathVariable Long userId) {
        return service.get(userId);
    }

    @PutMapping("/profile/{userId}")
    public UserProfile update(@PathVariable Long userId, @RequestBody UpdateProfileRequest request) {
        return service.update(userId, request);
    }
}
