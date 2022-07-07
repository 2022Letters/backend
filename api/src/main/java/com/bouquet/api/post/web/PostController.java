package com.bouquet.api.post.web;

import com.bouquet.api.config.NoAuth;
import com.bouquet.api.post.dto.PostRequest;
import com.bouquet.api.post.dto.PostResponse;
import com.bouquet.api.post.service.PostService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class PostController {

    private final PostService postService;

    @ApiOperation(value = "게시글 작성", notes = "userId : 유저 식별자, title: 제목, categoryId: 카테고리 식별자, date: 기념일(yyyy-mm-dd), visibility: 공개여부 값을 입력하여 Post 생성")
    @PostMapping("/post")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<PostResponse.OnlyId> create(@RequestBody PostRequest.Create request) {
        PostResponse.OnlyId response = postService.create(request);
        return ResponseEntity.created(URI.create("/api/post/" + response.getId())).body(response);
    }

    @NoAuth
    @ApiOperation(value = "게시글 상세 조회", notes = "postId : 게시글 식별자를 주소에 작성하여 게시글 하나 조회")
    @GetMapping("/post/{postId}")
    public ResponseEntity<PostResponse.GetPost> getPost(@PathVariable Long postId) {
        PostResponse.GetPost response = postService.getPost(postId);
        return ResponseEntity.ok().body(response);
    }

    @ApiOperation(value = "전체게시글목록 or 제목검색목록 or 카테고리별검색목록 조회", notes = "userId: 유저 식별자, mode: 검색 모드(전체: 1 / 제목: 2 / 카테고리: 3), keyword: 제목, categoryId: 카테고리 식별자 값을 입력하여 목록 조회")
    @GetMapping("/posts")
    public ResponseEntity<PostResponse.GetPosts> getPostList
            (@RequestParam(defaultValue = "") String keyword,
             @RequestParam(defaultValue = "1") int mode,
             @RequestParam(defaultValue = "1") int categoryId,
             @RequestParam Long userId) {
        PostResponse.GetPosts response = postService.getPosts(userId, mode, keyword, categoryId);
        return ResponseEntity.ok().body(response);
    }

    @ApiOperation(value = "게시글 수정", notes = "postId : 게시글 식별자를 주소에 작성하고 userId: 유저 식별자, title: 제목, categoryId: 카테고리 식별자, date: 기념일(yyyy-mm-dd), visibility: 공개여부 값을 입력하여 게시글 수정")
    @PutMapping("/post/{postId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<PostResponse.OnlyId> update(@PathVariable Long postId, @RequestBody PostRequest.Create request) {
        PostResponse.OnlyId response = postService.update(postId, request);
        return ResponseEntity.created(URI.create("/api/post/" + response.getId())).body(response);
    }

    @ApiOperation(value = "게시글 삭제", notes = "postId : 게시글 식별자를 주소에 작성하여 게시글 삭제")
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<PostResponse.OnlyId> delete(@PathVariable Long postId) {
        PostResponse.OnlyId response = postService.delete(postId);
        return ResponseEntity.ok().body(response);
    }

}
