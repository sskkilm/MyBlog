package com.sskkilm.myblog.dto;

import com.sskkilm.myblog.entity.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class PostUpdateDto {

    @Getter
    @Builder
    public static class Request {

        @NotBlank(message = "제목은 공백일 수 없습니다.")
        @Size(max = 200, message = "제목은 200글자를 초과할 수 없습니다.")
        private String title;

        @NotBlank(message = "내용은 공백일 수 없습니다.")
        @Size(max = 1000, message = "내용은 1000글자를 초과할 수 없습니다.")
        private String content;

    }

    @Getter
    @Builder
    public static class Response {
        private static final String POST_UPDATE_SUCCESS = "Update Post Success";

        private Long postId;
        private Long userId;
        private String title;
        private String content;
        @Setter
        private String message;

        public static Response fromEntity(Post post) {
            return Response.builder()
                    .postId(post.getId())
                    .userId(post.getUser().getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .message(POST_UPDATE_SUCCESS)
                    .build();
        }
    }
}
