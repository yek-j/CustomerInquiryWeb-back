package com.yekj.csinquiry.board.dto;

import com.yekj.csinquiry.board.entity.Board;
import com.yekj.csinquiry.board.entity.BoardComment;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class BoardDTO {
    private Board board;
    /* Board */
    private String title;
    private String content;
    private Date wdate;
    private boolean resolved;

    private String writerName;
    private String groupName;
    private String admin;
    /* ---- */

    private boolean edit;

    private List<BoardComment> boardComment = new ArrayList<>();

}
