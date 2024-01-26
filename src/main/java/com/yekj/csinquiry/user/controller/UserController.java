package com.yekj.csinquiry.user.controller;

import com.yekj.csinquiry.user.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class UserController {
    @PostMapping("/signin")
    public String signIn() {
        // 로그인
        return "signin";
    }

    @PostMapping("/signup")
    public String signUp(@RequestBody UserDTO user) {

        // 회원가입

        return "signup";
    }
}
