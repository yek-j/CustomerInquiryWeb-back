package com.yekj.csinquiry.board.controller;

import com.yekj.csinquiry.board.dto.BoardCommentFormDTO;
import com.yekj.csinquiry.board.service.BoardCommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class BoardCommentController {
    @Autowired
    private BoardCommentService boardCommentService;

    @PostMapping("/add-comment")
    public ResponseEntity<String> addBoardComment(@RequestBody BoardCommentFormDTO newComment, Authentication authentication) {
        String jwtToken = authentication.getCredentials().toString();

        try {
            boardCommentService.addComment(jwtToken, newComment);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.ok("댓글 저장 성공");
    }

}
