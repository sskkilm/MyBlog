package com.sskkilm.myblog.repository;

import com.sskkilm.myblog.config.JpaAuditingConfig;
import com.sskkilm.myblog.entity.Post;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(JpaAuditingConfig.class)
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("생성 및 수정 일자 자동 생성")
    void auditingTest() {
        //given
        //when
        Post post = postRepository.save(Post.builder()
                .title("title")
                .content("content")
                .build());

        //then
        assertNotNull(post.getCreatedAt());
        assertNotNull(post.getUpdatedAt());
    }
}