package com.yekj.csinquiry.user.service;

import com.yekj.csinquiry.config.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupService {
    @Autowired
    private JwtProvider jwtProvider;

    public String getGroupId(String token) {
        return jwtProvider.getGroup(token);
    }
}
