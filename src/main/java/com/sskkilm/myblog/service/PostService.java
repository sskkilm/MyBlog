package com.sskkilm.myblog.service;

import com.sskkilm.myblog.dto.PostCreateDto;
import com.sskkilm.myblog.entity.User;
import com.sskkilm.myblog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public PostCreateDto.Response createPost(User user, PostCreateDto.Request request) {

        return PostCreateDto.Response.fromEntity(
                postRepository.save(
                        PostCreateDto.Request.toEntity(user, request)
                )
        );
    }
}
