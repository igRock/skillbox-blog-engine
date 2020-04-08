package ru.skillbox.blog_engine.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skillbox.blog_engine.dto.ProfileDto;
import ru.skillbox.blog_engine.model.User;
import ru.skillbox.blog_engine.repository.UserRepository;

@Service
public class ProfileService {

    @Autowired
    private StorageService storageService;
    @Autowired
    private UserRepository userRepository;

    public ProfileService(StorageService storageService, UserRepository userRepository) {
        this.storageService = storageService;
        this.userRepository = userRepository;
    }

    public Boolean updateProfile(User user, ProfileDto profileDto) {

        String photo = profileDto.getPhoto();
        boolean removePhoto = profileDto.isRemovePhoto();
        String name = profileDto.getName();
        String email = profileDto.getEmail();
        String password = profileDto.getPassword();

        if (photo != null && (!photo.isBlank() && !photo.equals(user.getPhoto()))) {
            user.setPhoto(photo);
        }

        if (removePhoto && (user.getPhoto() != null)) {
            storageService.delete(user.getPhoto());
            user.setPhoto(null);
        }

        if (!name.isBlank() && !name.equals(user.getName())) {
            user.setName(name);
        }

        if (!email.isBlank() && !email.equals(user.getEmail())) {
            user.setEmail(email);
        }

        if (password != null && !password.isBlank()) {
            user.setPassword(password);
        }

        User savedUser = userRepository.save(user);
        return user.getId().equals(savedUser.getId());
    }
}
