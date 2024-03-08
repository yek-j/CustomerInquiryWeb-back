package com.yekj.csinquiry.board.entity;

import com.yekj.csinquiry.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

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

    @ManyToOne
    @JoinColumn(name = "writer")
    private User writer;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date wdate;
}
