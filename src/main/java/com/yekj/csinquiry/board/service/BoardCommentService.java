package com.yekj.csinquiry.board.service;

import org.springframework.stereotype.Service;

@Service
public class BoardCommentService {
    // saveComment
    // 저장 시 기존 Comment가 있는지 확인하고 없으면 parent_id = 0
    // 이미 Comment가 있으면 parent_id + 1
    public void saveComment(Long groupId) {
        
    }

}
