package com.yekj.csinquiry.user.controller;

import com.yekj.csinquiry.user.service.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
public class MainController {
    @Autowired
    private GroupService groupService;

    @GetMapping("/")
    public ResponseEntity<Map> mainPage(Authentication authentication) {
        String jwtToken = authentication.getCredentials().toString();
        Long groupId = groupService.getGroupId(jwtToken);

        boolean admin = authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().toString().equals("ROLE_ADMIN"));

        Map<String, String> result = new HashMap<>();
        result.put("admin", admin ? "admin" : "user");
        result.put("group", groupId != null ? String.valueOf(groupId) : null);

       return ResponseEntity.ok().body(result);
    }
}
