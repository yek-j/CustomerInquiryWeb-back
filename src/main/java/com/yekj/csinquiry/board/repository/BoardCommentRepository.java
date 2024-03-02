package com.yekj.csinquiry.board.repository;

import com.yekj.csinquiry.board.entity.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {
}
