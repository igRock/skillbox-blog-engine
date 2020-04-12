package ru.skillbox.blog_engine.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Random;

@Service
public class StorageService {

    private final String rootPath = new File("").getAbsolutePath()
            .concat("/src/main/resources");
    private Random random = new Random();

    public String store(MultipartFile file){
        String prefix = "/static";
        String absolutePathToFolder =
            "/img/upload/" + generatePathPart() + "/" + generatePathPart() + "/";
        new File(rootPath + prefix + absolutePathToFolder).mkdirs();
        String path = absolutePathToFolder + file.getOriginalFilename();
        try {
            Files.copy(file.getInputStream(), Paths.get(rootPath  + prefix + path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    public boolean delete(String filename) {
        boolean result = false;
        try {
            result = Files.deleteIfExists(Path.of(filename));
        } catch (NoSuchFileException e) {
            throw new RuntimeException("No such file exists: " + filename, e);
        } catch (IOException e) {
            throw new RuntimeException("Invalid permissions for file: " + filename, e);
        }
        return result;
    }

    public byte[] getImageByPath(Path path) {
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            return null;
        }
    }

    private String generatePathPart(){
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        return random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
