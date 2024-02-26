package com.yekj.csinquiry.admin.controller;

import com.yekj.csinquiry.admin.dto.GroupDTO;
import com.yekj.csinquiry.admin.dto.UserAuthDTO;
import com.yekj.csinquiry.admin.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/grouplist")
    public ResponseEntity<List<GroupDTO>> getGroupList() {
        List<GroupDTO> groupList = adminService.getGroupList();

        return ResponseEntity.ok(groupList);
    }

    @GetMapping("/group/{id}")
    public ResponseEntity<GroupDTO> getGroupInfo(@PathVariable String id) {
        GroupDTO group = adminService.getGroupInfo(id);
        log.info("group = {}", group);
        return ResponseEntity.ok(group);
    }

    @PostMapping("/set-group")
    public ResponseEntity<String> setUserAuth(@RequestBody UserAuthDTO userAuth) {
        try {
            adminService.setUserAuth(userAuth);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("사용자 권한 저장 실패.");
        }
        return ResponseEntity.ok("사용자 권한 저장 성공");
    }

    @PostMapping("/add-group")
    public ResponseEntity<List<GroupDTO>> addGroup(@RequestBody String name, String description) {
        List<GroupDTO> newGroupList = null;
        try {
            newGroupList = adminService.addGroup(name, description);
        } catch (Exception e) {
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("사용자 권한 저장 실패.");
        }

        return ResponseEntity.ok(newGroupList);
    }
}
