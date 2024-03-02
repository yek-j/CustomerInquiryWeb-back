package com.yekj.csinquiry.board.repository;

import com.yekj.csinquiry.board.dto.BoardListDTO;
import com.yekj.csinquiry.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    long count();
}
