package com.mate.springwebdemo.controller;

import com.mate.springwebdemo.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping("/index")
    public User getIndexPage(){
        User bob = new User("Bob", 21);
        return bob;
    }
}
