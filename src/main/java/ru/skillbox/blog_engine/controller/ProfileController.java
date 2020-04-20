package ru.skillbox.blog_engine.controller;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.blog_engine.dto.ProfileDto;
import ru.skillbox.blog_engine.dto.ResultResponse;
import ru.skillbox.blog_engine.model.User;
import ru.skillbox.blog_engine.services.AuthService;
import ru.skillbox.blog_engine.services.ProfileService;
import ru.skillbox.blog_engine.services.StorageService;

@RestController
@RequestMapping("/api/profile/my")
public class ProfileController {

    @Autowired
    private AuthService authService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private StorageService storageService;

    public ProfileController(AuthService authService, ProfileService profileService,
                             StorageService storageService) {
        this.authService = authService;
        this.profileService = profileService;
        this.storageService = storageService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> updateProfile(@RequestBody ProfileDto profileData) {
        Optional<User> userOptional = authService.getAuthorizedUser();
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        User user = userOptional.get();
        ResultResponse result = new ResultResponse();
        result.setResult(profileService.updateProfile(user, profileData));
        return ResponseEntity.ok(result);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResultResponse> updateProfileWithPhoto(@RequestParam("photo") MultipartFile photo,
                                                    @RequestParam("removePhoto") boolean removePhoto,
                                                    @RequestParam("name") String name,
                                                    @RequestParam("email") String email,
                                                    @RequestParam("password") String password) {
        Optional<User> userOptional = authService.getAuthorizedUser();
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        User user = userOptional.get();
        String pathToSavedFile = storageService.store(photo);

        ProfileDto profileData = ProfileDto.builder()
            .photo(pathToSavedFile)
            .removePhoto(removePhoto)
            .name(name)
            .email(email)
            .password(password)
            .build();
        ResultResponse result = new ResultResponse();
        result.setResult(profileService.updateProfile(user, profileData));
        return ResponseEntity.ok(result);
    }
}
