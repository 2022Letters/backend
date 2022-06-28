package com.bouquet.api.post.web;

import com.bouquet.api.post.dto.Post;
import com.bouquet.api.post.dto.PostRequest;
import com.bouquet.api.post.dto.PostResponse;
import com.bouquet.api.post.exception.PostNotFoundException;
import com.bouquet.api.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class PostController {

    private final PostService postService;

    @GetMapping("/requestParameter")
    public String requestParameter(
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
            @RequestParam("requestDateTime") LocalDateTime requestDateTime) {
        System.out.println(requestDateTime);

        return "requestParameter 성공";
    }

    @PostMapping("/post")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<PostResponse.OnlyId> create(@RequestBody PostRequest.Create request) {
        PostResponse.OnlyId response = postService.create(request);
        return ResponseEntity.created(URI.create("/api/post/" + response.getId())).body(response);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<PostResponse.GetPost> getPost(@PathVariable Long postId) {
        PostResponse.GetPost response = postService.getPost(postId);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/posts")
    public ResponseEntity<PostResponse.GetPosts> getPostList
            (@RequestParam(defaultValue = "") String keyword,
             @RequestParam(defaultValue = "1") int mode,
             @RequestParam(defaultValue = "1") int categoryId,
             @RequestParam Long userId) {
        PostResponse.GetPosts response = postService.getPosts(userId, mode, keyword, categoryId);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/post/{postId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ResponseEntity<PostResponse.OnlyId> update(@PathVariable Long postId, @RequestBody PostRequest.Create request) {
        PostResponse.OnlyId response = postService.update(postId, request);
        return ResponseEntity.created(URI.create("/api/post/" + response.getId())).body(response);
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<PostResponse.OnlyId> delete(@PathVariable Long postId) {
        PostResponse.OnlyId response = postService.delete(postId);
        return ResponseEntity.ok().body(response);
    }

}
