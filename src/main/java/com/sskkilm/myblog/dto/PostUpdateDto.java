package com.sskkilm.myblog.dto;

import com.sskkilm.myblog.entity.Post;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

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
        public static final String POST_UPDATE_SUCCESS = "Update Post Success";
        public static final String POST_UPDATE_WARNING = "하루 후 수정 불가";
        public static final int WARNING_DATE = 9;

        private Long postId;
        private Long userId;
        private String title;
        private String content;
        private String message;

        public static Response fromEntity(Post post) {
            ResponseBuilder builder = Response.builder()
                    .postId(post.getId())
                    .userId(post.getUser().getId())
                    .title(post.getTitle())
                    .content(post.getContent());
            if (post.getCreatedAt().plusDays(WARNING_DATE)
                    .isBefore(LocalDateTime.now())) {
                builder.message(POST_UPDATE_WARNING);
            } else {
                builder.message(POST_UPDATE_SUCCESS);
            }
            return builder.build();
        }
    }
}
