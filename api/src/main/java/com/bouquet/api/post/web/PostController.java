package com.bouquet.api.post.web;

import com.bouquet.api.post.dto.Post;
import com.bouquet.api.post.dto.PostRequest;
import com.bouquet.api.post.dto.PostResponse;
import com.bouquet.api.post.exception.PostNotFoundException;
import com.bouquet.api.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api")
public class PostController {

    @Autowired
    private final PostService postService;

    @PostMapping("/post")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<PostResponse.OnlyId> create(@RequestBody PostRequest.Create request) {
        PostResponse.OnlyId response = postService.create(request);
        return ResponseEntity.created(URI.create("/api/post/" + response.getId())).body(response);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<Post> getPost(@PathVariable Long id) {
        return new ResponseEntity<Post>(postService.getPost(id), HttpStatus.OK);
    }

}
