package com.yekj.csinquiry.admin.service;

import com.yekj.csinquiry.user.entity.User;
import com.yekj.csinquiry.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {
    @Autowired
    private UserRepository userRepository;

    public String[] getUserEmailList() {
        List<User> userList = userRepository.findAll();

        // email을 배열에 담고 return;
        String[] arrUserEmail = userList.stream()
                .map(User::getEmail)
                .toArray(String[]::new);

        return arrUserEmail;
    }

    public void setGroup() {

    }
}
