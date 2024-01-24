package com.yekj.csinquiry.user.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    private String password;

    private String name;

    private String email;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    private String admin;

    private String enabled;
}
