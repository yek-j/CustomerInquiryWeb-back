package com.yekj.csinquiry.board.dto;

import com.yekj.csinquiry.board.entity.Board;
import com.yekj.csinquiry.board.entity.BoardComment;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BoardDTO {
    private Board board;
    private List<BoardComment> boardComment;
    private String writerName;
    private String groupName;
}
