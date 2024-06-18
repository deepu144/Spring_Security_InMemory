package com.deepu.springsecuritydemo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @GetMapping("/home")
    public String home() {
        return "Hello World!";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/user")
    public String userHome() {
        return "Hello World! , User";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String adminHome() {
        return "Hello World! , Admin";
    }

//    @PostMapping("/register")
//    public String register(@RequestBody UserDto userDto){
//        if(userService.register(userDto)){
//            return "Register successful";
//        }else{
//            return "Register failed";
//        }
//    }

}
