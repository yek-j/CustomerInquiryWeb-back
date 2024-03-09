package com.yekj.csinquiry.board.dto;

import com.yekj.csinquiry.board.entity.Board;
import lombok.Data;

import java.util.Date;

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

}
