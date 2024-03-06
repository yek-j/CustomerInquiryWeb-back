package com.yekj.csinquiry.board.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Date;
import java.sql.Timestamp;

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

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date wdate;
}
