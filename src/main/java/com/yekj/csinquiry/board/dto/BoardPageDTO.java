package com.yekj.csinquiry.board.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BoardPageDTO {
    long total;
    List<BoardListDTO> list;
}
