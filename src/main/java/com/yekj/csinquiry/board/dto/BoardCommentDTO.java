package com.yekj.csinquiry.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardCommentDTO {
    private Long id;
    private String writer;
    private String comment;
    private String date;
}
