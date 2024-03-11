package com.yekj.csinquiry.user.controller;

import com.yekj.csinquiry.user.dto.Token;
import com.yekj.csinquiry.user.dto.UserDTO;
import com.yekj.csinquiry.user.service.SigninService;
import com.yekj.csinquiry.user.service.SignupService;
import com.yekj.csinquiry.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    @Autowired
    private SignupService signupService;

    @Autowired
    private SigninService signinService;

    @Autowired
    private UserService userService;

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

    @GetMapping("/chkadmin")
    public ResponseEntity<Map> chkAdmin(Authentication authentication) {
        boolean admin = authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().toString().equals("ROLE_ADMIN"));
        Map<String, String> response = new HashMap<>();

        if(admin) response.put("admin", "admin");
        else response.put("admin", "user");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/userinfo")
    public ResponseEntity<?> getUserInfo(Authentication authentication) {
        String jwtToken = authentication.getCredentials().toString();
        boolean admin = authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().toString().equals("ROLE_ADMIN"));

        UserDTO userDTO = null;

        try {
            userDTO = userService.getUserInfo(jwtToken);
            if(admin) userDTO.setAdmin("admin");
            else userDTO.setAdmin("user");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.ok(userDTO);
    }

    @PutMapping("/userinfo")
    public ResponseEntity<String> updateUserInfo(@RequestBody UserDTO updateInfo, Authentication authentication) {
        String jwtToken = authentication.getCredentials().toString();

        try {
            userService.updateUserInfo(updateInfo, jwtToken);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.ok("사용자 정보 수정 완료");
    }
}
