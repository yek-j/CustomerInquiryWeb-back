package com.yekj.csinquiry.board.controller;

import com.yekj.csinquiry.board.dto.BoardDTO;
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
    public ResponseEntity<BoardPageDTO> getBoardList(
            @RequestParam(name = "page") int page, @RequestParam(name = "resolved", required = false) String resolved,
            @RequestParam(name = "writer", required = false) String writer, Authentication authentication) {
        String jwtToken = authentication.getCredentials().toString();

        boolean admin = authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().toString().equals("ROLE_ADMIN"));

        BoardPageDTO result = boardService.getBoardList(jwtToken, admin, page, resolved, writer);

        return ResponseEntity.ok(result);
    }

    @PostMapping("/add-board")
    public ResponseEntity<String> addBoard(@RequestBody BoardFormDTO newBoard, Authentication authentication) {
        String jwtToken = authentication.getCredentials().toString();
        String title = newBoard.getTitle();
        String content = newBoard.getContent();

        boolean check = boardService.validBoard(title, content);

        if (!check) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("저장할 제목이나 내용이 존재하지 않습니다.");

        try {
            boardService.addBoard(title, content, jwtToken);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.ok("저장 성공");
    }

    @GetMapping("/board/{id}")
    public ResponseEntity<?> getBoard(@PathVariable String id, Authentication authentication) {
        BoardDTO board = null;
        String jwtToken = authentication.getCredentials().toString();
        boolean admin = authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().toString().equals("ROLE_ADMIN"));

        try {
            board = boardService.getBoard(Long.valueOf(id), jwtToken, admin);
            board.setAdmin(admin ? "admin" : "user");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.ok(board);
    }

    @DeleteMapping("/board/{id}")
    @ResponseBody
    public ResponseEntity<String> deleteBoard(@PathVariable String id, Authentication authentication) {
        String jwtToken = authentication.getCredentials().toString();

        try {
            boardService.deleteBoard(Long.valueOf(id), jwtToken);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.ok("게시글 삭제 완료");
    }

    @PutMapping("/board/{id}")
    public ResponseEntity<String> updateBoard(@PathVariable String id, Authentication authentication, @RequestBody BoardFormDTO updateBoard) {
        String jwtToken = authentication.getCredentials().toString();

        try {
            boardService.updateBoard(Long.valueOf(id), jwtToken, updateBoard);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.ok("게시글 수정 완료");
    }
    
    @PutMapping("/board/resolved/{id}")
    public ResponseEntity<String> updateResolved(@PathVariable String id, Authentication authentication) {
        boolean admin = authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().toString().equals("ROLE_ADMIN"));
        if (!admin) return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("관리자만 변경할 수 있습니다.");
        
        try {
            boardService.updateResloved(Long.valueOf(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        return ResponseEntity.ok("해결 상태 변경 완료");
    }
}
