package com.yekj.csinquiry.user.controller;

import com.yekj.csinquiry.user.dto.Token;
import com.yekj.csinquiry.user.dto.UserDTO;
import com.yekj.csinquiry.user.service.SigninService;
import com.yekj.csinquiry.user.service.SignupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    @Autowired
    private SignupService signupService;

    @Autowired
    private SigninService signinService;

    @PostMapping("/signin")
    public ResponseEntity<Token> signIn(@RequestBody UserDTO user) throws Exception {
        // 로그인
        log.info("로그인");
        Token token = null;

        try {
            token = signinService.Signin(user);
        } catch (BadCredentialsException e) {
            log.error(e.getMessage());
            return new ResponseEntity<Token>(token, HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<Token>(token, HttpStatus.OK);
    }

    @PostMapping("/signup")
    public ResponseEntity<Map> signUp(@RequestBody UserDTO user) {
        // 회원가입
        Map<String, String> response = new HashMap<>();

        if(signupService.addUser(user)) response.put("result", "ok");
        else response.put("result", "faile");

        return ResponseEntity.ok(response);
    }

}
