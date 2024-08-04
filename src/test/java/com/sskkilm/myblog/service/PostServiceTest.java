package com.sskkilm.myblog.service;

import com.sskkilm.myblog.dto.PostCreateDto;
import com.sskkilm.myblog.dto.PostUpdateDto;
import com.sskkilm.myblog.entity.Post;
import com.sskkilm.myblog.entity.User;
import com.sskkilm.myblog.repository.PostRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
        assertEquals("Create Post Success", response.getMessage());
    }

    @Test
    @DisplayName("게시글 수정 성공")
    void updatePost_success() {
        //given
        PostUpdateDto.Request request = PostUpdateDto.Request.builder()
                .title("updateTitle")
                .content("updateContent")
                .build();
        User user = User.builder()
                .id(1L)
                .build();
        Post post = Post.builder()
                .id(1L)
                .user(user)
                .title("title")
                .content("content")
                .createdAt(LocalDateTime.now())
                .build();
        given(postRepository.findById(1L)).willReturn(Optional.of(post));

        //when
        PostUpdateDto.Response response = postService.updatePost(1L, request);

        //then
        assertEquals(1L, response.getPostId());
        assertEquals("updateTitle", response.getTitle());
        assertEquals("updateContent", response.getContent());
        assertEquals("Update Post Success", response.getMessage());
    }

    @Test
    @DisplayName("게시글 수정 성공 - 경고 알림")
    void updatePost_success_warning() {
        //given
        PostUpdateDto.Request request = PostUpdateDto.Request.builder()
                .title("updateTitle")
                .content("updateContent")
                .build();
        User user = User.builder()
                .id(1L)
                .build();
        Post post = Post.builder()
                .id(1L)
                .user(user)
                .title("title")
                .content("content")
                .createdAt(LocalDateTime.now().minusDays(9))
                .build();
        given(postRepository.findById(1L)).willReturn(Optional.of(post));

        //when
        PostUpdateDto.Response response = postService.updatePost(1L, request);

        //then
        assertEquals(1L, response.getPostId());
        assertEquals("updateTitle", response.getTitle());
        assertEquals("updateContent", response.getContent());
        assertEquals("하루 후 수정 불가", response.getMessage());
    }

    @Test
    @DisplayName("게시글 수정 실패 - 존재하지 않는 게시글")
    void updatePost_fail_PostNotFound() {
        //given
        PostUpdateDto.Request request = PostUpdateDto.Request.builder()
                .title("updateTitle")
                .content("updateContent")
                .build();
        given(postRepository.findById(1L)).willReturn(Optional.empty());

        //when
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> postService.updatePost(1L, request));

        //then
        assertEquals("존재하지 않는 게시글", exception.getMessage());
    }

    @Test
    @DisplayName("게시글 수정 실패 - 생성일 기준 10일 이후 수정불가")
    void updatePost_fail_ExceedMaximumEditableDate() {
        //given
        PostUpdateDto.Request request = PostUpdateDto.Request.builder()
                .title("updateTitle")
                .content("updateContent")
                .build();
        User user = User.builder()
                .id(1L)
                .build();
        Post post = Post.builder()
                .id(1L)
                .user(user)
                .title("title")
                .content("content")
                .createdAt(LocalDateTime.now().minusDays(10))
                .build();
        given(postRepository.findById(1L)).willReturn(Optional.of(post));

        //when
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> postService.updatePost(1L, request));

        //then
        assertEquals("생성일 기준 10일 이후 수정 불가", exception.getMessage());
    }
}