package com.sskkilm.myblog.dto;

import com.sskkilm.myblog.entity.Post;
import com.sskkilm.myblog.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

public class PostCreateDto {

    @Getter
    @Builder
    public static class Request {

        @NotBlank(message = "제목은 공백일 수 없습니다.")
        @Size(max = 200, message = "제목은 200글자를 초과할 수 없습니다.")
        private String title;

        @NotBlank(message = "내용은 공백일 수 없습니다.")
        @Size(max = 1000, message = "내용은 1000글자를 초과할 수 없습니다.")
        private String content;

        public static Post toEntity(User user, Request request) {
            return Post.builder()
                    .title(request.getTitle())
                    .content(request.getContent())
                    .user(user)
                    .build();
        }

    }

    @Getter
    @Builder
    public static class Response {
        public static final String POST_CREATE_SUCCESS = "Create Post Success";

        private Long postId;
        private Long userId;
        private String title;
        private String content;
        private String message;

        public static Response fromEntity(Post post) {
            return Response.builder()
                    .postId(post.getId())
                    .userId(post.getUser().getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .message(POST_CREATE_SUCCESS)
                    .build();
        }
    }
}
