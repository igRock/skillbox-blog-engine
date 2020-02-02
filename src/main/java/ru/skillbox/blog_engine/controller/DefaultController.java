package ru.skillbox.blog_engine.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.skillbox.blog_engine.repository.GlobalSettingsRepository;
import ru.skillbox.blog_engine.repository.UserRepository;

@Controller
public class DefaultController {

//    @Autowired
//    private GlobalSettingsRepository globalSettingsRepository;
//    @Autowired
//    private UserRepository userRepository;

    @RequestMapping("/")
    public String index() {
//
//        System.out.println(globalSettingsRepository.findById(1));
//        User user = userRepository.findById(1).get();
//
//        System.out.println(user.getCode());
//        System.out.println(user.getId());
//        System.out.println(user.getName());
//        System.out.println(user.getRegTime().toString());
//        System.out.println(user.isModerator());

        return "index";
    }



}
