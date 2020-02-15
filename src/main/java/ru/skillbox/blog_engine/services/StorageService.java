package ru.skillbox.blog_engine.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageService {

    private final Path rootLocation = Paths.get("filestorage");

    public String store(MultipartFile file){
        Path pathAddress = this.rootLocation.resolve(file.getOriginalFilename());
        try {
            Files.copy(file.getInputStream(), pathAddress);
        } catch (Exception e) {
            throw new RuntimeException("FAIL! -> message = " + e.getMessage());
        }
        return pathAddress.toString();
    }
}
