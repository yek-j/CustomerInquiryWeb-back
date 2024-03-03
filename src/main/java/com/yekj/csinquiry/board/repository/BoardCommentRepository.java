package com.yekj.csinquiry.board.repository;

import com.yekj.csinquiry.board.entity.Board;
import com.yekj.csinquiry.board.entity.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {
    List<BoardComment> findBoardCommentByBoardId(Long id);
}
