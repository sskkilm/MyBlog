package com.sskkilm.myblog.controller;

import com.sskkilm.myblog.dto.PostCreateDto;
import com.sskkilm.myblog.entity.User;
import com.sskkilm.myblog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public PostCreateDto.Response createPost(
            @RequestBody @Valid PostCreateDto.Request request
    ) {
        return postService.createPost(User.builder().build(), request);
    }

}
