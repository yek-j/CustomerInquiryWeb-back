package com.yekj.csinquiry.admin.controller;

import com.yekj.csinquiry.admin.dto.SetGroupDTO;
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

    @PostMapping("/set-group")
    public ResponseEntity<String> setGroup(@RequestBody SetGroupDTO group) {

        return ResponseEntity.ok("");
    }
}
