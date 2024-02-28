package com.yekj.csinquiry.board.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "board")
@Data
public class Board {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private Long writer;

    private String title;

    private String content;

    @Column(name = "group_id")
    private Long gid;
}
