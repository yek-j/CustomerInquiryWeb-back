package com.yekj.csinquiry.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoardListDTO {
    private Long id;
    private String writerName;
    private String title;
    private String preview;
    private String groupName;
    private String wdate;
    private boolean resolved;
}
