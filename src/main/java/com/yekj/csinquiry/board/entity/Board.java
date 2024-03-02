package com.yekj.csinquiry.board.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Table(name = "board")
@Data
public class Board {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    private Long writer;

    @Column(length = 50)
    private String title;

    private String content;

    @Column(name = "group_id")
    private Long gid;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date wdate;
}
