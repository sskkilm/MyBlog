package com.sskkilm.myblog.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    @Column(length = 1000)
    private String content;

    private LocalDateTime deletedAt;
    
}
