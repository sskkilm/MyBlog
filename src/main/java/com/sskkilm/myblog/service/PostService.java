package com.sskkilm.myblog.service;

import com.sskkilm.myblog.dto.PostCreateDto;
import com.sskkilm.myblog.dto.PostUpdateDto;
import com.sskkilm.myblog.entity.Post;
import com.sskkilm.myblog.entity.User;
import com.sskkilm.myblog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PostService {

    private static final String POST_NOT_FOUND_ERROR = "존재하지 않는 게시글";
    private static final String POST_UPDATE_ERROR = "생성일 기준 10일 이후 수정 불가";
    private static final String POST_UPDATE_WARNING_MESSAGE = "하루 후 수정 불가";

    private static final int MAXIMUM_EDITABLE_DATE = 10;
    private static final int WARNING_DATE = 9;

    private final PostRepository postRepository;

    public PostCreateDto.Response createPost(User user, PostCreateDto.Request request) {

        return PostCreateDto.Response.fromEntity(
                postRepository.save(
                        PostCreateDto.Request.toEntity(user, request)
                )
        );
    }

    public PostUpdateDto.Response updatePost(Long postId, PostUpdateDto.Request request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException(POST_NOT_FOUND_ERROR));
        if (post.getCreatedAt().plusDays(MAXIMUM_EDITABLE_DATE).isBefore(LocalDateTime.now())) {
            throw new RuntimeException(POST_UPDATE_ERROR);
        }

        post.update(request.getTitle(), request.getContent());

        PostUpdateDto.Response response = PostUpdateDto.Response.fromEntity(post);
        if (post.getCreatedAt().plusDays(WARNING_DATE).isBefore(LocalDateTime.now())) {
            response.setMessage(POST_UPDATE_WARNING_MESSAGE);
        }

        return response;
    }
}
