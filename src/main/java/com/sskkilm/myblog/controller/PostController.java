package com.sskkilm.myblog.controller;

import com.sskkilm.myblog.dto.PostCreateDto;
import com.sskkilm.myblog.dto.PostUpdateDto;
import com.sskkilm.myblog.entity.User;
import com.sskkilm.myblog.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/{id}")
    public PostUpdateDto.Response updatePost(
            @PathVariable Long id,
            @RequestBody @Valid PostUpdateDto.Request request
    ) {
        return postService.updatePost(id, request);
    }
}
