package com.bouquet.api.post.service;

import com.bouquet.api.post.dto.Post;
import com.bouquet.api.post.dto.PostRequest;
import com.bouquet.api.post.dto.PostResponse;
import com.bouquet.api.post.exception.PostNotFoundException;
import com.bouquet.api.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {

    @Autowired
    private final PostRepository postRepository;

    @Transactional
    public PostResponse.OnlyId create(PostRequest.Create request) {
        Post post = Post.create(request);
        Post savedPost = postRepository.save(post);
        return PostResponse.OnlyId.build(savedPost);
    }

    public PostResponse.GetPost getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);

        return PostResponse.GetPost.build(post);
    }

    @Transactional
    public PostResponse.OnlyId delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        // 유저 확인 필요
        postRepository.deleteById(id);
        return PostResponse.OnlyId.build(post);
    }

}
