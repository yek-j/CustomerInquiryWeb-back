package com.yekj.csinquiry.board.controller;

import com.yekj.csinquiry.board.dto.BoardFormDTO;
import com.yekj.csinquiry.board.dto.BoardPageDTO;
import com.yekj.csinquiry.board.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
public class BoardController {
    @Autowired
    private BoardService boardService;

    @GetMapping("/boardlist")
    public ResponseEntity<BoardPageDTO> getBoardList(Authentication authentication) {
        String jwtToken = authentication.getCredentials().toString();

        boolean admin = authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().toString().equals("ROLE_ADMIN"));

        BoardPageDTO result = boardService.getBoardList(jwtToken, admin, 0);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/boardlist/{page}")
    public ResponseEntity<BoardPageDTO> getBoardListPage(@PathVariable String page, Authentication authentication) {
        String jwtToken = authentication.getCredentials().toString();

        boolean admin = authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().toString().equals("ROLE_ADMIN"));

        BoardPageDTO result = boardService.getBoardList(jwtToken, admin, Integer.valueOf(page));

        return ResponseEntity.ok(result);
    }

    @PostMapping("/add-board")
    public ResponseEntity<?> addBoard(@RequestBody BoardFormDTO newBoard, Authentication authentication) {
        String jwtToken = authentication.getCredentials().toString();
        String title = newBoard.getTitle();
        String content = newBoard.getContent();

        boolean check = boardService.validBoard(title, content);

        if (!check) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("저장할 제목이나 내용이 존재하지 않습니다.");

        try {
            boardService.addBoard(title, content, jwtToken);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("저장 실패");
        }

        return ResponseEntity.ok("저장 성공");
    }
}
