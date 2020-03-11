package ru.skillbox.blog_engine.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DefaultController {
    @RequestMapping(method =
        {RequestMethod.OPTIONS, RequestMethod.GET}, //принимаем только GET OPTIONS
        value = "/**/{path:[^\\.]*}") //описание обрабатываемых ссылок (регулярка с переменной)
    public String redirectToIndex() {
        return "forward:/"; //делаем перенаправление
    }
}
