package com.yekj.csinquiry.user.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "usergroup")
@Data
public class Group {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private long id;

    private String name;

    @OneToMany(mappedBy = "group")
    private List<User> users = new ArrayList<>();
}
