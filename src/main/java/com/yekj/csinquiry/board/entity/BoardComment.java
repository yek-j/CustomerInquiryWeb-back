package com.yekj.csinquiry.board.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "board_comment")
@Data
public class BoardComment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    private String comment;

    @Column(name = "board_id")
    private Long boardId;

    private Long writer;
}
