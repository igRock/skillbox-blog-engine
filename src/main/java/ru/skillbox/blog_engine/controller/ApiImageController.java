package ru.skillbox.blog_engine.controller;

import java.io.File;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.blog_engine.services.AuthService;
import ru.skillbox.blog_engine.services.StorageService;

@Controller
public class ApiImageController {
    @Autowired
    private AuthService authService;
    @Autowired
    private StorageService storageService;

    public ApiImageController(AuthService authService, StorageService storageService) {
        this.authService = authService;
        this.storageService = storageService;
    }

    @GetMapping(value = "/img/upload/{dir1}/{dir2}/{fileName}")
    public @ResponseBody byte[] getImage(@PathVariable String dir1,
                                         @PathVariable String dir2,
                                         @PathVariable String fileName) {
        // Вместо '/' использовать File.separator()
        String route = new File("").getAbsolutePath()
            .concat("/src/main/resources/static/img/upload/" + dir1 + "/" + dir2 + "/" + fileName);
        return storageService.getImageByPath(Path.of(route));
    }

    @PostMapping(value = "/api/image",
        consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
        produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> postImage(@RequestParam("image") MultipartFile image) {
        if (authService.getAuthorizedUser().isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(storageService.store(image), HttpStatus.OK);
    }
}
