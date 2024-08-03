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

    public static final int MAXIMUM_EDITABLE_DATE = 10;

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
                .orElseThrow(() -> new RuntimeException("Post not found -> " + postId));
        if (post.getCreatedAt().plusDays(MAXIMUM_EDITABLE_DATE).isBefore(LocalDateTime.now())) {
            throw new RuntimeException("생성일 기준 10일 이후 수정불가");
        }

        post.update(request.getTitle(), request.getContent());

        return PostUpdateDto.Response.fromEntity(post);
    }
}
