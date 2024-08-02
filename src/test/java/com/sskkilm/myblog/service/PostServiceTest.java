package com.sskkilm.myblog.service;

import com.sskkilm.myblog.dto.PostCreateDto;
import com.sskkilm.myblog.entity.Post;
import com.sskkilm.myblog.entity.User;
import com.sskkilm.myblog.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    @Test
    @DisplayName("게시글 등록 성공")
    void createPost_success() {
        //given
        User user = User.builder()
                .id(1L)
                .build();
        PostCreateDto.Request request = PostCreateDto.Request.builder()
                .title("title")
                .content("content")
                .build();

        given(postRepository.save(any(Post.class))).willReturn(
                Post.builder()
                        .id(1L)
                        .user(user)
                        .title("title")
                        .content("content")
                        .build()
        );

        //when
        PostCreateDto.Response response = postService.createPost(user, request);

        //then
        assertEquals(1L, response.getPostId());
        assertEquals(1L, response.getUserId());
        assertEquals("title", response.getTitle());
        assertEquals("content", response.getContent());
    }
}