package com.yekj.csinquiry.admin.controller;

import com.yekj.csinquiry.admin.dto.UserAuthDTO;
import com.yekj.csinquiry.admin.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @GetMapping("/userlist")
    public ResponseEntity<String[]> getUserList() {
        String[] userlist = adminService.getUserEmailList();

        return ResponseEntity.ok(userlist);
    }

    @GetMapping("/userauth/{email}")
    public ResponseEntity<UserAuthDTO> getUserAuth(@PathVariable String email) {
        UserAuthDTO userAuth = adminService.getUserAuth(email);

        return ResponseEntity.ok(userAuth);
    }

    @PostMapping("/set-group")
    public ResponseEntity<String> setGroup(@RequestBody UserAuthDTO group) {

        return ResponseEntity.ok("");
    }
}