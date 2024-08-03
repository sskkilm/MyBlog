package com.sskkilm.myblog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sskkilm.myblog.dto.PostCreateDto;
import com.sskkilm.myblog.dto.PostUpdateDto;
import com.sskkilm.myblog.entity.User;
import com.sskkilm.myblog.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static com.sskkilm.myblog.dto.PostCreateDto.Response.POST_CREATE_SUCCESS;
import static com.sskkilm.myblog.dto.PostUpdateDto.Response.POST_UPDATE_SUCCESS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
class PostControllerTest {

    @MockBean
    private PostService postService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("게시글 등록 성공")
    void createPost_success() throws Exception {
        //given
        PostCreateDto.Request request = PostCreateDto.Request.builder()
                .title("title")
                .content("content")
                .build();
        given(postService.createPost(any(User.class), any(PostCreateDto.Request.class)))
                .willReturn(
                        PostCreateDto.Response.builder()
                                .postId(1L)
                                .userId(1L)
                                .title("title")
                                .content("content")
                                .message(POST_CREATE_SUCCESS)
                                .build()
                );

        //when
        ResultActions resultActions = mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(1L))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.content").value("content"))
                .andExpect(jsonPath("$.message").value(POST_CREATE_SUCCESS))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 등록 실패 - 제목 공백")
    void createPost_fail_titleBlank() throws Exception {
        //given
        PostCreateDto.Request request = PostCreateDto.Request.builder()
                .title("")
                .content("content")
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 등록 실패 - 제목 200글자 초과")
    void createPost_fail_titleSizeGreaterThan200() throws Exception {
        //given
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 201; i++) {
            sb.append("*");
        }
        String title = sb.toString();

        PostCreateDto.Request request = PostCreateDto.Request.builder()
                .title(title)
                .content("content")
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 등록 실패 - 내용 공백")
    void createPost_fail_contentBlank() throws Exception {
        //given
        PostCreateDto.Request request = PostCreateDto.Request.builder()
                .title("title")
                .content("")
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 등록 실패 - 내용 1000글자 초과")
    void createPost_fail_contentSizeGreaterThan1000() throws Exception {
        //given
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1001; i++) {
            sb.append("*");
        }
        String content = sb.toString();

        PostCreateDto.Request request = PostCreateDto.Request.builder()
                .title("title")
                .content(content)
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(post("/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 수정 성공")
    void updatePost_success() throws Exception {
        //given
        PostUpdateDto.Request request = PostUpdateDto.Request.builder()
                .title("title")
                .content("content")
                .build();
        given(postService.updatePost(anyLong(), any(PostUpdateDto.Request.class)))
                .willReturn(PostUpdateDto.Response.builder()
                        .postId(1L)
                        .userId(1L)
                        .title("title")
                        .content("content")
                        .message(POST_UPDATE_SUCCESS)
                        .build());

        //when
        ResultActions resultActions = mockMvc.perform(put("/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.postId").value(1L))
                .andExpect(jsonPath("$.userId").value(1L))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.content").value("content"))
                .andExpect(jsonPath("$.message").value(POST_UPDATE_SUCCESS))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 수정 실패 - 제목 공백")
    void updatePost_fail_titleBlank() throws Exception {
        //given
        PostUpdateDto.Request request = PostUpdateDto.Request.builder()
                .title("")
                .content("content")
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(put("/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 수정 실패 - 제목 200글자 초과")
    void updatePost_fail_titleSizeGreaterThan200() throws Exception {
        //given
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 201; i++) {
            sb.append("*");
        }
        String title = sb.toString();

        PostUpdateDto.Request request = PostUpdateDto.Request.builder()
                .title(title)
                .content("content")
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(put("/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 수정 실패 - 내용 공백")
    void updatePost_fail_contentBlank() throws Exception {
        //given
        PostUpdateDto.Request request = PostUpdateDto.Request.builder()
                .title("title")
                .content("")
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(put("/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 수정 실패 - 내용 1000글자 초과")
    void updatePost_fail_contentSizeGreaterThan1000() throws Exception {
        //given
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1001; i++) {
            sb.append("*");
        }
        String content = sb.toString();

        PostUpdateDto.Request request = PostUpdateDto.Request.builder()
                .title("title")
                .content(content)
                .build();

        //when
        ResultActions resultActions = mockMvc.perform(put("/posts/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        //then
        resultActions.andExpect(status().isBadRequest())
                .andDo(print());
    }
}