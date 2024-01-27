package com.yekj.csinquiry.user.controller;

import com.yekj.csinquiry.user.dto.UserDTO;
import com.yekj.csinquiry.user.service.SignupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UserController {
    @Autowired
    private SignupService signupService;

    @PostMapping("/signin")
    public String signIn() {
        // 로그인
        return "signin";
    }

    @PostMapping("/signup")
    public String signUp(@RequestBody UserDTO user) {
        // 회원가입
        if(signupService.addUser(user)) return "{\"result\": \"ok\"}";
        else  return "{\"result\": \"fail\"}";
    }
}
