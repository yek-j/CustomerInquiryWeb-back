package com.yekj.csinquiry.board.controller;

import com.yekj.csinquiry.board.dto.BoardCommentDTO;
import com.yekj.csinquiry.board.dto.BoardCommentFormDTO;
import com.yekj.csinquiry.board.service.BoardCommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class BoardCommentController {
    @Autowired
    private BoardCommentService boardCommentService;

    @PostMapping("/comment")
    public ResponseEntity<String> addBoardComment(@RequestBody BoardCommentFormDTO newComment, Authentication authentication) {
        String jwtToken = authentication.getCredentials().toString();

        try {
            boardCommentService.addComment(jwtToken, newComment);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.ok("댓글 저장 성공");
    }

    @GetMapping("/comment/{id}")
    public ResponseEntity<List<BoardCommentDTO>> getCommentList(@PathVariable String id) {
        List<BoardCommentDTO> commentList = boardCommentService.getCommentList(Long.valueOf(id));

        return ResponseEntity.ok(commentList);
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<String> deleteComment(@PathVariable Long id, Authentication authentication) {
        String jwtToken = authentication.getCredentials().toString();
        boolean admin = authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().toString().equals("ROLE_ADMIN"));

        try {
            boardCommentService.deleteComment(id, jwtToken, admin);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.ok("댓글 삭제 성공");
    }

    @PutMapping("/comment/{id}")
    public ResponseEntity<String> updateComment(String comment, @PathVariable Long id, Authentication authentication) {
        String jwtToken = authentication.getCredentials().toString();

        try {
            boardCommentService.updateComment(comment, id, jwtToken);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.ok("댓글 수정 성공");
    }
}
